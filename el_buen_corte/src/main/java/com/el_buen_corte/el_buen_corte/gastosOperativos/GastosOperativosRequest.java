package com.el_buen_corte.el_buen_corte.gastosOperativos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GastosOperativosRequest {
    private String concepto;
    private Double monto;
    private LocalDate fecha;
}
