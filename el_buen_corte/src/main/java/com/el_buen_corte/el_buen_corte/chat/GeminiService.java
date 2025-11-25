package com.el_buen_corte.el_buen_corte.chat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

// TUS IMPORTS (Mantén los que ya tienes)
import com.el_buen_corte.el_buen_corte.chat.dto.*;
import com.el_buen_corte.el_buen_corte.cita.CitaRepository;
import com.el_buen_corte.el_buen_corte.cita.Status;
import com.el_buen_corte.el_buen_corte.cita.Cita;
import com.el_buen_corte.el_buen_corte.client.ClientRepository;
import com.el_buen_corte.el_buen_corte.movement.MovementRepository;
import com.el_buen_corte.el_buen_corte.payment.PaymentRepository; // NECESARIO
import com.el_buen_corte.el_buen_corte.product.ProductRepository;
import com.el_buen_corte.el_buen_corte.product.Product;
import com.el_buen_corte.el_buen_corte.client.Client;

@Service
public class GeminiService {

    @Value("${google.ai.key}")
    private String apiKey;

    @Value("${google.gemini.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;
    private final MovementRepository movementRepository;
    private final PaymentRepository paymentRepository; // INYECTADO DE NUEVO
    private final ClientRepository clientRepository;   // INYECTADO PARA BUSCAR CLIENTES
    private final ObjectMapper objectMapper;
    private final CitaRepository citaRepository;

    private final ZoneId zonaHoraria = ZoneId.systemDefault();

    public GeminiService(ProductRepository productRepository,
                         MovementRepository movementRepository,
                         PaymentRepository paymentRepository,
                         ClientRepository clientRepository,
                         ObjectMapper objectMapper,
                         CitaRepository citaRepository) {
        this.restTemplate = new RestTemplate();
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
        this.objectMapper = objectMapper;
        this.citaRepository = citaRepository;
    }

    // =========================================================================
    // 1. EL CEREBRO MEJORADO (Detecta Clientes y Empleados)
    // =========================================================================
    public String orquestarConsulta(String mensajeUsuario) {
        String p = mensajeUsuario.toLowerCase();
        Map<String, Object> contexto = new HashMap<>();

        // --- CARGA BASE (Siempre activa) ---
        contexto.put("finanzas_hoy", obtenerFinanzasTotales()); // CORREGIDO: Suma Pagos + Movimientos
        contexto.put("agenda_hoy", obtenerCitasDeHoy());
        contexto.put("alertas", obtenerAlertas());

        // --- CARGA CONDICIONAL (Para no saturar) ---

        // 1. Agenda Futura (Ampliamos palabras clave)
        if (p.contains("proximo") || p.contains("mañana") || p.contains("semana") || p.contains("viene") || p.contains("agenda")) {
            contexto.put("agenda_futura", obtenerAgendaFutura());
        }

        // 2. Información de Empleados (Ranking)
        if (p.contains("empleado") || p.contains("personal") || p.contains("barbero") || p.contains("generado")) {
            contexto.put("rendimiento_empleados_hoy", obtenerRankingEmpleadosHoy());
        }

        // 3. Información Específica de Cliente (Búsqueda por nombre)
        if (p.contains("cliente") || p.contains("sobre")) {
            // Intentamos buscar si menciona algún nombre de la base de datos
            // Esta lógica es simple: busca palabras del mensaje en la BD de clientes.
            List<Map<String, Object>> clientesEncontrados = buscarClienteEnMensaje(p);
            if (!clientesEncontrados.isEmpty()) {
                contexto.put("info_clientes_buscados", clientesEncontrados);
            }
        }

        try {
            String jsonContexto = objectMapper.writeValueAsString(contexto);
            return construirPromptYEnviar(mensajeUsuario, jsonContexto);
        } catch (Exception e) {
            return "Error construyendo contexto: " + e.getMessage();
        }
    }

    // =========================================================================
    // 2. LÓGICA DE NEGOCIO CORREGIDA
    // =========================================================================

    // FIX 1: Finanzas Reales (Pagos de Servicios + Ventas de Productos)
    private Map<String, Object> obtenerFinanzasTotales() {
        LocalDate hoy = LocalDate.now(zonaHoraria);
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(LocalTime.MAX);

        // A. Ingresos por Servicios (Cortes, etc.)
        Double ingresosServicios = paymentRepository.sumarPagosEnRango(inicio, fin);
        if (ingresosServicios == null) ingresosServicios = 0.0;

        // B. Ingresos por Productos (Shampoo, Gel...)
        Double ingresosProductos = movementRepository.calculateTotalIncomeMovement(hoy, hoy);
        if (ingresosProductos == null) ingresosProductos = 0.0;

        // C. Gastos (Entradas de inventario)
        Double gastos = movementRepository.calculateTotalExpenses(hoy, hoy);
        if (gastos == null) gastos = 0.0;

        Map<String, Object> finanzas = new HashMap<>();
        finanzas.put("ingresos_servicios", ingresosServicios);
        finanzas.put("ingresos_productos", ingresosProductos);
        finanzas.put("total_ingresos", ingresosServicios + ingresosProductos);
        finanzas.put("gastos_operativos", gastos);
        finanzas.put("balance_neto", (ingresosServicios + ingresosProductos) - gastos);

        return finanzas;
    }

    // FIX 2: Empleados (¿Quién generó más?)
    private List<Map<String, Object>> obtenerRankingEmpleadosHoy() {
        LocalDate hoy = LocalDate.now(zonaHoraria);

        // Traemos citas de hoy COMPLETADAS
        List<Cita> citasHoy = citaRepository.findAll().stream()
                .filter(c -> c.getDate().equals(hoy) && c.getStatus() == Status.COMPLETADO)
                .collect(Collectors.toList());

        Map<String, Double> ranking = new HashMap<>();

        for (Cita c : citasHoy) {
            // VERIFICACIÓN DE SEGURIDAD
            // Tienes que cambiar 'getEmployee()' por como se llame tu campo en la entidad Cita
            // Si tu cita no tiene empleado asignado, esto no funcionará.
            if (c.getService() != null /* && c.getEmployee() != null */) {

                // --- CAMBIA ESTO POR TU GETTER REAL ---
                // String nombreEmpleado = c.getEmployee().getFirstName();
                // POR AHORA, SI NO TIENES EMPLEADO, PONDRÁ "Sin Asignar" PARA QUE VEAS QUE FUNCIONA EL CÁLCULO
                String nombreEmpleado = "Barbero Genérico"; // <-- CAMBIA ESTO
                // --------------------------------------

                Double precio = c.getService().getPrice();
                ranking.put(nombreEmpleado, ranking.getOrDefault(nombreEmpleado, 0.0) + precio);
            }
        }

        if (ranking.isEmpty()) {
            return List.of(Map.of("mensaje", "No hay citas completadas hoy con empleados asignados para calcular ranking."));
        }

        return ranking.entrySet().stream()
                .map(e -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("empleado", e.getKey());
                    item.put("ingresos_generados", e.getValue());
                    return item;
                })
                .sorted((a, b) -> Double.compare((Double)b.get("ingresos_generados"), (Double)a.get("ingresos_generados"))) // Ordenar de mayor a menor
                .collect(Collectors.toList());
    }

