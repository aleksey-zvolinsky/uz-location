package com.kerriline.location.mail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.kerriline.location.domain.LocationResponse;

/**
 *
 * @author Aleksey
 *
 */
public class MailParser {

	private static final Logger LOG = LoggerFactory.getLogger(MailParser.class);

	private static final String UPDATE_FIELD = "ОБНОВЛЕНО";
	private static final String TANKS_SEPARATOR = "ДАННЫЕ";

	private static final String[] TO_DELETE = {"-- ИЗ ПОД ----------  ", "== РЕМОНТЫ     ==   : ", "== БЛОК РЕЙСА =====   ", "== ОПИСАНИЕ РЕЙСА ==  "};

	private List<String> duplicates = Arrays.asList("СТАНЦИЯ ОПЕРАЦИИ", "ДАТА ОПЕРАЦИИ", "СТАНЦИЯ НАЗНАЧЕНИЯ", "ГРУЗОПОЛУЧАТЕЛЬ", "ГРУЗ", "ГРУЗООТПРАВИТЕЛЬ",
			"ОПРЕРАЦИЯ", "ДОРОГА ПРИЕМА ГРУЗА", "СТАНЦИЯ", "ВИД РЕМОНТА", "ДОРОГА", "ВЧД", "ГРУЗОПОДЪЕМНОСТЬ");

	private List<String> idNameFields = Arrays.asList("КОД ГРУЗА", "СТАНЦИЯ НАЗНАЧЕНИЯ-0", "СТАНЦИЯ ОПЕРАЦИИ-0", "СТАНЦИЯ ПРИЕМА ГРУЗА");


	private static final MapSplitter MAP_SPLITTER = Splitter.on('\n')
			.trimResults()
			.omitEmptyStrings()
			.withKeyValueSeparator(Splitter.on(":").trimResults());
	private static final Splitter SPLIT_BY_TANKS = Splitter.on("++++++++++++").omitEmptyStrings();
	private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
	private DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

	public List<LocationResponse> text2table(final MessageBean messageBean) {
		//LOG.debug(messageBean.getBody());

		List<LocationResponse> res = new ArrayList<>();
		int processedTanks = 0;
		try {
			LOG.info("Preparing mail text");
			String text = prepareText(messageBean.getBody());
			LOG.info("Start processing tanks");
			for(String tankPart: SPLIT_BY_TANKS.split(text)){
				LOG.debug("Processing {} tank", ++processedTanks);
				String preFilteredTankPart = preFilter(tankPart);
				LOG.debug(preFilteredTankPart);

				// split rows into 
				Map<String, String> tankFields = MAP_SPLITTER.split(preFilteredTankPart);
				Map<String, String> postFilteredTankFields = postFilter(tankFields);
				postFilteredTankFields.put(UPDATE_FIELD, formatter.format(messageBean.getReceivedDate()));
                LocationResponse locationResponse = convertTableToLocationResponse(postFilteredTankFields);
				res.add(locationResponse);
			}
			LOG.info("Processed {} tanks", processedTanks);
		} catch (IllegalArgumentException e) {

			if(e.getMessage().contains("Duplicate key")) {
				duplicates.add(e.getMessage().substring(e.getMessage().indexOf("[")+1, e.getMessage().indexOf("]")));
				LOG.debug(e.getMessage());
				text2table(messageBean);
			} else {
				throw e;
			}
		} catch (Exception e) {
			LOG.error("Failed on {} tank", processedTanks, e);
			throw e;
		}

		return res;
	}

	private String prepareText(String body) {
		String res = body.replace(TANKS_SEPARATOR, "++++++++++++" + TANKS_SEPARATOR);
		for(String what: TO_DELETE) {
			res = res.replace(what, "");
		}

		StringBuilder b = new StringBuilder();
		int newPos = 0;
		int lastPos = 0;

		while((newPos = res.indexOf("ПОСЛЕДНИИ ОПЕРАЦИИ", lastPos)) != -1) {
			
			b.append(res.substring(lastPos, newPos));
			
			lastPos = newPos;
			newPos = res.indexOf(":", newPos)+1;
			b.append(res.substring(lastPos, newPos));
			

			lastPos = res.indexOf("КОД ГРУЗА", newPos)-2;
			b.append(res.substring(newPos, lastPos)
					.replace("\r\n", ";").replace(":", ","));
		}
		b.append(res.substring(lastPos));

		return b.toString();
	}

