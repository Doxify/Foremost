package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpawnMobCommand implements CommandExecutor {

    private static final List<EntityType> ALLOWED_MOBS = new ArrayList<>(Arrays.asList(
            EntityType.CREEPER,
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.BAT
    ));

    private EntityType isInList(String name) {
        for(EntityType type : ALLOWED_MOBS) {
            if(type.getName().toLowerCase().contains(name.toLowerCase())) {
                return type;
            }
        }
        return null;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            // Checking if the player has permission
            if(!p.hasPermission("foremost.spawnmob")) {
                p.sendMessage(Utilities.NO_PERMISSION);
                return true;
            }

            // Checking if the player is on their own island.
            Island island = ASkyBlockAPI.getInstance().getIslandAt(p.getLocation());
            if(!island.getMembers().contains(p.getUniqueId())) {
                p.sendMessage(ChatColor.RED + "/spawnmob can only be used on your island.");
                return true;
            }


            // Command Format: /spawnmob <EntityType>
            if(args.length != 1) {
                sendUsageMessage(p);
                return true;
            } else {
                EntityType entityType = isInList(args[0]);
                if(entityType instanceof EntityType) {
                    Block targettedBlock = p.getTargetBlock(null, 200);
                    Location playerLocation = p.getLocation();
                    Location targetLocation = targettedBlock.getLocation();

                    // Only allowing the player to spawn a mob within 16 blocks of their location.
                    if((Math.abs(targetLocation.getBlockZ() - playerLocation.getBlockZ()) <= 16) && (Math.abs(targetLocation.getBlockX() - playerLocation.getBlockX()) <= 16)) {
                        targettedBlock.getLocation().getWorld().spawnEntity(targettedBlock.getLocation().add(0,2,0), entityType);
                        p.sendMessage(ChatColor.GREEN + "Spawned one " + entityType.getName() + ".");
                        return true;
                    } else {
                        p.sendMessage(ChatColor.RED + "You can only spawn a mob within 16 blocks.");
                        return true;
                    }

                } else {
                    p.sendMessage(ChatColor.RED + "That mob is not able to be spawned.");
                    sendUsageMessage(p);
                    return true;
                }
            }



        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
            return true;
        }
    }

    private void sendUsageMessage(Player p) {
        StringBuilder mobList = new StringBuilder();
        p.sendMessage(ChatColor.RED + "Usage: /spawnmob <mob_type>");
        for(EntityType type : ALLOWED_MOBS) {
            mobList.append(ChatColor.GRAY + type.getName() + " ");
        }
        p.sendMessage(ChatColor.RED + "Allowed Mobs: " + mobList.toString());
    }

}
