package com.andreigeorgescu.foremost;

import java.util.HashMap;

import org.bukkit.Location;

public class Config {
	
	private Location spawn;
	private HashMap<String, Location> warps = new HashMap<String, Location>();
	
	public Config(Location spawnLocation, HashMap<String, Location> warpsMap) {
		spawn = spawnLocation;
		warps = warpsMap;
	}
	
	// =============================================
    // Spawn methods
    // =============================================
	public void setSpawn(Location spawnLocation) {
		spawn = spawnLocation;
	}
	
	public Location getSpawn() {
		return spawn;
	}
	
	// =============================================
    // Warp methods
    // =============================================
	public HashMap<String, Location> getWarps() {
		return warps;
	}
	
	public Location getWarp(String name) {
		if(warps.containsKey(name)) {
			return (Location) warps.get(name);
		} else {
			return null;
		}
	}
	
	public boolean checkIfWarpExists(String name) {
		return(warps.containsKey(name));
	}
	
	public boolean addWarp(String name, Location loc) {
		if(!checkIfWarpExists(name)) {
			warps.put(name, loc);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean deleteWarp(String name) {
		if(checkIfWarpExists(name)) {
			warps.remove(name);
			return true;
		} else {
			return false;
		}
	}

}
