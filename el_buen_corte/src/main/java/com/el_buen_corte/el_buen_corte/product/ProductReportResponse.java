package com.el_buen_corte.el_buen_corte.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReportResponse {
    private int totalProducts;
    private int totalLowStock;
    private double totalValue;
    private int totalMovementsToday;
}
