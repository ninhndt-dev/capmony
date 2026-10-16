# Story CM-26: Hỗ trợ người mất email

**Lifecycle:** specification  
**Planio:** Story CM-26 theo [manifest](../../docs/workflows/redmine-story-manifest.csv)  
**Blocked by:** CM-01, CM-04, CM-08 (HARD cho Dev). Phụ thuộc Dev không ngăn BA soạn bản nháp.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Ninh theo yêu cầu tra đúng email cũ, gọi lại số đã biết từ trước ngoài ứng dụng, nhận diện chắc, xác minh email mới rồi đổi, không xem dữ liệu tài chính. [R62; F10; H11; quyền bước 3]
- **Nguồn Story bước 8:** [CM-26](../capmoney-overall/issues/26-ho-tro-nguoi-mat-email.md); [spec 20 mục](spec.md) diễn đạt BR/AC và ngoại lệ.
- **Ngoài phạm vi:** Danh sách toàn bộ tài khoản, phục hồi email quản lý, đổi email không qua xác minh.
- **Câu hỏi/gap:** Cách ghi bằng chứng Ninh đã gọi lại số biết trước, nhận diện và xác nhận yêu cầu chưa được Ninh chọn. Xem [Q&A](qna.md).

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | Chưa đạt: BA draft 16/10/2026; chờ PM đối chiếu 20 mục, nguồn và câu hỏi | [spec](spec.md), [Q&A](qna.md), [handoff](handoff.md) | chưa có |
| `TECH_READY` | Chưa đánh giá; thiết kế kỹ thuật/review độc lập chưa có | chưa có `technical-design.md` | chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho từng ticket | chưa tạo ticket Dev | chưa có |
| `DEV_REVIEWED` | Chưa chạy | chưa có code/PR hoặc lệnh kiểm | chưa có |
| `QA_PASS` | Chưa chạy | chưa có test spec/log thực thi trên build | chưa có |

## Phụ thuộc và việc tiếp theo

- **Phụ thuộc:** CM-01, CM-04, CM-08 (HARD cho Dev). PM/Tech cần phân loại lại nếu hợp đồng đủ cho phần độc lập; không tự mở Dev.
- **Tiếp theo:** CapmmPM review BA; Ninh/CapmmBA xử lý gap ở [Q&A](qna.md). Sau `BA_READY`, Tech lập hợp đồng/thiết kế, reviewer khác người lập kiểm rồi PM xét `TECH_READY`. Không tạo ticket Dev từ Story ứng viên.
