package com.el_buen_corte.el_buen_corte.Stylist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HairDresserReportsResponse {
    private int totalPersonal;
    private Double totalIncome;
    private Long totalAppointments;
}
