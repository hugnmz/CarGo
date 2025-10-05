package util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailUtil {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = EmailUtil.class.getClassLoader()
                .getResourceAsStream("mail.properties")) { 
            if (in != null) props.load(in);
            else System.err.println("[EmailUtil] mail.properties not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Session buildSession() {
        final String username = props.getProperty("mail.username");
        final String password = props.getProperty("mail.password");

        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", props.getProperty("mail.smtp.auth", "true"));
        mailProps.put("mail.smtp.starttls.enable", props.getProperty("mail.smtp.starttls.enable", "true"));
        mailProps.put("mail.smtp.ssl.protocols", props.getProperty("mail.smtp.ssl.protocols", "TLSv1.2"));
        mailProps.put("mail.smtp.host", props.getProperty("mail.smtp.host", "smtp.gmail.com"));
        mailProps.put("mail.smtp.port", props.getProperty("mail.smtp.port", "587"));
        if ("true".equalsIgnoreCase(props.getProperty("mail.debug"))) {
            mailProps.put("mail.debug", "true");
        }

        return Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public static void send(String to, String subject, String htmlBody) throws MessagingException {
        Session session = buildSession();

        Message message = new MimeMessage(session);
        String from = props.getProperty("mail.from", props.getProperty("mail.username"));

        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(htmlBody, "text/html; charset=UTF-8");

        Transport.send(message);
        System.out.println("[EmailUtil] Mail sent to " + to);
    }
}
