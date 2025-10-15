package com.el_buen_corte.el_buen_corte.cita;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class CitaRequest {
    private LocalDate date;
    private LocalTime time;
    private Long client;
    private Long service;
    private Status status;
    private Long stylist;
    private String notes;
}
