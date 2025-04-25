package me.nstatus;

import me.nstatus.routes.Routes;
import org.bukkit.plugin.java.JavaPlugin;

import static spark.Spark.*;

public class NStatus extends JavaPlugin {

    // ANSI azul
    private static final String AZUL = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (!getConfig().contains("http-port") || getConfig().getString("http-port").isEmpty()) {
            getLogger().severe("Porta HTTP não configurada no config.yml!");
            getLogger().severe("O plugin nStatus continuará funcionando, mas o servidor HTTP não será iniciado.");
            return;
        }

        try {
            int httpPort = getConfig().getInt("http-port");
            port(httpPort);

            Routes routes = new Routes(this);
            routes.setupRoutes();

            // Prints em azul com prefixo customizado
            System.out.println(AZUL + "[nStatus] nStatus 1.21 iniciado com sucesso!" + RESET);
            System.out.println(AZUL + "[nStatus] Documentação do plugin: http://[IP]:" + httpPort + "/doc" + RESET);

        } catch (Exception e) {
            getLogger().severe("Erro ao iniciar o servidor HTTP: " + e.getMessage());
            getLogger().severe("O plugin nStatus continuará funcionando, mas o servidor HTTP não será iniciado.");
        }
    }

    @Override
    public void onDisable() {
        try {
            stop();
            getLogger().info("Servidor HTTP do nStatus encerrado.");
        } catch (Exception e) {
            getLogger().info("O servidor HTTP já estava parado ou ocorreu um erro ao encerrá-lo.");
        }
    }
}
