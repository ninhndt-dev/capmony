# Bước 5, vòng 4: chuyển tiền, xóa và quay lại luồng ghi chép

**Trạng thái:** Đã trả lời ngày 13/10/2026. BA, Tech và PM đã rà soát trước khi hỏi; Ninh chọn A cho Q1–Q4.

**Phụ thuộc đã chốt:** [Phiếu luồng người dùng](user-flows.md), [vòng 3 bước 5](28-step-5-user-flows-round-3.md) và [nghiệp vụ chính](core-business.md).

❓ **Q1 – Chuyển tiền trong lịch sử chung:** Một lần chuyển tiền giữa hai ví nên hiện thế nào trong lịch sử chung?

➡️ **A (Bờm đề xuất):** Một dòng chuyển tiền, ghi ví gửi, ví nhận và số tiền. **B:** Hai dòng hiển thị tiền ra và tiền vào, cùng dẫn tới chi tiết của một lần chuyển liên kết. Cả hai cách đều chỉ có một lần chuyển để sửa, xóa hoặc khôi phục; số dư mỗi ví chỉ đổi một lần.

---

❓ **Q2 – Thoát khi tạo ví đầu tiên:** Người dùng đang nhập ví đầu tiên, chuyển sang trang khác trước khi bấm lưu rồi quay lại trong cùng phiên ứng dụng có Internet. Nội dung nhập dở nên thế nào?

➡️ **A (Bờm đề xuất):** Giữ nội dung nhập dở để tiếp tục; chưa tạo ví cho đến khi bấm lưu thành công. **B:** Mở lại biểu mẫu trống; chưa tạo ví vì lần trước chưa lưu.

---

❓ **Q3 – Xóa một giao dịch:** Khi người dùng chọn xóa một khoản thu, chi hoặc lần chuyển tiền và thao tác xóa được phép theo quy tắc ví/ngăn bản cũ đã chốt, ứng dụng nên làm gì trước khi đưa vào mục đã xóa và cập nhật số dư?

➡️ **A (Bờm đề xuất):** Hiện xác nhận xóa, rồi xóa cả lần chuyển nếu đó là chuyển tiền. **B:** Xóa ngay và hiện cách khôi phục; lần chuyển vẫn xóa như một đơn vị. Cả hai cách đều cho khôi phục về sau theo quy tắc đã chốt.

---

❓ **Q4 – Ghi giao dịch khi mọi ví đã ngừng:** Nếu người dùng đã có ví nhưng tất cả đều ngừng sử dụng, rồi bấm nút ghi thu/chi chung, ứng dụng nên dẫn họ đi đâu? Trường hợp chưa từng có ví đã chốt ở F02: dẫn tạo ví đầu tiên.

➡️ **A (Bờm đề xuất):** Dẫn tới tạo ví mới hoặc bật dùng lại ví đã ngừng; nếu hoàn tất, quay về ghi thu/chi. **B:** Báo cần có ví đang sử dụng và đưa về danh sách ví để người dùng tự chọn bước tiếp theo.

## Nhật ký trả lời

Ninh trả lời **“A cả”**, được hiểu là chọn A cho Q1–Q4. Các lựa chọn này đã được chuyển vào [phiếu luồng người dùng](user-flows.md).

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Lịch sử chung hiện một dòng cho một lần chuyển tiền, ghi ví gửi, ví nhận và số tiền; lần chuyển vẫn là một đơn vị khi sửa, xóa, khôi phục. | Cách hiện trong lịch sử riêng từng ví chưa chốt. |
| Q2 | Khi chuyển sang trang khác trước khi lưu ví đầu tiên rồi quay lại trong cùng phiên có Internet, giữ nội dung nhập dở; ví chỉ được tạo sau khi lưu thành công. | Đóng hẳn trình duyệt khi chưa lưu chưa chốt. |
| Q3 | Trước khi xóa một khoản thu, chi hoặc lần chuyển tiền được phép xóa, hiện xác nhận; xóa lần chuyển như một đơn vị. | Cách dẫn khi bị chặn bởi ví ngừng/bản cũ chưa mô tả ở bước 5. |
| Q4 | Khi tất cả ví đã có đều ngừng sử dụng và người dùng bấm ghi thu/chi chung, dẫn tạo ví mới hoặc bật lại ví đã ngừng; nếu hoàn tất, quay về ghi thu/chi. | Trường hợp chưa từng có ví đã ở F02. |

## Phụ thuộc cho vòng sau

- Sau Q1, còn cần hỏi cách hiển thị lần chuyển ở lịch sử riêng từng ví; cách trình bày không được làm đếm đôi số dư.
- Sau Q2, xác định chuyện gì xảy ra nếu người dùng đóng hẳn trình duyệt khi chưa lưu; không suy ra khả năng dùng offline.
- Sau Q3, mô tả khôi phục khi ví liên quan đã ngừng hoặc khi cùng giao dịch thay đổi ở điện thoại khác, theo B13 và B30.
- Sau Q4, tổng hợp luồng ghi thu/chi, chuyển tiền, quản lý ví và lỗi/phục hồi để BA, Tech, PM rà soát trước khi kết thúc bước 5.
