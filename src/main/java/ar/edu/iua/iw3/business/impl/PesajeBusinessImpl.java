
package ar.edu.iua.iw3.business.impl;

import ar.edu.iua.iw3.business.IPesajeBusiness;
import ar.edu.iua.iw3.business.exception.BusinessException;
import ar.edu.iua.iw3.business.exception.NotFoundException;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.model.persistence.OrderRepository;
import ar.edu.iua.iw3.web.dto.InitialWeightDTO;
import ar.edu.iua.iw3.web.dto.FinalWeightDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class PesajeBusinessImpl implements IPesajeBusiness {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private HistorialEstadoBusiness historialEstadoBusiness;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public Order registerInitialWeight(InitialWeightDTO request)
            throws BusinessException, NotFoundException {

        // 1. Validar los datos recibidos desde TMS
        if (request == null
                || request.getNumeroOrden() == null
                || request.getTaraKg() == null
                || request.getTaraKg().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                "El número de orden y la tara positiva son obligatorios"
            );
        }

        // 2. Buscar la orden por su número externo
        Order order = orderRepository
            .findByNumeroOrden(request.getNumeroOrden())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la orden " + request.getNumeroOrden()
            ));

        // 3. Verificar el estado
        if (order.getEstado() != 1) {
            throw new IllegalArgumentException(
                "El pesaje inicial solo se permite en estado 1"
            );
        }

        // 4. Generar la clave de activación
        String clave = String.format(
            "%05d",
            secureRandom.nextInt(100000)
        );

        // 5. Registrar el pesaje inicial
        order.setPesoInicialKg(request.getTaraKg());
        order.setFechaPesajeInicial(LocalDateTime.now());
        order.setClaveActivacion(clave);

        // 6. Actualizar el estado
                
        order.setEstado(2);

        Order ordenGuardada = orderRepository.save(order);

        historialEstadoBusiness.registrarCambio(
            ordenGuardada,
            1,
            2,
            "TMS",
            "Pesaje inicial registrado"
        );

        return ordenGuardada;

    }

    @Override
    @Transactional
    public Order registerFinalWeight(FinalWeightDTO request)
            throws BusinessException, NotFoundException {

        // 1. Validar la información recibida desde TMS
        if (request == null
                || request.getNumeroOrden() == null
                || request.getPesoFinalKg() == null
                || request.getPesoFinalKg().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                "El número de orden y el peso final positivo son obligatorios"
            );
        }

        // 2. Buscar la orden por el número externo
        Order order = orderRepository
            .findByNumeroOrden(request.getNumeroOrden())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la orden " + request.getNumeroOrden()
            ));

        // 3. Validar el estado
        if (order.getEstado() != 3) {
            throw new IllegalArgumentException(
                "El pesaje final solo se permite en estado 3"
            );
        }

        // 4. Verificar que exista un pesaje inicial
        if (order.getPesoInicialKg() == null) {
            throw new IllegalArgumentException(
                "La orden no tiene registrado un pesaje inicial"
            );
        }

        // 5. El peso final no puede ser inferior a la tara
        if (request.getPesoFinalKg()
                .compareTo(order.getPesoInicialKg()) < 0) {

            throw new IllegalArgumentException(
                "El peso final no puede ser inferior a la tara inicial"
            );
        }

        // 6. Guardar el pesaje final
        order.setPesoFinalKg(request.getPesoFinalKg());
        order.setFechaPesajeFinal(LocalDateTime.now());

        // 7. Cambiar al estado 4
        order.setEstado(4);

        Order ordenGuardada = orderRepository.save(order);

        // 8. Registrar la transición en auditoría
        historialEstadoBusiness.registrarCambio(
            ordenGuardada,
            3,
            4,
            "TMS",
            "Pesaje final registrado"
        );

        return ordenGuardada;
    }

}
