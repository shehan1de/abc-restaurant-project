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
public class OrderEmailService {

    private final JavaMailSender mailSender;

    public OrderEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendOrderConfirmation(String userEmail, String orderId, List<Order.OrderItem> items,
                                      float taxAmount, float deliveryCharges,
                                      float discountAmount, float finalAmount) {
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
                    .append("<h2 style='color: #2c3e50;'>Order Confirmation</h2>")
                    .append("<p style='font-size: 16px;'>Thank you for your order! We wanted to let you know that your order is currently <strong style='color: #F1C40F;'>Pending</strong></p>")
                    .append("<div style='margin: 25px;'>")
                    .append("<p style='font-size: 18px;'><strong>Order ID - </strong><span style='color: #3498db;font-size: 22px;'>" + orderId + "</span></p>")
                    .append("<h3 style='color: #34495e;'>Order Details</h3>")
                    .append("<table style='font-size: 16px; width: 100%; margin: 10px 0; border-collapse: collapse;'>")
                    .append("<thead>")
                    .append("<tr style='background-color: #f0f0f0;'>")
                    .append("<th style='text-align: left; padding: 8px;'>Product</th>")
                    .append("<th style='text-align: center; padding: 8px;'>Quantity</th>")
                    .append("<th style='text-align: right; padding: 8px;'>Total Price</th>")
                    .append("</tr>")
                    .append("</thead>")
                    .append("<tbody>");

            // Add order items with total price (price * quantity)
            for (Order.OrderItem item : items) {
                float totalItemPrice = item.getPrice() * item.getQuantity(); // Calculate total price for each item
                htmlContent.append("<tr>")
                        .append("<td style='padding: 8px;'>").append(item.getProductName()).append("</td>")
                        .append("<td style='padding: 8px; text-align: center;'>").append(item.getQuantity()).append("</td>")
                        .append("<td style='padding: 8px; text-align: right;margin-bottom:15px;'>Rs. ").append(String.format("%.2f", totalItemPrice)).append("</td>") // Updated line to show total price
                        .append("</tr>");
            }

            htmlContent.append("</tbody>")
                    .append("</table>");

            htmlContent.append("</tbody>")
                    .append("</table>")
                    .append("<div style='margin-top: 20px;margin-bottom:15px; font-size: 16px;'>")
                    .append("<h3 style='color: #34495e;'>Billing Summary</h3>")
                    .append("<table style='width: 100%; border-collapse: collapse;'>")
                    .append("<tbody>")
                    .append("<tr>")
                    .append("<td style='padding: 8px; border-bottom: 1px solid #ddd;'><strong>Tax</strong></td>")
                    .append("<td style='padding: 8px; text-align: right; border-bottom: 1px solid #ddd;'>Rs. ").append(String.format("%.2f", taxAmount)).append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style='padding: 8px; border-bottom: 1px solid #ddd;'><strong>Delivery Charges</strong></td>")
                    .append("<td style='padding: 8px; text-align: right; border-bottom: 1px solid #ddd;'>Rs. ").append(String.format("%.2f", deliveryCharges)).append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style='padding: 8px; border-bottom: 1px solid #ddd;'><strong>Discount</strong></td>")
                    .append("<td style='padding: 8px; text-align: right; border-bottom: 1px solid #ddd;'>- Rs. ").append(String.format("%.2f", discountAmount)).append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style='padding: 8px; border-bottom: 1px solid #ddd;'><strong>Total Amount</strong></td>")
                    .append("<td style='padding: 8px; text-align: right; border-bottom: 1px solid #ddd;'>Rs. ").append(String.format("%.2f", finalAmount)).append("</td>")
                    .append("</tr>")
                    .append("</tbody>")
                    .append("</table>")
                    .append("</div>")
                    .append("<p style='font-size: 16px; font-weight:bold'>We will notify you once your order is processed and shipped.</p>")
                    .append("<img src='cid:logo' alt='ABC Restaurant Logo' style='width: 200px; height: auto; margin: 20px;'/>")
                    .append("<p style='font-size: 16px; font-weight:bold;'>ABC Restaurant</p>")
                    .append("</center>")
                    .append("</body></html>");

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



