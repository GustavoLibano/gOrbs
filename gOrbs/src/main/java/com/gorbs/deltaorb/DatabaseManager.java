package com.gorbs.deltaorb.database;

import com.gorbs.deltaorb.DeltaOrb;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private final DeltaOrb plugin;
    private HikariDataSource dataSource;

    public DatabaseManager(DeltaOrb plugin) {
        this.plugin = plugin;
        initializeDatabase();
    }

    private void initializeDatabase() {
        FileConfiguration config = plugin.getConfig();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s",
                config.getString("database.host", "localhost"),
                config.getInt("database.port", 3306),
                config.getString("database.database", "deltaorb")));
        hikariConfig.setUsername(config.getString("database.username", "root"));
        hikariConfig.setPassword(config.getString("database.password", ""));
        hikariConfig.setMaximumPoolSize(config.getInt("database.pool-size", 10));

        dataSource = new HikariDataSource(hikariConfig);

        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS player_density (" +
                            "uuid VARCHAR(36) PRIMARY KEY," +
                            "density DOUBLE," +
                            "progressbar VARCHAR(255)," +
                            "last_updated TIMESTAMP)"
            );
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS orbs (" +
                            "name VARCHAR(255) PRIMARY KEY," +
                            "value INT," +
                            "item VARCHAR(255)," +
                            "level INT DEFAULT 1)"
            );
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS tp_logs (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "uuid VARCHAR(36)," +
                            "tp_amount DOUBLE," +
                            "orb_name VARCHAR(255)," +
                            "orb_amount INT," +
                            "orb_id BIGINT," +
                            "timestamp TIMESTAMP)"
            );
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to initialize database: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public void savePlayerDensity(String uuid, double density, String progressbar) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO player_density (uuid, density, progressbar, last_updated) VALUES (?, ?, ?, NOW()) " +
                             "ON DUPLICATE KEY UPDATE density = ?, progressbar = ?, last_updated = NOW()")) {
            stmt.setString(1, uuid);
            stmt.setDouble(2, density);
            stmt.setString(3, progressbar);
            stmt.setDouble(4, density);
            stmt.setString(5, progressbar);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save player density: " + e.getMessage());
        }
    }

    public boolean hasPlayerDensity(String uuid) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM player_density WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao verificar densidade do jogador: " + e.getMessage());
            return false;
        }
    }

    public void ensureLogFile(Player player) {
        // Não é mais necessário criar arquivos YAML, já que os logs estão no banco de dados
    }
}