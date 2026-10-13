# Bước 8, vòng làm rõ 1: lịch sử và liên kết quản lý

**Trạng thái:** Đã trả lời ngày 13/10/2026. Ninh chọn A cho Q1–Q3. Cách chia 35 ticket đã được Ninh duyệt; từ 12 ticket trước vòng này, nay có 30 ticket đủ rõ tại [tracker](../../.scratch/capmoney-overall/issues/). PM đã rà các phương án của vòng này. Những câu trả lời này làm rõ đặc tả/ticket liên quan, không thay đổi phạm vi bản đầu.

**Nguồn:** [Đặc tả tổng thể](../../.scratch/capmoney-overall/spec.md), [luồng người dùng](user-flows.md), [bản chia ticket](35-step-8-ticket-breakdown.md).

❓ **Q1 – Thứ tự trong cùng một ngày:** Lịch sử đã chốt ngày mới nhất lên đầu. Nếu có nhiều giao dịch cùng ngày, chúng nên xếp thế nào?

➡️ **A (Bờm đề xuất):** Giao dịch được tạo sau đứng trước; sửa giao dịch không đổi vị trí trong ngày. **B:** Giao dịch được sửa/lưu gần nhất đứng trước.

---

❓ **Q2 – Lọc lịch sử theo ví:** Một lần chuyển tiền giữa hai ví của cùng người nên xuất hiện thế nào khi lọc từng ví?

➡️ **A (Bờm đề xuất):** Khi lọc ví gửi hoặc ví nhận, đều hiện một dòng ghi đủ ví gửi → ví nhận và số tiền; lịch sử chung vẫn một dòng. **B:** Cả hai ví đều hiện lần chuyển, nhưng khi lọc từng ví thì hiện “chuyển đi” hoặc “chuyển đến” theo góc nhìn của ví đó, kèm tên ví còn lại và số tiền; lịch sử chung vẫn một dòng.

---

❓ **Q3 – Liên kết đặt lại mật khẩu quản lý hết hạn:** Sau 30 phút, Ninh còn dùng được email quản lý muốn xin liên kết mới bằng cách nào?

➡️ **A (Bờm đề xuất):** Tự xin liên kết mới ngay trên màn hình báo hết hạn. **B:** Trở về màn hình đăng nhập và bắt đầu lại luồng “Quên mật khẩu” để xin liên kết mới. Với cả hai cách, liên kết mới chỉ gửi tới email quản lý đã xác minh; không đổi email hay tạo tài khoản quản lý khác.

## Nhật ký trả lời

Ninh trả lời **“A hết”**, được ghi là A cho Q1–Q3.

| Câu | Ninh đã xác nhận | Còn mở |
| --- | --- | --- |
| Q1 | Trong cùng ngày, giao dịch được tạo sau đứng trước; sửa giao dịch không đổi vị trí trong ngày. | Cách hiển thị ngày giờ cụ thể chưa quyết định. |
| Q2 | Khi lọc ví gửi hoặc ví nhận, đều hiện một dòng ghi đủ ví gửi → ví nhận và số tiền; lịch sử chung vẫn một dòng cho lần chuyển. | Chi tiết bố cục dòng lịch sử chưa quyết định. |
| Q3 | Khi liên kết đặt lại mật khẩu quản lý hết hạn sau 30 phút, Ninh tự xin liên kết mới ngay trên màn hình báo hết hạn; liên kết mới chỉ gửi tới email quản lý đã xác minh, không đổi email hay tạo tài khoản quản lý khác. | Nội dung thông báo cụ thể chưa quyết định. |
