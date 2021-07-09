package com.kerriline.location;

import com.kerriline.location.domain.Tank;
import com.kerriline.location.repository.TankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kerriline.location.data.Mileage;
import com.kerriline.location.mail.MailManager;
import com.kerriline.location.mail.MessageBean;
import com.kerriline.location.mail.MileageParser;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Aleksey
 *
 */
@Component
public class MileageManager {

	private static final Logger LOG = LoggerFactory.getLogger(MileageManager.class);

	private static final String REQUEST_NUMBER = "2612";

	@Autowired private MailManager mail;
	@Autowired private MileageParser parser;

	/**
	 * @throws IOException
	 * @throws GeneralSecurityException
	 *
	 */
	public void fullTrip() throws GeneralSecurityException, IOException {
		int retry = 5;

		List<Tank> tanks = readTanks()
				.stream()
				//.limit(20)
				.collect(Collectors.toList());

		List<MessageBean> responses = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.MINUTE, -30);
		Date skipBeforeDate = cal.getTime();

		List<Tank> absentTanks = findAbsentTanks(tanks, responses);
		while(!absentTanks.isEmpty() || 0 == retry--) {
			//skipBeforeDate =
			sendRequest(absentTanks);
			waitForResponse(3 + (absentTanks.size()/30));//minutes
			responses.addAll(readResponses(skipBeforeDate));
			absentTanks = findAbsentTanks(tanks, responses);
			LOG.info("Absent " + absentTanks.size() + " tanks");

			// waiting for another mails if they come
			int preSize = 0;
			while(preSize != absentTanks.size()){
				preSize = absentTanks.size();
				waitForResponse(1);//minutes
				responses.addAll(readResponses(skipBeforeDate));
				absentTanks = findAbsentTanks(tanks, responses);
				LOG.info("Absent " + absentTanks.size() + " tanks");
			}

		}
		LOG.info("Writing "+ (tanks.size() - absentTanks.size()) +" tanks. Total: " + tanks.size());
		LOG.warn("Missed tanks: " + absentTanks);
		List<Mileage> mileages = parseResponse(tanks, responses);
		writeMileage(mileages);
		LOG.info("Write completed");
		File report = exportReport();
		sendFile(report);
	}

	private void writeMileage(List<Mileage> mileages) throws IOException, GeneralSecurityException {
		//mileageWriter.writeAll(mileages);
	}

	private List<Mileage> parseResponse(List<Tank> tanks, List<MessageBean> responses) {
		List<String> tanksList = tanks.parallelStream()
			.map(Tank::getTankNumber)
			.collect(Collectors.toList());

		List<Mileage> mileages = responses.parallelStream()
			.distinct()
			.map((m) -> parser.parse(m.getSubject(), m.getBody()))
			.filter((m) -> tanksList.contains(m.getTankNumber()))
			.collect(Collectors.toList());

		return mileages;
	}

	private List<MessageBean> readResponses(Date skipBeforeDate) {
		return mail.searchMessages(REQUEST_NUMBER, skipBeforeDate);
	}

	/**
	 *
	 * @param tanks
	 * @param responses
	 * @return absent tanks in responses
	 */
	private List<Tank> findAbsentTanks(List<Tank> tanks, List<MessageBean> responses) {
		List<String> list = responses.stream()
			.map((m) -> m.getSubject().split("-")[1])
			.distinct()
			.collect(Collectors.toList());

		List<Tank> absentTanks = tanks.parallelStream()
			.filter((t) -> !list.contains(t.getTankNumber()))
			.collect(Collectors.toList());
		return absentTanks;
	}

	/**
	 * TODO: replace explicit wait with
	 */
	private void waitForResponse(int minutes) {
		try {
			LOG.info("Waiting {} minutes for response", minutes);
			Thread.sleep(minutes*60*1000);
			LOG.info("Finished waiting");
		} catch (InterruptedException e) {
			LOG.error("Failed to make explicit wait", e);
			throw new RuntimeException(e);
		}
	}

	private Date sendRequest(List<Tank> tanks) throws IOException {
		StringBuilder sb = new StringBuilder();
		tanks.stream()
			.forEach(t -> sb.append(t.getTankNumber()).append("\n"));
		mail.sendMail(REQUEST_NUMBER, sb.toString());
		return new Date();
	}

	@Autowired
    TankRepository tankRepository;

	private List<Tank> readTanks() throws GeneralSecurityException, IOException {
		LOG.info("Reading tanks");

        return tankRepository.findAll();
	}

	private File exportReport() {
		return null;
	}

	private void sendFile(File report) {
		// TODO Auto-generated method stub

	}
}
