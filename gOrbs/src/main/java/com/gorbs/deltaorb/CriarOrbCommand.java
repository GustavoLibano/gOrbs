package com.gorbs.deltaorb.commands;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CriarOrbCommand implements CommandExecutor {

    private final DeltaOrb plugin;

    public CriarOrbCommand(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("admin.sk")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Syntaxe incorreta. Use: /criarorb <ORB_NAME> <TP_VALUE>");
            return true;
        }

        String orbName = args[0];
        int value;
        try {
            value = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "O valor de TP deve ser um número.");
            return true;
        }

        ItemStack tool = player.getItemInHand();
        if (tool == null || tool.getTypeId() == 0) {
            sender.sendMessage(ChatColor.RED + "Você não está segurando nenhum item.");
            return true;
        }

        if (plugin.getOrbManager().checkIfExists(orbName)) {
            sender.sendMessage(ChatColor.RED + "Essa orb já existe.");
            return true;
        }

        String itemId = tool.getTypeId() + ":" + tool.getData().getData();
        plugin.getOrbManager().createOrb(orbName, value, itemId);
        sender.sendMessage(ChatColor.GREEN + "Orb criada com sucesso.");
        return true;
    }
}