package com.logistpulse.inventory.command;

public interface InventoryCommand {
    void execute();
    String getCommandId();
}
