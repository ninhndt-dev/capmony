# Bước 5, vòng 6: kết quả lưu khi đóng trình duyệt

**Trạng thái:** Đã trả lời ngày 13/10/2026. BA, Tech và PM đã rà soát trước khi hỏi; Ninh chọn B cho Q1.

**Phụ thuộc đã chốt:** [Phiếu luồng người dùng](user-flows.md), [vòng 2 bước 5](27-step-5-user-flows-round-2.md), [vòng 5 bước 5](30-step-5-user-flows-round-5.md).

❓ **Q1 – Đã bấm Lưu rồi đóng trình duyệt khi kết quả chưa rõ:** Khi mở lại cùng tài khoản với Internet, ứng dụng nên giúp người dùng biết giao dịch đã lưu hay chưa thế nào? Trường hợp này khác việc đóng trước khi bấm Lưu ở F21.

➡️ **A (Bờm đề xuất):** Hiện thao tác đang chờ và tự đối chiếu; chỉ báo đã lưu hoặc chưa lưu khi kiểm tra được kết quả. Nếu xác nhận chưa lưu, giữ nội dung để người dùng thử lại an toàn. **B:** Sau khi đóng trình duyệt, tự đối chiếu; chỉ báo đã lưu hoặc chưa lưu khi kiểm tra được kết quả. Nếu xác nhận chưa lưu, mở biểu mẫu trống để người dùng nhập lại. Đây là ngoại lệ chỉ sau khi đóng trình duyệt; F09 vẫn giữ nội dung khi phiên còn mở. **Cả hai:** Nếu vẫn chưa xác định được thì tiếp tục báo “chưa rõ”, không cho tạo lần lưu trùng; không báo thành công trước khi có xác nhận.

## Nhật ký trả lời

Ninh trả lời **“B”** cho Q1. Đã chuyển lựa chọn vào [phiếu luồng người dùng](user-flows.md).

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Nếu đã bấm Lưu rồi đóng trình duyệt khi kết quả chưa rõ, lúc mở lại bằng cùng tài khoản có Internet ứng dụng tự đối chiếu; chỉ báo đã lưu/chưa lưu khi kiểm tra được. Nếu xác nhận chưa lưu, mở biểu mẫu trống để nhập lại. Nếu vẫn chưa xác định được thì tiếp tục báo “chưa rõ”, không báo thành công sớm hoặc tạo bản trùng. Đây là ngoại lệ sau khi đóng trình duyệt; trong phiên còn mở vẫn giữ nội dung theo F09. | Cách ghi nhớ thao tác chờ và hiển thị tiến trình sẽ xác định ở bước đặc tả; không tự suy ra công nghệ. |

## Phụ thuộc cho tổng hợp bước 5

- Sau câu trả lời, cập nhật bản tổng hợp; BA → Tech → PM rà soát lại nếu quyết định làm đổi nhánh luồng.
- Ghi riêng hệ quả của hạn lời mời 3 ngày: nếu hết hạn giữa lúc nhập mã thì không thể hoàn tất bằng lời mời cũ và chỉ Ninh có quyền mời lại. Đây là suy luận từ R41/F01/F03, sẽ đưa Ninh xác nhận trong bản tổng hợp; thời điểm hiện thông báo cụ thể để bước đặc tả quyết định.
- Đưa Ninh bản tóm tắt các luồng đã chốt và điều còn mở để xác nhận cách hiểu trước khi chuyển bước 6.
