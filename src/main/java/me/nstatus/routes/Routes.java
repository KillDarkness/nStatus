package me.nstatus.routes;

import me.nstatus.auth.AuthHandler;
import org.bukkit.plugin.java.JavaPlugin;
import static spark.Spark.*;

public class Routes {
    
    private final JavaPlugin plugin;
    private final AuthHandler authHandler;
    
    public Routes(JavaPlugin plugin) {
        this.plugin = plugin;
        this.authHandler = new AuthHandler(plugin);
    }
    
    public void setupRoutes() {
        // Configurar tratamento de erros
        setupErrorHandlers();
        
        // Aplicar filtro de autenticação, se habilitado
        if (authHandler.isAuthEnabled()) {
            plugin.getLogger().info("Autenticação por token ativada. Todas as requisições precisarão de um token.");
            before("*", authHandler.getAuthFilter());
        } else {
            plugin.getLogger().info("Autenticação por token desativada. Todas as requisições serão permitidas.");
        }
        
        // Configura as rotas
        new MainRoute().setup();
        new StatusRoutes(plugin).setup();
        new ListRoutes(plugin).setup();
        
        // Novas rotas adicionadas
        new ListBanRoutes(plugin).setup();
        new WhitelistRoutes(plugin).setup();
        new MotdRoute(plugin).setup();
        new TpsRoute(plugin).setup();
    }
    
    private void setupErrorHandlers() {
        // Tratamento personalizado para rotas não encontradas (404)
        notFound((req, res) -> {
            res.type("application/json");
            res.status(404);
            return "{ \"error\": \"Rota não encontrada\", \"status\": 404 }";
        });
        
        // Tratamento para erros internos (500)
        internalServerError((req, res) -> {
            res.type("application/json");
            res.status(500);
            return "{ \"error\": \"Erro interno do servidor\", \"status\": 500 }";
        });
        
        // Para erros de validação de requisição (400)
        exception(Exception.class, (exception, req, res) -> {
            res.type("application/json");
            res.status(400);
            res.body("{ \"error\": \"" + exception.getMessage() + "\", \"status\": 400 }");
        });
    }
}