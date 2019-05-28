package com.andreigeorgescu.foremost.events;

import com.andreigeorgescu.foremost.kits.Kit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;
import org.bukkit.inventory.ItemStack;


public class EventsListener implements Listener {
	
	private final Foremost plugin;
	public EventsListener(Foremost p) {
        this.plugin = p;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Profile profile = new Profile(null);
		Player p = e.getPlayer();
		this.plugin.profileManager.addProfile(p.getUniqueId().toString(), profile);
		if(plugin.config.getSpawn() != null) {
			p.teleport(plugin.config.getSpawn());
		}

		// Giving players their default kit as per the value for "defaultKit" in config.json
		if(!p.hasPlayedBefore()) {
			if(plugin.config.getDefaultKit() != null) {
				if(plugin.kitsManager.kitExists(plugin.config.getDefaultKit())) {
					Kit defaultKit = plugin.kitsManager.getKitObject(plugin.config.getDefaultKit());
					givePlayerDefaultKit(p, defaultKit);
				}
			}
		}
	}

	// Helper function for onPlayerJoin
	private void givePlayerDefaultKit(Player p, Kit defaultKit) {
		for(ItemStack item : defaultKit.getKit().clone()) {
			if(item.getType().name().toLowerCase().contains("helmet")) {
				if(p.getEquipment().getHelmet() == null) {
					p.getEquipment().setHelmet(item);
				} else {
					p.getInventory().addItem(item);
				}
			} else if(item.getType().name().toLowerCase().contains("chestplate")) {
				if(p.getEquipment().getChestplate() == null) {
					p.getEquipment().setChestplate(item);
				} else {
					p.getInventory().addItem(item);
				}
			} else if(item.getType().name().toLowerCase().contains("leggings")) {
				if(p.getEquipment().getLeggings() == null) {
					p.getEquipment().setLeggings(item);
				} else {
					p.getInventory().addItem(item);
				}
			} else if(item.getType().name().toLowerCase().contains("boots")) {
				if(p.getEquipment().getBoots() == null) {
					p.getEquipment().setBoots(item);
				} else {
					p.getInventory().addItem(item);
				}
			} else {
				p.getInventory().addItem(item);
			}
		}
	}

	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		plugin.profileManager.deleteProfile(event.getPlayer().getUniqueId().toString());
		plugin.cooldownManager.handleDisconnect(event.getPlayer().getUniqueId().toString());
	}

	@EventHandler
	public void onPlayerDeath(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		event.setRespawnLocation(plugin.config.getSpawn());
	}
	
	@EventHandler
	public void onRainStart(WeatherChangeEvent event) {
    	event.setCancelled(true);
    }
	
	@EventHandler
	public void onThunderStart(ThunderChangeEvent event) {
    	event.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory().getTitle().contains("Inventory Viewer")) {
			event.setCancelled(true);
		};
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		boolean isCitizensNPC = event.getPlayer().hasMetadata("NPC");
		if(!isCitizensNPC) {
			if(event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN) {
				if(event.getFrom() instanceof Location) {
					Profile profile = plugin.profileManager.getProfile(event.getPlayer().getUniqueId().toString());
					profile.setLastLocation(event.getFrom());
				}
			}
		}
	}
}
