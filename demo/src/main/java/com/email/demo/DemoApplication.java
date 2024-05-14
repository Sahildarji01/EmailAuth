package com.email.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import jakarta.mail.MessagingException;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	private EmailService emailservice;
	@Autowired
	ClientRepository clientrepo;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	 @EventListener(ApplicationReadyEvent.class)
	    public void triggerMail() {
	        Client sender = clientrepo.findById(1L).orElse(null);
	        if (sender == null) {
	            System.err.println("Sender email not found in the database.");
	            return;
	        }

	        String senderEmail = sender.getEmail();
	        String recipient = "sahildarji1001@gmail.com"; 
	        String subject = "This is email subject";
	        String content = "This is email body";

	        emailservice.sendEmail(senderEmail, recipient, subject, content);
	    }

}
