# CM-02 — triển khai và tự kiểm

**Ngày:** 16/10/2026. **Phạm vi:** code Java/Spring và React của luồng xin lại link thiết lập quản lý đầu tiên hết hạn. Đây là bằng chứng do người viết code chạy; không thay review hoặc QA độc lập.

## Đã triển khai

- Liquibase `004-cm02-manager-link-renewal` thêm digest chứng minh quyền thử lại và mốc cooldown vào singleton `manager_bootstrap`; không sửa migration cũ.
- API `POST /api/manager/setup-link/status` và `/renew` dùng token trong JSON body, qua edge/Origin/CSRF/body limit; không nhận email đích từ request. Email nhận lấy từ singleton đã ghi khi CLI phát hành link đầu. Cờ `BOOTSTRAP_ENABLED` vẫn chỉ điều khiển CLI; xin lại trên web hoạt động khi cờ CLI tắt.
- Reservation khóa row DB, tăng generation trước SMTP; link cũ không thể tạo quản lý khi yêu cầu mới bắt đầu. SMTP gửi ngoài transaction, chỉ báo thành công sau CAS `issued`; lỗi/chưa rõ giữ proof để thử lại sau cooldown. Redis giới hạn request theo IP và toàn hệ thống; DB cooldown 15 phút. Sau khi tạo quản lý, không phát thêm link.
- Trang setup kiểm trạng thái link; link hết hạn hoặc lần gửi lỗi/chưa rõ hiện nút xin lại. Kết quả không rõ không tự gửi lại. Trang không có ô chọn email, token chỉ ở bộ nhớ tab và fragment được xóa khỏi URL.

## Kết quả tự kiểm

| Kiểm tra | Kết quả |
| --- | --- |
| Docker build API Java 25 và web Node 24/TypeScript/Vite | PASS trên source cuối. API image `sha256:7660af2b9dafe31033645698df8a0ad8dd3b7d79a67b616fee6602f0a75cf5a8`; web image `sha256:51e508654006883a940a8f9ab59ff775523b37d27c0876ad65266414eeea0a66`. |
| `ManagerLinkRenewalIntegrationTest` trên PostgreSQL/Redis Testcontainers riêng | PASS trên API image cuối (`capmoney-cm02-verification-20261016-newdaemon`, exit 0). Bao gồm migration, link còn hạn/hết hạn, CSRF HTTP, mail từ chối/timeout, proof thử lại, cooldown, hai request đồng thời, link mới hoàn tất một manager, link cũ/luồng sau hoàn tất bị từ chối. |
| Browser mock API: Chromium 393×851, WebKit 390×844 | PASS theo [script](browser-regression.cjs): form link còn hạn, xin lại link hết hạn, kết quả gửi chưa rõ không tự retry, URL fragment được xóa, đường dẫn trực tiếp không có token không hiện nút gửi. |
| Hồi quy `SetupHttpIntegrationTest` + `ManagerLinkRenewalIntegrationTest` trong lượt gộp | PASS cho hai class; `BootstrapIntegrationTest` cùng lượt lỗi hạ tầng khi hai CLI JVM không kết nối được PostgreSQL trong timeout 1 giây. Chạy riêng `BootstrapIntegrationTest` sau đó **PASS**. Không gộp hai lượt thành một full suite xanh. |

## Còn cần kiểm chứng

Thiết bị iPhone/Android thật, SMTP/Mailpit thật cho CM-02, hai JVM renew độc lập, lỗi Redis/DB theo thời điểm gửi, và QA độc lập trên build pin chưa chạy. CM-01 #44/#45 vẫn có gate riêng; kết quả này không đóng CM-01 hoặc CM-02 trên Planio.
