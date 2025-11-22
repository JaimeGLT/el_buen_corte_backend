package com.el_buen_corte.el_buen_corte.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("""
                SELECT count(c)
                FROM Client c
            """)
    Long countAllClients();

    Long countByIdNotNull();

    List<Client> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String n1, String n2,
            Pageable pageable);

    @Query("SELECT c FROM Client c WHERE c.lastAppointment >= :fechaLimite")
    List<Client> findActiveClients(@Param("fechaLimite") LocalDate fechaLimite);

    @Query("""
            SELECT COUNT(c)
            FROM Client c WHERE
              c.createdAt >= :startDate
              AND c.createdAt <= :endDate
            """)
    Long countAllClientsByDate();

    @Query("SELECT COUNT(c) FROM Client c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    long countNewClientsBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 3. Clientes VIP (Ejemplo: Clientes con más de 5 citas completadas)
    // Nota: Ajusta el status 'COMPLETED' a como se llame en tu Enum Status real
    @Query("SELECT COUNT(c) FROM Client c WHERE (SELECT COUNT(a) FROM Cita a WHERE a.client = c AND a.status = Status.COMPLETADO) >= :minAppointments")
    long countVipClients(@Param("minAppointments") int minAppointments);

    // 4. Clientes Recurrentes (Para retención: Clientes con más de 1 cita)
    @Query("SELECT COUNT(c) FROM Client c WHERE (SELECT COUNT(a) FROM Cita a WHERE a.client = c AND a.status = Status.COMPLETADO) > 1")
    long countRecurringClients();

}
