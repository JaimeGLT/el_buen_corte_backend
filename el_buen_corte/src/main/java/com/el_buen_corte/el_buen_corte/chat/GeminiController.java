package com.el_buen_corte.el_buen_corte.chat;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gemini")
@CrossOrigin(origins = "*")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService, ChatService chatService) {
        this.geminiService = geminiService;
    }

    // Usamos GET para que puedas probarlo RÁPIDO en el navegador
    @GetMapping("/chat")
    public String chatear(@RequestParam String prompt) {
        return geminiService.llamarGeminiAPI(prompt);
    }

    @PostMapping("/consultar")
    public ResponseEntity<Map<String, String>> consultarIa(@RequestBody Map<String, String> request) {

        // saca el mensaje del usuario por body
        String mensajeUsuario = request.get("message_user");

        if (mensajeUsuario == null || mensajeUsuario.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("respuesta", "El mensaje no puede estar vacío"));
        }

        // 2. Llamar al orquestador
        String respuestaIa = geminiService.orquestarConsulta(mensajeUsuario);

        // 3. Retornar JSON limpio
        return ResponseEntity.ok(Map.of("respuesta", respuestaIa));
    }
}
