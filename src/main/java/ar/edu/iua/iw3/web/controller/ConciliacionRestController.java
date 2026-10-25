package ar.edu.iua.iw3.web.controller;

import ar.edu.iua.iw3.business.IConciliacionBusiness;
import ar.edu.iua.iw3.business.exception.NotFoundException;
import ar.edu.iua.iw3.web.dto.ConciliationDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class ConciliacionRestController {

    private final IConciliacionBusiness conciliacionBusiness;

    public ConciliacionRestController(
            IConciliacionBusiness conciliacionBusiness) {
        this.conciliacionBusiness = conciliacionBusiness;
    }

    @GetMapping("/{id}/conciliation")
    public ResponseEntity<?> getConciliation(@PathVariable Long id) {

        try {
            ConciliationDTO resultado =
                    conciliacionBusiness.getConciliation(id);

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
