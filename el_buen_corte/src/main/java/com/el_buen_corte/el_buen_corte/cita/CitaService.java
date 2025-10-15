package com.el_buen_corte.el_buen_corte.cita;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.client.Client;
import com.el_buen_corte.el_buen_corte.client.ClientRepository;
import com.el_buen_corte.el_buen_corte.service.HairRepository;
import com.el_buen_corte.el_buen_corte.service.HairService;
import com.el_buen_corte.el_buen_corte.user.User;
import com.el_buen_corte.el_buen_corte.user.UserRepository;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final ClientRepository clientRepository;
    private final HairRepository hairRepository;
    private final UserRepository userRepository;

    public CitaResponse createCita(CitaRequest cita){

        Client client = clientRepository.findById(cita.getClient())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        User hairdresser = userRepository.findById(cita.getStylist())
                .orElseThrow(() -> new IllegalArgumentException("Hairdresser not found"));

        HairService service = hairRepository.findById(cita.getService())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

                
        Cita newCita = Cita.builder()
                .date(cita.getDate())
                .time(cita.getTime())
                .client(client)
                .service(service)
                .stylist(hairdresser)
                .status(cita.getStatus())
                .notes(cita.getNotes())
                .build();

        citaRepository.save(newCita);

        return toResponse(newCita);
    }

    public CitaResponse updateCita(CitaRequest request, Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita not found"));

        if(request.getClient() != null) {
            Client client = clientRepository.findById(request.getClient())
                    .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        cita.setClient(client);
        }

        if(request.getStylist() != null) {
                User hairdresser = userRepository.findById(request.getStylist())
                        .orElseThrow(() -> new IllegalArgumentException("Hairdresser not found"));
                cita.setStylist(hairdresser);
        }

        if(request.getService() != null) {
                HairService service = hairRepository.findById(request.getService())
                        .orElseThrow(() -> new IllegalArgumentException("Service not found"));
                cita.setService(service);
        }

        if (request.getDate() != null) cita.setDate(request.getDate());
        
        if (request.getTime() != null) cita.setTime(request.getTime());

         if(request.getStatus() != null) cita.setStatus(request.getStatus());

        if (request.getNotes() != null && !request.getNotes().isEmpty())
                cita.setNotes(request.getNotes());

        citaRepository.save(cita);

        return toResponse(cita);
    }

    public CitaResponse cancelCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita not found"));

        cita.setStatus(Status.CANCELADO);
        citaRepository.save(cita);

        return toResponse(cita);
    }

    public List<CitaResponse> getAllCitas() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream().map(cita -> this.toResponse(cita)).toList();
    }

    private CitaResponse toResponse(Cita cita) {
        return CitaResponse.builder()
                .date(cita.getDate())
                .time(cita.getTime())
                .client(cita.getClient())
                .service(cita.getService())
                .stylist(cita.getStylist())
                .status(cita.getStatus())
                .notes(cita.getNotes())
                .build();
    }
}
