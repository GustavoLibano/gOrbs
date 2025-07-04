package com.gorbs.deltaorb.commands;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetMaxDensityCommand implements CommandExecutor {

    private final DeltaOrb plugin;

    public SetMaxDensityCommand(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("admin.sk")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        boolean maxDensity = plugin.getConfig().getBoolean("maxdensity", false);
        if (!maxDensity) {
            plugin.getConfig().set("maxdensity", true);
            plugin.saveConfig();
            plugin.getKiDensityManager().resetDensities();
            Bukkit.broadcastMessage(ChatColor.GREEN + "Evento Densidade Máxima está rolando.");
        } else {
            plugin.getConfig().set("maxdensity", null);
            plugin.saveConfig();
            Bukkit.broadcastMessage(ChatColor.GREEN + "Evento Densidade Máxima foi desativado.");
        }
        return true;
    }
}