package com.andreigeorgescu.foremost;

import java.util.logging.Logger;
import com.andreigeorgescu.foremost.events.*;
import com.andreigeorgescu.foremost.kits.KitEvents;
import com.andreigeorgescu.foremost.kits.KitsManager;
import com.andreigeorgescu.foremost.kits.commands.KitAdminCommand;
import com.andreigeorgescu.foremost.kits.commands.KitCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.andreigeorgescu.foremost.command.*;
import com.saphron.nsa.NSA;

public class Foremost extends JavaPlugin {

	public NSA nsaPlugin = (NSA) this.getServer().getPluginManager().getPlugin("NSA");
    public Logger log = Bukkit.getLogger();
    public ProfileManager profileManager = new ProfileManager();
	public ChatManager chatManager = new ChatManager();
	public FileManager fileManager = new FileManager(this);
	public MessageManager messageManager = new MessageManager(this);
	public CooldownManager cooldownManager = new CooldownManager(this);
	public StaffModeManager staffModeManager = new StaffModeManager(this);
	public KitsManager kitsManager = null;
	public Config config = null;

			
    @Override
    public void onEnable() {
        log.info("Foremost has been enabled.");
        this.getServer().getPluginManager().registerEvents(new EventsListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ColoredSignsEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new ChatEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerTeleportEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new StaffModeEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new KitEvents(), this);

        // =============================================
        // Loading Config
        // =============================================
        config = fileManager.loadConfigFile("./plugins/Foremost", "config.json");
        kitsManager = fileManager.loadKitsManager();
//        kitsManager = new KitsManager();
        
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
        getCommand("tpa").setExecutor(new PlayerTeleportCommand(this));
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("staff").setExecutor(new StaffModeCommand(this));
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("rename").setExecutor(new RenameCommand());
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("kitAdmin").setExecutor(new KitAdminCommand(this));

    }
    
    public void onDisable() {
    	// =============================================
        // Saving config to file
        // =============================================
    	fileManager.saveConfigFile("./plugins/Foremost", "config.json", config);
    	kitsManager.removeExpiredCooldowns();
    	fileManager.saveKitsToFile();
    	kitsManager.handleServerClose();
    	cooldownManager.handleServerClose();
        staffModeManager.handleServerClose();
    }

    public KitsManager getKitsManager() {
        return kitsManager;
    }
}
