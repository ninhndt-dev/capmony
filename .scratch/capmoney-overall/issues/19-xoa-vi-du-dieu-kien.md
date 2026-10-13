# 19: Xóa ví đủ điều kiện

**What to build:** Chỉ ví 0 đồng chưa từng có giao dịch, kể cả khoản thu/chi hoặc lần chuyển đã xóa mềm, mới xóa hẳn; ví khác được giữ.

**Blocked by:** 06 – Tạo ví đầu tiên, 12 – Xóa mềm và khôi phục khoản thu/chi, 15 – Xóa và khôi phục lần chuyển

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Chỉ ví số dư 0 và chưa từng có bất kỳ khoản thu/chi hoặc lần chuyển nào mới xóa vĩnh viễn được.
- [ ] Giao dịch đã xóa mềm vẫn tính là từng có; ví đó không được xóa.
- [ ] Ví được xóa không còn trong danh sách sau tải lại; lịch sử của ví khác không bị ảnh hưởng.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
