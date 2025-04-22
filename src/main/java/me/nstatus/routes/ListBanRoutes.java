package me.nstatus.routes;

import org.bukkit.Bukkit;
import org.bukkit.BanList;
import org.bukkit.plugin.java.JavaPlugin;
import static spark.Spark.*;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class ListBanRoutes {
    
    private final JavaPlugin plugin;
    
    public ListBanRoutes(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void setup() {
        // Lista todos os jogadores banidos
        get("/listban", (req, res) -> {
            res.type("application/json");
            res.status(200);
            
            Set<org.bukkit.BanEntry> bannedPlayers = Bukkit.getBanList(BanList.Type.NAME).getBanEntries();
            StringBuilder json = new StringBuilder();
            
            json.append("{ \"count\": ").append(bannedPlayers.size()).append(", ");
            json.append("\"banned_players\": [");
            
            List<String> banEntries = new ArrayList<>();
            
            for (org.bukkit.BanEntry ban : bannedPlayers) {
                banEntries.add("{ " +
                    "\"name\": \"" + ban.getTarget() + "\", " +
                    "\"reason\": \"" + (ban.getReason() != null ? ban.getReason().replace("\"", "\\\"") : "") + "\", " +
                    "\"source\": \"" + (ban.getSource() != null ? ban.getSource().replace("\"", "\\\"") : "") + "\", " + 
                    "\"created\": \"" + ban.getCreated() + "\", " +
                    "\"expiration\": \"" + (ban.getExpiration() != null ? ban.getExpiration() : "never") + "\" " +
                "}");
            }
            
            json.append(String.join(", ", banEntries));
            json.append("], ");
            json.append("\"status\": 200 }");
            
            return json.toString();
        });
    }
}