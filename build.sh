#!/bin/bash

# Script para compilar o plugin nStatus

echo "🛠️  Executando mvn clean package..."
mvn clean package

if [ $? -ne 0 ]; then
    echo "❌ Erro no build Maven!"
    exit 1
fi

# Encontrar o arquivo JAR gerado
JAR_FILE=$(find target/ -name "nstatus-*.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ Nenhum arquivo nstatus-*.jar encontrado em target/"
    exit 1
fi

echo "📦 Arquivo gerado com sucesso: $JAR_FILE"
echo "✅ Build concluído!"