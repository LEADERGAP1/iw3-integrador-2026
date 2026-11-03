
package ar.edu.iua.iw3.web.controller;

import ar.edu.iua.iw3.business.IPesajeBusiness;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.web.dto.InitialWeightDTO;
import ar.edu.iua.iw3.web.dto.InitialWeightResponseDTO;
import ar.edu.iua.iw3.web.dto.FinalWeightDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

@Tag(
    name = "02 - Pesajes TMS",
    description = "Registro del pesaje inicial y final de camiones mediante el sistema TMS"
)
@RestController
@RequestMapping("/api/v1/orders")
public class PesajeRestController {

    private final IPesajeBusiness pesajeBusiness;

    public PesajeRestController(IPesajeBusiness pesajeBusiness) {
        this.pesajeBusiness = pesajeBusiness;
    }

    @Operation(
        summary = "Registrar pesaje inicial",
        description = "Recibe el número externo de orden y la tara del camión. " +
                      "Valida que la orden esté en estado 1, registra el pesaje, " +
                      "genera una clave de activación de cinco dígitos, " +
                      "actualiza el estado a 2 y registra la transición en auditoría."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Pesaje inicial registrado correctamente",
        content = @Content(
            mediaType = "application/json",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = """
                    {
                      "mensaje": "Pesaje inicial registrado correctamente",
                      "numeroOrden": 15040,
                      "estado": 2
                    }
                    """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Tara inválida, orden inexistente o estado incorrecto",
        content = @Content(
            mediaType = "text/plain",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "La tara debe ser mayor a cero"
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
            mediaType = "text/plain",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "Error interno al registrar el pesaje inicial"
            )
        )
    )
    @PostMapping("/initial-weight")
    public ResponseEntity<?> registerInitialWeight(
            @RequestBody InitialWeightDTO request) {

        try {
            Order order = pesajeBusiness.registerInitialWeight(request);

            return ResponseEntity.ok(new InitialWeightResponseDTO(
                "Pesaje inicial registrado correctamente",
                order.getNumeroOrden(),
                order.getEstado(),
                order.getPassword()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno al registrar el pesaje inicial");
        }
    }


    
    @Operation(
        summary = "Registrar pesaje final",
        description = "Recibe el número externo de orden y el peso bruto final " +
                      "del camión. Comprueba que la orden esté en estado 3, " +
                      "que exista una tara inicial y que el peso final no sea " +
                      "inferior a dicha tara. Registra el pesaje, actualiza " +
                      "el estado a 4 y registra la transición en auditoría."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Pesaje final registrado correctamente",
        content = @Content(
            mediaType = "application/json",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = """
                    {
                      "mensaje": "Pesaje final registrado correctamente",
                      "numeroOrden": 15040,
                      "estado": 4,
                      "pesoFinalKg": 17000.000
                    }
                    """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Peso inválido, orden inexistente o estado incorrecto",
        content = @Content(
            mediaType = "text/plain",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "El peso final no puede ser menor que la tara"
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
            mediaType = "text/plain",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "Error interno al registrar el pesaje final"
            )
        )
    )
    @PostMapping("/final-weight")
    public ResponseEntity<?> registerFinalWeightByOrderNumber(
            @RequestBody FinalWeightDTO request) {

        try {
            Order order = pesajeBusiness.registerFinalWeight(request);

            return ResponseEntity.ok(Map.of(
                "mensaje", "Pesaje final registrado correctamente",
                "numeroOrden", order.getNumeroOrden(),
                "estado", order.getEstado(),
                "pesoFinalKg", order.getPesoFinalKg()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al registrar el pesaje final");
        }
    }

}
