package com.andreigeorgescu.foremost;

import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.andreigeorgescu.foremost.command.*;
import com.andreigeorgescu.foremost.events.EventsListener;
import com.andreigeorgescu.foremost.events.ChatEventListener;
import com.andreigeorgescu.foremost.events.ColoredSignsEventListener;

import com.saphron.nsa.NSA;

public class Foremost extends JavaPlugin {

	public NSA nsaPlugin = (NSA) this.getServer().getPluginManager().getPlugin("NSA");
    public Logger log = Bukkit.getLogger();
    public ProfileManager profileManager = new ProfileManager();
	public ChatManager chatManager = new ChatManager();
	public FileManager fileManager = new FileManager(this);
	public MessageManager messageManager = new MessageManager(this);
	public CooldownManager cooldownManager = new CooldownManager(this);
	public Config config = null;
    public static Permission perms = null;
    public static Chat chat = null;
    public static Economy econ = null;
			
    @Override
    public void onEnable() {
        log.info("Foremost has been enabled.");
        this.getServer().getPluginManager().registerEvents(new EventsListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ColoredSignsEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new ChatEventListener(this), this);
        setupPermissions();
        setupChat();
        setupEcon();
        log.info("Vault permissions, chat, and economy has been loaded.");

        // =============================================
        // Loading Config
        // =============================================
        config = fileManager.loadConfigFile("./plugins/Foremost", "config.json");
        
        
        // =============================================
        // Plugin Commands
        // =============================================
        getCommand("msg").setExecutor(new MessageCommand(this, nsaPlugin));
        getCommand("r").setExecutor(new ReplyCommand(this, nsaPlugin));
        getCommand("day").setExecutor(new DayCommand());
        getCommand("night").setExecutor(new NightCommand());
        getCommand("sunset").setExecutor(new SunsetCommand());
        getCommand("ci").setExecutor(new ClearInventoryCommand());
        getCommand("repair").setExecutor(new RepairCommand(this));
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("heal").setExecutor(new HealCommand(this));
        getCommand("chat").setExecutor(new ChatManagerCommand(chatManager));
        getCommand("gm").setExecutor(new GamemodeCommand());
        getCommand("kill").setExecutor(new KillCommand());
        getCommand("inv").setExecutor(new InventoryCommand());
        getCommand("sudo").setExecutor(new SudoCommand());
        getCommand("killall").setExecutor(new KillAllCommand());
        getCommand("tphere").setExecutor(new TeleportHereCommand());
        getCommand("tppos").setExecutor(new TeleportPositionCommand());
        getCommand("tpall").setExecutor(new TeleportAllCommand());
        getCommand("back").setExecutor(new BackCommand(this));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("enderchest").setExecutor(new EnderChestCommand());
        getCommand("workbench").setExecutor(new WorkBenchCommand());
        getCommand("teleport").setExecutor(new TeleportCommand());
        getCommand("give").setExecutor(new GiveCommand());
        getCommand("tpa").setExecutor(new PlayerTeleportCommand(this));
        getCommand("fly").setExecutor(new FlyCommand());

    }
    
    public void onDisable() {
    	// =============================================
        // Saving config to file
        // =============================================
    	fileManager.saveConfigFile("./plugins/Foremost", "config.json", config);
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupEcon() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
        return econ != null;
    }
    


}
