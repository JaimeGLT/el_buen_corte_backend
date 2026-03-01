package com.el_buen_corte.el_buen_corte.gastosOperativos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GastosReportResponse {
    private Double totalExpenses;
    private int totalRecords;
    private Double averageExpense;
    private Double majorExpense;
}
