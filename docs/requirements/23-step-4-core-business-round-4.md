# Bước 4, vòng 4: giao dịch với ví ngừng và thao tác đồng thời

**Trạng thái:** Đã trả lời ngày 13/10/2026. Khi hỏi, các lựa chọn A là đề xuất của Bờm; Ninh đã chọn “Ahết” cho Q1–Q5.

**Phụ thuộc đã chốt:** [vòng 1](20-step-4-core-business-round-1.md), [vòng 2](21-step-4-core-business-round-2.md), [vòng 3](22-step-4-core-business-round-3.md) và [phiếu nghiệp vụ chính](core-business.md).

❓ **Q1 – Đổi giao dịch sang ví khác:** Khi sửa khoản thu/chi hoặc lần chuyển tiền để đổi ví, có chỉ cho chọn ví đang sử dụng không?

➡️ **A (Bờm đề xuất):** Có; muốn chọn ví đã ngừng thì bật dùng lại ví trước. **B:** Cho chọn ví đang ngừng nếu thao tác vẫn giữ số dư ví đó bằng 0.

---

❓ **Q2 – Sửa thông tin cũ mà không đổi số dư:** Với giao dịch còn hiệu lực gắn ví đã ngừng, người dùng có được sửa ngày, ghi chú hoặc danh mục của khoản thu/chi nếu số dư hiện tại của ví vẫn bằng 0 không?

➡️ **A (Bờm đề xuất):** Có; không cần bật lại ví cho thay đổi không ảnh hưởng số dư. **B:** Phải bật lại ví trước mọi lần sửa giao dịch cũ.

---

❓ **Q3 – Sửa giao dịch đã xóa:** Khoản thu, chi hoặc chuyển tiền đã xóa mềm có cần khôi phục trước khi sửa không?

➡️ **A (Bờm đề xuất):** Có; không sửa trực tiếp trong mục đã xóa. **B:** Cho sửa ngay cả khi giao dịch vẫn ở mục đã xóa.

---

❓ **Q4 – Chuyển vượt số dư ví gửi:** Khi số tiền chuyển lớn hơn số dư hiện tại của ví gửi, có cho lưu để ví gửi âm không?

➡️ **A (Bờm đề xuất):** Có; áp dụng quyền số dư âm đã chốt. **B:** Không cho chuyển vượt số dư, dù ví có thể âm vì khoản chi.

---

❓ **Q5 – Thêm hai khoản khác nhau cùng lúc:** Nếu cùng một tài khoản lưu hai giao dịch khác nhau trên hai điện thoại gần như đồng thời, có cần giữ cả hai và tính đúng số dư sau cả hai không?

➡️ **A (Bờm đề xuất):** Có; không mất một khoản đã báo lưu. **B:** Một lần lưu bị từ chối và người dùng phải tải lại, nhập lại khoản đó.

## Nhật ký trả lời

Ninh trả lời **“Ahết”** sau khi nhận đủ năm câu hỏi, được hiểu là **“A hết”** và áp dụng A cho Q1–Q5.

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Khi đổi ví của khoản thu/chi hoặc lần chuyển đã lưu, chỉ chọn ví đang sử dụng; muốn chọn ví đã ngừng phải bật dùng lại trước. | Không suy ra quy tắc hiển thị ví trong mọi màn hình từ quy tắc chọn khi sửa. |
| Q2 | Giao dịch còn hiệu lực gắn ví đã ngừng được sửa ngày, ghi chú hoặc danh mục của khoản thu/chi nếu số dư ví vẫn bằng 0, không cần bật lại ví. | Thao tác làm số dư khác 0 vẫn tuân B13. |
| Q3 | Khoản thu, chi hoặc chuyển tiền đã xóa mềm phải khôi phục trước khi sửa; không sửa trực tiếp trong mục đã xóa. | Khôi phục vẫn tuân B13 nếu ảnh hưởng ví đã ngừng. |
| Q4 | Cho chuyển tiền lớn hơn số dư ví gửi; ví gửi có thể âm sau chuyển. | Ví gửi/nhận vẫn phải khác nhau theo B09. |
| Q5 | Nếu hai điện thoại lưu hai giao dịch khác nhau gần như đồng thời, giữ cả hai và tính đúng số dư sau cả hai; không mất khoản đã báo lưu. | Hai thao tác sửa/xóa khác nhau cùng lúc sẽ hỏi riêng nếu cần. |

Các lựa chọn đã được chuyển vào [phiếu nghiệp vụ chính](core-business.md).

## Phụ thuộc cho vòng sau

- Quy tắc B13 nói ví đang ngừng luôn có số dư 0; từ đó suy ra khi sửa số dư ban đầu khiến số dư khác 0 thì phải bật dùng lại ví trước. Sẽ đưa suy luận này vào bản tóm tắt để Ninh xác nhận trước khi kết thúc bước 4.
- Khôi phục giao dịch vẫn phải tuân B13 nếu nó có thể làm ví đang ngừng khác 0.
- Các tình huống về màn hình báo lỗi, nhấn lưu lặp và kết nối mạng thuộc bước 5 về luồng người dùng.
