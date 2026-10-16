# Story 17: Ngừng và bật lại ví trống

**Lifecycle:** specification  
**Status nguồn:** `needs-triage` tại [issue bước 8](../capmoney-overall/issues/17-ngung-va-bat-lai-vi-trong.md); trạng thái live phải đối chiếu Planio.  
**Blocked by:** #08/#11/#14/#16 cần kiểm các thao tác với ví ngừng; phụ thuộc Dev/tích hợp không chặn BA đặc tả.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Là chủ tài khoản, tôi ngừng một ví chưa từng có giao dịch và đang có số dư 0, rồi bật lại khi cần; ví ngừng không nhận giao dịch mới và luôn giữ số dư 0. Mã nguồn: `R48, R53; B01, B04, B06, B13, B20, B26, B31`; đối chiếu [issue](../capmoney-overall/issues/17-ngung-va-bat-lai-vi-trong.md), [quy tắc nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md) và [intake](../../docs/requirements/00-intake.md).
- **Ngoài phạm vi:** Ví có lịch sử và số dư 0 #18; sửa thông tin ví #16; xóa ví #19. Bảo vệ commit khi đồng thời là điểm Tech phải chứng minh.
- **Câu hỏi còn mở:** [Q&A](qna.md). Chưa suy ra câu trả lời từ đề xuất của BA/Tech.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| `BA_READY` | Chưa đạt; bản nháp 20 mục cần PM đối chiếu nguồn/AC và xử lý Q&A | [spec](spec.md), [Q&A](qna.md) | Chưa có review độc lập |
| `TECH_READY` | Chưa đánh giá; Tech thiết kế sau BA gate | Chưa có `technical-design.md` | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa tạo ticket Dev | Theo [workflow](../../docs/workflows/capmm-end-to-end.md) | Chưa có |
| `DEV_REVIEWED` / `QA_PASS` | Chưa chạy | Chưa có build/test spec cho Story này | Chưa có |

## Phụ thuộc và việc tiếp theo

#08/#11/#14/#16 cần kiểm các thao tác với ví ngừng; phụ thuộc Dev/tích hợp không chặn BA đặc tả. BA/PM có thể làm rõ spec khi phụ thuộc Dev chưa hoàn thành. CapmmPM rà từng AC, câu hỏi sản phẩm và ranh giới; sau `BA_READY`, CapmmTech lập thiết kế/hợp đồng, reviewer kỹ thuật khác người lập rà, PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev hoặc tự đổi trạng thái Planio từ bản nháp này.
