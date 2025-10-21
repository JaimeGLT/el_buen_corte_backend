package com.el_buen_corte.el_buen_corte.reports.financiero;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/financiero")
public class FinancieroController {
    
    private final FinancieroService financieroService;

    @GetMapping("/today")
    public ResponseEntity<FinancieroResponse> earningsVsExpensesDay() {
        return ResponseEntity.ok(financieroService.earningsVsExpensesDay());
    }

    @GetMapping("/week")
    public ResponseEntity<FinancieroResponse> earningsVsExpensesWeek() {
        return ResponseEntity.ok(financieroService.earningsVsExpensesWeek());
    }

    @GetMapping("/month")
    public ResponseEntity<FinancieroResponse> earningsVsExpensesMonth() {
        return ResponseEntity.ok(financieroService.earningsVsExpensesMonth());
    }

    @GetMapping("/year")
    public ResponseEntity<FinancieroResponse> earningsVsExpensesYear() {
        return ResponseEntity.ok(financieroService.earningsVsExpensesYear());
    }
    
}
