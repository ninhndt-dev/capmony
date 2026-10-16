# Q&A: Story 13 – Chuyển tiền giữa hai ví

**Nguồn đã xác nhận:** [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); [issue nguồn](../capmoney-overall/issues/13-chuyen-tien-giua-vi.md). Mã liên quan: `R24, R42, R43, R51, R60, R74; B01–B02, B04, B09, B12, B23, B25; F17`. Không biến phương án BA/Tech thành quyết định.

## Đã có quyết định sản phẩm

- Phạm vi và hành vi ghi tại [spec](spec.md) dựa trên mã R/B/F/H đã xác nhận. CapmmPM đã kiểm AC và ví dụ với nguồn gốc trong [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).
- Ninh chọn **mở trống như thu/chi** khi rời biểu mẫu chuyển tiền **trước Lưu**, quay lại cùng phiên có Internet. Quyết định [R74](../../docs/requirements/44-step-9-transfer-round-1.md) không áp dụng sau khi đã bấm Lưu mà kết quả chưa rõ.

## Chưa rõ hoặc cần review

- Q1 **đã chốt R75:** Từ nút chung, cả ví gửi và ví nhận ban đầu trống; người dùng chọn đủ hai ví đang sử dụng, khác nhau, trước Lưu. [Nguồn](../../docs/requirements/45-step-9-transfer-wallet-selection.md). F14 và R70–R73 chỉ nói thu/chi, không dùng để suy điểm vào chuyển tiền từ một ví.
- Q2 **đã chốt R74:** Rời form chuyển trước Lưu rồi quay lại cùng phiên có Internet thì mở trống, không tạo lần chuyển hay đổi số dư. Sau bấm Lưu, F09/F25/R64 vẫn áp dụng.

## Ranh giới

- Khi câu hỏi chỉ liên quan cách thể hiện, không tự mở rộng AC. Khi ảnh hưởng hành vi nghiệm thu hoặc quyền, PM phải xin quyết định, cập nhật nguồn và rà lại `BA_READY`.
- `BA_READY` đạt ngày 16/10/2026 theo [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md); `TECH_READY` chưa thiết kế/review.
