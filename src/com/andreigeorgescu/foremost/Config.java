package com.andreigeorgescu.foremost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.andreigeorgescu.foremost.kits.Kit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Config {
	
	private Location spawn;

	public Config(Location spawnLocation) {
		spawn = spawnLocation;
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

}
