package com.el_buen_corte.el_buen_corte.payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<PaymentMethod, Double> getAllPaymentsFromLastMonth() {
        List<Payment> payments = paymentRepository.findAllByPaymentDateIsThisMonth();

      return payments.stream()
        .collect(Collectors.groupingBy(
            Payment::getPaymentMethod,
            Collectors.summingDouble(Payment::getAmount)
        ));

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
                .paymentDate(LocalDateTime.now())
                .client(payment.getClient())
                .service(payment.getService())
                .build();
    }

}
