package com.kerriline.location.mail;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;

/* class to demonstrate use of Gmail list labels API */
public class GmailQuickstart {
    /** Application name. */
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /** Directory to store authorization tokens for this application. */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GmailQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(80).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    public static void mainLabels(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        Gmail service = getGmail();

        // Print the labels in the user's account.
        String user = "me";
        ListLabelsResponse listResponse = service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.isEmpty()) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }
    }

    
    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        Gmail service = getGmail();

        // Print the labels in the user's account.
        String user = "me";
        
        ListMessagesResponse listResponse = service.users().messages().list(user)
        		.setQ("in:inbox subject:1392 is:unread")
        		.execute();

        if (listResponse.getResultSizeEstimate() == 0) {
            System.out.println("No messages found.");
        } else {
            System.out.println("Messages:");
            for (Message msgId : listResponse.getMessages()) {
                System.out.printf("- %s\n", msgId.getId());
                Message message = service.users().messages()
                		.get(user, msgId.getId())
                		.execute();

                System.out.printf("- %s %s\n", getSubject(message), getDateTime(message));
                System.out.print(getText(message));
                	
            }
        }
    }
    
    public static Message getFullMessage(Message message) throws IOException, GeneralSecurityException {
    	Gmail service = getGmail();
    	String user = "me";
		return service.users().messages()
			.get(user , message.getId())
			.execute();
    }
    
    public static String getSubject(Message message) {
    	List<MessagePartHeader> headers = message.getPayload().getHeaders();
        Optional<String> header = headers.stream()
            .filter(h -> h.getName().equals("Subject"))
        	.map(MessagePartHeader::getValue)
        	.findFirst();
        return header.orElse("");
    }
    
    public static ZonedDateTime getDateTime(Message message) {
    	Instant i = Instant.ofEpochSecond(message.getInternalDate());
		ZonedDateTime datetime = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());
        
        return datetime;
    }
    
	public static Gmail getGmail() throws GeneralSecurityException, IOException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
		return service;
	}
	
	public static Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		email.writeTo(baos);
		String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;
	}

	public static String getText(Message message) {
		
		List<MessagePart> parts = message.getPayload().getParts();
		MessagePartBody body = parts.get(0).getBody();
		
		byte[] decodedData = Base64.decodeBase64(body.getData());
		
		String data = new String(decodedData, Charset.forName("UTF-8"));
		
		return data;
	}

}