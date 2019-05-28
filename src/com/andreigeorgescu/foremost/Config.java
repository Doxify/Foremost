package com.andreigeorgescu.foremost;

import org.bukkit.Location;

public class Config {
	
	private Location spawn;
	private String defaultKit;

	public Config(Location spawn, String defaultKit) {
		setSpawn(spawn);
		setDefaultKit(defaultKit);

	}

	// Getters and setters
	public void setSpawn(Location spawnLocation) {
		this.spawn = spawnLocation;
	}
	
	public Location getSpawn() { return spawn; }

	public void setDefaultKit(String defaultKit) { this.defaultKit = defaultKit; }

	public String getDefaultKit() { return defaultKit; }

}
