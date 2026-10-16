# Story CM-23: Đổi email quản lý

**Lifecycle:** specification  
**Planio:** Story CM-23 theo [manifest](../../docs/workflows/redmine-story-manifest.csv)  
**Blocked by:** CM-01, CM-04 (HARD cho Dev/kiểm quyền). Phụ thuộc Dev không ngăn BA soạn bản nháp.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Ninh đang đăng nhập đổi email quản lý sau xác minh email mới; phiên cũ kết thúc. [R12; quyền bước 3; H01]
- **Nguồn Story bước 8:** [CM-23](../capmoney-overall/issues/23-doi-email-quan-ly.md); [spec 20 mục](spec.md) diễn đạt BR/AC và ngoại lệ.
- **Ngoài phạm vi:** Đổi email tài khoản ghi chép CM-25, hỗ trợ mất email quản lý sau đăng xuất.
- **Câu hỏi/gap:** Phương thức/TTL xác minh email và báo xung đột chưa chốt; Tech/PM đề xuất, BA hỏi Ninh nếu đổi quyền. Xem [Q&A](qna.md).

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | Chưa đạt: BA draft 16/10/2026; chờ PM đối chiếu 20 mục, nguồn và câu hỏi | [spec](spec.md), [Q&A](qna.md), [handoff](handoff.md) | chưa có |
| `TECH_READY` | Chưa đánh giá; thiết kế kỹ thuật/review độc lập chưa có | chưa có `technical-design.md` | chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho từng ticket | chưa tạo ticket Dev | chưa có |
| `DEV_REVIEWED` | Chưa chạy | chưa có code/PR hoặc lệnh kiểm | chưa có |
| `QA_PASS` | Chưa chạy | chưa có test spec/log thực thi trên build | chưa có |

## Phụ thuộc và việc tiếp theo

- **Phụ thuộc:** CM-01, CM-04 (HARD cho Dev/kiểm quyền). PM/Tech cần phân loại lại nếu hợp đồng đủ cho phần độc lập; không tự mở Dev.
- **Tiếp theo:** CapmmPM review BA; PM/Tech xử lý gap ở [Q&A](qna.md). Sau `BA_READY`, Tech lập hợp đồng/thiết kế, reviewer khác người lập kiểm rồi PM xét `TECH_READY`. Không tạo ticket Dev từ Story ứng viên.
