package com.el_buen_corte.el_buen_corte.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface HairRepository extends JpaRepository<HairService, Long> {
    @Query("SELECT COUNT(h) FROM HairService h WHERE h.active = true")
    long countActiveServices();


}
