package com.el_buen_corte.el_buen_corte.chat;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import com.el_buen_corte.el_buen_corte.chat.dto.VentasHoyDTO;
import com.el_buen_corte.el_buen_corte.movement.MovementRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MovementRepository movementRepository;

    public VentasHoyDTO ventasHoy() {
        LocalDate today = LocalDate.now();
        return movementRepository.ventasHoy(today, today);
    }
}
