package com.gorbs.deltaorb.listeners;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    private final DeltaOrb plugin;

    public InventoryListener(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryTitle = ChatColor.stripColor(event.getInventory().getTitle());

        if (inventoryTitle.equals("Densidade - Informações")) {
            event.setCancelled(true);
            return;
        }

        if (inventoryTitle.equals("Utilize suas orbs")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            String orbName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).trim();
            // Implementar lógica de troca de orbs, similar ao Skript
            plugin.getOrbManager().handleOrbUse(player, orbName, event.getSlot());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (ChatColor.stripColor(event.getInventory().getTitle()).equals("Utilize suas orbs")) {
            plugin.getOrbManager().clearPlayerOrbInventory(event.getPlayer().getUniqueId().toString());
        }
    }
}