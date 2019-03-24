package com.andreigeorgescu.foremost.waraps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;


public class Warp {

    private String name;
    private Location location;

    // Main Constructor
    public Warp(String n, Location l) {
        this.name = n;
        this.location = l;
    }

    // Constructor used for loading warps from JSON
    public Warp(JSONObject warpObject) {
        deserialize(warpObject);
    }

    // Get a warp's name
    public String getName() {
        return name;
    }

    // Get a warp's location
    public Location getLocation() {
        return location;
    }

    // Returns true if a player has permission to warp, false if not
    public boolean hasPermission(Player p) {
        if(p.hasPermission("foremost.warp." + name)) {
            return true;
        }
        return false;
    }

    // Serializes the warp object into a JSONObject
    public JSONObject serialize() {
        JSONObject warpJson = new JSONObject();
        StringBuilder locationString = new StringBuilder();
        locationString.append(location.getWorld().getName());
        locationString.append(";" + location.getX());
        locationString.append(";" + location.getY());
        locationString.append(";" + location.getZ());
        locationString.append(";" + location.getYaw());
        locationString.append(";" + location.getPitch());

        warpJson.put("name", name);
        warpJson.put("location", locationString.toString());

        return warpJson;
    }

    // Deserializes a JSONObject and initializes the values
    public void deserialize(JSONObject warpObject) {
        String[] locationRaw = ((String) warpObject.get("location")).split(";");
        this.name = (String) warpObject.get("name");
        this.location = new Location(Bukkit.getWorld(locationRaw[0]), Double.parseDouble(locationRaw[1]), Double.parseDouble(locationRaw[2]), Double.parseDouble(locationRaw[3]), Float.parseFloat(locationRaw[4]), Float.parseFloat(locationRaw[5]));
    }

}
