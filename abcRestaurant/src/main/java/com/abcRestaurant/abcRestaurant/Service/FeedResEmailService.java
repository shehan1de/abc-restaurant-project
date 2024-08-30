package com.abcRestaurant.abcRestaurant.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FeedResEmailService {

    private final JavaMailSender mailSender;

    public FeedResEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendFeedbackResponseEmail(String to, String name, String staffResponse, String userMessage) {
        String subject = "Response to Your Feedback";
        String htmlContent = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
                + "<center>"
                + "<h2 style='color: #2c3e50;'>Feedback Response</h2>"
                + "<p style='font-size: 16px;'>Dear " + name + ",</p>"
                + "<p style='font-size: 16px;'>Thank you for your feedback. We appreciate your input and have provided a response below:</p>"
                + "<div style='border: 1px solid #ddd; padding: 15px; background-color: #f9f9f9; border-radius: 5px;'>"
                + "<h3 style='color: #34495e;'>Your Message</h3>"
                + "<p style='font-size: 16px;'>" + userMessage + "</p>"
                + "</div>"

                + "<div style='border: 1px solid #ddd; padding: 15px; margin: 20px 0; border-radius: 5px;'>"

                + "<p style='font-size: 16px;'>" + staffResponse + "</p>"
                + "</div>"

                + "<p style='font-size: 16px;'>If you have any further questions or concerns, please feel free to reply to this email.</p>"
                + "<p style='font-size: 16px;'>Best regards,<br>The Team</p>"
                +"<img src='cid:logo' alt='ABC Restaurant Logo' style='width: 200px; height: auto; margin: 20px;'/>"
                +"<p style='font-size: 16px; font-weight:bold;'>ABC Restaurant</p>"
                + "</center>"
                + "</body></html>";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indicates HTML content

            helper.addInline("logo", new ClassPathResource("static/logo.png"));

            // Add inline image if needed
            // helper.addInline("logo", new ClassPathResource("static/logo.png"));

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle the exception properly in your application
        }
    }
}
