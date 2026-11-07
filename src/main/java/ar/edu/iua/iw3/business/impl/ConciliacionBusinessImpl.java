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
    public ConciliationDTO getConciliationByNumeroOrden(
            Integer numeroOrden)
            throws BusinessException, NotFoundException {

        // 1. Validar el número de orden
        if (numeroOrden == null) {
            throw new IllegalArgumentException(
                "El número de orden es obligatorio"
            );
        }

        // 2. Buscar la orden por su número externo
        Order order = orderRepository
            .findByNumeroOrden(numeroOrden)
            .orElseThrow(() -> new NotFoundException(
                "No se encontró la orden " + numeroOrden
            ));

        // 3. La conciliación solo corresponde al estado 4
        if (order.getEstado() != 4) {
            throw new IllegalArgumentException(
                "La conciliación solo está disponible en estado 4"
            );
        }

        // 4. Verificar que existan los datos necesarios
        if (order.getPesoInicialKg() == null
                || order.getPesoFinalKg() == null
                || order.getUltimaMasaKg() == null) {

            throw new IllegalArgumentException(
                "La orden no tiene todos los datos para conciliar"
            );
        }

        // 5. Calcular producto según la balanza
        double productoCargadoBalanza =
                order.getPesajeFinal() - order.getTara();

        // 6. Comparar contra la masa registrada
        double diferencia =
                productoCargadoBalanza
                - order.getUltimaMasaAcumulada();

        // 7. Construir el resultado
        return new ConciliationDTO(
            order.getNumeroOrden(),
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
