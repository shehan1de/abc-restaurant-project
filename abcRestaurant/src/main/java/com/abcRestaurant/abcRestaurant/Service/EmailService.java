package com.abcRestaurant.abcRestaurant.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendReservationConfirmation(String to, String reservationId, String bookingSeats, String date, String time, String branch) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject("Reservation Confirmation");

            String htmlContent = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<center>"
                    + "<h2 style='color: #2c3e50;'>Thank You for Choosing ABC Restaurant!</h2>"
                    + "<p style='font-size: 16px;'>We are pleased to inform you that your reservation is currently <strong style='color: #F1C40F;'>Pending</strong>.</p>"
                    + "<div style='margin: 20px;'>"
                    + "<p style='font-size: 16px;'><strong>Reservation ID:</strong> " + reservationId + "</p>"
                    + "<p style='font-size: 16px;'><strong>Seats:</strong> " + bookingSeats + "</p>"
                    + "<p style='font-size: 16px;'><strong>Date:</strong> " + date + "</p>"
                    + "<p style='font-size: 16px;'><strong>Time:</strong> " + time + "</p>"
                    + "<p style='font-size: 16px;'><strong>Branch:</strong> " + branch + "</p>"
                    + "</div>"
                    + "<p style='font-size: 16px;'>Our team will provide you with an update on your reservation as soon as possible.</p>"
                    + "<img src='cid:logo' alt='ABC Restaurant Logo' style='width: 200px; height: auto; margin: 20px;'/>"
                    + "<p style='font-size: 16px; font-weight:bold;'>ABC Restaurant</p>"
                    + "</center>"
                    + "</body></html>";


            helper.setText(htmlContent, true);
            helper.addInline("logo", new ClassPathResource("static/logo.png"));

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
