package com.el_buen_corte.el_buen_corte.reports.resumen;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaRepository;
import com.el_buen_corte.el_buen_corte.client.Client;
import com.el_buen_corte.el_buen_corte.product.ProductReportResponse;
import com.el_buen_corte.el_buen_corte.product.ProductService;
import com.el_buen_corte.el_buen_corte.reports.clientes.ClienteReportResponse;
import com.el_buen_corte.el_buen_corte.reports.clientes.ClienteService;
import com.el_buen_corte.el_buen_corte.reports.financiero.FinancieroResponse;
import com.el_buen_corte.el_buen_corte.reports.financiero.FinancieroService;
import com.el_buen_corte.el_buen_corte.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumenService {

    private final FinancieroService financieroService;
    private final ClienteService clienteService;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final CitaRepository citaRepository;

    public ResumenResponse getResumenReports() {

        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);
        LocalDate end = today.withDayOfMonth(today.lengthOfMonth());

        FinancieroResponse financieroResumen = financieroService.earningsVsExpensesMonth();
        ClienteReportResponse clienteResumen = clienteService.generalReportsMonth();
        ProductReportResponse productResumen = productService.reports();
        Long totalCanceledAppointments = citaRepository.canceledAppointments(start, end);
        Double profitMargin = financieroResumen.getEarnings() > 0
                ? (financieroResumen.getNetProfit() / financieroResumen.getEarnings()) * 100
                : 0.0;
        double occupancyRate = 0.0;
        long totalHairdressers = userRepository.findAllHairdressers().size();

        if (totalHairdressers > 0) {
            double totalPossibleAppointments = (totalHairdressers * 9.0) * 20.0;
            double totalAppointments = citaRepository.countAllServicesThisMonth(start, end);
            occupancyRate = (totalAppointments / totalPossibleAppointments) * 100.0;
        }

        Double retentionRate = calcularTasaRetencionMensual();
        Long recurringClients = calcularClientesRecurrentesDelMes();
        Long totalAppointments = citaRepository.countAllServicesThisMonth(start, end);

        return ResumenResponse.builder()
                .totalIncome(financieroResumen.getEarnings())
                .totalExpenses(financieroResumen.getExpenses())
                .netProfit(financieroResumen.getNetProfit())
                .profitMargin(profitMargin)
                .totalClients(clienteResumen.getTotalClients())
                .newClients(clienteResumen.getNewClients())
                .retentionRate(retentionRate)
                .recurringClients(recurringClients)
                .totalAppointments(totalAppointments)
                .averageTicket(financieroResumen.getAverageTicket())
                .occupancyRate(occupancyRate)
                .canceledAppointments(totalCanceledAppointments)
                .totalProducts(productResumen.getTotalProducts())
                .totalProductsValue(productResumen.getTotalValue())
                .lowStock(productResumen.getTotalLowStock())
                .totalMovements(productResumen.getTotalMovementsToday())

                .build();

    }

    private double calcularTasaRetencionMensual() {
        // Mes actual y mes anterior
        YearMonth mesActual = YearMonth.now();
        YearMonth mesAnterior = mesActual.minusMonths(1);

        // Rango de fechas de cada mes
        LocalDate inicioMesAnterior = mesAnterior.atDay(1);
        LocalDate finMesAnterior = mesAnterior.atEndOfMonth();
        LocalDate inicioMesActual = mesActual.atDay(1);
        LocalDate finMesActual = mesActual.atEndOfMonth();

        // Clientes con citas en cada mes
        List<Client> clientesMesAnterior = citaRepository.findClientsWithAppointmentsInPeriod(
                inicioMesAnterior, finMesAnterior);
        List<Client> clientesMesActual = citaRepository.findClientsWithAppointmentsInPeriod(
                inicioMesActual, finMesActual);

        if (clientesMesAnterior.isEmpty()) {
            return 0.0; // Evitar división por cero
        }

        // Buscar IDs que repiten entre ambos meses
        Set<Long> idsActuales = clientesMesActual.stream()
                .map(Client::getId)
                .collect(Collectors.toSet());

        long clientesRecurrentes = clientesMesAnterior.stream()
                .filter(c -> idsActuales.contains(c.getId()))
                .count();

        // Fórmula: (clientes que volvieron / total anterior) × 100
        return (clientesRecurrentes * 100.0) / clientesMesAnterior.size();
    }

    public long calcularClientesRecurrentesDelMes() {
        YearMonth mesActual = YearMonth.now();
        LocalDate inicioMesActual = mesActual.atDay(1);
        LocalDate finMesActual = mesActual.atEndOfMonth();

        // Clientes con citas antes de este mes
        List<Client> clientesAnteriores = citaRepository.findClientsWithAppointmentsBefore(inicioMesActual);

        // Clientes que vinieron en el mes actual
        List<Client> clientesMesActual = citaRepository.findClientsWithAppointmentsInPeriod(
                inicioMesActual, finMesActual);

        // Si no hay datos, devolvemos 0
        if (clientesMesActual.isEmpty())
            return 0L;

        // Obtenemos los IDs de clientes anteriores
        Set<Long> idsAnteriores = clientesAnteriores.stream()
                .map(Client::getId)
                .collect(Collectors.toSet());

        // Filtramos los que repitieron (estaban antes y vinieron este mes)
        return clientesMesActual.stream()
                .filter(c -> idsAnteriores.contains(c.getId()))
                .count();
    }

}
