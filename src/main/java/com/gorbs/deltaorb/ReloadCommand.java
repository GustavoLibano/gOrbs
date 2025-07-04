package com.gorbs.deltaorb;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final DeltaOrb plugin;

    public ReloadCommand(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("admin.sk")) {
            sender.sendMessage("§cVocê não tem permissão para usar este comando.");
            return true;
        }

        plugin.getConfigManager().reloadConfig();
        plugin.getDatabaseManager().closeConnection();
        plugin.getDatabaseManager().initializeDatabase();
        sender.sendMessage("§aConfiguração e conexão com o banco de dados recarregadas com sucesso.");
        return true;
    }
}