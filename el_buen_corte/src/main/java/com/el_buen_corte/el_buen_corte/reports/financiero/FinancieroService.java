package com.el_buen_corte.el_buen_corte.reports.financiero;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaRepository;
import com.el_buen_corte.el_buen_corte.movement.MovementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancieroService {

    private final CitaRepository citaRepository;
    private final MovementRepository movementRepository;

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

}
