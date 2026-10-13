# 10: Lịch sử chung và lọc theo ví

**What to build:** Xem thu/chi theo ngày mới nhất, lọc ví, xem số tiền và số dư hiện tại.

**Blocked by:** 08 – Danh mục mặc định và ghi khoản thu, 09 – Ghi khoản chi

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Lịch sử chung hiển thị khoản thu/chi của mọi ví; lọc một ví chỉ hiện giao dịch thuộc ví đó.
- [ ] Ngày giao dịch mới nhất lên trước; cùng ngày thì bản được tạo sau lên trước. Quy tắc vị trí sau khi sửa được kiểm chứng ở ticket 11.
- [ ] Mỗi dòng hiện số tiền; số dư hiện tại của ví được đọc lại đúng, chưa cần số dư sau từng dòng.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
