package com.logistpulse.inventory.command;

public interface InventoryCommand {
    boolean execute();
    void undo();
    String getCommandId();
}
