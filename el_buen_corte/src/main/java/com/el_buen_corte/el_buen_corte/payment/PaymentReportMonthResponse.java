package com.el_buen_corte.el_buen_corte.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReportMonthResponse {
    private int totalTransactionsMonth;
    private Double totalAmountMonth;
    private Double averageDaily;
    private Double totalCash;
    private Double totalDigital;
}
