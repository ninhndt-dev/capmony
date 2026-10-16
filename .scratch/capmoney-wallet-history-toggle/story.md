# Story 18: Ngừng ví có lịch sử và bảo vệ số dư 0

**Lifecycle:** design
**Status nguồn:** `needs-triage` tại [issue bước 8](../capmoney-overall/issues/18-ngung-vi-co-lich-su.md); trạng thái live phải đối chiếu Planio.  
**Blocked by:** #10/#11/#12/#14/#15/#17 để kiểm lịch sử và mọi tác động giao dịch; phụ thuộc tích hợp, không chặn BA soạn spec.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Là chủ tài khoản, tôi ngừng ví có lịch sử khi số dư hiện tại bằng 0, vẫn đọc lịch sử và ngăn mọi thao tác làm ví ngừng lệch 0; khi không còn ví dùng được, luồng ghi chép dẫn tôi tạo/bật ví. Mã nguồn: `R48, R53; B01, B04, B06, B13, B20–B21, B31; F20, R77`; đối chiếu [issue](../capmoney-overall/issues/18-ngung-vi-co-lich-su.md), [quy tắc nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md) và [intake](../../docs/requirements/00-intake.md).
- **Ngoài phạm vi:** Ngừng ví trống #17; xóa ví #19; các thao tác sửa/xóa/khôi phục thuộc #11/#12/#14/#15. R71/R73 áp dụng reset form thu/chi trước Lưu, không tự thay đổi bởi #18.
- **Câu hỏi còn mở:** [Q&A](qna.md). Chưa suy ra câu trả lời từ đề xuất của BA/Tech.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt 16/10/2026**: CapmmPM `/root` rà bản sau quyết định/chỉnh nguồn, 20 mục, BR/AC và ranh giới; không còn câu hỏi sản phẩm chặn trong AC | [spec](spec.md), [Q&A](qna.md), [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) | CapmmPM `/root` |
| `TECH_READY` | Chưa đánh giá; Tech thiết kế sau BA gate | Chưa có `technical-design.md` | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa tạo ticket Dev | Theo [workflow](../../docs/workflows/capmm-end-to-end.md) | Chưa có |
| `DEV_REVIEWED` / `QA_PASS` | Chưa chạy | Chưa có build/test spec cho Story này | Chưa có |

## Phụ thuộc và việc tiếp theo

#10/#11/#12/#14/#15/#17 để kiểm lịch sử và mọi tác động giao dịch; phụ thuộc tích hợp, không chặn BA soạn spec. BA/PM có thể làm rõ spec khi phụ thuộc Dev chưa hoàn thành. CapmmPM đã rà BR/AC và ranh giới cho `BA_READY`; CapmmTech lập thiết kế/hợp đồng, reviewer kỹ thuật khác người lập rà, PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev; trạng thái issue live phải đối chiếu Planio.
