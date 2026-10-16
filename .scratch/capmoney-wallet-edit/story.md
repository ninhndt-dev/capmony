# Story 16: Sửa tên và số dư ban đầu của ví

**Lifecycle:** specification  
**Status nguồn:** `needs-triage` tại [issue bước 8](../capmoney-overall/issues/16-sua-thong-tin-vi.md); trạng thái live phải đối chiếu Planio.  
**Blocked by:** #10 để kiểm lịch sử giao dịch cũ sau đổi tên và số dư; #06/#07 để có ví. BA có thể đặc tả trước tích hợp.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Là chủ tài khoản, tôi đổi tên hoặc số dư ban đầu của ví đã tạo; giao dịch cũ vẫn thuộc cùng ví và số dư hiện tại được tính lại. Mã nguồn: `R30, R40, R44; B01, B10, B15, B26, B29, B31`; đối chiếu [issue](../capmoney-overall/issues/16-sua-thong-tin-vi.md), [quy tắc nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md) và [intake](../../docs/requirements/00-intake.md).
- **Ngoài phạm vi:** Tạo ví #06/#07; ngừng/bật lại #17/#18; xóa ví #19; lịch sử #10. Không sửa giao dịch trong lát này.
- **Câu hỏi còn mở:** [Q&A](qna.md). Chưa suy ra câu trả lời từ đề xuất của BA/Tech.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| `BA_READY` | Chưa đạt; bản nháp 20 mục cần PM đối chiếu nguồn/AC và xử lý Q&A | [spec](spec.md), [Q&A](qna.md) | Chưa có review độc lập |
| `TECH_READY` | Chưa đánh giá; Tech thiết kế sau BA gate | Chưa có `technical-design.md` | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa tạo ticket Dev | Theo [workflow](../../docs/workflows/capmm-end-to-end.md) | Chưa có |
| `DEV_REVIEWED` / `QA_PASS` | Chưa chạy | Chưa có build/test spec cho Story này | Chưa có |

## Phụ thuộc và việc tiếp theo

#10 để kiểm lịch sử giao dịch cũ sau đổi tên và số dư; #06/#07 để có ví. BA có thể đặc tả trước tích hợp. BA/PM có thể làm rõ spec khi phụ thuộc Dev chưa hoàn thành. CapmmPM rà từng AC, câu hỏi sản phẩm và ranh giới; sau `BA_READY`, CapmmTech lập thiết kế/hợp đồng, reviewer kỹ thuật khác người lập rà, PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev hoặc tự đổi trạng thái Planio từ bản nháp này.
