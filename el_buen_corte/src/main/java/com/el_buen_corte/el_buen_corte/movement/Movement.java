package com.el_buen_corte.el_buen_corte.movement;

import java.time.LocalDate;

import com.el_buen_corte.el_buen_corte.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movement")
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private MovementType movementType;
    private LocalDate movementDate;
    private String reason;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
}
