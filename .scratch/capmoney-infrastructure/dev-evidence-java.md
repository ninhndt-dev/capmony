# Dev evidence Java — INFRA-01.1 / Planio #47

Ngày 14/10/2026, Dev `/root`; tiếp tục từ commit `13f2935` trên `feat/cm-01-manager-setup`. Đây là self-check, chưa QA_PASS. VM riêng `colima-capmoney-infra` (2 CPU, 4 GiB, 20 GiB) được tạo vì Docker Desktop thiếu executable và Colima default đã Broken; không xóa hay sửa môi trường cũ. VM mới dùng socket `~/.colima/capmoney-infra/docker.sock`, cấu hình CLI trong `.local/docker-cli` để dùng Compose cài sẵn.

## Thay đổi

- Đồng bộ npm lockfile với React Router/Tailwind; build FE qua. Pin digest base image/API/web/Compose.
- Maven Wrapper 3.3.4 / Maven 3.9.11; archive được so SHA-512 kho Maven Central trước khi ghi SHA-256 vào wrapper properties. Wrapper giữ artifact tải xuống theo chính sách không xóa; bản Windows chưa chạy.
- Sửa test probe vào `BOOT-INF/classes`, không đưa vào runtime JAR; bổ sung runner JavaMailSender và test DB/Redis outage, SMTP refused/timeout, Spring Boot context với Testcontainers PostgreSQL/Redis. Testcontainers opt-in, bắt buộc reuse và tắt Ryuk, giữ container/file thử. Dùng starter Liquibase để kích hoạt autoconfiguration Boot 4; changelog không có changeset nghiệp vụ.
- Chặn down/probe-down/reset trước Docker; stop/probe-stop giữ tài nguyên; up dùng no-recreate; runner giữ container và không recreate dependency. Probe riêng project `capmoney-test-probe`, cổng 38080, subnet 172.30.50.0/24. Guard test chọn stat theo OS và giữ artifacts.

## Lệnh và kết quả thực chạy

| Kiểm | Kết quả |
| --- | --- |
| `sh -n infra/scripts/stack.sh`; `sh infra/scripts/stack.test.sh` | exit 0, 12 cases; đã chạy trên macOS. Linux cần QA thực chạy. |
| `npm install --ignore-scripts --cache .local/npm-cache`; `npm run build` | exit 0; typecheck/Vite build qua, 0 vulnerabilities tại lượt install. Host Node 22.23.2 báo engine warning vì dự án chọn 24; Docker web build dùng Node 24.11.1 và qua. |
| `JAVA_HOME=/opt/homebrew/opt/openjdk@25 apps/api/mvnw -B -f apps/api/pom.xml test` | Unit DB/Redis 2 pass; SMTP refused/timeout 2 pass, Mailpit case skip khi không có SMTP_HOST; không coi skip là pass. |
| Cùng wrapper, `DOCKER_HOST=unix:///Users/ninhndt/.colima/capmoney-infra/docker.sock TESTCONTAINERS_HOST_OVERRIDE=127.0.0.1 TESTCONTAINERS_RYUK_DISABLED=true TESTCONTAINERS_REUSE_ENABLE=true CAPMONEY_CONTAINER_TESTS=true` | Spring Boot context + PG/Redis thật qua: tổng 6 test, 5 pass, 1 skip (Mailpit host); exit 0, log `.local/java-spring-tests-retry.log`. Java 25.0.4.1. |
| `stack.sh test mailcheck` với DOCKER_CONFIG/DOCKER_HOST VM trên | Runner `capmoney-test-mailcheck-run-5c0c5cb725c3` exit 0; MailpitMailTest 3/3 pass, gồm thư nhận trong hộp + refused + timeout. Reports `.local/mail-surefire-reports/`. |
| `stack.sh test up`; `test probe-up` | Cả stack được dựng/khởi động; lần đầu FE fail do lockfile cũ, đã sửa và rebuild qua. Hai lượt shell bị đọc lại phần đuôi do script đang được sửa khi tiến trình cũ chờ Docker, nên exit khác 0 sau khi container đã Started; không gọi hai lệnh này full pass. File script hiện tại qua sh -n/guard; QA cần chạy từ snapshot cố định. |
| HTTP/runtime, outage, ingress, persistence | JSON bên dưới ghi thực tế. Runtime test image `sha256:c7028ead464d4106a6c8d5a0a2368167d63a86a8c58574f4f3e130528421c82f` được build trước bổ sung starter Liquibase; không gán các kết quả này cho image cuối. QA phải kiểm lại image cuối. |

## Bằng chứng quan sát

- [Runtime](evidence/java-2026-10-14/runtime.json): `/`, deep link, health/readiness và OpenAPI 200; runtime probe 404. PostgreSQL outage: cả health/readiness 503; Redis outage: health 200, readiness 503; phục hồi cùng PID 3542 và StartedAt. Không 502/restart tay.
- [Ingress](evidence/java-2026-10-14/ingress.json): thiếu/sai secret, thiếu/lặp/malformed/mismatch IP và peer ngoài subnet đều 403, marker không đổi; hợp lệ 200. Qua Caddy: Origin thiếu/sai 403, đúng 200; đang dùng cùng filter chính.
- [Persistence](evidence/java-2026-10-14/persistence.json): marker còn qua restart; volume mới sạch không có marker và DB cũ không đổi.
- [Container kế tiếp](evidence/java-2026-10-14/successor.json): PostgreSQL mới dùng volume cũ đọc lại marker, giữ container trước đã dừng; sau thử dừng container mới và bật lại cũ. Caddy bổ sung cùng subnet/secret nhưng TCP peer khác được chấp nhận. Không xóa tài nguyên.

## Review và phần còn lại

Standards reviewer `/root/review_standards` không thấy blocker; Tech/Spec reviewer `/root/review_spec` xác nhận phương pháp bảo toàn tài nguyên giữ đúng mục tiêu kiểm thử, không còn blocker code trong phạm vi WIP. Reviewer không cấp QA_PASS. Các finding probe subnet, stat Linux và probe-stop đã sửa.

Dev còn phải bàn giao snapshot commit/image cuối; QA độc lập chạy IF-J-T01–08 trên snapshot đó, gồm Linux guard, secret/log/image checks, topology, giả header qua Caddy, secret đổi một byte, fresh environment và stopped-start regression. Chưa kiểm thiết bị hay sản phẩm CM-01, chưa sửa Planio/đóng #47. CM-01 #43 vẫn chờ #47 hoàn tất.

## P1 bổ sung — PostgreSQL im lặng

Review toàn bộ diff Java từ `b1aa9f4` đến `7dfe7b3` phát hiện Hikari acquisition/validation timeout không giới hạn đọc truy vấn đang chạy. `infra/scripts/postgres-timeout.test.py` đã tái hiện red trên dev image `7dfe7b3`: pause PostgreSQL, HTTP không hoàn tất trong 5 giây; script luôn unpause trong finally. Log `.local/db-timeout-before.log`. Đây là test hồi quy đúng đường HTTP qua Caddy, không phải unit test mô phỏng.

Sửa PgJDBC connectTimeout=1, socketTimeout=2, cancelSignalTimeout=1 và JdbcTemplate query-timeout=1s. Standards/Tech reviewers không thấy blocker mới; P1 chỉ đóng khi QA chạy green trên image mới và giữ nguyên PID/StartedAt. Wrapper unit suite sau sửa exit0: tổng6, pass4, skip2 (Testcontainers/Mailpit opt-in); không coi skip là pass. QA đang dựng project v2 riêng; chưa DEV_REVIEWED/QA_PASS khi chưa có green.
