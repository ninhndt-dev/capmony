# 06: Tạo ví đầu tiên

**What to build:** Người chưa có ví được dẫn tạo ví; bỏ trống số dư đầu thì dùng 0 VND. Rời trang rồi quay lại cùng phiên giữ bản nhập dở; đóng trình duyệt trước khi bấm Lưu thì mở lại biểu mẫu trống.

**Blocked by:** 04 – Nhận lời mời và đăng nhập cá nhân

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Khi chưa có ví, luồng ghi chép dẫn người dùng tạo ví đầu tiên; bỏ trống số dư ban đầu dùng 0 VND.
- [ ] Số dư ban đầu nhập vào là số nguyên VND, có thể âm; chỉ chủ tài khoản đọc lại được ví.
- [ ] Rời trang rồi quay lại cùng phiên giữ bản nhập dở; đóng trình duyệt trước khi bấm Lưu thì mở lại biểu mẫu trống.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
