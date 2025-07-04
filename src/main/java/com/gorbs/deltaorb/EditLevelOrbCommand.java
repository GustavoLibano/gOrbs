package com.gorbs.deltaorb.commands;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EditLevelOrbCommand implements CommandExecutor {

    private final DeltaOrb plugin;

    public EditLevelOrbCommand(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("admin.sk")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Comando correto: /editlevelorb <ORB_NAME> <NEW_LEVEL>");
            return true;
        }

        String orbName = args[0];
        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "O nível deve ser um número.");
            return true;
        }

        if (!plugin.getOrbManager().checkIfExists(orbName)) {
            sender.sendMessage(ChatColor.RED + "Essa orb não foi encontrada.");
            return true;
        }

        plugin.getOrbManager().editOrbLevel(orbName, level);
        sender.sendMessage(ChatColor.GREEN + "Novo level da orb '" + orbName + "' agora é " + level);
        return true;
    }
}