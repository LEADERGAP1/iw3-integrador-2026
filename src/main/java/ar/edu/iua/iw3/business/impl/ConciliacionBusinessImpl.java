package ar.edu.iua.iw3.business.impl;

import ar.edu.iua.iw3.business.IConciliacionBusiness;
import ar.edu.iua.iw3.business.exception.BusinessException;
import ar.edu.iua.iw3.business.exception.NotFoundException;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.model.persistence.OrderRepository;
import ar.edu.iua.iw3.web.dto.ConciliationDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConciliacionBusinessImpl
        implements IConciliacionBusiness {

    private final OrderRepository orderRepository;

    public ConciliacionBusinessImpl(
            OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public ConciliationDTO getConciliationByNumeroOrden(Integer numeroOrden)
            throws BusinessException, NotFoundException {

        Order order = orderRepository.findByNumeroOrden(numeroOrden)
            .orElseThrow(() -> new NotFoundException(
                "No se encontró la orden " + numeroOrden
            ));

        return getConciliation(order.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ConciliationDTO getConciliation(Long idOrder)
            throws BusinessException, NotFoundException {

        // 1. Buscar la orden
        Order order = orderRepository.findById(idOrder)
            .orElseThrow(() -> new NotFoundException(
                "No se encontró la orden " + idOrder
            ));

        // 2. Validar que tenga el pesaje final
        if (order.getEstado() != 4) {
            throw new IllegalArgumentException(
                "La conciliación solo está disponible en estado 4"
            );
        }

        // 3. Calcular la masa neta según la balanza
        double productoCargadoBalanza =
            order.getPesajeFinal() - order.getTara();

        // 4. Calcular la diferencia con el dispositivo
        double diferencia =
            productoCargadoBalanza
            - order.getUltimaMasaAcumulada();

        // 5. Construir el resultado
        return new ConciliationDTO(
            order.getId(),
            order.getTara(),
            order.getPesajeFinal(),
            productoCargadoBalanza,
            order.getUltimaMasaAcumulada(),
            diferencia,
            order.getUltimaTemperatura(),
            order.getUltimaDensidad()
        );
    }
}
