package com.andreigeorgescu.foremost.events;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;
import com.andreigeorgescu.foremost.utils.ValueFormat;
import com.saphron.nsa.Utilities;
import com.saphron.nsa.user.User;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.VaultEco;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.UUID;

public class ChatEventListener implements Listener {

    private final Foremost plugin;
    private String server;
    public ChatEventListener(Foremost p) {
        this.plugin = p;
        this.server = p.nsaPlugin.config.getServerName();
    }

    @EventHandler ( priority = EventPriority.MONITOR )
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if(!event.isCancelled()) {
            if(!plugin.chatManager.getChatMuteSetting() || event.getPlayer().hasPermission("foremost.chat.bypass")) {
                event.setCancelled(true);
                TextComponent message = createChatComponent(event);
                sendMessageToAllPlayers(message);
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Chat is currently muted and your message has not been sent.");
            }
        }
    }

    public TextComponent createChatComponent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String original = e.getMessage();
        String formatted = ChatColor.translateAlternateColorCodes('&', original);
        User user = plugin.nsaPlugin.userManager.getUser(p.getUniqueId().toString());
        String nickname = user.getNickname();
        String chatColor = ChatColor.translateAlternateColorCodes('&', user.getChatColor());
        String playerGroup = plugin.getPerms().getPrimaryGroup(e.getPlayer());
        URL messageURL = getURLfromMessage(original);

        String groupPrefixRaw = plugin.getChat().getGroupPrefix(e.getPlayer().getWorld().getName(), playerGroup);
        String groupPrefixFormatted = ChatColor.translateAlternateColorCodes('&', groupPrefixRaw);


        TextComponent message = new TextComponent(groupPrefixFormatted + (nickname == null ? e.getPlayer().getName() : nickname) + ChatColor.WHITE + ": " + chatColor + formatted);
        ComponentBuilder hoverComponent = getMessageHoverComponent(e.getPlayer(), user, groupPrefixFormatted);

        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent.create()));

        if(messageURL instanceof URL) {
            message.setClickEvent(new ClickEvent( ClickEvent.Action.OPEN_URL, messageURL.toString()));
        }

        return message;
    }

    public void sendMessageToAllPlayers(TextComponent message) {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.spigot().sendMessage(message);
        }
    }

    public ComponentBuilder getMessageHoverComponent(Player player, User user, String groupPrefixFormatted) {
        StringBuilder hoverComponent = new StringBuilder();

        hoverComponent.append(groupPrefixFormatted + player.getName() + "\n");
        //hoverComponent.append(ChatColor.WHITE + " Join Number: " + ChatColor.GREEN + "TODO" + "\n");
        hoverComponent.append(ChatColor.GRAY + "● " + ChatColor.WHITE + "Join Date: " + ChatColor.GREEN + user.getFirstJoinDate() + "\n");
        hoverComponent.append(ChatColor.GRAY + "● " + ChatColor.WHITE + "Island Level: " + ChatColor.GREEN + getIslandLevelFormatted(player.getUniqueId()) + "\n");
        hoverComponent.append(ChatColor.GRAY + "● " + ChatColor.WHITE + "Money: " + ChatColor.GREEN + Utilities.moneyFormat.format(plugin.getEcon().getBalance(player)));

        return new ComponentBuilder(hoverComponent.toString());
    }

    public URL getURLfromMessage(String message) {
        String[] chat = message.split(" ");

        for(String msg : chat) {
            try {
                URL url = new URL(msg);
                return url;
            } catch (MalformedURLException ignore) { }
        }

        return null;
    }

    public String getIslandLevelFormatted(UUID uuid) {
        long islandLevel = ASkyBlockAPI.getInstance().getLongIslandLevel(uuid);
        if(islandLevel >= 1000000000000L) {
            int settings = ValueFormat.COMMAS | ValueFormat.PRECISION(2) | ValueFormat.TRILLIONS;
            String formatted = ValueFormat.format(islandLevel, settings);
            return formatted;
        } else if(islandLevel >= 1000000000L) {
            int settings = ValueFormat.COMMAS | ValueFormat.PRECISION(2) | ValueFormat.BILLIONS;
            String formatted = ValueFormat.format(islandLevel, settings);
            return formatted;
        } else if(islandLevel >= 1000000L) {
            int settings = ValueFormat.COMMAS | ValueFormat.PRECISION(2) | ValueFormat.MILLIONS;
            String formatted = ValueFormat.format(islandLevel, settings);
            return formatted;
        } else if(islandLevel >= 1000){
            int settings = ValueFormat.COMMAS | ValueFormat.PRECISION(2) | ValueFormat.THOUSANDS;
            String formatted = ValueFormat.format(islandLevel, settings);
            return formatted;
        } else {
            return String.valueOf(islandLevel);
        }
    }


}
