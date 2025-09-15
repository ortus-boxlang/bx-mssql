#!/bin/bash

# Start SQL Server in the background
/opt/mssql/bin/sqlservr &

# Wait for SQL Server to start
echo "Waiting for SQL Server to start..."
sleep 30

# Check if SQL Server is ready - try different sqlcmd paths
SQLCMD_PATH=""
if [ -f "/opt/mssql-tools/bin/sqlcmd" ]; then
    SQLCMD_PATH="/opt/mssql-tools/bin/sqlcmd"
elif [ -f "/opt/mssql-tools18/bin/sqlcmd" ]; then
    SQLCMD_PATH="/opt/mssql-tools18/bin/sqlcmd"
else
    echo "Error: sqlcmd not found in expected locations"
    exit 1
fi

echo "Using sqlcmd at: $SQLCMD_PATH"

# Check if SQL Server is ready with timeout
RETRY_COUNT=0
MAX_RETRIES=30
until $SQLCMD_PATH -S localhost -U sa -P "$SA_PASSWORD" -Q "SELECT 1" -C &> /dev/null
do
  echo "SQL Server is starting up... (attempt $((RETRY_COUNT + 1))/$MAX_RETRIES)"
  sleep 5
  RETRY_COUNT=$((RETRY_COUNT + 1))
  if [ $RETRY_COUNT -ge $MAX_RETRIES ]; then
    echo "Error: SQL Server failed to start within expected time"
    exit 1
  fi
done

echo "SQL Server is ready. Running initialization scripts..."

# Run initialization scripts
for script in /docker/init/*.sql; do
    if [ -f "$script" ]; then
        echo "Executing $script..."
        $SQLCMD_PATH -S localhost -U sa -P "$SA_PASSWORD" -i "$script" -C
        if [ $? -eq 0 ]; then
            echo "Successfully executed $script"
        else
            echo "Error executing $script"
        fi
    fi
done

echo "Database initialization completed!"

# Keep the container running
wait