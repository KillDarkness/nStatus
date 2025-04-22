package me.nstatus.routes;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import static spark.Spark.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WhitelistRoutes {
    
    private final JavaPlugin plugin;
    
    public WhitelistRoutes(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void setup() {
        // Obter a whitelist completa
        get("/whitelist/get", (req, res) -> {
            res.type("application/json");
            res.status(200);
            
            Set<OfflinePlayer> whitelistedPlayers = Bukkit.getWhitelistedPlayers();
            StringBuilder json = new StringBuilder();
            
            json.append("{ \"count\": ").append(whitelistedPlayers.size()).append(", ");
            json.append("\"enabled\": ").append(Bukkit.hasWhitelist()).append(", ");
            json.append("\"players\": [");
            
            List<String> playerEntries = new ArrayList<>();
            
            for (OfflinePlayer player : whitelistedPlayers) {
                playerEntries.add("{ " +
                    "\"name\": \"" + player.getName() + "\", " +
                    "\"uuid\": \"" + player.getUniqueId() + "\", " +
                    "\"last_played\": " + player.getLastPlayed() +
                "}");
            }
            
            json.append(String.join(", ", playerEntries));
            json.append("], ");
            json.append("\"status\": 200 }");
            
            return json.toString();
        });
        
        // Adicionar um jogador à whitelist
        post("/whitelist/add/:player", (req, res) -> {
            String playerName = req.params(":player");
            
            if (playerName == null || playerName.isEmpty()) {
                res.status(400);
                return "{ \"error\": \"Nome do jogador não especificado\", \"status\": 400 }";
            }
            
            // Executa o comando na thread principal e aguarda o resultado
            CompletableFuture<Boolean> future = new CompletableFuture<>();
            
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                try {
                    // Executa o comando na thread principal
                    boolean success = plugin.getServer().dispatchCommand(
                        Bukkit.getConsoleSender(), "whitelist add " + playerName);
                    future.complete(success);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            
            try {
                boolean success = future.get(); // Aguarda conclusão
                
                if (success) {
                    res.status(200);
                    return "{ \"message\": \"" + playerName + " adicionado à whitelist\", \"status\": 200 }";
                } else {
                    res.status(500);
                    return "{ \"error\": \"Falha ao adicionar " + playerName + " à whitelist\", \"status\": 500 }";
                }
            } catch (InterruptedException | ExecutionException e) {
                res.status(500);
                return "{ \"error\": \"" + e.getMessage() + "\", \"status\": 500 }";
            }
        });
        
        // Remover um jogador da whitelist
        delete("/whitelist/remove/:player", (req, res) -> {
            String playerName = req.params(":player");
            
            if (playerName == null || playerName.isEmpty()) {
                res.status(400);
                return "{ \"error\": \"Nome do jogador não especificado\", \"status\": 400 }";
            }
            
            // Executa o comando na thread principal e aguarda o resultado
            CompletableFuture<Boolean> future = new CompletableFuture<>();
            
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                try {
                    // Executa o comando na thread principal
                    boolean success = plugin.getServer().dispatchCommand(
                        Bukkit.getConsoleSender(), "whitelist remove " + playerName);
                    future.complete(success);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            
            try {
                boolean success = future.get(); // Aguarda conclusão
                
                if (success) {
                    res.status(200);
                    return "{ \"message\": \"" + playerName + " removido da whitelist\", \"status\": 200 }";
                } else {
                    res.status(500);
                    return "{ \"error\": \"Falha ao remover " + playerName + " da whitelist\", \"status\": 500 }";
                }
            } catch (InterruptedException | ExecutionException e) {
                res.status(500);
                return "{ \"error\": \"" + e.getMessage() + "\", \"status\": 500 }";
            }
        });
        
        // Ativar a whitelist
        post("/whitelist/on", (req, res) -> {
            res.type("application/json");
            
            // Executa o comando na thread principal e aguarda o resultado
            CompletableFuture<Boolean> future = new CompletableFuture<>();
            
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                try {
                    // Executa o comando na thread principal
                    boolean success = plugin.getServer().dispatchCommand(
                        Bukkit.getConsoleSender(), "whitelist on");
                    future.complete(success);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            
            try {
                boolean success = future.get(); // Aguarda conclusão
                
                if (success) {
                    res.status(200);
                    return "{ \"message\": \"Whitelist ativada com sucesso\", \"status\": 200 }";
                } else {
                    res.status(500);
                    return "{ \"error\": \"Falha ao ativar a whitelist\", \"status\": 500 }";
                }
            } catch (InterruptedException | ExecutionException e) {
                res.status(500);
                return "{ \"error\": \"" + e.getMessage() + "\", \"status\": 500 }";
            }
        });
        
        // Desativar a whitelist
        post("/whitelist/off", (req, res) -> {
            res.type("application/json");
            
            // Executa o comando na thread principal e aguarda o resultado
            CompletableFuture<Boolean> future = new CompletableFuture<>();
            
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                try {
                    // Executa o comando na thread principal
                    boolean success = plugin.getServer().dispatchCommand(
                        Bukkit.getConsoleSender(), "whitelist off");
                    future.complete(success);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            
            try {
                boolean success = future.get(); // Aguarda conclusão
                
                if (success) {
                    res.status(200);
                    return "{ \"message\": \"Whitelist desativada com sucesso\", \"status\": 200 }";
                } else {
                    res.status(500);
                    return "{ \"error\": \"Falha ao desativar a whitelist\", \"status\": 500 }";
                }
            } catch (InterruptedException | ExecutionException e) {
                res.status(500);
                return "{ \"error\": \"" + e.getMessage() + "\", \"status\": 500 }";
            }
        });
    }
}