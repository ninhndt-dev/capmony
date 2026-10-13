# 14: Sửa lần chuyển

**What to build:** Sửa ngày, số tiền, hai ví hoặc ghi chú của một lần chuyển như một đơn vị; số dư hai ví đọc lại đúng.

**Blocked by:** 13 – Chuyển tiền giữa hai ví

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Chủ tài khoản sửa được ngày, số tiền, ví gửi, ví nhận và ghi chú của lần chuyển còn hiệu lực như một đơn vị.
- [ ] Hai ví phải khác nhau, đang sử dụng và thuộc cùng chủ; ngày tương lai hoặc số tiền không dương bị từ chối.
- [ ] Số dư các ví liên quan, kể cả ví cũ và mới, được đọc lại đúng sau sửa.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
