# Q&A: CM-26 – Hỗ trợ người mất email

## Ninh đã xác nhận

- R62: Ninh chỉ hỗ trợ người đã đăng xuất và mất email cũ khi đã quen/nhận diện chắc; gọi lại số đã lưu từ trước **ngoài CapMoney**, xác nhận yêu cầu. Không có số cũ hoặc không chắc đúng người thì chưa đổi; email mới vẫn phải xác minh.
- R79: Ninh xác nhận trong ứng dụng đã hoàn tất bước gọi lại/nhận diện; CapMoney lưu người xác nhận, thời điểm và tài khoản được hỗ trợ để truy vết lần đổi email. Không yêu cầu số điện thoại, ghi âm hay ghi chú nhận diện. [Nguồn](../../docs/requirements/48-step-9-lost-email-evidence.md)
- F10 và quyền bước 3: chỉ tra đúng email cũ theo yêu cầu, không xem danh sách hoặc dữ liệu tài chính; đổi xong phiên cũ kết thúc.

| Mã | Câu hỏi cần Ninh chọn | A (đề xuất) | B | Trạng thái | Tác động |
| --- | --- | --- | --- | --- | --- |
| Q26.1 | Trong CapMoney nên ghi bằng chứng Ninh đã gọi lại số có trước, nhận diện đúng người và xác nhận yêu cầu như thế nào? | Ninh xác nhận điều kiện hỗ trợ; lưu người xác nhận, thời điểm và tài khoản được hỗ trợ. | Ninh nhập thêm ghi chú nhận diện; phương án không lưu bằng chứng cũng được nêu trên giao diện hỏi. | **Đã chốt R79: chọn A ngày 16/10/2026** | BR-26-04/AC5 yêu cầu bản ghi khi Ninh xác nhận trong ứng dụng. Xác nhận trong ứng dụng là lời xác nhận của Ninh, không tự chứng minh cuộc gọi ngoài ứng dụng. |

Phương thức xác minh email mới, TTL và copy UI là việc Tech/PM đề xuất; nếu thay đổi quyền hoặc quy tắc R62 thì hỏi Ninh riêng.

CapmmPM đã rà bản sau R79, không còn câu hỏi sản phẩm chặn AC hiện tại; [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md) cấp `BA_READY`. `TECH_READY` còn chờ thiết kế và review độc lập.
