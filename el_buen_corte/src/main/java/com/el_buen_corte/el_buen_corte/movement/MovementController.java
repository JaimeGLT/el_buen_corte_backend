package com.el_buen_corte.el_buen_corte.movement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/movement")
@RequiredArgsConstructor
public class MovementController {
    private final MovementService movementService;

    @PostMapping
    public ResponseEntity<MovementResponse> createMovement(@RequestBody MovementRequest request) {
        return ResponseEntity.ok(movementService.createMovement(request));
    }

    @GetMapping
    public ResponseEntity<List<MovementResponse>> getAllMovements() {
        return ResponseEntity.ok(movementService.getAllMovements());
    }
    
    
}
