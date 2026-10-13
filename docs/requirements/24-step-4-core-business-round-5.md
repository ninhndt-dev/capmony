# Bước 4, vòng 5: phân loại chuyển tiền và thay đổi đồng thời

**Trạng thái:** Đã trả lời ngày 13/10/2026. Câu hỏi đã được BA/PM rà soát trước khi hỏi; khi hỏi, các lựa chọn A là đề xuất của Bờm. Ninh đã chọn “A hét”, được hiểu là “A hết”, cho Q1–Q4.

**Phụ thuộc đã chốt:** [Phiếu nghiệp vụ chính](core-business.md) sau bốn vòng hỏi bước 4.

❓ **Q1 – Danh mục của chuyển tiền:** Một lần chuyển giữa hai ví của cùng người có cần gắn danh mục như khoản thu/chi không?

➡️ **A (Bờm đề xuất):** Không; chuyển tiền chỉ chuyển số dư giữa hai ví, không phải khoản thu hoặc chi có danh mục. **B:** Có; người dùng phải chọn danh mục cho lần chuyển.

---

❓ **Q2 – Đổi tên ví:** Người dùng có được đổi tên ví sau khi ví đã có giao dịch hoặc đã ngừng sử dụng không?

➡️ **A (Bờm đề xuất):** Có; lịch sử của ví vẫn giữ nguyên. Cách hiển thị tên ví trên lịch sử sẽ hỏi sau. **B:** Chỉ đổi tên ví khi ví chưa từng có giao dịch và đang sử dụng.

---

❓ **Q3 – Sửa hai giao dịch khác nhau cùng lúc:** Nếu hai điện thoại sửa hai giao dịch khác nhau của cùng một tài khoản gần như đồng thời, có giữ cả hai thay đổi đã báo lưu và tính lại số dư đúng không?

➡️ **A (Bờm đề xuất):** Có; quy tắc báo xung đột B18 chỉ áp dụng khi cả hai cùng sửa một giao dịch. **B:** Một lần sửa bị từ chối và người dùng phải tải lại.

---

❓ **Q4 – Một điện thoại sửa, điện thoại kia xóa:** Nếu một điện thoại đã sửa một giao dịch còn điện thoại kia vẫn mở bản cũ của chính giao dịch đó rồi bấm xóa, có cần báo giao dịch đã thay đổi và yêu cầu tải lại trước khi xóa không?

➡️ **A (Bờm đề xuất):** Có; không xóa dựa trên bản cũ. **B:** Cho thao tác xóa sau ghi đè tình trạng trước đó.

## Nhật ký trả lời

Ninh trả lời **“A hét”** sau khi nhận đủ bốn câu hỏi, được hiểu là **“A hết”** và áp dụng A cho Q1–Q4.

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Lần chuyển tiền giữa hai ví của cùng người không cần danh mục; danh mục gắn với khoản thu/chi. | Không suy ra màn hình chuyển tiền không có ghi chú; B12 đã chốt ghi chú tùy chọn. |
| Q2 | Người dùng được đổi tên ví sau khi ví đã có giao dịch hoặc đã ngừng; lịch sử ví vẫn giữ. | Tên ví hiển thị trên giao dịch cũ sau khi đổi tên sẽ hỏi tiếp. |
| Q3 | Hai điện thoại sửa hai giao dịch khác nhau gần như đồng thời thì giữ cả hai thay đổi đã báo lưu và tính đúng số dư. | Quy tắc B18 vẫn áp dụng khi cùng một giao dịch bị sửa từ hai bản khác nhau. |
| Q4 | Nếu một điện thoại đã sửa giao dịch còn điện thoại kia định xóa chính giao dịch đó từ bản cũ, báo dữ liệu thay đổi và yêu cầu tải lại trước khi xóa. | Các cặp thao tác khác trên cùng giao dịch sẽ hỏi tiếp. |

Các lựa chọn đã được chuyển vào [phiếu nghiệp vụ chính](core-business.md).

## Phụ thuộc cho vòng sau

- Hỏi lịch sử giao dịch có hiển thị tên ví mới sau khi đổi tên không.
- Hỏi quy tắc phát hiện bản cũ có áp dụng cho mọi cặp thao tác sửa, xóa, khôi phục trên cùng một giao dịch không.
- Tình huống mạng mất khi lưu, bấm lưu lặp và thông báo lỗi thuộc bước 5 về luồng người dùng.
