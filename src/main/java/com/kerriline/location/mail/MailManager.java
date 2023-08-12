package com.kerriline.location.mail;

import static jakarta.mail.Flags.Flag.SEEN;

import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.search.SubjectTerm;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Oleksii
 *
 */
@Component
public class MailManager {

    private static final String LOCATION_NUMBER = "1392";

    private static final Logger LOG = LoggerFactory.getLogger(MailManager.class);

    @Value("${application.report-mail.login}")
    private final String email = "sledline@gmail.com";

    @Value("${application.report-mail.password}")
    private final String pass = "kerriline2020";

    @Value("${application.report-mail.to}")
    private final String autoreportAddress = "autoinform@uz.gov.ua";

    public List<MessageBean> search1392Messages() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        return searchMessages(LOCATION_NUMBER);
    }

    public List<MessageBean> searchMessages(String readWithSubject) {
        List<MessageBean> result = new ArrayList<>();
        try (Store store = getStore(); Folder inbox = store.getFolder("Inbox")) {
            // create the inbox object and open it
            inbox.open(Folder.READ_WRITE);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = inbox.search(new AndTerm(new FlagTerm(new Flags(SEEN), false), new SubjectTerm(readWithSubject)));

            LOG.info("Total Messages:- {}", messages.length);
            LOG.info("------------------------------");

            if (messages.length == 0) {
                return result;
            }

            for (Message message : messages) {
                Date date = message.getReceivedDate();
                String subject = message.getSubject();
                String text = message.getContent().toString();

                LOG.info("Found required mail: {}, received {}", subject, message.getReceivedDate());

                MessageBean bean = new MessageBean(subject, text, date);

                result.add(bean);

                message.setFlag(SEEN, true);
            }
        } catch (IOException | MessagingException e) {
            LOG.error("Failed to make a mail search", e);
        }
        return result;
    }

    public Long getAll1392MessageCount() {
        try {
            Store store = getStore();

            // create the inbox object and open it
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = inbox.search(new AndTerm(new FlagTerm(new Flags(SEEN), false), new SubjectTerm("1392")));

            LOG.info("Total 1392-kind Messages:- {}", messages.length);
            return (long) messages.length;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Store getStore() throws MessagingException {
        Properties properties = new Properties();

        String host = "imap.gmail.com";

        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.starttls.enable", "true");
        properties.put("mail.imap.ssl.trust", host);

        Session emailSession = Session.getDefaultInstance(properties);
        Store store = emailSession.getStore("imaps");

        store.connect(host, email, pass);
        return store;
    }

    public void submitRequest(String subject, String text) {
        sendMail(subject, text, autoreportAddress);
    }

    public void sendMail(String subject, String text, String mailTo) {
        try {
            Session session = getSMTPSession();

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

            LOG.info("Email have been sent successfully");
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private Session getSMTPSession() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(
            prop,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, pass);
                }
            }
        );
    }

    public void sendFileByMail(String subject, String text, File fileToSend, String mailTo) throws IOException {
        try {
            Session session = getSMTPSession();

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(text);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(fileToSend);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);

            LOG.info("Mail with attachment sent successfully");
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }
}
