# Q&A: CM-26 – Hỗ trợ người mất email

## Ninh đã xác nhận

- R62: Ninh chỉ hỗ trợ người đã đăng xuất và mất email cũ khi đã quen/nhận diện chắc; gọi lại số đã lưu từ trước **ngoài CapMoney**, xác nhận yêu cầu. Không có số cũ hoặc không chắc đúng người thì chưa đổi; email mới vẫn phải xác minh.
- F10 và quyền bước 3: chỉ tra đúng email cũ theo yêu cầu, không xem danh sách hoặc dữ liệu tài chính; đổi xong phiên cũ kết thúc.

| Mã | Câu hỏi cần Ninh chọn | A (đề xuất) | B | Trạng thái | Tác động |
| --- | --- | --- | --- | --- | --- |
| Q26.1 | Trong CapMoney nên ghi bằng chứng Ninh đã gọi lại số có trước, nhận diện đúng người và xác nhận yêu cầu như thế nào? | Ninh xác nhận ba điều kiện trên màn hỗ trợ; lưu người xác nhận và thời điểm, không lưu số/ghi âm. | Ninh nhập ghi chú ngắn về căn cứ nhận diện cùng xác nhận; lưu ghi chú hạn chế quyền xem. | **Chưa hỏi/chưa chốt; câu hỏi sản phẩm chặn `BA_READY`** | Quyết định dữ liệu bằng chứng, quyền xem và AC của thao tác hỗ trợ. Không được coi một ô tick/ghi chú là bằng chứng thật khi Ninh chưa chọn. |

Phương thức xác minh email mới, TTL và copy UI là việc Tech/PM đề xuất; nếu thay đổi quyền hoặc quy tắc R62 thì hỏi Ninh riêng.
