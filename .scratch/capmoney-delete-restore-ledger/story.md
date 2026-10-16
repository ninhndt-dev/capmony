# Story 12: Xóa mềm và khôi phục khoản thu/chi

**Lifecycle:** specification  
**Status nguồn:** `needs-triage` tại [issue bước 8](../capmoney-overall/issues/12-xoa-va-khoi-phuc-thu-chi.md); trạng thái live phải đối chiếu Planio.  
**Blocked by:** #10 cho danh sách; #11 cho kiểm không sửa trực tiếp bản đã xóa. Không chặn BA chuẩn bị spec.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Là chủ tài khoản, tôi xác nhận trước khi xóa mềm khoản thu/chi, xem ở mục đã xóa và khôi phục bất cứ lúc nào để số dư được tính lại đúng. Mã nguồn: `R26, R33, R37, R46, R50; B01, B13, B22, B30; F19`; đối chiếu [issue](../capmoney-overall/issues/12-xoa-va-khoi-phuc-thu-chi.md), [quy tắc nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md) và [intake](../../docs/requirements/00-intake.md).
- **Ngoài phạm vi:** Xóa/khôi phục lần chuyển #15; xóa ví #19; chặn bản cũ #31; lưu chưa rõ cho tạo thu/chi #33/#35. Không gộp xóa vĩnh viễn tài khoản #27.
- **Câu hỏi còn mở:** [Q&A](qna.md). Chưa suy ra câu trả lời từ đề xuất của BA/Tech.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| `BA_READY` | Chưa đạt; bản nháp 20 mục cần PM đối chiếu nguồn/AC và xử lý Q&A | [spec](spec.md), [Q&A](qna.md) | Chưa có review độc lập |
| `TECH_READY` | Chưa đánh giá; Tech thiết kế sau BA gate | Chưa có `technical-design.md` | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa tạo ticket Dev | Theo [workflow](../../docs/workflows/capmm-end-to-end.md) | Chưa có |
| `DEV_REVIEWED` / `QA_PASS` | Chưa chạy | Chưa có build/test spec cho Story này | Chưa có |

## Phụ thuộc và việc tiếp theo

#10 cho danh sách; #11 cho kiểm không sửa trực tiếp bản đã xóa. Không chặn BA chuẩn bị spec. BA/PM có thể làm rõ spec khi phụ thuộc Dev chưa hoàn thành. CapmmPM rà từng AC, câu hỏi sản phẩm và ranh giới; sau `BA_READY`, CapmmTech lập thiết kế/hợp đồng, reviewer kỹ thuật khác người lập rà, PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev hoặc tự đổi trạng thái Planio từ bản nháp này.
