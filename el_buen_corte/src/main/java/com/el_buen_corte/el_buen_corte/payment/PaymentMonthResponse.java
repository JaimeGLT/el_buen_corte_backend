package com.el_buen_corte.el_buen_corte.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMonthResponse {
    private Double totalCashAmountMonth;
    private Double totalQRAmountMonth;
    private Double totalAmountMonth;
    private Double totalCardAmountMonth;
}
