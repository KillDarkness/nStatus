package me.nstatus.routes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import static spark.Spark.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class ListRoutes {
    
    private final JavaPlugin plugin;
    
    public ListRoutes(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void setup() {
        // Lista todos os jogadores online
        get("/list", (req, res) -> {
            res.type("application/json");
            res.status(200);
            
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            StringBuilder json = new StringBuilder();
            
            json.append("{ \"count\": ").append(onlinePlayers.size()).append(", ");
            json.append("\"players\": [");
            
            List<String> playerEntries = new ArrayList<>();
            
            for (Player player : onlinePlayers) {
                playerEntries.add("{ " +
                    "\"name\": \"" + player.getName() + "\", " +
                    "\"health\": " + player.getHealth() + ", " +
                    "\"level\": " + player.getLevel() + ", " + 
                    "\"world\": \"" + player.getWorld().getName() + "\" " +
                "}");
            }
            
            json.append(String.join(", ", playerEntries));
            json.append("], ");
            json.append("\"status\": 200 }");
            
            return json.toString();
        });
    }
}