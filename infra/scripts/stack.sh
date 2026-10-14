#!/bin/sh
set -eu

repo_root="$(cd "$(dirname "$0")/../.." && pwd)"
environment="${1:-}"
action="${2:-}"
if [ "$environment" != dev ] && [ "$environment" != test ]; then
  echo 'Usage: infra/scripts/stack.sh dev|test up|down|config|ps|mailcheck|probe-up|probe-down|reset' >&2
  exit 2
fi
if [ -z "$action" ]; then
  echo 'Missing action' >&2
  exit 2
fi
if [ "$environment" != test ] && { [ "$action" = probe-up ] || [ "$action" = probe-down ]; }; then
  echo 'Security probe is allowed only in the isolated test project' >&2
  exit 2
fi

if [ "$environment" = dev ]; then
  default_port=18080
  default_subnet=172.30.48.0/24
  default_ip=172.30.48.10
else
  default_port=28080
  default_subnet=172.30.49.0/24
  default_ip=172.30.49.10
fi
export CAPMONEY_PORT="${CAPMONEY_PORT:-$default_port}"
export API_SUBNET="${API_SUBNET:-$default_subnet}"
export CADDY_API_IP="${CADDY_API_IP:-$default_ip}"
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
    ;;
esac

compose() {
  if [ "$action" = probe-up ] || [ "$action" = probe-down ]; then
    docker compose -p "capmoney-$environment" -f "$repo_root/infra/compose.yaml" -f "$repo_root/infra/compose.probe.yaml" "$@"
  else
    docker compose -p "capmoney-$environment" -f "$repo_root/infra/compose.yaml" "$@"
  fi
}
case "$action" in
  up|probe-up) compose up --build -d ;;
  down|probe-down) compose down ;;
  config) compose config ;;
  ps) compose ps ;;
  mailcheck) compose --profile tools run --rm mailcheck ;;
  reset)
    if [ "$environment" != test ]; then
      echo 'Reset is allowed only for the isolated test project' >&2
      exit 2
    fi
    compose down --volumes
    echo 'Isolated test volume removed' ;;
  *) echo "Unknown action: $action" >&2; exit 2 ;;
esac
