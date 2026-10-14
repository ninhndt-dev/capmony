# INFRA-01.1: Dựng môi trường Docker dev/test Java

**Parent:** [Planio #46 – INFRA-01](https://tanninh.planio.com/issues/46).
**Planio child:** [#47 – INFRA-01.1](https://tanninh.planio.com/issues/47).
**Status:** `ready-for-agent` — `READY_FOR_IMPLEMENTATION` đạt cho bản Java/Redis ngày 14/10/2026 sau BA/Tech ticket review và PM kiểm gate. Readiness/Dev review của Node/Hono trước TS10–TS38 chỉ là lịch sử. Planio còn `Open` tại lần đọc gần nhất; PM/root cần đồng bộ tracker trước khi giao Dev.
**Blocked by:** None; không có `HARD` blocker khởi động.
**Integration blocked by:** không có bên cung cấp khác. #47 là `HARD` blocker của CM-01 #43 theo relation #8, không phải ngược lại.
**Owner / review:** PM giao DevOps Dev sau readiness; reviewer code khác người thực hiện, PM rà phạm vi và QA độc lập chạy build.

## What it delivers

Một môi trường Docker dev/test cô lập để thử React/Vite placeholder, API health/readiness Java Spring, PostgreSQL, Redis và Mailpit qua một Caddy origin loopback. API/DB/Redis/Mailpit không publish host port. Dữ liệu/secret dev và test tách nhau; không dùng dữ liệu thật. Không làm tài khoản quản lý, JWT, CSRF sản phẩm, giao dịch tài chính hoặc VPS production.

## Acceptance criteria tham chiếu

- [ ] `AC-IF01-1` — Compose, bản vá/image được pin, secret và dữ liệu thử.
- [ ] `AC-IF01-2` — cùng origin, SPA deep link, health DB, readiness Redis, outage/recovery.
- [ ] `AC-IF01-3` — port/network, edge peer/header và Origin probe fail closed.
- [ ] `AC-IF01-4` — PostgreSQL marker qua restart/recreate, reset test cô lập.
- [ ] `AC-IF01-5` — JavaMailSender tới Mailpit và lỗi/timeout SMTP.

Nội dung chuẩn ở [spec 20 mục](../spec.md); đúng 5 mã AC. [Thiết kế Java V1–V8](../technical-design-java.md) là hợp đồng kiểm chứng, [ADR 0003](../../../docs/adr/0003-java-spring-and-deployment.md) là ranh giới chung. Không dùng [thiết kế Node/Hono](../technical-design.md) để triển khai.

## Hợp đồng triển khai

1. `apps/api` trở thành Spring Boot 4.1/Maven trên Temurin 25, Spring MVC giữ `/api`; dùng JPA/Liquibase chỉ ở mức cấu hình health, **không tạo schema nghiệp vụ**. `GET /api/health` phản ánh API+PostgreSQL; `GET /api/ready/auth` phản ánh PostgreSQL+Redis. Khi PG/Redis mất sau khi JVM đã chạy, endpoint liên quan trả **HTTP 503 từ cùng PID** qua Caddy, không 502/restart loop; tự `UP` lại khi phụ thuộc phục hồi. Springdoc/OpenAPI được pin và smoke-test trên cặp bản vá thực.
2. `apps/web` là React/Vite/Router/Tailwind placeholder ghi rõ môi trường thử. Caddy phục vụ `/`, deep link `/manager/setup`, chuyển `/api` giữ prefix tới Java; chỉ Caddy bind host `127.0.0.1`. Tạo dev/test Compose project, volume PostgreSQL, namespace Redis và secret tách biệt. Chỉ reset volume test sau khi xác minh project name; `down` thường giữ volume. Không gắn volume Node cũ hay production.

   TS22 **TanStack Query + fetch vẫn được Ninh chọn** nhưng #47 chưa tải dữ liệu sản phẩm; pin/cài Query ở ticket FE đầu tiên gọi API. Placeholder #47 chỉ cần React/Vite/Router/Tailwind và phép thử route/health.
3. Caddy ghi đè IP headers/edge secret, Spring kiểm TCP peer thật + secret + XFF/X-Real-IP đơn hợp lệ trước auth/throttle. Secret edge là 64 ký tự hex ASCII từ file `0600` không newline, chỉ mount read-only; không in giá trị trong Git/image/log/`docker compose config`. `APP_ORIGIN` phải đúng Caddy loopback. Probe POST chỉ trong test source/image dùng cùng main JAR/filter, runtime image trả 404; Origin/peer/header/secret thiếu/sai không đổi marker. `caddy adapt` qua và request thật chứng minh bytes secret cùng được đọc; không coi cú pháp là bằng chứng chạy đúng. CSRF/JWT sản phẩm thuộc CM-01.
4. JavaMailSender gửi thư thử tới `manager@example.test` qua Mailpit nội bộ; SMTP refused/timeout báo rõ thất bại hoặc kết quả chưa rõ, không báo đã gửi. Testcontainers PostgreSQL/Redis chạy ở host/CI có Docker daemon; runtime image không chứa test class hoặc probe. Pin Maven Wrapper, Java/Boot/springdoc và image versions/digests trước khi QA chạy.

## Dữ liệu và bằng chứng thực chạy cần bàn giao

- Secret dev/test giả nhưng ngẫu nhiên và tách biệt, DB marker không nhạy cảm, email `manager@example.test`, `APP_ORIGIN` loopback khác nhau; không có credential hay URL production.
- Dev ghi commit, image digest, Compose project, versions, lệnh/exit code, HTTP status/body, PID/uptime qua outage, network inspect và log đã che secret cho V1–V8. Runtime probe 404; request bị từ chối không đổi marker.
- [Test spec Java](../test-spec.md) IF-J-T01–IF-J-T08 do QA chạy sau code review. Kết quả Node/Hono IF-T03 fail trong [QA evidence cũ](../qa-evidence.md) không chuyển thành pass. `DEV_REVIEWED` cần reviewer khác người code và PM; `QA_PASS` cần chạy thật mọi AC trên build Java xác định.

## Rủi ro và giới hạn

Nếu Spring/Caddy không chứng minh peer/header/secret, probe lọt runtime, secret lộ hoặc DB outage tạo 502/JVM restart thì dừng gate Dev/QA và sửa trước khi tích hợp CM-01. Volume giữ dữ liệu test qua recreate **không phải backup**. Production Ubuntu/Temurin/systemd/TLS/SMTP thật, bảo mật JWT/CSRF, quota Redis và hai API instance thuộc ticket sau; #47 hoàn tất không tự mở readiness #43.
