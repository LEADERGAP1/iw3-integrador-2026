package ar.edu.iua.iw3.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@Schema(description = "Resultado del registro de tara; password generada para activar la carga")
public class InitialWeightResponseDTO {

    @Schema(description = "Mensaje del resultado", example = "Pesaje inicial registrado correctamente")
    private final String mensaje;
    @Schema(description = "Número externo de orden", example = "15040")
    private final Integer numeroOrden;
    @Schema(description = "Estado actual de la orden", example = "2")
    private final int estado;
    @Schema(description = "Password de activación generada para esta orden", example = "12345")
    private final String password;
}
