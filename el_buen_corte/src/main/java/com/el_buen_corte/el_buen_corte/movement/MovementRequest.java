package com.el_buen_corte.el_buen_corte.movement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementRequest {
    private Integer quantity;
    private MovementType movementType;
    private String reason;
    private Long product;
}
