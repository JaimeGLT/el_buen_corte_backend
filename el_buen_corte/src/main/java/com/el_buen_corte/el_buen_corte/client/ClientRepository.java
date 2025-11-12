package com.el_buen_corte.el_buen_corte.client;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("""
                SELECT count(c)
                FROM Client c
            """)
    Long countAllClients();

}
