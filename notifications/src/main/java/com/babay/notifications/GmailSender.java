package com.babay.notifications;

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
import com.google.api.services.gmail.model.Message;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.MimetypesFileTypeMap;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Properties;



@Component
public class GmailSender {
    private static final String SENDER = "babayfiras6@gmail.com";
    private static final String APPLICATION_NAME = "jobportal";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    public static MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    public static Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);

        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.getEncoder().encodeToString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static void sendMessage(String destination, String subject, String body , InputStream in)
            throws MessagingException, IOException, GeneralSecurityException {

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        Message message = createMessageWithEmail(getMimeMessage(destination, subject, body, in));
        service.users().messages().send("me", message).execute();
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GmailSender.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        assert in != null;
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static MimeMessage getMimeMessage(String destination, String subject, String body, InputStream in) throws MessagingException {
        Session s = Session.getDefaultInstance(new Properties(), null);
        MimeMessage email = new MimeMessage(s);

        email.setFrom(new InternetAddress(SENDER));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO,
                new InternetAddress(destination));
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(body, "text/plain");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new InputStreamDataSource(in, "cv");

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName("cv.pdf");

        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);

        return email;

    }

    public static class InputStreamDataSource implements DataSource {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        private final String name;

        public InputStreamDataSource(InputStream inputStream, String name) {
            this.name = name;
            try {
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                inputStream.close();
                buffer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(buffer.toByteArray());
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Read-only data");
        }

        @Override
        public String getContentType() {
            return new MimetypesFileTypeMap().getContentType(name);
        }

        @Override
        public String getName() {
            return name;
        }
    }
}