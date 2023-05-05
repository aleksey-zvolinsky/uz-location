package com.kerriline.location;

import com.kerriline.location.domain.LocationResponse;
import com.kerriline.location.domain.Tank;
import com.kerriline.location.mail.MailManager;
import com.kerriline.location.mail.MailParser;
import com.kerriline.location.mail.MessageBean;
import com.kerriline.location.repository.LocationResponseRepository;
import com.kerriline.location.repository.TankRepository;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

/**
 *
 * @author Aleksey
 *
 */
@Component
public class LocationManager {

    private static final String REQUEST_NUMBER = "1392";

    private static final Logger LOG = LoggerFactory.getLogger(LocationManager.class);

    @Autowired
    MailManager mail;

    @Autowired
    MailParser source;

    @Value("${application.result-mail.to}")
    private final String mailTo = "service@kerriline.com.ua";

    @Autowired
    TankRepository tankRepository;

    /**
     * send requests on email
     * @return number of emails
     */
    public int sendLocationRequest() {
        int mailCount = 0;
        LOG.info("Reading tanks");

        List<Tank> tanks = tankRepository.findAll();
        StringBuilder text = new StringBuilder();
        int i = 0;
        for (Tank tank : tanks) {
            i++;
            text.append(tank.getTankNumber()).append("\n");
            if (i >= 150) {
                LOG.info("Sending mail");
                mail.submitRequest(REQUEST_NUMBER, text.toString());
                mailCount++;
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

            for (LocationResponse response : responses) {
                LocationResponse example = new LocationResponse().tankNumber(response.getTankNumber());

                LocationResponse storedResponse = locationResponseRepository.findOne(Example.of(example)).orElse(response);

                BeanUtils.copyProperties(response, storedResponse, "version", "id");

                locationResponseRepository.save(storedResponse);
            }
        }
        LOG.info("Database updated");
    }

    @Autowired
    ReportManager reportManager;

    public void sendReport(File file) throws IOException {
        mail.sendFileByMail("Дислокація", "Звіт додано до листа", file, mailTo);
    }

    public void updateAndSendReport() throws GeneralSecurityException, IOException, InterruptedException, MessagingException {
        int requestedMails = sendLocationRequest();
        LOG.info("Sleep for 10 minutes before checking mails");
        Thread.sleep(10 * 60 * 1000);
        Long receivedMessagesCount = mail.getAll1392MessageCount();
        if (requestedMails > receivedMessagesCount) {
            LOG.warn("We have received less mails then expected. Received {}. Expected {} ", receivedMessagesCount, requestedMails);
            sendLocationRequest();
            LOG.info("Sleep for another 10 minutes before checking mails");
            Thread.sleep(10 * 60 * 1000);
        }
        saveLocationResponses();
        sendReport(reportManager.generateReport());
    }
}
