package com.el_buen_corte.el_buen_corte.movement;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
