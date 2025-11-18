package com.el_buen_corte.el_buen_corte.summary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BusinessSummaryController {

    private final BusinessSummaryService businessSummaryService;

    @GetMapping("/api/resumen")
    public BusinessSummaryDTO getBusinessSummary() {
        return businessSummaryService.getSummary();
    }
}
