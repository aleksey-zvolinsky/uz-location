package com.kerriline.location;

import com.kerriline.location.domain.LocationResponse;
import com.kerriline.location.domain.Tank;
import com.kerriline.location.repository.KerrilineLocationResponseRepository;
import com.kerriline.location.repository.TankRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Aleksey
 *
 */
@Component
public class ReportManager {

    private static final Logger LOG = LoggerFactory.getLogger(ReportManager.class);

    @Autowired
    TankRepository tankRepository;

    @Autowired
    KerrilineLocationResponseRepository locationResponseRepository;

    public File generateReport() throws IOException {
        InputStream templateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("template.xlsx");

        String reportFile = Files.createTempFile("uz-location", ".xlsx").toString();

        assert templateStream != null;
        Files.copy(templateStream, Paths.get(reportFile), StandardCopyOption.REPLACE_EXISTING);

        try (Workbook wb = WorkbookFactory.create(new File(reportFile))) {
            Sheet tanksSheet = wb.getSheet("Вагоны");

            List<Tank> tanks = tankRepository.findAll();

            int rownum = 2;
            for (Tank tank : tanks) {
                Row row = tanksSheet.createRow(rownum++);
                int cellnum = 0;
                row.createCell(cellnum++).setCellValue(rownum - 2);
                row.createCell(cellnum++).setCellValue(tank.getClientName());
                row.createCell(cellnum++).setCellValue(tank.getOwnerName());
                row.createCell(cellnum).setCellValue(tank.getTankNumber());
            }

            Map<String, Tank> tanksMap = tanks
                .stream()
                .filter(distinctByKey(Tank::getTankNumber))
                .collect(Collectors.toMap(Tank::getTankNumber, tank -> tank));

            ZonedDateTime offset = ZonedDateTime.now().minusDays(7);

            printToExcel(wb, "Результат", locationResponseRepository.findByResponseDatetimeAfter(offset), tanksMap, true);

            List<String> tankTypes = Arrays.asList("60 ПОЛУВАГОНЫ", "68 ГЛУХОДОННЫЕ");

            printToExcel(
                wb,
                "Полувагоны",
                locationResponseRepository.findByTankTypeInAndResponseDatetimeAfter(tankTypes, offset),
                tanksMap,
                false
            );

            printToExcel(
                wb,
                "Не полувагоны",
                locationResponseRepository.findByTankTypeNotInAndResponseDatetimeAfter(tankTypes, offset),
                tanksMap,
                false
            );

            String finalReportFile = Files.createTempFile("uz-location", ".xlsx").toString();

            LOG.info("Storing file to {}", finalReportFile);

            wb.setForceFormulaRecalculation(true);

            try (FileOutputStream out = new FileOutputStream(finalReportFile)) {
                wb.write(out);
            }

            return new File(finalReportFile);
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private void printToExcel(
        Workbook wb,
        String sheetName,
        List<LocationResponse> responses,
        Map<String, Tank> originalTanks,
        boolean addEmptyTanks
    ) {
        Map<String, Tank> tanks = new HashMap<>(originalTanks);

        Sheet sh = wb.getSheet(sheetName);

        int rownum = 3;
        for (LocationResponse response : responses) {
            if (!tanks.containsKey(response.getTankNumber())) {
                continue;
            }

            Tank tank = tanks.get(response.getTankNumber());

            Row row = sh.createRow(rownum++);
            int cellnum = 0;
            row.createCell(cellnum++).setCellValue(rownum - 3);
            row.createCell(cellnum++).setCellValue(tank.getClientName());
            row.createCell(cellnum++).setCellValue(tank.getOwnerName());
            row.createCell(cellnum++).setCellValue(response.getTankNumber());
            row.createCell(cellnum++).setCellValue(response.getTankType());
            row.createCell(cellnum++).setCellValue(response.getCargoId());
            row.createCell(cellnum++).setCellValue(response.getCargoName());
            row.createCell(cellnum++).setCellValue(response.getWeight());
            row.createCell(cellnum++).setCellValue(response.getReceiverId());
            row.createCell(cellnum++).setCellValue(response.getTankIndex());
            row.createCell(cellnum++).setCellValue(response.getLocationStationId());
            row.createCell(cellnum++).setCellValue(response.getLocationStationName());
            row.createCell(cellnum++).setCellValue(response.getLocationDatetime());
            row.createCell(cellnum++).setCellValue(response.getLocationOperation());
            row.createCell(cellnum++).setCellValue(response.getStateToStationId());
            row.createCell(cellnum++).setCellValue(response.getStateToStationName());
            row.createCell(cellnum++).setCellValue(response.getStateFromStationId());
            row.createCell(cellnum++).setCellValue(response.getStateFromStationName());
            row.createCell(cellnum++).setCellValue(response.getStateSendDatetime());
            row.createCell(cellnum++).setCellValue(response.getStateSenderId());
            row.createCell(cellnum++).setCellValue(response.getPlanedServiceDatetime());
            row.createCell(cellnum++).setCellValue(response.getTankOwner());
            row.createCell(cellnum++).setCellValue(response.getTankModel());
            row.createCell(cellnum++).setCellValue(response.getDefectRegion());
            row.createCell(cellnum++).setCellValue(response.getDefectStation());
            row.createCell(cellnum++).setCellValue(response.getDefectDatetime());
            row.createCell(cellnum++).setCellValue(response.getDefectDetails());
            row.createCell(cellnum++).setCellValue(response.getRepairRegion());
            row.createCell(cellnum++).setCellValue(response.getRepairStation());
            row.createCell(cellnum++).setCellValue(response.getRepairDatetime());
            row.createCell(cellnum).setCellValue(response.getUpdateDatetime());
            // taking just first occurrence
            tanks.remove(response.getTankNumber());
        }

        if (addEmptyTanks) {
            for (Tank tank : tanks.values()) {
                Row row = sh.createRow(rownum++);
                int cellnum = 0;
                row.createCell(cellnum++).setCellValue(rownum - 3);
                row.createCell(cellnum++).setCellValue(tank.getClientName());
                row.createCell(cellnum++).setCellValue(tank.getOwnerName());
                row.createCell(cellnum).setCellValue(tank.getTankNumber());
            }
        }
    }
}
