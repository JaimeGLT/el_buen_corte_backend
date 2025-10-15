package com.el_buen_corte.el_buen_corte.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Double amount;
    private PaymentMethod paymentMethod;
    private Long clientId;
    private Long serviceId;
}
