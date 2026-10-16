# Story CM-26: Hỗ trợ người mất email

**Lifecycle:** design
**Planio:** Story CM-26 theo [manifest](../../docs/workflows/redmine-story-manifest.csv)  
**Blocked by:** CM-01, CM-04, CM-08 (HARD cho Dev). Phụ thuộc Dev không ngăn đặc tả/thiết kế Story này.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Ninh theo yêu cầu tra đúng email cũ, gọi lại số đã biết từ trước ngoài ứng dụng, nhận diện chắc, xác minh email mới rồi đổi, không xem dữ liệu tài chính; ghi người xác nhận, thời điểm và tài khoản được hỗ trợ. [R62, R79; F10; H11; quyền bước 3]
- **Nguồn Story bước 8:** [CM-26](../capmoney-overall/issues/26-ho-tro-nguoi-mat-email.md); [spec 20 mục](spec.md) diễn đạt BR/AC và ngoại lệ.
- **Ngoài phạm vi:** Danh sách toàn bộ tài khoản, phục hồi email quản lý, đổi email không qua xác minh.
- **Q26.1:** Ninh đã chọn R79; xem [Q&A](qna.md). Chi tiết lưu trữ và chống sửa là gap Tech, không thêm quyết định sản phẩm.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt 16/10/2026**: CapmmPM `/root` rà bản sau R79, 20 mục, BR/AC và quyền; không còn câu hỏi sản phẩm chặn AC | [spec](spec.md), [Q&A](qna.md), [R79](../../docs/requirements/48-step-9-lost-email-evidence.md), [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) | CapmmPM `/root` |
| `TECH_READY` | Chưa đánh giá; thiết kế kỹ thuật/review độc lập chưa có | chưa có `technical-design.md` | chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho từng ticket | chưa tạo ticket Dev | chưa có |
| `DEV_REVIEWED` | Chưa chạy | chưa có code/PR hoặc lệnh kiểm | chưa có |
| `QA_PASS` | Chưa chạy | chưa có test spec/log thực thi trên build | chưa có |

## Phụ thuộc và việc tiếp theo

- **Phụ thuộc:** CM-01, CM-04, CM-08 (HARD cho Dev). PM/Tech cần phân loại lại nếu hợp đồng đủ cho phần độc lập; không tự mở Dev.
- **Tiếp theo:** Tech lập hợp đồng/thiết kế và cách lưu bản ghi R79, reviewer khác người lập kiểm rồi PM xét `TECH_READY`. Không tạo ticket Dev từ Story ứng viên.
