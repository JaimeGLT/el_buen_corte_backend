package com.el_buen_corte.el_buen_corte.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AiService {

    private final WebClient webClient;

    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.model}")
    private String model;

    public AiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api-inference.huggingface.co").build();
    }

    public Mono<String> generateResponse(String question) {
        // Para desarrollo, devolver respuestas simuladas
        return Mono.just(generateMockResponse(question));
    }

    private String generateMockResponse(String question) {
        String lowerQuestion = question.toLowerCase();

        if (lowerQuestion.contains("hola") || lowerQuestion.contains("hello")) {
            return "¡Hola! Soy tu asistente IA especializado en el ERP de El Buen Corte. ¿En qué puedo ayudarte hoy? Puedo proporcionarte información sobre ventas, inventario, clientes o reportes financieros.";
        } else if (lowerQuestion.contains("venta") || lowerQuestion.contains("ventas")) {
            return "Sobre ventas: El sistema registra todas las transacciones de servicios de peluquería. Puedes ver reportes diarios, semanales y mensuales de ingresos. ¿Te gustaría saber sobre algún período específico?";
        } else if (lowerQuestion.contains("cliente") || lowerQuestion.contains("clientes")) {
            return "Información de clientes: El ERP mantiene un registro completo de todos los clientes, incluyendo su historial de servicios, preferencias y datos de contacto. ¿Necesitas buscar algún cliente en particular?";
        } else if (lowerQuestion.contains("reporte") || lowerQuestion.contains("reportes")) {
            return "Reportes disponibles: Tienes acceso a reportes financieros (ingresos vs gastos), reportes de servicios, y estadísticas generales del negocio. ¿Qué tipo de reporte te interesa?";
        } else if (lowerQuestion.contains("inventario")) {
            return "Sobre inventario: El sistema controla los productos disponibles, alertas de stock bajo y sugerencias de reposición. ¿Quieres verificar el estado de algún producto específico?";
        } else {
            return "Entiendo tu consulta. Como asistente del ERP El Buen Corte, puedo ayudarte con información sobre ventas, clientes, inventario, reportes y estadísticas del negocio. ¿Podrías ser más específico sobre lo que necesitas?";
        }
    }

    private String parseResponse(String response) {
        // Parse simple del JSON de Hugging Face (ej. [{"generated_text": "..."}])
        if (response.contains("generated_text")) {
            int start = response.indexOf("\"generated_text\":\"") + 17;
            int end = response.indexOf("\"", start);
            return response.substring(start, end);
        }
        return "Respuesta no disponible.";
    }
}
