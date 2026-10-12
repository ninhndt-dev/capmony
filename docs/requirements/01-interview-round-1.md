# Vòng hỏi 1: khởi động lại

**Trạng thái:** Đã trả lời ngày 12/10/2026. Các câu cùng vòng độc lập; câu hỏi phụ thuộc sang vòng sau.

❓ **Q1 – Giữ đầu vào cũ?** Khi làm lại từ bước tiếp nhận yêu cầu, Ninh có muốn giữ sáu quyết định D01–D06 trong [phiếu tiếp nhận](00-intake.md) không?

➡️ **Bờm đề xuất: Có.** Bỏ code và những quy tắc Bờm tự đề xuất mà Ninh chưa duyệt, giữ yêu cầu Ninh đã trả lời trực tiếp.

---

❓ **Q2 – Vai trò của ảnh CapMoney?** Bộ ảnh iPhone Ninh đã gửi là chuẩn để tái tạo bố cục và hành vi ở MVP, hay chỉ dùng để lấy cảm hứng?

➡️ **Bờm đề xuất: Chuẩn tham chiếu mạnh.** BA liệt kê từng màn và thao tác, Ninh duyệt trước khi triển khai; độ khớp cuối cùng cần đối chiếu trên thiết bị.

---

❓ **Q3 – Dữ liệu đã ghi trong CapMoney?** Ở bản mới, Ninh muốn bắt đầu với dữ liệu trống, hay cần đưa lịch sử giao dịch hiện tại từ CapMoney sang?

➡️ **Bờm đề xuất: Bắt đầu trống nếu chưa có dữ liệu cần giữ.** Nếu cần chuyển dữ liệu, Ninh chỉ cần nói có; BA sẽ kiểm tra cách xuất dữ liệu và đặt yêu cầu nhập ở vòng sau.

## Nhật ký trả lời

| Câu | Ninh chọn | Hệ quả |
| --- | --- | --- |
| Q1 | Hỏi lại từ đầu | D01–D06 chỉ là lịch sử, không giữ là yêu cầu mới |
| Q2 | Ảnh chỉ tham khảo | Không đặt chuẩn khớp pixel/bố cục CapMoney |
| Q3 | Bắt đầu trống | Không đưa chuyển dữ liệu CapMoney cũ vào MVP |

BA đã chuyển các hệ quả trên vào `00-intake.md`.
