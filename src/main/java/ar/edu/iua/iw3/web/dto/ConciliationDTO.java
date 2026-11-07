package ar.edu.iua.iw3.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Valores devueltos por la consulta de conciliación")
public class ConciliationDTO {
    @Schema(description = "Número externo de orden", example = "15040")
    private Integer numeroOrden;
    @Schema(description = "Tara registrada en el pesaje inicial", example = "12500.0")
    private double tara;
    @Schema(description = "Peso bruto registrado en el pesaje final", example = "17000.0")
    private double pesajeFinal;
    @Schema(description = "Producto neto calculado por diferencia entre pesaje final y tara", example = "4500.0")
    private double productoCargadoBalanza;
    @Schema(description = "Última masa acumulada informada durante la carga", example = "4480.0")
    private double masaCaudalimetro;
    @Schema(description = "Diferencia entre producto según balanza y masa acumulada informada", example = "20.0")
    private double diferencia;
    @Schema(description = "Última temperatura registrada durante la carga", example = "20.0")
    private double temperaturaFinal;
    @Schema(description = "Última densidad registrada durante la carga", example = "0.55")
    private double densidadFinal;
}
