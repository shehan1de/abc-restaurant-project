package com.abcRestaurant.abcRestaurant.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinancialReportData {
    private double totalRevenue;
    private double totalExpenses;
    private double netProfit;
    private double totalTaxes;
}
