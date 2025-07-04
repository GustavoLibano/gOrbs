package com.gorbs.deltaorb.listeners;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final DeltaOrb plugin;

    public PlayerListener(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getDatabaseManager().hasPlayerDensity(player.getUniqueId().toString())) {
            plugin.getKiDensityManager().setPlayerDensity(player);
        }

        // Criar arquivo de log se não existir (mantido como no Skript, mas agora no banco)
        plugin.getDatabaseManager().ensureLogFile(player);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item == null || item.getTypeId() == 0) return;

        String itemName = item.getItemMeta().getDisplayName();
        if (itemName == null) return;

        // Verificar clique em orbs
        if (item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).contains("Orb")) {
            event.setCancelled(true);
            String levelStr = ChatColor.stripColor(item.getItemMeta().getLore().get(2)).replace("Requires LVL", "").trim();
            int level = Integer.parseInt(levelStr);
            int playerLevel = plugin.getOrbManager().getPlayerLevel(player); // Implementar no OrbManager
            if (playerLevel >= level) {
                plugin.getOrbManager().openOrbMenu(player);
            } else {
                player.sendMessage(ChatColor.RED + "Você precisa de LVL " + level + " para utilizar isso.");
            }
        }

        // Verificar clique em Densidade Máxima
        if (itemName.equals(ChatColor.translateAlternateColorCodes('&', "&bDENSIDADE MÁXIMA"))) {
            plugin.getKiDensityManager().setPlayerDensity(player, 85);
            player.sendMessage(ChatColor.GREEN + "Você usou a [" + ChatColor.AQUA + "Densidade Máxima" + ChatColor.GREEN + "].");
            item.setAmount(item.getAmount() - 1);
        }
    }
}