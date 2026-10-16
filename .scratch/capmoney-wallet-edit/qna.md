# Q&A: Story 16 – Sửa tên và số dư ban đầu của ví

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/16-sua-thong-tin-vi.md). Mã liên quan: `R30, R40, R44; B01, B10, B15, B26, B29, B31`. R76 đã được Ninh trả lời ngày 16/10/2026; không biến phương án BA/Tech khác thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. CapmmPM đã kiểm AC và ví dụ với nguồn gốc trong [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).

## Chưa rõ hoặc cần review

- Q1 **đã chốt R76:** tên trống khi đổi tên ví không được Lưu; tên/lịch sử cũ giữ nguyên. [Nguồn](../../docs/requirements/46-step-9-wallet-edit-and-return.md).
- Q2: Có cho hai ví cùng tên không? Chưa có quyết định; không tự đặt quy tắc duy nhất tên ví. **Phân loại:** Chưa có AC yêu cầu tên duy nhất; không chặn BA hiện tại và không tự đặt quy tắc mới. **Trạng thái:** CapmmPM đã phân loại là không chặn BA theo AC hiện tại; chi tiết UX/Tech còn chờ thiết kế, và phải hỏi Ninh nếu phát sinh quyết định sản phẩm mới.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định, cập nhật nguồn và rà lại `BA_READY`.
- `BA_READY` đạt ngày 16/10/2026 theo [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md); `TECH_READY` chưa thiết kế/review.
