package com.andreigeorgescu.foremost.staff;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StaffModePlayer {

    private Player player;
    private Location previousLocation;
    private ItemStack[] previousArmor;
    private ItemStack[] previousInventory;
    private boolean vanished;


    public StaffModePlayer(Player player) {
        this.player = player;
        this.previousLocation = player.getLocation().clone();
        this.previousInventory = player.getInventory().getContents().clone();
        this.previousArmor = player.getInventory().getArmorContents().clone();
        this.vanished = false;
    }

    public void setVanished(boolean vanished) { this.vanished = vanished; }

    public boolean isVanished() { return this.vanished; }

    public Location getPreviousLocation() { return this.previousLocation; }

    public ItemStack[] getPreviousArmor() { return this.previousArmor; }

    public ItemStack[] getPreviousInventory() { return this.previousInventory; }

    public Player getPlayer() { return this.player; }


}
