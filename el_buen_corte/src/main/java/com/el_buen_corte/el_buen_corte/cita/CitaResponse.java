package com.el_buen_corte.el_buen_corte.cita;

import com.el_buen_corte.el_buen_corte.client.Client;
import com.el_buen_corte.el_buen_corte.service.HairService;
import com.el_buen_corte.el_buen_corte.user.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class CitaResponse {
    private LocalDate date;
    private LocalTime time;
    private Status status;
    private Client client;
    private HairService service;
    private User stylist;
    private String notes;
}
