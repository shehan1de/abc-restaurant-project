package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.Order;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;




@Service
public class ReportService {

    @Autowired
    private OrderService orderService;

    public byte[] generateBill(String orderId) throws DocumentException, IOException {
        Optional<Order> orderOptional = Optional.ofNullable(orderService.findOrderById(orderId));
        if (!orderOptional.isPresent()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }

        Order order = orderOptional.get();
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, baos);
        document.open();

        // Add logo and restaurant name on the same line
        PdfPTable headerTable = new PdfPTable(2); // Two columns
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingAfter(10f);

        // Add logo
        PdfPCell logoCell = new PdfPCell();
        try {
            // Load the logo image
            ClassPathResource logoResource = new ClassPathResource("static/logo.png");
            Image logo = Image.getInstance(logoResource.getURL());
            logo.scaleToFit(100, 100);
            logo.setAlignment(Image.ALIGN_LEFT);
            logoCell.addElement(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        headerTable.addCell(logoCell);

        PdfPCell nameCell = new PdfPCell();
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph restaurantName = new Paragraph("ABC Restaurant", titleFont);
        restaurantName.setAlignment(Paragraph.ALIGN_RIGHT);
        restaurantName.setSpacingBefore(10);
        restaurantName.setSpacingAfter(5);
        nameCell.addElement(restaurantName);

        Paragraph underline = new Paragraph(" ");
        underline.setSpacingBefore(5);
        nameCell.addElement(underline);

        nameCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(nameCell);

        document.add(headerTable);

        Font goldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25, new BaseColor(166, 144, 3)); // Gold color
        Paragraph orderListTitle = new Paragraph("Order List", goldFont);
        orderListTitle.setAlignment(Paragraph.ALIGN_CENTER);
        orderListTitle.setSpacingBefore(10);
        orderListTitle.setSpacingAfter(50);
        document.add(orderListTitle);

        Font detailFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Font itemFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);

        document.add(new Paragraph("Order ID: " + order.getOrderId(), detailFont));
        document.add(new Paragraph("Order Date: " + order.getOrderDate(), detailFont));
        document.add(new Paragraph("Order Status: " + order.getOrderStatus(), detailFont));
        document.add(new Paragraph("User Email: " + order.getUserEmail(), detailFont));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(50f);
        table.setSpacingAfter(50f);

        PdfPCell cell = new PdfPCell(new Phrase("Product Name", itemFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY); // Background color for header
        table.addCell(cell);
        table.addCell(new PdfPCell(new Phrase("Quantity", itemFont)));
        table.addCell(new PdfPCell(new Phrase("Price", itemFont)));
        table.addCell(new PdfPCell(new Phrase("Total", itemFont)));

        for (Order.OrderItem item : order.getItems()) {
            table.addCell(new PdfPCell(new Phrase(item.getProductName(), detailFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), detailFont)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", item.getPrice()), detailFont)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", item.getQuantity() * item.getPrice()), detailFont)));
        }

        document.add(table);

        PdfPTable financialTable = new PdfPTable(1);
        financialTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        financialTable.setWidthPercentage(100);
        financialTable.setSpacingBefore(10f);
        financialTable.setSpacingAfter(10f);

        PdfPCell taxCell = new PdfPCell(new Phrase("Tax Amount: " + String.format("%.2f", order.getTaxAmount()), detailFont));
        taxCell.setBorder(Rectangle.NO_BORDER);
        taxCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        financialTable.addCell(taxCell);

        PdfPCell deliveryCell = new PdfPCell(new Phrase("Delivery Charges: " + String.format("%.2f", order.getDeliveryCharges()), detailFont));
        deliveryCell.setBorder(Rectangle.NO_BORDER);
        deliveryCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        financialTable.addCell(deliveryCell);

        PdfPCell discountCell = new PdfPCell(new Phrase("Discount Amount: " + String.format("%.2f", order.getDiscountAmount()), detailFont));
        discountCell.setBorder(Rectangle.NO_BORDER);
        discountCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        financialTable.addCell(discountCell);

        Font finalAmountFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);

        PdfPCell finalAmountCell = new PdfPCell(new Phrase("Final Amount: " + String.format("%.2f", order.getFinalAmount()), finalAmountFont));
        finalAmountCell.setBorder(Rectangle.NO_BORDER);
        finalAmountCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        financialTable.addCell(finalAmountCell);

        document.add(financialTable);


        document.close();
        return baos.toByteArray();
    }
}