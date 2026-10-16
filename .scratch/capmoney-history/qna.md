# Q&A: Story 10 – Lịch sử chung và lọc theo ví

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/10-lich-su-chung-va-loc-vi.md). Mã liên quan: `R07, R59–R60; B01, B17; F15–F17`. Các câu dưới đây chưa có trả lời mới của Ninh; không biến phương án BA/Tech thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. CapmmPM đã kiểm AC và ví dụ với nguồn gốc trong [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).

## Chưa rõ hoặc cần review

- Q1: Có cần Ninh chốt nội dung hiển thị tối thiểu khác ngoài ngày, số tiền và ví để người dùng phân biệt hai khoản cùng số tiền? Issue #10 chỉ nêu ba dữ kiện; BA không tự thêm AC về nhãn/danh mục. **Phân loại:** Sản phẩm cần PM xác định có chặn BA gate; không tự thêm AC. **Trạng thái:** CapmmPM đã phân loại là không chặn BA theo AC hiện tại; chi tiết UX/Tech còn chờ thiết kế, và phải hỏi Ninh nếu phát sinh quyết định sản phẩm mới.
- Q2: Với lọc ví, bộ lọc có cần được giữ khi người dùng rời rồi quay lại màn lịch sử không? Chưa có quyết định nguồn; không chặn logic lọc/tính toán nhưng ảnh hưởng UX. **Phân loại:** Chi tiết UX, PM/UX có thể đề xuất; không chặn quy tắc lọc. **Trạng thái:** CapmmPM đã phân loại là không chặn BA theo AC hiện tại; chi tiết UX/Tech còn chờ thiết kế, và phải hỏi Ninh nếu phát sinh quyết định sản phẩm mới.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định, cập nhật nguồn và rà lại `BA_READY`.
- `BA_READY` đạt ngày 16/10/2026 theo [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md); `TECH_READY` chưa thiết kế/review.
