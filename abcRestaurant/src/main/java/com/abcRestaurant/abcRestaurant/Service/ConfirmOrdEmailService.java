package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfirmOrdEmailService {

    private final JavaMailSender mailSender;

    public ConfirmOrdEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendOrderConfirmation(String userEmail, String orderId, float finalAmount, String orderStatus) {
        try {
            // Create a MimeMessage
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(userEmail);
            helper.setSubject("Order Confirmation - Your Order ID: " + orderId);

            // Build the HTML content for the email
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><body style='font-family: Arial, sans-serif; color: #333;'>")
                    .append("<center>")
                    .append("<h2 style='color: #2c3e50;'>Order Status Update</h2>");

            if ("Accepted".equals(orderStatus)) {
                htmlContent.append("<p style='font-size: 16px;'>Thank you for your order! We are pleased to inform you that your order has been <strong style='color: #2ecc71;'>Accepted</strong>.</p>");
            } else if ("Denied".equals(orderStatus)) {
                htmlContent.append("<p style='font-size: 16px;'>We regret to inform you that your order has been <strong style='color: #e74c3c;'>Denied</strong>. We apologize for any inconvenience caused.</p>");
            } else {
                htmlContent.append("<p style='font-size: 16px;'>Your order status is currently <strong style='color: #f39c12;'>").append(orderStatus).append("</strong>.</p>");
            }

            htmlContent.append("<div style='margin: 25px;'>")
                    .append("<p style='font-size: 18px;'><strong>Order ID - </strong><span style='color: #3498db;font-size: 22px;'>").append(orderId).append("</span></p>")
                    .append("</div>")
                    .append("<div style='margin-top: 20px; font-size: 16px;'>")
                    .append("<h3 style='color: #34495e;'>Billing Summary</h3>")
                    .append("<table style='width: 100%; border-collapse: collapse;'>")
                    .append("<tbody>")
                    .append("<tr>")
                    .append("<td style='padding: 8px; border-bottom: 1px solid #ddd;'><strong>Total Amount</strong></td>")
                    .append("<td style='padding: 8px; text-align: right; border-bottom: 1px solid #ddd;'>Rs. ").append(String.format("%.2f", finalAmount)).append("</td>")
                    .append("</tr>")
                    .append("</tbody>")
                    .append("</table>")
                    .append("</div>")
                    .append("<img src='cid:logo' alt='ABC Restaurant Logo' style='width: 200px; height: auto; margin: 20px;'/>")
                    .append("<p style='font-size: 16px; font-weight:bold;'>ABC Restaurant</p>")
                    .append("</center>")
                    .append("</body></html>");

// To use this content, you can pass it to your email service or client to send the email

            // Set the email content
            helper.setText(htmlContent.toString(), true); // true indicates HTML content

            // Add inline image if needed
            helper.addInline("logo", new ClassPathResource("static/logo.png"));

            // Send the email
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle the exception properly in your application
        }
    }
}
