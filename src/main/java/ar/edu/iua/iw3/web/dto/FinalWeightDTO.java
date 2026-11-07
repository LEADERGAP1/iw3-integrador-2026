
package ar.edu.iua.iw3.web.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Datos del pesaje final recibidos desde TMS")
public class FinalWeightDTO {

    @Schema(
        description = "Número externo de orden enviado por SAP",
        example = "15040",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer numeroOrden;

    @Schema(
        description = "Peso bruto del camión cargado en kilogramos",
        example = "17000.000",
        minimum = "0",
        exclusiveMinimum = true,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal pesoFinalKg;
}
