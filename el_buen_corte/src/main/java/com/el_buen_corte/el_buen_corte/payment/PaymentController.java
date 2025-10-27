package com.el_buen_corte.el_buen_corte.payment;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    @GetMapping("/today")
    public ResponseEntity<List<PaymentResponse>> getAllPaymentsFromToday() {
        return ResponseEntity.ok(paymentService.getAllPaymentsFromToday());
    }

    @GetMapping("/month")
    public ResponseEntity<PaymentMonthResponse> getAllPaymentsFromLastMonth() {
        return ResponseEntity.ok(paymentService.getAllPaymentsFromLastMonth());

    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/reports_today")
    public ResponseEntity<PaymentReportTodayResponse> getReportsToday() {
        return ResponseEntity.ok(paymentService.reportsToday());
    }

    @GetMapping("/reports_month")
    public ResponseEntity<PaymentReportMonthResponse> getReportsMonth() {
        return ResponseEntity.ok(paymentService.reportsMonth());
    }
    
}
