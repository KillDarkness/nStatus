package me.nstatus.routes;

import static spark.Spark.*;

public class MainRoute {
    
    public void setup() {
        // Rota principal
        get("/", (req, res) -> {
            return "nStatus API 1.21 - Endpoints disponíveis: /status, /status/:player, /list";
        });
    }
}