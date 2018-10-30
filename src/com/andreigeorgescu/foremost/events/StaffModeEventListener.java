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
        if(p.hasPermission("foremost.staffmode.force") && !plugin.staffModeManager.hasStaffMode(p.getUniqueId().toString()) && !p.hasPermission("foremost.staffmode.bypass")) {
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
                if(p.hasPermission("foremost.staffmode.force") && !p.hasPermission("foremost.staffmode.bypass")) {
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

                switch(e.getClickedInventory().getName().toUpperCase()) {
                    case "ONLINE STAFF": {
                        ItemStack clickedItem = e.getCurrentItem();
                        String targetName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                        Player targetPlayer = Bukkit.getPlayer(targetName);

                        if (targetPlayer != null) {
                            if(targetPlayer.getName() != p.getName()) {
                                p.teleport(targetPlayer.getLocation());
                                p.sendMessage(ChatColor.GREEN + "You've been teleported to " + targetPlayer.getName());
                                break;
                            } else {
                                p.sendMessage(ChatColor.RED + "You cannot teleport to yourself.");
                                break;
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + targetName + " is not online.");
                            break;
                        }
                    }
                    case "SERVER TOOLS": {
                        ItemStack clickedItem = e.getCurrentItem();
                        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

                        switch(itemName.toUpperCase()) {
                            case "UNMUTE CHAT": {
                                p.performCommand("chat unmute");
                                p.openInventory(plugin.staffModeManager.getServerToolsGui(p));
                                break;
                            }
                            case "MUTE CHAT": {
                                p.performCommand("chat mute");
                                p.openInventory(plugin.staffModeManager.getServerToolsGui(p));
                                break;
                            }
                            case "CLEAR CHAT": {
                                p.performCommand("chat clear");
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                    default: {
                        break;
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
            e.setCancelled(true);

            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                String itemName = ChatColor.stripColor(p.getItemInHand().getItemMeta().getDisplayName());
                switch (itemName.toUpperCase()) {
                    case "RANDOM TELEPORT": {
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
                    case "SPAWN": {
                        p.performCommand("spawn");
                        break;
                    }
                    case "ONLINE STAFF": {
                        Inventory onlineStaff = plugin.staffModeManager.getOnlineStaffGUI(p);
                        if(onlineStaff != null) {
                            p.openInventory(onlineStaff);
                        }
                        break;
                    }
                    case "SERVER TOOLS": {
                        Inventory serverTools = plugin.staffModeManager.getServerToolsGui(p);
                        p.openInventory(serverTools);
                    }
                    default: {
                        break;
                    }
                }
            }
        }
    }


}
