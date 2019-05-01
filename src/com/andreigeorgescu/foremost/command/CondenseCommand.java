package com.andreigeorgescu.foremost.command;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;
import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CondenseCommand implements CommandExecutor {

    private Foremost plugin;

    public CondenseCommand(Foremost plugin) {
        this.plugin = plugin;
    }

    private static final List<Material> TYPES = new ArrayList<>(
            Arrays.asList(
                    Material.EMERALD_ORE,
                    Material.EMERALD,
                    Material.DIAMOND_ORE,
                    Material.DIAMOND,
                    Material.IRON_ORE,
                    Material.IRON_INGOT,
                    Material.GOLD_ORE,
                    Material.GOLD_INGOT,
                    Material.GOLD_NUGGET,
                    Material.REDSTONE,
                    Material.REDSTONE_ORE,
                    Material.LAPIS_ORE,
                    Material.COAL_ORE,
                    Material.COAL
            ));

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.condense")) {
                Player p = (Player) sender;
                Profile profile = plugin.profileManager.getProfile(p.getUniqueId().toString());

                // Checking for cooldown
                if(profile.getCondenseCooldown()) {
                    p.sendMessage(ChatColor.RED + "You can only use /condense once every 5 minutes.");
                } else {
                    // Condensing
                    condensePlayerInventory(p);

                    profile.setCondenseCooldown(true);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
                        @Override
                        public void run() {
                            OfflinePlayer target = Bukkit.getOfflinePlayer(p.getUniqueId());
                            if(target.isOnline()) {
                                profile.setCondenseCooldown(false);
                            }
                        }
                    }, 300 * 20);
                }

                return true;
            } else {
                sender.sendMessage(Utilities.NO_PERMISSION);
                return true;
            }
        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
            return true;
        }
    }

    private void condensePlayerInventory(Player p) {
        boolean droppedOnGround = false;
        int condensedItemsCount = 0;

        // Going through each slot of their inventory
        for(ItemStack item : p.getInventory().getContents()) {
            if(item != null) {
                if(TYPES.contains(item.getType())) {
                    // Making sure they have at least enough for one block
                    if(item.getAmount() >= 9) {
                        // Getting the block the item corresponds to
                        Material condencedType = getCondensedType(item.getType());
                        if(condencedType != null) {
                            // Condensing the inventory and giving the player the items
                            ItemStack[] condencedItems = condenseItemStack(item, condencedType);
                            condensedItemsCount += (condencedItems.length * 9);
                            if(givePlayerItems(condencedItems, p)) {
                                droppedOnGround = true;
                            }
                        }
                    }
                }
            }
        }

        if(condensedItemsCount > 0) {
            p.sendMessage(ChatColor.GREEN + "Successfully condensed " + condensedItemsCount + " items into blocks.");
            if(droppedOnGround) {
                p.sendMessage(ChatColor.RED + "Some of your blocks didn't fit in your inventory, check the ground!");
            }
        } else {
            p.sendMessage(ChatColor.RED + "Couldn't find any items in your inventory to condense.");
        }
    }

    // Condenses the player's inventory and then returns the items it should give the player.
    private ItemStack[] condenseItemStack(ItemStack itemStack, Material material) {
        ItemStack[] items;
        if(itemStack.getAmount() >= 63) {
            itemStack.setAmount(itemStack.getAmount() - 63);
            items = new ItemStack[7];
        } else if(itemStack.getAmount() >= 54) {
            itemStack.setAmount(itemStack.getAmount() - 54);
            items = new ItemStack[6];
        } else if(itemStack.getAmount() >= 45) {
            itemStack.setAmount(itemStack.getAmount() - 45);
            items = new ItemStack[5];
        } else if(itemStack.getAmount() >= 36) {
            itemStack.setAmount(itemStack.getAmount() - 36);
            items = new ItemStack[4];
        } else if(itemStack.getAmount() >= 27) {
            itemStack.setAmount(itemStack.getAmount() - 27);
            items = new ItemStack[3];
        } else if(itemStack.getAmount() >= 18) {
            itemStack.setAmount(itemStack.getAmount() - 18);
            items = new ItemStack[2];
        } else {
            itemStack.setAmount(itemStack.getAmount() - 9);
            items = new ItemStack[1];
        }

        for(int i = 0; i < items.length; i++) {
            items[i] = new ItemStack(material);
        }

        return items;
    }

    // Returns true if it dropped some on the ground, false if it didn't.
    private boolean givePlayerItems(ItemStack[] items, Player p) {
        HashMap<Integer, ItemStack> leftOverItems = p.getInventory().addItem(items);

        if(leftOverItems.isEmpty()) {
            return false;
        } else {
            for(ItemStack item : leftOverItems.values()) {
                p.getWorld().dropItem(p.getLocation(), item);
            }
            return true;
        }
    }

    private Material getCondensedType(Material material) {
        switch (material) {
            case EMERALD_ORE: case EMERALD: {
                return Material.EMERALD_BLOCK;
            }
            case DIAMOND_ORE: case DIAMOND: {
                return Material.DIAMOND_BLOCK;
            }
            case IRON_ORE: {
                return Material.IRON_BLOCK;
            }
            case IRON_INGOT: {
                return Material.IRON_BLOCK;
            }
            case GOLD_ORE: {
                return Material.GOLD_BLOCK;
            }
            case GOLD_INGOT: {
                return Material.GOLD_BLOCK;
            }
            case REDSTONE_ORE: case REDSTONE: {
                return Material.REDSTONE_BLOCK;
            }
            case LAPIS_ORE: {
                return Material.LAPIS_BLOCK;
            }
            case COAL_ORE: {
                return Material.COAL_BLOCK;
            }
            case COAL: {
                return Material.COAL_BLOCK;
            }
            case GOLD_NUGGET: {
                return Material.GOLD_INGOT;
            }
        }
        return null;
    }



}
