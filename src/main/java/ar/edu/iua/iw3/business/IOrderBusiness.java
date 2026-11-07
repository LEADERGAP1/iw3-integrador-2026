package ar.edu.iua.iw3.business;

import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.web.dto.OrderReceptionDTO;
import ar.edu.iua.iw3.business.exception.BusinessException;


public interface IOrderBusiness {

    Order receiveOrder(OrderReceptionDTO request)
            throws BusinessException;

}
