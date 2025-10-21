package com.el_buen_corte.el_buen_corte.reports.servicio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceReportService {
    
    private final CitaRepository citaRepository;

    public List<ServiceUsageDTO> servicesUsedInMonth() {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now();

        List<Object[]> rawData = citaRepository.countServicesUsedInMonth(startDate, endDate);

        return rawData.stream()
            .map(row -> new ServiceUsageDTO(
                (Long) row[0],          // serviceId
                (String) row[1],        // serviceName
                ((Long) row[2]).intValue(), // totalUsed
                (Double) row[3]         // totalIncome
            ))
            .toList();
    }

    public List<DailyTotalServicesDTO> totalServicesPerDayThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

        List<Object[]> rawData = citaRepository.countTotalServicesPerDayThisWeek(startOfWeek, endOfWeek);

        return rawData.stream()
            .map(row -> new DailyTotalServicesDTO(
                (LocalDate) row[0],
                ((Long) row[1]).intValue()
            ))
            .toList();
    }



}
