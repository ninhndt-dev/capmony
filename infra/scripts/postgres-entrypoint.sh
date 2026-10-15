#!/bin/sh
set -eu

secret_dir=/run/capmoney-secrets
test -d "$secret_dir"
cp /run/secrets/pg_password "$secret_dir/pg_password"
chown postgres:postgres "$secret_dir" "$secret_dir/pg_password"
chmod 700 "$secret_dir"
chmod 600 "$secret_dir/pg_password"
export POSTGRES_PASSWORD_FILE="$secret_dir/pg_password"

exec /usr/local/bin/docker-entrypoint.sh "$@"
