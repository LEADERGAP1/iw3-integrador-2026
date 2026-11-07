package ar.edu.iua.iw3;

import ar.edu.iua.iw3.business.impl.CargaBusinessImpl;
import ar.edu.iua.iw3.business.impl.HistorialEstadoBusiness;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.model.persistence.OrderRepository;
import ar.edu.iua.iw3.web.dto.MeasurementDTO;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CargaBusinessImplTest {

    @Test
    void rechazaMedicionConCaudalCeroONegativo() {
        for (double caudal : new double[] {0.0, -1.0}) {
            Order order = ordenEnCarga();
            OrderRepository orderRepository = mock(OrderRepository.class);
            when(orderRepository.findByNumeroOrden(15040))
                    .thenReturn(Optional.of(order));
            CargaBusinessImpl business = new CargaBusinessImpl(
                    orderRepository, mock(HistorialEstadoBusiness.class));

            assertThrows(IllegalArgumentException.class,
                    () -> business.processMeasurementByNumeroOrden(
                            15040, medicion(100.0, caudal)));
            verify(orderRepository, never()).save(order);
        }
    }

    @Test
    void medicionValidaActualizaElUltimoCaudal() throws Exception {
        Order order = ordenEnCarga();
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findByNumeroOrden(15040))
                .thenReturn(Optional.of(order));
        CargaBusinessImpl business = new CargaBusinessImpl(
                orderRepository, mock(HistorialEstadoBusiness.class));

        business.processMeasurementByNumeroOrden(15040, medicion(100.0, 240.5));

        assertEquals(new BigDecimal("240.5"), order.getUltimoCaudalKgHora());
        verify(orderRepository).save(order);
    }

    @Test
    void rechazaMasaAcumuladaCeroONegativa() {
        for (double masa : new double[] {0.0, -1.0}) {
            Order order = ordenEnCarga();
            OrderRepository orderRepository = mock(OrderRepository.class);
            when(orderRepository.findByNumeroOrden(15040))
                    .thenReturn(Optional.of(order));
            CargaBusinessImpl business = new CargaBusinessImpl(
                    orderRepository, mock(HistorialEstadoBusiness.class));

            assertThrows(IllegalArgumentException.class,
                    () -> business.processMeasurementByNumeroOrden(
                            15040, medicion(masa, 240.5)));
            verify(orderRepository, never()).save(order);
        }
    }

    @Test
    void rechazaMasaAcumuladaMenorALaUltimaMedicion() {
        Order order = ordenEnCarga();
        order.setUltimaMasaKg(new BigDecimal("200.0"));
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findByNumeroOrden(15040))
                .thenReturn(Optional.of(order));
        CargaBusinessImpl business = new CargaBusinessImpl(
                orderRepository, mock(HistorialEstadoBusiness.class));

        assertThrows(IllegalArgumentException.class,
                () -> business.processMeasurementByNumeroOrden(
                        15040, medicion(199.0, 240.5)));
        verify(orderRepository, never()).save(order);
    }

    @Test
    void rechazaMedicionConCamposObligatoriosAusentes() {
        Order order = ordenEnCarga();
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findByNumeroOrden(15040))
                .thenReturn(Optional.of(order));
        CargaBusinessImpl business = new CargaBusinessImpl(
                orderRepository, mock(HistorialEstadoBusiness.class));
        MeasurementDTO measurement = medicion(100.0, 240.5);
        measurement.setDensidad(null);

        assertThrows(IllegalArgumentException.class,
                () -> business.processMeasurementByNumeroOrden(
                        15040, measurement));
        verify(orderRepository, never()).save(order);
    }

    private Order ordenEnCarga() {
        Order order = new Order();
        order.setNumeroOrden(15040);
        order.setEstado(2);
        order.setPresetKg(new BigDecimal("5000.0"));
        order.setFechaInicioCarga(LocalDateTime.now());
        return order;
    }

    private MeasurementDTO medicion(double masa, double caudal) {
        MeasurementDTO measurement = new MeasurementDTO();
        measurement.setMasa(masa);
        measurement.setDensidad(0.55);
        measurement.setTemperatura(20.0);
        measurement.setCaudal(caudal);
        return measurement;
    }
}
