package com.el_buen_corte.el_buen_corte.payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.client.Client;
import com.el_buen_corte.el_buen_corte.client.ClientRepository;
import com.el_buen_corte.el_buen_corte.service.HairRepository;
import com.el_buen_corte.el_buen_corte.service.HairService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

        private final PaymentRepository paymentRepository;
        private final HairRepository hairRepository;
        private final ClientRepository clientRepository;

        public PaymentResponse createPayment(PaymentRequest request) {

                HairService service = hairRepository.findById(request.getServiceId())
                                .orElseThrow(() -> new RuntimeException("Service not found"));

                Client client = clientRepository.findById(request.getClientId())
                                .orElseThrow(() -> new RuntimeException("Client not found"));

                Payment payment = Payment.builder()
                                .amount(request.getAmount())
                                .paymentMethod(request.getPaymentMethod())
                                .paymentDate(LocalDateTime.now())
                                .client(client)
                                .service(service)
                                .build();

                paymentRepository.save(payment);

                return toResponse(payment);

        }

        public List<PaymentResponse> getAllPaymentsFromToday() {
                List<Payment> payments = paymentRepository.findAllByPaymentDateIsToday();
                return payments.stream()
                                .map(this::toResponse)
                                .toList();
        }

        public PaymentReportTodayResponse reportsToday() {

                LocalDate today = LocalDate.now();
                LocalDateTime startOfDay = today.atStartOfDay();
                LocalDateTime endOfDay = startOfDay.plusDays(1);

                Double totalAmountToday = paymentRepository.totalAmountToday();
                int totalPayments = paymentRepository.findAllByPaymentDateIsToday().size();
                Double totalCashAmountToday = paymentRepository.totalAmountByMethodBetween(startOfDay, endOfDay,
                                PaymentMethod.EFECTIVO);
                Double totalCardAmountToday = paymentRepository.totalAmountByMethodBetween(startOfDay, endOfDay,
                                PaymentMethod.TARJETA);
                Double totalQRAmountToday = paymentRepository.totalAmountByMethodBetween(startOfDay, endOfDay,
                                PaymentMethod.QR);

                return PaymentReportTodayResponse.builder()
                                .totalCardAmountToday(totalCardAmountToday)
                                .totalCashAmountToday(totalCashAmountToday)
                                .totalPaymentAmountToday(totalAmountToday)
                                .totalQRAmountToday(totalQRAmountToday)
                                .totalTransactionsToday(totalPayments)
                                .build();
        }

        public PaymentReportMonthResponse reportsMonth() {

                LocalDate now = LocalDate.now();
                LocalDateTime startOfMonth = now.withDayOfMonth(1).atStartOfDay();
                LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

                Double totalCardAmountMonth = paymentRepository.totalAmountByMethodBetween(startOfMonth, endOfMonth,
                                PaymentMethod.TARJETA);
                Double totalQRAmountMonth = paymentRepository.totalAmountByMethodBetween(startOfMonth, endOfMonth,
                                PaymentMethod.QR);

                long daysSoFar = now.getDayOfMonth();

                Double totalCashAmountMonth = paymentRepository.totalAmountByMethodBetween(startOfMonth, endOfMonth,
                                PaymentMethod.EFECTIVO);
                Double totalAmountMonth = totalCardAmountMonth + totalCashAmountMonth + totalQRAmountMonth;
                Double totalAmountDigital = totalCardAmountMonth + totalQRAmountMonth;
                Double dailyAverage = totalAmountMonth / daysSoFar;
                int totalOfTransactions = paymentRepository.findAll().size();

                return PaymentReportMonthResponse.builder()
                                .totalTransactionsMonth(totalOfTransactions)
                                .totalAmountMonth(totalAmountMonth)
                                .totalCash(totalCashAmountMonth)
                                .averageDaily(dailyAverage)
                                .totalDigital(totalAmountDigital)
                                .build();
        }

        public PaymentMonthResponse getAllPaymentsFromLastMonth() {
                LocalDate now = LocalDate.now();
                LocalDateTime startOfMonth = now.withDayOfMonth(1).atStartOfDay();
                LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

                Double totalCashAmountMonth = paymentRepository.totalAmountByMethodBetween(startOfMonth, endOfMonth,
                                PaymentMethod.EFECTIVO);
                Double totalCardAmountMonth = paymentRepository.totalAmountByMethodBetween(startOfMonth, endOfMonth,
                                PaymentMethod.TARJETA);
                Double totalQRAmountMonth = paymentRepository.totalAmountByMethodBetween(startOfMonth, endOfMonth,
                                PaymentMethod.QR);
                Double totalAmountMonth = totalCardAmountMonth + totalCashAmountMonth + totalQRAmountMonth;

                return PaymentMonthResponse.builder()
                                .totalCashAmountMonth(totalCashAmountMonth)
                                .totalQRAmountMonth(totalQRAmountMonth)
                                .totalAmountMonth(totalAmountMonth)
                                .totalCardAmountMonth(totalCardAmountMonth)
                                .build();

        }

        public List<PaymentResponse> getAllPayments() {
                List<Payment> payments = paymentRepository.findAll();
                return payments.stream()
                                .map(this::toResponse)
                                .toList();
        }

        public PaymentResponse toResponse(Payment payment) {
                return PaymentResponse.builder()
                                .id(payment.getId())
                                .amount(payment.getAmount())
                                .paymentMethod(payment.getPaymentMethod())
                                .paymentDate(payment.getPaymentDate())
                                .client(payment.getClient())
                                .service(payment.getService())
                                .build();
        }

}
