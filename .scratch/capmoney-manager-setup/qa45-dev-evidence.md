# CM-01.3 / #45 — bằng chứng Dev, 15/10/2026

**Addendum 16/10/2026:** Bản `6676d5e` sửa web để không tự gửi lại refresh sau kết quả chưa rõ; xem [báo cáo tự kiểm](qa45-refresh-unknown-fix.md). Báo cáo bên dưới là snapshot Dev ở `4a4a1a4`; không tự cấp `DEV_REVIEWED` hoặc `QA_PASS` từ addendum.

**Trạng thái:** Đã triển khai đăng nhập và phiên quản lý trên nhánh `feat/cm-01-manager-setup`. Đây là bằng chứng do người triển khai tự kiểm, chưa phải `DEV_REVIEWED`, `QA_PASS` #45 hoặc `QA_PASS` Story. `QA_PASS` #44 và gate `HARD` #44 → #45 vẫn cần QA/PM độc lập đối chiếu.

## Mã và môi trường

- API Spring Boot/Java 25: login BCrypt12, Redis Lua quota, access JWT 15 phút, refresh family hết hạn tuyệt đối sau 7 ngày, kiểm family/epoch trong PostgreSQL mỗi request, refresh CAS/reuse, logout mọi family. Cookie HttpOnly, SameSite=Lax, path riêng; production yêu cầu Secure. FE React/Vite: login, khu vực quản lý tối thiểu, reload/deep link, gia hạn phiên, logout.
- Linux Compose: bốn secret file mode 600 từ host được sao chép vào tmpfs container với đúng chủ sở hữu trước khi API hạ quyền xuống UID 10001; PostgreSQL tương tự. Không mở quyền đọc secret trên host. Kiểm runtime qua Caddy ở project cô lập `capmoney-cm01-authqa` với file mode 600, một manager test và Mailpit; các container/volume được giữ lại để tra cứu.
- Snapshot kiểm API cuối: image `capmoney-cm01-auth-final-test:work` (`sha256:399d5c5ef6bf6afcfd16795d937631932fdea64cc12932e71e173b8b9d53475f`). Browser runtime dùng image API trước sửa cuối cùng của handler lỗi DB; test tích hợp cuối xác nhận handler `REVOCATION_UNKNOWN`.

## Phép kiểm đã chạy

| Phép kiểm | Kết quả | Giới hạn |
| --- | --- | --- |
| `docker run ... capmoney-cm01-auth-final-test:work mvn -B -q test` với Testcontainers PostgreSQL/Redis | PASS: 14 test, 0 failure/error, 1 skipped | Surefire được giữ trong container `capmoney-cm01-auth-final-suite-work` và `/tmp/capmoney-cm01-auth-final-surefire`; test cần `CAPMONEY_CONTAINER_TESTS=true`. |
| `docker build --target build -f apps/web/Dockerfile ...` | PASS: TypeScript và Vite build | Chạy trên Node 24 trong Docker. |
| `infra/scripts/stack.test.sh` | PASS: 12 ca | Kiểm script tạo secret/Compose; không thay thế kiểm runtime. |
| Mailpit → setup manager → login → me → refresh → logout → me bị từ chối qua Caddy | PASS | Dữ liệu tổng hợp và DB cô lập, không dùng email thật. |
| Chromium Android 393×851 và WebKit iPhone 390×844 DPR3 | PASS: login, reload, cookie path/HttpOnly, hai phiên, logout một phiên làm phiên kia nhận 401 | [Script](evidence/qa45/qa45-login.cjs), [JSON](evidence/qa45/qa45-login.json), [Chromium](evidence/qa45/qa45-chromium-manager.png), [WebKit](evidence/qa45/qa45-webkit-manager.png). Đây là giả lập, không phải thiết bị vật lý. |
| DB dừng khi me/logout | PASS: me 503, logout 503 `REVOCATION_UNKNOWN` trong test tích hợp cuối | Lượt manual trên image trước sửa cuối trả 503 `DEPENDENCY_UNAVAILABLE`; không dùng lượt đó để xác nhận mã lỗi cuối. |

Các test API kiểm login đúng/sai, refresh rotate/reuse, stale refresh không thể logout-all, access hợp lệ logout-all, hai family, Redis down còn phiên đang dùng, DB down fail closed, JWT keyring rotate/lookup digest và thời hạn. `git diff --check` sạch. Secret/phiên test không được đưa vào Git.

## Phần chưa được xác nhận

- QA khác người code cần chạy/đối chiếu đầy đủ T13–T20 trên commit và image đã pin, nhất là hai JVM thật cho race/quota/key rotation, CSRF/Origin âm, HTTPS cookie production và phục hồi DB. Test tích hợp hiện chứng minh 503 khi DB dừng; chưa chứng minh phiên hoạt động lại sau DB khởi động trong cùng JVM vì harness Testcontainers không phục hồi ổn định ở lượt thử này.
- PM cần xác nhận QA #44 và gate #45; reviewer code khác người triển khai cần xét `DEV_REVIEWED`. Story T21 còn regression toàn luồng và iPhone/Android vật lý theo CM01-Q4. Không tự cập nhật trạng thái Planio hoặc cấp `QA_PASS` từ bằng chứng Dev này.
