package com.kerriline.location;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.kerriline.location.domain.LocationResponse;
import com.kerriline.location.domain.Tank;
import com.kerriline.location.mail.MailManager;
import com.kerriline.location.mail.MailParser;
import com.kerriline.location.mail.MessageBean;
import com.kerriline.location.repository.LocationResponseRepository;
import com.kerriline.location.repository.TankRepository;

/**
 *
 * @author Aleksey
 *
 */
public class LocationManager {

	private static final String REQUEST_NUMBER = "1392";

	private static final Logger LOG = LoggerFactory.getLogger(LocationManager.class);

	@Autowired
    MailManager mail;
	@Autowired
    MailParser source;


	@Value("${application.result-mail.to}")
	private String mailTo = "service@kerriline.com.ua";

	@Autowired
    TankRepository tankRepository;

	/**
	 * send requests on email
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 * @throws InterruptedException
	 */
	public int sendLocationRequest() throws GeneralSecurityException, IOException, InterruptedException {
		int mailCount = 0;
		LOG.info("Reading tanks");

        List<Tank> tanks = tankRepository.findAll();
		StringBuilder text = new StringBuilder();
		int i = 0;
		for (Tank tank : tanks) {
			i++;
			text.append(tank.getTankNumber()).append("\n");
			if(i >= 150) {
				LOG.info("Sending mail");
				mail.submitRequest(REQUEST_NUMBER, text.toString());
				mailCount++;
				Thread.sleep(5000);
				text.setLength(0);
				i = 0;
			}
		}
		LOG.info("Sending mail");
		mail.submitRequest(REQUEST_NUMBER, text.toString());
		mailCount++;
		return mailCount;
	}

	@Autowired
    LocationResponseRepository locationResponseRepository;

	public void saveLocationResponses() {
		LOG.info("Reading mails");
		List<MessageBean> messages = mail.search1392Messages();
		Collections.reverse(messages);
		for (MessageBean messageBean : messages) {
			List<LocationResponse> responses = source.text2table(messageBean);
			LOG.info("Writing data");
            locationResponseRepository.saveAll(responses);
		}
		LOG.info("Sheet updated");
	}
	
	@Autowired
	ReportManager reportManager;

	public void fullTrip() throws GeneralSecurityException, IOException, InterruptedException, MessagingException {
		int requestedMails = sendLocationRequest();
		LOG.info("Sleep for 10 minutes before checking mails");
		Thread.sleep(10 * 60 * 1000);
		if(requestedMails > mail.getAll1392MessageCount()) {
			requestedMails = sendLocationRequest();
			LOG.info("Sleep for 10 minutes before checking mails");
			Thread.sleep(10 * 60 * 1000);
		}
		saveLocationResponses();
		sendReport(reportManager.generateReport());
	}

	
	public void sendReport(File file) throws GeneralSecurityException, IOException, MessagingException {
		//mail.springSendFile(file, mailTo);
		mail.sendFileByMail("Дислокація", "Звіт додано до листа", file, "frendos.a@gmail.com");
	}
}
