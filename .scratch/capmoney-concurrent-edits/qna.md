# Q&A: Story 30 – Giữ hai sửa đổi khác nhau gần đồng thời

## Ninh đã xác nhận

Các quyết định B01, B27, B30, R51 được dẫn theo từng BR/AC trong [spec](spec.md). Đây là nguồn hành vi, không phải câu trả lời mới ở bước 9. [Story ứng viên #30](../capmoney-overall/issues/30-giu-hai-sua-doi-dong-thoi.md) xác định ranh giới lát cắt.

## Câu hỏi sản phẩm chặn BA

CapmmPM đã rà AC và chưa phát hiện câu hỏi Ninh phải chọn thêm; Story này đạt `BA_READY` ngày 16/10/2026 theo [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md). Nếu thiết kế phát sinh nhánh mới làm thay đổi trải nghiệm đã chốt, cần đưa phương án cụ thể cho Ninh và rà lại gate; không suy quyết định từ đề xuất Tech/UX.

## UX/Tech còn mở, không tự nhận là quyết định của Ninh

- Ghi đủ hai delta giao dịch khác nhau mà không ghi đè; bảo toàn hai ví của lần chuyển.
- Câu chữ, bố cục, mã lỗi và ngưỡng hiệu năng cụ thể chưa được Ninh chốt; đề xuất trong spec chỉ để truy vết hành vi.
- Không có câu trả lời mới của Ninh cho Story này tại thời điểm soạn.
