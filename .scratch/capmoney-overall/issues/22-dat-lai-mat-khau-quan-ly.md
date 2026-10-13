# 22: Đặt lại mật khẩu quản lý

**What to build:** Ninh còn dùng email quản lý tự đặt lại qua liên kết riêng 30 phút; mọi phiên quản lý cũ kết thúc.

**Blocked by:** 01 – Thiết lập và đăng nhập quản lý

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Ninh còn dùng email quản lý đã xác minh nhận được liên kết đặt lại mật khẩu riêng, hết hạn 30 phút từ lúc gửi.
- [ ] Liên kết hết hạn không đặt lại được; trên màn hình báo hết hạn Ninh tự xin liên kết mới chỉ gửi tới email quản lý đã xác minh, không tạo tài khoản quản lý khác.
- [ ] Đặt lại thành công kết thúc mọi phiên quản lý cũ; Ninh đăng nhập bằng mật khẩu mới.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
