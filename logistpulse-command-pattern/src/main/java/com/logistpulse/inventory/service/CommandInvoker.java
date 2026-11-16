package com.logistpulse.inventory.service;

import com.logistpulse.inventory.command.InventoryCommand;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;

@Service
public class CommandInvoker {
    private final LedgerService ledger;
    private final Deque<InventoryCommand> history = new ArrayDeque<>();

    public CommandInvoker(LedgerService ledger) {
        this.ledger = ledger;
    }

    public synchronized boolean submit(InventoryCommand command) {
        boolean executed = command.execute();
        if (executed) {
            history.push(command);
        }
        return executed;
    }

    public synchronized String undoLast() {
        InventoryCommand command = history.peek();
        if (command == null) {
            throw new IllegalStateException("There is no executed command to undo.");
        }
        command.undo();
        ledger.recordUndo(command.getCommandId());
        history.pop();
        return command.getCommandId();
    }
}
