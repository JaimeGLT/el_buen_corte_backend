package com.el_buen_corte.el_buen_corte.reports.servicio;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyTotalServicesDTO {
    private LocalDate date;
    private Integer totalServices;
    
}
