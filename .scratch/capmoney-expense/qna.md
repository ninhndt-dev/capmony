# Q&A: Story 09 – Ghi khoản chi

Ninh đã chọn **Q1=A, Q2=A** trên giao diện ngày 14/10/2026; ghi thành [R72–R73](../../docs/requirements/00-intake.md) trong [nhật ký vòng #09](../../docs/requirements/43-step-9-expense-round-1.md). Hai quyết định này chỉ xét **khoản chi #09**. [R70–R71](../../docs/requirements/42-step-9-income-round-1.md) vẫn là nguồn riêng cho khoản thu #08.

| Mã | Câu hỏi cần Ninh chọn | A (BA đề xuất) | B | Trạng thái | Nguồn trả lời và tác động |
| --- | --- | --- | --- | --- | --- |
| Q1 | Mở ghi **khoản chi** từ nút ghi chung khi có từ hai ví đang sử dụng: ví trên biểu mẫu ban đầu được chọn thế nào? | Chưa chọn ví; người dùng tự chọn một ví đang sử dụng trước Lưu, cùng cách Ninh đã chọn cho khoản thu ở R70. | Chọn sẵn ví đang sử dụng **được tạo sớm nhất**; người dùng vẫn đổi được trước Lưu. | **Ninh đã xác nhận A → R72.** | [Nhật ký #09](../../docs/requirements/43-step-9-expense-round-1.md). AC2 yêu cầu chưa chọn ví từ nút chung khi có ≥2 ví; phải chọn trước Lưu. B không được chọn. |
| Q2 | Đang nhập **khoản chi**, chưa bấm Lưu, rời biểu mẫu sang trang khác rồi quay lại trong **cùng phiên có Internet**: bản nhập dở thế nào? | Mở biểu mẫu trống; chưa tạo khoản chi hay đổi số dư, cùng cách Ninh đã chọn cho khoản thu ở R71. | Giữ bản nhập dở để tiếp tục; chưa tạo khoản chi hay đổi số dư. | **Ninh đã xác nhận A → R73.** | [Nhật ký #09](../../docs/requirements/43-step-9-expense-round-1.md). AC7 yêu cầu form trống, không sinh khoản chi hay đổi số dư từ bản nhập dở. |

## Ranh giới đã chốt và đề xuất

- **Ninh đã xác nhận:** khoản chi có thể làm số dư âm (R39, B01), nhưng **số tiền nhập** vẫn phải là nguyên VND dương (B02). Ví đã ngừng không nhận giao dịch mới (B04); danh mục thu/chi dùng chung (R45, B16); vào từ ví đang dùng thì chọn sẵn ví đó, đổi được (F14).
- **Ninh đã xác nhận:** từ nút chung có ≥2 ví đang dùng, form chi ban đầu chưa chọn ví, phải tự chọn trước Lưu (R72). Rời form chi trước Lưu rồi quay lại cùng phiên có Internet thì form trống, chưa tạo khoản chi hoặc đổi số dư (R73).
- **Ninh đã xác nhận:** đóng trình duyệt **trước Lưu** mở form trống (F21). Sau khi **đã bấm Lưu giao dịch** và kết quả chưa rõ, F09/F25/R64 áp dụng; Q2 không hỏi lại các nhánh đó.
- **Không suy rộng:** R72–R73 chỉ xác nhận khoản chi; không áp dụng mặc nhiên cho chuyển tiền hoặc tạo ví. Cách trình bày khi nút chung chỉ có một ví là chi tiết UX, nhưng trước Lưu vẫn phải có ví hợp lệ.
- **BA/Tech đề xuất hoặc chưa rõ ở mức thiết kế:** bố cục, câu chữ, hợp đồng lưu và ca kiểm chứng lỗi mạng; Q1–Q2 không còn chặn BA. PM đã xác nhận `BA_READY` trong [story](story.md) và [handoff](handoff.md).
