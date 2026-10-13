# 31: Chặn sửa/xóa/khôi phục khoản thu/chi từ bản cũ

**What to build:** Trên hai điện thoại, thao tác từ bản cũ của cùng khoản thu/chi bị chặn, giữ phần vừa nhập để tự đối chiếu, không ghi đè bản mới.

**Blocked by:** 11 – Sửa khoản thu/chi, 12 – Xóa mềm và khôi phục khoản thu/chi

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Sửa, xóa hoặc khôi phục khoản thu/chi từ bản cũ trên điện thoại thứ hai bị chặn và báo dữ liệu đã thay đổi; bản mới không bị ghi đè.
- [ ] Phần người dùng vừa nhập được giữ để tự so sánh với bản mới, nhập lại hoặc bỏ; ứng dụng không tự hợp nhất.
- [ ] Số dư và lịch sử sau thử thao tác từ bản cũ vẫn đúng theo bản đã lưu mới nhất.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
