# Q&A: Story 07 – Thêm ví

Hai câu đã gửi Ninh qua giao diện; **chưa có câu trả lời được ghi trong repo**. A là đề xuất, chưa phải quyết định đã xác nhận. F18 và R67 chỉ chốt cho **ví đầu tiên**; F21 áp dụng cho ví trước khi bấm Lưu.

| Mã | Câu hỏi cần Ninh chọn | A (Bờm đề xuất) | B | Trạng thái | Nguồn trả lời và tác động |
| --- | --- | --- | --- | --- | --- |
| Q1 | Khi đang thêm ví thứ hai trở đi, chuyển sang trang khác **trước khi bấm Lưu** rồi quay lại trong cùng phiên có Internet, nội dung đã nhập thế nào? | Giữ bản nhập dở để tiếp tục; chưa tạo ví cho tới khi Lưu thành công, như F18 của ví đầu tiên. | Mở lại biểu mẫu trống; chưa tạo ví. | Đã gửi qua giao diện, **chờ Ninh trả lời**. | Chưa có nguồn trả lời. Ảnh hưởng luồng ngoại lệ, AC bản nháp và phần trạng thái trong [spec](spec.md). |
| Q2 | Đã bấm Lưu ví thêm rồi đóng trình duyệt khi kết quả chưa rõ; mở lại bằng cùng tài khoản có Internet thì xử lý thế nào? | Áp dụng cách R67 của ví đầu tiên: tự đối chiếu đúng thao tác, chỉ báo đã/chưa lưu khi chắc; nếu chắc chưa lưu mở biểu mẫu trống; nếu còn chưa rõ hiện đang kiểm tra và chặn tạo trùng. | Chỉ tải danh sách ví đã xác nhận lưu; nếu chưa thấy ví thì mở biểu mẫu trống, nhưng vẫn nhận diện và chặn thao tác cũ còn chờ để không tạo ví trùng. | Đã gửi qua giao diện, **chờ Ninh trả lời**. | Chưa có nguồn trả lời. Ảnh hưởng phục hồi sau đóng trình duyệt, AC kết quả Lưu và điểm Tech phải chứng minh. |

## Ranh giới quyết định

- **Ninh đã xác nhận:** R66 bắt buộc tên ví; F21 mở biểu mẫu trống sau khi đóng trình duyệt **trước khi bấm Lưu**. R67 xử lý **ví đầu tiên** sau khi bấm Lưu; chưa tự áp dụng cho ví thêm.
- **BA/Tech đề xuất, chưa được Ninh xác nhận:** phương án A của Q1–Q2. Quy tắc tên trùng, chuẩn hóa tên, giới hạn độ dài, thông báo và công nghệ giữ bản nháp/đối chiếu thuộc bước thiết kế; nếu đề xuất tạo hạn chế sản phẩm mới, BA/PM phải hỏi Ninh.
- **Ranh giới #07:** F20 là bối cảnh tích hợp #18, không phải câu hỏi hay tiêu chí điều hướng của Story thêm ví; quyền sửa ví thuộc #16.
- Chỉ chuyển Q1–Q2 sang mục **Ninh đã xác nhận** khi có câu trả lời và nguồn rõ; cập nhật [spec](spec.md), [story](story.md) và [handoff](handoff.md) trước khi xét `BA_READY`.
