package ar.edu.iua.iw3.web.dto;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Medición recibida durante el proceso de carga")
public class MeasurementDTO {
    @Schema(description = "Masa acumulada reportada por el dispositivo", example = "2500.0", minimum = "0", exclusiveMinimum = true, requiredMode = Schema.RequiredMode.REQUIRED)
    private Double masa;
    @Schema(description = "Densidad reportada por el dispositivo", example = "0.55", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double densidad;
    @Schema(description = "Temperatura reportada por el dispositivo", example = "20.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double temperatura;
    @Schema(description = "Caudal reportado por el dispositivo", example = "1200.0", minimum = "0", exclusiveMinimum = true, requiredMode = Schema.RequiredMode.REQUIRED)
    private Double caudal;
}
