package com.andreigeorgescu.foremost.events;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.StaffMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StaffModeEventListener implements Listener {

    Foremost plugin;

    public StaffModeEventListener(Foremost p) {
        plugin = p;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(p.hasPermission("foremost.staffmode.force") && !plugin.staffModeManager.hasStaffMode(p.getUniqueId().toString())) {
            p.performCommand("staff");
            p.sendMessage(ChatColor.YELLOW + "You've been forced into staff mode.");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if(plugin.staffModeManager.hasStaffMode(p.getUniqueId().toString()) && !p.hasPermission("foremost.staffmode.bypass")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onStaffMemberCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if(plugin.staffModeManager.hasStaffMode(p.getUniqueId().toString())) {
            if(e.getMessage().toUpperCase().contains("VANISH")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "Vanish is required while staff mode is enabled.");
            } else if(e.getMessage().toUpperCase().contains("STAFF")) {
                if(p.hasPermission("foremost.staffmode.force")) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You are not allowed to exit staff mode.");
                }
            }
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(p != null) {
            if(plugin.staffModeManager.hasStaffMode(p.getUniqueId().toString())) {
                e.setCancelled(true);
                ItemStack clickedItem = e.getCurrentItem();
                if(clickedItem.getType() == Material.SKULL_ITEM) {
                    String targetName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                    Player targetPlayer = Bukkit.getPlayer(targetName);
                    if (targetPlayer != null) {
                        if(targetPlayer.getName() != p.getName()) {
                            p.teleport(targetPlayer.getLocation());
                            p.sendMessage(ChatColor.GREEN + "You've been teleported to " + targetPlayer.getName());
                        } else {
                            p.sendMessage(ChatColor.RED + "You cannot teleport to yourself.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + targetName + " is not online.");
                    }
                }
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
