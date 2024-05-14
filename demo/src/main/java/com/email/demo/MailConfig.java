package com.email.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Autowired
    private ClientRepository clientRepository;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        Client client = clientRepository.findById(3L).orElse(null); 
        if (client == null) {
            throw new RuntimeException("Email configuration not found in the database.");
        }
        
        mailSender.setHost(client.getSmtpHost());
        mailSender.setPort(client.getSmtpPort());
        mailSender.setUsername(client.getUsername());
        mailSender.setPassword(client.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}

