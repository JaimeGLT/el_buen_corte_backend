package com.el_buen_corte.el_buen_corte.service;

import com.el_buen_corte.el_buen_corte.cita.Cita;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "service")
public class HairService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String type;
    private Double price;
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    private Duration duration;
    @OneToMany(mappedBy = "service")
    @JsonIgnore
    private List<Cita> citas;
    


}
