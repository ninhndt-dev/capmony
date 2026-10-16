# Q&A: Story 14 – Sửa lần chuyển

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/14-sua-lan-chuyen.md). Mã liên quan: `B01–B02, B04, B08–B09, B12, B14, B20–B21, B30; R42–R43; F17`. Các câu dưới đây chưa có trả lời mới của Ninh; không biến phương án BA/Tech thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. CapmmPM đã kiểm AC và ví dụ với nguồn gốc trong [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).

## Chưa rõ hoặc cần review

- Q1: Khi sửa một lần chuyển gắn ví ngừng, có cho thay ngày/ghi chú nhưng không đổi số dư theo B21; Tech cần nêu cách kiểm tổng thể và PM xác nhận đúng phạm vi. **Phân loại:** Tech/PM kiểm hợp đồng; B21 là nguồn sản phẩm đã chốt. **Trạng thái:** CapmmPM đã phân loại là không chặn BA theo AC hiện tại; chi tiết UX/Tech còn chờ thiết kế, và phải hỏi Ninh nếu phát sinh quyết định sản phẩm mới.
- Q2: Nhánh mất kết nối sau Lưu sửa chưa có quy tắc UI/đối chiếu riêng; BA/PM xem có cần hỏi Ninh ngoài bất biến không báo thành công sai. **Phân loại:** Tech/PM cần thiết kế nhánh lỗi; nếu đổi hành vi thì hỏi Ninh. **Trạng thái:** CapmmPM đã phân loại là không chặn BA theo AC hiện tại; chi tiết UX/Tech còn chờ thiết kế, và phải hỏi Ninh nếu phát sinh quyết định sản phẩm mới.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định, cập nhật nguồn và rà lại `BA_READY`.
- `BA_READY` đạt ngày 16/10/2026 theo [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md); `TECH_READY` chưa thiết kế/review.
