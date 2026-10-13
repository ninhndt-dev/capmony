# 26: Hỗ trợ người mất email

**What to build:** Theo yêu cầu của người đã đăng xuất và mất email cũ, Ninh dùng tài khoản quản lý tra đúng email cũ, xác minh đúng người qua cuộc gọi lại số đã biết, rồi đổi sang email mới đã xác minh mà không xem dữ liệu tài chính.

**Blocked by:** 01 – Thiết lập và đăng nhập quản lý, 04 – Nhận lời mời và đăng nhập cá nhân, 08 – Danh mục mặc định và ghi khoản thu

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Ninh chỉ tra đúng email cũ theo yêu cầu của người mất email; trước khi đổi, Ninh gọi lại số đã lưu từ trước ngoài CapMoney, nhận diện chắc người quen và xác nhận chính người đó yêu cầu.
- [ ] Nếu không có số đã biết trước hoặc Ninh không chắc đúng người, email đăng nhập không đổi. Email mới phải được xác minh trước khi đổi có hiệu lực.
- [ ] Đổi thành công kết thúc các phiên cũ; người dùng đăng nhập lại bằng email mới và thấy nguyên dữ liệu của mình, còn tài khoản quản lý không xem được dữ liệu tài chính.

## Comments

- Nguồn: F10, R62, quyền tài khoản quản lý ở bước 3 và cách chia bước 8. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
