package com.el_buen_corte.el_buen_corte.reports.clientes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteReportResponse {
    private Long totalClients;
    private int newClients;
    private int totalVisits;
}
