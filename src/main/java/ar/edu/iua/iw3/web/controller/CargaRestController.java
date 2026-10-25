package ar.edu.iua.iw3.web.controller;

import ar.edu.iua.iw3.business.ICargaBusiness;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.web.dto.MeasurementDTO;
import ar.edu.iua.iw3.business.exception.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class CargaRestController {

    private final ICargaBusiness cargaBusiness;

    public CargaRestController(ICargaBusiness cargaBusiness) {
        this.cargaBusiness = cargaBusiness;
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activateLoad(
            @PathVariable("id") Integer numeroOrden,
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

    @PostMapping("/{id}/measurements")
    public ResponseEntity<?> processMeasurement(
            @PathVariable("id") Integer numeroOrden,
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

    @PostMapping("/{id}/close")
    public ResponseEntity<?> closeLoad(
            @PathVariable("id") Integer numeroOrden) {

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
