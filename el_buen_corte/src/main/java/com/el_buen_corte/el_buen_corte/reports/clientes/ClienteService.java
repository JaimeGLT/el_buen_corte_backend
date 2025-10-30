package com.el_buen_corte.el_buen_corte.reports.clientes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import com.el_buen_corte.el_buen_corte.client.ClientRepository;
import org.springframework.stereotype.Service;
import com.el_buen_corte.el_buen_corte.cita.CitaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {
    
    private final CitaRepository citaRepository;
    private final ClientRepository clientRepository;

    public List<ClienteResponse> getClientReport() {

        List<Object[]> clients = citaRepository.countAppointmentsAndTotalSpentPerClientAllTime();
        return clients.stream()
            .map(this::toResponse2)
            .toList();
    }


    public ClienteReportResponse generalReportsMonth() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfMonth(1);
        LocalDate endDate = today;

        long totalClients = clientRepository.countAllClients();
        int newClients = citaRepository.findNewClientsThisMonth(startDate, endDate).size();
        int totalVisits = citaRepository.countAppointmentsPerClientThisWeek(startDate, endDate)
                .stream()
                .mapToInt(obj -> ((Long) obj[3]).intValue())
                .sum();

        return ClienteReportResponse.builder()
                .newClients(newClients )
                .totalClients(totalClients)
                .totalVisits(totalVisits)
                .build();
    }

    public ClienteReportResponse generalReportsWeek() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(DayOfWeek.MONDAY);
        LocalDate endDate = today;

        long totalClients = clientRepository.countAllClients();
        int newClients = citaRepository.findNewClientsThisMonth(startDate, endDate).size();
        int totalVisits = citaRepository.countAppointmentsPerClientThisWeek(startDate, endDate)
                .stream()
                .mapToInt(obj -> ((Long) obj[3]).intValue())
                .sum();

        return ClienteReportResponse.builder()
                .newClients(newClients )
                .totalClients(totalClients)
                .totalVisits(totalVisits)
                .build();
    }

    public ClienteReportResponse generalReportsYear() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfYear(1);
        LocalDate endDate = today;

        long totalClients = clientRepository.countAllClients();
        int newClients = citaRepository.findNewClientsThisMonth(startDate, endDate).size();
        int totalVisits = citaRepository.countAppointmentsPerClientThisWeek(startDate, endDate)
                .stream()
                .mapToInt(obj -> ((Long) obj[3]).intValue())
                .sum();

        return ClienteReportResponse.builder()
                .newClients(newClients )
                .totalClients(totalClients)
                .totalVisits(totalVisits)
                .build();
    }

    private ClienteResponse toResponse(Object[] row) {
        return ClienteResponse.builder()
            .id((Long) row[0])
            .firstName((String) row[1])
            .lastName((String) row[2])
            .totalAppointments(((Number) row[3]).intValue())
            .build();
    }

    private ClienteResponse toResponse2(Object[] client) {
        Long id = (Long) client[0];
        String firstName = (String) client[1];
        String lastName = (String) client[2];
        int totalAppointments = ((Number) client[3]).intValue();
        Double totalSpent = (Double) client[4];

        return new ClienteResponse(id, firstName, lastName, totalAppointments, totalSpent);
    }


}
