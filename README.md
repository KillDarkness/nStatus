# nStatus - API de Status para Minecraft 1.21

Uma API HTTP leve e fácil de usar para obter informações sobre seu servidor Minecraft e jogadores em tempo real.

## Características

- 🚀 API HTTP simples e rápida
- 📊 Informações sobre o servidor e jogadores
- 🔒 Sistema de autenticação por token opcional
- ⚙️ Configurável via config.yml
- 📱 Integração fácil com websites, bots e aplicativos

## Instalação

1. Baixe o arquivo JAR da [seção de releases](https://github.com/YourUsername/nStatus/releases)
2. Coloque o arquivo em sua pasta `plugins` do servidor
3. Reinicie o servidor ou use `/reload`
4. Configure o arquivo `config.yml` que será gerado automaticamente

## Configuração

O arquivo `config.yml` contém as seguintes opções:

```yaml
# Porta HTTP para o servidor da API
http-port: 25641

# Token de autenticação para a API
# Deixe em branco para desativar a autenticação
api-token: 
```

* `http-port`: Porta em que o servidor HTTP irá rodar (padrão: 25641)
* `api-token`: Token de segurança para proteger sua API. Se definido, todas as requisições precisarão incluir este token.

## Endpoints da API

### Rota Principal
**Endpoint:** `/`  
**Método:** GET  
**Descrição:** Informações básicas sobre a API  
**Exemplo:**
```
http://localhost:25641/
```
**Resposta:**
```json
{
  "message": "nStatus API 1.21 - Endpoints disponíveis: /status, /status/:player, /list",
  "status": 200
}
```

### Status do Servidor
**Endpoint:** `/status`  
**Método:** GET  
**Descrição:** Retorna o número de jogadores online e o máximo permitido  
**Exemplo:**
```
http://localhost:25641/status
```
**Resposta:**
```json
{
  "online": 5,
  "max": 20,
  "status": 200
}
```

### Status de um Jogador
**Endpoint:** `/status/:player`  
**Método:** GET  
**Descrição:** Retorna informações detalhadas sobre um jogador específico  
**Exemplo:**
```
http://localhost:25641/status/KillDarkness
```
**Resposta (jogador online):**
```json
{
  "player": "KillDarkness",
  "online": true,
  "health": 20.0,
  "world": "world",
  "status": 200
}
```
**Resposta (jogador offline):**
```json
{
  "error": "Jogador não encontrado ou offline",
  "status": 404
}
```

### Lista de Jogadores
**Endpoint:** `/list`  
**Método:** GET  
**Descrição:** Lista todos os jogadores online com detalhes  
**Exemplo:**
```
http://localhost:25641/list
```
**Resposta:**
```json
{
  "count": 3,
  "players": [
    {
      "name": "KillDarkness",
      "health": 20.0,
      "level": 5,
      "world": "world"
    },
    {
      "name": "Player2",
      "health": 15.5,
      "level": 10,
      "world": "world_nether"
    },
    {
      "name": "Player3",
      "health": 8.0,
      "level": 7,
      "world": "world_the_end"
    }
  ],
  "status": 200
}
```

## Autenticação

Se você configurou um token de API no arquivo `config.yml`, precisará incluí-lo em todas as requisições:

```
http://localhost:25641/status?token=seu_token_secreto
```

## Códigos de Status HTTP

Todas as respostas incluem um campo `status` com o código HTTP:

* **200**: Sucesso
* **400**: Erro na requisição (parâmetros inválidos)
* **401**: Não autorizado (token inválido)
* **404**: Recurso não encontrado
* **500**: Erro interno do servidor

## Exemplos de Uso

### cURL
```bash
# Obter status do servidor
curl -X GET "http://localhost:25641/status"

# Obter status de um jogador
curl -X GET "http://localhost:25641/status/KillDarkness"

# Listar todos os jogadores
curl -X GET "http://localhost:25641/list"

# Com autenticação
curl -X GET "http://localhost:25641/status?token=seu_token_secreto"
```

### JavaScript (Node.js)
```javascript
const fetch = require('node-fetch');

// Obter status do servidor
fetch('http://localhost:25641/status')
  .then(res => res.json())
  .then(data => console.log(data));

// Com autenticação
fetch('http://localhost:25641/status?token=seu_token_secreto')
  .then(res => res.json())
  .then(data => console.log(data));
```

### Python
```python
import requests

# Obter status do servidor
response = requests.get('http://localhost:25641/status')
data = response.json()
print(data)

# Com autenticação
response = requests.get('http://localhost:25641/status?token=seu_token_secreto')
data = response.json()
print(data)
```

## Integração

Você pode facilmente integrar o nStatus com:

* Painéis web de administração
* Bots do Discord
* Aplicativos móveis
* Qualquer sistema que possa fazer requisições HTTP

## Compilação

Para compilar o plugin a partir do código fonte:

1. Clone o repositório
2. Certifique-se de ter o Maven instalado
3. Execute `mvn clean package`
4. O arquivo JAR será gerado na pasta `target/`

## Requisitos

* Servidor Paper/Spigot 1.21+
* Java 21+

## Licença

[MIT License](LICENSE)

## Autor

KillDarkness

---

Feito com ❤️ para a comunidade Minecraft