	private Map<String, String> postFilter(Map<String, String> map) {
		Map<String, String> newMap = new HashMap<>();
		newMap.putAll(map);
		splitFieldsWithId(newMap);
		return newMap;
	}

	private void splitFieldsWithId(Map<String, String> map) {
		for(String toSplitField: idNameFields) {
			String fullValue = map.get(toSplitField);
			int pos;
			if(fullValue == null || (pos = fullValue.indexOf(" ")) == -1) {
				continue;
			}
			String id = fullValue.substring(0, pos);
			String name = fullValue.substring(pos+1);

			map.put(toSplitField + "-ID", id);
			map.put(toSplitField + "-NAME", name);
		}
	}

	/**
	 * 
	 * @param block
	 * @return duplicated fields will have numbers and tank number just numbers
	 */
	private String preFilter(final String block) {
		String s = block;
		s = processDuplicates(s);
		s = processTankNumbers(s);
		return s;
	}

	/**
	 * ДАННЫЕ О ВАГОНЕ : 58999913 (СОБСТВЕННЫЙ) ->  ДАННЫЕ О ВАГОНЕ : 58999913
	 * @param block
	 * @return
	 */
	private String processTankNumbers(final String block) {
		String tank = block.substring(0,block.indexOf("\n"));
		String res = tank.substring(0, block.indexOf("(")) + block.substring(block.indexOf("\n")) ;
		return res;
	}

	/**
	 * Add numbers to repeating columns
	 *
	 * @param block
	 * @return
	 */
	private String processDuplicates(final String block) {
		StringBuilder sb = new StringBuilder(block);
		String res = block;

		for(String duplicate: duplicates) {
			sb.setLength(0);
			int i = 0;
			for(String line : Splitter.on("\n").split(res)) {
				
				List<String> val = Splitter.on(":").trimResults().splitToList(line);
				
				if(duplicate.equals(val.get(0))) {
					sb.append(duplicate).append("-").append(i++).append(":").append(val.get(1));
				} else {
					sb.append(line);
				}
				sb.append("\n");
			}
			res = sb.toString();
		}
		return res;
	}

	public List<Map<String, String>> merge(List<Map<String, String>> tanks, List<Map<String, String>> rawData) {
		String tankKey = "вагон";
		String dataKey = "ДАННЫЕ О ВАГОНЕ";
		List<Map<String, String>> result = new ArrayList<>();
		for (Map<String, String> tank : tanks) {
			String value = tank.get(tankKey);
			for(Map<String, String> rec : rawData) {
				if(value.equals(rec.get(dataKey))) {
					Map<String, String> merged = new HashMap<>();
					merged.putAll(tank);
					merged.putAll(rec);
					result.add(merged);
					break;
				}
			}
		}
		return result;
	}

	public List<String> getObsoleteTanks(List<Map<String, String>> tanks, List<Map<String, String>> oldTanks, String existTankKey) {
		String tankKey = "вагон";
		List<String> result = new ArrayList<>();
		List<Map<String, String>> existTanks = new ArrayList<>();

		for(Map<String, String> rec : existTanks) {
			result.add(rec.get(existTankKey));
		}

		existTanks.addAll(oldTanks);
		for (Map<String, String> tank : tanks) {
			String value = tank.get(tankKey);
			result.remove(value);
		}
		return result;
	}
	
	public static String ifEmpty(String... strings) {
		for(String string: strings) {
			if(null != string && !string.isBlank()) {
				return string;
			}
		}
		return "";
	}

