# Q&A: Story 12 – Xóa mềm và khôi phục khoản thu/chi

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/12-xoa-va-khoi-phuc-thu-chi.md). Mã liên quan: `R26, R33, R37, R46, R50; B01, B13, B22, B30; F19`. Các câu dưới đây chưa có trả lời mới của Ninh; không biến phương án BA/Tech thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. PM cần kiểm lại từng AC và ví dụ với nguồn gốc.

## Chưa rõ hoặc cần review

- Q1: Khi kết quả xóa/khôi phục chưa rõ do mất kết nối, UI và hợp đồng đối chiếu cần thiết kế riêng; F25 không trực tiếp xác nhận nhánh này. BA/PM cần xem liệu có câu hỏi sản phẩm mới. **Phân loại:** Tech/PM cần thiết kế nhánh lỗi; nếu đổi hành vi đã xác nhận thì hỏi Ninh. **Trạng thái:** chờ CapmmPM rà; chưa được Ninh xác nhận riêng nếu cần quyết định sản phẩm.
- Q2: Câu chữ cảnh báo xóa và mục đã xóa chưa được Ninh duyệt; F19 chỉ chốt bước xác nhận. **Phân loại:** Chi tiết UX; bước xác nhận đã chốt ở F19. **Trạng thái:** chờ CapmmPM rà; chưa được Ninh xác nhận riêng nếu cần quyết định sản phẩm.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định và cập nhật nguồn trước `BA_READY`.
- `BA_READY` chưa được đề xuất hoặc xác nhận; `TECH_READY` chưa thiết kế/review.
