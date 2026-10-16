# Q&A: Story 15 – Xóa mềm và khôi phục lần chuyển

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/15-xoa-va-khoi-phuc-lan-chuyen.md). Mã liên quan: `R26, R33, R37, R46, R50–R51, R55; B01, B13, B22, B30; F17, F19`. Các câu dưới đây chưa có trả lời mới của Ninh; không biến phương án BA/Tech thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. PM cần kiểm lại từng AC và ví dụ với nguồn gốc.

## Chưa rõ hoặc cần review

- Q1: Khi kết quả xóa/khôi phục chưa rõ do mạng, thông báo và cách đối chiếu cần Tech đề xuất; F25 không chốt riêng thao tác này. **Phân loại:** Tech/PM cần thiết kế nhánh lỗi; nếu đổi hành vi thì hỏi Ninh. **Trạng thái:** chờ CapmmPM rà; chưa được Ninh xác nhận riêng nếu cần quyết định sản phẩm.
- Q2: Cách đặt mục đã xóa và nội dung xác nhận chưa được Ninh chốt; F19/R50 chỉ chốt hành vi. **Phân loại:** Chi tiết UX; hành vi đã chốt ở F19/R50. **Trạng thái:** chờ CapmmPM rà; chưa được Ninh xác nhận riêng nếu cần quyết định sản phẩm.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định và cập nhật nguồn trước `BA_READY`.
- `BA_READY` chưa được đề xuất hoặc xác nhận; `TECH_READY` chưa thiết kế/review.
