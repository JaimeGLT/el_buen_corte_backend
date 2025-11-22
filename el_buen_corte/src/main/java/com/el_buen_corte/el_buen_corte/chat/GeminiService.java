package com.el_buen_corte.el_buen_corte.chat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.el_buen_corte.el_buen_corte.chat.dto.*;
import com.el_buen_corte.el_buen_corte.cita.Cita;
import com.el_buen_corte.el_buen_corte.cita.CitaRepository;
import com.el_buen_corte.el_buen_corte.cita.Status;
import com.el_buen_corte.el_buen_corte.client.Client;
import com.el_buen_corte.el_buen_corte.client.ClientRepository;
import com.el_buen_corte.el_buen_corte.movement.MovementRepository;
import com.el_buen_corte.el_buen_corte.payment.PaymentRepository;
import com.el_buen_corte.el_buen_corte.product.ProductRepository;
import com.el_buen_corte.el_buen_corte.product.Product;

@Service
public class GeminiService {

    @Value("${google.ai.key}")
    private String apiKey;

    @Value("${google.gemini.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final MovementRepository movementRepository;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final CitaRepository citaRepository;

    public GeminiService(ProductRepository productRepository, ClientRepository clientRepository,
            MovementRepository movementRepository, ObjectMapper objectMapper, PaymentRepository paymentRepository,
            CitaRepository citaRepository) {
        this.restTemplate = new RestTemplate();
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.movementRepository = movementRepository;
        this.objectMapper = objectMapper;
        this.paymentRepository = paymentRepository;
        this.citaRepository = citaRepository;
    }

    public String orquestarConsulta(String mensajeUsuario) {
        String p = mensajeUsuario.toLowerCase();
        Map<String, Object> bolsaDeDatos = new HashMap<>();

        try {

            // finanzas
            if (p.contains("gasto") || p.contains("ingreso") || p.contains("ganancia") || p.contains("rentabilidad")
                    || p.contains("balance") || p.contains("dinero") || p.contains("venta") || p.contains("caja")
                    || p.contains("pago")) {

                LocalDate fechaInicio = LocalDate.now();
                LocalDate fechaFin = LocalDate.now();

                if (p.contains("ayer")) {
                    fechaInicio = fechaInicio.minusDays(1);
                    fechaFin = fechaFin.minusDays(1);
                } else if (p.contains("mes")) {
                    fechaInicio = fechaInicio.withDayOfMonth(1);
                    fechaFin = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());
                }

                // gastos
                Double gastosEntradas = movementRepository.calcularGastosPorEntradas(fechaInicio, fechaFin);
                if (gastosEntradas == null)
                    gastosEntradas = 0.0;

                // ingresos a pagos directos
                LocalDateTime inicioTime = fechaInicio.atStartOfDay();
                LocalDateTime finTime = fechaFin.atTime(LocalTime.MAX);
                Double ingresosPagos = paymentRepository.sumarPagosEnRango(inicioTime, finTime);
                if (ingresosPagos == null)
                    ingresosPagos = 0.0;

                // ingresos de las salidas
                Double ingresosSalidas = movementRepository.calcularIngresosPorSalidas(fechaInicio, fechaFin);
                if (ingresosSalidas == null)
                    ingresosSalidas = 0.0;

                Double totalIngresos = ingresosPagos + ingresosSalidas;
                Double gananciaNeta = totalIngresos - gastosEntradas;

                Map<String, Object> finanzas = new HashMap<>();
                finanzas.put("periodo_analizado", fechaInicio.toString() + " al " + fechaFin.toString());
                finanzas.put("total_ingresos", totalIngresos);
                finanzas.put("detalle_ingresos",
                        "Pagos Caja ($" + ingresosPagos + ") + Salidas Stock ($" + ingresosSalidas + ")");
                finanzas.put("total_gastos", gastosEntradas);
                finanzas.put("explicacion_gastos", "Costo de inventario entrante");
                finanzas.put("ganancia_neta", gananciaNeta);

                bolsaDeDatos.put("reporte_financiero", finanzas);
            }

            // productos
            if (p.contains("stock") || p.contains("producto") || p.contains("precio") || p.contains("marca")
                    || p.contains("shampoo") || p.contains("cera")) {

                var pageable = PageRequest.of(0, 20);
                List<Product> productos;

                if (p.contains("bajo") || p.contains("falta")) {
                    productos = productRepository.findStockBajo(pageable);
                } else {
                    productos = productRepository.findAll(pageable).getContent();
                }

                var listaProductos = productos.stream().map(prod -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("nombre", prod.getName());
                    item.put("stock", prod.getInitialStock());
                    item.put("precio", prod.getPrice());
                    return item;
                }).collect(Collectors.toList());

                bolsaDeDatos.put("inventario_disponible", listaProductos);
            }

            // clientes
            if (p.contains("cliente") || p.contains("quien") || p.contains("telefono") || p.contains("juan")
                    || p.contains("maria") || p.contains("pedro")) {

                var pageable = PageRequest.of(0, 10);
                // IDEAL: Usar el método buscarPorNombreOTelefono si ya lo creaste en el Repo
                var clientes = clientRepository.findAll(pageable).getContent();

                var listaClientes = clientes.stream().map(c -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", c.getId());
                    item.put("nombre", c.getFirstName() + " " + c.getLastName());
                    item.put("telefono", c.getPhoneNumber());
                    return item;
                }).collect(Collectors.toList());

                bolsaDeDatos.put("clientes_encontrados", listaClientes);
            }
            // citas
            if (p.contains("cita") || p.contains("agenda") || p.contains("hoy") || p.contains("mañana")
                    || p.contains("hora")) {
                LocalDate fecha = LocalDate.now();
                if (p.contains("mañana"))
                    fecha = fecha.plusDays(1);

                var citas = citaRepository.findByDateAndStatusNot(fecha, Status.CANCELADO);

                var listaCitas = citas.stream().map(c -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("hora", c.getTime().toString());
                    item.put("cliente", (c.getClient() != null) ? c.getClient().getFirstName() : "Anónimo");
                    item.put("servicio", (c.getService() != null) ? c.getService().getName() : "-");
                    return item;
                }).collect(Collectors.toList());

