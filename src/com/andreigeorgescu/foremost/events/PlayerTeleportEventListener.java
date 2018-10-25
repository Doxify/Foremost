package com.andreigeorgescu.foremost.events;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportEventListener implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if(e.getTo().getWorld().getEnvironment() == World.Environment.NETHER) {
            if(!p.hasPermission("foremost.nether.access")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "Only donators can access the nether!");
                p.sendMessage(ChatColor.GRAY + "You can purchase a rank with /buy");
            }
        }
    }
}
