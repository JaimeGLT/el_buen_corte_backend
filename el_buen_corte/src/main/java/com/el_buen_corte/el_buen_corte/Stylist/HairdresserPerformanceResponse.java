package com.el_buen_corte.el_buen_corte.Stylist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HairdresserPerformanceResponse {
    private String firstName;
    private String lastName;
    private HairdresserRole hairdresserRole;
    private Double average;
    private Double totalEarnings;
    private Integer totalServices;

}
