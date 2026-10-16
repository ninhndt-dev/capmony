# QA CM-01.2 / Planio #44 — kiểm thử trên build Java 25

**Addendum 16/10/2026:** Lỗi quyền secret Linux nêu trong báo cáo snapshot `059d868` đã được sửa bằng tmpfs/chown ở commit `4a4a1a4` và chạy lại Compose cô lập với secret host mode 600. Xem [bằng chứng Dev #45](qa45-dev-evidence.md). Đây không thay kết luận về người kiểm: #44 vẫn cần QA khác người code và PM xác nhận trên build được pin trước khi cấp `QA_PASS`.

**Ngày:** 15/10/2026. **Trạng thái:** Đã chạy các phép kiểm setup T07–T12, phần setup T18–T19. Chưa cấp `QA_PASS` ticket #44 hoặc mở gate #45: người ghi lượt này cũng là tác giả web #44 trong handoff, nên cần QA độc lập và PM đối chiếu; còn vấn đề quyền đọc secret trên Linux nêu bên dưới.

## Bản mã và môi trường

- Nhánh `feat/cm-01-manager-setup`, commit `059d868741b3c15c640075dd7917bee1bebc0886` sau `git pull --ff-only origin feat/cm-01-manager-setup`; working tree sạch trước lượt QA.
- Docker 28.1.1 trên Ubuntu; build API bằng Dockerfile pin Maven 3.9.11 / Temurin 25, image `capmoney-qa44-build:059d868` (`sha256:f936b1dd73814dad88b269ee8510c22d849fe584dc1f78f9c50dd7cc255c1827`). Build web bằng Dockerfile pin Node 24.11.1, image `capmoney-qa44-web:059d868` (`sha256:221922f842b49df2ffd907fab3a864f7a345639179501d90037f1392777af0aa`).
- Testcontainers dùng PostgreSQL 17.7 và Redis 7.4.6 trong tài nguyên test mới, `TESTCONTAINERS_RYUK_DISABLED=true`, `TESTCONTAINERS_REUSE_ENABLE=true`. Kiểm runtime dùng năm Compose project riêng: `capmoney-qa44-059d868` (cổng 48080, hai JVM), `capmoney-qa44-boundary` (48081), `capmoney-qa44-unknown` (48082), `capmoney-qa44-webkit` (48083), `capmoney-qa44-success` (48084). Mỗi project có PostgreSQL/Redis/network/volume/secret riêng. Các project đã kiểm xong được `stop`, không `down`/xóa.
- Chrome headless trên Linux với viewport Android 393×851; WebKit trong image `mcr.microsoft.com/playwright:v1.64.1-noble` với viewport iPhone 390×844, DPR 3. Đây là giả lập theo CM01-Q4, không phải thiết bị thật của gate Story cuối.

## Kết quả đã chạy

| Kiểm tra | Kết quả | Phạm vi thực tế |
| --- | --- | --- |
| `docker build --target build -f apps/api/Dockerfile -t capmoney-qa44-build:059d868 .` | PASS | `mvn package` trên Java 25; các test mặc định trong bước package không bật container tests. |
| `docker build --target build -f apps/web/Dockerfile -t capmoney-qa44-web:059d868 .` | PASS | `npm ci`, `tsc --noEmit`, Vite build trên Node 24. Cảnh báo `use client` từ TanStack Query không làm build fail. |
| `docker run --name capmoney-qa44-junit-059d868 ... capmoney-qa44-build:059d868 mvn -B -Dtest=SetupHttpIntegrationTest test` | PASS | 1 test, 0 fail/error/skipped, 32.708 giây. Kiểm HTTP/CSRF, validation, DB rollback, Redis unavailable/quota, hai request đồng thời trong **một JVM**, account singleton và link consumed. |
| `docker run --name capmoney-qa44-full-059d868 ... capmoney-qa44-build:059d868 mvn -B test` | PASS | 12 test, 0 fail/error, 1 skipped, 54.789 giây. Các test container chạy trên DB test mới; không suy ra QA của toàn ticket từ suite này. |

`npm run typecheck` trên host không chạy vì host chưa cài `tsc`; build web trong Docker Node 24 đã chạy typecheck thành công.

## Kết quả runtime theo case

| Case | Kết quả và bằng chứng |
| --- | --- |
| T07 | **PASS:** DB clock fixture tại 72:00 → `409`, `issued`/0 account; token thế hệ cũ → `409`; fixture thế hệ mới tại 71:59 → `204`, đúng một account, link `consumed`, digest null. [expired](evidence/qa44/expired.json), [stale](evidence/qa44/stale.json), [valid](evidence/qa44/valid.json). CAS/acceptedAt ở ticket #43 còn được kiểm trong full suite; không dùng kết quả Node cũ. |
| T08 | **PASS:** qua Caddy, mật khẩu 11/73 byte, Unicode 74 byte, NUL, confirmation lệch, token sai format → `400`; body >4 KiB → `413`, DB không đổi. Mật khẩu đúng 12 byte tại T07 và Unicode đúng 72 byte ở WebKit T11 → `204`; DB lưu `$2a$12$`. Test JUnit burst xác nhận giới hạn hash đồng thời bằng `429`, đo hai request BCrypt12 khoảng 288–291 ms trên máy này. [security](evidence/qa44/security.json), [WebKit unknown](evidence/qa44/webkit-unknown.json). |
| T09 | **PASS:** qua Caddy, thiếu/sai Origin và thiếu/sai CSRF → `403`, DB giữ `issued`/0 account; gọi API trực tiếp không edge secret → `403`. Link/token sai chỉ nhận mã lỗi chung, không trả email hoặc token. [security](evidence/qa44/security.json). |
| T10 | **PASS:** hai JVM riêng cùng PostgreSQL/Redis nhận cùng token đồng thời: một `204`, một `409`; DB cuối đúng một `manager_account(id=1)`, link `consumed`, digest null; replay `409`. JUnit fixture ép insert DB rollback trả `503` và giữ `issued`/0 account. [race](evidence/qa44/race.json), `SetupHttpIntegrationTest` trong full suite. |
| T11 | **PASS:** Chrome và WebKit gửi POST thật qua Caddy; `route.fetch()` nhận server `204` sau commit, rồi route hủy response cho browser. Cả hai UI báo “Chưa rõ kết quả”, bỏ form, không tự retry; reload không giữ form. DB từng stack có đúng một manager/link consumed. Mở lại link bằng Chrome/WebKit trả `409`, DB vẫn một manager. [Chromium JSON](evidence/qa44/chromium-unknown.json), [WebKit JSON](evidence/qa44/webkit-unknown.json), [ảnh WebKit](evidence/qa44/webkit-unknown.png), [replay](evidence/qa44/chromium-replay.json). Đây là response mất **sau khi server đã trả 204**, không phải abort trước commit. |
| T12 | **PASS:** sau setup, CLI `--capmoney.bootstrap-manager` với email khác trả `bootstrap=unavailable generation=0`, exit 2; DB vẫn `consumed`, generation 1, một account id 1. Direct replay và link thế hệ cũ đều `409`. |
| T18 setup | **PASS:** GET CSRF `204`, `Cache-Control: no-store`, cookie host-only/JS đọc được; thiếu/sai Origin/CSRF `403`. Luồng thành công trên WebKit gọi CSRF hai lần (trước và sau POST `204`), chuyển tới placeholder đăng nhập; chỉ còn cookie `XSRF-TOKEN`, không có phiên auth từ setup. [security](evidence/qa44/security.json), [WebKit success](evidence/qa44/webkit-success.json), [ảnh](evidence/qa44/webkit-success.png). Nhánh login/refresh/logout thuộc #45. |
| T19 setup | **PASS:** mười request qua hai JVM dùng chung IP được xử lý, lượt 11 `429` có `Retry-After`, lần sau vẫn `429` và TTL không tăng. Đổi header IP giả ở từng request qua Caddy không né quota (lượt 11 `429`). Dừng Redis QA → setup `503`, DB không đổi; khởi động lại → request trở về `409` do link đã dùng. [quota](evidence/qa44/quota.json), [IP giả](evidence/qa44/spoof.json), [outage](evidence/qa44/outage.json), [recovery](evidence/qa44/recovered.json). Nhánh quota login thuộc #45. |

Probe và script browser dùng token/email/mật khẩu **tổng hợp** trong các DB test cô lập, được lưu tại [evidence/qa44](evidence/qa44/) cùng JSON và ảnh. Không có SMTP thật, VPS/TLS production hoặc thiết bị vật lý trong lượt này.

## Vấn đề môi trường và gate

`infra/scripts/stack.sh` tạo secret mode `600` do UID host 1000 sở hữu; trên Docker Linux hiện tại API chạy UID 10001 nên không đọc được `/run/secrets/edge_auth` và `/run/secrets/pg_password`, Caddy trả `502`. Cả năm project QA có thư mục secret riêng mode `700`; riêng hai file test chuyển mode `604` để container đọc được. Đây là ngoại lệ **chỉ cho QA cô lập**, không phải cấu hình đã duyệt cho dev/test hoặc production. Cần sửa cơ chế cấp quyền secret Linux và kiểm lại stack mặc định trước khi xem hạ tầng này sẵn sàng vận hành.

Một probe IP giả đầu tiên fail assertion vì IP container tái sử dụng quota từ probe trước; chạy lại với IP mới cố định `172.30.51.200` đã pass, lưu [kết quả](evidence/qa44/spoof.json). Không xóa counter để làm test xanh.

Chưa ghi `QA_PASS`/#45 readiness hay cập nhật Planio: workflow yêu cầu QA khác người code, trong khi root đã làm web #44. PM/QA độc lập cần đối chiếu các artifact trên và xử lý vấn đề secret trước khi cấp gate. Không sửa code ứng dụng, không commit ở lượt ghi bằng chứng này.
