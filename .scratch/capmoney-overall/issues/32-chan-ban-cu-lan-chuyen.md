# 32: Chặn sửa/xóa/khôi phục lần chuyển từ bản cũ

**What to build:** Áp dụng quy tắc bản cũ cho lần chuyển như một đơn vị; dữ liệu mới và số dư hai ví được giữ.

**Blocked by:** 14 – Sửa lần chuyển, 15 – Xóa và khôi phục lần chuyển

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Sửa, xóa hoặc khôi phục lần chuyển từ bản cũ bị chặn như một đơn vị; cả hai ví giữ số dư theo bản mới nhất.
- [ ] Phần vừa nhập được giữ để người dùng tự đối chiếu, nhập lại hoặc bỏ; không tự ghi đè hay hợp nhất.
- [ ] Sau tải lại, lịch sử và số dư hai ví vẫn đúng, không có nửa lần chuyển.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
