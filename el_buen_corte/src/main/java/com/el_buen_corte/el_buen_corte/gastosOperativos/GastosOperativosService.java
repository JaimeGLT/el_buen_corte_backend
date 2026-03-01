package com.el_buen_corte.el_buen_corte.gastosOperativos;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GastosOperativosService {

    private final GastosOperativosRepository gastosOperativosRepository;

    public GastosOperativos createGastosOperativos(GastosOperativosRequest gastosOperativosRequest) {
        GastosOperativos gastosOperativos = GastosOperativos.builder()
                .concepto(gastosOperativosRequest.getConcepto())
                .monto(gastosOperativosRequest.getMonto())
                .fecha(gastosOperativosRequest.getFecha())
                .build();
        return gastosOperativosRepository.save(gastosOperativos);
    }

    public List<GastosOperativos> getAllGastosOperativos() {
        return gastosOperativosRepository.findAll();
    }

    // ... import java.util.NoSuchElementException;

    public GastosOperativos updateGastosOperativos(Long id, GastosOperativosRequest request) {
        // 1. Buscamos primero. Si no existe, GRITAMOS (Lanzamos excepción).
        GastosOperativos gastoExistente = gastosOperativosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado con ID: " + id));

        // 2. Actualizamos SOLO lo que nos interesa.
        // Aquí es donde un Mapper sería útil, pero hazlo a mano para que entiendas el
        // costo.
        gastoExistente.setConcepto(request.getConcepto());
        gastoExistente.setMonto(request.getMonto());
        gastoExistente.setFecha(request.getFecha());

        // 3. Guardamos la entidad ya existente con los nuevos datos.
        return gastosOperativosRepository.save(gastoExistente);
    }

    public void deleteGastosOperativos(Long id) {
        // 1. Validamos existencia antes de intentar borrar.
        if (!gastosOperativosRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Gasto no encontrado con ID: " + id);
        }
        gastosOperativosRepository.deleteById(id);
    }

    public GastosOperativos getGastoOperativoById(Long id) {
        return gastosOperativosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado con ID: " + id));
    }

    public GastosReportResponse reports() {
        Double totalExpenses = gastosOperativosRepository.totalExpenses();
        int totalRecords = gastosOperativosRepository.findAll().size();

        Double averageExpense = totalExpenses / totalRecords;

        Double majorExpense = gastosOperativosRepository.findMaxMontoByDateRange();

        return GastosReportResponse.builder()
                .totalExpenses(totalExpenses)
                .totalRecords(totalRecords)
                .averageExpense(averageExpense)
                .majorExpense(majorExpense)
                .build();
    }

}
