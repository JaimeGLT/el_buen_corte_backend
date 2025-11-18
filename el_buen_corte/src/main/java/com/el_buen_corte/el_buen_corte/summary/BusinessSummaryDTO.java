package com.el_buen_corte.el_buen_corte.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessSummaryDTO {
    private Double totalIncome;
    private Double totalExpenses;
    private Double netProfit;
    private Double profitMargin;

    private Long totalClients;
    private Long newClients;
    private Double retentionRate;
    private Long recurringClients;

    private Long totalAppointments;
    private Long canceledAppointments;
    private Double averageTicket;
    private Double occupancyRate;

    private Long totalProducts;
    private Double totalProductsValue;
    private Long lowStockProducts;

    private String mostUsedService;
    private Long mostUsedServiceCount;
}
