package com.gorbs.deltaorb.commands;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeletarOrbCommand implements CommandExecutor {

    private final DeltaOrb plugin;

    public DeletarOrbCommand(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("admin.sk")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Syntaxe incorreta. Use: /deletarorb <ORB_NAME>");
            return true;
        }

        String orbName = args[0];
        if (!plugin.getOrbManager().checkIfExists(orbName)) {
            sender.sendMessage(ChatColor.RED + "Nenhuma orb com esse nome foi encontrada.");
            return true;
        }

        if (plugin.getOrbManager().deleteOrb(orbName)) {
            sender.sendMessage(ChatColor.GREEN + "Orb deletada com sucesso.");
        } else {
            sender.sendMessage(ChatColor.RED + "Não foi possível deletar essa orb.");
        }
        return true;
    }
}