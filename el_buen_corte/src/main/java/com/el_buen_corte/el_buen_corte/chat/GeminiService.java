package com.el_buen_corte.el_buen_corte.chat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper; // IMPORTANTE: Para convertir datos a texto

import com.el_buen_corte.el_buen_corte.chat.dto.*;
import com.el_buen_corte.el_buen_corte.client.ClientRepository;
import com.el_buen_corte.el_buen_corte.movement.MovementRepository;
import com.el_buen_corte.el_buen_corte.product.ProductRepository;

@Service
public class GeminiService {

    @Value("${google.ai.key}") // Asegúrate que coincida con application.properties
    private String apiKey;

    @Value("${google.gemini.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final MovementRepository movementRepository;
    private final ObjectMapper objectMapper; // Nueva herramienta

    public GeminiService(ProductRepository productRepository, ClientRepository clientRepository,
            MovementRepository movementRepository, ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.movementRepository = movementRepository;
        this.objectMapper = objectMapper;
    }

    // Método base para llamar a Google (Igual que antes)
    public String llamarGeminiAPI(String prompt) {
        String finalUrl = apiUrl + "?key=" + apiKey;
        Part part = new Part(prompt);
        Content content = new Content(Collections.singletonList(part));
        GeminiRequest request = new GeminiRequest(Collections.singletonList(content));

        try {
            GeminiResponse response = restTemplate.postForObject(finalUrl, request, GeminiResponse.class);
            if (response != null && !response.candidates().isEmpty()) {
                return response.candidates().get(0).content().parts().get(0).text();
            }
            return "No se recibió respuesta de Gemini.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al conectar con Gemini: " + e.getMessage();
        }
    }

    // ================================================================
    // EL ORQUESTADOR (TAREA 4)
    // ================================================================
    public String orquestarConsulta(String mensajeUsuario) {
        String promptFinal = "";
        String datosDeContexto = "";
        String intencionDetectada = "general";

        String p = mensajeUsuario.toLowerCase();

        try {
            // 1. ROUTER: Decidir qué buscar en la BD
            if (p.contains("producto") || p.contains("stock") || p.contains("precios")) {
                intencionDetectada = "inventario";
                var productos = productRepository.findAll(); // Ojo: Si son muchos, usar findTop20
                datosDeContexto = objectMapper.writeValueAsString(productos);
            } else if (p.contains("cliente") || p.contains("deuda")) {
                intencionDetectada = "clientes";
                var clientes = clientRepository.findAll();
                datosDeContexto = objectMapper.writeValueAsString(clientes);
            } else if (p.contains("venta") || p.contains("vendimos") || p.contains("hoy")) {
                intencionDetectada = "ventas";
                // Asegúrate de que tu repositorio tenga este método funcionando
                var ventas = movementRepository.ventasHoy(LocalDate.now(), LocalDate.now());
                datosDeContexto = objectMapper.writeValueAsString(ventas);
            }

            // 2. CONSTRUCCIÓN DEL PROMPT (La ingeniería del prompt)
            StringBuilder sb = new StringBuilder();
            sb.append("Actúa como un asistente administrativo útil para la peluquería 'El Buen Corte'.\n");

            if (!datosDeContexto.isEmpty()) {
                sb.append("Tengo acceso a la base de datos. Aquí tienes la información relevante para responder:\n");
                sb.append("--- DATOS INICIO ---\n");
                sb.append(datosDeContexto).append("\n");
                sb.append("--- DATOS FIN ---\n");
                sb.append(
                        "Usa estos datos para responder la pregunta del usuario de forma natural y resumida. No inventes datos.\n");
            } else {
                sb.append(
                        "El usuario preguntó algo general o no encontré datos específicos en la BD. Responde amablemente.\n");
            }

            sb.append("Pregunta del Usuario: ").append(mensajeUsuario);

            promptFinal = sb.toString();

            // 3. LLAMADA FINAL A GEMINI
            return llamarGeminiAPI(promptFinal);

        } catch (Exception e) {
            return "Ocurrió un error procesando tu consulta: " + e.getMessage();
        }
    }
}