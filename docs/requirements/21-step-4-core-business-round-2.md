# Bước 4, vòng 2: sửa dữ liệu và trường hợp biên

**Trạng thái:** Đã trả lời ngày 13/10/2026. Câu hỏi đã được BA/PM rà soát trước khi hỏi; khi hỏi, các lựa chọn A là đề xuất của Bờm. Ninh đã chọn “A hết” cho Q1–Q7.

**Phụ thuộc đã chốt:** [vòng 1 bước 4](20-step-4-core-business-round-1.md) và [phiếu nghiệp vụ chính](core-business.md).

❓ **Q1 – Dùng lại ví đã ngừng:** Người dùng có được bật dùng lại một ví đã ngừng để ghi giao dịch mới không?

➡️ **A (Bờm đề xuất):** Có; ví tiếp tục giữ toàn bộ lịch sử cũ. **B:** Không; phải tạo ví khác.

---

❓ **Q2 – Các trường được sửa của khoản thu/chi:** Ngoài quyền đổi thu thành chi hoặc ngược lại đã chốt, người dùng có được sửa ngày, số tiền, ví và danh mục của một khoản đã lưu không?

➡️ **A (Bờm đề xuất):** Sửa được cả bốn trường; ngày vẫn chỉ là hôm nay hoặc ngày đã qua theo R43, số dư các ví liên quan được tính lại theo B01. **B:** Chỉ sửa một số trường; Ninh ghi trường muốn giữ cố định.

---

❓ **Q3 – Sửa lần chuyển tiền:** Người dùng có được sửa ngày, số tiền, ví gửi hoặc ví nhận của một lần chuyển đã lưu không?

➡️ **A (Bờm đề xuất):** Có; ngày vẫn chỉ là hôm nay hoặc ngày đã qua theo R43, sửa cả lần chuyển như một thao tác liên kết, các ví liên quan được tính lại theo B01. **B:** Chỉ sửa một số trường hoặc không cho sửa; Ninh ghi rõ.

---

❓ **Q4 – Chuyển tiền về chính ví gửi:** Có cần chặn chuyển tiền khi ví gửi và ví nhận là cùng một ví không?

➡️ **A (Bờm đề xuất):** Có, phải chọn hai ví khác nhau. **B:** Cho phép chọn cùng một ví.

---

❓ **Q5 – Số dư ban đầu âm:** Khi tạo ví hoặc sửa số dư ban đầu, có cho nhập giá trị âm không?

➡️ **A (Bờm đề xuất):** Có; người dùng có thể bắt đầu với khoản đang nợ. **B:** Chỉ cho số dư ban đầu từ 0 trở lên, dù giao dịch về sau có thể làm ví âm.

---

❓ **Q6 – Xóa danh mục chưa dùng:** Danh mục chưa từng gắn vào giao dịch, kể cả danh mục mặc định, có thể xóa hẳn khỏi danh sách riêng của người dùng không?

➡️ **A (Bờm đề xuất):** Có. **B:** Không; mọi danh mục chỉ đổi tên được.

---

❓ **Q7 – Ghi chú giao dịch:** Mỗi khoản thu, chi hoặc chuyển tiền có cần chỗ nhập ghi chú tùy chọn không?

➡️ **A (Bờm đề xuất):** Có; có thể để trống. **B:** Bản đầu chưa cần ghi chú.

## Nhật ký trả lời

Ninh trả lời **“A hết”** sau khi nhận đủ bảy câu hỏi, áp dụng A cho Q1–Q7.

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Người dùng được bật dùng lại ví đã ngừng để ghi giao dịch mới; lịch sử cũ vẫn giữ. | Tác động của việc sửa/xóa/khôi phục giao dịch cũ khi ví vẫn ngừng sẽ hỏi tiếp. |
| Q2 | Khoản thu/chi đã lưu sửa được ngày, số tiền, ví và danh mục; ngày vẫn chỉ là hôm nay hoặc quá khứ theo R43; số dư ví liên quan tính lại theo B01. | Chưa chốt quyền chọn ví đã ngừng khi đổi ví. |
| Q3 | Lần chuyển tiền đã lưu sửa được ngày, số tiền, ví gửi và ví nhận như một thao tác liên kết; ngày vẫn theo R43; các ví liên quan tính lại theo B01. | Chưa chốt quyền chọn ví đã ngừng khi đổi ví gửi/nhận. |
| Q4 | Không được chuyển tiền từ một ví về chính ví đó; ví gửi và ví nhận phải khác nhau. | Không có phụ thuộc của câu này. |
| Q5 | Số dư ban đầu được nhập giá trị âm khi tạo ví hoặc sửa sau này. | Độ chính xác số dư ban đầu sẽ hỏi riêng. |
| Q6 | Danh mục chưa từng gắn vào giao dịch có thể xóa hẳn khỏi danh sách riêng của người dùng, kể cả danh mục mặc định. | Danh mục đã từng dùng giữ theo B05. |
| Q7 | Khoản thu, chi và chuyển tiền có ghi chú tùy chọn; có thể để trống. | Quyền sửa ghi chú sau khi lưu chưa chốt. |

Các lựa chọn đã được chuyển vào [phiếu nghiệp vụ chính](core-business.md).

## Phụ thuộc cho vòng sau

- Sau Q1, làm rõ sửa, xóa hoặc khôi phục giao dịch cũ khiến ví đã ngừng không còn số dư 0.
- Sau Q2–Q3, làm rõ trường hợp chuyển giao dịch giữa ví và tác động lên ví đã ngừng.
- Nếu Q2–Q3=A, quyền đổi ví không tự bao gồm quyền chọn ví đã ngừng; sẽ hỏi riêng theo nhánh Q1.
- Nếu Q7=A, làm rõ quyền sửa ghi chú khi sửa giao dịch; chưa ngầm coi là đã chốt.
- Hỏi tên danh mục mặc định, hiển thị số dư theo ngày và thao tác đồng thời trên nhiều điện thoại khi đến vòng phù hợp.
