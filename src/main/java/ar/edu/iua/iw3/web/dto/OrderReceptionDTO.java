
package ar.edu.iua.iw3.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderReceptionDTO {

    private Integer numeroOrden;
    private LocalDateTime fechaCargaPrevista;
    private BigDecimal presetKg;

    private CamionDTO camion;
    private ChoferDTO chofer;
    private ClienteDTO cliente;
    private ProductoDTO producto;

    @Getter
    @Setter
    public static class CamionDTO {
        private String codigoExterno;
        private String patente;
        private String descripcion;
        private List<CisternaDTO> cisternas;
    }

    @Getter
    @Setter
    public static class CisternaDTO {
        private String numero;
        private BigDecimal capacidadLitros;
    }

    @Getter
    @Setter
    public static class ChoferDTO {
        private String codigoExterno;
        private String nombre;
        private String apellido;
        private String documento;
    }

    @Getter
    @Setter
    public static class ClienteDTO {
        private String codigoExterno;
        private String razonSocial;
        private String contacto;
    }

    @Getter
    @Setter
    public static class ProductoDTO {
        private String codigoExterno;
        private String nombre;
        private String descripcion;
    }
}
