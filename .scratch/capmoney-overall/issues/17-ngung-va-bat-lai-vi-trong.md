# 17: Ngừng và bật lại ví trống

**What to build:** Ví chưa có giao dịch, số dư 0 được ngừng và bật lại; ví ngừng không nhận giao dịch mới hoặc nhận giao dịch cũ khi sửa để đổi ví. Sửa số dư đầu làm ví ngừng khác 0 cũng bị chặn; người dùng phải bật lại trước.

**Blocked by:** 08 – Danh mục mặc định và ghi khoản thu, 11 – Sửa khoản thu/chi, 14 – Sửa lần chuyển, 16 – Sửa thông tin ví

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Ví chưa từng có giao dịch và có số dư 0 được ngừng, sau đó bật lại được; ví còn số dư khác 0 không được ngừng.
- [ ] Ví ngừng không được chọn cho giao dịch mới hoặc làm ví đích khi sửa thu/chi, chuyển tiền; muốn dùng lại phải bật trước.
- [ ] Sửa số dư ban đầu khiến ví ngừng khác 0 bị chặn và yêu cầu bật lại trước.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
