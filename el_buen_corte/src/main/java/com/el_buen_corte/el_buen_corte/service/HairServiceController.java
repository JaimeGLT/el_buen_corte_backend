package com.el_buen_corte.el_buen_corte.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/service")
public class HairServiceController {
    private final HairServiceService service;

    @PostMapping
    public ResponseEntity<HairServiceResponse> createService(@RequestBody HairServiceRequest request) {
        return ResponseEntity.ok(service.createService(request));
    }

    @GetMapping
    public ResponseEntity<List<HairServiceResponse>> getAllServices() {
        return ResponseEntity.ok(service.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HairServiceResponse> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getServiceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HairServiceResponse> updateService(@PathVariable Long id, @RequestBody HairServiceRequest request) {
        return ResponseEntity.ok(service.updateService(id, request));
    }

    @GetMapping("/reports")
    public ResponseEntity<HairServiceReportResponse> getReportsService() {
        return ResponseEntity.ok(service.reports());
    }
}
