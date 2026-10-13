# Bước 5, vòng 3: vào luồng ghi chép hằng ngày

**Trạng thái:** Đã trả lời ngày 13/10/2026. BA, Tech và PM đã rà soát trước khi hỏi; Ninh chọn A cho Q1–Q4.

**Phụ thuộc đã chốt:** [Phiếu tiếp nhận](00-intake.md), [nghiệp vụ chính](core-business.md), [luồng người dùng](user-flows.md).

❓ **Q1 – Bỏ trống số dư ban đầu:** Khi tạo ví đầu tiên mà không nhập số dư ban đầu, ứng dụng xử lý thế nào?

➡️ **A (Bờm đề xuất):** Tạo ví với số dư ban đầu 0 VND; sau này vẫn sửa được. **B:** Trước khi tạo ví, hỏi người dùng xác nhận dùng 0 VND cho ô bỏ trống; không cần họ nhập lại số và sau này vẫn sửa được.

---

❓ **Q2 – Mở màn hình ghi thu/chi:** Trên điện thoại, người dùng muốn ghi một khoản thu hoặc chi có thể bắt đầu từ đâu?

➡️ **A (Bờm đề xuất):** Từ nút ghi giao dịch chung hoặc từ một ví đang sử dụng; nếu vào từ ví thì ví đó được chọn sẵn và có thể đổi sang ví đang sử dụng khác. **B:** Chỉ bắt đầu từ từng ví đang sử dụng, ví đó được chọn sẵn và có thể đổi sang ví đang sử dụng khác.

---

❓ **Q3 – Xem lịch sử của nhiều ví:** Khi có nhiều ví, người dùng xem lịch sử giao dịch theo phạm vi nào?

➡️ **A (Bờm đề xuất):** Có lịch sử chung của tất cả ví và có thể lọc theo từng ví. **B:** Chỉ xem lịch sử riêng trong từng ví, không có danh sách chung.

---

❓ **Q4 – Thứ tự lịch sử khi ghi bù ngày cũ:** Giao dịch có thể được ghi cho ngày đã qua. Danh sách lịch sử nên sắp xếp theo thứ tự nào? Ngày giao dịch vẫn hiển thị là ngày người dùng đã chọn.

➡️ **A (Bờm đề xuất):** Ngày giao dịch do người dùng chọn, mới nhất lên đầu. **B:** Thời điểm người dùng nhập vào ứng dụng, mới nhập lên đầu dù ngày giao dịch ở quá khứ.

## Nhật ký trả lời

Ninh trả lời **“A hếta”**, được hiểu là “A hết” cho Q1–Q4. Các lựa chọn này đã được chuyển vào [phiếu luồng người dùng](user-flows.md).

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Tạo ví không nhập số dư ban đầu thì dùng 0 VND; sau này vẫn sửa được. | Khi thoát giữa bước tạo ví đầu tiên sẽ hỏi tiếp. |
| Q2 | Có thể mở luồng ghi thu/chi từ nút chung hoặc từ ví đang sử dụng; vào từ ví thì chọn sẵn ví đó và có thể đổi sang ví đang sử dụng khác. | Chi tiết thao tác lưu và lỗi của biểu mẫu còn mở. |
| Q3 | Có lịch sử chung của tất cả ví và có thể lọc theo từng ví. | Cách hiện một lần chuyển trong lịch sử chung sẽ hỏi tiếp. |
| Q4 | Lịch sử sắp xếp theo ngày giao dịch do người dùng chọn, mới nhất lên đầu; ngày giao dịch vẫn là ngày người dùng chọn. | Thứ tự khi nhiều giao dịch cùng ngày chưa chốt. |

## Phụ thuộc cho vòng sau

- Sau Q1, mô tả đường tạo ví đầu tiên khi người dùng thoát giữa chừng.
- Sau Q2–Q4, mô tả luồng tạo, sửa và xem giao dịch theo ví hoặc trên lịch sử chung; nếu Q3 là A, hỏi tiếp cách thể hiện lần chuyển tiền trên danh sách chung. Tránh hỏi lại quy tắc số dư và ngày đã chốt ở bước 4.
- Các luồng xóa/khôi phục giao dịch, ngừng/bật lại ví và xử lý lỗi còn cần được tổng hợp trước khi kết thúc bước 5.
- Chi tiết nút lưu lặp, rời màn hình khi kết quả lưu chưa rõ, thông báo trên điện thoại khác sau xóa tài khoản, thời hạn liên kết thiết lập tài khoản quản lý và cách xin lại liên kết đặt lại mật khẩu vẫn chưa chốt; có thể xử lý ở vòng sau nếu ảnh hưởng luồng chính.
