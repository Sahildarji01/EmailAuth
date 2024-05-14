package com.email.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String senderEmail, String recipient, String subject, String content) {
        Client sender = clientRepository.findByEmail(senderEmail);
        if (sender == null) {
            System.err.println("Sender not found for email: " + senderEmail);
            return;
        }

        String smtpHost = sender.getSmtpHost();
        int smtpPort = sender.getSmtpPort();
        String username = sender.getUsername();
        String password = sender.getPassword();

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("Mail Sent...");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
