# Q&A: Story 07 – Thêm ví

Ninh đã trả lời trên giao diện ngày 14/10/2026: **Q1=B, Q2=A**. Câu trả lời được ghi thành [R68–R69](../../docs/requirements/00-intake.md) trong [nhật ký vòng #07](../../docs/requirements/41-step-9-add-wallet-round-1.md). Nhãn “A (Bờm đề xuất)” dưới đây mô tả lựa chọn lúc hỏi; trạng thái xác nhận nằm ở cột riêng.

| Mã | Câu hỏi cần Ninh chọn | A (Bờm đề xuất) | B | Trạng thái | Nguồn trả lời và tác động |
| --- | --- | --- | --- | --- | --- |
| Q1 | Khi đang thêm ví thứ hai trở đi, chuyển sang trang khác **trước khi bấm Lưu** rồi quay lại trong cùng phiên có Internet, nội dung đã nhập thế nào? | Giữ bản nhập dở để tiếp tục; chưa tạo ví cho tới khi Lưu thành công, như F18 của ví đầu tiên. | Mở lại biểu mẫu trống; chưa tạo ví. | **Ninh đã xác nhận B → R68.** | [Nhật ký #07](../../docs/requirements/41-step-9-add-wallet-round-1.md); AC7 mở biểu mẫu trống, khác F18 của ví đầu tiên. |
| Q2 | Đã bấm Lưu ví thêm rồi đóng trình duyệt khi kết quả chưa rõ; mở lại bằng cùng tài khoản có Internet thì xử lý thế nào? | Áp dụng cách R67 của ví đầu tiên: tự đối chiếu đúng thao tác, chỉ báo đã/chưa lưu khi chắc; nếu chắc chưa lưu mở biểu mẫu trống; nếu còn chưa rõ hiện đang kiểm tra và chặn tạo trùng. | Chỉ tải danh sách ví đã xác nhận lưu; nếu chưa thấy ví thì mở biểu mẫu trống, nhưng vẫn nhận diện và chặn thao tác cũ còn chờ để không tạo ví trùng. | **Ninh đã xác nhận A → R69.** | [Nhật ký #07](../../docs/requirements/41-step-9-add-wallet-round-1.md); AC8 tự đối chiếu đúng thao tác, chỉ báo khi chắc, chặn lưu trùng lúc chưa rõ. |

## Ranh giới quyết định

- **Ninh đã xác nhận:** R66 bắt buộc tên ví; F21 mở biểu mẫu trống sau khi đóng trình duyệt **trước khi bấm Lưu**. R68 chọn B: rời form ví thêm trước Lưu rồi quay lại cùng phiên cũng mở trống. R69 chọn A: sau khi bấm Lưu ví thêm rồi đóng khi chưa rõ, mở lại tự đối chiếu đúng thao tác; chắc chưa lưu thì form trống, còn chưa rõ thì hiện kiểm tra và chặn trùng. F18/R67 vẫn là quyết định riêng cho ví đầu tiên.
- **BA/Tech đề xuất hoặc chưa rõ ở mức thiết kế:** quy tắc tên trùng, chuẩn hóa tên, giới hạn độ dài, câu chữ thông báo và công nghệ giữ định danh thao tác/đối chiếu; không thay đổi R68–R69. Nếu đề xuất tạo hạn chế sản phẩm mới, BA/PM hỏi Ninh.
- **Ranh giới #07:** F20 là bối cảnh tích hợp #18, không phải câu hỏi hay tiêu chí điều hướng của Story thêm ví; quyền sửa ví thuộc #16.
- Q1–Q2 không còn chặn BA. [Spec](spec.md), [story](story.md) và [handoff](handoff.md) đã dẫn R68–R69; CapmmPM đã xác nhận `BA_READY` ngày 14/10/2026. `TECH_READY` chưa đạt.
