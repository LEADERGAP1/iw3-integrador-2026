
package ar.edu.iua.iw3.web.dto;

import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "Orden de carga y datos maestros asociados recibidos desde SAP")
public class OrderReceptionDTO {

    @Schema(
        description = "Número único de orden recibido desde SAP",
        example = "15040",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer numeroOrden;

    @Schema(
        description = "Fecha y hora previstas para realizar la carga",
        example = "2026-11-05T09:30:00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime fechaCargaPrevista;

    @Schema(
        description = "Masa de producto prevista para cargar, en kilogramos",
        example = "8000.000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal presetKg;

    @Schema(description = "Camión y cisternas asignados a la orden", requiredMode = Schema.RequiredMode.REQUIRED)
    private CamionDTO camion;
    @Schema(description = "Chofer asignado a la orden", requiredMode = Schema.RequiredMode.REQUIRED)
    private ChoferDTO chofer;
    @Schema(description = "Cliente asociado a la orden", requiredMode = Schema.RequiredMode.REQUIRED)
    private ClienteDTO cliente;
    @Schema(description = "Producto solicitado en la orden", requiredMode = Schema.RequiredMode.REQUIRED)
    private ProductoDTO producto;

    @Getter
    @Setter
    public static class CamionDTO {
        @Schema(description = "Código externo del camión", example = "CAM-001", requiredMode = Schema.RequiredMode.REQUIRED)
        private String codigoExterno;
        @Schema(description = "Patente del camión", example = "AB123CD", requiredMode = Schema.RequiredMode.REQUIRED)
        private String patente;
        @Schema(description = "Descripción del camión", example = "Camion cisterna")
        private String descripcion;
        @Schema(description = "Cisternas asociadas al camión", requiredMode = Schema.RequiredMode.REQUIRED)
        private List<CisternaDTO> cisternas;
    }

    @Getter
    @Setter
    public static class CisternaDTO {
        @Schema(description = "Número de la cisterna", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        private String numero;
        @Schema(description = "Capacidad de la cisterna en litros", example = "10000.00", requiredMode = Schema.RequiredMode.REQUIRED)
        private BigDecimal capacidadLitros;
    }

    @Getter
    @Setter
    public static class ChoferDTO {
        @Schema(description = "Código externo del chofer", example = "CHO-001", requiredMode = Schema.RequiredMode.REQUIRED)
        private String codigoExterno;
        @Schema(description = "Nombre del chofer", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
        private String nombre;
        @Schema(description = "Apellido del chofer", example = "Perez", requiredMode = Schema.RequiredMode.REQUIRED)
        private String apellido;
        @Schema(description = "Documento del chofer", example = "30123456")
        private String documento;
    }

    @Getter
    @Setter
    public static class ClienteDTO {
        @Schema(description = "Código externo del cliente", example = "CLI-001", requiredMode = Schema.RequiredMode.REQUIRED)
        private String codigoExterno;
        @Schema(description = "Razón social del cliente", example = "Empresa del Centro SA", requiredMode = Schema.RequiredMode.REQUIRED)
        private String razonSocial;
        @Schema(description = "Contacto del cliente", example = "administracion@empresa.com")
        private String contacto;
    }

    @Getter
    @Setter
    public static class ProductoDTO {
        @Schema(description = "Código externo del producto", example = "PROD-001", requiredMode = Schema.RequiredMode.REQUIRED)
        private String codigoExterno;
        @Schema(description = "Nombre del producto", example = "Gas licuado", requiredMode = Schema.RequiredMode.REQUIRED)
        private String nombre;
        @Schema(description = "Descripción del producto", example = "Producto para carga industrial")
        private String descripcion;
    }
}
