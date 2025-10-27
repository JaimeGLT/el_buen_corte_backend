package com.el_buen_corte.el_buen_corte.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HairServiceResponse {
    private Long id;
    private String name;
    private String description;
    private String type;
    private Double price;
    private long duration;
    private Integer servicesThisMonth;
    private Double incomeGenerated;
    private boolean active;
    private double popularityPercentage;
}
