package com.el_buen_corte.el_buen_corte.client;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaResumenResponse;

@Service
@RequiredArgsConstructor
public class ClientService {

        private final ClientRepository clientRepository;
        private static final int VIP_THRESHOLD = 5;

        public String createClient(ClientRequest request) {
                var newclient = Client.builder()
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .email(request.getEmail())
                                .phoneNumber(request.getPhoneNumber())
                                .observations(request.getObservations())
                                .build();
                clientRepository.save(newclient);

                return "Cliente creado correctamente";
        }

        public List<ClientResponse> getAllClients() {
                List<Client> clients = clientRepository.findAll();
                return clients.stream()
                                .map(client -> this.toResponse(client))
                                .toList();
        }

        public ClientResponse getClientById(Long id) {
                Client client = clientRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Client not found"));
                return toResponse(client);
        }

        public ClientResponse updateClient(Long id, ClientRequest request) {
                Client client = clientRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Client not found"));

                if (request.getFirstName() != null)
                        client.setFirstName(request.getFirstName());
                if (request.getLastName() != null)
                        client.setLastName(request.getLastName());
                if (request.getEmail() != null)
                        client.setEmail(request.getEmail());
                if (request.getPhoneNumber() != null)
                        client.setPhoneNumber(request.getPhoneNumber());
                if (request.getObservations() != null)
                        client.setObservations(request.getObservations());

                clientRepository.save(client);
                return toResponse(client);
        }

        public ClientMetricsDTO getClientMetrics() {
                LocalDate now = LocalDate.now();
                LocalDate startOfThisMonth = now.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate startOfLastMonth = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                LocalDate endOfLastMonth = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

                // 1. Total Clientes
                long totalClients = clientRepository.count();

                if (totalClients == 0)
                        return ClientMetricsDTO.builder().build(); // Retorno vacío seguro

                // 2. Nuevos este mes
                long newThisMonth = clientRepository.countNewClientsBetween(startOfThisMonth, now);

                // 3. Nuevos mes anterior (para cálculo de crecimiento)
                long newLastMonth = clientRepository.countNewClientsBetween(startOfLastMonth, endOfLastMonth);

                // 4. VIPs
                long vipCount = clientRepository.countVipClients(VIP_THRESHOLD);

                // 5. Recurrentes (para retención)
                long recurringCount = clientRepository.countRecurringClients();

                // Cálculos Matemáticos
                double vipPercentage = ((double) vipCount / totalClients) * 100;

                double retentionRate = ((double) recurringCount / totalClients) * 100;

                double growthPercentage = 0.0;
                if (newLastMonth > 0) {
                        growthPercentage = ((double) (newThisMonth - newLastMonth) / newLastMonth) * 100;
                } else if (newThisMonth > 0) {
                        growthPercentage = 100.0; // Crecimiento infinito si antes era 0
                }

                return ClientMetricsDTO.builder()
                                .totalClients(totalClients)
                                .newClientsThisMonth(newThisMonth)
                                .vipClients(vipCount)
                                .vipPercentage(Math.round(vipPercentage * 10.0) / 10.0) // Redondeo a 1 decimal
                                .newClientsCurrentMonth(newThisMonth)
                                .growthPercentage(Math.round(growthPercentage * 10.0) / 10.0)
                                .retentionRate(Math.round(retentionRate * 10.0) / 10.0)
                                .build();
        }

        private ClientResponse toResponse(Client client) {
                List<CitaResumenResponse> citas = client.getCitas() == null ? List.of()
                                : client.getCitas().stream()
                                                .map(cita -> {
                                                        var service = cita.getService();
                                                        var stylist = cita.getStylist();
                                                        return CitaResumenResponse.builder()
                                                                        .id(cita.getId())
                                                                        .date(cita.getDate())
                                                                        .price(service == null ? 0 : service.getPrice())
                                                                        .service(service == null ? ""
                                                                                        : service.getName())
                                                                        .stylist(stylist == null ? ""
                                                                                        : stylist.getFirstName() + " "
                                                                                                        + stylist.getLastName())
                                                                        .build();
                                                }).toList();

                return ClientResponse.builder()
                                .id(client.getId())
                                .firstName(client.getFirstName())
                                .lastName(client.getLastName())
                                .email(client.getEmail())
                                .phoneNumber(client.getPhoneNumber())
                                .observations(client.getObservations())
                                .citas(citas)
                                .lastAppointment(client.getLastAppointment())
                                .build();
        }

}
