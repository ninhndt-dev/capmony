# Story 15: Xóa mềm và khôi phục lần chuyển

**Lifecycle:** specification  
**Status nguồn:** `needs-triage` tại [issue bước 8](../capmoney-overall/issues/15-xoa-va-khoi-phuc-lan-chuyen.md); trạng thái live phải đối chiếu Planio.  
**Blocked by:** #13 tạo lần chuyển; #14 xác định không sửa bản đã xóa. Phụ thuộc Dev/tích hợp.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Là chủ tài khoản, tôi xác nhận xóa mềm hoặc khôi phục cả lần chuyển như một đơn vị, để số dư hai ví và lịch sử không bị lệch. Mã nguồn: `R26, R33, R37, R46, R50–R51, R55; B01, B13, B22, B30; F17, F19`; đối chiếu [issue](../capmoney-overall/issues/15-xoa-va-khoi-phuc-lan-chuyen.md), [quy tắc nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md) và [intake](../../docs/requirements/00-intake.md).
- **Ngoài phạm vi:** Xóa/khôi phục thu/chi #12; tạo/sửa chuyển #13/#14; xóa ví #19; bản cũ #32. Không có xóa vĩnh viễn từng lần chuyển.
- **Câu hỏi còn mở:** [Q&A](qna.md). Chưa suy ra câu trả lời từ đề xuất của BA/Tech.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| `BA_READY` | Chưa đạt; bản nháp 20 mục cần PM đối chiếu nguồn/AC và xử lý Q&A | [spec](spec.md), [Q&A](qna.md) | Chưa có review độc lập |
| `TECH_READY` | Chưa đánh giá; Tech thiết kế sau BA gate | Chưa có `technical-design.md` | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa tạo ticket Dev | Theo [workflow](../../docs/workflows/capmm-end-to-end.md) | Chưa có |
| `DEV_REVIEWED` / `QA_PASS` | Chưa chạy | Chưa có build/test spec cho Story này | Chưa có |

## Phụ thuộc và việc tiếp theo

#13 tạo lần chuyển; #14 xác định không sửa bản đã xóa. Phụ thuộc Dev/tích hợp. BA/PM có thể làm rõ spec khi phụ thuộc Dev chưa hoàn thành. CapmmPM rà từng AC, câu hỏi sản phẩm và ranh giới; sau `BA_READY`, CapmmTech lập thiết kế/hợp đồng, reviewer kỹ thuật khác người lập rà, PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev hoặc tự đổi trạng thái Planio từ bản nháp này.
