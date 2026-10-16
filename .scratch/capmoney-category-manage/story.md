# Story CM-20: Thêm và đổi tên danh mục

**Lifecycle:** design
**Planio:** Story CM-20 theo [manifest](../../docs/workflows/redmine-story-manifest.csv)  
**Blocked by:** CM-08, CM-10 (HARD cho Dev/kiểm chứng). Phụ thuộc Dev không ngăn BA soạn bản nháp.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Chủ thêm và đổi tên danh mục riêng dùng chung thu/chi; lịch sử cũ hiện tên mới. [R12, R32, R45, R49, R54; B05, B16; H05]
- **Nguồn Story bước 8:** [CM-20](../capmoney-overall/issues/20-them-va-doi-ten-danh-muc.md); [spec 20 mục](spec.md) diễn đạt BR/AC và ngoại lệ.
- **Ngoài phạm vi:** Xóa danh mục CM-21, sửa giao dịch và quản lý danh mục của người khác.
- **Câu hỏi/gap:** Tên trùng, giới hạn/chuẩn hóa tên và câu chữ lỗi chưa chốt; không tự áp đặt tên duy nhất. Xem [Q&A](qna.md).

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt 16/10/2026**: CapmmPM `/root` rà bản sau quyết định/chỉnh nguồn, 20 mục, BR/AC và ranh giới; không còn câu hỏi sản phẩm chặn trong AC | [spec](spec.md), [Q&A](qna.md), [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) | CapmmPM `/root` |
| `TECH_READY` | Chưa đánh giá; thiết kế kỹ thuật/review độc lập chưa có | chưa có `technical-design.md` | chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho từng ticket | chưa tạo ticket Dev | chưa có |
| `DEV_REVIEWED` | Chưa chạy | chưa có code/PR hoặc lệnh kiểm | chưa có |
| `QA_PASS` | Chưa chạy | chưa có test spec/log thực thi trên build | chưa có |

## Phụ thuộc và việc tiếp theo

- **Phụ thuộc:** CM-08, CM-10 (HARD cho Dev/kiểm chứng). PM/Tech cần phân loại lại nếu hợp đồng đủ cho phần độc lập; không tự mở Dev.
- **Tiếp theo:** CapmmPM review BA; PM/Tech; Ninh nếu thay đổi hành vi sản phẩm xử lý gap ở [Q&A](qna.md). Sau `BA_READY`, Tech lập hợp đồng/thiết kế, reviewer khác người lập kiểm rồi PM xét `TECH_READY`. Không tạo ticket Dev từ Story ứng viên.
