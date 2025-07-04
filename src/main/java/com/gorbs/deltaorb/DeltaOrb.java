package com.gorbs.deltaorb;

import com.gorbs.deltaorb.commands.*;
import com.gorbs.deltaorb.listeners.InventoryListener;
import com.gorbs.deltaorb.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class DeltaOrb extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private KiDensityManager kiDensityManager;
    private OrbManager orbManager;

    @Override
    public void onEnable() {
        // Initialize managers
        configManager = new ConfigManager(this);
        databaseManager = new DatabaseManager(this);
        kiDensityManager = new KiDensityManager(this);
        orbManager = new OrbManager(this);

        // Register commands
        getCommand("densidade").setExecutor(new DensidadeCommand(this));
        getCommand("criarorb").setExecutor(new CriarOrbCommand(this));
        getCommand("editlevelorb").setExecutor(new EditLevelOrbCommand(this));
        getCommand("deletarorb").setExecutor(new DeletarOrbCommand(this));
        getCommand("giveorb").setExecutor(new GiveOrbCommand(this));
        getCommand("maxdensity").setExecutor(new MaxDensityCommand(this));
        getCommand("setmaxdensity").setExecutor(new SetMaxDensityCommand(this));
        getCommand("reload").setExecutor(new ReloadCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        // Start Ki Density task
        kiDensityManager.startDensityTask();
    }

    @Override
    public void onDisable() {
        databaseManager.closeConnection();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public KiDensityManager getKiDensityManager() {
        return kiDensityManager;
    }

    public OrbManager getOrbManager() {
        return orbManager;
    }
}