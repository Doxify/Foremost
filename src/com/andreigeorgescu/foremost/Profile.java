package com.andreigeorgescu.foremost;

import org.bukkit.Location;

public class Profile {
	
	private String lastReply;
	private Location lastLocation;
	private String teleportRequest;

	public Profile(String lr) {
		setLastReply(lr);
		setLastLocation(null);
		setTeleportRequest(null);
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

	// -------------------------------------------------------------
	// getTeleportRequest methods
	// -------------------------------------------------------------
	public String getTeleportRequest() {
		return teleportRequest;
	}

	public void setTeleportRequest(String tr) {
		teleportRequest = tr;
	}

}
