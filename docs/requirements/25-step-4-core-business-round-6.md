# Bước 4, vòng 6: tên ví và quy tắc phát hiện thay đổi

**Trạng thái:** Đã trả lời ngày 13/10/2026. Câu hỏi đã được BA/PM rà soát trước khi hỏi; khi hỏi, các lựa chọn A là đề xuất hoặc suy luận của Bờm. Ninh trả lời “A hêa”, được hiểu là “A hết” cho Q1–Q3.

**Phụ thuộc đã chốt:** [Phiếu nghiệp vụ chính](core-business.md) sau năm vòng hỏi bước 4.

❓ **Q1 – Tên ví trong lịch sử sau khi đổi:** Khi người dùng đổi tên ví, các giao dịch cũ có hiển thị tên ví mới không?

➡️ **A (Bờm đề xuất):** Có; giao dịch vẫn thuộc cùng ví, chỉ tên hiển thị thay đổi. **B:** Giao dịch cũ tiếp tục hiển thị tên ví tại thời điểm ghi.

---

❓ **Q2 – Bản cũ của cùng một giao dịch:** Quy tắc báo thay đổi và yêu cầu tải lại có áp dụng cho mọi thao tác sửa, xóa hoặc khôi phục cùng một giao dịch từ hai điện thoại khi một điện thoại đang dùng bản cũ không?

➡️ **A (Bờm đề xuất):** Có; không thao tác trên bản cũ của cùng giao dịch, bất kể thao tác nào. **B:** Chỉ áp dụng cho hai trường hợp đã chốt là sửa/sửa và sửa/xóa; Ninh ghi quy tắc cho các cặp còn lại.

---

❓ **Q3 – Số dư ban đầu của ví đã ngừng:** Bờm suy ra từ B13 rằng khi sửa số dư ban đầu có thể làm ví đang ngừng khác 0, người dùng phải bật dùng lại ví trước khi sửa. Ninh xác nhận cách hiểu này không?

➡️ **A (Bờm đề xuất):** Đúng; ví đang ngừng luôn có số dư 0. **B:** Chưa đúng; Ninh ghi quy tắc muốn áp dụng.

## Nhật ký trả lời

Ninh trả lời **“A hêa”** sau khi nhận đủ ba câu hỏi, được hiểu là **“A hết”** và áp dụng A cho Q1–Q3.

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Khi đổi tên ví, giao dịch cũ hiển thị tên ví mới; giao dịch vẫn thuộc cùng ví. | Không có phụ thuộc của câu này. |
| Q2 | Nếu hai điện thoại dùng bản khác nhau của cùng một giao dịch, mọi thao tác sửa, xóa hoặc khôi phục từ bản cũ đều báo thay đổi và yêu cầu tải lại; không ghi đè. | Cách thông báo cụ thể thuộc bước 5 về luồng người dùng. |
| Q3 | Nếu sửa số dư ban đầu có thể khiến ví đang ngừng khác 0, người dùng phải bật dùng lại ví trước khi sửa; ví đang ngừng luôn có số dư 0. | Đây là suy luận từ B13 đã được Ninh xác nhận rõ, không chỉ còn là đề xuất. |

Các lựa chọn đã được chuyển vào [phiếu nghiệp vụ chính](core-business.md).

## Sau vòng này

Không mở nhánh phụ thuộc mới. BA và PM sẽ rà soát toàn bộ đầu ra bước 4; Bờm đưa bản tóm tắt để Ninh xác nhận trước khi chuyển bước 5.
