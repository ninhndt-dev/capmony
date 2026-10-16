# Q&A: Story 14 – Sửa lần chuyển

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/14-sua-lan-chuyen.md). Mã liên quan: `B01–B02, B04, B08–B09, B12, B14, B20–B21, B30; R42–R43; F17`. Các câu dưới đây chưa có trả lời mới của Ninh; không biến phương án BA/Tech thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. PM cần kiểm lại từng AC và ví dụ với nguồn gốc.

## Chưa rõ hoặc cần review

- Q1: Khi sửa một lần chuyển gắn ví ngừng, có cho thay ngày/ghi chú nhưng không đổi số dư theo B21; Tech cần nêu cách kiểm tổng thể và PM xác nhận đúng phạm vi. **Phân loại:** Tech/PM kiểm hợp đồng; B21 là nguồn sản phẩm đã chốt. **Trạng thái:** chờ CapmmPM rà; chưa được Ninh xác nhận riêng nếu cần quyết định sản phẩm.
- Q2: Nhánh mất kết nối sau Lưu sửa chưa có quy tắc UI/đối chiếu riêng; BA/PM xem có cần hỏi Ninh ngoài bất biến không báo thành công sai. **Phân loại:** Tech/PM cần thiết kế nhánh lỗi; nếu đổi hành vi thì hỏi Ninh. **Trạng thái:** chờ CapmmPM rà; chưa được Ninh xác nhận riêng nếu cần quyết định sản phẩm.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định và cập nhật nguồn trước `BA_READY`.
- `BA_READY` chưa được đề xuất hoặc xác nhận; `TECH_READY` chưa thiết kế/review.
