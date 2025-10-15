package com.el_buen_corte.el_buen_corte.user;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.el_buen_corte.el_buen_corte.Stylist.HairdresserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "staff")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "hairdresser_role")
    @Enumerated(EnumType.STRING)
    private HairdresserRole hairdresserRole;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name="phone_number",unique = true, nullable = false)
    private String phoneNumber;
    @Column(name = "working_hours_start", nullable = false)
    private LocalTime workingHoursStart;
    @Column(name = "working_hours_finish", nullable = false)
    private LocalTime workingHoursFinish;
    @Column(nullable = false)
    private List<String> specialties;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }
}
