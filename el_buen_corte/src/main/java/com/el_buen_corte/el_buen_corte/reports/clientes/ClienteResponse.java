package com.el_buen_corte.el_buen_corte.reports.clientes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer totalAppointments;
}
