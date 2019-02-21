package com.andreigeorgescu.foremost.kits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class KitEvents implements Listener {

    @EventHandler
    public void onKitPreviewGUIUse(InventoryClickEvent e) {
        if(e.getInventory().getName().contains("Kit Preview")) {
            e.setCancelled(true);
        }
    }

}
