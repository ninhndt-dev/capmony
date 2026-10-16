# Story 29: Giữ giao dịch mới gần đồng thời

**Lifecycle:** design
**Status nguồn:** `needs-triage` ở issue bước 8; gate hiện hành ở bảng dưới; trạng thái live đối chiếu Planio.
**Blocked by:** #08 và #13 là HARD; #09 bổ sung khoản chi; #33/#34 đối chiếu kết quả chưa rõ.

## Phạm vi và nguồn

- Hành vi và AC1..ACn: [spec 20 mục](spec.md), [Story ứng viên #29](../capmoney-overall/issues/29-giu-giao-dich-moi-dong-thoi.md).
- Quyết định đã xác nhận: B01, B24, R51, F09, R56; xem [intake](../../docs/requirements/00-intake.md), [nghiệp vụ](../../docs/requirements/core-business.md) và [luồng](../../docs/requirements/user-flows.md).
- Ngoài phạm vi: tính năng/phụ thuộc khác được nêu ở mục 18 của spec; không tự triển khai hoặc cấp gate cho chúng.
- Câu hỏi sản phẩm chặn BA: không phát hiện trong phạm vi AC hiện tại; [Q&A](qna.md) tách các điểm UX/Tech còn mở.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt 16/10/2026**: CapmmPM `/root` rà độc lập 20 mục, nguồn, BR/AC và ranh giới; không còn câu hỏi sản phẩm chặn trong phạm vi AC | [spec](spec.md), [Q&A](qna.md), [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) | CapmmPM `/root` |
| `TECH_READY` | Chưa đánh giá; có `TECH_GAP` cần thiết kế/kiểm chứng | Ghi giao dịch và số dư nguyên tử dưới yêu cầu chồng thời gian; phân biệt hai ý định với gửi lặp cùng ý định. | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho ticket Dev | Chưa chia ticket từ Story này | Chưa có |
| `DEV_REVIEWED` | Chưa chạy | Chưa có commit/build | Chưa có |
| `QA_PASS` | Chưa chạy | Chưa có test thực trên build | Chưa có |

## Phụ thuộc và việc tiếp theo

- #08 và #13 là HARD; #09 bổ sung khoản chi; #33/#34 đối chiếu kết quả chưa rõ.
- PM đã đối chiếu nguồn, 20 mục và AC và xác nhận `BA_READY`. Sau đó Tech lập thiết kế/hợp đồng, reviewer Tech khác người lập và PM kiểm trước `TECH_READY`; chỉ chia/giao ticket Dev sau các gate.
