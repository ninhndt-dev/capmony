# Q&A: Story 16 – Sửa tên và số dư ban đầu của ví

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/16-sua-thong-tin-vi.md). Mã liên quan: `R30, R40, R44; B01, B10, B15, B26, B29, B31`. Các câu dưới đây chưa có trả lời mới của Ninh; không biến phương án BA/Tech thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. PM cần kiểm lại từng AC và ví dụ với nguồn gốc.

## Chưa rõ hoặc cần review

- Q1: Tên ví trống khi **sửa** có bị từ chối như khi tạo theo R66 không? R66 chỉ nói tạo; cần Ninh/BA quyết định trước BA_READY nếu muốn AC cụ thể. **Phân loại:** Câu hỏi sản phẩm có thể chặn BA gate; R66 chỉ áp dụng lúc tạo. **Trạng thái:** chờ CapmmPM rà; chưa được Ninh xác nhận riêng nếu cần quyết định sản phẩm.
- Q2: Có cho hai ví cùng tên không? Chưa có quyết định; không tự đặt quy tắc duy nhất tên ví. **Phân loại:** Câu hỏi sản phẩm có thể chặn BA gate; không tự đặt duy nhất tên ví. **Trạng thái:** chờ CapmmPM rà; chưa được Ninh xác nhận riêng nếu cần quyết định sản phẩm.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định và cập nhật nguồn trước `BA_READY`.
- `BA_READY` chưa được đề xuất hoặc xác nhận; `TECH_READY` chưa thiết kế/review.
