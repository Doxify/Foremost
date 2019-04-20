package com.andreigeorgescu.foremost.homes;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.utils.InventoryGuard;
import de.Herbystar.TTA.TTA_Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class HomeClickEvent implements Listener {

    Foremost plugin;

    public HomeClickEvent(Foremost plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHomeInterfaceUse(InventoryClickEvent e) {
        if(InventoryGuard.passedInventoryChecks(e, "Homes")) {
            Player p = (Player) e.getWhoClicked();
            String clickedHome = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            Home home = plugin.homeManager.getHome(p, clickedHome);

            if(home != null) {
                if(home.getOwner().equals(p.getUniqueId())) {
                    p.teleport(home.getLocation());
                    p.sendMessage(ChatColor.GREEN + "You've been teleported to " + home.getName());
                }
            }
        }
    }
}
