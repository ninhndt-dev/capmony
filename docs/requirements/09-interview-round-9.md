# Vòng hỏi 9: giữ lịch sử và bảo vệ dữ liệu

**Trạng thái:** Đã trả lời ngày 12/10/2026. Các câu dựa trên ví, danh mục, giao dịch và xóa/khôi phục đã được xác nhận. Phương án A là đề xuất Bờm nêu khi hỏi; nhật ký dưới đây ghi riêng lựa chọn của Ninh.

❓ **Q1 – Ví đã có giao dịch:** Khi không dùng một ví nữa, có nên cho ngừng sử dụng ví nhưng giữ các giao dịch và số dư lịch sử không? Hay ví vẫn phải nằm trong danh sách dùng?

➡️ **A (Bờm đề xuất):** Ngừng sử dụng ví, giữ lịch sử. **B:** Giữ ví trong danh sách dùng.

---

❓ **Q2 – Danh mục đã được dùng:** Khi không cần một danh mục nữa, có nên ẩn danh mục khỏi lựa chọn mới nhưng giữ danh mục trên các giao dịch cũ không? Hay danh mục vẫn nằm trong danh sách chọn mới?

➡️ **A (Bờm đề xuất):** Ẩn khỏi lựa chọn mới, giữ lịch sử. **B:** Giữ danh mục trong danh sách chọn mới.

---

❓ **Q3 – Giao dịch đã xóa:** Giao dịch đã xóa có nên ẩn khỏi lịch sử thông thường và xuất hiện trong mục riêng để khôi phục không?

➡️ **A (Bờm đề xuất):** Có mục riêng để khôi phục. **B:** Vẫn hiện trong lịch sử thông thường với nhãn đã xóa.

---

❓ **Q4 – Bản chất chuyển tiền:** Một lần chuyển giữa hai ví có nên là một thao tác liên kết, cùng lúc trừ ví gửi và cộng ví nhận, thay vì hai khoản thu/chi độc lập không?

➡️ **A (Bờm đề xuất):** Một thao tác chuyển liên kết hai ví. **B:** Hai khoản thu/chi độc lập.

---

❓ **Q5 – Dữ liệu khi có sự cố:** Nếu hệ thống gặp sự cố, Ninh cần có bản sao lưu để khôi phục dữ liệu tài chính của người dùng, đúng không?

➡️ **A (Bờm đề xuất):** Cần khả năng khôi phục từ bản sao lưu. **B:** Chưa cần trong bản đầu.

## Nhật ký trả lời

Ninh trả lời theo thứ tự: **A, B, A, A, B**.

| Câu | Ninh đã xác nhận | Đề xuất chưa được xác nhận / điều chưa rõ |
| --- | --- | --- |
| Q1 | A — ngừng sử dụng ví đã có giao dịch và giữ lịch sử | Chưa rõ ví phải về số dư 0 trước khi ngừng dùng và có thể kích hoạt lại không |
| Q2 | B — danh mục đã dùng vẫn chọn được cho giao dịch mới | Chưa rõ đổi tên danh mục tác động thế nào đến giao dịch cũ |
| Q3 | A — giao dịch đã xóa nằm trong mục riêng để khôi phục | Đã xác nhận ngừng ảnh hưởng số dư ở R33; chưa rõ các bộ lọc của mục riêng |
| Q4 | A — một thao tác chuyển tiền liên kết hai ví và cập nhật cả hai số dư | Chưa rõ xóa/khôi phục thao tác chuyển tiền có được phép không |
| Q5 | B — bản đầu chưa yêu cầu bản sao lưu để khôi phục khi hệ thống gặp sự cố | Không thay đổi R27: đăng nhập trên điện thoại khác vẫn thấy dữ liệu đã ghi; chưa chốt cách lưu dữ liệu |

BA đã chuyển các lựa chọn vào [phiếu tiếp nhận](00-intake.md). Các điểm phụ thuộc được hỏi ở [vòng 10](10-interview-round-10.md).
