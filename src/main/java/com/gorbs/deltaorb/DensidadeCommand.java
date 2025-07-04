package com.gorbs.deltaorb;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class DensidadeCommand implements CommandExecutor {

    private final DeltaOrb plugin;

    public DensidadeCommand(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;
        openDensityMenu(player);
        return true;
    }

    private void openDensityMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 27, ChatColor.translateAlternateColorCodes('&', "&8Densidade - Informações"));

        // Item de ajuda
        ItemStack helpItem = new ItemStack(340); // Livro
        ItemMeta helpMeta = helpItem.getItemMeta();
        helpMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eAjuda"));
        helpMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', "&f|| &7O que é a Densidade de KI?"),
                ChatColor.translateAlternateColorCodes('&', "&aA densidade de KI pode tanto beneficiar,"),
                ChatColor.translateAlternateColorCodes('&', "&aquanto prejudicar seu FARM. O TP da sua orb"),
                ChatColor.translateAlternateColorCodes('&', "&avai ser multiplicada dependendo dele."),
                ChatColor.translateAlternateColorCodes('&', "&7Por exemplo, minha densidade de KI é 25%%."),
                ChatColor.translateAlternateColorCodes('&', "&aO TP vai ser aumentado em 25%%. Caso seja negativo,"),
                ChatColor.translateAlternateColorCodes('&', "&aele vai diminuir em -25%%."),
                ChatColor.translateAlternateColorCodes('&', "&7Ele troca automaticamente a cada 10 minutos.")
        ));
        helpItem.setItemMeta(helpMeta);
        inventory.setItem(11, helpItem);

        // Item de tempo
        long remainingSeconds = plugin.getConfigManager().getKiDensityTime() - plugin.getKiDensityManager().getDensityTime();
        String remaining = formatTime(remainingSeconds);
        ItemStack timeItem = new ItemStack(347); // Relógio
        ItemMeta timeMeta = timeItem.getItemMeta();
        timeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aTempo"));
        timeMeta.setLore(Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', "&f|| &aO tempo para a mudança da densidade"),
                ChatColor.translateAlternateColorCodes('&', "&ade KI é de '" + remaining + "'."),
                ChatColor.translateAlternateColorCodes('&', "&f|| &8▪ &7Level da Densidade: &f" + plugin.getKiDensityManager().getDensityLevel())
        ));
        timeItem.setItemMeta(timeMeta);
        inventory.setItem(15, timeItem);

        player.openInventory(inventory);

        // Atualizar o item de tempo após 1 segundo
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&8Densidade - Informações"))) {
                long updatedSeconds = plugin.getConfigManager().getKiDensityTime() - plugin.getKiDensityManager().getDensityTime();
                String updatedRemaining = formatTime(updatedSeconds);
                timeMeta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&', "&f|| &aO tempo para a mudança da densidade"),
                        ChatColor.translateAlternateColorCodes('&', "&ade KI é de '" + updatedRemaining + "'."),
                        ChatColor.translateAlternateColorCodes('&', "&f|| &8▪ &7Level da Densidade: &f" + plugin.getKiDensityManager().getDensityLevel())
                ));
                timeItem.setItemMeta(timeMeta);
                inventory.setItem(15, timeItem);
            }
        }, 20L);
    }

    private String formatTime(long seconds) {
        long minutes = TimeUnit.SECONDS.toMinutes(seconds);
        long remainingSeconds = seconds % 60;
        StringBuilder result = new StringBuilder();
        if (minutes > 0) {
            result.append(minutes).append(" minuto").append(minutes > 1 ? "s" : "");
        }
        if (remainingSeconds > 0) {
            if (minutes > 0) result.append(" e ");
            result.append(remainingSeconds).append(" segundo").append(remainingSeconds > 1 ? "s" : "");
        }
        return result.toString();
    }
}