    // FIX 3: Búsqueda de Cliente (Gasto Histórico + Info)
    private List<Map<String, Object>> buscarClienteEnMensaje(String mensaje) {
        String[] palabras = mensaje.split(" ");
        List<Map<String, Object>> resultados = new ArrayList<>();

        for (String palabra : palabras) {
            if (palabra.length() > 3) {
                // Buscamos clientes que coincidan con la palabra
                List<Client> encontrados = clientRepository.findByFirstNameContainingIgnoreCase(palabra);

                for (Client c : encontrados) {
                    Map<String, Object> info = new HashMap<>();
                    info.put("nombre", c.getFirstName() + " " + c.getLastName());
                    info.put("telefono", (c.getPhoneNumber() != null) ? c.getPhoneNumber() : "Sin registro");

                    // --- AQUÍ ESTABA EL ERROR DEL 250.0 ---
                    // AHORA LLAMAMOS A LA BD DE VERDAD:
                    Double gastoReal = citaRepository.calcularGastoTotalCliente(c.getId());
                    Integer visitasReales = citaRepository.contarVisitasCliente(c.getId());

                    info.put("total_visitas", visitasReales);
                    info.put("gasto_historico_real", gastoReal);
                    // --------------------------------------

                    resultados.add(info);
                }
            }
        }
        return resultados.stream().distinct().collect(Collectors.toList());
    }

