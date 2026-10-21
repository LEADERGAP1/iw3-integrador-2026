package ar.edu.iua.iw3.business.impl;

import ar.edu.iua.iw3.business.IOrderBusiness;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.model.persistence.OrderRepository;
import ar.edu.iua.iw3.web.dto.ConciliationDTO;
import ar.edu.iua.iw3.web.dto.MeasurementDTO;
import ar.edu.iua.iw3.business.exception.BusinessException;
import ar.edu.iua.iw3.business.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderBusinessImpl implements IOrderBusiness {

    @Autowired
    private OrderRepository orderRepository;

    private Order load(Long id) throws NotFoundException {
        Optional<Order> op = orderRepository.findById(id);
        if (op.isEmpty()) throw new NotFoundException("No se encontró la orden " + id);
        return op.get();
    }

    @Override
    public Order activateLoad(Long idOrder, String password) throws BusinessException, NotFoundException {
        Order order = load(idOrder);
        if (order.getEstado() != 2) throw new IllegalArgumentException("La orden no está en Estado 2.");
        if (!order.getPassword().equals(password)) throw new IllegalArgumentException("Contraseña incorrecta.");
        return order;
    }

    @Override
    public void processMeasurement(Long idOrder, MeasurementDTO measurement) throws BusinessException, NotFoundException {
        Order order = load(idOrder);
        if (order.getEstado() != 2) throw new IllegalArgumentException("La orden no está en carga.");
        if (measurement.getMasa() > order.getPreset()) throw new IllegalArgumentException("Masa supera el preset.");
        
        order.setUltimaMasaAcumulada(measurement.getMasa());
        order.setUltimaDensidad(measurement.getDensidad());
        order.setUltimaTemperatura(measurement.getTemperatura());
        orderRepository.save(order);
    }

    @Override
    public Order closeLoad(Long idOrder) throws BusinessException, NotFoundException {
        Order order = load(idOrder);
        if (order.getEstado() != 2) throw new IllegalArgumentException("Solo se cierra en Estado 2.");
        order.setEstado(3);
        return orderRepository.save(order);
    }

    @Override
    public Order registerFinalWeight(Long idOrder, double pesajeFinal) throws BusinessException, NotFoundException {
        Order order = load(idOrder);
        if (order.getEstado() != 3) throw new IllegalArgumentException("Debe estar en Estado 3.");
        order.setPesajeFinal(pesajeFinal);
        order.setEstado(4);
        return orderRepository.save(order);
    }

    @Override
    public ConciliationDTO getConciliation(Long idOrder) throws BusinessException, NotFoundException {
        Order order = load(idOrder);
        if (order.getEstado() != 4) throw new IllegalArgumentException("Solo disponible en Estado 4.");
        
        double productoCargadoBalanza = order.getPesajeFinal() - order.getTara();
        double diferencia = productoCargadoBalanza - order.getUltimaMasaAcumulada();

        return new ConciliationDTO(
            order.getId(), order.getTara(), order.getPesajeFinal(),
            productoCargadoBalanza, order.getUltimaMasaAcumulada(),
            diferencia, order.getUltimaTemperatura(), order.getUltimaDensidad()
        );
    }
}