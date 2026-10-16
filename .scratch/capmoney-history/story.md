# Story 10: Lịch sử chung và lọc theo ví

**Lifecycle:** specification  
**Status nguồn:** `needs-triage` tại [issue bước 8](../capmoney-overall/issues/10-lich-su-chung-va-loc-vi.md); trạng thái live phải đối chiếu Planio.  
**Blocked by:** #08 và #09 là dữ liệu thu/chi để kiểm lịch sử tích hợp; không chặn BA viết đặc tả.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Là chủ tài khoản ghi chép, tôi xem khoản thu/chi của mọi ví theo ngày và lọc một ví để kiểm lại dòng tiền và số dư hiện tại. Lát này chỉ hiện dữ liệu đã lưu; chuyển tiền được bổ sung ở #13. Mã nguồn: `R07, R59–R60; B01, B17; F15–F17`; đối chiếu [issue](../capmoney-overall/issues/10-lich-su-chung-va-loc-vi.md), [quy tắc nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md) và [intake](../../docs/requirements/00-intake.md).
- **Ngoài phạm vi:** Sửa thu/chi #11, xóa/khôi phục #12, chuyển tiền và một dòng chuyển #13, tự tải điện thoại thứ hai #28. Bản này không chốt số dư sau từng dòng (B17).
- **Câu hỏi còn mở:** [Q&A](qna.md). Chưa suy ra câu trả lời từ đề xuất của BA/Tech.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| `BA_READY` | Chưa đạt; bản nháp 20 mục cần PM đối chiếu nguồn/AC và xử lý Q&A | [spec](spec.md), [Q&A](qna.md) | Chưa có review độc lập |
| `TECH_READY` | Chưa đánh giá; Tech thiết kế sau BA gate | Chưa có `technical-design.md` | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa tạo ticket Dev | Theo [workflow](../../docs/workflows/capmm-end-to-end.md) | Chưa có |
| `DEV_REVIEWED` / `QA_PASS` | Chưa chạy | Chưa có build/test spec cho Story này | Chưa có |

## Phụ thuộc và việc tiếp theo

#08 và #09 là dữ liệu thu/chi để kiểm lịch sử tích hợp; không chặn BA viết đặc tả. BA/PM có thể làm rõ spec khi phụ thuộc Dev chưa hoàn thành. CapmmPM rà từng AC, câu hỏi sản phẩm và ranh giới; sau `BA_READY`, CapmmTech lập thiết kế/hợp đồng, reviewer kỹ thuật khác người lập rà, PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev hoặc tự đổi trạng thái Planio từ bản nháp này.
