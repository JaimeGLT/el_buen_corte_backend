package com.el_buen_corte.el_buen_corte.service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

        return toResponse(newService);
    }

    public List<HairServiceResponse> getAllServices() {
        List<HairService> services = hairRepository.findAll();
        return services.stream()
                .map(service -> this.toResponse(service))
                .toList();
    }

    private HairServiceResponse toResponse(HairService hairService) {
        return HairServiceResponse.builder()
                .id(hairService.getId())
                .name(hairService.getName())
                .description(hairService.getDescription())
                .type(hairService.getType())
                .price(hairService.getPrice())
                .duration(hairService.getDuration())
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

        hairRepository.save(hairService);
        return toResponse(hairService);
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
