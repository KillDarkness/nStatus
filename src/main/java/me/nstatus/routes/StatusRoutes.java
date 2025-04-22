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
            res.status(200);
            return "{ \"online\": " + Bukkit.getOnlinePlayers().size() + 
                   ", \"max\": " + Bukkit.getMaxPlayers() + 
                   ", \"status\": 200 }";
        });

        // Status de um jogador
        get("/status/:player", (req, res) -> {
            String playerName = req.params(":player");
            
            if (playerName == null || playerName.isEmpty()) {
                res.status(400);
                return "{ \"error\": \"Nome do jogador não especificado\", \"status\": 400 }";
            }
            
            Player player = Bukkit.getPlayer(playerName);
            
            if (player == null) {
                res.status(404);
                return "{ \"error\": \"Jogador não encontrado ou offline\", \"status\": 404 }";
            }

            res.status(200);
            return "{ " +
                "\"player\": \"" + player.getName() + "\", " +
                "\"online\": true, " +
                "\"health\": " + player.getHealth() + ", " +
                "\"world\": \"" + player.getWorld().getName() + "\", " +
                "\"status\": 200" +
            " }";
        });
    }
}