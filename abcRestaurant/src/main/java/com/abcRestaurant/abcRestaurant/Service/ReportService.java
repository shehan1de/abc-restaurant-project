package com.abcRestaurant.abcRestaurant.Service;

import com.abcRestaurant.abcRestaurant.Model.FinancialReportData;
import com.abcRestaurant.abcRestaurant.Model.Order;
import com.abcRestaurant.abcRestaurant.Model.SalesReportData;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private OrderService orderService;


    // Generate PDF bill for an order
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
        document.close();

        return baos.toByteArray();
    }

    // Generate PDF for Sales Report
    public byte[] generateSalesReportPDF(LocalDateTime startDate, LocalDateTime endDate) throws DocumentException, IOException {
        SalesReportData salesData = orderService.getSalesReportData(startDate, endDate);
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
            ClassPathResource logoResource = new ClassPathResource("static/logo.png");
            Image logo = Image.getInstance(logoResource.getURL());
            logo.scaleToFit(100, 100);
            logo.setAlignment(Image.ALIGN_LEFT);
            logoCell.addElement(logo);
            logoCell.setBorder(Rectangle.NO_BORDER); // No border around the cell
        } catch (Exception e) {
            e.printStackTrace();
        }
        headerTable.addCell(logoCell);

        // Add restaurant name and dates
        PdfPCell nameCell = new PdfPCell();
        nameCell.setBorder(Rectangle.NO_BORDER); // No border around the cell

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph restaurantName = new Paragraph("ABC Restaurant", titleFont);
        restaurantName.setAlignment(Paragraph.ALIGN_RIGHT);
        nameCell.addElement(restaurantName);

        // Add report title and date range
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Paragraph dateRange = new Paragraph("Sales Report\n" +
                "From: " + startDate.toLocalDate().toString() + " To: " + endDate.toLocalDate().toString(), dateFont);
        dateRange.setAlignment(Paragraph.ALIGN_RIGHT);
        dateRange.setSpacingBefore(5);
        nameCell.addElement(dateRange);

        headerTable.addCell(nameCell);

        document.add(headerTable); // Add the header table to the document

        // Add a space before the title
        document.add(new Paragraph(" "));

        // Sales Report Title
        Font goldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25, new BaseColor(166, 144, 3)); // Gold color
        Paragraph orderListTitle = new Paragraph("Sales Report", goldFont);
        orderListTitle.setAlignment(Paragraph.ALIGN_CENTER);
        orderListTitle.setSpacingBefore(10);
        orderListTitle.setSpacingAfter(20);
        document.add(orderListTitle);

        // Add total sales data
        document.add(new Paragraph("Total Revenue: Rs. " + String.format("%.2f", salesData.getTotalRevenue())));
        document.add(new Paragraph("Total Orders - " + salesData.getTotalOrders()));
        document.add(new Paragraph("Total Products Sold - " + salesData.getTotalProductsSold()));

        // Add top-selling products section
        document.add(new Paragraph("\nTop-Selling Products\n"));

        Paragraph marginBottom = new Paragraph(" ");
        marginBottom.setSpacingAfter(10);
        document.add(marginBottom);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);


        Font itemFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);

        PdfPCell cell = new PdfPCell(new Phrase("Product Name", itemFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell(new PdfPCell(new Phrase("Quantity Sold", itemFont)));


        for (Map.Entry<String, Long> entry : salesData.getTopSellingProducts().entrySet()) {
            table.addCell(entry.getKey());
            table.addCell(String.valueOf(entry.getValue()));
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }


    public byte[] generateFinancialReportPDF(LocalDateTime startDate, LocalDateTime endDate) throws DocumentException, IOException {
        FinancialReportData financialData = orderService.getFinancialReportData(startDate, endDate);
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

        // Add restaurant name and dates
        PdfPCell nameCell = new PdfPCell();
        nameCell.setBorder(Rectangle.NO_BORDER);

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph restaurantName = new Paragraph("ABC Restaurant", titleFont);
        restaurantName.setAlignment(Paragraph.ALIGN_RIGHT);
        nameCell.addElement(restaurantName);

        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Paragraph dateRange = new Paragraph("Financial Report\n" +
                "From: " + startDate.toLocalDate().toString() + " To: " + endDate.toLocalDate().toString(), dateFont);
        dateRange.setAlignment(Paragraph.ALIGN_RIGHT);
        dateRange.setSpacingBefore(5);
        nameCell.addElement(dateRange);

        headerTable.addCell(nameCell);
        document.add(headerTable);

        // Add a space before the title
        document.add(new Paragraph(" "));

        // Financial Report Title
        Font goldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25, new BaseColor(166, 144, 3));
        Paragraph reportTitle = new Paragraph("Financial Report", goldFont);
        reportTitle.setAlignment(Paragraph.ALIGN_CENTER);
        reportTitle.setSpacingBefore(10);
        reportTitle.setSpacingAfter(20);
        document.add(reportTitle);


        // Add top-selling products section
        document.add(new Paragraph("\nSummary of Financial Metrics\n"));

        Paragraph marginBottom = new Paragraph(" ");
        marginBottom.setSpacingAfter(10);
        document.add(marginBottom);


        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);


        Font itemFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);

        PdfPCell cell = new PdfPCell(new Phrase("Metrics", itemFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell(new PdfPCell(new Phrase("Total Amount", itemFont)));

        table.addCell("Total Income");
        table.addCell("Rs. " + String.format("%.2f", financialData.getTotalRevenue()));

        table.addCell("Total Expenses (Delivery + Offer price)");
        table.addCell("Rs. " + String.format("%.2f", financialData.getTotalExpenses()));

        table.addCell("Total Taxes");
        table.addCell("Rs. " + String.format("%.2f", financialData.getTotalTaxes()));

        table.addCell("Net Profit");
        table.addCell("Rs. " + String.format("%.2f", financialData.getNetProfit()));



        document.add(table);
        document.close();

        return baos.toByteArray();
    }

}


