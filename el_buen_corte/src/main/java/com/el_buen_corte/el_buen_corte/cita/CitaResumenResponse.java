package com.el_buen_corte.el_buen_corte.cita;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitaResumenResponse {
    private Long id;
    private LocalDate date;
    private Double price;
    private String service;
    private String stylist;
}
