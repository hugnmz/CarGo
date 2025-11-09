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
import util.exception.ApplicationException;
import util.exception.BusinessException;

// class xu ly gui email
public class EmailUtil {

    // properties chua cau hinh email
    private static final Properties props = new Properties();

    // khoi tao cau hinh email tu file mail.properties
    static {
        try (InputStream in = EmailUtil.class.getClassLoader()
                .getResourceAsStream("mail.properties")) {
            if (in != null) {
                // tai cau hinh tu file
                props.load(in);
            } else {
                // neu khong tim thay file thi bo qua
            }
        } catch (IOException e) {
            throw new ApplicationException("error.system.properties.load", e);
        }
    }


    // method tao session email voi cau hinh smtp
    private static Session buildSession() {
        // lay username va password tu cau hinh
        final String username = props.getProperty("mail.username");
        final String password = props.getProperty("mail.password");

        // tao properties cho smtp
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", props.getProperty("mail.smtp.auth", "true"));
        mailProps.put("mail.smtp.starttls.enable", props.getProperty("mail.smtp.starttls.enable", "true"));
        mailProps.put("mail.smtp.ssl.protocols", props.getProperty("mail.smtp.ssl.protocols", "TLSv1.2"));
        mailProps.put("mail.smtp.host", props.getProperty("mail.smtp.host", "smtp.gmail.com"));
        mailProps.put("mail.smtp.port", props.getProperty("mail.smtp.port", "587"));
        // neu co bat debug mode
        if ("true".equalsIgnoreCase(props.getProperty("mail.debug"))) {
            mailProps.put("mail.debug", "true");
        }

        // tao session voi authenticator
        return Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    // method gui email 
    public static void send(String to, String subject, String htmlBody) throws MessagingException {
        // tao session
        Session session = buildSession();

        // tao message
        Message message = new MimeMessage(session);
        String from = props.getProperty("mail.from", props.getProperty("mail.username"));

        // dat thong tin nguoi gui va nguoi nhan
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(htmlBody, "text/html; charset=UTF-8");

        // gui email
        Transport.send(message);
    }

    
   
    //Admin gửi thông tin tài khoản cho nhân viên
    public static void sendCredentials(String toEmail, String fullname,
            String username, String password, String role) {
        // Load cấu hình từ mail.properties
        final String fromEmail = props.getProperty("mail.from", props.getProperty("mail.username"));
        final String appPassword = props.getProperty("mail.password");

        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", props.getProperty("mail.smtp.auth", "true"));
        mailProps.put("mail.smtp.starttls.enable", props.getProperty("mail.smtp.starttls.enable", "true"));
        mailProps.put("mail.smtp.ssl.protocols", props.getProperty("mail.smtp.ssl.protocols", "TLSv1.2"));
        mailProps.put("mail.smtp.host", props.getProperty("mail.smtp.host", "smtp.gmail.com"));
        mailProps.put("mail.smtp.port", props.getProperty("mail.smtp.port", "587"));

        // Nếu có bật debug mode
        if ("true".equalsIgnoreCase(props.getProperty("mail.debug"))) {
            mailProps.put("mail.debug", "true");
        }

        Session session = Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "CarGo Admin"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("CarGo account your information");

            String content = "<h3>Xin chào " + fullname + ",</h3>"
                    + "<p>Tài khoản của bạn đã được tạo trong hệ thống CarGo.</p>"
                    + "<p><b>Tên đăng nhập:</b> " + username + "<br>"
                    + "<b>Mật khẩu:</b> " + password + "<br>"
                    + "<b>Vai trò:</b> " + role + "</p>"
                    + "<p>Vui lòng đăng nhập tại: <a href='http://localhost:8080/RentalCar/Login'>CarGo</a></p>"
                    + "<p>Trân trọng,<br>CarGo Team</p>";

            message.setContent(content, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("[EmailUtil] Mail sent to " + toEmail);
        } catch (Exception e) {
            throw new BusinessException("error.system.email.send", e);
        }
    }
}
