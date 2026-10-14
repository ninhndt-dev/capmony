# 01: Thiết lập và đăng nhập quản lý

**What to build:** Ninh dùng liên kết thiết lập một lần còn hạn gửi đến email đã khai, xác minh email, đặt mật khẩu và đăng nhập tài khoản quản lý riêng.

**Blocked by:** None (can start immediately)

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Liên kết thiết lập chỉ gửi tới email quản lý Ninh đã khai; dùng một lần và hết hạn sau 3 ngày.
- [ ] Ninh xác minh email, đặt mật khẩu và đăng nhập được tài khoản quản lý riêng.
- [ ] Không tạo thêm tài khoản quản lý từ liên kết đã dùng hoặc email khác.
- [ ] Sau đăng nhập hoặc tải lại, chỉ phiên quản lý hợp lệ mở được khu vực tài khoản quản lý; người chưa đăng nhập bị từ chối ngay cả khi dùng đường dẫn trực tiếp.
- [ ] Hai lần hoàn tất cùng liên kết gần đồng thời hoặc bấm xác nhận lặp chỉ có tối đa một lần thành công và một tài khoản quản lý tồn tại.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
- Đặc tả chi tiết bước 9: [Thiết lập và đăng nhập tài khoản quản lý](../../capmoney-manager-setup/spec.md). Trạng thái ticket giữ `needs-triage` tới khi Tech chốt các quyết định thực thi và PM kiểm tra lại.
- Rà soát BA → PM → Tech → PM ngày 14/10/2026: hành vi khớp F11/F23. Better Auth chưa được chọn vì cần chứng minh luồng chỉ mời và thiết lập một quản lý; xem [đối chiếu kỹ thuật](../../capmoney-manager-setup/technical-options.md). Chưa chuyển `ready-for-agent`.
