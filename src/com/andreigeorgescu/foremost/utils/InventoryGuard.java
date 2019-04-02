package com.andreigeorgescu.foremost.utils;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryGuard {

    public static boolean passedInventoryChecks(InventoryClickEvent e, String inventoryName) {
        if(e.getClickedInventory() != null) {
            if(e.getView().getTopInventory().getName().equalsIgnoreCase(inventoryName) || e.getView().getTopInventory().getName().contains(inventoryName)) {
                e.setCancelled(true);

                if(e.getClickedInventory().getType().equals(InventoryType.CHEST)) {
                    if(!e.isShiftClick()) {
                        if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
