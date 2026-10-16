# Q&A: Story 28 – Tải dữ liệu trên điện thoại thứ hai

## Ninh đã xác nhận

Các quyết định F06, F08, F21, R27, R56, R08, R12 và R71/R73/R74 được dẫn theo từng BR/AC trong [spec](spec.md). **Tự tải khi vẫn ở form** giữ phần nhập dở; **rời form sang trang khác trước Lưu** rồi quay lại mở trống cho thu/chi/chuyển. Đây là nguồn hành vi, không phải câu trả lời mới ở Story #28. [Story ứng viên #28](../capmoney-overall/issues/28-tai-du-lieu-dien-thoai-thu-hai.md) xác định ranh giới lát cắt.

## Câu hỏi sản phẩm chặn BA

Chưa phát hiện câu hỏi Ninh phải chọn thêm cho AC của Story này. Nếu PM thấy một nhánh mới làm thay đổi trải nghiệm đã chốt, cần đưa phương án cụ thể cho Ninh trước khi cấp `BA_READY`; không suy quyết định từ đề xuất Tech/UX.

## UX/Tech còn mở, không tự nhận là quyết định của Ninh

- Sự kiện quay lại, cache/tải mới, giữ bản nhập dở và kiểm quyền hai phiên.
- Câu chữ, bố cục, mã lỗi và ngưỡng hiệu năng cụ thể chưa được Ninh chốt; đề xuất trong spec chỉ để truy vết hành vi.
- Không có câu trả lời mới của Ninh cho Story này tại thời điểm soạn.
