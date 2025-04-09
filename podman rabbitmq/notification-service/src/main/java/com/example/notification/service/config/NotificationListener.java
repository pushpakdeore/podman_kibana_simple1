package com.example.notification.service.config;

//import com.example.notification.service.service.EmailService;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;

import com.example.notification.service.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    private final EmailService emailService;

    public NotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "user.queue")
    public void receiveMessage(String email) {
        logger.info("Received email message: {}", email);

        try {
            String token = java.util.UUID.randomUUID().toString();
            String confirmationLink = "http://localhost:8080/verify?token=" + token;

            String subject = "Confirm Your Email";
            String body = "Thank you for registering! Please confirm your email using the following link: \n" + confirmationLink;

            emailService.sendEmail(email.trim(), subject, body);

            logger.info("Email sent successfully to {}", email);
        } catch (Exception e) {
            logger.error("Failed to process message for email: {}", email, e);
        }
    }
}
