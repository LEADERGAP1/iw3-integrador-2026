package ar.edu.iua.iw3.business;

import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.web.dto.MeasurementDTO;
import ar.edu.iua.iw3.business.exception.BusinessException;
import ar.edu.iua.iw3.business.exception.NotFoundException;

public interface ICargaBusiness {

    // Activar una carga utilizando la contraseña
    Order activateLoad(Long idOrder, String password)
            throws BusinessException, NotFoundException;

    // Recibir una medición del dispositivo
    void processMeasurement(Long idOrder, MeasurementDTO measurement)
            throws BusinessException, NotFoundException;

    // Finalizar el proceso de carga
    Order closeLoad(Long idOrder)
            throws BusinessException, NotFoundException;

    // Activación mediante el número externo de orden
    Order activateLoadByNumeroOrden(Integer numeroOrden, String password)
            throws BusinessException, NotFoundException;

    // Mediciones mediante el número externo de orden
    void processMeasurementByNumeroOrden(
            Integer numeroOrden,
            MeasurementDTO measurement)
            throws BusinessException, NotFoundException;

    // Cierre mediante el número externo de orden
    Order closeLoadByNumeroOrden(Integer numeroOrden)
            throws BusinessException, NotFoundException;
}
