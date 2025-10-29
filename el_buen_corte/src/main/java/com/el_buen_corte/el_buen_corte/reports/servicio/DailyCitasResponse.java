package com.el_buen_corte.el_buen_corte.reports.servicio;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyCitasResponse {
    private String dayName;   // Nombre del día: "Lunes", "Martes", ...
    private int totalCitas;
}

