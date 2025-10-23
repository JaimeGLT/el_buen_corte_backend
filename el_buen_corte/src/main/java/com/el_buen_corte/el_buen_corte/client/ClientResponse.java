package com.el_buen_corte.el_buen_corte.client;

import java.util.List;

import com.el_buen_corte.el_buen_corte.cita.CitaResumenResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String observations;
    private String lastVisit;
    private List<CitaResumenResponse> citas;
}
