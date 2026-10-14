# CM-01.3: Đăng nhập và giữ phiên quản lý

**Parent:** [CM-01 / Planio #8](https://tanninh.planio.com/issues/8)
**Planio child:** chờ tạo sau khi có API key; không tự đặt ID.
**Status:** draft, chờ đồng bộ Planio và review readiness.
**Blocked by:** `HARD` CM-01.2 – hoàn tất thiết lập một quản lý.
**Integration blocked by:** None sau khi CM-01.2 xong.
**Owner / review:** CapmmDev thực hiện; CapmmBA rà AC, reviewer code độc lập và CapmmPM rà trước QA.

## What it delivers

Ninh đăng nhập bằng email quản lý và mật khẩu đã đặt, tải lại vẫn vào khu vực quản lý tối thiểu, và đăng xuất thu hồi phiên. Khách, thông tin sai, phiên hết hạn/thu hồi hoặc gọi URL trực tiếp không xem được nội dung quản lý; không có đăng ký công khai.

## Acceptance criteria tham chiếu

- [ ] `AC3` trong [spec CM-01](../spec.md).
- [ ] `AC4` trong [spec CM-01](../spec.md), phần quyền quản lý ở server và không có đường tạo quản lý khác.

## Hợp đồng và dữ liệu thử

- Theo [technical-design](../technical-design.md) mục 5–6: email/mật khẩu kiểm ở server, lỗi chung; giới hạn 10 yêu cầu/IP/cửa sổ 15 phút và 60/tài khoản, counter DB nguyên tử, IP tin cậy/Origin fail-closed. Session token ngẫu nhiên chỉ lưu hash, cookie `HttpOnly`, `SameSite=Lax`, host-only, `Secure` khi HTTPS; quyền kiểm trên mọi route/action, không chỉ UI. Logout thu hồi ở DB.
- Dữ liệu thử: một manager từ CM-01.2; mật khẩu đúng/sai, email sai, hai tiến trình ứng dụng cùng DB, phiên còn hạn/hết hạn/bị thu hồi, Origin thiếu/sai, lỗi DB throttle. Không dùng tài khoản thật.
- Demo: đăng nhập, tải lại/mở URL trực tiếp, đăng xuất; khách và phiên cũ bị chặn. Quota **đếm cả lần đăng nhập thành công**; vượt ngưỡng trả thời gian thử lại nhưng phiên đã mở còn dùng. Nếu bị thử sai liên tục, Ninh có thể bị cản qua nhiều cửa sổ; không có `/signup`.

## Rủi ro và kiểm chứng

- Kiểm thử nhiều tiến trình thật và cấu hình proxy/IP của môi trường chạy; spike Tech mới kiểm hai cổng cùng một tiến trình. Nếu đổi host/proxy phải Tech review lại ADR. Khi #04 mở nhiều tài khoản/NAT chung phải rà quota.
- Review theo [test-spec](../test-spec.md) T10–T15. Cần lưu commit/build, lệnh test và kết quả thật; chưa có kết quả QA.
