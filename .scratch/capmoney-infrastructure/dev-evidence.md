# Dev evidence – INFRA-01.1 / Planio #47

**Ngày:** 14/10/2026. **Người thực hiện code:** `/root/infra_dev`. **Trạng thái:** Dev self-check xong trên worktree nhánh `feat/cm-01-manager-setup`; chờ reviewer code độc lập, PM và QA. Đây chưa phải `DEV_REVIEWED` hoặc `QA_PASS`.

## Phạm vi đã triển khai

- `infra/compose.yaml`, `Caddyfile`, `scripts/stack.sh`: hai project `capmoney-dev`/`capmoney-test` với volume/secret riêng, bốn mạng, chỉ Caddy bind loopback; Caddy giữ `/api`, web SPA fallback.
- `apps/web`: React/Vite placeholder, không giả làm màn CM-01. `apps/api`: Hono health kiểm PostgreSQL với timeout; helper IP/Origin fail-closed. Probe POST và SMTP CLI nằm trong Docker target `probe` riêng, không có trong target runtime.
- `package-lock.json` và Dockerfile pin phiên bản; secret PostgreSQL tạo trong `.local/secrets/<môi trường>` (Git/Docker ignore, mode 600), không đưa giá trị vào env hoặc Compose config.

## Lệnh và kết quả Dev đã quan sát

| Lệnh/kiểm | Môi trường | Kết quả |
| --- | --- | --- |
| `npm ci && npm run typecheck && npm test && npm run build` | Container `node:24.11.1-alpine3.22`, Docker volume cho `node_modules` | exit 0; TypeScript API/web, Vitest 10/10, bốn guard test của `stack.sh` qua, Vite build qua; chạy lại sau sửa P2 |
| `npm run build:probe --workspace apps/api` | Cùng image Node 24 | exit 0; chạy lại sau sửa P2 |
| `sh -n infra/scripts/stack.sh`, `sh -n infra/scripts/stack.test.sh`, `sh infra/scripts/stack.test.sh`, `git diff --check` | Host | exit 0; guard test 4/4 |
| `./infra/scripts/stack.sh dev config`, `test config` | Docker Compose v2.35.1 | exit 0; config ghi đường dẫn secret, không ghi giá trị |
| `./infra/scripts/stack.sh dev up` | Docker Engine 28.1.1 | build/up exit 0 sau khi chuyển Caddy static IP từ `.2` (đã trùng API) sang `.10` |
| GET `http://127.0.0.1:18080/api/health` | Dev stack qua Caddy | HTTP 200, `{"status":"healthy"}` |
| GET `http://127.0.0.1:18080/manager/setup` | Dev stack qua Caddy | HTTP 200, HTML placeholder |
| `docker compose ps` | Dev stack | Caddy `127.0.0.1:18080`; API/PostgreSQL/Mailpit không publish host port |
| `./infra/scripts/stack.sh test probe-up` | Test project cô lập | build/up exit 0; **chưa** đồng nghĩa policy đã qua probe request |
| `./infra/scripts/stack.sh test mailcheck` | Test project, CLI nội bộ | exit 0, `smtp_test_received_in_mailpit` |

## Self review và việc còn phải kiểm độc lập

Đã so ranh giới source với ticket: không có tài khoản/CM-01, tiền, schema hay production deploy. Runtime API Dockerfile chỉ copy `dist` từ bước build chính (`tsconfig.build.json` chỉ biên dịch `src/`); source `test/probe-server.ts` được compile riêng ở target `probe`. Secret local bị `.gitignore` và `.dockerignore` bỏ qua. Health không trả thông tin kết nối DB khi lỗi; SMTP CLI báo `smtp_unavailable_or_result_unknown` hoặc `smtp_accepted_inbox_unverified` khi không chứng minh được Mailpit nhận.

Sau review độc lập P2, Dev đã buộc `APP_ORIGIN` đúng loopback/port của project, luôn tách file secret override theo `/dev` và `/test`, từ chối probe trên project dev và cho runtime/probe dùng cùng cấu hình Pool. Test guard chạy chính script với Docker giả để xác nhận Origin sai dừng trước Docker, hai secret file khác nhau và probe dev bị chặn. Node 24/Alpine typecheck/test/build đã chạy lại qua. Compose dev/test ban đầu chạy trước sửa guard; QA phải build/chạy lại trên commit mới để kiểm runtime đầy đủ.

QA cần chạy IF-T01–08/V1–V6 trên build xác định, đặc biệt tắt DB, Origin/IP/peer giả qua probe, marker qua recreate, reset test không tác động dev và SMTP lỗi/timeout. Các kiểm này chưa được ghi pass tại đây. Reviewer khác người viết code cần kiểm diff và xác nhận image/runtime không chứa probe trước khi PM xét `DEV_REVIEWED`.

## Bản sửa sau QA IF-T03 (chưa commit)

QA trên commit `493f3d4` phát hiện khi dừng PostgreSQL, `pg.Pool` phát `error` ở idle client làm Node thoát; Caddy trả 502 thay vì API trả JSON 503, API không tự phục hồi sau khi DB trở lại. Dev bổ sung listener trong `createDbPool` dùng chung runtime/probe; listener chỉ ghi `db_pool_error`, không ghi lỗi gốc có thể chứa thông tin kết nối. Health vẫn tự query DB và xử lý lỗi bằng JSON `degraded`/503 như trước. Test `apps/api/test/db.test.ts` phát idle error giả chứa secret, xác nhận không throw và không log secret. Node 24/Alpine recheck `typecheck`, Vitest **11/11**, guard **4/4**, build web/API và probe đều exit 0. Chưa rebuild stack test trong lúc QA kiểm commit cũ; reviewer code độc lập và QA cần retest IF-T03 trên commit mới trước khi đánh gate.
