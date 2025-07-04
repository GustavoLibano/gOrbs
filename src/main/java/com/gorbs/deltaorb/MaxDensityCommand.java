package com.gorbs.deltaorb.commands;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MaxDensityCommand implements CommandExecutor {

    private final DeltaOrb plugin;

    public MaxDensityCommand(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("admin.sk")) {
            sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Syntaxe incorreta. Use: /maxdensity <PLAYER>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Nenhum jogador com esse nome foi encontrado.");
            return true;
        }

        ItemStack maxDensityItem = new ItemStack(4453);
        ItemMeta meta = maxDensityItem.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bDENSIDADE MÁXIMA"));
        meta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', "&f|| &7Um item que pode ser obtido no site,"),
                ChatColor.translateAlternateColorCodes('&', "&7ou como recompensa de tempo."),
                ChatColor.translateAlternateColorCodes('&', "&aDensidade vai se transformar em 85%%.")
        ));
        maxDensityItem.setItemMeta(meta);
        target.getInventory().addItem(maxDensityItem);
        return true;
    }
}