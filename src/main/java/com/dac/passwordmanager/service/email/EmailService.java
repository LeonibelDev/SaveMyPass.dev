package com.dac.passwordmanager.service.email;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.*;
import com.dac.passwordmanager.entity.MessageEntity;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // google mail account credentials
    @Value("${smtp.username}")
    private String username;

    @Value("${smtp.password}")
    private String password;

    @Async
    public void sendConfirmationEmail(String to, MessageEntity messageEntity) throws IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = getSession(props, username, password);

        // Sender
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(messageEntity.getSubject());

            if (messageEntity.getFile() == null) {
                message.setContent(EmailTemplate.getTemplate(messageEntity), "text/html; charset=utf-8");
            } else {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(EmailTemplate.getTemplate(messageEntity), "text/html; charset=utf-8");

                MimeBodyPart filePart = new MimeBodyPart();
                filePart.attachFile(messageEntity.getFile());

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                multipart.addBodyPart(filePart);

                message.setContent(multipart);
            }

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            if (messageEntity.getFile() != null && messageEntity.getFile().exists()) {
                messageEntity.getFile().delete();
            }
        }

    }

    public Session getSession(
            Properties props,
            String username,
            String password) {
        Session session = Session.getInstance(
                props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        return session;
    }
}
