package com.el_buen_corte.el_buen_corte.reports.financiero;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.el_buen_corte.el_buen_corte.payment.PaymentRepository;
import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaRepository;
import com.el_buen_corte.el_buen_corte.movement.MovementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancieroService {

    private final CitaRepository citaRepository;
    private final MovementRepository movementRepository;
    private final PaymentRepository paymentRepository;

    public FinancieroResponse earningsVsExpensesMonth() {
        
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now();

        Double income = Optional.ofNullable(
            citaRepository.calculateTotalIncome(startDate, endDate)
        ).orElse(0.0);

        Double expenses = Optional.ofNullable(
            movementRepository.calculateTotalExpenses(startDate, endDate)
        ).orElse(0.0);

        Double netProfit = income - expenses;

        return FinancieroResponse.builder()
            .earnings(income)
            .expenses(expenses)
            .netProfit(netProfit)
        .build();
    }
    public FinancieroResponse earningsVsExpensesYear() {
        
        LocalDate startDate = LocalDate.now().withDayOfYear(1);
        LocalDate endDate = LocalDate.now();

        Double income = Optional.ofNullable(
            citaRepository.calculateTotalIncome(startDate, endDate)
        ).orElse(0.0);

        Double expenses = Optional.ofNullable(
            movementRepository.calculateTotalExpenses(startDate, endDate)
        ).orElse(0.0);

        Double netProfit = income - expenses;

        return FinancieroResponse.builder()
            .earnings(income)
            .expenses(expenses)
            .netProfit(netProfit)
        .build();
    }

    public FinancieroResponse earningsVsExpensesWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endDate = today.with(java.time.DayOfWeek.SUNDAY);

        Double income = Optional.ofNullable(
            citaRepository.calculateTotalIncome(startDate, endDate)
        ).orElse(0.0);

        Double expenses = Optional.ofNullable(
            movementRepository.calculateTotalExpenses(startDate, endDate)
        ).orElse(0.0);

        Double netProfit = income - expenses;

        return FinancieroResponse.builder()
            .earnings(income)
            .expenses(expenses)
            .netProfit(netProfit)
        .build();
    }

    public FinancieroResponse earningsVsExpensesDay() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        Double income = Optional.ofNullable(
            citaRepository.calculateTotalIncome(startDate, endDate)
        ).orElse(0.0);

        Double expenses = Optional.ofNullable(
            movementRepository.calculateTotalExpenses(startDate, endDate)
        ).orElse(0.0);

        Double netProfit = income - expenses;

        return FinancieroResponse.builder()
            .earnings(income)
            .expenses(expenses)
            .netProfit(netProfit)
        .build();
    }

    public List<IncomeExpenseResponse> getYearlyIncomeExpenseReport() {

        LocalDate start = LocalDate.now().withDayOfYear(1);
        LocalDate end = LocalDate.now().withMonth(12).withDayOfMonth(31);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        List<Object[]> incomesRaw = paymentRepository.getMonthlyIncome(startDateTime, endDateTime);
        List<Object[]> expensesRaw = movementRepository.getMonthlyExpenses(start, end);
        List<Object[]> incomesRawProduct = movementRepository.getMonthlyIncomes(start, end);

        // Inicializamos la lista de 12 meses
        List<IncomeExpenseResponse> report = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {

            // Ingresos de citas
            int finalI2 = i;
            double income = incomesRaw.stream()
                    .filter(r -> ((Number) r[0]).intValue() == finalI2)
                    .map(r -> ((Number) r[1]).doubleValue())
                    .findFirst().orElse(0.0);

            // Ingresos de productos
            int finalI = i;
            double incomeProducts = incomesRawProduct.stream()
                    .filter(r -> ((Number) r[0]).intValue() == finalI)
                    .map(r -> ((Number) r[1]).doubleValue())
                    .findFirst().orElse(0.0);

            // Total ingresos
            double totalIncome = income + incomeProducts;

            // Gastos
            int finalI1 = i;
            double expense = expensesRaw.stream()
                    .filter(r -> ((Number) r[0]).intValue() == finalI1)
                    .map(r -> ((Number) r[1]).doubleValue())
                    .findFirst().orElse(0.0);

            report.add(IncomeExpenseResponse.builder()
                    .month(i)
                    .income(totalIncome)
                    .expense(expense)
                    .build());
        }

        return report;
    }

}
