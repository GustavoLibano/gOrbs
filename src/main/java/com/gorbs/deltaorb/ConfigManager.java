package com.gorbs.deltaorb;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private final DeltaOrb plugin;
    private FileConfiguration config;

    public ConfigManager(DeltaOrb plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        reloadConfig();
    }

    public void reloadConfig() {
        config = plugin.getConfig();
    }

    public int getCoinsValue() {
        return config.getInt("coins_value", 15);
    }

    public int getKiDensityMin() {
        return config.getInt("kidensity.min", -50);
    }

    public int getKiDensityMax() {
        return config.getInt("kidensity.max", 50);
    }

    public int getKiDensityTime() {
        return config.getInt("kidensity.time", 600);
    }

    public boolean isEvento2x() {
        return config.getBoolean("evento2x", true);
    }

    public List<String> getVipBoosts() {
        return config.getStringList("vip_boosts");
    }

    public List<String> getRandomOrbs() {
        return config.getStringList("random_orbs");
    }
}