# Story 14: Sửa lần chuyển

**Lifecycle:** specification  
**Status nguồn:** `needs-triage` tại [issue bước 8](../capmoney-overall/issues/14-sua-lan-chuyen.md); trạng thái live phải đối chiếu Planio.  
**Blocked by:** #13 cung cấp một lần chuyển và số dư hai ví để kiểm; không chặn BA soạn spec.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Là chủ tài khoản, tôi sửa lần chuyển còn hiệu lực như một đơn vị để ngày, tiền, hai ví hoặc ghi chú đúng và mọi ví liên quan có số dư chính xác. Mã nguồn: `B01–B02, B04, B08–B09, B12, B14, B20–B21, B30; R42–R43; F17`; đối chiếu [issue](../capmoney-overall/issues/14-sua-lan-chuyen.md), [quy tắc nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md) và [intake](../../docs/requirements/00-intake.md).
- **Ngoài phạm vi:** Tạo chuyển #13; xóa/khôi phục #15; đồng thời hai sửa khác nhau #30; bản cũ #32; đối chiếu Lưu chưa rõ cho tạo lần chuyển #34/#35.
- **Câu hỏi còn mở:** [Q&A](qna.md). Chưa suy ra câu trả lời từ đề xuất của BA/Tech.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| `BA_READY` | Chưa đạt; bản nháp 20 mục cần PM đối chiếu nguồn/AC và xử lý Q&A | [spec](spec.md), [Q&A](qna.md) | Chưa có review độc lập |
| `TECH_READY` | Chưa đánh giá; Tech thiết kế sau BA gate | Chưa có `technical-design.md` | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa tạo ticket Dev | Theo [workflow](../../docs/workflows/capmm-end-to-end.md) | Chưa có |
| `DEV_REVIEWED` / `QA_PASS` | Chưa chạy | Chưa có build/test spec cho Story này | Chưa có |

## Phụ thuộc và việc tiếp theo

#13 cung cấp một lần chuyển và số dư hai ví để kiểm; không chặn BA soạn spec. BA/PM có thể làm rõ spec khi phụ thuộc Dev chưa hoàn thành. CapmmPM rà từng AC, câu hỏi sản phẩm và ranh giới; sau `BA_READY`, CapmmTech lập thiết kế/hợp đồng, reviewer kỹ thuật khác người lập rà, PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev hoặc tự đổi trạng thái Planio từ bản nháp này.
