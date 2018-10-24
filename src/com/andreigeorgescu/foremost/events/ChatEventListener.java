package com.andreigeorgescu.foremost.events;

import com.andreigeorgescu.foremost.Foremost;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;

public class ChatEventListener implements Listener {

    private final Foremost plugin;
    private final String[] colors = {"&f", "&a", "&6", "&e", "&1", "&c", "&d", "&7"};

    public ChatEventListener(Foremost p) {
        this.plugin = p;
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
            event.setCancelled(true);
            TextComponent message = createChatComponent(event);
            sendMessageToAllPlayers(message, event.getPlayer());

        }

    }

    public TextComponent createChatComponent(AsyncPlayerChatEvent event) {
        String original = event.getMessage();
        String formatted = ChatColor.translateAlternateColorCodes('&', original);
        String uuid = event.getPlayer().getUniqueId().toString();
        com.saphron.nsa.Profile profile = plugin.nsaPlugin.getProfileManager().getProfile(uuid);
        String chatColor = ChatColor.translateAlternateColorCodes('&', profile.getChatColor());
        String playerGroup = plugin.perms.getPrimaryGroup(event.getPlayer());

        String groupPrefixRaw = plugin.chat.getGroupPrefix(event.getPlayer().getWorld().getName(), playerGroup);
        String groupPrefixFormatted = ChatColor.translateAlternateColorCodes('&', groupPrefixRaw);

        TextComponent message = new TextComponent(groupPrefixFormatted + event.getPlayer().getName() + ChatColor.WHITE + ": " + chatColor + formatted);

        ComponentBuilder hoverComponent = new ComponentBuilder(groupPrefixFormatted + event.getPlayer().getName() + "'s profile" + "\n"
                                                                  + ChatColor.WHITE + " * Rank: " + ChatColor.GREEN + plugin.perms.getPrimaryGroup(event.getPlayer()) + "\n"
                                                                  + ChatColor.WHITE + " * Money: " + ChatColor.GREEN + plugin.econ.getBalance(event.getPlayer()));

        message.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, hoverComponent.create()));

        return message;
    }

    public void sendMessageToAllPlayers(TextComponent message, Player sender) {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.spigot().sendMessage(message);
        }
    }
}
