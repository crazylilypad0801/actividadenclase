package com.logistpulse.inventory.controller;

import com.logistpulse.inventory.dto.ReservationRequest;
import com.logistpulse.inventory.service.ReservationValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations/validate")
public class ReservationValidationController {

    private final ReservationValidationService validationService;

    public ReservationValidationController(ReservationValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<String> validate(@RequestBody ReservationRequest request) {
        try {
            validationService.validateReservation(request);
            return ResponseEntity.ok("Reserva aprobada: superó toda la cadena (SKU -> Stock -> Lote -> Capacidad).");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
