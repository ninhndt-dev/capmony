# Bước 4, vòng 3: ví ngừng sử dụng và tính nhất quán

**Trạng thái:** Đã trả lời ngày 13/10/2026. Câu hỏi đã được BA/PM rà soát trước khi hỏi; khi hỏi, các lựa chọn A là đề xuất của Bờm. Ninh đã chọn “A hết” cho Q1–Q7.

**Phụ thuộc đã chốt:** [vòng 1](20-step-4-core-business-round-1.md), [vòng 2](21-step-4-core-business-round-2.md) và [phiếu nghiệp vụ chính](core-business.md).

❓ **Q1 – Giao dịch cũ làm ví đã ngừng lệch 0:** Nếu sửa, xóa hoặc khôi phục giao dịch cũ có thể làm số dư ví đang ngừng sử dụng khác 0, có yêu cầu bật lại ví trước khi thực hiện không?

➡️ **A (Bờm đề xuất):** Có; giữ quy tắc ví đang ngừng luôn ở số dư 0. **B:** Không; cho ví vẫn ngừng dù số dư sau đó khác 0.

---

❓ **Q2 – Sửa ghi chú:** Ghi chú tùy chọn của khoản thu, chi hoặc chuyển tiền có sửa được sau khi đã lưu không?

➡️ **A (Bờm đề xuất):** Có, kể cả thêm hoặc xóa nội dung ghi chú. **B:** Ghi chú chỉ đặt lúc tạo giao dịch.

---

❓ **Q3 – Độ chính xác số dư ban đầu:** Số dư ban đầu của ví, kể cả khi âm, có chỉ dùng số nguyên VND không?

➡️ **A (Bờm đề xuất):** Có; cùng đơn vị với số tiền giao dịch đã chốt. **B:** Cho nhập phần lẻ của VND.

---

❓ **Q4 – Tên danh mục mặc định:** Bộ danh mục có sẵn cho mỗi người mới có gồm **Lương, Thưởng, Ăn uống, Đi lại, Mua sắm, Nhà ở, Hóa đơn, Sức khỏe, Giải trí, Khác** không? Đây là một danh sách dùng chung cho thu và chi theo R45.

➡️ **A (Bờm đề xuất):** Dùng đúng mười tên trên; người dùng vẫn có thể thêm, đổi tên hoặc xóa danh mục chưa dùng theo các quy tắc đã chốt. **B:** Dùng danh sách khác; Ninh ghi tên cần thêm/bớt.

---

❓ **Q5 – Số dư sau từng giao dịch trong lịch sử:** Khi xem lịch sử, mỗi dòng có cần hiện số dư của ví ngay sau giao dịch đó không?

➡️ **A (Bờm đề xuất):** Bản đầu chỉ cần số dư hiện tại của ví và số tiền từng giao dịch, chưa cần số dư sau từng dòng. **B:** Cần số dư sau từng dòng.

---

❓ **Q6 – Hai điện thoại sửa cùng một giao dịch:** Nếu giao dịch đã được sửa trên điện thoại thứ nhất trong lúc điện thoại thứ hai còn mở bản cũ, khi điện thoại thứ hai bấm lưu có cần báo dữ liệu đã thay đổi và yêu cầu tải lại không?

➡️ **A (Bờm đề xuất):** Có; không tự ghi đè thay đổi mới hơn. **B:** Lần bấm lưu sau ghi đè lần trước.

---

❓ **Q7 – Xóa ví tạo nhầm:** Ví đang có số dư 0 và chưa từng có giao dịch nào có thể xóa vĩnh viễn khỏi danh sách không? Giao dịch đã xóa mềm vẫn tính là từng có.

➡️ **A (Bờm đề xuất):** Có. **B:** Không; chỉ cho ngừng sử dụng ví.

## Nhật ký trả lời

Ninh trả lời **“A hết”** sau khi nhận đủ bảy câu hỏi, áp dụng A cho Q1–Q7.

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Nếu sửa, xóa hoặc khôi phục giao dịch cũ có thể làm số dư ví đã ngừng khác 0, phải bật lại ví trước thao tác; ví đang ngừng luôn giữ số dư 0. | Sửa số dư ban đầu của ví đã ngừng và sửa thông tin không đổi số dư sẽ hỏi riêng. |
| Q2 | Ghi chú của khoản thu, chi và chuyển tiền sửa được sau khi lưu, kể cả thêm hoặc xóa nội dung. | Không có phụ thuộc của câu này. |
| Q3 | Số dư ban đầu của ví dùng số nguyên VND, có thể âm. | Không có phụ thuộc của câu này. |
| Q4 | Mỗi người mới có mười danh mục mặc định chung cho thu/chi: Lương, Thưởng, Ăn uống, Đi lại, Mua sắm, Nhà ở, Hóa đơn, Sức khỏe, Giải trí, Khác. | Người dùng vẫn có quyền thêm/đổi tên và xóa danh mục chưa dùng theo R32, B05, B11. |
| Q5 | Bản đầu chưa cần hiện số dư của ví sau từng dòng lịch sử; cần số dư hiện tại và số tiền từng giao dịch. | Cách trình bày lịch sử cụ thể thuộc bước sau. |
| Q6 | Nếu hai điện thoại sửa cùng một giao dịch từ hai bản khác nhau, lần lưu sau phải báo dữ liệu đã đổi và yêu cầu tải lại; không tự ghi đè thay đổi mới hơn. | Các tình huống đồng thời khác còn mở. |
| Q7 | Ví có số dư 0 và chưa từng có giao dịch nào có thể xóa vĩnh viễn; giao dịch đã xóa mềm vẫn tính là từng có nên ví ấy không thuộc trường hợp này. | Không suy ra quyền xóa ví đã có lịch sử. |

Các lựa chọn đã được chuyển vào [phiếu nghiệp vụ chính](core-business.md).

## Phụ thuộc cho vòng sau

- Hỏi riêng ví nào được chọn khi đổi ví cho giao dịch cũ; không mâu thuẫn với quy tắc ví đang ngừng luôn có số dư 0.
- Hỏi tác động của sửa số dư ban đầu và sửa thông tin không đổi số dư đối với ví đã ngừng.
- Làm rõ đồng thời thêm giao dịch trên hai điện thoại và các ví dụ biên còn cần ở bước 4.
