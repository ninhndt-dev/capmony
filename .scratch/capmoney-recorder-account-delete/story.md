# Story CM-27: Tự xóa tài khoản ghi chép

**Lifecycle:** design
**Planio:** Story CM-27 theo [manifest](../../docs/workflows/redmine-story-manifest.csv)  
**Blocked by:** CM-07, CM-09, CM-12, CM-15, CM-18, CM-20 (HARD cho Dev). Phụ thuộc Dev không ngăn BA soạn bản nháp.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Chủ cảnh báo, nhập lại mật khẩu, xác nhận cuối rồi xóa vĩnh viễn tài khoản và dữ liệu; điện thoại khác mất quyền, mời lại cùng email tạo tài khoản trống. [R63; F05; H12; quyền bước 3]
- **Nguồn Story bước 8:** [CM-27](../capmoney-overall/issues/27-tu-xoa-tai-khoan-ghi-chep.md); [spec 20 mục](spec.md) diễn đạt BR/AC và ngoại lệ.
- **Ngoài phạm vi:** Xóa quản lý, hoàn tác, xóa vĩnh viễn từng giao dịch riêng.
- **Câu hỏi/gap:** Copy cảnh báo/điện thoại khác và kiểm kê log/dữ liệu phụ là UX/Tech; không thu hẹp toàn bộ dữ liệu. Xem [Q&A](qna.md).

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt 16/10/2026**: CapmmPM `/root` rà độc lập 20 mục, nguồn, BR/AC và ranh giới; không còn câu hỏi sản phẩm chặn trong phạm vi AC | [spec](spec.md), [Q&A](qna.md), [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) | CapmmPM `/root` |
| `TECH_READY` | Chưa đánh giá; thiết kế kỹ thuật/review độc lập chưa có | chưa có `technical-design.md` | chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho từng ticket | chưa tạo ticket Dev | chưa có |
| `DEV_REVIEWED` | Chưa chạy | chưa có code/PR hoặc lệnh kiểm | chưa có |
| `QA_PASS` | Chưa chạy | chưa có test spec/log thực thi trên build | chưa có |

## Phụ thuộc và việc tiếp theo

- **Phụ thuộc:** CM-07, CM-09, CM-12, CM-15, CM-18, CM-20 (HARD cho Dev). PM/Tech cần phân loại lại nếu hợp đồng đủ cho phần độc lập; không tự mở Dev.
- **Tiếp theo:** CapmmPM đã review BA; PM/Tech xử lý gap ở [Q&A](qna.md). Sau `BA_READY`, Tech lập hợp đồng/thiết kế, reviewer khác người lập kiểm rồi PM xét `TECH_READY`. Không tạo ticket Dev từ Story ứng viên.
