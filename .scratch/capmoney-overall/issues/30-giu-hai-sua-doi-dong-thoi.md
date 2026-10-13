# 30: Giữ hai giao dịch khác nhau được sửa gần đồng thời

**What to build:** Hai điện thoại sửa hai giao dịch khác nhau, gồm thu/chi và lần chuyển, gần đồng thời; cả hai thay đổi đã báo lưu đều được giữ và số dư đúng.

**Blocked by:** 11 – Sửa khoản thu/chi, 14 – Sửa lần chuyển

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Hai điện thoại sửa hai giao dịch khác nhau gần đồng thời, gồm thu/chi và lần chuyển; cả hai thay đổi đã báo lưu đều đọc lại được.
- [ ] Số dư các ví phản ánh cả hai thay đổi sau tải lại, không ghi đè thay đổi độc lập.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
