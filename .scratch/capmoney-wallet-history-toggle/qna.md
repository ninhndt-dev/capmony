# Q&A: Story 18 – Ngừng ví có lịch sử và bảo vệ số dư 0

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/18-ngung-vi-co-lich-su.md). Mã liên quan: `R48, R53; B01, B04, B06, B13, B20–B21, B31; F20`. R77 đã được Ninh trả lời ngày 16/10/2026; không biến phương án BA/Tech khác thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. PM cần kiểm lại từng AC và ví dụ với nguồn gốc.

## Chưa rõ hoặc cần review

- Q1 **đã chốt R77:** khi tạo hoặc bật lại ví từ luồng mọi ví ngừng rồi quay về ghi thu/chi, ví ấy được chọn sẵn. [Nguồn](../../docs/requirements/46-step-9-wallet-edit-and-return.md).
- Q2: Nếu người dùng đã nhập form rồi rời để bật ví trước Lưu, R71/R73 yêu cầu form trống khi quay lại; cần UX/PM xác nhận cách thể hiện mà không giữ bản nháp trái nguồn. **Phân loại:** Chi tiết UX/PM phải giữ R71/R73; không tự khôi phục bản nháp. **Trạng thái:** chờ CapmmPM rà; chưa được Ninh xác nhận riêng nếu cần quyết định sản phẩm.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định và cập nhật nguồn trước `BA_READY`.
- `BA_READY` chưa được đề xuất hoặc xác nhận; `TECH_READY` chưa thiết kế/review.
