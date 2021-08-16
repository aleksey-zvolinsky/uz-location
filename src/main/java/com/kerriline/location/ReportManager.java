package com.kerriline.location;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kerriline.location.domain.LocationResponse;
import com.kerriline.location.domain.Tank;
import com.kerriline.location.mail.MailManager;
import com.kerriline.location.mail.MailParser;
import com.kerriline.location.repository.LocationResponseRepository;
import com.kerriline.location.repository.TankRepository;

/**
 *
 * @author Aleksey
 *
 */
@Component
public class ReportManager {

	private static final Logger LOG = LoggerFactory.getLogger(ReportManager.class);

	@Autowired
    MailManager mail;
	@Autowired
    MailParser source;


	@Value("${application.result-mail.to}")
	private String mailTo = "service@kerriline.com.ua";

	@Autowired
    TankRepository tankRepository;

	@Autowired
    LocationResponseRepository locationResponseRepository;
	
	public File generateReport() throws GeneralSecurityException, IOException, MessagingException {

		
	    var template = Thread.currentThread().getContextClassLoader().getResource("template.xlsx");
		
		String reportFile = Files.createTempFile("uz-location", ".xlsx").toString();
		
		// Do we need to copy file?
		try (FileOutputStream fileOutputStream = new FileOutputStream(reportFile)) {

		    try {
				Files.copy(Path.of(template.toURI()), fileOutputStream);
			} catch (IOException | URISyntaxException e) {
				throw new RuntimeException("Failed to copy template file");
			}

		}
		
		
		try(Workbook wb = WorkbookFactory.create(new File(reportFile))) {
			
			
			Sheet tanksSheet = wb.getSheet("Вагоны");
			
			
			List<Tank> tanks = tankRepository.findAll();
			
			int rownum = 2;
			for (Tank tank : tanks) {
				Row row = tanksSheet.createRow(rownum++);
				int cellnum = 0;
				row.createCell(cellnum++).setCellValue(rownum-2);
				row.createCell(cellnum++).setCellValue(tank.getClientName());
				row.createCell(cellnum++).setCellValue(tank.getOwnerName());
				row.createCell(cellnum++).setCellValue(tank.getTankNumber());
			}
			
			Map<String, Tank> tanksMap = tanks.stream()
				.filter(distinctByKey(t -> t.getTankNumber()))
				.collect(Collectors.toMap(Tank::getTankNumber, tank -> tank));
			
			fillResults(wb, tanksMap);

			String finalReportFile = Files.createTempFile("uz-location", ".xlsx").toString();
			
	        LOG.info("Storing file to {}", finalReportFile);
	        
	        wb.setForceFormulaRecalculation(true);
	        
	        try(FileOutputStream out = new FileOutputStream(finalReportFile)) {
	        	wb.write(out);
	        }
	        
	        return new File(finalReportFile);
		}
		
	}
	
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


	private void fillResults(Workbook wb, Map<String, Tank> tanks) {
		
		LocalDate cutDate = LocalDate.now().minusDays(3);
		List<LocationResponse> responses = locationResponseRepository.findAllWithResposeDateTimeAfter(cutDate );
		
		Sheet sh = wb.getSheet("Результат");
		
		int rownum = 3;
		for (LocationResponse response : responses) {
				if(!tanks.containsKey(response.getTankNumber())) {
					continue;
				}	
				
				Tank tank = tanks.get(response.getTankNumber());
				
				LOG.debug("{}", response.getResponseDatetime());
				
				Row row = sh.createRow(rownum++);
				int cellnum = 0;
				row.createCell(cellnum++)
					.setCellValue(rownum-3);
				row.createCell(cellnum++)
					.setCellValue(tank.getClientName());
				row.createCell(cellnum++)
					.setCellValue(tank.getOwnerName());
				row.createCell(cellnum++)
					.setCellValue(response.getTankNumber());
				row.createCell(cellnum++)
					.setCellValue(response.getTankType());
				row.createCell(cellnum++)
					.setCellValue(response.getCargoId());
				row.createCell(cellnum++)
					.setCellValue(response.getCargoName());
				row.createCell(cellnum++)
					.setCellValue(response.getWeight());
				row.createCell(cellnum++)
					.setCellValue(response.getReceiverId());
				row.createCell(cellnum++)
					.setCellValue(response.getTankIndex());
		        row.createCell(cellnum++)
		        	.setCellValue(response.getLocationStationId());
		        row.createCell(cellnum++)
		            .setCellValue(response.getLocationStationName());
		        row.createCell(cellnum++)
		            .setCellValue(response.getLocationDatetime());
		        row.createCell(cellnum++)
		            .setCellValue(response.getLocationOperation());
		        row.createCell(cellnum++)
		            .setCellValue(response.getStateToStationId());
		        row.createCell(cellnum++)
		            .setCellValue(response.getStateToStationName());
		        row.createCell(cellnum++)
		            .setCellValue(response.getStateFromStationId());
		        row.createCell(cellnum++)
		            .setCellValue(response.getStateFromStationName());
		        row.createCell(cellnum++)
		            .setCellValue(response.getStateSendDatetime());
		        row.createCell(cellnum++)
		            .setCellValue(response.getStateSenderId());
		        row.createCell(cellnum++)
		            .setCellValue(response.getPlanedServiceDatetime());
		        //skipping mileage fields
		        cellnum = cellnum+4;
		        row.createCell(cellnum++)
		            .setCellValue(response.getTankOwner());
		        row.createCell(cellnum++)
		            .setCellValue(response.getTankModel());
		        row.createCell(cellnum++)
		            .setCellValue(response.getDefectRegion());
		        row.createCell(cellnum++)
		            .setCellValue(response.getDefectStation());
		        row.createCell(cellnum++)
		            .setCellValue(response.getDefectDatetime());
		        row.createCell(cellnum++)
		            .setCellValue(response.getDefectDetails());
		        row.createCell(cellnum++)
		            .setCellValue(response.getRepairRegion());
		        row.createCell(cellnum++)
		            .setCellValue(response.getRepairStation());
		        row.createCell(cellnum++)
		            .setCellValue(response.getRepairDatetime());
		        row.createCell(cellnum++)
		            .setCellValue(response.getUpdateDatetime());
		        // taking just first occurrence
		        tanks.remove(response.getTankNumber());
		}
		
		for (Tank tank: tanks.values()) {
			Row row = sh.createRow(rownum++);
			int cellnum = 0;
			row.createCell(cellnum++)
				.setCellValue(rownum-3);
			row.createCell(cellnum++)
				.setCellValue(tank.getClientName());
			row.createCell(cellnum++)
				.setCellValue(tank.getOwnerName());
			row.createCell(cellnum++)
				.setCellValue(tank.getTankNumber());
		}
		
		
	}

	
	public void exportAndSend() throws GeneralSecurityException, IOException, MessagingException {
		//File file = drive.export();
		//mail.springSendFile(file, mailTo);
		//mail.springSendFile(file, "frendos.a@gmail.com");
	}
}
