package com.el_buen_corte.el_buen_corte.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  
    @Query("SELECT u FROM User u WHERE u.role = 'ESTILISTA'")
    List<User> findAllHairdressers(); 

}
