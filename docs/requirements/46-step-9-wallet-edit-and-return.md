# Bước 9, vòng làm rõ #12: đổi tên ví và quay về ghi chép

**Trạng thái:** Ninh trả lời hai câu hỏi trên giao diện ngày 16/10/2026; ghi thành R76–R77 cho CM-16 và CM-18. Các Story còn chờ PM review gate.

| Mã | Câu hỏi | Ninh chọn | Phạm vi |
| --- | --- | --- | --- |
| R76 | Khi đổi tên ví đã tạo, có cho Lưu tên trống không? | **Không cho Lưu tên trống**, như lúc tạo ví. | Chỉ đổi tên ví trong CM-16; không tự đặt quy tắc tên duy nhất hay độ dài mới. |
| R77 | Khi mọi ví đang ngừng, sau khi tạo hoặc bật lại một ví để quay về ghi thu/chi, biểu mẫu chọn ví nào? | **Chọn sẵn ví vừa tạo hoặc bật lại.** | Luồng quay về của CM-18/F20; phần nhập thu/chi chưa Lưu trước khi rời form vẫn bị bỏ theo R71/R73. |

**Ví dụ R76:** đổi tên “Tiền mặt” thành rỗng rồi bấm Lưu không đổi tên ví hay lịch sử; nhập “Tiền dự phòng” thì có thể Lưu nếu trường khác hợp lệ.

**Ví dụ R77:** mọi ví đang ngừng, người dùng mở ghi chi, bật lại ví “Ngân hàng” rồi quay về ghi chi; biểu mẫu mới chọn sẵn “Ngân hàng”, nhưng số tiền/ghi chú đã nhập ở biểu mẫu cũ trước khi rời trang không được khôi phục.