	public LocationResponse convertTableToLocationResponse(Map<String, String> tankFields) {
        LocationResponse response = new LocationResponse();
        
        response.setResponseDatetime(ZonedDateTime.now());
        
        response.setTankNumber(tankFields.get("ДАННЫЕ О ВАГОНЕ"));
        
        response.setTankType(tankFields.get("РОД ВАГОНА"));
        response.setCargoId(tankFields.get("КОД ГРУЗА-ID"));
        response.setCargoName(tankFields.get("КОД ГРУЗА-NAME"));

        response.setWeight(tankFields.get("ВЕС"));
        response.setReceiverId(tankFields.get("ГРУЗОПОЛУЧАТЕЛЬ-0"));
        response.setTankIndex(tankFields.get("ПОЕЗД"));
        if (Strings.isBlank(response.getTankIndex())) {
        	response.setTankIndex(getIndexFromOperations(tankFields.get("ПОСЛЕДНИИ ОПЕРАЦИИ")));
        }
        response.setLocationStationId(tankFields.get("СТАНЦИЯ ОПЕРАЦИИ-0-ID"));
        response.setLocationStationName(tankFields.get("СТАНЦИЯ ОПЕРАЦИИ-0-NAME"));
        response.setLocationDatetime(tankFields.get("ДАТА ОПЕРАЦИИ-0"));
        response.setLocationOperation(tankFields.get("ОПЕРАЦИЯ"));
        
        response.setStateToStationId(tankFields.get("СТАНЦИЯ НАЗНАЧЕНИЯ-0-ID"));
        if (Strings.isBlank(response.getStateToStationId())) {
        	response.setStateToStationId(tankFields.get("СТАНЦИЯ ОПЕРАЦИИ-0-ID"));
        }
        
        response.setStateToStationName(tankFields.get("СТАНЦИЯ НАЗНАЧЕНИЯ-0-NAME"));
        if (Strings.isBlank(response.getStateToStationName())) {
        	response.setStateToStationName(tankFields.get("СТАНЦИЯ ОПЕРАЦИИ-0-NAME"));
        }
        
        response.setStateFromStationId(tankFields.get("СТАНЦИЯ ПРИЕМА ГРУЗА-ID"));
        response.setStateFromStationName(tankFields.get("СТАНЦИЯ ПРИЕМА ГРУЗА-NAME"));
        response.setStateSendDatetime(tankFields.get("ДАТА ПРИЕМА ГРУЗА"));
        response.setStateSenderId(tankFields.get("ГРУЗООТПРАВИТЕЛЬ-0"));
        response.setPlanedServiceDatetime(tankFields.get("ДАТА ПЛАН. РЕМОНТА"));
        //response.setMileageCurrent(tankFields.get("ПРОБЕГ-ТЕКУЩИЙ"));
        //response.setMileageDatetime(tankFields.get("ПРОБЕГ-ДАТА"));
        //response.setMileageRemain(tankFields.get("ПРОБЕГ-ОСТАЛОСЬ"));
        //response.setMileageUpdateDatetime(tankFields.get("ПРОБЕГ-ОБНОВЛЕННО"));
        response.setTankOwner(tankFields.get("ПPEДПPИЯTИE"));
        response.setTankModel(tankFields.get("МОДЕЛЬ ВАГОНА"));
        response.setDefectRegion(tankFields.get("ДОРОГА-0"));
        response.setDefectStation(tankFields.get("СТАНЦИЯ-1"));
        response.setDefectDatetime(tankFields.get("ДАТА ПЕРЕВОДА В НРП"));
        response.setDefectDetails(tankFields.get("НЕИСПРАВНОСТЬ"));
        response.setRepairRegion(tankFields.get("ДОРОГА-1"));
        response.setRepairStation(tankFields.get("СТАНЦИЯ-2"));
        response.setRepairDatetime(tankFields.get("ДАТА ПЕРЕВОДА В РП"));
        response.setUpdateDatetime(tankFields.get("ОБНОВЛЕНО"));
        return response;
    }

	private String getIndexFromOperations(String rawOperations) {
		try {
			String[] operations = rawOperations.split(";");
			String[] operationsFields = operations[2].split(",");
			return operationsFields[1];
		} catch (Exception e) {
			LOG.warn("Failed to get TankIndex from Operations using {}", rawOperations, e);
		}
		return "";
	}
}

