package me.nstatus.routes;

import static spark.Spark.*;

public class MainRoute {
    
    public void setup() {
        // Rota principal
        get("/", (req, res) -> {
            res.status(200);
            res.type("application/json");
            return "{ \"message\": \"nStatus API 1.21 - Endpoints disponíveis: /status, /status/:player, /list\", \"status\": 200 }";
        });
    }
}