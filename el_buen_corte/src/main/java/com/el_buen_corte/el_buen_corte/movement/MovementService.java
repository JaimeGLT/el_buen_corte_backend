package com.el_buen_corte.el_buen_corte.movement;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.product.Product;
import com.el_buen_corte.el_buen_corte.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovementService {
    
    private final MovementRepository movementRepository;
    private final ProductRepository productRepository;

    public MovementResponse createMovement(MovementRequest request) {

        Product product = productRepository.findById(request.getProduct())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (request.getMovementType() == MovementType.ENTRADA) {
            product.setInitialStock(product.getInitialStock() + request.getQuantity());
        } else if (request.getMovementType() == MovementType.SALIDA) {
            if (product.getInitialStock() < request.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }
            product.setInitialStock(product.getInitialStock() - request.getQuantity());
        }

        productRepository.save(product);

        Movement movement = Movement.builder()
            .product(product)
            .quantity(request.getQuantity())
            .movementType(request.getMovementType())
            .movementDate(LocalDate.now())
            .reason(request.getReason())
            .build();

        movementRepository.save(movement);

        return toResponse(movement);
    }

    public List<MovementResponse> getAllMovements() {
        List<Movement> movements = movementRepository.findAll();
        return movements.stream()
                .map(this::toResponse)
                .toList();
    }

    public MovementResponse toResponse(Movement movement) {
        return MovementResponse.builder()
                .id(movement.getId())
                .product(movement.getProduct())
                .quantity(movement.getQuantity())
                .movementType(movement.getMovementType())
                .movementDate(movement.getMovementDate())
                .reason(movement.getReason())
                .build();
    }

}
