package com.el_buen_corte.el_buen_corte.ai;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);

    private final Client client;

    @Value("${google.ai.model:gemini-2.5-flash}")
    private String model;

    public AiService(@Value("${google.ai.key}") String apiKey) {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    /**
     * Genera una respuesta usando Gemini.
     * Siempre intenta retornar texto útil,
     * y si no es posible, usa una respuesta mock.
     */
    public String generateResponse(String question) {
        try {
            String prompt = buildPrompt(question);

            Content content = Content.fromParts(
                    Part.fromText(prompt));

            GenerateContentConfig config = GenerateContentConfig.builder()
                    .temperature(0.7f)
                    .maxOutputTokens(300)
                    .build();

            GenerateContentResponse response = client.models.generateContent(
                    model,
                    content,
                    config);

            // Extraer texto de forma segura
            String text = extractTextSafe(response);

            if (text == null || text.isBlank()) {
                log.warn("Gemini devolvió respuesta vacía. Usando mock.");
                return generateMockResponse(question);
            }

            log.info("[Gemini] Respuesta generada correctamente.");
            return text.trim();

        } catch (Exception e) {
            log.error("Error al llamar a Gemini: {}", e.getMessage(), e);
            return generateMockResponse(question);
        }
    }

    /**
     * Extrae el texto de forma segura evitando NullPointer
     */
    private String extractTextSafe(GenerateContentResponse response) {
        try {
            return response.text();
        } catch (Exception e) {
            log.warn("No se pudo extraer texto del response: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Prompt base
     */
    private String buildPrompt(String question) {
        return """
                Eres un asistente del sistema ERP 'El Buen Corte'.
                Responde de forma clara, profesional y breve (máx. 150 palabras).

                Áreas del ERP:
                - Gestión de ventas y servicios
                - Control de clientes y citas
                - Inventario
                - Reportes financieros
                - Gestión de empleados

                Pregunta del usuario:
                %s

                Responde en el idioma original de la pregunta.
                """.formatted(question);
    }

    /**
     * Respuesta fallback si Gemini falla
     */
    private String generateMockResponse(String question) {
        String lower = question.toLowerCase();

        if (lower.contains("hola") || lower.contains("hello"))
            return "¡Hola! Soy tu asistente del ERP 'El Buen Corte'. ¿En qué puedo ayudarte hoy?";

        if (lower.contains("venta"))
            return "El módulo de ventas permite registrar servicios, productos y generar reportes de ingresos.";

        if (lower.contains("cliente"))
            return "El sistema gestiona clientes, historial de servicios, preferencias y citas agendadas.";

        if (lower.contains("inventario"))
            return "El inventario controla stock, alertas de reposición y movimientos de productos.";

        if (lower.contains("reporte"))
            return "Puedes generar reportes financieros, operativos y estadísticas generales del negocio.";

        if (lower.contains("cita"))
            return "El módulo de citas permite programar, editar, cancelar y gestionar recordatorios.";

        if (lower.contains("empleado"))
            return "El sistema gestiona comisiones, horarios, roles y rendimiento del personal.";

        return "Sobre tu consulta: '%s'. ¿Puedes darme un poco más de detalle?".formatted(question);
    }
}
