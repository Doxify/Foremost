package com.andreigeorgescu.foremost.staff.events;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.staff.StaffModePlayer;
import com.andreigeorgescu.foremost.utils.InventoryGuard;
import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class StaffInventoryUse implements Listener {

    @EventHandler
    public void onStaffModeInventoryUse(PlayerInteractEvent e) {
        if(Foremost.getPlugin().staffManager.isStaffMode(e.getPlayer())) {
            if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                ItemStack clickedItem = e.getItem();

                if(clickedItem != null) {
                    e.setCancelled(true);
                    Player p = e.getPlayer();
                    StaffModePlayer staffModePlayer = Foremost.getPlugin().staffManager.getStaffModePlayer(p);
                    String clickedItemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

                    if(clickedItemName.equalsIgnoreCase("fun stick")) { // DONE
                        Player target = getFunStickTarget(p);

                        if(target != null) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 2, 1));
                            p.sendMessage(ChatColor.LIGHT_PURPLE + target.getName() + " was given the effects of the Fun Stick!");
                        }

                        return;
                    }

                    if(clickedItemName.equalsIgnoreCase("staff")) {
                        p.openInventory(Foremost.getPlugin().staffManager.getStaffMenu());
                        return;
                    }

                    if(clickedItemName.equalsIgnoreCase("thru")) { // DONE
                        Bukkit.dispatchCommand(p, "thru");
                        return;
                    }

                    if(clickedItemName.equalsIgnoreCase("vanish")) { // DONE
                        toggleVanish(staffModePlayer);
                        Foremost.getPlugin().staffManager.giveStaffInventory(staffModePlayer);
                        return;
                    }

                    if(clickedItemName.equalsIgnoreCase("toggles")) {
                        Foremost.getPlugin().staffManager.updateTogglesMenu();
                        p.openInventory(Foremost.getPlugin().staffManager.getTogglesMenu());
                        return;
                    }

                }
            }
        }
    }

    @EventHandler
    public void onStaffTogglesUse(InventoryClickEvent e) {
        if(Foremost.getPlugin().staffManager.isStaffMode((Player) e.getWhoClicked()))  {
            e.setCancelled(true);

            if(InventoryGuard.passedInventoryChecks(e,  "Staff Toggles")) {
                String clickedToggle = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                Player staff = (Player) e.getWhoClicked();

                if(clickedToggle.equalsIgnoreCase("mute chat")) {
                    if(staff.hasPermission("foremost.chat.mute") && staff.hasPermission("foremost.chat.unmute")) {
                        if(Foremost.getPlugin().chatManager.getChatMuteSetting()) {
                            if(Foremost.getPlugin().chatManager.unmuteChat()) {
                                // Chat was unmuted successfully.
                                Foremost.getPlugin().staffManager.updateTogglesMenu();
                                Bukkit.broadcastMessage(ChatColor.RED + "Chat has been unmuted by " + staff.getName() + ".");
                                staff.sendMessage(ChatColor.GREEN  + "Successfully unmuted chat.");

                            }
                        } else {
                            if(Foremost.getPlugin().chatManager.muteChat()) {
                                // Chat was muted successfully.
                                Foremost.getPlugin().staffManager.updateTogglesMenu();
                                Bukkit.broadcastMessage(ChatColor.RED + "Chat has been muted by " + staff.getName() + ".");
                                staff.sendMessage(ChatColor.GREEN  + "Successfully muted chat.");
                            }
                        }
                    } else {
                        staff.sendMessage(Utilities.NO_PERMISSION);
                    }
                }

                if(clickedToggle.equalsIgnoreCase("clear chat")) {
                    if(staff.hasPermission("foremost.chat.clear")) {
                        Foremost.getPlugin().chatManager.clearChat(staff.getName());
                    } else  {
                        staff.sendMessage(Utilities.NO_PERMISSION);
                    }
                }

                if (clickedToggle.equalsIgnoreCase("slow chat soon")) {
                    staff.sendMessage("soon");
                }

                return;
            }
        }
    }

    @EventHandler
    public void onStaffMenuUse(InventoryClickEvent e) {
        if(Foremost.getPlugin().staffManager.isStaffMode((Player) e.getWhoClicked())) {
            e.setCancelled(true);

            if(InventoryGuard.passedInventoryChecks(e, "Staff")) {
                String clickedStaff = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).split(" ")[1];
                Player p = (Player) e.getWhoClicked();
                Player target = Bukkit.getPlayer(clickedStaff);

                if(target != null && target != p) {
                    p.teleport(target);
                    p.sendMessage(ChatColor.GREEN + "You've been teleported to " + target.getName() + ".");
                }

                return;
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if(Foremost.getPlugin().staffManager.isStaffMode((Player) e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if(Foremost.getPlugin().staffManager.isStaffMode(e.getPlayer())) {
            e.setCancelled(true);
        }
    }


    // Helper function for Fun Stick
    private Player getFunStickTarget(Player from) {
        assert from != null;
        // SOME FIXED VALUES (maybe define them globally somewhere):
        // the radius^2:
        double radius2 = 5.0D * 5.0D;
        // the min. dot product (defines the min. angle to the target player)
        // higher value means lower angle means that the player is looking "more directly" at the target):
        // do some experiments, which angle / dotProduct value fits best for your case
        double minDot = 0.98D;

        String fromName = from.getName();
        Location fromLocation = from.getEyeLocation();
        String fromWorldName = fromLocation.getWorld().getName();
        Vector fromDirection = fromLocation.getDirection().normalize();
        Vector fromVectorPos = fromLocation.toVector();

        Player target = null;
        double minDistance2 = Double.MAX_VALUE;
        for (Player somePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if (somePlayer.getName().equals(fromName)) continue;
            Location newTargetLocation = somePlayer.getEyeLocation();
            // check the world:
            if (!newTargetLocation.getWorld().getName().equals(fromWorldName)) continue;
            // check distance:
            double newTargetDistance2 = newTargetLocation.distanceSquared(fromLocation);
            if (newTargetDistance2 > radius2) continue;
            // check angle to target:
            Vector toTarget = newTargetLocation.toVector().subtract(fromVectorPos).normalize();
            // check the dotProduct instead of the angle, because it's faster:
            double dotProduct = toTarget.dot(fromDirection);
            if (dotProduct > minDot && from.hasLineOfSight(somePlayer) && (target == null || newTargetDistance2 < minDistance2)) {
                target = somePlayer;
                minDistance2 = newTargetDistance2;
            }
        }

        // can return null, if no player was found, which meets the conditions:
        return target;
    }

    // Helper function for Vanish
    public void toggleVanish(StaffModePlayer staffModePlayer) {
        Player p = staffModePlayer.getPlayer();

        if(staffModePlayer.isVanished()) {
            // Player is currently vanished, they want to get out of vanish.
            Foremost.getPlugin().staffManager.removeVanished(staffModePlayer);
            p.sendMessage(ChatColor.YELLOW + "You are no longer vanished.");
        } else {
            // Player is not vanished, they want to enter vanish.
            Foremost.getPlugin().staffManager.addVanished(staffModePlayer);
            p.sendMessage(ChatColor.GREEN + "You are now vanished.");
        }
    }


}
