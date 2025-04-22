package me.nstatus.routes;

import org.bukkit.plugin.java.JavaPlugin;

public class Routes {
    
    private final JavaPlugin plugin;
    
    public Routes(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void setupRoutes() {
        // Configura a rota principal
        new MainRoute().setup();
        
        // Configura as rotas de status
        new StatusRoutes(plugin).setup();
        
        // Configura as rotas de listagem de jogadores
        new ListRoutes(plugin).setup();
    }
}