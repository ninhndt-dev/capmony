# 35: Đối chiếu lần lưu giao dịch chưa rõ sau khi mở lại

**What to build:** Sau khi đóng trình duyệt với lần lưu thu/chi hoặc chuyển tiền chưa rõ, người dùng mở lại bằng cùng tài khoản sẽ thấy trạng thái chờ và kết quả được tự đối chiếu mà không tạo giao dịch trùng.

**Blocked by:** 33 – Đối chiếu lần lưu thu/chi chưa rõ trong phiên, 34 – Đối chiếu lần lưu chuyển tiền chưa rõ trong phiên

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Nếu đã bấm Lưu thu/chi hoặc lần chuyển rồi đóng trình duyệt khi kết quả chưa rõ, mở lại bằng cùng tài khoản có Internet thì ứng dụng tự đối chiếu; nếu vẫn chưa rõ thì thấy đang kiểm tra, nếu đã xác định thì thấy kết quả đã lưu/chưa lưu.
- [ ] Nếu chắc chắn đã lưu, chỉ báo thành công và đọc lại giao dịch một lần; nếu chắc chắn chưa lưu, báo chưa lưu và mở biểu mẫu trống để nhập lại.
- [ ] Nếu vẫn chưa xác định được, tiếp tục báo chưa rõ, cho làm việc khác khi có Internet và chặn thử lại đúng thao tác đang chờ; số dư không bị nhân đôi ở bất kỳ ví nào.

## Comments

- Nguồn: F25, R64 và cách chia bước 8. Kiểm thử qua đóng/mở trình duyệt, đọc lại lịch sử và số dư; chưa chọn schema/API/công nghệ.
