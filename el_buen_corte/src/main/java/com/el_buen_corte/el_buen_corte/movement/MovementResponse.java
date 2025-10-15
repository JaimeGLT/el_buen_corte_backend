package com.el_buen_corte.el_buen_corte.movement;

import java.time.LocalDate;

import com.el_buen_corte.el_buen_corte.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementResponse {
    private Long id;
    private Integer quantity;
    private MovementType movementType;
    private LocalDate movementDate;
    private String reason;
    private Product product;
}
