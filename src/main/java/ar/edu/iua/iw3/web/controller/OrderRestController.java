package ar.edu.iua.iw3.web.controller;

import ar.edu.iua.iw3.business.IOrderBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.iua.iw3.web.dto.OrderReceptionDTO;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    @Autowired
    private IOrderBusiness orderBusiness;

    
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
