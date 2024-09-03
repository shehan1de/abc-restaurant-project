package com.abcRestaurant.abcRestaurant.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalesReportData {
    private double totalRevenue;
    private long totalOrders;
    private long totalProductsSold;
    private Map<String, Long> topSellingProducts;
}
