package com.el_buen_corte.el_buen_corte.reports.financiero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeExpenseResponse {
    private int month;
    private double income;
    private double expense;
}