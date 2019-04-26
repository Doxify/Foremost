package com.andreigeorgescu.foremost.kits;

import com.andreigeorgescu.foremost.utils.InventoryGuard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class KitEvents implements Listener {

    @EventHandler
    public void onKitPreviewGUIUse(InventoryClickEvent e) {
        if(InventoryGuard.passedInventoryChecks(e, "Kit Preview")) {
            String inventoryTitle[] = e.getClickedInventory().getName().split(":");
            Player p = (Player) e.getWhoClicked();
            String clickedItemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            if(clickedItemName.equalsIgnoreCase("view all")) {
                p.openInventory(KitGUI.getKitsInterface(p));
            } else if(clickedItemName.equalsIgnoreCase("exit")) {
                p.closeInventory();
            } else if(clickedItemName.equalsIgnoreCase(inventoryTitle[1].trim())){
                Bukkit.dispatchCommand(p, "kit " + clickedItemName);
            }
        }
    }

    @EventHandler
    public void onKitInterfaceGUIUse(InventoryClickEvent e) {
        if (InventoryGuard.passedInventoryChecks(e, "Kits")) {
            Player p = (Player) e.getWhoClicked();
            String clickedItemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            if(clickedItemName.equalsIgnoreCase("view all")) {
                p.openInventory(KitGUI.getKitsInterface(p));
            } else if(clickedItemName.equalsIgnoreCase("exit")) {
                p.closeInventory();
            } else {
                if(e.getClick().isRightClick()) {
                    Bukkit.dispatchCommand(p, "kit preview " + clickedItemName);
                } else {
                    Bukkit.dispatchCommand(p, "kit " + clickedItemName);
                    p.closeInventory();
                }
            }
        }
    }

}
