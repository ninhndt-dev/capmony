# Docker dev/test (INFRA-01.1)

Chạy tại gốc repo: `./infra/scripts/stack.sh dev up`. Mở `http://127.0.0.1:18080/`, tải lại `/manager/setup`, kiểm `GET /api/health`. `./infra/scripts/stack.sh dev down` dừng stack nhưng giữ volume. Với môi trường test độc lập dùng `./infra/scripts/stack.sh test up` (cổng 28080); `./infra/scripts/stack.sh test reset` chỉ xóa volume dự án test.

Script tạo mật khẩu PostgreSQL ngẫu nhiên trong `.local/secrets/<dev|test>/pg-password` với quyền 600. `.local/` bị Git và Docker build bỏ qua. Có thể đặt `CAPMONEY_SECRET_DIR` tới **thư mục gốc bí mật** ngoài repo; script luôn thêm `/dev` hoặc `/test`, nên hai môi trường không dùng chung file. `APP_ORIGIN` của stack local phải đúng `http://127.0.0.1:<CAPMONEY_PORT>`; override khác sẽ bị từ chối trước khi chạy Docker. Không chép secret vào `.env`, Planio, log hoặc Git. `docker compose config` sẽ hiện **đường dẫn** secret, không hiện giá trị. Sau khi thay secret, phải reset volume test hoặc đổi mật khẩu trong DB; xóa file secret một mình không đổi mật khẩu DB đã tạo.

Kiểm SMTP nội bộ: `./infra/scripts/stack.sh test mailcheck`. Mailpit không publish cổng; xem hộp thư bằng truy vấn nội bộ qua container hoặc `docker compose exec mailpit ...` trong test. Để thử policy IP/Origin bằng route dành riêng cho test, dừng test stack rồi chạy `./infra/scripts/stack.sh test probe-up`; script từ chối probe trong project dev. Image API test có `/api/__probe`, image runtime bình thường không có route này. `./infra/scripts/stack.sh test probe-down` dừng sau khi thử. Không chạy probe với dữ liệu thật.

Chỉ Caddy bind `127.0.0.1`; API/PostgreSQL/Mailpit chỉ nằm trong các mạng Compose. Đây là hạ tầng dev/test; chưa có tài khoản, ví, production TLS hay backup.
