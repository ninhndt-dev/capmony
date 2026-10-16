# 13: Chuyển tiền giữa hai ví

**What to build:** Chuyển phí 0 giữa hai ví khác nhau, cho số dư âm; lịch sử chung có một dòng và mỗi ví đổi số dư đúng một lần.

**Blocked by:** 07 – Thêm ví, 10 – Lịch sử chung và lọc theo ví

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Chủ tài khoản chuyển số nguyên VND dương giữa hai ví đang sử dụng khác nhau của mình; ngày không ở tương lai, phí cố định 0, không cần danh mục, ghi chú tùy chọn.
- [ ] Một lần chuyển trừ ví gửi và cộng ví nhận đúng một lần, kể cả khi ví gửi thành âm; số dư đọc lại đúng.
- [ ] Lịch sử chung có một dòng đủ ví gửi → ví nhận và số tiền; lọc ví gửi hoặc ví nhận cũng hiện đúng một dòng ấy.
- [ ] Rời biểu mẫu chuyển tiền trước Lưu rồi quay lại trong cùng phiên có Internet thì biểu mẫu trống; không tạo lần chuyển hoặc đổi số dư hai ví. [R74]

## Comments

- Nguồn: đặc tả tổng thể bước 7, các quyết định R01–R61, [R74](../../../docs/requirements/44-step-9-transfer-round-1.md) và cách chia bước 8 đã được Ninh xác nhận. Kiểm thử qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
