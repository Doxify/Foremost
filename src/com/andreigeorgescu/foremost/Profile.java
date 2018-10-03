package com.andreigeorgescu.foremost;

import org.bukkit.Location;

public class Profile {
	
	private String lastReply;
	private Location lastLocation;
	
	public Profile(String lr) {
		setLastReply(lr);
		setLastLocation(null);
	}

	// -------------------------------------------------------------
	// getLastReply methods
	// -------------------------------------------------------------
	public String getLastReply() {
		return lastReply;
	}

	public void setLastReply(String lr) {
		lastReply = lr;
	}
	
	// -------------------------------------------------------------
	// lastLocation methods
	// -------------------------------------------------------------
	public Location getLastLocation() {
		return lastLocation;
	}
	
	public void setLastLocation(Location loc) {
		lastLocation = loc;
	}

}
