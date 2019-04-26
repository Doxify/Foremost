package com.andreigeorgescu.foremost.utils;

import com.andreigeorgescu.foremost.Foremost;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;

public class HologramHandler {

    public static Foremost plugin;

    public HologramHandler(Foremost plugin) {
        this.plugin = plugin;
    }

    public static Hologram createHologram(Location location) {
        Hologram hologram = HologramsAPI.createHologram(plugin, location);
        return hologram;
    }

}
