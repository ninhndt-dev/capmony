#!/bin/sh
set -eu

secret_dir=/run/capmoney-secrets
test -d "$secret_dir"
cp /run/secrets/pg_password "$secret_dir/pg_password"
cp /run/secrets/edge_auth "$secret_dir/edge_auth"
cp /run/secrets/jwt_signing "$secret_dir/jwt_signing"
cp /run/secrets/jwt_lookup "$secret_dir/jwt_lookup"
chown capmoney:capmoney "$secret_dir" "$secret_dir/pg_password" "$secret_dir/edge_auth" "$secret_dir/jwt_signing" "$secret_dir/jwt_lookup"
chmod 700 "$secret_dir"
chmod 600 "$secret_dir/pg_password" "$secret_dir/edge_auth" "$secret_dir/jwt_signing" "$secret_dir/jwt_lookup"

exec runuser -u capmoney -- java -jar /app/app.jar
