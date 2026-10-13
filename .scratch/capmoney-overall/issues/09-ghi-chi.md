# 09: Ghi khoản chi

**What to build:** Ghi khoản chi hợp lệ, kể cả khi số dư thành âm; số dư đọc lại theo B01.

**Blocked by:** 08 – Danh mục mặc định và ghi khoản thu

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Ghi khoản chi hợp lệ bằng chính danh sách danh mục dùng chung cho thu/chi của ticket 08, ngày và số nguyên VND dương; ghi chú có thể để trống.
- [ ] Số dư ví đọc lại giảm đúng, được phép âm; ngày tương lai và số tiền không dương bị từ chối.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
