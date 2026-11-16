package com.logistpulse.inventory.controller;

import com.logistpulse.inventory.command.InventoryCommand;
import com.logistpulse.inventory.dto.OperationRequest;
import com.logistpulse.inventory.service.CommandInvoker;
import com.logistpulse.inventory.service.IdempotencyConflictException;
import com.logistpulse.inventory.service.InventoryCommandFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operations")
public class InventoryOperationController {
    private final InventoryCommandFactory commandFactory;
    private final CommandInvoker commandInvoker;

    public InventoryOperationController(
            InventoryCommandFactory commandFactory, CommandInvoker commandInvoker) {
        this.commandFactory = commandFactory;
        this.commandInvoker = commandInvoker;
    }

    @PostMapping
    public ResponseEntity<String> execute(
            @RequestHeader("X-Command-Id") String commandId,
            @RequestBody OperationRequest request) {
        try {
            InventoryCommand command = commandFactory.create(commandId, request);
            boolean executed = commandInvoker.submit(command);
            String message = executed
                    ? "Command processed: " + commandId
                    : "Command already processed: " + commandId;
            return ResponseEntity.ok(message);
        } catch (IdempotencyConflictException exception) {
            return ResponseEntity.status(409).body(exception.getMessage());
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
