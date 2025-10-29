package com.el_buen_corte.el_buen_corte.reports.financiero;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.el_buen_corte.el_buen_corte.payment.PaymentRepository;
import com.el_buen_corte.el_buen_corte.product.ProductRepository;
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
    private final ProductRepository productRepository;

    public FinancieroResponse earningsVsExpensesMonth() {
        
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Double income = Optional.ofNullable(
            paymentRepository.getMonthlyIncomeDouble(startDateTime, endDateTime)
        ).orElse(0.0);

        Double incomeMovement = Optional.ofNullable(
            movementRepository.calculateTotalIncomeMovement(startDate, endDate)
        ).orElse(0.0);

        Double priceTotalProductos = Optional.ofNullable(
            productRepository.sumTotalPriceCreatedByDate(startDate, endDate)
        ).orElse(0.0);

        Double expenses = Optional.ofNullable(
            movementRepository.calculateTotalExpenses(startDate, endDate)
        ).orElse(0.0);

        Double netProfit = (income + incomeMovement) - (expenses + priceTotalProductos);

        Long totalAppointments = citaRepository.countAllServicesThisMonth(startDate, endDate);

        Double averageTicket = citaRepository.calculateAveragePriceThisMonth(startDate, endDate);

        return FinancieroResponse.builder()
            .earnings(income + incomeMovement)
            .expenses(expenses + priceTotalProductos)
            .netProfit(netProfit)
            .totalAppointments(totalAppointments)
            .averageTicket(averageTicket)

        .build();
    }
    public FinancieroResponse earningsVsExpensesYear() {
        
        LocalDate startDate = LocalDate.now().withDayOfYear(1);
        LocalDate endDate = LocalDate.now();

        Double income = Optional.ofNullable(
            citaRepository.calculateTotalIncome(startDate, endDate)
        ).orElse(0.0);

        Double incomeMovement = Optional.ofNullable(
                movementRepository.calculateTotalIncomeMovement(startDate, endDate)
        ).orElse(0.0);

        Double priceTotalProductos = Optional.ofNullable(
                productRepository.sumTotalPriceCreatedByDate(startDate, endDate)
        ).orElse(0.0);

        Double expenses = Optional.ofNullable(
            movementRepository.calculateTotalExpenses(startDate, endDate)
        ).orElse(0.0);

        Double netProfit = (income + incomeMovement) - (expenses + priceTotalProductos);

        Long totalAppointments = citaRepository.countAllServicesThisMonth(startDate, endDate);

        Double averageTicket = citaRepository.calculateAveragePriceThisMonth(startDate, endDate);

        return FinancieroResponse.builder()
            .earnings(income + incomeMovement)
            .expenses(expenses + priceTotalProductos)
            .totalAppointments(totalAppointments)
            .netProfit(netProfit)
            .averageTicket(averageTicket)
        .build();
    }



    public FinancieroResponse earningsVsExpensesWeek() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endDate = today.with(java.time.DayOfWeek.SUNDAY);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Double income = Optional.ofNullable(
                paymentRepository.getMonthlyIncomeDouble(startDateTime, endDateTime)
        ).orElse(0.0);

        Double incomeMovement = Optional.ofNullable(
                movementRepository.calculateTotalIncomeMovement(startDate, endDate)
        ).orElse(0.0);

        Double priceTotalProductos = Optional.ofNullable(
                productRepository.sumTotalPriceCreatedByDate(startDate, endDate)
        ).orElse(0.0);

        Double expenses = Optional.ofNullable(
                movementRepository.calculateTotalExpenses(startDate, endDate)
        ).orElse(0.0);

        Double netProfit = (income + incomeMovement) - (expenses + priceTotalProductos);

        Long totalAppointments = citaRepository.countAllServicesThisMonth(startDate, endDate);

        Double averageTicket = citaRepository.calculateAveragePriceThisMonth(startDate, endDate);

        return FinancieroResponse.builder()
                .earnings(income + incomeMovement)
                .expenses(expenses + priceTotalProductos)
                .netProfit(netProfit)
                .totalAppointments(totalAppointments)
                .averageTicket(averageTicket)
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
        List<Object[]> stockExpensesRaw = productRepository.getMonthlyProductStockValue(start, end); // 🟢 nuevo

        List<IncomeExpenseResponse> report = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            int monthIndex = i;

            double income = incomesRaw.stream()
                    .filter(r -> ((Number) r[0]).intValue() == monthIndex)
                    .map(r -> ((Number) r[1]).doubleValue())
                    .findFirst().orElse(0.0);

            double incomeProducts = incomesRawProduct.stream()
                    .filter(r -> ((Number) r[0]).intValue() == monthIndex)
                    .map(r -> ((Number) r[1]).doubleValue())
                    .findFirst().orElse(0.0);

            double totalIncome = income + incomeProducts;

            double expense = expensesRaw.stream()
                    .filter(r -> ((Number) r[0]).intValue() == monthIndex)
                    .map(r -> ((Number) r[1]).doubleValue())
                    .findFirst().orElse(0.0);

            double expenseStock = stockExpensesRaw.stream()
                    .filter(r -> ((Number) r[0]).intValue() == monthIndex)
                    .map(r -> ((Number) r[1]).doubleValue())
                    .findFirst().orElse(0.0);

            double totalExpense = expense + expenseStock;

            report.add(IncomeExpenseResponse.builder()
                    .month(monthIndex)
                    .income(totalIncome)
                    .expense(totalExpense)
                    .build());
        }

        return report;
    }



}
