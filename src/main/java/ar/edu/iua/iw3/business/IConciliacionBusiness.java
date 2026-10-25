package ar.edu.iua.iw3.business;

import ar.edu.iua.iw3.web.dto.ConciliationDTO;
import ar.edu.iua.iw3.business.exception.BusinessException;
import ar.edu.iua.iw3.business.exception.NotFoundException;

public interface IConciliacionBusiness {

    // Consultar la conciliación de una orden finalizada
    ConciliationDTO getConciliation(Long idOrder)
            throws BusinessException, NotFoundException;

}
