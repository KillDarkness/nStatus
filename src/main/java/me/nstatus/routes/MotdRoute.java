package me.nstatus.routes;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import static spark.Spark.*;

public class MotdRoute {
    
    private final JavaPlugin plugin;
    
    public MotdRoute(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void setup() {
        // Obter o MOTD (Message of the Day) do servidor
        get("/motd", (req, res) -> {
            res.type("application/json");
            res.status(200);
            
            String motd = Bukkit.getMotd();
            
            // Escapando aspas duplas para formatação JSON adequada
            motd = motd.replace("\"", "\\\"");
            
            return "{ \"motd\": \"" + motd + "\", \"status\": 200 }";
        });
        
        // Atualizar o MOTD (Message of the Day) do servidor
        post("/motd", (req, res) -> {
            String newMotd = req.queryParams("message");
            
            if (newMotd == null || newMotd.isEmpty()) {
                res.status(400);
                return "{ \"error\": \"Nova mensagem MOTD não especificada. Use o parâmetro 'message'.\", \"status\": 400 }";
            }
            
            // Atualiza o MOTD do servidor
            Bukkit.setMotd(newMotd);
            
            res.status(200);
            return "{ \"message\": \"MOTD atualizado com sucesso\", \"motd\": \"" + newMotd.replace("\"", "\\\"") + "\", \"status\": 200 }";
        });
    }
}