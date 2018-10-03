package com.andreigeorgescu.foremost.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;

public class EventsListener implements Listener {
	
	private final Foremost plugin;

	public EventsListener(Foremost p) {
        this.plugin = p;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Profile profile = new Profile(null);
		this.plugin.profileManager.addProfile(event.getPlayer().getUniqueId().toString(), profile);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		plugin.profileManager.deleteProfile(event.getPlayer().getUniqueId().toString());
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
	public void onChatMessage(AsyncPlayerChatEvent event) {
		if(plugin.chatManager.getChatMuteSetting()) {
			if(!event.getPlayer().hasPermission("foremost.chat.bypass")) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "Chat is currently muted and your message has not been sent.");
			}
		} else {
			String original = event.getMessage();
			String formatted = ChatColor.translateAlternateColorCodes('&', original);
			event.setMessage(formatted);
		}
		
	}
	
//	@EventHandler
//	public void preCommand(PlayerCommandPreprocessEvent event){
//		if()
//	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory().getTitle().contains("Inventory Viewer")) {
			event.setCancelled(true);
		};
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN) {
			plugin.profileManager.getProfile(event.getPlayer().getUniqueId().toString()).setLastLocation(event.getFrom());
		}
	}
}
