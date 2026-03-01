package com.el_buen_corte.el_buen_corte.gastosOperativos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GastosOperativosRepository extends JpaRepository<GastosOperativos, Long> {
        // En GastosOperativosRepository
        @Query("SELECT COALESCE(SUM(g.monto), 0) FROM GastosOperativos g WHERE g.fecha BETWEEN :startDate AND :endDate")
        Double sumTotalMontoByDateRange(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        // Para el reporte anual (agrupado por mes)
        @Query("SELECT MONTH(g.fecha) as month, COALESCE(SUM(g.monto), 0) as total FROM GastosOperativos g WHERE g.fecha BETWEEN :startDate AND :endDate GROUP BY MONTH(g.fecha)")
        List<Object[]> getMonthlyGastos(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT COALESCE(SUM(g.monto), 0) FROM GastosOperativos g")
        Double totalExpenses();

        // En GastosOperativosRepository.java

        @Query("SELECT COALESCE(MAX(g.monto), 0) FROM GastosOperativos g")
        Double findMaxMontoByDateRange();
}
