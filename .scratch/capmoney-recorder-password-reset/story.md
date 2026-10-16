# Story CM-24: Đặt lại mật khẩu ghi chép

**Lifecycle:** design
**Planio:** Story CM-24 theo [manifest](../../docs/workflows/redmine-story-manifest.csv)  
**Blocked by:** CM-04, CM-08 (HARD cho Dev/kiểm dữ liệu). Phụ thuộc Dev không ngăn BA soạn bản nháp.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Chủ còn email nhận link 30 phút, reset mật khẩu, mọi phiên cũ kết thúc, dữ liệu giữ nguyên. [R29; F04, F12; H03]
- **Nguồn Story bước 8:** [CM-24](../capmoney-overall/issues/24-dat-lai-mat-khau-ghi-chep.md); [spec 20 mục](spec.md) diễn đạt BR/AC và ngoại lệ.
- **Ngoài phạm vi:** Reset quản lý CM-22, hỗ trợ mất email CM-26.
- **Câu hỏi/gap:** Đường xin link ghi chép mới khi link hết hạn chưa được Ninh chốt như R61 quản lý; PM/UX xác định trong phạm vi F12. Xem [Q&A](qna.md).

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt 16/10/2026**: CapmmPM `/root` rà bản sau quyết định/chỉnh nguồn, 20 mục, BR/AC và ranh giới; không còn câu hỏi sản phẩm chặn trong AC | [spec](spec.md), [Q&A](qna.md), [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) | CapmmPM `/root` |
| `TECH_READY` | Chưa đánh giá; thiết kế kỹ thuật/review độc lập chưa có | chưa có `technical-design.md` | chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho từng ticket | chưa tạo ticket Dev | chưa có |
| `DEV_REVIEWED` | Chưa chạy | chưa có code/PR hoặc lệnh kiểm | chưa có |
| `QA_PASS` | Chưa chạy | chưa có test spec/log thực thi trên build | chưa có |

## Phụ thuộc và việc tiếp theo

- **Phụ thuộc:** CM-04, CM-08 (HARD cho Dev/kiểm dữ liệu). PM/Tech cần phân loại lại nếu hợp đồng đủ cho phần độc lập; không tự mở Dev.
- **Tiếp theo:** CapmmPM review BA; PM/UX; Ninh nếu cần hành vi mới xử lý gap ở [Q&A](qna.md). Sau `BA_READY`, Tech lập hợp đồng/thiết kế, reviewer khác người lập kiểm rồi PM xét `TECH_READY`. Không tạo ticket Dev từ Story ứng viên.
