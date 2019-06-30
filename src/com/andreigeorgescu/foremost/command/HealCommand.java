package com.andreigeorgescu.foremost.command;

import com.andreigeorgescu.foremost.cooldowns.Cooldown;
import com.andreigeorgescu.foremost.cooldowns.CooldownManager;
import com.saphron.nsa.Utilities;
import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    private Foremost plugin;

    public HealCommand(Foremost p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            switch (args.length) {
                // Sender ran "/heal"
                case 0: {
                    if(sender.hasPermission("foremost.heal.cooldown.3")) {
                        if(!Foremost.getPlugin().cooldownManager.hasCooldown(CooldownManager.COOLDOWN.HEAL, p.getUniqueId())) {
                            healPlayer(p);
                            p.sendMessage(ChatColor.GRAY + "/heal is now on cooldown for 3 minutes.");
                            Foremost.getPlugin().cooldownManager.addCooldown(CooldownManager.COOLDOWN.HEAL, p.getUniqueId(), 180);
                        } else {
                            Cooldown cooldown = Foremost.getPlugin().cooldownManager.getCooldown(CooldownManager.COOLDOWN.HEAL, p.getUniqueId());
                            p.sendMessage(ChatColor.RED + "/heal is on cooldown for " + Utilities.getTimeStringWordsWithSeconds(cooldown.getRemainingCooldown()));
                            p.sendMessage(ChatColor.RED + "Purchase a higher rank for a reduced or no cooldown.");
                        }
                    } else if (p.hasPermission("foremost.heal.bypass")) {
                        healPlayer(p);
                    } else {
                        sender.sendMessage(Utilities.NO_PERMISSION);
                    }
                    return true;
                }
                // Sender ran "/heal <playername>"
                case 1: {
                    if(sender.hasPermission("foremost.heal.others")) {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        if(target != null) {
                            healPlayer(p);
                            sender.sendMessage(ChatColor.GREEN + "You have healed " + target.getName() + ".");
                            target.sendMessage(ChatColor.GREEN + "You have been healed by " + sender.getName() + ".");
                        } else {
                            sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
                        }
                    } else {
                        sender.sendMessage(Utilities.NO_PERMISSION);
                    }
                    return true;
                }
                default: {
                    p.sendMessage(ChatColor.RED + "Usage: /heal");
                    return true;
                }
            }
        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
            return true;
        }
    }

    public void healPlayer(Player player) {
        player.setHealth(player.getHealthScale());
        player.setFoodLevel(20);
        player.sendMessage(ChatColor.GREEN + "You have been healed.");
    }
}
