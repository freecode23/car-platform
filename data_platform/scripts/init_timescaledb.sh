#!/bin/bash
set -e

# Wait for TimescaleDB to be ready
until pg_isready -h "$POSTGRES_HOST" -p "$POSTGRES_PORT" -U "$POSTGRES_USER"; do
  >&2 echo "TimescaleDB is unavailable - sleeping"
  sleep 1
done

# Create table and convert to hypertable
PGHOST="$POSTGRES_HOST" PGPORT="$POSTGRES_PORT" PGUSER="$POSTGRES_USER" PGDATABASE="$POSTGRES_DB" PGPASSWORD="$POSTGRES_PASSWORD" psql -v ON_ERROR_STOP=1 <<-EOSQL
  CREATE TABLE IF NOT EXISTS gps_data (
      timeISO TIMESTAMPTZ NOT NULL,
      time BIGINT NOT NULL,
      latitude DOUBLE PRECISION NOT NULL,
      longitude DOUBLE PRECISION NOT NULL
  );

  SELECT create_hypertable('gps_data', 'time', if_not_exists => TRUE);
EOSQL
