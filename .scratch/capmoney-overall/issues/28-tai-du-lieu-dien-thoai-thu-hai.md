# 28: Tải dữ liệu trên điện thoại thứ hai

**What to build:** Cùng tài khoản trên hai điện thoại: quay lại khi có Internet thì tự thấy dữ liệu mới trong lịch sử, vẫn giữ phần giao dịch đang nhập dở trong phiên.

**Blocked by:** 10 – Lịch sử chung và lọc theo ví

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Cùng tài khoản trên hai điện thoại: khi quay lại ứng dụng có Internet, điện thoại thứ hai tự thấy giao dịch đã lưu từ điện thoại thứ nhất trong lịch sử.
- [ ] Phần giao dịch đang nhập dở ở điện thoại thứ hai vẫn được giữ trong phiên khi dữ liệu mới tải về.
- [ ] Tài khoản khác không nhận dữ liệu đó; sau tải lại trang số dư và lịch sử đã lưu vẫn đúng.

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61 và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
