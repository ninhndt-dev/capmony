# 12: Xóa mềm và khôi phục khoản thu/chi

**What to build:** Xác nhận xóa, xem mục đã xóa, khôi phục bất cứ lúc nào; số dư được tính lại, giao dịch đã xóa phải khôi phục trước khi sửa.

**Blocked by:** 10 – Lịch sử chung và lọc theo ví, 11 – Sửa khoản thu/chi

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Trước khi xóa khoản thu/chi, ứng dụng hỏi xác nhận; bản đã xóa chuyển sang mục riêng và ngừng tác động số dư.
- [ ] Khôi phục không hạn thời gian làm giao dịch ảnh hưởng số dư trở lại; chưa có xóa vĩnh viễn từng giao dịch.
- [ ] Bản đã xóa phải khôi phục trước khi sửa; số dư và lịch sử đọc lại đúng sau từng thao tác.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
