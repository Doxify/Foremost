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
            Player p = (Player) e.getWhoClicked();
            String clickedItemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            if(clickedItemName.equalsIgnoreCase("Redeem Kit")) {
                String[] inventoryTitle = e.getClickedInventory().getTitle().split(":");
                String command = "kit " + inventoryTitle[1].trim();
                Bukkit.dispatchCommand(p, command);
            }
        }
    }

}
