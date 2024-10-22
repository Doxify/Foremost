package com.andreigeorgescu.foremost;

import com.andreigeorgescu.foremost.cooldowns.CooldownManager;
import com.andreigeorgescu.foremost.events.*;
import com.andreigeorgescu.foremost.homes.HomeClickEvent;
import com.andreigeorgescu.foremost.homes.HomeCommand;
import com.andreigeorgescu.foremost.homes.HomeManager;
import com.andreigeorgescu.foremost.kits.KitEvents;
import com.andreigeorgescu.foremost.kits.KitsManager;
import com.andreigeorgescu.foremost.kits.commands.KitAdminCommand;
import com.andreigeorgescu.foremost.kits.commands.KitCommand;
import com.andreigeorgescu.foremost.staff.StaffManager;
import com.andreigeorgescu.foremost.staff.StaffPlaceholders;
import com.andreigeorgescu.foremost.staff.commands.StaffCommand;
import com.andreigeorgescu.foremost.staff.events.ForemostQueueListener;
import com.andreigeorgescu.foremost.staff.events.StaffInventoryUse;
import com.andreigeorgescu.foremost.staff.events.StaffJoinAndLeave;
import com.andreigeorgescu.foremost.staff.events.StaffTools;
import com.andreigeorgescu.foremost.utils.HologramHandler;
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
import org.saphron.saphblock.Saphblock;

public class Foremost extends JavaPlugin {

    public static Foremost instance;
	public NSA nsaPlugin = (NSA) this.getServer().getPluginManager().getPlugin("NSA");
	public Saphblock saphblock = (Saphblock) this.getServer().getPluginManager().getPlugin("Saphblock");
    public ProfileManager profileManager = new ProfileManager();
	public ChatManager chatManager = new ChatManager();
	public FileManager fileManager = new FileManager(this);
	public MessageManager messageManager = new MessageManager(this);
	public CooldownManager cooldownManager = null;
	public KitsManager kitsManager = null;
	public Config config = null;
	public WarpManager warpManager = null;
	public HomeManager homeManager = null;
	public HologramHandler hologramHandler = null;
	public StaffManager staffManager = null;
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

			
    @Override
    public void onEnable() {
        instance = this;

        // =============================================
        // Vault Hook
        // =============================================
        setupChat();
        setupPermissions();
        setupEcon();


        // =============================================
        // Loading Managers
        // =============================================
        config = fileManager.loadConfigFile("./plugins/Foremost", "config.json");
        kitsManager = fileManager.loadKitsManager();
        warpManager = new WarpManager();
        homeManager = new HomeManager(this);
        hologramHandler = new HologramHandler(this);
        cooldownManager = new CooldownManager();
        staffManager = new StaffManager();

        // =============================================
        // Loading Deps
        // =============================================
        saphblock = (Saphblock) this.getServer().getPluginManager().getPlugin("Saphblock");

        // =============================================
        // Loading Event Listeners
        // =============================================
        this.getServer().getPluginManager().registerEvents(new EventsListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ColoredSignsEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new ChatEventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerTeleportEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new KitEvents(), this);
        this.getServer().getPluginManager().registerEvents(new WarpEventListener(warpManager), this);
        this.getServer().getPluginManager().registerEvents(new HomeClickEvent(this), this);

        this.getServer().getPluginManager().registerEvents(new StaffJoinAndLeave(), this);
        this.getServer().getPluginManager().registerEvents(new StaffInventoryUse(), this);
        this.getServer().getPluginManager().registerEvents(new StaffTools(), this);


        // =============================================
        // Plugin Commands
        // =============================================
        getCommand("msg").setExecutor(new MessageCommand(this, nsaPlugin));
        getCommand("r").setExecutor(new ReplyCommand(this, nsaPlugin));
        getCommand("day").setExecutor(new DayCommand());
        getCommand("night").setExecutor(new NightCommand());
        getCommand("sunset").setExecutor(new SunsetCommand());
        getCommand("ci").setExecutor(new ClearInventoryCommand());
        getCommand("repair").setExecutor(new RepairCommand());
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
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("rename").setExecutor(new RenameCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("kitAdmin").setExecutor(new KitAdminCommand(this));
        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("spawnmob").setExecutor(new SpawnMobCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("condense").setExecutor(new CondenseCommand(this));
        getCommand("staff").setExecutor(new StaffCommand());
        getCommand("helpop").setExecutor(new HelpOpCommand());


        // Registering placeholders
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new StaffPlaceholders().register();
        }

        // Registering queue channel
        getServer().getMessenger().registerOutgoingPluginChannel(this, "Queue");
        getServer().getMessenger().registerIncomingPluginChannel(this, "ForemostQueue", new ForemostQueueListener());


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
        warpManager.handleServerClose();
        homeManager.handleServerClose();
        staffManager.handleServerClose();
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
    public static Foremost getPlugin() { return instance; }
}
