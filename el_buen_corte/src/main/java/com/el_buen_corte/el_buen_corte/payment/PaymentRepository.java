package com.el_buen_corte.el_buen_corte.payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(
        value = """
            SELECT * FROM payment p
            WHERE p.payment_date >= CURRENT_DATE
            AND p.payment_date < CURRENT_DATE + INTERVAL '1 day'
        """,
        nativeQuery = true
    )
    List<Payment> findAllByPaymentDateIsToday();

    @Query(
            value = """
            SELECT COALESCE(SUM(p.amount), 0) FROM Payment p
            WHERE p.payment_date >= CURRENT_DATE
            AND p.payment_date < CURRENT_DATE + INTERVAL '1 day'
        """,
            nativeQuery = true
    )
    Double totalAmountToday();

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.paymentDate >= :startDate
      AND p.paymentDate < :endDate
      AND p.paymentMethod = :method
    """)
    Double totalAmountByMethodBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("method") PaymentMethod method
    );

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.paymentDate >= :startDate
      AND p.paymentDate < :endDate
    """)
    Double totalAmountByDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query(
        value = """
            SELECT * FROM payment p
            WHERE DATE_TRUNC('month', p.payment_date) = DATE_TRUNC('month', CURRENT_DATE)
        """,
        nativeQuery = true
    )
    List<Payment> findAllByPaymentDateIsThisMonth();

    @Query("""
        SELECT MONTH(p.paymentDate) AS month, SUM(p.amount) AS total
        FROM Payment p
        WHERE p.paymentDate BETWEEN :startDate AND :endDate
        GROUP BY MONTH(p.paymentDate)
        ORDER BY MONTH(p.paymentDate)
    """)
    List<Object[]> getMonthlyIncome(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);
    
}
