package com.el_buen_corte.el_buen_corte.reports.clientes;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/client")
public class ClienteController {

    private final ClienteService clienteService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/all")
    public ResponseEntity<List<ClienteResponse>> getClients() {
        return ResponseEntity.ok(clienteService.getClientReport());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/general_reports_month")
    public ResponseEntity<ClienteReportResponse> getGeneralReportsMonth() {
        return ResponseEntity.ok(clienteService.generalReportsMonth());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/general_reports_week")
    public ResponseEntity<ClienteReportResponse> getGeneralReportsWeek() {
        return ResponseEntity.ok(clienteService.generalReportsWeek());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/general_reports_year")
    public ResponseEntity<ClienteReportResponse> getGeneralReportsYear() {
        return ResponseEntity.ok(clienteService.generalReportsYear());
    }
}
