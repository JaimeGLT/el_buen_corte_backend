package com.el_buen_corte.el_buen_corte.movement;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.el_buen_corte.el_buen_corte.chat.dto.VentasHoyDTO;

public interface MovementRepository extends JpaRepository<Movement, Long> {
        @Query("""
                        SELECT COALESCE(SUM(a.product.price * a.quantity), 0)
                        FROM Movement a
                        WHERE a.movementType = com.el_buen_corte.el_buen_corte.movement.MovementType.ENTRADA
                          AND a.movementDate >= :startDate
                          AND a.movementDate <= :endDate
                          """)
        Double calculateTotalExpenses(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("""
                        SELECT COALESCE(SUM(a.product.price * a.quantity), 0)
                        FROM Movement a
                        WHERE a.movementType = com.el_buen_corte.el_buen_corte.movement.MovementType.SALIDA
                          AND a.movementDate >= :startDate
                          AND a.movementDate <= :endDate
                          """)
        Double calculateTotalIncomeMovement(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("""
                            SELECT a
                            FROM Movement a
                            WHERE
                                 a.movementDate >= :startDate
                              AND a.movementDate <= :endDate
                        """)
        List<Movement> movementsByDate(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("""
                            SELECT MONTH(m.movementDate) AS month, SUM(m.quantity * m.product.price) AS total
                            FROM Movement m
                            WHERE m.movementType = 'ENTRADA' AND m.movementDate BETWEEN :startDate AND :endDate
                            GROUP BY MONTH(m.movementDate)
                            ORDER BY MONTH(m.movementDate)
                        """)
        List<Object[]> getMonthlyExpenses(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("""
                            SELECT MONTH(m.movementDate) AS month, SUM(m.quantity * m.product.price) AS total
                            FROM Movement m
                            WHERE m.movementType = 'SALIDA' AND m.movementDate BETWEEN :startDate AND :endDate
                            GROUP BY MONTH(m.movementDate)
                            ORDER BY MONTH(m.movementDate)
                        """)
        List<Object[]> getMonthlyIncomes(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("""
                        SELECT new com.el_buen_corte.el_buen_corte.chat.dto.VentasHoyDTO(
                            COALESCE(SUM(m.product.price * m.quantity), 0),
                            COALESCE(SUM(m.quantity), 0)
                        ) FROM Movement m
                        WHERE m.movementType = 'SALIDA'
                        AND m.movementDate >= :startDate
                        AND m.movementDate <= :endDate
                        """)
        VentasHoyDTO ventasHoy(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(m.quantity * p.price) FROM Movement m JOIN m.product p " +
                        "WHERE m.movementType = 'ENTRY' AND m.movementDate BETWEEN :inicio AND :fin")
        Double calcularGastosPorEntradas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

        // REGLA 2: Ingresos extra = Movimientos de SALIDA * Precio (si aplica)
        @Query("SELECT SUM(m.quantity * p.price) FROM Movement m JOIN m.product p " +
                        "WHERE m.movementType = 'EXIT' AND m.movementDate BETWEEN :inicio AND :fin")
        Double calcularIngresosPorSalidas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}
