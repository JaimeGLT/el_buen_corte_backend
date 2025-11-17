package com.el_buen_corte.el_buen_corte.ai;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('RECEPCIONISTA') or hasRole('ESTILISTA')")
    public Mono<ResponseEntity<AiResponse>> chat(@RequestBody AiRequest request) {
        // Respuesta reactiva (Mono) para integración con WebFlux
        return Mono.fromSupplier(() -> {
            String answer = aiService.generateResponse(request.getQuestion());
            return ResponseEntity.ok(new AiResponse(answer));
        });
    }
}
