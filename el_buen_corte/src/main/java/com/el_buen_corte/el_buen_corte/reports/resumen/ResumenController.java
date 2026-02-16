package com.el_buen_corte.el_buen_corte.reports.resumen;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/resumen")
public class ResumenController {

    private final ResumenService resumenService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<ResumenResponse> getReportsResumen() {
        return ResponseEntity.ok(resumenService.getResumenReports());
    }

}
