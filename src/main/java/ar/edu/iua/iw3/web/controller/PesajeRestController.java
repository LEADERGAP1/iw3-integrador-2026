
package ar.edu.iua.iw3.web.controller;

import ar.edu.iua.iw3.business.IPesajeBusiness;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.web.dto.InitialWeightDTO;
import ar.edu.iua.iw3.web.dto.FinalWeightDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class PesajeRestController {

    private final IPesajeBusiness pesajeBusiness;

    public PesajeRestController(IPesajeBusiness pesajeBusiness) {
        this.pesajeBusiness = pesajeBusiness;
    }

    @PostMapping("/initial-weight")
    public ResponseEntity<?> registerInitialWeight(
            @RequestBody InitialWeightDTO request) {

        try {
            Order order = pesajeBusiness.registerInitialWeight(request);

            return ResponseEntity.ok(Map.of(
                "mensaje", "Pesaje inicial registrado correctamente",
                "numeroOrden", order.getNumeroOrden(),
                "estado", order.getEstado(),
                "password", order.getPassword()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno al registrar el pesaje inicial");
        }
    }


    
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
