package com.el_buen_corte.el_buen_corte.cita;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cita")
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<CitaResponse> createCita(@RequestBody CitaRequest request){
        return ResponseEntity.ok(citaService.createCita(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaResponse> updateCita(@RequestBody CitaRequest request, @PathVariable Long id){
        return ResponseEntity.ok(citaService.updateCita(request, id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<CitaResponse> cancelCita(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.cancelCita(id));
    }

    @GetMapping
    public ResponseEntity<List<CitaResponse>> getAllCitas() {
        return ResponseEntity.ok(citaService.getAllCitas());
    }
}
