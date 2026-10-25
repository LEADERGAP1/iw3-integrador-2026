package ar.edu.iua.iw3.business.impl;

import ar.edu.iua.iw3.business.ICargaBusiness;
import ar.edu.iua.iw3.business.exception.BusinessException;
import ar.edu.iua.iw3.business.exception.NotFoundException;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.model.persistence.OrderRepository;
import ar.edu.iua.iw3.web.dto.MeasurementDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class CargaBusinessImpl implements ICargaBusiness {

    private final OrderRepository orderRepository;
    private final HistorialEstadoBusiness historialEstadoBusiness;

    public CargaBusinessImpl(
            OrderRepository orderRepository,
            HistorialEstadoBusiness historialEstadoBusiness) {

        this.orderRepository = orderRepository;
        this.historialEstadoBusiness = historialEstadoBusiness;
    }

    @Override
    @Transactional
    public Order activateLoad(Long idOrder, String password)
            throws BusinessException, NotFoundException {

        Order order = buscarOrden(idOrder);

        if (order.getEstado() != 2) {
            throw new IllegalArgumentException(
                "La orden no está en estado 2"
            );
        }

        if (!Objects.equals(order.getClaveActivacion(), password)
                || order.getClaveActivacion() == null) {
            throw new IllegalArgumentException(
                "Contraseña de activación incorrecta"
            );
        }

        if (order.getFechaInicioCarga() != null) {
            throw new IllegalArgumentException(
                "La carga ya fue activada"
            );
        }

        order.setFechaInicioCarga(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void processMeasurement(
            Long idOrder,
            MeasurementDTO measurement)
            throws BusinessException, NotFoundException {

        Order order = buscarOrden(idOrder);

        if (order.getEstado() != 2) {
            throw new IllegalArgumentException(
                "La orden no está en proceso de carga"
            );
        }

        if (order.getFechaInicioCarga() == null) {
            throw new IllegalArgumentException(
                "La carga todavía no fue activada"
            );
        }

        if (measurement == null) {
            throw new IllegalArgumentException(
                "La medición es obligatoria"
            );
        }

        if (!Double.isFinite(measurement.getMasa())
                || !Double.isFinite(measurement.getDensidad())
                || !Double.isFinite(measurement.getTemperatura())
                || measurement.getMasa() < 0
                || measurement.getMasa() > order.getPreset()) {

            throw new IllegalArgumentException(
                "La medición contiene valores inválidos"
            );
        }

        order.setUltimaMasaAcumulada(measurement.getMasa());
        order.setUltimaDensidad(measurement.getDensidad());
        order.setUltimaTemperatura(measurement.getTemperatura());
        order.setFechaUltimaMedicion(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order closeLoad(Long idOrder)
            throws BusinessException, NotFoundException {

        Order order = buscarOrden(idOrder);

        if (order.getEstado() != 2) {
            throw new IllegalArgumentException(
                "La carga solo puede cerrarse en estado 2"
            );
        }

        if (order.getFechaInicioCarga() == null) {
            throw new IllegalArgumentException(
                "No se puede cerrar una carga que no fue activada"
            );
        }

        order.setFechaFinCarga(LocalDateTime.now());
        order.setEstado(3);

        Order ordenGuardada = orderRepository.save(order);

        historialEstadoBusiness.registrarCambio(
            ordenGuardada,
            2,
            3,
            "DISPOSITIVO",
            "Proceso de carga finalizado"
        );

        return ordenGuardada;
    }

    private Order buscarOrden(Long idOrder)
            throws NotFoundException {

        return orderRepository.findById(idOrder)
            .orElseThrow(() -> new NotFoundException(
                "No se encontró la orden " + idOrder
            ));
    }
    @Override
    @Transactional
    public Order activateLoadByNumeroOrden(
            Integer numeroOrden, String password)
            throws BusinessException, NotFoundException {

        Order order = buscarPorNumeroOrden(numeroOrden);

        return activateLoad(order.getId(), password);
    }

    @Override
    @Transactional
    public void processMeasurementByNumeroOrden(
            Integer numeroOrden, MeasurementDTO measurement)
            throws BusinessException, NotFoundException {

        Order order = buscarPorNumeroOrden(numeroOrden);

        processMeasurement(order.getId(), measurement);
    }

    @Override
    @Transactional
    public Order closeLoadByNumeroOrden(Integer numeroOrden)
            throws BusinessException, NotFoundException {

        Order order = buscarPorNumeroOrden(numeroOrden);

        return closeLoad(order.getId());
    }

    private Order buscarPorNumeroOrden(Integer numeroOrden)
            throws NotFoundException {

        if (numeroOrden == null) {
            throw new IllegalArgumentException(
                "El número de orden es obligatorio"
            );
        }

        return orderRepository.findByNumeroOrden(numeroOrden)
            .orElseThrow(() -> new NotFoundException(
                "No se encontró la orden " + numeroOrden
            ));
    }
}
