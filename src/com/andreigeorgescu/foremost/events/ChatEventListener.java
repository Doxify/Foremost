package com.andreigeorgescu.foremost.events;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.DecimalFormat;

public class ChatEventListener implements Listener {

    private final Foremost plugin;
    private String server;
    public ChatEventListener(Foremost p) {
        this.plugin = p;
        server = p.nsaPlugin.getServerName();
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
            sendMessageToAllPlayers(message);

        }

    }

    public TextComponent createChatComponent(AsyncPlayerChatEvent event) {
        String original = event.getMessage();
        String formatted = ChatColor.translateAlternateColorCodes('&', original);
        String uuid = event.getPlayer().getUniqueId().toString();
        com.saphron.nsa.Profile profile = plugin.nsaPlugin.getProfileManager().getProfile(uuid);
        String nickname = profile.getNickname();
        String chatColor = ChatColor.translateAlternateColorCodes('&', profile.getChatColor());
        String playerGroup = plugin.perms.getPrimaryGroup(event.getPlayer());

        String groupPrefixRaw = plugin.chat.getGroupPrefix(event.getPlayer().getWorld().getName(), playerGroup);
        String groupPrefixFormatted = ChatColor.translateAlternateColorCodes('&', groupPrefixRaw);


        TextComponent message = new TextComponent(groupPrefixFormatted + (nickname == null ? event.getPlayer().getName() : nickname) + ChatColor.WHITE + ": " + chatColor + formatted);

        ComponentBuilder hoverComponent = getMessageHoverComponent(event.getPlayer(), profile, groupPrefixFormatted);

        message.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, hoverComponent.create()));

        return message;
    }

    public void sendMessageToAllPlayers(TextComponent message) {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.spigot().sendMessage(message);
        }
    }

    public ComponentBuilder getMessageHoverComponent(Player player, com.saphron.nsa.Profile profile, String groupPrefixFormatted) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        StringBuilder hoverComponent = new StringBuilder();

        hoverComponent.append(groupPrefixFormatted + player.getName() + "\n");

        if(!plugin.nsaPlugin.getServerName().toUpperCase().contains("LOBBY")) {
            hoverComponent.append(ChatColor.WHITE + " Money: " + ChatColor.GREEN + df.format(plugin.econ.getBalance(player)) + "\n");
        }

        hoverComponent.append(ChatColor.WHITE + " Join Number: " + ChatColor.GREEN + profile.getJoinNumber() + "\n");
        hoverComponent.append(ChatColor.WHITE + " Join Date: " + ChatColor.GREEN + profile.getFirstJoined());

        return new ComponentBuilder(hoverComponent.toString());
    }
}
