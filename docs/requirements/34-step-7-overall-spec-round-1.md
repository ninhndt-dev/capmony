# Bước 7, vòng 1: xác nhận đặc tả tổng thể và điểm kiểm thử

**Trạng thái:** Đã trả lời ngày 13/10/2026. PM → Tech đã rà soát [đặc tả tổng thể](../../.scratch/capmoney-overall/spec.md); Tech đã đối chiếu S13 với nhật ký gốc và xác nhận Ninh chọn sửa hết cả lỗi nhỏ trước khi mở rộng.

**Mục đích:** Xác nhận cách hiểu bản đầu và điểm kiểm thử chính trước khi kết thúc bước 7. Các mục “Chưa rõ” trong đặc tả vẫn mở; câu trả lời vòng này không tự chốt chúng.

❓ **Q1 – Nội dung đặc tả:** Bản tổng thể H01–H12, phạm vi, thứ tự demo và điều kiện mở đến khoảng 10 người có đúng ý Ninh không?

➡️ **A (Bờm đề xuất):** Đúng; giữ các chi tiết được đánh dấu `needs-info` để làm rõ trước đặc tả/ticket liên quan. **B:** Cần sửa; Ninh ghi phần muốn đổi.

---

❓ **Q2 – Điểm kiểm thử chính:** Trước khi dùng thật, kiểm tra qua thao tác web trên iPhone và Android, dùng ít nhất hai tài khoản riêng, rồi tải lại trang hoặc mở điện thoại thứ hai để đối chiếu dữ liệu và số dư đã lưu có đúng kỳ vọng không?

➡️ **A (Bờm đề xuất):** Đúng; áp dụng cách kiểm tra hành vi từ thao tác đến dữ liệu đọc lại, cùng các ca lỗi/quyền trong đặc tả. **B:** Muốn cách kiểm tra khác; Ninh ghi cách mong muốn.

## Nhật ký trả lời

Ninh trả lời hai dòng **A, A** theo thứ tự Q1–Q2.

| Câu | Ninh đã xác nhận | Vẫn chưa rõ |
| --- | --- | --- |
| Q1 | Bản đặc tả tổng thể H01–H12, phạm vi, thứ tự demo và điều kiện mở rộng đúng ý; chuyển sang bước 8. | Các mục `needs-info` trong đặc tả chưa được xác nhận riêng. |
| Q2 | Điểm kiểm thử chính là thao tác web trên iPhone và Android với ít nhất hai tài khoản riêng, rồi đọc lại dữ liệu và số dư sau tải lại trang hoặc trên điện thoại thứ hai; kiểm tra thêm các ca lỗi/quyền đã nêu. | Công cụ, thiết bị/trình duyệt cụ thể và bằng chứng chi tiết sẽ được xác định khi chuẩn bị kiểm thử. |
