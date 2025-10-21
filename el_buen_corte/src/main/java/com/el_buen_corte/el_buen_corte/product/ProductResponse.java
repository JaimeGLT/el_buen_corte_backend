package com.el_buen_corte.el_buen_corte.product;

import java.time.LocalDate;

import com.el_buen_corte.el_buen_corte.category.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String brand;
    private Integer initialStock;
    private LocalDate creationDate;
    private Integer minimumStock;
    private Double price;
    private String supplier;
    private Category category;
}
