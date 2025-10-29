package com.el_buen_corte.el_buen_corte.reports.financiero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancieroResponse {
    private Double earnings;
    private Double expenses;
    private Double netProfit;
    private Long totalAppointments;
    private Double averageTicket;
}
