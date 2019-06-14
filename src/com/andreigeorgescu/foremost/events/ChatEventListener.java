package com.andreigeorgescu.foremost.events;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.utils.ValueFormat;
import com.saphron.nsa.Utilities;
import com.saphron.nsa.tags.Tag;
import com.saphron.nsa.user.User;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class ChatEventListener implements Listener {

    private final Foremost plugin;
    public static boolean ECON_LOADED;
    public static boolean ASKYBLOCK_LOADED;
    public static boolean IS_LOBBY;

    public ChatEventListener(Foremost p) {
        this.plugin = p;
        ECON_LOADED = Bukkit.getServer().getPluginManager().isPluginEnabled("SaneEconomy");
        ASKYBLOCK_LOADED = Bukkit.getServer().getPluginManager().isPluginEnabled("ASkyBlock");
        IS_LOBBY = Bukkit.getServer().getPluginManager().isPluginEnabled("Saphub");
    }

    @EventHandler ( priority = EventPriority.MONITOR )
    public void onChatMessage(AsyncPlayerChatEvent e) {
        if(!e.isCancelled()) {
            Player p = e.getPlayer();
            if(plugin.nsaPlugin.userManager.isLoaded(p.getUniqueId())) {
                if(!plugin.chatManager.getChatMuteSetting() || p.hasPermission("foremost.chat.bypass")) {
                    e.setCancelled(true);
                    TextComponent message = createChatComponent(e);
                    sendMessageToAllPlayers(message);
                } else {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "Chat is currently muted, please try again once chat is unmuted.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Please wait until your profile has been loaded to chat.");
                e.setCancelled(true);
            }
        }
    }

    public TextComponent createChatComponent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        User user = plugin.nsaPlugin.userManager.getUser(p.getUniqueId().toString());
        String formattedChatMessage = handleChatMessageFormatting(p, user, e.getMessage());
        TextComponent formattedTextComponent = handleTextComponentFormatting(p, user, formattedChatMessage);
        ComponentBuilder hoverComponent = getMessageHoverComponent(p, user);
        URL messageURL = getUrlFromMessage(e.getMessage());

        formattedTextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent.create()));

        if(messageURL != null) {
            formattedTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, messageURL.toString()));
        } else {
            formattedTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + p.getName()));
        }
        return formattedTextComponent;
    }

    private String handleChatMessageFormatting(Player p, User user, String message) {
        String[] unformattedMessageArr = message.split(" ");
        StringBuilder formattedMessage = new StringBuilder();
        boolean canFormatChat = p.hasPermission("foremost.chat.color.message");
        String chatColor = ChatColor.translateAlternateColorCodes('&', user.getChatColor());

        // If the user can format their chat messages then we will format each arg
        // individually keep track of the previous chat color to apply it to the next
        // arg as the user probably won't supply a format for each arg.
        if(canFormatChat) {
            if(message.contains("&")) {
                String lastChatColor = ChatColor.WHITE.toString();
                for(String arg : unformattedMessageArr) {
                    if(arg.contains("&")) {
                        String formatted = ChatColor.translateAlternateColorCodes('&', arg);
                        lastChatColor = ChatColor.getLastColors(formatted);
                        formattedMessage.append(formatted + " ");
                    } else {
                        formattedMessage.append(lastChatColor + arg + " ");
                    }
                }
                return formattedMessage.toString();
            } else {
                for(String arg : unformattedMessageArr) {
                    formattedMessage.append(chatColor + arg + " ");
                }
                return formattedMessage.toString();
            }
        // If the user can't format their own chat messages, then we will just apply the chat color
        // that is stored in the database.
        } else {
            for(String arg : unformattedMessageArr) {
                formattedMessage.append(chatColor + arg + " ");
            }
            return formattedMessage.toString();
        }
    }

    private TextComponent handleTextComponentFormatting(Player p, User user, String message) {
        String playerGroup = plugin.getPerms().getPrimaryGroup(p);
        String groupPrefixRaw = plugin.getChat().getGroupPrefix(p.getName(), playerGroup);
        String groupPrefixFormatted = ChatColor.translateAlternateColorCodes('&', groupPrefixRaw);
        String displayName = (user.getNickname() == null ? p.getName() : user.getNickname());

        // Creating the chat line string with the user's rank, tag (if one exists), display name, and message.
        StringBuilder chatLineString = new StringBuilder();
        chatLineString.append(groupPrefixFormatted + "");

        if(user.getTagName() != null) {
            Tag tag = plugin.nsaPlugin.tagManager.getTag(user.getTagName());
            if(tag.isPrefix()) {
                chatLineString.append(tag.getTagFormatted() + displayName + ChatColor.WHITE + ": ");
            } else {
                chatLineString.append(displayName + " " + tag.getTagFormatted() + ChatColor.WHITE + ": ");
            }
        } else {
            chatLineString.append(displayName + ChatColor.WHITE + ": ");
        }

        chatLineString.append(message);

        TextComponent textComponent = new TextComponent(chatLineString.toString());
        return textComponent;
    }


    private ComponentBuilder getMessageHoverComponent(Player p, User user) {
        StringBuilder hoverComponent = new StringBuilder();
        String playerGroup = plugin.getPerms().getPrimaryGroup(p);
        String groupPrefixRaw = plugin.getChat().getGroupPrefix(p.getName(), playerGroup);
        String groupPrefixFormatted = ChatColor.translateAlternateColorCodes('&', groupPrefixRaw);

        hoverComponent.append(groupPrefixFormatted + p.getName() + "\n");

        if(ECON_LOADED) {
            hoverComponent.append(ChatColor.WHITE + "Balance: " + ChatColor.GOLD + Utilities.moneyFormat.format(plugin.getEcon().getBalance(p)) + "\n");
            if(ASKYBLOCK_LOADED) {
                hoverComponent.append(ChatColor.WHITE + "Island Level: " + ChatColor.GOLD + getIslandLevelFormatted(p.getUniqueId()) + "\n\n");
            } else {
                hoverComponent.append("\n");
            }
        }

        if(!IS_LOBBY) {
            hoverComponent.append(ChatColor.WHITE + "Played on this server for " + ChatColor.RED + Utilities.getTimeStringWords(p.getStatistic(Statistic.PLAY_ONE_TICK) / 20) + "\n");
        }

        hoverComponent.append(ChatColor.WHITE + "First joined Saphron on " + ChatColor.RED + user.getFirstJoinDate());

        return new ComponentBuilder(hoverComponent.toString());
    }

    private URL getUrlFromMessage(String message) {
        String[] chat = message.split(" ");

        for(String msg : chat) {
            try {
                URL url = new URL(msg);
                return url;
            } catch (MalformedURLException ignore) { }
        }

        return null;
    }

    private void sendMessageToAllPlayers(TextComponent message) {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.spigot().sendMessage(message);
        }
    }

    public static String getIslandLevelFormatted(UUID uuid) {
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
