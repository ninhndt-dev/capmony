# 11: Sửa khoản thu/chi

**What to build:** Sửa các trường đã chốt và đọc lại số dư các ví bị ảnh hưởng; chặn ngày tương lai.

**Blocked by:** 10 – Lịch sử chung và lọc theo ví

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Chủ tài khoản sửa được loại thu/chi, ngày, số tiền, ví, danh mục và ghi chú của giao dịch còn hiệu lực.
- [ ] Ngày tương lai, số tiền không dương hoặc đổi sang ví không thuộc mình bị từ chối.
- [ ] Sau sửa giao dịch cũ, số dư của mọi ví liên quan và lịch sử đọc lại đúng theo B01.
- [ ] Nếu sửa giao dịch nhưng ngày giao dịch không đổi, vị trí của bản đó trong nhóm cùng ngày không đổi theo R59.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
