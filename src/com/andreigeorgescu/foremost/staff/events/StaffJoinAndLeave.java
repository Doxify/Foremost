package com.andreigeorgescu.foremost.staff.events;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.staff.StaffModePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffJoinAndLeave implements Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    public void onStaffModeJoin(PlayerJoinEvent e) {
        if(!Foremost.getPlugin().nsaPlugin.isHub()) {
            if(e.getPlayer().hasPermission("foremost.staff")) {
                Foremost.getPlugin().staffManager.addToStaffMode(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onStaffModeLeave(PlayerQuitEvent e) {
        if(!Foremost.getPlugin().nsaPlugin.isHub()) {
            if(Foremost.getPlugin().staffManager.isStaffMode(e.getPlayer())) {
                Foremost.getPlugin().staffManager.removeFromStaffMode(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onNonStaffJoin(PlayerJoinEvent e) {
        if(!Foremost.getPlugin().nsaPlugin.isHub()) {
            if(Foremost.getPlugin().staffManager.getVanished().size() > 0) {
                if(!Foremost.getPlugin().staffManager.hasStaffMode(e.getPlayer())) {
                    for(StaffModePlayer staffModePlayer : Foremost.getPlugin().staffManager.getVanished()) {
                        e.getPlayer().hidePlayer(staffModePlayer.getPlayer());
                    }
                }
            }
        }
    }


}
