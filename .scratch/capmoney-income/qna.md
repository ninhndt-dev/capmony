# Q&A: Story 08 – Danh mục mặc định và ghi khoản thu

Ninh đã chọn **Q1=A, Q2=B** trên giao diện ngày 14/10/2026. Câu trả lời được ghi thành [R70–R71](../../docs/requirements/00-intake.md) trong [nhật ký vòng #08](../../docs/requirements/42-step-9-income-round-1.md). Nhãn “A (BA đề xuất)” chỉ ghi phương án lúc hỏi; cột trạng thái nêu câu Ninh đã chọn.

| Mã | Câu hỏi cần Ninh chọn | A (BA đề xuất) | B | Trạng thái | Nguồn và tác động |
| --- | --- | --- | --- | --- | --- |
| Q1 | Khi mở ghi khoản thu từ **nút ghi chung** và có từ hai ví đang sử dụng, ví trên biểu mẫu ban đầu được chọn thế nào? | Chưa chọn ví; người dùng tự chọn một ví đang sử dụng trước khi Lưu. | Chọn sẵn ví đang sử dụng gắn với khoản thu/chi **được tạo gần nhất**; nếu chưa có khoản thu/chi ở ví đang sử dụng thì chọn ví đang sử dụng **được tạo sớm nhất**. Người dùng vẫn đổi được trước Lưu. | **Ninh đã xác nhận A → R70.** | [Nhật ký #08](../../docs/requirements/42-step-9-income-round-1.md). AC2 yêu cầu chưa chọn ví từ nút chung khi có ≥2 ví; phải chọn trước Lưu. B không được chọn. |
| Q2 | Đang nhập **khoản thu nhưng chưa bấm Lưu**, rời biểu mẫu sang trang khác rồi quay lại trong **cùng phiên ứng dụng có Internet**: phần vừa nhập thế nào? | Giữ bản nhập dở để tiếp tục; chưa có khoản thu hoặc thay đổi số dư. | Mở biểu mẫu trống; chưa có khoản thu hoặc thay đổi số dư. | **Ninh đã xác nhận B → R71.** | [Nhật ký #08](../../docs/requirements/42-step-9-income-round-1.md). AC7 yêu cầu form trống, không sinh khoản thu hay đổi số dư từ bản nhập dở. |

## Ranh giới quyết định đã có

- **Ninh đã xác nhận:** vào từ một ví đang sử dụng thì ví ấy được chọn sẵn, được đổi sang ví đang sử dụng khác (F14). Từ nút chung khi có ≥2 ví đang sử dụng thì chưa chọn ví cho khoản thu và phải tự chọn trước Lưu (R70). Nếu chưa có ví thì tạo ví đầu tiên trước khi ghi (F02); nếu mọi ví đã ngừng thì dẫn tạo/bật lại rồi quay về ghi chép (F20, thuộc kiểm chứng tích hợp #18).
- **Ninh đã xác nhận:** F21 mở form trống sau khi đóng hẳn trình duyệt **trước Lưu**. Sau khi **đã bấm Lưu giao dịch** và kết quả chưa rõ, trong phiên áp dụng F09/R64; sau đóng/mở áp dụng F25. Q2 không thay đổi các nhánh này.
- **Ninh đã xác nhận:** rời form khoản thu trước Lưu rồi quay lại cùng phiên có Internet thì form trống, không tạo khoản thu hay đổi số dư (R71). Không suy R70–R71 cho khoản chi #09.
- **BA/Tech đề xuất hoặc chưa rõ ở mức thiết kế:** vị trí điều khiển, câu chữ, cách trình bày khi chỉ có một ví đang sử dụng ở nút chung. Các chi tiết này không thay đổi yêu cầu phải chọn ví hợp lệ trước Lưu. Q1–Q2 không còn chặn BA; PM đã xác nhận `BA_READY` trong [story](story.md) và [handoff](handoff.md).
