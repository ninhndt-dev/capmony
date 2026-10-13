# 16: Sửa thông tin ví

**What to build:** Đổi tên, sửa số dư ban đầu kể cả số âm; lịch sử giữ đúng ví và số dư tính lại.

**Blocked by:** 10 – Lịch sử chung và lọc theo ví

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Chủ tài khoản đổi tên ví đã có giao dịch; giao dịch cũ vẫn thuộc cùng ví và hiện tên mới trong lịch sử.
- [ ] Số dư ban đầu sửa được bằng số nguyên VND, kể cả âm; sau sửa số dư hiện tại đọc lại theo B01.
- [ ] Số dư ban đầu không xuất hiện như khoản thu trong lịch sử; tài khoản khác không sửa được ví.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
