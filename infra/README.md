# Docker dev/test Java (INFRA-01.1)

Chạy tại gốc repo: `./infra/scripts/stack.sh dev up`. Mở `http://127.0.0.1:18080/`, tải lại `/manager/setup`, kiểm `/api/health` và `/api/ready/auth`. Test dùng `./infra/scripts/stack.sh test up` trên cổng 28080. Chỉ Caddy publish loopback; PostgreSQL, Redis, Java API và Mailpit dùng mạng nội bộ.

Dừng bằng `./infra/scripts/stack.sh dev stop` hoặc `test stop`: giữ container, network và volume. `down`, `probe-down`, `reset` bị từ chối trước khi gọi Docker theo quyền Ninh xác nhận ngày 14/10/2026. Cần dữ liệu sạch thì tạo môi trường thử mới; không xóa hoặc tái sử dụng volume cũ. `up` dùng `--no-recreate`, giữ container đã có; build mới không tự thay container cũ. Khi cần kiểm build khác, tạo môi trường riêng.

Script sinh secret PostgreSQL, edge, khóa ký JWT và khóa tra cứu refresh trong `.local/secrets/<dev|test>/`, file mode 600, thư mục 700. Git và Docker build bỏ qua `.local/`. `CAPMONEY_SECRET_DIR` là thư mục gốc; script thêm `/dev` hoặc `/test`. `APP_ORIGIN` phải bằng `http://127.0.0.1:<CAPMONEY_PORT>`. Không đưa secret vào env, log hay Git; Compose config chỉ hiện đường dẫn. API/PostgreSQL sao chép secret từ bind mount vào tmpfs thuộc user tiến trình trước khi chạy, nên quyền 600 hoạt động trên Docker Linux. Giữ file secret tương ứng với volume DB đã tạo; đổi file không tự đổi mật khẩu DB hoặc giữ được phiên JWT cũ.

`./infra/scripts/stack.sh test probe-up` dùng project riêng `capmoney-test-probe` và test image; cổng 38080 và subnet 172.30.50.0/24 riêng, chạy song song runtime test; probe bị cấm trên dev. Dừng probe bằng `./infra/scripts/stack.sh test probe-stop`. `test mailcheck` chạy JavaMailSender test runner trong tools profile và giữ container kiểm thử. Runner và tích hợp SMTP còn phải có bằng chứng thực chạy trước gate QA.

Build API trên host có JDK 25: `cd apps/api && ./mvnw -B test`. Hoặc build bằng Docker: `docker build --target build -f apps/api/Dockerfile .`. Test unit không chứng minh JVM sống qua outage thật; QA phải kiểm HTTP và PID trên stack Docker. Chưa có VPS production, TLS hay backup trong phạm vi hạ tầng này.
