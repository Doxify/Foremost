package com.andreigeorgescu.foremost.utils;

import com.andreigeorgescu.foremost.homes.Home;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class NotificationHologram {

    public enum TYPE {
        DEFAULT, HOME, HOME_DELETE, KIT,
    }

    private static final int COOLDOWN = 10;
    private TYPE type;
    private Hologram hologram;
    private Player p;
    private Home home;
    private List<String> lines = new ArrayList<>();

    // Sets the player who this hologram belongs to.
    // Creates a new hologram two blocks in front of the player and clear's it's lines.
    public NotificationHologram setPlayer(Player p) {
        this.p = p;
        return this;
    }

    // Sets the type of the hologram, this is mainly used for doing things at the end
    // of destroying this hologram.
    public NotificationHologram setType(TYPE type) {
        switch (type) {
            case HOME: {
                this.type = TYPE.HOME;
                Location loc = home.getLocation();
                lines.clear();
                lines.add(ChatColor.GREEN + "Home: " + ChatColor.BOLD.toString() + home.getName() + ChatColor.RESET);
                lines.add(ChatColor.WHITE.toString() + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                break;
            }
            case KIT: {
                this.type = TYPE.KIT;
                break;
            }
            case HOME_DELETE: {
                this.type = TYPE.HOME_DELETE;
                Location loc = home.getLocation();
                lines.clear();
                lines.add(ChatColor.RED + "Removed Home: " + ChatColor.BOLD.toString() + home.getName() + ChatColor.RESET);
                lines.add(ChatColor.GRAY.toString() + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                break;
            }
            case DEFAULT: {
                this.type = TYPE.DEFAULT;
                break;
            }
            default: {
                this.type = TYPE.DEFAULT;
                break;
            }
        }
        return this;
    }

    public NotificationHologram setHome(Home home) {
        this.home = home;
        return this;
    }

    public NotificationHologram setLines(List<String> lines) {
        this.lines.clear();
        this.lines = lines;
        return this;
    }

    public NotificationHologram addLine(String line) {
        lines.add(line);
        return this;
    }

    // Attempts to display the hologram.
    // Returns true if successful, false if not
    public boolean build() {
        if(p != null) {
            Vector direction = p.getLocation().clone().getDirection();
            Location hologramLocation = p.getLocation().clone().add(direction.multiply(2));
            hologram = HologramHandler.createHologram(hologramLocation.add(0, 2, 0));
            VisibilityManager visibilityManager = hologram.getVisibilityManager();
            visibilityManager.showTo(p);
            visibilityManager.setVisibleByDefault(false);
            hologram.clearLines();

            for(String line : lines) {
                hologram.appendTextLine(line);
            }

            Bukkit.getScheduler().runTaskLater(HologramHandler.plugin, new Runnable() {
                @Override
                public void run() {
                    hologram.delete();

                    // Doing specific stuff based on the type of hologram this is.
                    if(type != null) {
                        switch (type) {
                            case HOME: {
                                home.setCooldown(false);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                }
            }, COOLDOWN * 20);
            return true;
        }
        return false;
    }
}
