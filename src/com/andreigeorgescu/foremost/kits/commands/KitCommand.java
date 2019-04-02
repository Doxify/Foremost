package com.andreigeorgescu.foremost.kits.commands;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.kits.Kit;
import com.andreigeorgescu.foremost.kits.KitGUI;
import com.andreigeorgescu.foremost.kits.KitsManager;
import com.saphron.nsa.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

public class KitCommand implements CommandExecutor {

    Foremost plugin;
    KitsManager kitsManager;

    public KitCommand(Foremost p) { plugin = p; kitsManager = plugin.getKitsManager(); }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            /*
            Commands:
                1. /kit - Sends the player a list of the kits
                2. /kit "name" - Gives the player the requested kit
                3. /kit preview "name" - Gives the player a preview gui
             */
            switch (args.length) {
                case 0: {
                    kitsManager.getKitList(p);
                    break;
                }
                case 1: {
                    String kitName = args[0];
                    if(kitsManager.kitExists(kitName)) {
                        Kit kit = kitsManager.getKitObject(kitName);

                        if(kit.hasPermission(p) && kit.getCooldown() != -1) {
                            if(!kitsManager.hasCooldown(p.getUniqueId(), kit.getName())) {
                                kitsManager.givePlayerKit(p, kit);
                                break;
                            } else {
                                if(kitsManager.isCooldownOver(p.getUniqueId(), kit.getName())) {
                                    kitsManager.removeCooldown(p.getUniqueId(), kit.getName());
                                    kitsManager.givePlayerKit(p, kit);
                                    break;
                                } else {
                                    LocalDateTime cooldown = kitsManager.getCooldown(p.getUniqueId(), kit.getName());
                                    p.sendMessage(ChatColor.RED + kit.getName() + " is on cooldown for " + kitsManager.getCooldownString(cooldown) + ".");
                                    break;
                                }
                            }
                        } else {
                            p.sendMessage(Utilities.noKitPermission(kit.getName()));
                            break;
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "That kit doesn't exist.");
                        break;
                    }
                }
                case 2: {
                    if(args[0].equalsIgnoreCase("PREVIEW")) {
                        String kitName = args[1];
                        if(kitsManager.kitExists(kitName)) {
                            Kit kit = kitsManager.getKitObject(kitName);
                            p.openInventory(new KitGUI().generateKitPreviewGUI(p, kit));
                            break;
                        } else {
                            p.sendMessage(ChatColor.RED + "That kit doesn't exist.");
                            break;
                        }
                    } else {
                        p.sendMessage(getKitCommandGuide());
                        break;
                    }
                }
                default: {
                    p.sendMessage(getKitCommandGuide());
                    break;
                }
            }

        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
        }

        return true;
    }

    public String getKitCommandGuide() {
        StringBuilder string = new StringBuilder(ChatColor.RED + "Usage: ");
        string.append("\n/kit - Get a list of kits");
        string.append("\n/kit <name> - Redeem a kit");
        string.append("\n/kit preview <name> - Preview a kit");

        return string.toString();
    }
}
