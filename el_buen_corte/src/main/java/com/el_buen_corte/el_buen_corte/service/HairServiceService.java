package com.el_buen_corte.el_buen_corte.service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaRepository;

@Service
@RequiredArgsConstructor
public class HairServiceService {

    private final HairRepository hairRepository;
    private final CitaRepository citaRepository;

    public HairServiceResponse createService(HairServiceRequest request) {
        var newService = HairService.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .price(request.getPrice())
                .duration(request.getDuration())
                .build();
                
        hairRepository.save(newService);

        return toResponse(newService, 0.00);
    }

    public List<HairServiceResponse> getAllServices() {
        List<HairService> services = hairRepository.findAll();
        LocalDate start = LocalDate.now().withDayOfMonth(1); // inicio del mes
        LocalDate end = LocalDate.now(); // hoy

        Long totalServices = citaRepository.countAllServicesThisMonth(start, end);
        List<Object[]> servicesUsedThisMonth = citaRepository.countServicesUsedInMonth(start, end);

        // Crear un mapa <serviceId, totalAppointments>
        Map<Long, Long> serviceAppointmentsMap = servicesUsedThisMonth.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[2]
                ));

        // Mapear servicios a HairServiceResponse incluyendo popularidad
        return services.stream()
                .map(service -> {
                    Long appointments = serviceAppointmentsMap.getOrDefault(service.getId(), 0L);
                    Double popularityPercentage = (totalServices != null && totalServices > 0)
                            ? (appointments.doubleValue() / totalServices) * 100
                            : 0.0;

                    return this.toResponse(service, popularityPercentage);
                })
                .toList();
    }


    public HairServiceReportResponse reports() {

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now();

        long totalActiveServices = hairRepository.countActiveServices();
        long totalServicesThisMonth = citaRepository.countAllServicesThisMonth(startDate, endDate);
        double totalIncomeThisMonth = citaRepository.calculateTotalIncome(startDate, endDate);
        double calculateAveragePriceThisMonth = totalIncomeThisMonth / totalServicesThisMonth;

        return HairServiceReportResponse.builder()
                .totalActiveServices(totalActiveServices)
                .totalIncomeThisMonth(totalIncomeThisMonth)
                .servicesThisMonth(totalServicesThisMonth)
                .averagePricePerService(calculateAveragePriceThisMonth)
                .build();
    }

    private HairServiceResponse toResponse(HairService hairService, Double popularityPercentaje) {
        return HairServiceResponse.builder()
                .id(hairService.getId())
                .name(hairService.getName())
                .description(hairService.getDescription())
                .type(hairService.getType())
                .price(hairService.getPrice())
                .duration(hairService.getDuration())
                .active(hairService.isActive())
                .popularityPercentage(popularityPercentaje)
                .build();
    }

    private HairServiceResponse toResponse2(HairService hairService, Integer servicesThisMonth, Double incomeGenerated) {
        return HairServiceResponse.builder()
                .id(hairService.getId())
                .name(hairService.getName())
                .description(hairService.getDescription())
                .type(hairService.getType())
                .servicesThisMonth(servicesThisMonth)
                .incomeGenerated(incomeGenerated)
                .price(hairService.getPrice())
                .duration(hairService.getDuration())
                .active(hairService.isActive())
                .build();
    }

    public HairServiceResponse updateService(Long id, HairServiceRequest request) {
        HairService hairService = hairRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if(request.getName() != null)
            hairService.setName(request.getName());
        if(request.getDescription() != null)
            hairService.setDescription(request.getDescription());
        if(request.getType() != null)
            hairService.setType(request.getType());
        if(request.getPrice() != null)
            hairService.setPrice(request.getPrice());
        if(request.getDuration() != null)
            hairService.setDuration(request.getDuration());

        hairService.setActive(request.isActive());

        hairRepository.save(hairService);
        return toResponse(hairService, 0.00);
    }

    public HairServiceResponse getServiceById(Long id) {
        HairService hairService = hairRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        LocalDate startOfMonth = LocalDate.now().minusMonths(1);

        Integer servicesThisMonth = citaRepository.countNonCancelledAppointmentsThisMonth(id, startOfMonth);
        Double incomeGenerated = citaRepository.calculateIncomeThisMonth(id, startOfMonth);

        return toResponse2(hairService, servicesThisMonth, incomeGenerated);
    }
}
