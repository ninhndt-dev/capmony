# Bước 9, vòng làm rõ #08: chọn ví từ nút chung và bản nhập khoản thu

**Trạng thái:** Ninh chọn **A cho Q1** và **B cho Q2** trên giao diện ngày 14/10/2026. Hai câu trả lời được ghi thành R70–R71 cho **khoản thu thuộc Story #08**; BA cập nhật đặc tả và PM đã xác nhận `BA_READY`. Thiết kế còn `STORY_TECH_GAP`.

**Nguồn trước khi hỏi:** [Story #08](../../.scratch/capmoney-income/story.md), [Q&A #08](../../.scratch/capmoney-income/qna.md), [F14/F08/F21](user-flows.md), [issue #08](../../.scratch/capmoney-overall/issues/08-danh-muc-mac-dinh-va-ghi-thu.md). F14 đã chốt chọn sẵn khi vào từ ví, chưa chốt mặc định từ nút chung. F08 chốt giữ bản nhập khi điện thoại thứ hai tự tải dữ liệu; F21 chốt mở trống sau **đóng trình duyệt** trước Lưu, chưa trả lời trường hợp **rời form trong cùng phiên**.

## Câu hỏi và phương án đã đưa Ninh

| Mã | Câu hỏi | A (BA đề xuất) | B |
| --- | --- | --- | --- |
| Q1 | Mở ghi **khoản thu** từ nút ghi chung khi có từ hai ví đang sử dụng: ví trên biểu mẫu ban đầu được chọn thế nào? | Chưa chọn ví; người dùng tự chọn ví đang sử dụng trước khi Lưu. | Chọn sẵn ví đang sử dụng gắn với khoản thu/chi được tạo gần nhất; nếu chưa có khoản thu/chi ở ví đang sử dụng thì chọn ví đang sử dụng tạo sớm nhất. Vẫn đổi được. |
| Q2 | Đang nhập **khoản thu**, chưa bấm Lưu, rời biểu mẫu sang trang khác rồi quay lại trong cùng phiên có Internet: bản nhập dở thế nào? | Giữ bản nhập để tiếp tục; chưa tạo khoản thu hay đổi số dư. | Mở biểu mẫu trống; chưa tạo khoản thu hay đổi số dư. |

## Nhật ký trả lời và tác động

- **Ninh đã xác nhận Q1=A → R70:** từ nút ghi chung với từ hai ví đang sử dụng, biểu mẫu **khoản thu** ban đầu chưa chọn ví; người dùng phải tự chọn một ví đang sử dụng trước khi Lưu. Khi vào từ ví, F14 vẫn chọn sẵn ví đó và cho đổi ví. **Ví dụ:** có ví “Tiền mặt” và “Ngân hàng”, bấm nút ghi chung để ghi thu 120.000 VND; nếu chưa chọn ví thì không Lưu, chọn “Ngân hàng” thì khoản thu gắn “Ngân hàng”.
- **Ninh đã xác nhận Q2=B → R71:** rời biểu mẫu **khoản thu** trước khi bấm Lưu rồi quay lại trong cùng phiên có Internet thì biểu mẫu trống; phần nhập dở không tạo khoản thu hay đổi số dư. **Ví dụ:** nhập thu 120.000 VND vào “Ngân hàng” rồi chuyển trang trước Lưu; quay lại cùng phiên, các trường nhập dở trống, số dư “Ngân hàng” không tăng. F21 vẫn áp dụng nếu đóng hẳn trình duyệt trước Lưu; F09/F25/R64 chỉ áp dụng sau khi đã bấm Lưu mà kết quả chưa rõ.

## Phạm vi còn mở

- **Không suy R70–R71 cho khoản chi #09.** F14 vẫn là quy tắc điểm vào chung cho thu/chi; cách mặc định từ nút chung và rời biểu mẫu **chi** cùng phiên cần được xác nhận khi đặc tả #09 nếu ảnh hưởng AC của Story đó.
- **BA/Tech đề xuất, chưa được Ninh xác nhận riêng:** bố cục, câu chữ, độ dài ghi chú, cách trình bày ngày/số tiền và hợp đồng lưu. Quy tắc lỗi mạng/đối chiếu đã có F09/F25/R64, nhưng thiết kế và kiểm chứng các lát #33/#35 chưa hoàn tất.
- Q1–Q2 của Story #08 không còn là câu hỏi sản phẩm mở; PM đã rà bản BA sau trả lời và xác nhận `BA_READY` trong [story](../../.scratch/capmoney-income/story.md) và [handoff](../../.scratch/capmoney-income/handoff.md).
