package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Order;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ConfirmOrdEmailService {

    private final JavaMailSender mailSender;
    private final ReportService reportService;

    public ConfirmOrdEmailService(JavaMailSender mailSender, ReportService reportService) {
        this.mailSender = mailSender;
        this.reportService = reportService;
    }

    @Async
    public void sendOrderConfirmation(String userEmail, String orderId, float finalAmount, String orderStatus) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(userEmail);
            helper.setSubject("Order Confirmation - Your Order ID: " + orderId);

            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><body style='font-family: Arial, sans-serif; color: #333;'>")
                    .append("<center>")
                    .append("<h2 style='color: #2c3e50;'>Order Status Update</h2>");

            if ("Accepted".equals(orderStatus)) {
                htmlContent.append("<p style='font-size: 16px;'>Thank you for your order! We are pleased to inform you that your order has been <strong style='color: #2ecc71;'>Accepted</strong>.</p>");


                byte[] pdfBytes = reportService.generateBill(orderId);
                helper.addAttachment("Online Receipt.pdf", new ByteArrayResource(pdfBytes));
            } else if ("Denied".equals(orderStatus)) {
                htmlContent.append("<p style='font-size: 16px;'>We regret to inform you that your order has been <strong style='color: #e74c3c;'>Denied</strong>. We apologize for any inconvenience caused.</p>");
            } else {
                htmlContent.append("<p style='font-size: 16px;'>Your order status is currently <strong style='color: #f39c12;'>").append(orderStatus).append("</strong>.</p>");
            }

            htmlContent.append("<div style='margin: 25px;'>")
                    .append("<p style='font-size: 18px;'><strong>Order ID - </strong><span style='color: #3498db;font-size: 22px;'>").append(orderId).append("</span></p>")
                    .append("</div>")
                    .append("<p style='font-size: 16px; font-weight:bold;'>ABC Restaurant</p>")
                    .append("</center>")
                    .append("</body></html>");


            helper.setText(htmlContent.toString(), true);


            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
