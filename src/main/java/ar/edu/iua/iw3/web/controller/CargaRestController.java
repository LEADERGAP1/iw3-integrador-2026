package ar.edu.iua.iw3.web.controller;

import ar.edu.iua.iw3.business.ICargaBusiness;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.web.dto.MeasurementDTO;
import ar.edu.iua.iw3.business.exception.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "03 - Proceso de carga",
    description = "Activación, mediciones y cierre de la carga"
)
@RestController
@RequestMapping("/api/v1/orders")
public class CargaRestController {

    private final ICargaBusiness cargaBusiness;

    public CargaRestController(ICargaBusiness cargaBusiness) {
        this.cargaBusiness = cargaBusiness;
    }

    @Operation(
        summary = "Activar carga",
        description = "Valida el password de activación para la orden identificada por su número externo y registra el inicio de carga."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Carga activada correctamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(type = "object"),
            examples = @ExampleObject(value = """
                {"mensaje":"Carga activada correctamente","numeroOrden":15040,"estado":2}
                """)
        )
    )
    @ApiResponse(responseCode = "400", description = "Password incorrecta o la orden no está en un estado que permita activar la carga", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Contraseña de activación incorrecta")))
    @ApiResponse(responseCode = "404", description = "No existe una orden con ese número externo", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "No se encontró la orden 15040")))
    @ApiResponse(responseCode = "500", description = "Error interno al activar la carga", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error interno al activar la carga")))
    @PostMapping("/{numeroOrden}/activate")
    public ResponseEntity<?> activateLoad(
            @Parameter(description = "Número externo de orden (numeroOrden)", example = "15040", required = true)
            @PathVariable("numeroOrden") Integer numeroOrden,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Password generada al registrar el pesaje inicial",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"password\":\"12345\"}"),
                    examples = @ExampleObject(value = "{\"password\":\"12345\"}")
                )
            )
            @RequestBody Map<String, String> request) {

        try {
            String password = request.get("password");

            Order order = cargaBusiness.activateLoadByNumeroOrden(
                    numeroOrden, password);

            return ResponseEntity.ok(Map.of(
                "mensaje", "Carga activada correctamente",
                "numeroOrden", order.getNumeroOrden(),
                "estado", order.getEstado()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al activar la carga");
        }
    }

    @Operation(
        summary = "Registrar medición de carga",
        description = "Recibe y procesa una medición para la orden identificada por su número externo."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Medición recibida correctamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(type = "object"),
            examples = @ExampleObject(value = "{\"mensaje\":\"Medición recibida correctamente\"}")
        )
    )
    @ApiResponse(responseCode = "400", description = "Medición inválida o la orden no admite mediciones en su estado actual", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "La medición contiene valores inválidos")))
    @ApiResponse(responseCode = "404", description = "No existe una orden con ese número externo", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "No se encontró la orden 15040")))
    @ApiResponse(responseCode = "500", description = "Error interno al procesar la medición", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error interno al procesar la medición")))
    @PostMapping("/{numeroOrden}/measurements")
    public ResponseEntity<?> processMeasurement(
            @Parameter(description = "Número externo de orden (numeroOrden)", example = "15040", required = true)
            @PathVariable("numeroOrden") Integer numeroOrden,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Medición recibida durante el proceso de carga",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MeasurementDTO.class),
                    examples = @ExampleObject(value = "{\"masa\":2500.0,\"densidad\":0.55,\"temperatura\":20.0,\"caudal\":1200.0}")
                )
            )
            @RequestBody MeasurementDTO request) {

        try {
            cargaBusiness.processMeasurementByNumeroOrden(
                    numeroOrden, request);

            return ResponseEntity.ok(Map.of(
                "mensaje", "Medición recibida correctamente"
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la medición");
        }
    }

    @Operation(
        summary = "Cerrar carga",
        description = "Cierra el proceso de carga para la orden identificada por su número externo."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Carga finalizada correctamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(type = "object"),
            examples = @ExampleObject(value = "{\"mensaje\":\"Carga finalizada correctamente\",\"numeroOrden\":15040,\"estado\":3}")
        )
    )
    @ApiResponse(responseCode = "400", description = "La carga no puede cerrarse en el estado actual", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "La carga solo puede cerrarse en estado 2")))
    @ApiResponse(responseCode = "404", description = "No existe una orden con ese número externo", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "No se encontró la orden 15040")))
    @ApiResponse(responseCode = "500", description = "Error interno al finalizar la carga", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error interno al finalizar la carga")))
    @PostMapping("/{numeroOrden}/close")
    public ResponseEntity<?> closeLoad(
            @Parameter(description = "Número externo de orden (numeroOrden)", example = "15040", required = true)
            @PathVariable("numeroOrden") Integer numeroOrden) {

        try {
            Order order = cargaBusiness.closeLoadByNumeroOrden(numeroOrden);

            return ResponseEntity.ok(Map.of(
                "mensaje", "Carga finalizada correctamente",
                "numeroOrden", order.getNumeroOrden(),
                "estado", order.getEstado()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al finalizar la carga");
        }
    }
}
