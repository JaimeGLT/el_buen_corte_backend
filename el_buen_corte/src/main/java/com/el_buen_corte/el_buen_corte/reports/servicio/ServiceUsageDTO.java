package com.el_buen_corte.el_buen_corte.reports.servicio;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceUsageDTO {
    private Long serviceId;
    private String serviceName;
    private int count;
    private Double totalIncome;
}

