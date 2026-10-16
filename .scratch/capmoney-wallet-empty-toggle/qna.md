# Q&A: Story 17 – Ngừng và bật lại ví trống

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/17-ngung-va-bat-lai-vi-trong.md). Mã liên quan: `R48, R53; B01, B04, B06, B13, B20, B26, B31`. Các câu dưới đây chưa có trả lời mới của Ninh; không biến phương án BA/Tech thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. CapmmPM đã kiểm AC và ví dụ với nguồn gốc trong [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).

## Chưa rõ hoặc cần review

- Q1: Nếu ví “trống” có số dư đầu 0 nhưng đã từng có giao dịch xóa mềm thì #17 hay #18 hiển thị thao tác ngừng? B19 xác nhận vẫn từng có; BA đề xuất chuyển kiểm chứng vào #18. **Phân loại:** Phân ranh giới Story theo B19; PM kiểm, không cần đổi quy tắc sản phẩm. **Trạng thái:** CapmmPM đã phân loại là không chặn BA theo AC hiện tại; chi tiết UX/Tech còn chờ thiết kế, và phải hỏi Ninh nếu phát sinh quyết định sản phẩm mới.
- Q2: Câu chữ và cách hiển thị ví ngừng chưa được Ninh chốt; quy tắc không chọn được đã chốt B04/B20. **Phân loại:** Chi tiết UX; B04/B20 đã chốt hành vi. **Trạng thái:** CapmmPM đã phân loại là không chặn BA theo AC hiện tại; chi tiết UX/Tech còn chờ thiết kế, và phải hỏi Ninh nếu phát sinh quyết định sản phẩm mới.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định, cập nhật nguồn và rà lại `BA_READY`.
- `BA_READY` đạt ngày 16/10/2026 theo [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md); `TECH_READY` chưa thiết kế/review.
