package com.el_buen_corte.el_buen_corte.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentReportTodayResponse {
    private Double totalCashAmountToday;
    private Double totalCardAmountToday;
    private Double totalQRAmountToday;
    private Double totalPaymentAmountToday;
    private int totalTransactionsToday;

}
