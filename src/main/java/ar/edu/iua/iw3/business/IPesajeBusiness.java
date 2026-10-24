
package ar.edu.iua.iw3.business;

import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.web.dto.InitialWeightDTO;
import ar.edu.iua.iw3.web.dto.FinalWeightDTO;
import ar.edu.iua.iw3.business.exception.BusinessException;
import ar.edu.iua.iw3.business.exception.NotFoundException;

public interface IPesajeBusiness {

    // Proceso 2: pesaje inicial desde TMS
    Order registerInitialWeight(InitialWeightDTO request)
            throws BusinessException, NotFoundException;

    // Nuevo contrato: pesaje final por número externo de orden
    Order registerFinalWeight(FinalWeightDTO request)
            throws BusinessException, NotFoundException;
}
