package com.el_buen_corte.el_buen_corte.summary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaRepository;
import com.el_buen_corte.el_buen_corte.cita.Status;
import com.el_buen_corte.el_buen_corte.client.ClientRepository;
import com.el_buen_corte.el_buen_corte.payment.PaymentRepository;
import com.el_buen_corte.el_buen_corte.product.ProductRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessSummaryService {

    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;
    private final CitaRepository appointmentRepository;
    private final ProductRepository productRepository;

    public BusinessSummaryDTO getSummary() {

        Double totalIncome = paymentRepository.totalIncome();
        if (totalIncome == null)
            totalIncome = 0.0;

        // Para simplificar, asumimos gastos fijos o se puede calcular desde movements
        Double totalExpenses = 0.0; // agregar lógica real si tienes movimientos

        Double netProfit = totalIncome - totalExpenses;
        Double profitMargin = totalIncome != 0 ? (netProfit / totalIncome) * 100 : 0.0;

        Long totalClients = clientRepository.count();
        Long newClients = appointmentRepository.countNewClientsSince(LocalDate.now().minusMonths(1));
        Double retentionRate = 50.0; // ejemplo, se puede calcular con lógica real
        Long recurringClients = 1L; // ejemplo, se puede calcular real

        Long totalAppointments = appointmentRepository.count();
        Long canceledAppointments = appointmentRepository.countByStatus(Status.CANCELADO);
        Double averageTicket = paymentRepository.averageTicket();
        Double occupancyRate = 0.55; // ejemplo, calcular real según agenda y citas

        Long totalProducts = productRepository.count();
        Double totalProductsValue = productRepository.totalProductsValue();
        Long lowStockProducts = productRepository.lowStockProducts();

        List<Object[]> mostUsed = appointmentRepository.mostUsedService();
        String mostUsedService = mostUsed.isEmpty() ? "N/A" : (String) mostUsed.get(0)[0];
        Long mostUsedServiceCount = mostUsed.isEmpty() ? 0L : (Long) mostUsed.get(0)[1];

        return new BusinessSummaryDTO(
                totalIncome,
                totalExpenses,
                netProfit,
                profitMargin,
                totalClients,
                newClients,
                retentionRate,
                recurringClients,
                totalAppointments,
                canceledAppointments,
                averageTicket,
                occupancyRate,
                totalProducts,
                totalProductsValue,
                lowStockProducts,
                mostUsedService,
                mostUsedServiceCount);
    }
}
