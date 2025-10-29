package com.el_buen_corte.el_buen_corte.reports.servicio;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/report/service")
@RequiredArgsConstructor
public class ServiceReportController {
    private final ServiceReportService service;

    @GetMapping("/total_services")
    public ResponseEntity<List<ServiceUsageResponse>> serviceUseInMonth() {
        return ResponseEntity.ok(service.getServiceUsageIncomeAllTime());
    }

    @GetMapping("/total-per-week")
    public ResponseEntity<List<DailyCitasResponse>> totalServicesPerDayWeek() {
        return ResponseEntity.ok(service.getWeeklyCitasReport());
    }
}
