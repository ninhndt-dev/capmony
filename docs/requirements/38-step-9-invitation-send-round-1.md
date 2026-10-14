# Bước 9, vòng làm rõ #03: gửi lại lời mời còn hạn

**Trạng thái:** Ninh chọn A cho Q1 trên giao diện ngày 14/10/2026. BA và PM đã đối chiếu phạm vi; quyết định được ghi là R65 trong [phiếu tiếp nhận](00-intake.md). Cách thực hiện kỹ thuật còn chờ Tech kiểm chứng.

**Nguồn:** [Ticket #03](../../.scratch/capmoney-overall/issues/03-gui-loi-moi.md), [người dùng và quyền](actors-and-roles.md), [F03/F07/F26](user-flows.md).

❓ **Q1 – Gửi lại lời mời chưa dùng, còn hạn:** Khi lời mời chưa dùng, vẫn còn hạn 3 ngày nhưng người nhận không thấy email, Ninh muốn xử lý thế nào?

➡️ **A (Bờm đề xuất):** Gửi lại cùng liên kết, giữ mốc hết hạn ban đầu. **B:** Chờ lời mời hết hạn rồi gửi lời mời mới.

## Nhật ký trả lời

Ninh chọn **A** qua giao diện: Ninh được gửi lại **chính liên kết của lời mời đó** tới cùng email; lần gửi lại không tạo lời mời mới hoặc gia hạn mốc 3 ngày. Đây là **Ninh đã xác nhận**, không còn là đề xuất. Việc xin lại mã xác minh [F07] là luồng khác; sau khi lời mời hết hạn vẫn áp dụng [F03/F26] và chỉ Ninh gửi lời mời mới.

**Chưa rõ:** Câu trả lời này không tự chốt thao tác gửi lại khi người nhận đã mở liên kết và đang nhập mã dở, hoặc nội dung thông báo chi tiết. Cách giữ và gửi lại đúng URL, xử lý lỗi gửi/đồng thời và giới hạn tần suất là quyết định Tech cần kiểm chứng trước khi #03 sẵn sàng giao Dev.
