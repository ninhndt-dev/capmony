# Story 28: Tải dữ liệu trên điện thoại thứ hai

**Lifecycle:** design
**Status:** needs-triage; bản nháp BA bước 9
**Blocked by:** #10 là HARD cho lịch sử/số dư; #08/#09/#13 cấp giao dịch đã lưu.

## Phạm vi và nguồn

- Hành vi và AC1..ACn: [spec 20 mục](spec.md), [Story ứng viên #28](../capmoney-overall/issues/28-tai-du-lieu-dien-thoai-thu-hai.md).
- Quyết định đã xác nhận: F06, F08, F21, R27, R56, R08, R12; R71/R73/R74 quy định riêng trường hợp **rời biểu mẫu sang trang khác trước Lưu**. Xem [intake](../../docs/requirements/00-intake.md), [nghiệp vụ](../../docs/requirements/core-business.md) và [luồng](../../docs/requirements/user-flows.md).
- Ngoài phạm vi: tính năng/phụ thuộc khác được nêu ở mục 18 của spec; không tự triển khai hoặc cấp gate cho chúng.
- Câu hỏi sản phẩm chặn BA: không phát hiện trong phạm vi AC hiện tại; [Q&A](qna.md) tách các điểm UX/Tech còn mở.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | Chưa đánh giá; bản nháp chờ PM đối chiếu | [spec](spec.md), [Q&A](qna.md), [handoff](handoff.md) | Chưa có |
| `TECH_READY` | Chưa đánh giá; có `TECH_GAP` cần thiết kế/kiểm chứng | Sự kiện quay lại, cache/tải mới, giữ bản nhập dở và kiểm quyền hai phiên. | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho ticket Dev | Chưa chia ticket từ Story này | Chưa có |
| `DEV_REVIEWED` | Chưa chạy | Chưa có commit/build | Chưa có |
| `QA_PASS` | Chưa chạy | Chưa có test thực trên build | Chưa có |

## Phụ thuộc và việc tiếp theo

- #10 là HARD cho lịch sử/số dư; #08/#09/#13 cấp giao dịch đã lưu.
- PM độc lập đối chiếu nguồn, 20 mục và AC rồi quyết định `BA_READY`. Sau đó Tech lập thiết kế/hợp đồng, reviewer Tech khác người lập và PM kiểm trước `TECH_READY`; chỉ chia/giao ticket Dev sau các gate.
