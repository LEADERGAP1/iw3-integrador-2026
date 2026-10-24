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


import ar.edu.iua.iw3.model.Camion;
import ar.edu.iua.iw3.model.Cisterna;
import ar.edu.iua.iw3.model.Chofer;
import ar.edu.iua.iw3.model.Cliente;
import ar.edu.iua.iw3.model.Producto;

import ar.edu.iua.iw3.model.persistence.CamionRepository;
import ar.edu.iua.iw3.model.persistence.CisternaRepository;
import ar.edu.iua.iw3.model.persistence.ChoferRepository;
import ar.edu.iua.iw3.model.persistence.ClienteRepository;
import ar.edu.iua.iw3.model.persistence.ProductoRepository;

import ar.edu.iua.iw3.web.dto.OrderReceptionDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.math.BigDecimal;


import java.util.Optional;

@Service
public class OrderBusinessImpl implements IOrderBusiness {

   
     @Autowired
    private HistorialEstadoBusiness historialEstadoBusiness;

    
    @Autowired
    private OrderRepository orderRepository;

    
    @Autowired
    private CamionRepository camionRepository;

    @Autowired
    private CisternaRepository cisternaRepository;

    @Autowired
    private ChoferRepository choferRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    

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

    
@Override
@Transactional
public Order receiveOrder(OrderReceptionDTO request)
        throws BusinessException {

    // 1. Validar la solicitud
    if (request == null
            || request.getNumeroOrden() == null
            || request.getFechaCargaPrevista() == null
            || request.getPresetKg() == null
            || request.getPresetKg().compareTo(BigDecimal.ZERO) <= 0
            || request.getCamion() == null
            || request.getChofer() == null
            || request.getCliente() == null
            || request.getProducto() == null) {

        throw new IllegalArgumentException(
            "La orden contiene datos obligatorios inválidos"
        );
    }

    // 2. Evitar números de orden duplicados
    if (orderRepository.existsByNumeroOrden(
            request.getNumeroOrden())) {

        throw new IllegalArgumentException(
            "La orden ya existe: " + request.getNumeroOrden()
        );
    }

    // 3. Sincronizar las entidades externas
    Camion camion = sincronizarCamion(request.getCamion());
    Chofer chofer = sincronizarChofer(request.getChofer());
    Cliente cliente = sincronizarCliente(request.getCliente());
    Producto producto = sincronizarProducto(request.getProducto());

    // 4. Crear la orden
    Order order = new Order();

    order.setNumeroOrden(request.getNumeroOrden());
    order.setFechaCargaPrevista(request.getFechaCargaPrevista());
    order.setFechaRecepcion(LocalDateTime.now());
    order.setPresetKg(request.getPresetKg());

    order.setCamion(camion);
    order.setChofer(chofer);
    order.setCliente(cliente);
    order.setProducto(producto);

    // 5. Estado inicial obligatorio
    order.setEstado(1);

    // 6. Persistir y devolver
    
   
    Order ordenGuardada = orderRepository.save(order);

    historialEstadoBusiness.registrarCambio(
        ordenGuardada,
        null,
        1,
        "SAP",
        "Orden recibida desde SAP"
    );

    return ordenGuardada;

    }

    
private Camion sincronizarCamion(
        OrderReceptionDTO.CamionDTO dto) {

    validarTexto(dto.getCodigoExterno(), "Código del camión");
    validarTexto(dto.getPatente(), "Patente");

    Camion camion = camionRepository
        .findByCodigoExterno(dto.getCodigoExterno())
        .orElseGet(Camion::new);

    camion.setCodigoExterno(dto.getCodigoExterno());
    camion.setPatente(dto.getPatente());
    camion.setDescripcion(dto.getDescripcion());

    camion = camionRepository.save(camion);

    if (dto.getCisternas() == null || dto.getCisternas().isEmpty()) {
        throw new IllegalArgumentException(
            "El camión debe informar sus cisternas"
        );
    }

    for (OrderReceptionDTO.CisternaDTO item : dto.getCisternas()) {

        validarTexto(item.getNumero(), "Número de cisterna");

        if (item.getCapacidadLitros() == null
                || item.getCapacidadLitros()
                       .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                "Capacidad de cisterna inválida"
            );
        }

        Cisterna cisterna = cisternaRepository
            .findByCamionIdAndNumero(camion.getId(), item.getNumero())
            .orElseGet(Cisterna::new);

        cisterna.setCamion(camion);
        cisterna.setNumero(item.getNumero());
        cisterna.setCapacidadLitros(item.getCapacidadLitros());

        cisternaRepository.save(cisterna);
    }

    return camion;
    
    }

    
private Chofer sincronizarChofer(
        OrderReceptionDTO.ChoferDTO dto) {

    validarTexto(dto.getCodigoExterno(), "Código del chofer");
    validarTexto(dto.getNombre(), "Nombre del chofer");
    validarTexto(dto.getApellido(), "Apellido del chofer");

    Chofer chofer = choferRepository
        .findByCodigoExterno(dto.getCodigoExterno())
        .orElseGet(Chofer::new);

    chofer.setCodigoExterno(dto.getCodigoExterno());
    chofer.setNombre(dto.getNombre());
    chofer.setApellido(dto.getApellido());
    chofer.setDocumento(dto.getDocumento());

    return choferRepository.save(chofer);

    }

    
private Cliente sincronizarCliente(
        OrderReceptionDTO.ClienteDTO dto) {

    validarTexto(dto.getCodigoExterno(), "Código del cliente");
    validarTexto(dto.getRazonSocial(), "Razón social");

    Cliente cliente = clienteRepository
        .findByCodigoExterno(dto.getCodigoExterno())
        .orElseGet(Cliente::new);

    cliente.setCodigoExterno(dto.getCodigoExterno());
    cliente.setRazonSocial(dto.getRazonSocial());
    cliente.setContacto(dto.getContacto());

    return clienteRepository.save(cliente);
    
    }

    
private Producto sincronizarProducto(
        OrderReceptionDTO.ProductoDTO dto) {

    validarTexto(dto.getCodigoExterno(), "Código del producto");
    validarTexto(dto.getNombre(), "Nombre del producto");

    Producto producto = productoRepository
        .findByCodigoExterno(dto.getCodigoExterno())
        .orElseGet(Producto::new);

    producto.setCodigoExterno(dto.getCodigoExterno());
    producto.setNombre(dto.getNombre());
    producto.setDescripcion(dto.getDescripcion());

    return productoRepository.save(producto);

    }

    
    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                "El campo " + campo + " es obligatorio"
            );
        }
    }

}
