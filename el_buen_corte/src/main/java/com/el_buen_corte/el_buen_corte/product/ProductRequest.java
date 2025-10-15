package com.el_buen_corte.el_buen_corte.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String brand;
    private Integer initialStock;
    private Integer minimumStock;
    private String supplier;
    private Long category;
}
