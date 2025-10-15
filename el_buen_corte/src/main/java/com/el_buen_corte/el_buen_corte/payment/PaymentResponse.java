package com.el_buen_corte.el_buen_corte.payment;

import java.time.LocalDateTime;

import com.el_buen_corte.el_buen_corte.client.Client;
import com.el_buen_corte.el_buen_corte.service.HairService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long id;
    private Double amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;
    private Client client;
    private HairService service;
}
