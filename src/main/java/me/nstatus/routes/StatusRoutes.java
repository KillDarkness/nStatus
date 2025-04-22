package me.nstatus.routes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import static spark.Spark.*;

public class StatusRoutes {
    
    private final JavaPlugin plugin;
    
    public StatusRoutes(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void setup() {
        // Quantidade de jogadores online
        get("/status", (req, res) -> {
            res.type("application/json");
            return "{ \"online\": " + Bukkit.getOnlinePlayers().size() + ", \"max\": " + Bukkit.getMaxPlayers() + " }";
        });

        // Status de um jogador
        get("/status/:player", (req, res) -> {
            String playerName = req.params(":player");
            Player player = Bukkit.getPlayer(playerName);
            
            if (player == null) {
                res.status(404);
                return "{ \"error\": \"Jogador não encontrado ou offline\" }";
            }

            return "{ " +
                "\"player\": \"" + player.getName() + "\", " +
                "\"online\": true, " +
                "\"health\": " + player.getHealth() + ", " +
                "\"world\": \"" + player.getWorld().getName() + "\"" +
            " }";
        });
    }
}