package com.el_buen_corte.el_buen_corte.reports.clientes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import com.el_buen_corte.el_buen_corte.cita.CitaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {
    
    private final CitaRepository citaRepository;

    public List<ClienteResponse> getWeeklyClientReport() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(DayOfWeek.MONDAY);
        LocalDate endDate = today.with(DayOfWeek.SUNDAY);


        List<Object[]> clients = citaRepository.countAppointmentsPerClientThisWeek(startDate, endDate);
        return clients.stream()
            .map(this::toResponse)
            .toList();
    }

    public List<ClienteResponse> getMonthClientReport() {

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now();


        List<Object[]> clients = citaRepository.countAppointmentsPerClientThisWeek(startDate, endDate);
        return clients.stream()
            .map(this::toResponse)
            .toList();
    }

    public List<ClienteResponse> getYearClientReport() {

        LocalDate startDate = LocalDate.now().withDayOfYear(1);
        LocalDate endDate = LocalDate.now();


        List<Object[]> clients = citaRepository.countAppointmentsPerClientThisWeek(startDate, endDate);
        return clients.stream()
            .map(this::toResponse)
            .toList();
    }

    private ClienteResponse toResponse(Object[] row) {
        return ClienteResponse.builder()
            .id((Long) row[0])
            .firstName((String) row[1])
            .lastName((String) row[2])
            .totalAppointments(((Number) row[3]).intValue())
            .build();
    }

}
