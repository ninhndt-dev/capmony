# 34: Đối chiếu lần lưu chuyển tiền chưa rõ trong phiên

**What to build:** Khi mất mạng sau bấm Lưu lần chuyển mà kết quả chưa rõ, người dùng được tự đối chiếu khi có Internet lại; một lần chuyển không được ghi trùng ở hai ví.

**Blocked by:** 13 – Chuyển tiền giữa hai ví

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Khi mất mạng sau bấm Lưu lần chuyển và chưa biết kết quả, ứng dụng báo chưa rõ, giữ nội dung khi phiên còn mở và không báo thành công sớm.
- [ ] Khi có Internet, ứng dụng tự đối chiếu và chặn lưu lặp đúng lần chuyển đang chờ; người dùng có thể làm việc khác. Khi quay lại, nếu chưa xác định kết quả thì thấy đang kiểm tra, nếu đã xác định thì thấy đã lưu/chưa lưu.
- [ ] Kết quả được báo sau khi xác định được; lịch sử và số dư đọc lại không có lần chuyển trùng hoặc chỉ tác động một ví.

## Comments

- Nguồn: F09, R64 và cách chia bước 8. Kiểm thử qua web, mất/kết nối lại Internet và đọc lại cả hai ví; chưa chọn schema/API/công nghệ.
