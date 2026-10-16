# Q&A: Story 11 – Sửa khoản thu/chi

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/11-sua-thu-chi.md). Mã liên quan: `B01–B03, B07, B12, B14, B20–B21, B30; R43, R59; F22`. Các câu dưới đây chưa có trả lời mới của Ninh; không biến phương án BA/Tech thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. CapmmPM đã kiểm AC và ví dụ với nguồn gốc trong [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).

## Chưa rõ hoặc cần review

- Q1: Bố cục form sửa và câu chữ lỗi ví ngừng/bản cũ chưa được Ninh chốt; BA giữ hành vi B13/B20/B30, không biến UI đề xuất thành AC. **Phân loại:** Chi tiết UX/Tech; giữ hành vi đã xác nhận. **Trạng thái:** CapmmPM đã phân loại là không chặn BA theo AC hiện tại; chi tiết UX/Tech còn chờ thiết kế, và phải hỏi Ninh nếu phát sinh quyết định sản phẩm mới.
- Q2: Thời điểm kiểm ngày “hôm nay” theo múi giờ nào là quyết định kỹ thuật có thể ảnh hưởng R43; Tech cần đề xuất và PM/BA đối chiếu trước gate. **Phân loại:** Tech phải đề xuất múi giờ rồi BA/PM đối chiếu R43; có thể ảnh hưởng gate kỹ thuật. **Trạng thái:** CapmmPM đã phân loại là không chặn BA theo AC hiện tại; chi tiết UX/Tech còn chờ thiết kế, và phải hỏi Ninh nếu phát sinh quyết định sản phẩm mới.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định, cập nhật nguồn và rà lại `BA_READY`.
- `BA_READY` đạt ngày 16/10/2026 theo [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md); `TECH_READY` chưa thiết kế/review.