    // FIX 4: Mapeo de Cita con PRECIO (Para que la IA calcule ingresos futuros)
    private Map<String, Object> mapearCitaCompleta(Cita c) {
        Map<String, Object> m = new HashMap<>();
        m.put("fecha", c.getDate().toString());
        m.put("hora", c.getTime().toString());
        m.put("cliente", (c.getClient() != null) ? c.getClient().getFirstName() : "Anonimo");
        m.put("servicio", (c.getService() != null) ? c.getService().getName() : "General");
        // CLAVE: Pasamos el precio
        m.put("precio_estimado", (c.getService() != null) ? c.getService().getPrice() : 0.0);
        m.put("estado", c.getStatus().toString());
        return m;
    }

    // --- MÉTODOS DE APOYO (Alertas, Agenda, etc.) ACTUALIZADOS AL NUEVO MAPEO ---

    private Map<String, Object> obtenerAlertas() {
        LocalDate hoy = LocalDate.now(zonaHoraria);
        Map<String, Object> alertas = new HashMap<>();

        List<Cita> vencidas = citaRepository.findAll().stream()
                .filter(c -> c.getDate().isBefore(hoy) && c.getStatus() == Status.PENDIENTE)
                .collect(Collectors.toList());

        if (!vencidas.isEmpty()) {
            alertas.put("citas_vencidas_urgente", vencidas.stream().map(this::mapearCitaCompleta).collect(Collectors.toList()));
        }

        List<Product> criticos = productRepository.findStockBajo(PageRequest.of(0, 10));
        if (!criticos.isEmpty()) {
            alertas.put("stock_critico", criticos.stream().map(p -> Map.of(
                    "producto", p.getName(),
                    "actual", p.getInitialStock(),
                    "minimo", p.getMinimumStock()
            )).collect(Collectors.toList()));
        }
        return alertas;
    }

    private List<Map<String, Object>> obtenerCitasDeHoy() {
        LocalDate hoy = LocalDate.now(zonaHoraria);
        return citaRepository.findAll().stream()
                .filter(c -> c.getDate().equals(hoy) && c.getStatus() != Status.CANCELADO)
                .sorted(Comparator.comparing(Cita::getTime))
                .map(this::mapearCitaCompleta)
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> obtenerAgendaFutura() {
        LocalDate hoy = LocalDate.now(zonaHoraria);
        return citaRepository.findAll().stream()
                .filter(c -> c.getDate().isAfter(hoy) && c.getStatus() != Status.CANCELADO)
                .sorted(Comparator.comparing(Cita::getDate))
                .limit(7)
                .map(this::mapearCitaCompleta)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // 3. PROMPT ENGINEERING (Ajustado para leer los nuevos datos)
    // =========================================================================

    private String construirPromptYEnviar(String pregunta, String json) {
        StringBuilder sb = new StringBuilder();
        sb.append("ERES: El ERP Inteligente de 'El Buen Corte'.\n");
        sb.append("DATOS EN TIEMPO REAL:\n```json\n").append(json).append("\n```\n");
        sb.append("REGLAS:\n");
        sb.append("1. **Finanzas:** Tienes 'total_ingresos' (Suma de Servicios + Productos). Úsalo para responder cuánto dinero entró.\n");
        sb.append("2. **Agenda:** Si preguntan por ingresos futuros, SUMA los 'precio_estimado' de 'agenda_futura' y da una proyección.\n");
        sb.append("3. **Clientes:** Si ves 'info_clientes_buscados', esa es la info específica que pidió el usuario sobre una persona.\n");
        sb.append("4. **Empleados:** Si ves 'rendimiento_empleados_hoy', responde quién generó más.\n");
        sb.append("5. **Citas Vencidas:** ALERTA solo si es relevante o preguntan por problemas.\n");
        sb.append("PREGUNTA: ").append(pregunta);

        return llamarGeminiAPI(sb.toString());
    }

    public String llamarGeminiAPI(String prompt) {
        String finalUrl = apiUrl + "?key=" + apiKey;
        try {
            Part part = new Part(prompt);
            Content content = new Content(Collections.singletonList(part));
            GeminiRequest request = new GeminiRequest(Collections.singletonList(content));
            GeminiResponse response = restTemplate.postForObject(finalUrl, request, GeminiResponse.class);
            if (response != null && !response.candidates().isEmpty()) {
                return response.candidates().get(0).content().parts().get(0).text();
            }
            return "Error IA.";
        } catch (Exception e) {
            return "Error API: " + e.getMessage();
        }
    }
}