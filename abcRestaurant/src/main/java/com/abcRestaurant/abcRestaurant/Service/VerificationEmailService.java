package com.abcRestaurant.abcRestaurant.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class VerificationEmailService {
    private final JavaMailSender mailSender;

    public VerificationEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String to, String code) {
        try {
            // Create a MimeMessage
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject("Password Reset Code");

            // HTML content
            String htmlContent = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<center>"
                    + "<h2 style='color: #2c3e50;'>Password Reset Request</h2>"
                    + "<p style='font-size: 16px;'>We received a request to reset your password. Please use the following code to proceed:</p>"
                    + "<div style='margin: 20px;'>"
                    + "<p style='font-size: 16px;'><strong>Password Reset Code:</strong> <span style='color: #e74c3c;font-size:25px;font-weight:bold;'>" + code + "</span></p>"
                    + "</div>"
                    + "<p style='font-size: 16px;'>If you did not request this, please ignore this email or contact support if you have any concerns.</p>"
                    + "<img src='cid:logo' alt='ABC Restaurant Logo' style='width: 200px; height: auto; margin: 20px;'/>"
                    + "<p style='font-size: 16px; font-weight:bold;'>ABC Restaurant</p>"
                    + "</center>"

                    + "</body></html>";

            // Set the email content
            helper.setText(htmlContent, true); // true indicates HTML content

            // Add inline image if needed
            helper.addInline("logo", new ClassPathResource("static/logo.png"));

            // Send the email
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle the exception properly in your application
        }
    }
}
