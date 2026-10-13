# 18: Ngừng ví 0 có lịch sử, bảo vệ số dư và quay lại ghi thu/chi

**What to build:** Ví đã có lịch sử nhưng số dư 0 được ngừng và vẫn xem lịch sử. Sửa/xóa/khôi phục thu/chi hoặc lần chuyển mà làm ví ngừng khác 0 bị chặn, yêu cầu bật lại trước; các thao tác không đổi số dư vẫn được phép. Khi mọi ví đã ngừng, luồng thu/chi dẫn tạo/bật lại ví rồi quay về biểu mẫu.

**Blocked by:** 10 – Lịch sử chung và lọc theo ví, 11 – Sửa khoản thu/chi, 12 – Xóa mềm và khôi phục khoản thu/chi, 14 – Sửa lần chuyển, 15 – Xóa và khôi phục lần chuyển, 17 – Ngừng và bật lại ví trống

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Ví có lịch sử nhưng số dư hiện tại 0 được ngừng và vẫn xem được lịch sử; ví ngừng luôn giữ số dư 0.
- [ ] Sửa, xóa hoặc khôi phục khoản thu/chi hay lần chuyển mà làm ví ngừng khác 0 bị chặn và yêu cầu bật lại; sửa thông tin không đổi số dư vẫn được phép.
- [ ] Khi mọi ví đã ngừng, nút ghi thu/chi dẫn tạo hoặc bật lại ví, rồi quay về biểu mẫu ghi chép.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
