package com.el_buen_corte.el_buen_corte.reports.clientes;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/client")
public class ClienteController {
    
    private final ClienteService clienteService;

    @GetMapping("/week")
    public ResponseEntity<List<ClienteResponse>> getAppointmentsPerClientThisWeek() {
        return ResponseEntity.ok(clienteService.getWeeklyClientReport());
    }
    
    @GetMapping("/month")
    public ResponseEntity<List<ClienteResponse>> getAppointmentsPerClientThisMonth() {
        return ResponseEntity.ok(clienteService.getMonthClientReport());
    }

    @GetMapping("/year")
    public ResponseEntity<List<ClienteResponse>> getAppointmentsPerClientThisYear() {
        return ResponseEntity.ok(clienteService.getYearClientReport());
    }

    

}
