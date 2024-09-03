package com.abcRestaurant.abcRestaurant.Controller;

import com.abcRestaurant.abcRestaurant.Service.ReportService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/sales")
    public ResponseEntity<byte[]> generateSalesBill(@RequestParam(required = false) String orderId) {
        byte[] pdfBytes;
        HttpHeaders headers = new HttpHeaders();

        try {
            if (orderId != null) {
                pdfBytes = reportService.generateBill(orderId);
            } else {
                pdfBytes = reportService.generateBill(orderId);
            }

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales-report.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(pdfBytes.length));

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException | DocumentException e) {
            e.printStackTrace(); // Optionally log the error message
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