                bolsaDeDatos.put("agenda_del_dia", listaCitas);
            }
            String jsonFinal = objectMapper.writeValueAsString(bolsaDeDatos);
            return construirPromptYEnviar(mensajeUsuario, jsonFinal);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error procesando la consulta global: " + e.getMessage();
        }
    }

    public String alertastStock() {
        Map<String, Object> bolsaDeDatos = new HashMap<>();
        var pageable = PageRequest.of(0, 20);
        List<Product> productos;
        try {
            productos = productRepository.findStockBajo(pageable);

            var listaProductos = productos.stream().map(prod -> {
                Map<String, Object> item = new HashMap<>();
                item.put("nombre", prod.getName());
                item.put("stock", prod.getInitialStock());
                item.put("stock_minimo", prod.getMinimumStock());
                item.put("precio", prod.getPrice());
                return item;
            }).collect(Collectors.toList());

            bolsaDeDatos.put("Alertas Stock", listaProductos);

            String jsonFinal = objectMapper.writeValueAsString(bolsaDeDatos);
            return construirPromptYEnviar("¿Cuales son los productos con bajo stock?", jsonFinal);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error procesando la consulta global: " + e.getMessage();
        }
    }

    public String inventario() {
        Map<String, Object> bolsaDeDatos = new HashMap<>();
        var pageable = PageRequest.of(0, 30);
        List<Product> productos;
        try {
            productos = productRepository.inventarioACtual(pageable);

            var listaProductos = productos.stream().map(prod -> {
                Map<String, Object> item = new HashMap<>();
                item.put("nombre", prod.getName());
                item.put("stock", prod.getInitialStock());
                item.put("stock_minimo", prod.getMinimumStock());
                item.put("precio", prod.getPrice());
                return item;
            }).collect(Collectors.toList());

            bolsaDeDatos.put("Inventario Actual", listaProductos);

            String jsonFinal = objectMapper.writeValueAsString(bolsaDeDatos);
            return construirPromptYEnviar("¿Cual es el inventario actual?", jsonFinal);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error procesando la consulta global: " + e.getMessage();
        }
    }

    public String clientesActivos() {
        Map<String, Object> bolsaDeDatos = new HashMap<>();
        LocalDate fechaLimite = LocalDate.now().minusDays(90);
        List<Client> clientes;
        try {
            clientes = clientRepository.findActiveClients(fechaLimite);

            var listaProductos = clientes.stream().map(cliente -> {
                Map<String, Object> item = new HashMap<>();
                item.put("nombre", cliente.getFirstName() + " " + cliente.getLastName());
                item.put("ultimaCita", cliente.getLastAppointment());
                return item;
            }).collect(Collectors.toList());

            bolsaDeDatos.put("Clientes Activos", listaProductos);

            String jsonFinal = objectMapper.writeValueAsString(bolsaDeDatos);
            return construirPromptYEnviar("¿Cuales son los clientes activos?", jsonFinal);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error procesando la consulta global: " + e.getMessage();
        }
    }

    public String citasPendientes() {
        Map<String, Object> bolsaDeDatos = new HashMap<>();
        var pageable = PageRequest.of(0, 20);
        List<Cita> citas;
        try {
            citas = citaRepository.findPendientCitas(pageable);

            var listaProductos = citas.stream().map(cita -> {
                Map<String, Object> item = new HashMap<>();
                item.put("fechaCita", cita.getDate());
                item.put("cliente", cita.getClient().getFirstName() + " " + cita.getClient().getLastName());
                return item;
            }).collect(Collectors.toList());

            bolsaDeDatos.put("Citas pendientes", listaProductos);

            String jsonFinal = objectMapper.writeValueAsString(bolsaDeDatos);
            return construirPromptYEnviar("¿Cuantas y cuales son las citas pendientes?", jsonFinal);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error procesando la consulta global: " + e.getMessage();
        }
    }

    public String ingresoDiario() {
        Map<String, Object> bolsaDeDatos = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDateTime inicioDia = today.atStartOfDay();
        LocalDateTime finDia = today.atTime(LocalTime.MAX);

        try {
            Double totalPayments = paymentRepository.totalAmountByDate(inicioDia, finDia);
            VentasHoyDTO totalSales = movementRepository.ventasHoy(today, today);

            Double ingresosTotales = totalPayments + totalSales.monto();

            Map<String, Object> item = new HashMap<>();

            item.put("Total Ingresos hoy", ingresosTotales);
            item.put("Ingresos por Servicio", totalPayments);
            item.put("Ingresos por venta de produtos", totalSales.monto());

            bolsaDeDatos.put("Ingresos del día", item);

            String jsonFinal = objectMapper.writeValueAsString(bolsaDeDatos);
            return construirPromptYEnviar("¿Cuales son los ingresos de hoy", jsonFinal);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error procesando la consulta global: " + e.getMessage();
        }
    }

    private String construirPromptYEnviar(String pregunta, String json) {
        StringBuilder sb = new StringBuilder();
        sb.append("Eres el asistente financiero y operativo del ERP.\n");
        sb.append("Tienes acceso total a los datos operativos del ERP.\n\n");

        sb.append("DATOS DISPONIBLES (En formato JSON):\n");
        sb.append("```json\n").append(json).append("\n```\n\n");

        sb.append("REGLAS DE RAZONAMIENTO:\n");
        sb.append("1. Analiza el JSON completo.\n");
        sb.append("- Si ves el objeto 'reporte_financiero', úsalo como la verdad absoluta.\n");
        sb.append("- 'Ganancia Neta' es lo que realmente queda en el bolsillo (Ingresos - Gastos).\n");
        sb.append("- 'Alertas Stock' son los productos con stock bajo.\n");
        sb.append("- 'Ingresos del día' son los ingresos del dia de hoy en Bs.\n");
        sb.append("- 'Clientes Activos' son los clientes que tienen citas recurrentes.\n");
        sb.append("- 'Inventario Actual' son los productos que se encuentran en el inventario.\n");
        sb.append("- 'Citas pendientes' son las citas que estan pendientes.\n");
        sb.append(
                "- Si el usuario pregunta por qué los gastos son X, explica que provienen de las entradas de mercancía multiplicadas por su costo.\n");
        sb.append(
                "2. CRUZA INFORMACIÓN: Si el usuario pregunta '¿Juan tiene cita?', busca en 'clientes_encontrados' quién es Juan y luego mira en 'agenda_del_dia'.\n");
        sb.append("3. Si el JSON está vacío ({}), di amablemente que no encontraste registros.\n");
        sb.append("4. Sé breve y ejecutivo.\n");

        sb.append("\nPREGUNTA DEL USUARIO: ").append(pregunta);

        return llamarGeminiAPI(sb.toString());
    }

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
            return "Gemini no devolvió respuesta.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al conectar con Gemini API: " + e.getMessage();
        }
    }
}