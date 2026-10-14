# Bước 9, vòng làm rõ #07: bản nhập dở và kết quả Lưu ví thêm

**Trạng thái:** Ninh chọn B cho Q1 và A cho Q2 trên giao diện ngày 14/10/2026. Ghi thành R68–R69; BA/PM cập nhật Story #07 theo workflow CapMM.

**Nguồn:** [Story #07](../../.scratch/capmoney-add-wallet/story.md), [Q&A #07](../../.scratch/capmoney-add-wallet/qna.md), F18/F21/F25 và R67. F18/R67 trước đó chỉ chốt cho ví đầu tiên; không suy áp cho ví thêm trước câu trả lời này.

❓ **Q1 – Rời biểu mẫu thêm ví trước Lưu:** Khi đang tạo ví thêm, rời biểu mẫu trước khi bấm Lưu rồi quay lại trong cùng phiên có Internet, CapMoney nên làm gì?

➡️ **A (Bờm đề xuất):** Giữ phần đã nhập để tiếp tục. **B:** Mở lại biểu mẫu trống.

❓ **Q2 – Lưu ví thêm chưa rõ rồi đóng trình duyệt:** Đã bấm Lưu ví thêm rồi đóng trình duyệt khi chưa biết kết quả; mở lại cùng tài khoản thì xử lý thế nào?

➡️ **A (Bờm đề xuất):** Tự kiểm tra đúng thao tác; chỉ báo đã/chưa lưu khi chắc, còn chưa rõ thì chặn lưu trùng. Nếu chắc chưa lưu, mở biểu mẫu trống. **B:** Tải ví đã lưu; nếu chưa thấy thì mở biểu mẫu trống, vẫn chặn thao tác cũ chưa rõ.

## Nhật ký trả lời

Ninh chọn **B cho Q1** qua giao diện. Với **ví thêm**, rời biểu mẫu **trước khi bấm Lưu** rồi quay lại trong cùng phiên có Internet thì biểu mẫu mở **trống**; chưa tạo ví từ phần nhập dở. Đây là **Ninh đã xác nhận** thành R68, khác F18 của **ví đầu tiên** (giữ bản nhập dở). Nếu đã đóng hẳn trình duyệt trước Lưu thì F21 cũng mở biểu mẫu trống.

Ninh chọn **A cho Q2** qua giao diện. Với **ví thêm đã bấm Lưu**, nếu đóng trình duyệt khi kết quả chưa rõ, mở lại bằng cùng tài khoản có Internet thì CapMoney tự đối chiếu **đúng thao tác Lưu đó**. Chỉ báo đã/chưa lưu khi biết chắc; nếu chắc chưa lưu thì biểu mẫu trống; nếu vẫn chưa rõ thì hiện đang kiểm tra và chặn lưu trùng. Đây là **Ninh đã xác nhận** thành R69, cùng kiểu xử lý R67 của ví đầu tiên nhưng có phạm vi riêng cho ví thêm.

**Chưa rõ ở mức Tech:** cách giữ định danh thao tác Lưu qua lần đóng/mở, xử lý lỗi đối chiếu và câu chữ thông báo. Đây không còn là câu hỏi sản phẩm Q1/Q2; phải được thiết kế và kiểm chứng trước `TECH_READY`.
