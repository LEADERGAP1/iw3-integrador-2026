package ar.edu.iua.iw3.web.controller;

import ar.edu.iua.iw3.business.IOrderBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.iua.iw3.web.dto.OrderReceptionDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@Tag(
    name = "01 - Órdenes SAP",
    description = "Recepción de órdenes de carga provenientes del sistema SAP"
)
@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    @Autowired
    private IOrderBusiness orderBusiness;

    
    @Operation(
        summary = "Recibir orden desde SAP",
        description = "Recibe una orden de carga, valida su información, " +
                      "sincroniza camión, cisternas, chofer, cliente y producto, " +
                      "registra la orden en estado 1 y genera su auditoría inicial."
    )
    @ApiResponse(
        responseCode = "201",
        description = "Orden recibida y registrada correctamente",
        content = @Content(
            mediaType = "text/plain",
            examples = @ExampleObject(
                value = "Orden recibida correctamente"
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Datos inválidos o número de orden duplicado",
        content = @Content(
            mediaType = "text/plain",
            examples = @ExampleObject(
                value = "El número de orden ya existe"
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
            mediaType = "text/plain",
            examples = @ExampleObject(
                value = "Error interno al procesar la orden"
            )
        )
    )
    @PostMapping
    public ResponseEntity<?> receiveOrder(
            @RequestBody OrderReceptionDTO request) {

        try {
            orderBusiness.receiveOrder(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Orden recibida correctamente");

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la orden");
        }
    }


}
