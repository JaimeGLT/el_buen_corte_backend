package com.el_buen_corte.el_buen_corte.reports.servicio;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceReportService {
    
    private final CitaRepository citaRepository;

    public List<ServiceUsageResponse> getServiceUsageIncomeAllTime() {

        List<Object[]> rawData = citaRepository.getServiceUsageAndIncomeAllTime();

        // Total de ingresos generados por todos los servicios
        double totalIncome = rawData.stream()
                .mapToDouble(r -> ((Number) r[2]).doubleValue())
                .sum();

        List<ServiceUsageResponse> report = new ArrayList<>();
        for (Object[] r : rawData) {
            String serviceName = (String) r[0];
            long totalUsed = ((Number) r[1]).longValue();
            double totalGenerated = ((Number) r[2]).doubleValue();

            // Porcentaje basado en ingresos, no cantidad de citas
            double percentage = totalIncome > 0 ? (totalGenerated * 100.0 / totalIncome) : 0.0;

            report.add(ServiceUsageResponse.builder()
                    .serviceName(serviceName)
                    .totalUsed(totalUsed)
                    .percentage(Math.round(percentage * 100.0) / 100.0)
                    .totalGenerated(Math.round(totalGenerated * 100.0) / 100.0)
                    .build());
        }

        return report;
    }


    public List<DailyCitasResponse> getWeeklyCitasReport() {

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

        List<Object[]> rawData = citaRepository.countCitasPerDayThisWeek(startOfWeek, endOfWeek);

        List<DailyCitasResponse> report = new ArrayList<>();
        Locale spanish = new Locale("es", "ES"); // Para nombres de días en español

        for (int i = 0; i < 7; i++) {
            LocalDate day = startOfWeek.plusDays(i);

            int totalCitas = rawData.stream()
                    .filter(r -> ((LocalDate) r[0]).isEqual(day))
                    .map(r -> ((Number) r[1]).intValue())
                    .findFirst()
                    .orElse(0);

            DayOfWeek dayOfWeek = day.getDayOfWeek();
            String dayName = dayOfWeek.getDisplayName(TextStyle.FULL, spanish); // "lunes", "martes", ...

            // Capitalizamos la primera letra
            dayName = dayName.substring(0, 1).toUpperCase() + dayName.substring(1);

            report.add(DailyCitasResponse.builder()
                    .dayName(dayName)
                    .totalCitas(totalCitas)
                    .build());
        }

        return report;
    }
}
