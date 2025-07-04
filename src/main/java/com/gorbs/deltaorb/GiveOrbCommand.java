package com.gorbs.deltaorb.commands;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveOrbCommand implements CommandExecutor {

    private final DeltaOrb plugin;

    public GiveOrbCommand(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("admin.sk")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Syntaxe incorreta. Use: /giveorb <PLAYER> <ORB_NAME> [QUANTIDADE]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Nenhum jogador com esse nome foi encontrado.");
            return true;
        }

        String orbName = args[1];
        if (!plugin.getOrbManager().checkIfExists(orbName)) {
            sender.sendMessage(ChatColor.RED + "Nenhuma orb com esse nome foi encontrada.");
            return true;
        }

        int amount = 1;
        if (args.length > 2) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "A quantidade deve ser um número.");
                return true;
            }
        }

        for (int i = 0; i < amount; i++) {
            plugin.getOrbManager().giveOrb(target, orbName);
        }
        sender.sendMessage(ChatColor.GREEN + "Você deu " + amount + " orb(s) para o jogador " + target.getName() + ".");
        return true;
    }
}