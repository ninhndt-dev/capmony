# Bước 9, vòng làm rõ #09: chọn ví từ nút chung và bản nhập khoản chi

**Trạng thái:** Ninh chọn **A cho Q1** và **A cho Q2** trên giao diện ngày 14/10/2026. Ghi thành R72–R73 cho **khoản chi thuộc Story #09**; BA cập nhật đặc tả và PM đã xác nhận `BA_READY`.

**Nguồn trước khi hỏi:** [Story #09](../../.scratch/capmoney-expense/story.md), [Q&A #09](../../.scratch/capmoney-expense/qna.md), [F14/F08/F21](user-flows.md), [issue #09](../../.scratch/capmoney-overall/issues/09-ghi-chi.md), [R70–R71 riêng khoản thu](42-step-9-income-round-1.md). F14 chốt chọn sẵn khi vào từ ví; R70–R71 trước đó chỉ xác nhận cho khoản thu.

## Câu hỏi và phương án đã đưa Ninh

| Mã | Câu hỏi | A (BA đề xuất) | B |
| --- | --- | --- | --- |
| Q1 | Mở ghi **khoản chi** từ nút ghi chung khi có từ hai ví đang sử dụng: ví trên biểu mẫu ban đầu được chọn thế nào? | Chưa chọn ví; người dùng tự chọn một ví đang sử dụng trước Lưu, giống cách đã chọn cho khoản thu ở R70. | Chọn sẵn ví đang sử dụng **được tạo sớm nhất**; vẫn đổi được trước Lưu. |
| Q2 | Đang nhập **khoản chi**, chưa bấm Lưu, rời biểu mẫu sang trang khác rồi quay lại trong cùng phiên có Internet: bản nhập dở thế nào? | Mở biểu mẫu trống; chưa tạo khoản chi hay đổi số dư, giống cách đã chọn cho khoản thu ở R71. | Giữ bản nhập dở để tiếp tục; chưa tạo khoản chi hay đổi số dư. |

## Nhật ký trả lời và tác động

- **Ninh đã xác nhận Q1=A → R72:** khi mở ghi **khoản chi** từ nút chung và có từ hai ví đang sử dụng, biểu mẫu ban đầu **chưa chọn ví**; người dùng tự chọn một ví đang sử dụng trước khi Lưu. Vào từ ví vẫn theo F14: chọn sẵn ví ấy và đổi được. **Ví dụ:** có “Tiền mặt” và “Ngân hàng”, mở ghi chi từ nút chung; bấm Lưu khi chưa chọn ví không tạo khoản chi, chọn “Ngân hàng” rồi Lưu thì khoản chi gắn “Ngân hàng”.
- **Ninh đã xác nhận Q2=A → R73:** rời biểu mẫu **khoản chi** trước Lưu rồi quay lại trong cùng phiên có Internet thì biểu mẫu **trống**; phần nhập dở không tạo khoản chi hoặc đổi số dư. **Ví dụ:** nhập chi 120.000 VND vào “Ngân hàng” rồi chuyển trang trước Lưu; quay lại cùng phiên thấy form trống, số dư ví không giảm. F21 tiếp tục xử lý đóng hẳn trình duyệt trước Lưu; F09/F25/R64 áp dụng sau khi đã bấm Lưu mà kết quả chưa rõ.

## Ranh giới quyết định

- R70–R71 là quyết định riêng cho **khoản thu**; R72–R73 là quyết định riêng cho **khoản chi**. Việc hai cặp có cùng hành vi không tự mở rộng quy tắc sang **chuyển tiền** hoặc **tạo ví**; các Story ấy dùng nguồn riêng.
- **BA/Tech đề xuất hoặc chưa rõ ở mức thiết kế:** bố cục, câu chữ, cách trình bày khi từ nút chung chỉ có một ví, hợp đồng lưu và kiểm chứng lỗi mạng. Các chi tiết không thay đổi quy tắc phải có ví hợp lệ trước Lưu. Không còn câu hỏi sản phẩm Q1–Q2 cho Story #09; PM đã xác nhận `BA_READY` trong [story](../../.scratch/capmoney-expense/story.md) và [handoff](../../.scratch/capmoney-expense/handoff.md).
