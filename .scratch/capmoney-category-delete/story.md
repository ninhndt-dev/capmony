# Story CM-21: Xóa danh mục chưa dùng

**Lifecycle:** design
**Planio:** Story CM-21 theo [manifest](../../docs/workflows/redmine-story-manifest.csv)  
**Blocked by:** CM-08, CM-20 (HARD cho Dev). Phụ thuộc Dev không ngăn BA soạn bản nháp.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Chủ xóa danh mục chưa từng gắn giao dịch, kể cả mặc định; danh mục đã dùng giữ lại. [R12, R49; B05, B11, B16; H05]
- **Nguồn Story bước 8:** [CM-21](../capmoney-overall/issues/21-xoa-danh-muc-chua-dung.md); [spec 20 mục](spec.md) diễn đạt BR/AC và ngoại lệ.
- **Ngoài phạm vi:** Xóa danh mục đã dùng, xóa giao dịch, đổi tên CM-20.
- **Câu hỏi/gap:** PM đối chiếu diễn giải: giao dịch đã xóa mềm vẫn là từng gắn, nên danh mục giữ lại theo B05/B11. Xem [Q&A](qna.md).

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt 16/10/2026**: CapmmPM `/root` rà độc lập 20 mục, nguồn, BR/AC và ranh giới; không còn câu hỏi sản phẩm chặn trong phạm vi AC | [spec](spec.md), [Q&A](qna.md), [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) | CapmmPM `/root` |
| `TECH_READY` | Chưa đánh giá; thiết kế kỹ thuật/review độc lập chưa có | chưa có `technical-design.md` | chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho từng ticket | chưa tạo ticket Dev | chưa có |
| `DEV_REVIEWED` | Chưa chạy | chưa có code/PR hoặc lệnh kiểm | chưa có |
| `QA_PASS` | Chưa chạy | chưa có test spec/log thực thi trên build | chưa có |

## Phụ thuộc và việc tiếp theo

- **Phụ thuộc:** CM-08, CM-20 (HARD cho Dev). PM/Tech cần phân loại lại nếu hợp đồng đủ cho phần độc lập; không tự mở Dev.
- **Tiếp theo:** CapmmPM đã review BA; CapmmPM xử lý gap ở [Q&A](qna.md). Sau `BA_READY`, Tech lập hợp đồng/thiết kế, reviewer khác người lập kiểm rồi PM xét `TECH_READY`. Không tạo ticket Dev từ Story ứng viên.
