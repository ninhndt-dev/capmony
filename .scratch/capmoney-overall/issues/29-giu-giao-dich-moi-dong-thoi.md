# 29: Giữ các giao dịch mới lưu gần đồng thời

**What to build:** Hai điện thoại ghi hai giao dịch khác nhau, gồm thu/chi và lần chuyển, gần đồng thời; cả hai khoản đã báo lưu đều được giữ và số dư đúng.

**Blocked by:** 08 – Danh mục mặc định và ghi khoản thu, 13 – Chuyển tiền giữa hai ví

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Hai điện thoại của cùng tài khoản ghi hai giao dịch khác nhau gần đồng thời, gồm thu/chi và lần chuyển; cả hai giao dịch đã báo lưu đều đọc lại được.
- [ ] Số dư các ví phản ánh đủ cả hai giao dịch, không mất hoặc nhân đôi giao dịch đã báo lưu.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
