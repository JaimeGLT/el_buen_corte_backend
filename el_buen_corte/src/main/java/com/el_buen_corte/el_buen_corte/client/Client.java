package com.el_buen_corte.el_buen_corte.client;

import com.el_buen_corte.el_buen_corte.cita.Cita;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    @Column(name = "last_appointment")
    private LocalDate lastAppointment;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String observations;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;
    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Cita> citas;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

}
