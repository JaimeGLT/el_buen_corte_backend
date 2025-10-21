package com.el_buen_corte.el_buen_corte.product;

import java.time.LocalDate;
import java.util.List;

import com.el_buen_corte.el_buen_corte.category.Category;
import com.el_buen_corte.el_buen_corte.movement.Movement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    @Column(name = "initial_stock")
    private Integer initialStock;
    @Column(name = "minimum_stock")
    private Integer minimumStock;
    private String supplier;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "product")
    private List<Movement> movements;
}
