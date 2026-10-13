# 21: Xóa danh mục chưa dùng

**What to build:** Danh mục chưa từng dùng, kể cả danh mục mặc định, được xóa; danh mục đã dùng không xóa được.

**Blocked by:** 08 – Danh mục mặc định và ghi khoản thu, 20 – Thêm và đổi tên danh mục

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Danh mục chưa từng gắn vào giao dịch, kể cả danh mục mặc định, được xóa hẳn khỏi danh sách riêng của người dùng.
- [ ] Danh mục đã dùng không bị xóa và vẫn chọn được cho giao dịch mới.
- [ ] Lịch sử giao dịch cũ giữ đúng danh mục đã dùng sau tải lại.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
