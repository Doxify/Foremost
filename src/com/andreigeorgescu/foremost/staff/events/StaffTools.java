package com.andreigeorgescu.foremost.staff.events;

import com.andreigeorgescu.foremost.Foremost;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

public class StaffTools implements Listener {

    @EventHandler
    public void onStaffModeChestUse(PlayerInteractEvent e) {
        if(Foremost.getPlugin().staffManager.isStaffMode(e.getPlayer())) {
            e.setCancelled(true);

            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Player p = e.getPlayer();

                // Chest Viewer
                if(e.getClickedBlock().getType().equals(Material.CHEST)) {
                    Chest chest = (Chest) e.getClickedBlock().getState();
                    Inventory chestInventory = chest.getBlockInventory();

                    if(chestInventory instanceof DoubleChestInventory) {
                        DoubleChest doubleChest = (DoubleChest) chestInventory.getHolder();
                        chestInventory = doubleChest.getInventory();
                    }

                    openVirtualInventory(p, chestInventory);
                    return;
                }

                // Furnace Viewer
                if(e.getClickedBlock().getType().equals(Material.FURNACE)) {
                    Furnace furnace = (Furnace) e.getClickedBlock().getState();
                    openVirtualInventory(p, furnace.getInventory());
                    return;
                }

            }

        }
    }

    private void openVirtualInventory(Player p, Inventory inventory) {
        Inventory virtualInventory = Bukkit.createInventory(null, inventory.getType(), "Staff Inventory Viewer");
        virtualInventory.setContents(inventory.getContents());
        p.openInventory(virtualInventory);

        return;
    }


}
