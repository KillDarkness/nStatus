package me.nstatus.routes;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import static spark.Spark.*;

public class TpsRoute {
    
    private final JavaPlugin plugin;
    
    public TpsRoute(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void setup() {
        // Obter o TPS (Ticks Por Segundo) do servidor
        get("/tps", (req, res) -> {
            res.type("application/json");
            res.status(200);
            
            // O método de obter TPS pode variar dependendo da versão do servidor
            // Esta é uma abordagem comum para servidores Spigot/Paper
            double[] tps;
            
            try {
                // Tenta acessar o método TPS através de reflexão (funciona em Paper/Spigot)
                Object serverInstance = Bukkit.getServer().getClass().getMethod("getServer").invoke(Bukkit.getServer());
                tps = (double[]) serverInstance.getClass().getField("recentTps").get(serverInstance);
            } catch (Exception e) {
                // Fallback se não conseguir acessar TPS diretamente
                plugin.getLogger().warning("Não foi possível obter o TPS do servidor: " + e.getMessage());
                res.status(500);
                return "{ \"error\": \"Não foi possível obter o TPS do servidor\", \"status\": 500 }";
            }
            
            // Formata os valores de TPS (1min, 5min, 15min)
            return "{ " +
                   "\"tps_1m\": " + String.format("%.2f", Math.min(20, tps[0])) + ", " +
                   "\"tps_5m\": " + String.format("%.2f", Math.min(20, tps[1])) + ", " +
                   "\"tps_15m\": " + String.format("%.2f", Math.min(20, tps[2])) + ", " +
                   "\"status\": 200 " +
                   "}";
        });
    }
}