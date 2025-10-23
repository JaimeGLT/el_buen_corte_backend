package com.el_buen_corte.el_buen_corte.service;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HairServiceReportResponse {
    private long totalActiveServices;
    private long servicesThisMonth;
    private double totalIncomeThisMonth;
    private double averagePricePerService;
}
