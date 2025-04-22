#!/bin/bash

# Configurações do SFTP
SFTP_SERVER="ascenty-01.destinyhost.com.br"
SFTP_USER="killdarknes.c0420c89"
SFTP_PASSWORD="killdarkness45"
SFTP_PORT="2022"
REMOTE_DIR="plugins/"

# Encontrar o arquivo JAR mais recente
JAR_FILE=$(find target/ -name "nstatus-*.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ Nenhum arquivo nstatus-*.jar encontrado em target/"
    echo "   Execute ./build.sh primeiro pra compilar o plugin."
    exit 1
fi

echo "📦 Arquivo encontrado: $JAR_FILE"

# Função pra deletar os arquivos antigos no servidor
delete_old_jars() {
    echo "🧹 Limpando JARs antigos do servidor..."
    sshpass -p "$SFTP_PASSWORD" sftp -q -P "$SFTP_PORT" -o StrictHostKeyChecking=no "$SFTP_USER@$SFTP_SERVER" > /dev/null 2>&1 <<EOF
cd $REMOTE_DIR
rm nstatus-*.jar
bye
EOF
}

# Função para enviar o novo arquivo com % manual
send_file_with_progress() {
    FILESIZE=$(stat -c%s "$JAR_FILE")
    echo "🚀 Enviando arquivo ($((FILESIZE / 1024)) KB)..."

    sshpass -p "$SFTP_PASSWORD" sftp -q -P "$SFTP_PORT" -oBatchMode=no -b - "$SFTP_USER@$SFTP_SERVER" > /dev/null 2>&1 <<EOF &
cd $REMOTE_DIR
put $JAR_FILE
bye
EOF

    SENT=0
    while [ $SENT -lt $FILESIZE ]; do
        sleep 0.3
        SENT=$((SENT + 32768))
        [ $SENT -gt $FILESIZE ] && SENT=$FILESIZE
        PERCENT=$((SENT * 100 / FILESIZE))
        echo -ne "\r📤 Enviando: $PERCENT%"
    done
    echo ""
    wait
}

# Execução
delete_old_jars
send_file_with_progress

if [ $? -eq 0 ]; then
    echo "✅ Deploy concluído com sucesso!"
else
    echo "❌ Falha no upload via SFTP"
    exit 1
fi
