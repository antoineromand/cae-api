#!/bin/sh
set -e

echo "⏳ Waiting for DB at $FLYWAY_URL..."
until flyway -url="$FLYWAY_URL" -user="$FLYWAY_USER" -password="$FLYWAY_PASSWORD" info > /dev/null 2>&1; do
  echo "🔁 Still waiting for DB..."
  sleep 3
done

echo "✅ DB is up. Running migrations..."
exec flyway migrate