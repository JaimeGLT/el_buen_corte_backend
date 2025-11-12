package com.el_buen_corte.el_buen_corte.reports.resumen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumenResponse {
    private Double totalIncome;
    private Double totalExpenses;
    private Double netProfit;
    private Double profitMargin;

    private Long totalClients;
    private int newClients;
    private Double retentionRate;
    private Long recurringClients;

    private Long totalAppointments;
    private double averageTicket;
    private Double occupancyRate;
    private Long canceledAppointments;

    private int totalProducts;
    private double totalProductsValue;
    private int lowStock;
    private int totalMovements;
}
