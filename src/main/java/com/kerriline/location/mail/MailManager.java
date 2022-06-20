package com.kerriline.location.mail;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.ModifyMessageRequest;

/**
 *
 * @author Oleksii
 *
 */
@Component
public class MailManager {

    private static final String userId = "me";

	private static final String LOCATION_NUMBER = "1392";

	private static final Logger LOG = LoggerFactory.getLogger(MailManager.class);

    @Value("${application.report-mail.login}")
    private String email = "sledline@gmail.com";

    @Value("${application.report-mail.password}")
    private String pass = "kerriline2020";

    @Value("${application.report-mail.to}")
    private String autoreportAddress = "autoinform@uz.gov.ua";

    @Value("${application.result-mail.subject}")
    private String resultSubject = "Дислокация";

    @Value("${application.result-mail.text}")
    private String resultText = "Смотрите вложение";

    @Value("${application.result-mail.attachmentFileName}")
    private String attachmentFileName = "dislocation.xlsx";


    public List<MessageBean> search1392Messages() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        return searchMessages(LOCATION_NUMBER, cal.getTime());
    }
    
    public List<MessageBean> searchMessages(String readWithSubject, Date skipBeforeDate) {
    	
    	try {
    		Gmail gmail = GmailQuickstart.getGmail();
		
			ListMessagesResponse list = gmail
					.users().messages().list(userId)
					.setQ("in:inbox subject:1392 is:unread")
					.execute();
			
			
            Long messageCount = list.getResultSizeEstimate();

            LOG.info("Total Messages:- {}", messageCount);
            
            
            LOG.info("------------------------------");
            
            List<MessageBean> result = new ArrayList<MessageBean>();
            
            if (messageCount == 0) {
            	return result;
            }
            
			for (Message message : list.getMessages()) {
				message = GmailQuickstart.getFullMessage(message);
				
				Date date = Date.from(GmailQuickstart.getDateTime(message).toInstant());
				if(date.before(skipBeforeDate)) {
					continue;
				}
				String subject = GmailQuickstart.getSubject(message);
				String text = GmailQuickstart.getText(message);
				
                LOG.info("Found required mail: {}, received {}", subject, GmailQuickstart.getDateTime(message));

                MessageBean bean = new MessageBean(subject, text, date);
                
				result.add(bean);
				
				ModifyMessageRequest mods =
	                    new ModifyMessageRequest()
	                    .setAddLabelIds(Collections.singletonList("INBOX"))
	                    .setRemoveLabelIds(Collections.singletonList("UNREAD"));
				
				gmail.users().messages()
					.modify(userId, message.getId(), mods)
					.execute();

            }
			
            return result;
		} catch (IOException | GeneralSecurityException | MessagingException e) {
			throw new RuntimeException("Failed to make a mail search", e);
		}
    	
    }

    public int getAll1392MessageCount() {
    	ZonedDateTime hourAgo = ZonedDateTime.now().minusHours(1);
    	
    	try {
    		Gmail gmail = GmailQuickstart.getGmail();
			ListMessagesResponse messages = gmail
					.users().messages().list(userId)
					.setQ("in:inbox subject:1393 is:unread")
					.execute();
			LOG.info("Total Messages:- {}", messages.getResultSizeEstimate());
			int count = 0;
			if (messages.getResultSizeEstimate() == 0) {
				return 0;
			}
			for(Message message: messages.getMessages()) {
				message = GmailQuickstart.getFullMessage(message);
				String subject = GmailQuickstart.getSubject(message);
				ZonedDateTime date = GmailQuickstart.getDateTime(message);
	            
	            LOG.debug("Mail Subject:- {}, received {}", subject, date);
                if (hourAgo.isBefore(date)) {
                    LOG.info("Found required mail: {}, received {}", subject, date);
                    count ++;
                }
			}
			return count;
		} catch (IOException | GeneralSecurityException e) {
			throw new RuntimeException("Failed to getAll1392MessageCount ");
		}
    }

    public void submitRequest(String subject, String text) {
        sendMail(subject, text, autoreportAddress);
    }

    public void sendMail(String subject, String text, String mailTo) {
        try {
        	
        	Gmail gmail = GmailQuickstart.getGmail();
        	
    		Properties props = new Properties();
    		Session session = Session.getDefaultInstance(props, null);
    		
        	MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setSubject(subject);
            message.setText(text);
            

			gmail.users().messages()
				.send(userId, GmailQuickstart.createMessageWithEmail(message))
				.execute();
            
            LOG.info("Email have been sent successfully");
        } catch (MessagingException | GeneralSecurityException | IOException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
    

    public void sendFileByMail(String subject, String text, File fileToSend, String mailTo) throws IOException {

        try {
        	
        	Gmail gmail = GmailQuickstart.getGmail();
        	
    		Properties props = new Properties();
    		Session session = Session.getDefaultInstance(props, null);
    		
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

            gmail.users().messages()
            	.send(userId, GmailQuickstart.createMessageWithEmail(message))
            	.execute();

            LOG.info("Mail with attachment sent successfully");
        } catch (MessagingException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }

}
