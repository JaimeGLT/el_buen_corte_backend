package com.el_buen_corte.el_buen_corte.cita;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.el_buen_corte.el_buen_corte.Stylist.HairdresserPerformanceResponse;

public interface CitaRepository extends JpaRepository<Cita, Long> {
@Query("""
        SELECT COUNT(a)
        FROM Cita a
        WHERE a.service.id = :serviceId
          AND a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
          AND a.date >= :startDate
    """)
    Integer countNonCancelledAppointmentsThisMonth(@Param("serviceId") Long serviceId, 
                                                   @Param("startDate") LocalDate startDate);

    @Query("""
        SELECT COALESCE(SUM(a.service.price), 0)
        FROM Cita a
        WHERE a.service.id = :serviceId
          AND a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
          AND a.date >= :startDate
    """)
    Double calculateIncomeThisMonth(@Param("serviceId") Long serviceId, 
                                    @Param("startDate") LocalDate startDate);

  @Query("""
      SELECT c.stylist.firstName, c.stylist.lastName, c.stylist.hairdresserRole,
            AVG(c.service.price), SUM(c.service.price), COUNT(c)
      FROM Cita c
      GROUP BY c.stylist.id, c.stylist.firstName, c.stylist.lastName, c.stylist.hairdresserRole
  """)
  List<Object[]> findHairdresserPerformanceRaw();



}
