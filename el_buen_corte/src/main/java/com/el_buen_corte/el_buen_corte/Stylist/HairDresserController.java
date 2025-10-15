package com.el_buen_corte.el_buen_corte.Stylist;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/hairdresser")
@RequiredArgsConstructor
public class HairDresserController {
    
    private final HairDresserService service;

    @PostMapping
    public ResponseEntity<HairDresserResponse> createHairDresser(@RequestBody HairDresserRequest request) {
        
        return ResponseEntity.ok(service.createHairDresser(request));
    }
    
    @GetMapping
    public ResponseEntity<List<HairDresserResponse>> getAllHairdressers() {
        
        return ResponseEntity.ok(service.getAllHairdressers());
    }

    @GetMapping("/performance")
    public ResponseEntity<List<HairdresserPerformanceResponse>> getHairdresserPerformance() {
        
        return ResponseEntity.ok(service.getHairdresserPerformance());
    }
    

}
