package com.el_buen_corte.el_buen_corte.auth;

import com.el_buen_corte.el_buen_corte.Stylist.HairdresserRole;
import com.el_buen_corte.el_buen_corte.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private HairdresserRole hairdresserRole;
    private String email;
    private String phoneNumber;
    private LocalTime workingHoursStart;
    private LocalTime workingHoursFinish;
    private List<String> specialties;
    private Role role;
    private String password;
}
