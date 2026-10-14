#!/bin/sh
set -eu

repo_root="$(cd "$(dirname "$0")/../.." && pwd)"
environment="${1:-}"
action="${2:-}"
if [ "$environment" != dev ] && [ "$environment" != test ]; then
  echo 'Usage: infra/scripts/stack.sh dev|test up|stop|config|ps|mailcheck|probe-up|probe-stop' >&2
  exit 2
fi
case "$action" in
  down|probe-down|reset)
    echo 'Resource deletion is forbidden. Use stop to preserve containers, networks and volumes; use a new isolated environment for clean tests.' >&2
    exit 2 ;;
esac
if [ -z "$action" ]; then
  echo 'Missing action' >&2
  exit 2
fi
if [ "$environment" != test ] && { [ "$action" = probe-up ] || [ "$action" = probe-stop ]; }; then
  echo 'Security probe is allowed only in the isolated test project' >&2
  exit 2
fi

if [ "$environment" = dev ]; then
  default_port=18080
  default_subnet=172.30.48.0/24
  default_redis_database=0
else
  default_port=28080
  default_subnet=172.30.49.0/24
  default_redis_database=1
fi
if [ "$action" = probe-up ] || [ "$action" = probe-stop ]; then
  default_port=38080
  default_subnet=172.30.50.0/24
fi
export CAPMONEY_PORT="${CAPMONEY_PORT:-$default_port}"
export API_SUBNET="${API_SUBNET:-$default_subnet}"
export REDIS_DATABASE="$default_redis_database"
expected_origin="http://127.0.0.1:$CAPMONEY_PORT"
if [ -n "${APP_ORIGIN:-}" ] && [ "$APP_ORIGIN" != "$expected_origin" ]; then
  echo "APP_ORIGIN must equal $expected_origin for the local stack" >&2
  exit 2
fi
export APP_ORIGIN="$expected_origin"
export DB_NAME="capmoney_$environment"
export DB_USER="capmoney_$environment"
secret_root="${CAPMONEY_SECRET_DIR:-$repo_root/.local/secrets}"
export CAPMONEY_SECRET_DIR="$secret_root/$environment"

case "$action" in
  up|probe-up|config|mailcheck)
    mkdir -p "$CAPMONEY_SECRET_DIR"
    chmod 700 "$CAPMONEY_SECRET_DIR"
    if [ ! -e "$CAPMONEY_SECRET_DIR/pg-password" ]; then
      umask 077
      head -c 32 /dev/urandom | od -An -tx1 | tr -d ' \n' > "$CAPMONEY_SECRET_DIR/pg-password"
    fi
    chmod 600 "$CAPMONEY_SECRET_DIR/pg-password"
    if [ ! -e "$CAPMONEY_SECRET_DIR/edge-auth" ]; then
      umask 077
      head -c 32 /dev/urandom | od -An -tx1 | tr -d ' \n' > "$CAPMONEY_SECRET_DIR/edge-auth"
    fi
    chmod 600 "$CAPMONEY_SECRET_DIR/edge-auth"
    if [ "$(wc -c < "$CAPMONEY_SECRET_DIR/edge-auth")" -ne 64 ] || ! LC_ALL=C grep -Eq '^[0-9a-fA-F]{64}$' "$CAPMONEY_SECRET_DIR/edge-auth"; then
      echo 'edge-auth must contain exactly 64 ASCII hex bytes without newline' >&2
      exit 2
    fi
    ;;
esac

compose() {
  if [ "$action" = probe-up ] || [ "$action" = probe-stop ]; then
    docker compose -p "capmoney-$environment-probe" -f "$repo_root/infra/compose.yaml" -f "$repo_root/infra/compose.probe.yaml" "$@"
  else
    docker compose -p "capmoney-$environment" -f "$repo_root/infra/compose.yaml" "$@"
  fi
}
case "$action" in
  up|probe-up) compose up --build --no-recreate -d ;;
  stop|probe-stop) compose stop ;;
  config) compose config ;;
  ps) compose ps ;;
  mailcheck) compose --profile tools run --build --no-deps mailcheck ;;
  *) echo "Unknown action: $action" >&2; exit 2 ;;
esac
