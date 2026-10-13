# Bước 4, vòng 1: quy tắc nghiệp vụ chính

**Trạng thái:** Đã trả lời ngày 12/10/2026. Khi hỏi, các lựa chọn A là đề xuất của Bờm; Ninh đã chọn “A hết” cho Q1–Q5.

**Nguồn đã chốt:** [Phiếu tiếp nhận](00-intake.md), [mục tiêu và phạm vi](goal-and-scope.md), [người dùng và quyền](actors-and-roles.md).

❓ **Q1 – Tính số dư ví:** Số dư hiện tại của ví có bằng số dư ban đầu, cộng các khoản thu và tiền chuyển đến, trừ các khoản chi và tiền chuyển đi còn hiệu lực, bất kể chúng được ghi cho ngày nào không?

➡️ **A (Bờm đề xuất):** Có; khi thêm, sửa, xóa hoặc khôi phục giao dịch cũ thì số dư hiện tại được tính lại. **B:** Cần quy tắc khác; Ninh ghi rõ.

---

❓ **Q2 – Số tiền được nhập:** Mỗi khoản thu, chi và chuyển tiền có bắt buộc là số nguyên VND lớn hơn 0 không?

➡️ **A (Bờm đề xuất):** Có; không nhập 0, số âm hoặc phần lẻ. Ví vẫn có thể có *số dư* âm theo R39. **B:** Cho phép số tiền 0 hoặc có phần lẻ; Ninh ghi trường hợp cần.

---

❓ **Q3 – Đổi loại thu/chi khi sửa:** Khi sửa một giao dịch thu/chi đã lưu, người dùng có được đổi từ thu sang chi hoặc ngược lại không?

➡️ **A (Bờm đề xuất):** Có. **B:** Không; khi sửa vẫn giữ loại thu hoặc chi ban đầu.

---

❓ **Q4 – Ví đã ngừng sử dụng:** Sau khi ví về 0 và người dùng ngừng sử dụng nó, ví có ngừng xuất hiện trong lựa chọn cho giao dịch hoặc chuyển tiền mới, còn lịch sử cũ vẫn xem được không?

➡️ **A (Bờm đề xuất):** Có. **B:** Ví vẫn chọn được cho giao dịch mới; Ninh ghi mục đích ngừng sử dụng.

---

❓ **Q5 – Xóa danh mục đã dùng:** Với danh mục đã gắn vào giao dịch, bản đầu có giữ danh mục đó và chỉ cho đổi tên, không cho xóa không?

➡️ **A (Bờm đề xuất):** Có; danh mục vẫn chọn được cho giao dịch mới theo R49. **B:** Cho xóa danh mục đã dùng; vòng sau sẽ hỏi giao dịch cũ hiển thị thế nào.

## Nhật ký trả lời

Ninh trả lời **“A hết”** sau khi nhận đủ năm câu hỏi, áp dụng A cho Q1–Q5.

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Số dư hiện tại = số dư ban đầu + thu còn hiệu lực + chuyển đến còn hiệu lực − chi còn hiệu lực − chuyển đi còn hiệu lực, không phụ thuộc ngày ghi. Thêm, sửa, xóa hoặc khôi phục giao dịch cũ làm tính lại số dư hiện tại. | Cách hiển thị số dư theo từng ngày trong lịch sử chưa rõ. |
| Q2 | Khoản thu, chi và số tiền chuyển giữa ví phải là số nguyên VND lớn hơn 0; không nhập 0, số âm hoặc phần lẻ. | Số dư ví có thể âm theo R39; giá trị số dư ban đầu sẽ hỏi riêng nếu cần. |
| Q3 | Khi sửa một khoản thu/chi đã lưu, người dùng có thể đổi từ thu sang chi hoặc ngược lại. | Các trường khác được sửa sẽ hỏi ở vòng sau. |
| Q4 | Ví đã về 0 và ngừng sử dụng không được chọn cho giao dịch hoặc chuyển tiền mới; lịch sử cũ vẫn xem được. | Có thể dùng lại ví hay sửa giao dịch cũ của ví đó chưa rõ. |
| Q5 | Danh mục đã gắn vào giao dịch được giữ lại, chỉ cho đổi tên chứ không xóa; vẫn chọn được cho giao dịch mới theo R49. | Xử lý danh mục chưa từng dùng sẽ hỏi riêng. |

Các lựa chọn đã được chuyển vào [phiếu nghiệp vụ chính](core-business.md). BA và PM đã rà soát câu hỏi trước khi Ninh trả lời; Q3 được chỉnh để chỉ hỏi quyền đổi loại thu/chi.

## Phụ thuộc cho vòng sau

- Hỏi về việc dùng lại ví đã ngừng; sau câu trả lời đó, hỏi cách sửa giao dịch cũ liên quan đến ví đó.
- Hỏi các trường được sửa của khoản thu/chi, quy tắc sửa một lần chuyển tiền, danh mục chưa từng dùng, giao dịch đồng thời trên nhiều điện thoại và ví dụ biên.
