# Bước 9, vòng làm rõ #10: rời biểu mẫu chuyển tiền trước Lưu

**Trạng thái:** Ninh chọn **mở trống như thu/chi** trên giao diện ngày 16/10/2026. Quyết định được ghi thành R74 cho Story #13; BA cập nhật đặc tả, PM chưa rà gate của Story.

**Nguồn trước khi hỏi:** [Story #13](../../.scratch/capmoney-overall/issues/13-chuyen-tien-giua-vi.md), [luồng người dùng](user-flows.md), [R71 cho khoản thu](42-step-9-income-round-1.md) và [R73 cho khoản chi](43-step-9-expense-round-1.md). Các quyết định trước đó chưa nói rõ việc rời biểu mẫu **chuyển tiền** trước Lưu rồi quay lại trong cùng phiên.

## Câu hỏi và câu trả lời

| Câu hỏi | Các phương án đã đưa | Ninh chọn |
| --- | --- | --- |
| Đang nhập lần chuyển tiền, chưa bấm Lưu, rời biểu mẫu rồi quay lại trong cùng phiên có Internet thì giữ phần nhập dở hay mở trống? | Mở trống như thu/chi (đề xuất); giữ phần đã nhập. | **Mở trống như thu/chi.** |

## R74 – Quy tắc đã xác nhận

- Khi rời biểu mẫu **chuyển tiền trước khi bấm Lưu** rồi quay lại trong cùng phiên có Internet, biểu mẫu mở **trống**. Phần nhập dở không tạo lần chuyển hay thay đổi số dư của ví gửi và ví nhận.
- Ví dụ: đã nhập chuyển 120.000 VND từ “Tiền mặt” sang “Ngân hàng”, sau đó chuyển trang trước Lưu; khi quay lại cùng phiên, hai ví và số tiền trong biểu mẫu đều trống, số dư hai ví không đổi.
- F21 vẫn áp dụng khi đóng hẳn trình duyệt **trước Lưu**. F09/F25/R64 áp dụng cho **giao dịch đã bấm Lưu** nhưng kết quả chưa rõ; R74 không yêu cầu xóa trạng thái đang đối chiếu của thao tác đã gửi.

## Ranh giới

R74 chỉ chốt nhánh rời biểu mẫu chuyển tiền trước Lưu. Vị trí nút chuyển tiền, câu chữ và cơ chế giữ trạng thái thao tác đã gửi là chi tiết UX/Tech cần rà ở thiết kế, không phải lựa chọn sản phẩm mới trong câu trả lời này.
