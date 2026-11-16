package com.logistpulse.inventory.service;

import com.logistpulse.inventory.command.AdjustCommand;
import com.logistpulse.inventory.command.CommandOperation;
import com.logistpulse.inventory.command.InventoryCommand;
import com.logistpulse.inventory.command.ReleaseCommand;
import com.logistpulse.inventory.command.ReserveCommand;
import com.logistpulse.inventory.command.TransferCommand;
import com.logistpulse.inventory.dto.OperationRequest;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class InventoryCommandFactory {
    private final InventoryReceiver receiver;
    private final LedgerService ledger;

    public InventoryCommandFactory(InventoryReceiver receiver, LedgerService ledger) {
        this.receiver = receiver;
        this.ledger = ledger;
    }

    public InventoryCommand create(String commandId, OperationRequest request) {
        if (request == null || request.getOperation() == null || request.getOperation().isBlank()) {
            throw new IllegalArgumentException("Operation must not be blank.");
        }
        CommandOperation operation;
        try {
            operation = CommandOperation.valueOf(request.getOperation().trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unknown inventory operation: " + request.getOperation());
        }

        return switch (operation) {
            case RESERVE -> new ReserveCommand(
                    receiver, ledger, commandId, request.getSku(), request.getQuantity(), request.getLocation());
            case RELEASE -> new ReleaseCommand(
                    receiver, ledger, commandId, request.getSku(), request.getQuantity(), request.getLocation());
            case ADJUST -> new AdjustCommand(
                    receiver, ledger, commandId, request.getSku(), request.getQuantity(), request.getLocation());
            case TRANSFER -> new TransferCommand(
                    receiver, ledger, commandId, request.getSku(), request.getQuantity(),
                    request.getLocation(), request.getDestinationLocation());
        };
    }
}
