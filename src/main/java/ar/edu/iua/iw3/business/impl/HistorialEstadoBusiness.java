
package ar.edu.iua.iw3.business.impl;

import ar.edu.iua.iw3.model.HistorialEstado;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.model.persistence.HistorialEstadoRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistorialEstadoBusiness {

    private final HistorialEstadoRepository historialRepository;

    public HistorialEstadoBusiness(
            HistorialEstadoRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    public void registrarCambio(
            Order orden,
            Integer estadoAnterior,
            Integer estadoNuevo,
            String responsable,
            String observaciones) {

        HistorialEstado historial = new HistorialEstado();

        historial.setOrden(orden);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setFechaCambio(LocalDateTime.now());
        historial.setResponsable(responsable);
        historial.setObservaciones(observaciones);

        historialRepository.save(historial);
    }
}
