# 20: Thêm và đổi tên danh mục

**What to build:** Người dùng thêm danh mục của mình; đổi tên thì giao dịch cũ hiện tên mới và vẫn chọn được.

**Blocked by:** 08 – Danh mục mặc định và ghi khoản thu, 10 – Lịch sử chung và lọc theo ví

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Thêm hoặc đổi tên danh mục thành tên trống không Lưu và không đổi dữ liệu đã lưu. [R78]

- [ ] Chủ tài khoản thêm danh mục riêng và chọn được cho khoản thu/chi mới.
- [ ] Đổi tên danh mục đã dùng làm giao dịch cũ hiển thị tên mới; danh mục đó vẫn chọn được.
- [ ] Tài khoản khác không thấy hoặc sửa danh mục của người này.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
