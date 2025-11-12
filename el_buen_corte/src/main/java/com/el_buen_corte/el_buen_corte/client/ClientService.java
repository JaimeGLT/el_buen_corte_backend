package com.el_buen_corte.el_buen_corte.client;

import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.cita.CitaResumenResponse;

@Service
@RequiredArgsConstructor
public class ClientService {

        private final ClientRepository clientRepository;

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

                String lastVisit = citas.stream()
                                .map(CitaResumenResponse::getDate)
                                .max(Comparator.naturalOrder())
                                .map(date -> date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                .orElse(null);

                return ClientResponse.builder()
                                .id(client.getId())
                                .firstName(client.getFirstName())
                                .lastName(client.getLastName())
                                .email(client.getEmail())
                                .phoneNumber(client.getPhoneNumber())
                                .observations(client.getObservations())
                                .citas(citas)
                                .lastVisit(lastVisit)
                                .build();
        }

}
