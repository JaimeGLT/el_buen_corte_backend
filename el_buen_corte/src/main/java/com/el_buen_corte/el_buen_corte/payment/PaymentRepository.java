package com.el_buen_corte.el_buen_corte.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
            SELECT * FROM payment p
            WHERE DATE_TRUNC('month', p.payment_date) = DATE_TRUNC('month', CURRENT_DATE)
        """,
        nativeQuery = true
    )
    List<Payment> findAllByPaymentDateIsThisMonth();


    
}
