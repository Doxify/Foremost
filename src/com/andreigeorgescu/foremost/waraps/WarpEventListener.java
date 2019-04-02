package com.andreigeorgescu.foremost.waraps;

import com.andreigeorgescu.foremost.utils.InventoryGuard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class WarpEventListener implements Listener {

    WarpManager warpManager;

    public WarpEventListener(WarpManager wm) { this.warpManager = wm; }

    @EventHandler
    public void onWarpInventoryUse(InventoryClickEvent e) {
        if(InventoryGuard.passedInventoryChecks(e, "Warp Menu")) {
            Player p = (Player) e.getWhoClicked();
            String clickedWarp = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            Warp warp = warpManager.getWarp(clickedWarp);

            if(warp instanceof Warp) {
                if(warp.hasPermission(p)) {
                    p.teleport(warp.getLocation());
                    p.sendMessage(ChatColor.GREEN + "Teleported to warp: " + clickedWarp);
                    return;
                } else {
                    p.sendMessage(ChatColor.RED + "You don't have permission to use this warp.");
                    return;
                }
            } else {
                p.sendMessage(ChatColor.RED + "There was an error getting the location for that warp.");
                return;
            }
        }
    }
}
