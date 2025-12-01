package com.el_buen_corte.el_buen_corte.gastosOperativos;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gastos-operativos")
@RequiredArgsConstructor
public class GastosOperativosController {

    private final GastosOperativosService gastosOperativosService;

    @PostMapping
    public ResponseEntity<GastosOperativos> createGastosOperativos(
            @Valid @RequestBody GastosOperativosRequest gastosOperativosRequest) {
        return ResponseEntity.ok(gastosOperativosService.createGastosOperativos(gastosOperativosRequest));
    }

    @GetMapping
    public ResponseEntity<List<GastosOperativos>> getAllGastosOperativos() {
        return ResponseEntity.ok(gastosOperativosService.getAllGastosOperativos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GastosOperativos> updateGastosOperativos(
            @PathVariable Long id,
            @RequestBody GastosOperativosRequest gastosOperativosRequest) {

        return ResponseEntity.ok(gastosOperativosService.updateGastosOperativos(id, gastosOperativosRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGastosOperativos(@PathVariable Long id) {
        gastosOperativosService.deleteGastosOperativos(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reports")
    public ResponseEntity<GastosReportResponse> reports() {
        return ResponseEntity.ok(gastosOperativosService.reports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastosOperativos> getGastoOperativo(@PathVariable Long id) {
        return ResponseEntity.ok(gastosOperativosService.getGastoOperativoById(id));
    }

}
