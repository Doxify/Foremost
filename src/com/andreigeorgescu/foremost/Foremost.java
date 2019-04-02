package com.andreigeorgescu.foremost;

import java.util.logging.Logger;
import com.andreigeorgescu.foremost.events.*;
import com.andreigeorgescu.foremost.kits.KitEvents;
import com.andreigeorgescu.foremost.kits.KitsManager;
import com.andreigeorgescu.foremost.kits.commands.KitAdminCommand;
import com.andreigeorgescu.foremost.kits.commands.KitCommand;
import com.andreigeorgescu.foremost.waraps.WarpCommand;
import com.andreigeorgescu.foremost.waraps.WarpEventListener;
import com.andreigeorgescu.foremost.waraps.WarpManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
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
	public WarpManager warpManager = null;
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

			
    @Override
    public void onEnable() {
        // =============================================
        // Vault Hook
        // =============================================
        setupChat();
        setupPermissions();
        setupEcon();


        // =============================================
        // Loading Configs
        // =============================================
        config = fileManager.loadConfigFile("./plugins/Foremost", "config.json");
        kitsManager = fileManager.loadKitsManager();
        warpManager = new WarpManager();

        // =============================================
        // Loading Event Listeners
        // =============================================
        this.getServer().getPluginManager().registerEvents(new EventsListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ColoredSignsEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new ChatEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerTeleportEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new StaffModeEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new KitEvents(), this);
        this.getServer().getPluginManager().registerEvents(new WarpEventListener(warpManager), this);


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
        getCommand("warp").setExecutor(new WarpCommand(warpManager));
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
        // Saving configs to file
        // =============================================
    	fileManager.saveConfigFile("./plugins/Foremost", "config.json", config);
    	kitsManager.removeExpiredCooldowns();
    	fileManager.saveKitsToFile();
    	kitsManager.handleServerClose();
    	cooldownManager.handleServerClose();
        staffModeManager.handleServerClose();
        warpManager.handleServerClose();
    }

    public KitsManager getKitsManager() {
        return kitsManager;
    }

    private boolean setupEcon() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if(rsp == null) {
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if(rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }

    public Permission getPerms() { return perms; }
    public Chat getChat() { return chat; }
    public Economy getEcon() { return econ; }
}
