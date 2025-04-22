package me.nstatus.auth;

import org.bukkit.plugin.java.JavaPlugin;
import spark.Filter;
import spark.Request;
import spark.Response;
import static spark.Spark.halt;  // Adicionando o import correto para halt()

public class AuthHandler {
    
    private final JavaPlugin plugin;
    private final String apiToken;
    
    public AuthHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.apiToken = plugin.getConfig().getString("api-token");
    }
    
    public boolean isAuthEnabled() {
        return apiToken != null && !apiToken.isEmpty();
    }
    
    public Filter getAuthFilter() {
        return (Request request, Response response) -> {
            // Se a autenticação está ativada
            if (isAuthEnabled()) {
                String requestToken = request.queryParams("token");
                
                // Se o token não foi fornecido ou está incorreto
                if (requestToken == null || !requestToken.equals(apiToken)) {
                    response.status(401); // Unauthorized
                    response.body("{ \"error\": \"Token de autenticação inválido ou ausente\", \"status\": 401 }");
                    request.attribute("auth-failed", true);
                    response.type("application/json");
                    response.header("Content-Type", "application/json");
                    halt(401, "{ \"error\": \"Token de autenticação inválido ou ausente\", \"status\": 401 }");
                }
            }
        };
    }
    
    public boolean isAuthenticated(Request request) {
        return !Boolean.TRUE.equals(request.attribute("auth-failed"));
    }
}