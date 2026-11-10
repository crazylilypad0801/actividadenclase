package com.logistpulse.inventory.controller;

import com.logistpulse.inventory.command.InventoryCommand;
import com.logistpulse.inventory.command.ReserveCommand;
import com.logistpulse.inventory.dto.ReservationRequest;
import com.logistpulse.inventory.service.InventoryReceiver;
import com.logistpulse.inventory.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final InventoryReceiver inventoryReceiver;
    private final LedgerService ledgerService;

    public ReservationController(InventoryReceiver inventoryReceiver, LedgerService ledgerService) {
        this.inventoryReceiver = inventoryReceiver;
        this.ledgerService = ledgerService;
    }

    @PostMapping
    public ResponseEntity<String> createReservation(
            @RequestHeader("X-Command-Id") String commandId,
            @RequestBody ReservationRequest request) {

        InventoryCommand command = new ReserveCommand(
            inventoryReceiver, 
            ledgerService, 
            commandId, 
            request.getSku(), 
            request.getQuantity()
        );

        command.execute();

        return ResponseEntity.ok("Comando procesado correctamente bajo el ID: " + commandId);
    }
}
