package com.el_buen_corte.el_buen_corte.chat;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gemini")
@CrossOrigin(origins = "*")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // 1. Cambiamos a GET
    @GetMapping("/consultar")
    // 2. Usamos @RequestParam.
    // Esto espera algo como: .../consultar?prompt=hola
    public ResponseEntity<Map<String, String>> consultarIa(@RequestParam("prompt") String mensajeUsuario) {

        System.out.println("DEBUG GET - Prompt recibido: " + mensajeUsuario);

        // Validación básica
        if (mensajeUsuario == null || mensajeUsuario.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("respuesta", "El prompt llegó vacío."));
        }

        // Limpieza básica por si Voiceflow envía comillas extra
        mensajeUsuario = mensajeUsuario.replace("\"", "").trim();

        // 3. Llamar al orquestador (igual que antes)
        String respuestaIa = geminiService.orquestarConsulta(mensajeUsuario);

        return ResponseEntity.ok(Map.of("respuesta", respuestaIa));
    }

}
