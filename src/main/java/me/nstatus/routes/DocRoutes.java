package me.nstatus.routes;

import org.bukkit.plugin.java.JavaPlugin;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.get;

public class DocRoutes {
    
    private final JavaPlugin plugin;
    private final Map<String, String> contentTypes;
    
    public DocRoutes(JavaPlugin plugin) {
        this.plugin = plugin;
        
        // Mapeamento de extensões para tipos de conteúdo
        this.contentTypes = new HashMap<>();
        contentTypes.put("html", "text/html");
        contentTypes.put("css", "text/css");
        contentTypes.put("js", "application/javascript");
        contentTypes.put("json", "application/json");
        contentTypes.put("png", "image/png");
        contentTypes.put("jpg", "image/jpeg");
        contentTypes.put("svg", "image/svg+xml");
    }
    
    public void setup() {
        // Rota principal da documentação
        get("/doc", serveFile("doc/index.html", "text/html"));
        
        // Rotas para arquivos estáticos
        get("/doc/*", (req, res) -> {
            String path = req.splat()[0];
            
            // Determinar o tipo de conteúdo com base na extensão do arquivo
            String contentType = determineContentType(path);
            return serveFileContent("doc/" + path, contentType, res);
        });
    }
    
    private Route serveFile(String webPath, String contentType) {
        return (req, res) -> {
            return serveFileContent(webPath, contentType, res);
        };
    }
    
    private String serveFileContent(String webPath, String contentType, Response res) {
        res.type(contentType);
        
        try {
            // Busca o arquivo dos recursos do plugin
            InputStream inputStream = plugin.getResource("web/" + webPath);
            
            if (inputStream == null) {
                res.status(404);
                return "{ \"error\": \"Arquivo não encontrado\", \"status\": 404 }";
            }
            
            // Lê o conteúdo do arquivo
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String content = reader.lines().collect(Collectors.joining("\n"));
                return content;
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Erro ao servir arquivo " + webPath + ": " + e.getMessage());
            res.status(500);
            return "{ \"error\": \"Erro interno do servidor\", \"status\": 500 }";
        }
    }
    
    private String determineContentType(String path) {
        // Extrai a extensão do arquivo
        int lastDotPos = path.lastIndexOf('.');
        if (lastDotPos >= 0) {
            String extension = path.substring(lastDotPos + 1).toLowerCase();
            if (contentTypes.containsKey(extension)) {
                return contentTypes.get(extension);
            }
        }
        
        // Tipo padrão se não encontrar correspondência
        return "text/plain";
    }
}