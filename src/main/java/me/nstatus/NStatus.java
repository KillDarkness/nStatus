package me.nstatus;

import me.nstatus.routes.Routes;
import org.bukkit.plugin.java.JavaPlugin;
import static spark.Spark.*;

public class NStatus extends JavaPlugin {

    @Override
    public void onEnable() {
        // Salva a configuração padrão se não existir
        saveDefaultConfig();
        
        // Verifica se a porta está configurada
        if (!getConfig().contains("http-port") || getConfig().getString("http-port").isEmpty()) {
            getLogger().severe("Porta HTTP não configurada no config.yml!");
            getLogger().severe("O plugin nStatus continuará funcionando, mas o servidor HTTP não será iniciado.");
            return;
        }
        
        try {
            // Obtém a porta do arquivo de configuração
            int httpPort = getConfig().getInt("http-port");
            
            // Configura a porta HTTP
            port(httpPort);
            
            // Inicializa as rotas
            Routes routes = new Routes(this);
            routes.setupRoutes();
            
            getLogger().info("nStatus 1.21 rodando em http://[IP]:" + httpPort);
        } catch (Exception e) {
            getLogger().severe("Erro ao iniciar o servidor HTTP: " + e.getMessage());
            getLogger().severe("O plugin nStatus continuará funcionando, mas o servidor HTTP não será iniciado.");
        }
    }

    @Override
    public void onDisable() {
        try {
            stop(); // Encerra o servidor HTTP
            getLogger().info("Servidor HTTP do nStatus encerrado.");
        } catch (Exception e) {
            getLogger().info("O servidor HTTP já estava parado ou ocorreu um erro ao encerrá-lo.");
        }
    }
}