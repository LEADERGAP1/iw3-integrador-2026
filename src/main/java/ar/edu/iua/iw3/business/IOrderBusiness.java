package ar.edu.iua.iw3.business;

import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.web.dto.ConciliationDTO;
import ar.edu.iua.iw3.web.dto.MeasurementDTO;
import ar.edu.iua.iw3.business.exception.BusinessException;
import ar.edu.iua.iw3.business.exception.NotFoundException;

public interface IOrderBusiness {
    Order activateLoad(Long idOrder, String password) throws BusinessException, NotFoundException;
    void processMeasurement(Long idOrder, MeasurementDTO measurement) throws BusinessException, NotFoundException;
    Order closeLoad(Long idOrder) throws BusinessException, NotFoundException;
    Order registerFinalWeight(Long idOrder, double pesajeFinal) throws BusinessException, NotFoundException;
    ConciliationDTO getConciliation(Long idOrder) throws BusinessException, NotFoundException;
}