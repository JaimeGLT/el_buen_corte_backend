package com.el_buen_corte.el_buen_corte.reports.servicio;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceUsageResponse {
    private String serviceName;
    private long totalUsed;
    private double percentage;
    private double totalGenerated;
}
