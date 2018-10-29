package com.andreigeorgescu.foremost.events;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.StaffMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class StaffModeEventListener implements Listener {

    Foremost plugin;

    public StaffModeEventListener(Foremost p) {
        plugin = p;
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(p != null) {
            if(plugin.staffModeManager.hasStaffMode(p.getUniqueId().toString())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(plugin.staffModeManager.hasStaffMode(p.getUniqueId().toString())) {
            plugin.staffModeManager.removeStaff(p.getUniqueId().toString());
            p.sendMessage(ChatColor.RED + "Staff mode has been disabled.");
        }
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(plugin.staffModeManager.hasStaffMode(p.getUniqueId().toString())) {
            StaffMode sm = plugin.staffModeManager.getStaffMode(p.getUniqueId().toString());
            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                String itemName = ChatColor.stripColor(p.getItemInHand().getItemMeta().getDisplayName());
                switch (itemName) {
                    case "Random Teleport": {
                        e.setCancelled(true);
                        Player teleportTarget = Bukkit.getPlayer(plugin.staffModeManager.handleRandomTeleport(p));
                        if(teleportTarget != null) {
                            p.teleport(teleportTarget.getLocation());
                            p.sendMessage(ChatColor.GREEN + "You've been randomly teleported to " + teleportTarget.getName());
                        } else {
                            p.sendMessage(ChatColor.RED + "There are no players you could be teleported to.");
                        }
                        break;
                    }
                    case "Online Staff": {
                        e.setCancelled(true);
                        Inventory onlineStaff = plugin.staffModeManager.getOnlineStaffGUI(p);
                        if(onlineStaff != null) {
                            p.openInventory(onlineStaff);
                        }
                    }
                    default: {
                        break;
                    }
                }
            }
        }
    }


}
