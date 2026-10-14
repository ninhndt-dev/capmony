#!/bin/sh
set -eu

repo_root="$(cd "$(dirname "$0")/../.." && pwd)"
stack="$repo_root/infra/scripts/stack.sh"
test_root="$(mktemp -d "${TMPDIR:-/tmp}/capmoney-stack-test.XXXXXX")"
# Keep test artifacts for inspection; project policy forbids cleanup deletion.
mkdir -p "$test_root/bin"
export FAKE_DOCKER_LOG="$test_root/docker.log"
cat > "$test_root/bin/docker" <<'EOF'
#!/bin/sh
printf '%s|%s|%s|%s\n' "$*" "$APP_ORIGIN" "$CAPMONEY_SECRET_DIR" "$CAPMONEY_PORT" >> "$FAKE_DOCKER_LOG"
EOF
chmod +x "$test_root/bin/docker"
export PATH="$test_root/bin:$PATH"

if APP_ORIGIN='http://evil.invalid' CAPMONEY_SECRET_DIR="$test_root/secrets" "$stack" dev config > "$test_root/output" 2>&1; then
  echo 'wrong APP_ORIGIN was accepted' >&2
  exit 1
fi
if [ -s "$FAKE_DOCKER_LOG" ] || [ -d "$test_root/secrets" ]; then
  echo 'wrong APP_ORIGIN reached Docker or created a secret' >&2
  exit 1
fi

CAPMONEY_SECRET_DIR="$test_root/secrets" CAPMONEY_PORT=19001 APP_ORIGIN='http://127.0.0.1:19001' "$stack" dev config
if [ ! -f "$test_root/secrets/dev/pg-password" ]; then
  echo 'dev secret was not created in its own directory' >&2
  exit 1
fi
if ! tail -1 "$FAKE_DOCKER_LOG" | grep -Fq "|http://127.0.0.1:19001|$test_root/secrets/dev|19001"; then
  echo 'valid custom origin was not passed to Compose' >&2
  exit 1
fi

APP_ORIGIN= CAPMONEY_SECRET_DIR="$test_root/secrets" "$stack" test config
if [ ! -f "$test_root/secrets/test/pg-password" ]; then
  echo 'test secret was not created in its own directory' >&2
  exit 1
fi
file_mode() {
  case "$(uname -s)" in
    Darwin) stat -f %Lp "$1" ;;
    *) stat -c %a "$1" ;;
  esac
}
if [ "$(file_mode "$test_root/secrets/dev/pg-password")" != 600 ] || [ "$(file_mode "$test_root/secrets/test/pg-password")" != 600 ]; then
  echo 'secret file permissions are not 600' >&2
  exit 1
fi
if [ "$(realpath "$test_root/secrets/dev/pg-password")" = "$(realpath "$test_root/secrets/test/pg-password")" ]; then
  echo 'dev and test share a secret file' >&2
  exit 1
fi

calls_before="$(wc -l < "$FAKE_DOCKER_LOG")"
if CAPMONEY_SECRET_DIR="$test_root/probe-secrets" "$stack" dev probe-up > "$test_root/output" 2>&1; then
  echo 'dev probe was accepted' >&2
  exit 1
fi
if [ "$(wc -l < "$FAKE_DOCKER_LOG")" != "$calls_before" ] || [ -d "$test_root/probe-secrets" ]; then
  echo 'dev probe reached Docker or created a secret' >&2
  exit 1
fi

for environment in dev test; do
  for action in down probe-down reset; do
    if CAPMONEY_SECRET_DIR="$test_root/blocked-secrets" "$stack" "$environment" "$action" > "$test_root/output" 2>&1; then
      echo "destructive action was accepted: $environment $action" >&2
      exit 1
    fi
  done
done
if [ "$(wc -l < "$FAKE_DOCKER_LOG")" != "$calls_before" ] || [ -d "$test_root/blocked-secrets" ]; then
  echo 'destructive action reached Docker or created a secret' >&2
  exit 1
fi
APP_ORIGIN= CAPMONEY_SECRET_DIR="$test_root/secrets" "$stack" test stop
if ! tail -1 "$FAKE_DOCKER_LOG" | grep -Fq ' stop|'; then
  echo 'stop did not preserve resources with Compose stop' >&2
  exit 1
fi
APP_ORIGIN= CAPMONEY_SECRET_DIR="$test_root/secrets" "$stack" test probe-up
if ! tail -1 "$FAKE_DOCKER_LOG" | grep -Fq -- '-p capmoney-test-probe ' || ! tail -1 "$FAKE_DOCKER_LOG" | grep -Fq -- 'up --build --no-recreate -d|http://127.0.0.1:38080|'; then
  echo 'probe project/port or preservation guard is incorrect' >&2
  exit 1
fi
printf 'stack config guards: 12 cases passed; artifacts: %s\n' "$test_root"
