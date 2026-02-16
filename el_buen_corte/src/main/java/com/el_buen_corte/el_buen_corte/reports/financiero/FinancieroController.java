package com.el_buen_corte.el_buen_corte.reports.financiero;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/financiero")
public class FinancieroController {

    private final FinancieroService financieroService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/today")
    public ResponseEntity<FinancieroResponse> earningsVsExpensesDay() {
        return ResponseEntity.ok(financieroService.earningsVsExpensesDay());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/week")
    public ResponseEntity<FinancieroResponse> earningsVsExpensesWeek() {
        return ResponseEntity.ok(financieroService.earningsVsExpensesWeek());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/month")
    public ResponseEntity<FinancieroResponse> earningsVsExpensesMonth() {
        return ResponseEntity.ok(financieroService.earningsVsExpensesMonth());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/year")
    public ResponseEntity<FinancieroResponse> earningsVsExpensesYear() {
        return ResponseEntity.ok(financieroService.earningsVsExpensesYear());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/expensesVsIncome")
    public ResponseEntity<List<IncomeExpenseResponse>> earningsVsIncome() {
        return ResponseEntity.ok(financieroService.getYearlyIncomeExpenseReport());
    }
}
