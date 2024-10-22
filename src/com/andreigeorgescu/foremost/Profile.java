package com.andreigeorgescu.foremost;

import org.bukkit.Location;

public class Profile {

	private String lastReply;
	private Location lastLocation;
	private String teleportRequest;

	private boolean staffMode;
	private boolean condenseCooldown;

	public Profile(String lr) {
		setStaffMode(false);
		setLastReply(lr);
		setLastLocation(null);
		setTeleportRequest(null);
	}

	// -------------------------------------------------------------
	// staffMode methods
	// -------------------------------------------------------------
	public boolean getStaffMode() {
		return staffMode;
	}

	public void setStaffMode(boolean sm) {
		staffMode = sm;
	}

	// -------------------------------------------------------------
	// lastReply methods
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

	// -------------------------------------------------------------
	// condenseCooldown methods
	// -------------------------------------------------------------
	public boolean getCondenseCooldown() {
		return condenseCooldown;
	}

	public void setCondenseCooldown(boolean condenseCooldown) {
		this.condenseCooldown = condenseCooldown;
	}

}
