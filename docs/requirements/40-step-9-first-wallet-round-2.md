# Bước 9, vòng làm rõ #06: kết quả Lưu ví sau khi đóng trình duyệt

**Trạng thái:** Ninh chọn A cho Q2 trên giao diện ngày 14/10/2026. Quyết định được ghi là R67; Tech/PM còn rà đặc tả #06.

**Nguồn:** [Ticket #06](../../.scratch/capmoney-overall/issues/06-tao-vi-dau-tien.md), [F21/F25](user-flows.md), [vòng 5](30-step-5-user-flows-round-5.md), [vòng 6](31-step-5-user-flows-round-6.md). BA → PM xác nhận câu hỏi gốc F21 chốt đóng **trước Lưu** cho ví/giao dịch; F25/R64 hỏi **giao dịch** sau Lưu, chưa chốt riêng cho ví.

❓ **Q2 – Đã bấm Lưu ví đầu tiên rồi đóng trình duyệt khi kết quả chưa rõ:** Khi mở lại cùng tài khoản, CapMoney xử lý thế nào?

➡️ **A (Bờm đề xuất):** Tự đối chiếu đúng thao tác; chỉ báo đã/chưa lưu khi biết chắc. Nếu chắc chưa lưu, mở biểu mẫu trống; nếu vẫn chưa rõ, hiện đang kiểm tra và chặn tạo ví trùng. **B:** Chỉ tải danh sách ví đã xác nhận lưu; nếu chưa thấy ví thì mở biểu mẫu trống, nhưng vẫn nhận diện và chặn thao tác cũ còn chờ để không tạo ví trùng.

## Nhật ký trả lời

Ninh chọn **A** qua giao diện. Với **ví đầu tiên đã bấm Lưu**, nếu người dùng đóng trình duyệt trước khi biết kết quả, khi mở lại bằng cùng tài khoản có Internet CapMoney tự đối chiếu **đúng thao tác Lưu đó**. Chỉ báo đã lưu/chưa lưu khi biết chắc; nếu chắc chưa lưu, mở biểu mẫu trống; nếu còn chưa rõ, hiện đang kiểm tra và chặn lưu trùng. Đây là **Ninh đã xác nhận** thành R67, không còn là đề xuất. Quy tắc bản nhập dở **chưa bấm Lưu** vẫn theo F18/F21; F25/R64 ban đầu hỏi giao dịch, không tự mở rộng R67 sang mọi thao tác khác.

**Chưa rõ ở mức Tech/thiết kế:** cách giữ định danh thao tác Lưu qua lần đóng/mở, cách đối chiếu khi phản hồi từ nơi lưu chậm hoặc lỗi, và câu chữ trạng thái trên màn hình; phải chứng minh trước khi giao Dev.
