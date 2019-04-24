package com.andreigeorgescu.foremost.homes;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.utils.InventoryGuard;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.saphron.nsa.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

public class HomeClickEvent implements Listener {

    private static Foremost plugin;
    private static final int COOLDONW = 10;

    public HomeClickEvent(Foremost plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHomeInterfaceUse(InventoryClickEvent e) {
        if(InventoryGuard.passedInventoryChecks(e, "Homes")) {
            Player p = (Player) e.getWhoClicked();
            User user = plugin.nsaPlugin.userManager.getUser(p.getUniqueId().toString());
            String clickedHome = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            Home home = plugin.homeManager.getHome(p, clickedHome);

            if(home != null) {
                if(home.getOwner().equals(p.getUniqueId())) {
                    if(home.isOnCooldown()) {
                        p.closeInventory();
                        p.sendMessage(ChatColor.RED + "You can only teleport to a home once every " + COOLDONW + " seconds.");
                    } else {
                        p.teleport(home.getLocation());

                        // Setting the home to cooldown mode and creating the hologram
                        home.setCooldown(true);
                        createTemporaryHologram(home, p, true);

                        // If the user has the setting toggle off then we know they want
                        // sounds sent to them.
                        if(!user.getToggleSounds()) {
                            p.playSound(p.getLocation(), Sound.PORTAL_TRAVEL, (float) 0.5, (float) 2.0);
                        }

                        // Particle Effect
//                        ParticleEffect.SPELL_WITCH.send(Bukkit.getServer().getOnlinePlayers(), p.getLocation(), 0, 1.5, 0, 0, 1);

                        p.sendMessage(ChatColor.GREEN + "You've been teleported to " + home.getName());
                    }
                }
            }
        }
    }

    @EventHandler
    public static void createTemporaryHologram(Home home, Player p, boolean withCooldown) {
        Hologram homeHologram = generateHologram(p.getLocation());

        homeHologram.appendTextLine(ChatColor.GREEN + "Home: " + ChatColor.BOLD.toString() + home.getName());
        homeHologram.appendTextLine(ChatColor.WHITE.toString() + home.getLocation().getBlockX() + ", " + home.getLocation().getBlockY() + ", " + home.getLocation().getBlockZ());

        homeHologram.getVisibilityManager().showTo(p);

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                if(withCooldown) {
                    home.setCooldown(false);
                }
                homeHologram.delete();
            }
        }, COOLDONW * 20);
    }

    public static void createTemporaryDeleteHologram(Home home, Player p) {
        Hologram homeHologram = generateHologram(p.getLocation());

        homeHologram.appendTextLine(ChatColor.RED + "Removed Home: " + ChatColor.BOLD.toString() + home.getName());
        homeHologram.appendTextLine(ChatColor.GRAY.toString() + home.getLocation().getBlockX() + ", " + home.getLocation().getBlockY() + ", " + home.getLocation().getBlockZ());

        homeHologram.getVisibilityManager().showTo(p);

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {

                homeHologram.delete();
            }
        }, COOLDONW * 20);
    }

    private static Hologram generateHologram(Location location) {
        Vector direction = location.clone().getDirection();
        Location hologramLocation = location.clone().add(direction.multiply(2));

        Hologram homeHologram = HologramsAPI.createHologram(plugin, hologramLocation.add(0, 2, 0));
        VisibilityManager visibilityManager = homeHologram.getVisibilityManager();
        visibilityManager.setVisibleByDefault(false);
        homeHologram.clearLines();

        return homeHologram;
    }
}
