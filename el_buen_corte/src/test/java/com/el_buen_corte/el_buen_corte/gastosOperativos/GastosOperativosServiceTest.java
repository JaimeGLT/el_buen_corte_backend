package com.el_buen_corte.el_buen_corte.gastosOperativos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GastosOperativosServiceTest {

    @Mock
    private GastosOperativosRepository gastosOperativosRepository;

    @InjectMocks
    private GastosOperativosService gastosOperativosService;

    // ----------------------------------------------------------------------------------
    // TEST 1: CREACIÓN - El camino feliz
    // ----------------------------------------------------------------------------------
    @Test
    @DisplayName("Create: Debería guardar y retornar el gasto cuando los datos son válidos")
    void createGastosOperativos_ShouldSaveAndReturn() {
        // ARRANGE
        GastosOperativosRequest request = new GastosOperativosRequest("Tijeras", 150.0, LocalDate.now());
        GastosOperativos expectedGasto = GastosOperativos.builder()
                .id(1L)
                .concepto("Tijeras")
                .monto(150.0)
                .fecha(LocalDate.now())
                .build();

        // Simulamos que al guardar cualquier cosa, el repo devuelve el objeto con ID
        when(gastosOperativosRepository.save(any(GastosOperativos.class))).thenReturn(expectedGasto);

        // ACT
        GastosOperativos result = gastosOperativosService.createGastosOperativos(request);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getConcepto()).isEqualTo("Tijeras");

        // Verificamos que se llamó al save 1 vez
        verify(gastosOperativosRepository, times(1)).save(any(GastosOperativos.class));
    }

    // ----------------------------------------------------------------------------------
    // TEST 2: ACTUALIZACIÓN - Manejo de errores
    // ----------------------------------------------------------------------------------
    @Test
    @DisplayName("Update: Debería lanzar RuntimeException si el ID no existe")
    void updateGastosOperativos_ShouldThrowException_WhenIdNotFound() {
        // ARRANGE
        Long idInexistente = 99L;
        GastosOperativosRequest request = new GastosOperativosRequest("Nada", 0.0, LocalDate.now());

        // Simulamos que no encuentra nada
        when(gastosOperativosRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> gastosOperativosService.updateGastosOperativos(idInexistente, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Gasto no encontrado con ID: " + idInexistente);

        // Aseguramos que NUNCA se intente guardar si no se encontró
        verify(gastosOperativosRepository, never()).save(any());
    }

    // ----------------------------------------------------------------------------------
    // TEST 3: ELIMINACIÓN
    // ----------------------------------------------------------------------------------
    @Test
    @DisplayName("Delete: Debería eliminar si el ID existe")
    void deleteGastosOperativos_ShouldDelete_WhenIdExists() {
        // ARRANGE
        Long id = 1L;
        when(gastosOperativosRepository.existsById(id)).thenReturn(true);

        // ACT
        gastosOperativosService.deleteGastosOperativos(id);

        // ASSERT
        verify(gastosOperativosRepository, times(1)).deleteById(id);
    }

    // ----------------------------------------------------------------------------------
    // TEST 4: REPORTES - Aquí es donde tu lógica actual es frágil
    // ----------------------------------------------------------------------------------
    @Test
    @DisplayName("Reports: Debería calcular promedios y totales correctamente")
    void reports_ShouldCalculateMetrics() {
        // ARRANGE
        // Simulamos datos para que las matemáticas cuadren
        Double totalExpensesMock = 1000.0;
        Double maxExpenseMock = 500.0;

        // Tu servicio llama a findAll().size(). Tenemos que mockear una lista.
        // OJO: Esto expone tu mal diseño de traer todos los datos.
        List<GastosOperativos> fakeList = Arrays.asList(
                new GastosOperativos(1L, "A", 500.0, LocalDate.now()),
                new GastosOperativos(2L, "B", 500.0, LocalDate.now()));

        when(gastosOperativosRepository.totalExpenses()).thenReturn(totalExpensesMock);
        when(gastosOperativosRepository.findAll()).thenReturn(fakeList); // <-- ESTO ES LENTO EN PROD
        when(gastosOperativosRepository.findMaxMontoByDateRange()).thenReturn(maxExpenseMock);

        // ACT
        GastosReportResponse response = gastosOperativosService.reports();

        // ASSERT
        assertThat(response.getTotalExpenses()).isEqualTo(1000.0);
        assertThat(response.getTotalRecords()).isEqualTo(2);

        // 1000 / 2 = 500
        assertThat(response.getAverageExpense()).isEqualTo(500.0);
        assertThat(response.getMajorExpense()).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Reports: Debería manejar división por cero (o Infinity) si no hay registros")
    void reports_ShouldHandleEmptyData() {
        // ARRANGE
        when(gastosOperativosRepository.totalExpenses()).thenReturn(0.0);
        when(gastosOperativosRepository.findAll()).thenReturn(Collections.emptyList());
        when(gastosOperativosRepository.findMaxMontoByDateRange()).thenReturn(0.0);

        // ACT
        GastosReportResponse response = gastosOperativosService.reports();

        // ASSERT
        assertThat(response.getTotalRecords()).isZero();

        // En Java, Double / 0.0 da NaN (Not a Number) o Infinity, no lanza excepción
        // aritmética como Integer.
        // Verifica si esto es lo que quieres devolver al frontend.
        assertThat(response.getAverageExpense()).isNaN();
    }
}