package com.el_buen_corte.el_buen_corte.cita;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    SELECT COALESCE(SUM(a.service.price), 0)
    FROM Cita a
    WHERE a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
      AND a.date >= :startDate
      AND a.date <= :endDate
  """)
  Double calculateTotalIncome(@Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);


  @Query("""
      SELECT c.stylist.firstName, c.stylist.lastName, c.stylist.hairdresserRole,
            AVG(c.service.price), SUM(c.service.price), COUNT(c)
      FROM Cita c
      GROUP BY c.stylist.id, c.stylist.firstName, c.stylist.lastName, c.stylist.hairdresserRole
  """)
  List<Object[]> findHairdresserPerformanceRaw();

  @Query("""
      SELECT a.service.id, a.service.name,
            COUNT(a), COALESCE(SUM(a.service.price), 0)
      FROM Cita a
      WHERE a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
        AND a.date >= :startDate
        AND a.date <= :endDate
      GROUP BY a.service.id, a.service.name
      ORDER BY a.service.name
  """)
  List<Object[]> countServicesUsedInMonth(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);



  @Query("""
      SELECT a.date, COUNT(a)
      FROM Cita a
      WHERE a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
        AND a.date >= :startDate
        AND a.date <= :endDate
      GROUP BY a.date
      ORDER BY a.date
  """)
  List<Object[]> countTotalServicesPerDayThisWeek(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);
                                                  
  @Query("""
      SELECT a.client.id, 
            a.client.firstName, 
            a.client.lastName, 
            COUNT(a)
      FROM Cita a
      WHERE a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
        AND a.date >= :startDate
        AND a.date <= :endDate
      GROUP BY a.client.id, a.client.firstName, a.client.lastName
      ORDER BY COUNT(a) DESC
  """)
  List<Object[]> countAppointmentsPerClientThisWeek(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate
  );

    @Query("""
    SELECT COUNT(a)
    FROM Cita a
    WHERE a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
      AND a.date >= :startDate
      AND a.date <= :endDate
    """)
        Long countAllServicesThisMonth(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

        @Query("""
        SELECT COALESCE(AVG(a.service.price), 0)
        FROM Cita a
        WHERE a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
          AND a.date >= :startDate
          AND a.date <= :endDate
    """)
        Double calculateAveragePriceThisMonth(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    @Query("""
    SELECT COALESCE(SUM(a.service.price), 0)
    FROM Cita a
    WHERE a.service.id = :serviceId
      AND a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
      AND a.date >= :startDate
      AND a.date <= :endDate
""")
    Double calculateIncomeThisMonthForService(@Param("serviceId") Long serviceId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    @Query("""
    SELECT a.date AS day, COUNT(a) AS total
    FROM Cita a
    WHERE a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
      AND a.date BETWEEN :startDate AND :endDate
    GROUP BY a.date
    ORDER BY a.date
""")
    List<Object[]> countCitasPerDayThisWeek(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    @Query("""
    SELECT a.service.name AS serviceName,
           COUNT(a) AS totalUsed,
           COALESCE(SUM(a.service.price), 0) AS totalGenerated
    FROM Cita a
    WHERE a.status <> com.el_buen_corte.el_buen_corte.cita.Status.CANCELADO
    GROUP BY a.service.id, a.service.name
    ORDER BY totalGenerated DESC
""")
    List<Object[]> getServiceUsageAndIncomeAllTime();



}
