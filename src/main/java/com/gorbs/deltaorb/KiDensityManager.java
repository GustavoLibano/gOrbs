package com.gorbs.deltaorb.managers;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class KiDensityManager {

    private final DeltaOrb plugin;
    private final Random random = new Random();
    private int densityTime;
    private int densityLevel;

    public KiDensityManager(DeltaOrb plugin) {
        this.plugin = plugin;
        this.densityTime = 0;
        this.densityLevel = 0;
    }

    public void startDensityTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                densityTime++;
                if (densityTime >= plugin.getConfigManager().getKiDensityTime()) {
                    resetDensities();
                    densityTime = 0;
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public int getDensityTime() {
        return densityTime;
    }

    public int getDensityLevel() {
        return densityLevel;
    }

    public double getDensity(Player player) {
        int min = plugin.getConfigManager().getKiDensityMin();
        int max = plugin.getConfigManager().getKiDensityMax();
        int onlinePlayers = 0;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("lb.block")) {
                onlinePlayers++;
            }
        }

        densityLevel = 0;
        if (hasVipPermission(player)) {
            max = 75;
        }

        if (onlinePlayers >= 5 && onlinePlayers <= 9) {
            min = -25;
            densityLevel = 1;
        } else if (onlinePlayers >= 10 && onlinePlayers <= 14) {
            min = 0;
            densityLevel = 2;
        } else if (onlinePlayers >= 15 && onlinePlayers <= 19) {
            min = 15;
            max = hasVipPermission(player) ? 85 : 60;
            densityLevel = 3;
        } else if (onlinePlayers >= 20 && onlinePlayers <= 29) {
            min = 25;
            max = hasVipPermission(player) ? 100 : 75;
            densityLevel = 4;
        } else if (onlinePlayers >= 30 && onlinePlayers <= 39) {
            min = 35;
            max = hasVipPermission(player) ? 120 : 85;
            densityLevel = 5;
        } else if (onlinePlayers >= 40 && onlinePlayers <= 59) {
            min = 40;
            max = hasVipPermission(player) ? 130 : 95;
            densityLevel = 6;
        } else if (onlinePlayers >= 60 && onlinePlayers <= 80) {
            min = 50;
            max = hasVipPermission(player) ? 145 : 105;
            densityLevel = 6;
        }

        double density = random.nextInt(max - min + 1) + min;
        if (plugin.getConfig().getBoolean("maxdensity", false)) {
            density = max;
        }

        return Math.round(density);
    }

    public void setPlayerDensity(Player player) {
        double density = getDensity(player);
        String progressbar = getProgressbar(density);
        plugin.getDatabaseManager().savePlayerDensity(player.getUniqueId().toString(), density, progressbar);
    }

    public void setPlayerDensity(Player player, double density) {
        String progressbar = getProgressbar(density);
        plugin.getDatabaseManager().savePlayerDensity(player.getUniqueId().toString(), density, progressbar);
    }

    public double getPlayerDensity(String uuid) {
        try (Connection conn = plugin.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT density FROM player_density WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("density");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao obter densidade: " + e.getMessage());
        }
        return 0;
    }

    public void resetDensities() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setPlayerDensity(player);
        }
        announceDensities();
    }

    private String getProgressbar(double density) {
        String color = density >= 50 ? "&e" : density > 0 ? "&a" : density < 0 ? "&c" : "&7";
        int absValue = (int) Math.abs(density) / 10;
        StringBuilder progress = new StringBuilder();
        for (int i = 0; i < absValue && i < 5; i++) {
            progress.append(color).append("■");
        }
        for (int i = absValue; i < 5; i++) {
            progress.append("&7■");
        }
        return progress.toString();
    }

    private void announceDensities() {
        double totalDensity = 0;
        int validPlayers = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("lb.block")) {
                totalDensity += getPlayerDensity(player.getUniqueId().toString());
                validPlayers++;
            }
        }

        String highest = getDensityRank(1);
        String lowest = getDensityRank(validPlayers);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b&lDENSIDADE DE KI"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&fA densidade total é de &b" + totalDensity + "%."));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&fMaior Densidade: &a" + highest));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&fMenor Densidade: &c" + lowest));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&fLevel da Densidade: &e" + densityLevel));
    }

    private String getDensityRank(int rank) {
        // Implementar ranking de densidade
        return "Ninguém;0";
    }

    private boolean hasVipPermission(Player player) {
        return player.hasPermission("vip.sk") || player.hasPermission("vip+.sk") ||
                player.hasPermission("mvp.sk") || player.hasPermission("mvp+.sk") ||
                player.hasPermission("delta.sk");
    }
}