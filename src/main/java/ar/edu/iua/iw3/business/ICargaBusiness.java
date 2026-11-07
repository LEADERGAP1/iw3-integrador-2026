package ar.edu.iua.iw3.business;

import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.web.dto.MeasurementDTO;
import ar.edu.iua.iw3.business.exception.BusinessException;
import ar.edu.iua.iw3.business.exception.NotFoundException;

public interface ICargaBusiness {

    Order activateLoadByNumeroOrden(
            Integer numeroOrden, String password)
            throws BusinessException, NotFoundException;

    void processMeasurementByNumeroOrden(
            Integer numeroOrden, MeasurementDTO measurement)
            throws BusinessException, NotFoundException;

    Order closeLoadByNumeroOrden(Integer numeroOrden)
            throws BusinessException, NotFoundException;
}
