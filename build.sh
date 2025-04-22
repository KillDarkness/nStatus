#!/bin/bash

# Script para compilar o plugin nStatus

echo "🔧 Iniciando processo de build do plugin nStatus..."
sleep 0.5
echo "⏳ Compilando com Maven..."

# Executa o build e oculta a saída, mostrando só se deu bom ou ruim
mvn clean package > /dev/null 2>&1
BUILD_STATUS=$?

if [ $BUILD_STATUS -ne 0 ]; then
    echo "❌ Erro ao compilar o plugin! Verifique o código."
    exit 1
fi

# Encontrar o arquivo JAR gerado
JAR_FILE=$(find target/ -name "nstatus-*.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ Nenhum arquivo nstatus-*.jar encontrado em target/"
    exit 1
fi

echo "✅ Build finalizado com sucesso!"
echo "📦 Arquivo gerado: $JAR_FILE"
