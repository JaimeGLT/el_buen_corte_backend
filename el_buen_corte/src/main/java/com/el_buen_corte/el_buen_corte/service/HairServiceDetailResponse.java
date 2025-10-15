package com.el_buen_corte.el_buen_corte.service;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HairServiceDetailResponse {
    private Long id;
    private String name;
    private String description;
    private String type;
    private Double price;
    private Duration duration; 
    private Integer servicesThisMonth;
    private Integer incomeGenerated;
}
