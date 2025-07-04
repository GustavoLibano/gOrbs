package com.gorbs.deltaorb.managers;

import com.gorbs.deltaorb.DeltaOrb;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrbManager {

    private final DeltaOrb plugin;

    public OrbManager(DeltaOrb plugin) {
        this.plugin = plugin;
    }

    public boolean checkIfExists(String orbName) {
        try (Connection conn = plugin.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM orbs WHERE name = ?")) {
            stmt.setString(1, orbName);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao verificar orb: " + e.getMessage());
            return false;
        }
    }

    public void createOrb(String orbName, int value, String item) {
        try (Connection conn = plugin.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO orbs (name, value, item, level) VALUES (?, ?, ?, 1)")) {
            stmt.setString(1, orbName);
            stmt.setInt(2, value);
            stmt.setString(3, item);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao criar orb: " + e.getMessage());
        }
    }

    public void editOrbLevel(String orbName, int level) {
        try (Connection conn = plugin.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE orbs SET level = ? WHERE name = ?")) {
            stmt.setInt(1, level);
            stmt.setString(2, orbName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao editar nível da orb: " + e.getMessage());
        }
    }

    public boolean deleteOrb(String orbName) {
        try (Connection conn = plugin.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM orbs WHERE name = ?")) {
            stmt.setString(1, orbName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao deletar orb: " + e.getMessage());
            return false;
        }
    }

    public void giveOrb(Player player, String orbName) {
        try (Connection conn = plugin.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT value, item, level FROM orbs WHERE name = ?")) {
            stmt.setString(1, orbName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int value = rs.getInt("value");
                String itemId = rs.getString("item");
                int level = rs.getInt("level");

                long id = new Random().nextLong(100000000000L, 999999999999L);
                String lore = getOrbLore(player, orbName, id, value, level);
                ItemStack orb = createOrbItem(itemId, orbName, lore);
                player.getInventory().addItem(orb);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao dar orb: " + e.getMessage());
        }
    }

    private ItemStack createOrbItem(String itemId, String orbName, String lore) {
        String[] parts = itemId.split(":");
        int typeId = Integer.parseInt(parts[0]);
        short data = parts.length > 1 ? Short.parseShort(parts[1]) : 0;
        ItemStack item = new ItemStack(typeId, 1, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a" + orbName + " &7[1]"));
        meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore).split("\\|\\|")));
        item.setItemMeta(meta);
        return item;
    }

    private String getOrbLore(Player player, String orbName, long id, int value, int level) {
        List<String> bonuses = new ArrayList<>();
        double totalBoost = 0;

        // Adicionar boosts de VIP
        for (String vipBoost : plugin.getConfigManager().getVipBoosts()) {
            String[] parts = vipBoost.split(";");
            if (player.hasPermission(parts[0])) {
                bonuses.add(parts[1] + ";" + parts[2] + ";" + parts[3]);
                totalBoost += Double.parseDouble(parts[2]);
            }
        }

        // Adicionar boost de evento2x
        if (plugin.getConfigManager().isEvento2x()) {
            bonuses.add("Evento2x;100;&b");
            totalBoost += 100;
        }

        // Calcular valor final
        double finalValue = value + (value * totalBoost / 100);
        StringBuilder lore = new StringBuilder();
        lore.append("&cOrb |||| &eRequires LVL ").append(level).append("||||")
                .append("&7 ■ Training Points:&f ").append(formatNumber(finalValue)).append("||")
                .append("&7 ■ Boosts||");

        for (String bonus : bonuses) {
            String[] parts = bonus.split(";");
            String signal = Double.parseDouble(parts[1]) >= 0 ? "+" : "";
            lore.append("     ").append(parts[2]).append("➳ ").append(parts[0]).append(":&f ")
                    .append(signal).append(parts[1]).append("% Training Points||");
        }

        lore.append("&7 ■ Obtida por:&f ").append(player.getName()).append("||")
                .append("&7 ■ Id: &f").append(id).append("|||| &7&lITEM COMUM||");
        return lore.toString();
    }

    public void openOrbMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 27, ChatColor.translateAlternateColorCodes('&', "&8Utilize suas orbs"));
        List<Map<String, Object>> orbs = new ArrayList<>();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() &&
                    ChatColor.stripColor(item.getItemMeta().getLore().get(0)).contains("Orb")) {
                String name = getOrbName(item);
                int amount = getOrbAmount(item);
                double tp = getTrainingPoints(item);
                int level = getOrbLevel(item);
                orbs.add(Map.of("name", name, "tp", tp, "level", level, "amount", amount, "item", item));
            }
        }

        int slot = 10;
        for (Map<String, Object> orb : orbs) {
            if (slot > 16) break;
            String name = (String) orb.get("name");
            double tp = (double) orb.get("tp");
            int amount = (int) orb.get("amount");
            double bonus = plugin.getKiDensityManager().getPlayerDensity(player.getUniqueId().toString()) * (tp * amount) / 100;
            double totalTp = Math.round(tp * amount + bonus);
            int coins = plugin.getConfigManager().getCoinsValue() * amount;

            ItemStack item = (ItemStack) orb.get("item");
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&', " || &aClique para receber " + formatNumber(totalTp) + " de Training Points"),
                    ChatColor.translateAlternateColorCodes('&', "  &7- Training Point p/ Orb:&f " + formatNumber(tp + bonus / amount)),
                    ChatColor.translateAlternateColorCodes('&', "  &7- Coins p/ Orb:&f " + formatNumber(coins)),
                    ChatColor.translateAlternateColorCodes('&', "  &7- Qntd. Bruta:&f x" + amount)
            ));
            item.setItemMeta(meta);
            inventory.setItem(slot++, item);
        }

        player.openInventory(inventory);
    }

    public void handleOrbUse(Player player, String orbName, int slot) {
        // Implementar lógica de uso de orbs
        // Remover orbs, conceder TP e coins, logar no banco
    }

    public void clearPlayerOrbInventory(String uuid) {
        // Limpar dados temporários do inventário de orbs
    }

    public int getPlayerLevel(Player player) {
        // Implementar lógica para obter o nível do jogador
        return 1; // Placeholder
    }

    private String getOrbName(ItemStack item) {
        return ChatColor.stripColor(item.getItemMeta().getDisplayName()).split(" ")[0];
    }

    private int getOrbAmount(ItemStack item) {
        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        String amountStr = name.substring(name.indexOf("[") + 1, name.indexOf("]"));
        return Integer.parseInt(amountStr.replace(".", ""));
    }

    private double getTrainingPoints(ItemStack item) {
        String tpLine = ChatColor.stripColor(item.getItemMeta().getLore().get(2));
        return Double.parseDouble(tpLine.replace("Training Points:", "").replace("■", "").trim());
    }

    private int getOrbLevel(ItemStack item) {
        String levelLine = ChatColor.stripColor(item.getItemMeta().getLore().get(1));
        return Integer.parseInt(levelLine.replace("Requires LVL", "").trim());
    }

    private String formatNumber(double number) {
        return String.format("%,.0f", number);
    }
}