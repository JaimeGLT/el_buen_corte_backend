package com.el_buen_corte.el_buen_corte.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientMetricsDTO {
    // Total Clientes
    private long totalClients;
    private long newClientsThisMonth; // El "+18 este mes"

    // VIP
    private long vipClients;
    private double vipPercentage; // El "17% del total"

    // Nuevos (Crecimiento)
    private long newClientsCurrentMonth; // Redundante con arriba, pero para el bloque "Nuevos"
    private double growthPercentage; // El "+25% vs mes anterior"

    // Retención
    private double retentionRate; // El "87%"
}