package com.el_buen_corte.el_buen_corte.reports.financiero;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaRepository;
import com.el_buen_corte.el_buen_corte.gastosOperativos.GastosOperativosRepository;
import com.el_buen_corte.el_buen_corte.movement.MovementRepository;
import com.el_buen_corte.el_buen_corte.payment.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancieroService {

    private final CitaRepository citaRepository;
    private final MovementRepository movementRepository;
    private final PaymentRepository paymentRepository;
    private final GastosOperativosRepository gastosOperativosRepository; // Inyectamos el nuevo repo

    // ==========================================
    // MÉTODOS PÚBLICOS (API)
    // ==========================================

    public FinancieroResponse earningsVsExpensesMonth() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now(); // Hasta hoy
        return calculateMetrics(start, end);
    }

    public FinancieroResponse earningsVsExpensesYear() {
        LocalDate start = LocalDate.now().withDayOfYear(1);
        LocalDate end = LocalDate.now();
        return calculateMetrics(start, end);
    }

    public FinancieroResponse earningsVsExpensesWeek() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate end = today.with(java.time.DayOfWeek.SUNDAY);
        return calculateMetrics(start, end);
    }

    public FinancieroResponse earningsVsExpensesDay() {
        LocalDate today = LocalDate.now();
        return calculateMetrics(today, today);
    }

    // ==========================================
    // LÓGICA CENTRALIZADA (DRY PRINCIPLE)
    // ==========================================

    private FinancieroResponse calculateMetrics(LocalDate startDate, LocalDate endDate) {
        // Mantenemos LocalDateTime SOLO para Payments (porque Payments si usa fecha y
        // hora exacta)
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // 1. INGRESOS (Payments usa LocalDateTime)
        Double incomeFromPayments = paymentRepository.getMonthlyIncomeDouble(startDateTime, endDateTime);
        Double incomeFromMovements = movementRepository.calculateTotalIncomeMovement(startDate, endDate);

        double totalIncome = (incomeFromPayments != null ? incomeFromPayments : 0.0)
                + (incomeFromMovements != null ? incomeFromMovements : 0.0);

        // 2. GASTOS (CORRECCIÓN AQUÍ)
        // Usamos startDate y endDate directos (LocalDate), SIN convertir a
        // LocalDateTime
        Double totalExpenses = gastosOperativosRepository.sumTotalMontoByDateRange(startDate, endDate);

        if (totalExpenses == null)
            totalExpenses = 0.0;

        // 3. CÁLCULOS
        double netProfit = totalIncome - totalExpenses;
        Long totalAppointments = citaRepository.countAllServicesThisMonth(startDate, endDate);
        Double averageTicket = citaRepository.calculateAveragePriceThisMonth(startDate, endDate);
        if (averageTicket == null)
            averageTicket = 0.0;

        return FinancieroResponse.builder()
                .earnings(totalIncome)
                .expenses(totalExpenses)
                .netProfit(netProfit)
                .totalAppointments(totalAppointments != null ? totalAppointments : 0L)
                .averageTicket(averageTicket)
                .build();
    }

    // ==========================================
    // REPORTE ANUAL (GRÁFICOS)
    // ==========================================

    public List<IncomeExpenseResponse> getYearlyIncomeExpenseReport() {
        LocalDate start = LocalDate.now().withDayOfYear(1);
        LocalDate end = LocalDate.now().withMonth(12).withDayOfMonth(31);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        List<Object[]> incomesPayments = paymentRepository.getMonthlyIncome(startDateTime, endDateTime);
        List<Object[]> incomesMovements = movementRepository.getMonthlyIncomes(start, end);

        // CORRECCIÓN AQUÍ: Pasa LocalDate (start, end) en lugar de LocalDateTime
        List<Object[]> expensesOperativos = gastosOperativosRepository.getMonthlyGastos(start, end);

        // Convertimos List<Object[]> a Map<Mes, Monto> para acceso rápido
        Map<Integer, Double> paymentsMap = convertToMap(incomesPayments);
        Map<Integer, Double> movementsMap = convertToMap(incomesMovements);
        Map<Integer, Double> expensesMap = convertToMap(expensesOperativos);

        List<IncomeExpenseResponse> report = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            double income = paymentsMap.getOrDefault(month, 0.0) + movementsMap.getOrDefault(month, 0.0);
            double expense = expensesMap.getOrDefault(month, 0.0);

            report.add(IncomeExpenseResponse.builder()
                    .month(month)
                    .income(income)
                    .expense(expense)
                    .build());
        }

        return report;
    }

    // Helper para convertir las respuestas raras de SQL (Object[]) a un Mapa fácil
    // de usar en Java
    private Map<Integer, Double> convertToMap(List<Object[]> rawList) {
        return rawList.stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).intValue(), // Key: Mes
                        row -> ((Number) row[1]).doubleValue(), // Value: Monto
                        (existing, replacement) -> existing // En caso de duplicados (no debería pasar), mantén el
                                                            // existente
                ));
    }
}