# 25: Đổi email ghi chép

**What to build:** Chủ tài khoản đang đăng nhập đổi email sau xác minh email mới; phiên cũ kết thúc, dữ liệu giữ nguyên.

**Blocked by:** 08 – Danh mục mặc định và ghi khoản thu

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Chủ tài khoản đang đăng nhập xác minh email mới trước khi email đăng nhập đổi có hiệu lực.
- [ ] Sau khi đổi, mọi phiên cũ kết thúc; đăng nhập bằng email mới thấy nguyên dữ liệu của mình.
- [ ] Tài khoản khác không được xem hoặc sửa dữ liệu tài chính của người đổi email.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
