# Story 11: Sửa khoản thu/chi

**Lifecycle:** design
**Status nguồn:** `needs-triage` tại [issue bước 8](../capmoney-overall/issues/11-sua-thu-chi.md); trạng thái live phải đối chiếu Planio.  
**Blocked by:** #10 là đường xem giao dịch và đối chiếu lịch sử; #08/#09 là nền ghi giao dịch. Phụ thuộc Dev/tích hợp, không chặn BA soạn spec.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Là chủ tài khoản, tôi sửa khoản thu/chi còn hiệu lực đã lưu để chữa sai loại, ngày, số tiền, ví, danh mục hoặc ghi chú; số dư các ví liên quan và lịch sử vẫn đúng. Mã nguồn: `B01–B03, B07, B12, B14, B20–B21, B30; R43, R59; F22`; đối chiếu [issue](../capmoney-overall/issues/11-sua-thu-chi.md), [quy tắc nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md) và [intake](../../docs/requirements/00-intake.md).
- **Ngoài phạm vi:** Xóa/khôi phục #12; xung đột bản cũ đầy đủ #31; đối chiếu Lưu chưa rõ #33/#35; sửa lần chuyển #14.
- **Câu hỏi còn mở:** [Q&A](qna.md). Chưa suy ra câu trả lời từ đề xuất của BA/Tech.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt 16/10/2026**: CapmmPM `/root` rà độc lập 20 mục, nguồn, BR/AC và ranh giới; không còn câu hỏi sản phẩm chặn trong phạm vi AC | [spec](spec.md), [Q&A](qna.md), [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) | CapmmPM `/root` |
| `TECH_READY` | Chưa đánh giá; Tech thiết kế sau BA gate | Chưa có `technical-design.md` | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa tạo ticket Dev | Theo [workflow](../../docs/workflows/capmm-end-to-end.md) | Chưa có |
| `DEV_REVIEWED` / `QA_PASS` | Chưa chạy | Chưa có build/test spec cho Story này | Chưa có |

## Phụ thuộc và việc tiếp theo

#10 là đường xem giao dịch và đối chiếu lịch sử; #08/#09 là nền ghi giao dịch. Phụ thuộc Dev/tích hợp, không chặn BA soạn spec. BA/PM có thể làm rõ spec khi phụ thuộc Dev chưa hoàn thành. CapmmPM đã rà BR/AC và ranh giới cho `BA_READY`; CapmmTech lập thiết kế/hợp đồng, reviewer kỹ thuật khác người lập rà, PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev; trạng thái issue live phải đối chiếu Planio.
