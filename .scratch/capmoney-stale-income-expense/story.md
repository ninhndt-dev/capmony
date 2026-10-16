# Story 31: Chặn bản cũ của khoản thu/chi

**Lifecycle:** design
**Status:** needs-triage; bản nháp BA bước 9
**Blocked by:** #11 và #12 là HARD; #30 phân biệt hai giao dịch khác nhau.

## Phạm vi và nguồn

- Hành vi và AC1..ACn: [spec 20 mục](spec.md), [Story ứng viên #31](../capmoney-overall/issues/31-chan-ban-cu-thu-chi.md).
- Quyết định đã xác nhận: B18, B28, B30, F22, R08, R12; xem [intake](../../docs/requirements/00-intake.md), [nghiệp vụ](../../docs/requirements/core-business.md) và [luồng](../../docs/requirements/user-flows.md).
- Ngoài phạm vi: tính năng/phụ thuộc khác được nêu ở mục 18 của spec; không tự triển khai hoặc cấp gate cho chúng.
- Câu hỏi sản phẩm chặn BA: không phát hiện trong phạm vi AC hiện tại; [Q&A](qna.md) tách các điểm UX/Tech còn mở.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | Chưa đánh giá; bản nháp chờ PM đối chiếu | [spec](spec.md), [Q&A](qna.md), [handoff](handoff.md) | Chưa có |
| `TECH_READY` | Chưa đánh giá; có `TECH_GAP` cần thiết kế/kiểm chứng | Kiểm phiên bản cùng giao dịch tại ranh giới ghi; giữ phần nhập và trả bản mới an toàn. | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho ticket Dev | Chưa chia ticket từ Story này | Chưa có |
| `DEV_REVIEWED` | Chưa chạy | Chưa có commit/build | Chưa có |
| `QA_PASS` | Chưa chạy | Chưa có test thực trên build | Chưa có |

## Phụ thuộc và việc tiếp theo

- #11 và #12 là HARD; #30 phân biệt hai giao dịch khác nhau.
- PM độc lập đối chiếu nguồn, 20 mục và AC rồi quyết định `BA_READY`. Sau đó Tech lập thiết kế/hợp đồng, reviewer Tech khác người lập và PM kiểm trước `TECH_READY`; chỉ chia/giao ticket Dev sau các gate.
