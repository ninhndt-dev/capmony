# 15: Xóa và khôi phục lần chuyển

**What to build:** Xóa hoặc khôi phục cả lần chuyển, tác động hai ví như một đơn vị và giữ một dòng lịch sử khi còn hiệu lực.

**Blocked by:** 13 – Chuyển tiền giữa hai ví, 14 – Sửa lần chuyển

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Trước khi xóa lần chuyển, ứng dụng hỏi xác nhận; lần chuyển được xóa như một đơn vị và ngừng tác động cả hai ví.
- [ ] Mục đã xóa giữ lần chuyển để khôi phục không hạn thời gian; khôi phục tác động lại cả hai ví một lần.
- [ ] Không sửa lần chuyển đã xóa trước khi khôi phục; lịch sử và số dư đọc lại đúng.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
