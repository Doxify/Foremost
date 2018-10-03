package com.andreigeorgescu.foremost;

import java.util.HashMap;

public class ProfileManager {
	static HashMap<String, Profile> profiles;
	
	public ProfileManager() {
		profiles = new HashMap<>();
	}
	
	public void addProfile(String uuid, Profile profile) {
		profiles.put(uuid, profile);
	}
	
	public void deleteProfile(String uuid) {
		profiles.remove(uuid);
	}

	public Profile getProfile(String uuid) {
		return profiles.get(uuid);

	}


}
