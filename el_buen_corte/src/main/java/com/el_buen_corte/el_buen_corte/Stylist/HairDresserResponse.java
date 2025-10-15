package com.el_buen_corte.el_buen_corte.Stylist;

import java.time.LocalTime;
import java.util.List;

import com.el_buen_corte.el_buen_corte.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HairDresserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private HairdresserRole hairdresserRole;
    private String email;
    private String phoneNumber;
    private LocalTime workingHoursStart;
    private LocalTime workingHoursFinish;
    private List<String> specialties;
    private Role role;

}
