# Chạy CapMoney bằng Docker (dev/test)

Đây là môi trường phát triển và kiểm thử Java/Spring, React/Vite. Docker Compose **không** phải cấu hình production. Production đã chọn cài trực tiếp trên VPS nhưng chưa được triển khai; xem [tổng quan](../docs/project-overview.md).

## Khởi động và kiểm tra

Tại gốc repo, cần Docker Engine và Docker Compose:

```sh
./infra/scripts/stack.sh dev up
./infra/scripts/stack.sh dev ps
```

Dev dùng `http://127.0.0.1:18080`; test dùng `./infra/scripts/stack.sh test up` tại `http://127.0.0.1:28080`. Script tự đặt `APP_ORIGIN`, cổng, subnet API, tên DB và Redis DB theo môi trường; `APP_ORIGIN` tự đặt ngoài script phải khớp cổng. Chỉ Caddy publish loopback; API, PostgreSQL, Redis và Mailpit ở mạng nội bộ.

| URL | Ý nghĩa |
| --- | --- |
| `/` | Placeholder; chức năng ví/thu chi chưa có |
| `/manager/setup` | Trang nhận link thiết lập quản lý lần đầu |
| `/manager/login`, `/manager` | Đăng nhập và khu vực quản lý tối thiểu |
| `/api/health` | Kiểm kết nối PostgreSQL |
| `/api/ready/auth` | Kiểm PostgreSQL và Redis cho auth |

Đường API auth khác gồm `GET /api/auth/csrf`, `POST /api/manager/setup`, `POST /api/auth/login`, `POST /api/auth/refresh`, `POST /api/auth/logout`, `GET /api/manager/me`. POST cần Origin và CSRF; các endpoint quản lý cần cookie phiên. Không có API tự đăng ký/gửi link đầu tiên. [Bản đồ hành vi](../docs/project-overview.md) ghi phạm vi và trạng thái từng luồng.

## Thiết lập quản lý thử

Sau khi `dev up`, dùng CLI trong container API đang chạy, với email thử, để phát hành link qua Mailpit nội bộ:

```sh
docker exec -u 10001 -e BOOTSTRAP_ENABLED=true -e MANAGER_EMAIL=manager@example.test capmoney-dev-api-1 java -jar /app/app.jar --capmoney.bootstrap-manager
```

CLI chỉ in `bootstrap=<code> generation=<n>`, không in token/link. Mailpit không publish cổng ra host. Sau khi CLI trả `accepted`, xem danh sách thư rồi đọc thư theo `ID` từ terminal (thay `REPLACE_WITH_ID` bằng ID trong JSON):

```sh
docker exec capmoney-dev-mailpit-1 wget -qO- http://127.0.0.1:8025/api/v1/messages
docker exec capmoney-dev-mailpit-1 wget -qO- http://127.0.0.1:8025/api/v1/message/REPLACE_WITH_ID
```

Lệnh `docker exec` chỉ trả nội dung ra terminal, không lưu token trong Docker container logs. Đừng dán nội dung thư/link vào chat, Git hoặc file log. Mailpit của Compose không gắn volume; đọc thư trước khi dừng môi trường. [Runbook bootstrap](../.scratch/capmoney-manager-setup/bootstrap-runbook.md) giải thích `accepted`, `failed`, `unknown`, cách xử lý an toàn và điều kiện một lần. Đừng chạy lại mù khi kết quả chưa rõ. Thiết lập thành công chuyển sang trang login; không tự cấp phiên.

## Dừng và dùng bản build khác

```sh
./infra/scripts/stack.sh dev stop
./infra/scripts/stack.sh test stop
```

`stop` giữ container, network và volume. `down`, `probe-down`, `reset` bị script từ chối theo quyền bảo toàn tài nguyên của dự án. `up` chạy `--no-recreate`: khi mã hoặc secret thay đổi, container cũ không tự nhận bản mới. Cần snapshot hoặc DB sạch thì tạo project/thư mục secret/cổng/subnet thử **mới**, giữ tài nguyên cũ. Không xóa hoặc tái sử dụng volume cũ để làm test xanh.

Script sinh bốn secret `pg-password`, `edge-auth`, `jwt-signing`, `jwt-lookup` trong `.local/secrets/dev/` hoặc `.local/secrets/test/`, file mode 600 và thư mục 700. Nếu đặt `CAPMONEY_SECRET_DIR`, script sẽ **thêm `/dev` hoặc `/test`** vào thư mục gốc đó. Git/Docker build bỏ qua `.local/`. API và PostgreSQL sao chép file từ bind mount vào tmpfs đúng chủ sở hữu trước khi chạy; không nới quyền secret host. Secret và volume DB là một cặp trạng thái: đổi `pg-password` không đổi mật khẩu DB đã khởi tạo; đổi khóa JWT làm phiên cũ không còn xác minh được nếu không quản lý keyring. Không đưa secret vào Git, chat, log hoặc command argument.

## Kiểm thử

- `npm ci && npm run build` kiểm TypeScript/Vite với Node 24; `npm test` kiểm script stack.
- `cd apps/api && ./mvnw -B test` cần Java 25; test container được bỏ qua nếu không đặt `CAPMONEY_CONTAINER_TESTS=true`.
- Để chạy toàn bộ API suite như snapshot #45, tại gốc repo dùng tên container mới cho mỗi lượt (ví dụ thay ngày/giờ trong `--name`):

  ```sh
  docker build --target build -f apps/api/Dockerfile -t capmoney-api-test:local .
  docker run --name capmoney-api-test-unique -v /var/run/docker.sock:/var/run/docker.sock -e CAPMONEY_CONTAINER_TESTS=true -e TESTCONTAINERS_RYUK_DISABLED=true -e TESTCONTAINERS_REUSE_ENABLE=true capmoney-api-test:local mvn -B -q test
  ```

  Không dùng lại tên hoặc xóa container/volume test cũ; Surefire ở `/work/apps/api/target/surefire-reports` trong container. [Bằng chứng #45](../.scratch/capmoney-manager-setup/qa45-dev-evidence.md) ghi snapshot và kết quả thực chạy.
- `./infra/scripts/stack.sh test probe-up` dùng project/cổng 38080/subnet riêng để kiểm ingress; dừng bằng `test probe-stop`. `./infra/scripts/stack.sh test mailcheck` dùng `--no-deps`, nên phải chạy `test up` để Mailpit có sẵn trước. Đây là test SMTP/Mailpit, không phải email thật.

Bằng chứng hạ tầng, #43–#45 và các gate còn chờ được nối từ [README gốc](../README.md). Chưa có VPS, TLS/SMTP production hoặc backup đã kiểm.
