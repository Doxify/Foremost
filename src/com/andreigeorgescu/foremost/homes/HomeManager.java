package com.andreigeorgescu.foremost.homes;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.waraps.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HomeManager {

    private Foremost plugin;
    private HashMap<UUID, List<Home>> homes;
    private HashMap<String, Integer> homePermissions;

    public HomeManager(Foremost plugin) {
        this.plugin = plugin;
        this.homes = new HashMap<>();
        loadHomeData();
    }

    public void handleServerClose() {
        saveHomeData();
        homes.clear();
    }

    private void loadHomeData() {
        homes = new HashMap<>();

        plugin.getLogger().info("[Foremost] Loading homes.json...");

        try {
            Object homesRaw = new JSONParser().parse(new FileReader("./plugins/Foremost/homes.json"));
            JSONObject homesJson = (JSONObject) homesRaw;
            JSONArray homesJsonArray = (JSONArray) homesJson.get("homes");

            for(Object object : homesJsonArray) {
                JSONObject homeObject = (JSONObject) object;
                Home home = new Home(homeObject);
                addhome(home);
            }

        } catch (IOException | ParseException e) {
            plugin.getLogger().severe("[Foremost] Error in loading homes.json, returning no homes!");
            homes = new HashMap<>();
        }
    }

    private void saveHomeData() {
        try {
            JSONObject homesFile = new JSONObject();
            JSONArray homesArray = new JSONArray();

            for(UUID user : homes.keySet()) {
                for(Home home : homes.get(user)) {
                    homesArray.add(home.serialize());
                }
            }

            homesFile.put("homes", homesArray);

            FileWriter writer = new FileWriter("./plugins/Foremost/homes.json");
            writer.write(homesFile.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[foremost] Failed to save homes.json");
        }

    }

    // Returns a list of a player's homes
    public List<Home> getHomes(Player p) {
        if(homes.containsKey(p.getUniqueId())) {
           return homes.get(p.getUniqueId());
        } else {
            return null;
        }
    }

    public Home getHome(Player p, String name) {
        List<Home> playerHomes = getHomes(p);
        for(Home home : playerHomes) {
            if(home.getName().equalsIgnoreCase(name)) {
                return home;
            }
        }
        return null;
    }

    // Adds a home into the map
    public boolean addHome(UUID player, String homeName, Location homeLocation) {
        List<Home> playerHomes;

        if(homes.containsKey(player)) {
            playerHomes = homes.get(player);
            for(Home h : playerHomes) {
                if(h.getName().equalsIgnoreCase(homeName)) {
                    return false;
                }
            }
        } else {
            playerHomes = new ArrayList<>();
        }

        Home home = new Home(homeName, homeLocation, player);
        playerHomes.add(home);
        homes.put(player, playerHomes);
        return true;
    }

    public boolean addhome(Home home) {
        List<Home> playerHomes;

        if(homes.containsKey(home.getOwner())) {
            playerHomes = homes.get(home.getOwner());
            for(Home h : playerHomes) {
                if(h.getName().equalsIgnoreCase(home.getName())) {
                    return false;
                }
            }
        } else {
            playerHomes = new ArrayList<>();
        }

        playerHomes.add(home);
        homes.put(home.getOwner(), playerHomes);
        return true;
    }

    // Removes a home from the map
    public boolean removeHome(Player p, String homeName) {
        if(homes.containsKey(p.getUniqueId())) {
            for(Home home : homes.get(p.getUniqueId())) {
                if(home.getName().equalsIgnoreCase(homeName)) {
                    homes.get(p.getUniqueId()).remove(home);
                    return true;
                }
            }
        }
        return false;
    }

    public int getAllowedHomes(Player p) {
        for(int i = 54; i > 0; i--) {
            if(p.hasPermission("foremost.home." + i)) {
                return i;
            }
        }

        return 0;
    }
}
