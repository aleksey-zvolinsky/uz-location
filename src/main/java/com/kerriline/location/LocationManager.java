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
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Aleksey
 *
 */
@Component
public class LocationManager {

    public LocationManager(
        MailManager mail,
        MailParser source,
        TankRepository tankRepository,
        LocationResponseRepository locationResponseRepository,
        ReportManager reportManager
    ) {
        this.mail = mail;
        this.source = source;
        this.tankRepository = tankRepository;
        this.locationResponseRepository = locationResponseRepository;
        this.reportManager = reportManager;
    }

    private static final String REQUEST_NUMBER = "1392";
    private static final Logger LOG = LoggerFactory.getLogger(LocationManager.class);

    private final MailManager mail;
    private final MailParser source;
    private final TankRepository tankRepository;
    private final LocationResponseRepository locationResponseRepository;
    private final ReportManager reportManager;

    @Value("${application.result-mail.to}")
    private final String mailTo = "service@kerriline.com.ua";

    /**
     * send requests on email
     */
    public int sendLocationRequest() throws InterruptedException {
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

    public void saveLocationResponses() {
        LOG.info("Reading mails");
        List<MessageBean> messages = mail.search1392Messages();
        Collections.reverse(messages);
        for (MessageBean messageBean : messages) {
            List<LocationResponse> responses = source.text2table(messageBean);
            LOG.info("Writing data");

            for (LocationResponse response : responses) {
                LocationResponse example = new LocationResponse().tankNumber(response.getTankNumber());

                Optional<LocationResponse> one = locationResponseRepository.findOne(Example.of(example));

                LocationResponse storedResponse = one.orElse(response);

                BeanUtils.copyProperties(response, storedResponse, "version", "id");

                locationResponseRepository.save(storedResponse);
            }
        }
        LOG.info("Sheet updated");
    }

    @Scheduled(cron = "${application.location-schedule}")
    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 100))
    public void fullTrip() throws IOException, InterruptedException {
        int requestedMails = sendLocationRequest();
        LOG.info("Sleep for 10 minutes before checking mails");
        Thread.sleep(Duration.ofMillis(10).toMillis());
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

    public void sendReport(File file) throws IOException {
        mail.sendFileByMail("Дислокація", "Звіт додано до листа", file, mailTo);
    }
}
