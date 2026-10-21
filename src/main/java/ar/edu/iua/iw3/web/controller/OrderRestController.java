package ar.edu.iua.iw3.web.controller;

import ar.edu.iua.iw3.business.IOrderBusiness;
import ar.edu.iua.iw3.web.dto.MeasurementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    @Autowired
    private IOrderBusiness orderBusiness;

    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activateLoad(@PathVariable Long id, @RequestParam String password) {
        try {
            return new ResponseEntity<>(orderBusiness.activateLoad(id, password), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/measurements")
    public ResponseEntity<?> receiveMeasurement(@PathVariable Long id, @RequestBody MeasurementDTO measurement) {
        try {
            orderBusiness.processMeasurement(id, measurement);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<?> closeLoad(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(orderBusiness.closeLoad(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/weight")
    public ResponseEntity<?> registerFinalWeight(@PathVariable Long id, @RequestParam double weight) {
        try {
            return new ResponseEntity<>(orderBusiness.registerFinalWeight(id, weight), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/conciliation")
    public ResponseEntity<?> getConciliation(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(orderBusiness.getConciliation(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}