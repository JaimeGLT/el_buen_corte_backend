package com.el_buen_corte.el_buen_corte.Stylist;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaRepository;
import com.el_buen_corte.el_buen_corte.user.Role;
import com.el_buen_corte.el_buen_corte.user.User;
import com.el_buen_corte.el_buen_corte.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HairDresserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CitaRepository citaRepository;

    public HairDresserResponse createHairDresser(HairDresserRequest request) {
        var newHairDresser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .hairdresserRole(request.getHairdresserRole())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .workingHoursStart(request.getWorkingHoursStart())
                .workingHoursFinish(request.getWorkingHoursFinish())
                .specialties(request.getSpecialties())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ESTILISTA)
                .build();
                
        userRepository.save(newHairDresser);

        return toResponse(newHairDresser);
    }

    public List<HairDresserResponse> getAllHairdressers() {
        List<User> hairdressers = userRepository.findAllHairdressers();
        return hairdressers.stream()
                .map(this::toResponse)
                .toList();
    }

    public HairDresserReportsResponse getReports() {

        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);

        Long totalAppointments = citaRepository.countTotalAppointments(startDate, now);
        int totalPersonal = userRepository.findAll().size();
        Double totalIncome = citaRepository.calculateTotalIncome(startDate, now);

        return HairDresserReportsResponse.builder()
                .totalAppointments(totalAppointments)
                .totalPersonal(totalPersonal)
                .totalIncome(totalIncome)
                .build();

    }

    public List<HairdresserPerformanceResponse> getHairdresserPerformance() {
        List<Object[]> raw = citaRepository.findHairdresserPerformanceRaw();

        return raw.stream().map(o -> HairdresserPerformanceResponse.builder()
            .firstName((String) o[0])
            .lastName((String) o[1])
            .hairdresserRole((HairdresserRole) o[2])
            .average(o[3] != null ? (Double) o[3] : 0.0)
            .totalEarnings(o[4] != null ? (Double) o[4] : 0.0)
            .totalServices(o[5] != null ? ((Long) o[5]).intValue() : 0)
            .build()
        ).toList();
    }



    private HairDresserResponse toResponse(User user) {
        return HairDresserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .hairdresserRole(user.getHairdresserRole())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .workingHoursStart(user.getWorkingHoursStart())
                .workingHoursFinish(user.getWorkingHoursFinish())
                .specialties(user.getSpecialties())
                .role(user.getRole())
                .build();
    }

}
