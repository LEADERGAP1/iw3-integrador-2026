package ar.edu.iua.iw3.web.controller;

import ar.edu.iua.iw3.business.IConciliacionBusiness;
import ar.edu.iua.iw3.business.exception.NotFoundException;
import ar.edu.iua.iw3.web.dto.ConciliationDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "04 - Conciliación",
    description = "Consulta de conciliación de órdenes finalizadas"
)
@RestController
@RequestMapping("/api/v1/orders")
public class ConciliacionRestController {

    private final IConciliacionBusiness conciliacionBusiness;

    public ConciliacionRestController(
            IConciliacionBusiness conciliacionBusiness) {
        this.conciliacionBusiness = conciliacionBusiness;
    }

    @Operation(
        summary = "Consultar conciliación",
        description = "Devuelve los valores de conciliación para una orden identificada por su número externo. El servicio solo permite consultar órdenes en estado 4 y requiere los datos necesarios para el cálculo."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Conciliación calculada correctamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ConciliationDTO.class),
            examples = @ExampleObject(value = """
                {"numeroOrden":15040,"tara":12500.0,"pesajeFinal":17000.0,"productoCargadoBalanza":4500.0,"masaCaudalimetro":4480.0,"diferencia":20.0,"temperaturaFinal":20.0,"densidadFinal":0.55}
                """)
        )
    )
    @ApiResponse(responseCode = "400", description = "La orden no está finalizada o faltan datos para conciliar", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "La conciliación solo está disponible en estado 4")))
    @ApiResponse(responseCode = "404", description = "No existe una orden con ese número externo", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "No se encontró la orden 15040")))
    @ApiResponse(responseCode = "500", description = "Error interno al calcular la conciliación", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error interno al calcular la conciliación")))
    @GetMapping("/{numeroOrden}/conciliation")
    public ResponseEntity<?> getConciliation(
            @Parameter(description = "Número externo de orden (numeroOrden)", example = "15040", required = true)
            @PathVariable("numeroOrden") Integer numeroOrden) {

        try {
            ConciliationDTO resultado =
                    conciliacionBusiness.getConciliationByNumeroOrden(numeroOrden);

            return ResponseEntity.ok(resultado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al calcular la conciliación");
        }
    }
}
