# Bước 5, vòng 2: lỗi xác minh, đồng bộ và khôi phục truy cập

**Trạng thái:** Đã trả lời ngày 13/10/2026. BA, Tech và PM đã rà soát trước khi hỏi; Ninh chọn A cho Q1–Q6.

**Phụ thuộc đã chốt:** [Vòng 1 bước 5](26-step-5-user-flows-round-1.md) và [phiếu luồng người dùng](user-flows.md).

❓ **Q1 – Mã xác minh lời mời sai hoặc hết hạn:** Khi liên kết mời vẫn còn trong hạn 3 ngày nhưng mã xác minh sai hoặc hết hạn, người được mời có được xin mã mới tới cùng email không?

➡️ **A (Bờm đề xuất):** Có; mã cũ hết hiệu lực, hạn 3 ngày của lời mời không đổi. **B:** Không; nếu không còn mã hợp lệ, chờ lời mời hiện tại hết hạn rồi liên hệ Ninh để được mời lại.

---

❓ **Q2 – Đang nhập dở khi dữ liệu tự tải:** Nếu ứng dụng tự tải dữ liệu mới khi người dùng quay lại trên điện thoại thứ hai, trong lúc ở đó đang có giao dịch nhập dở chưa lưu, có giữ nguyên nội dung họ đang nhập không?

➡️ **A (Bờm đề xuất):** Có; dữ liệu mới được cập nhật nhưng bản nhập dở không bị mất. Khi bấm lưu, vẫn áp dụng quy tắc phát hiện bản cũ của cùng giao dịch. **B:** Bản nhập dở bị xóa và người dùng phải nhập lại.

---

❓ **Q3 – Mất mạng đúng lúc lưu:** Nếu người dùng bấm lưu giao dịch rồi mạng ngắt trước khi thấy kết quả, khi có mạng lại ứng dụng có cần đối chiếu giao dịch đã được lưu hay chưa trước khi thử gửi lại, để tránh tạo hai khoản không?

➡️ **A (Bờm đề xuất):** Có; khi mất mạng báo trạng thái chưa rõ, giữ nội dung đang nhập. Khi có mạng lại, tự đối chiếu kết quả trước khi thử gửi lại; không báo thành công khi chưa xác nhận và không tạo bản trùng. **B:** Báo kết quả chưa rõ; khi có mạng lại, người dùng tự kiểm tra lịch sử rồi quyết định nhập/lưu lại.

---

❓ **Q4 – Ninh xác minh người mất email cũ:** Khi người dùng đã đăng xuất, mất quyền dùng email cũ và nhờ Ninh đổi sang email mới, Ninh nên xác minh họ qua cách nào trước khi đổi email?

➡️ **A (Bờm đề xuất):** Liên hệ qua kênh độc lập với email cũ mà Ninh đã biết từ trước, xác minh đúng người rồi xác minh email mới. Nếu không có kênh đó hoặc không xác minh được thì chưa đổi email. **B:** Gặp trực tiếp người đó để xác minh, rồi xác minh email mới. Nếu không xác minh được thì chưa đổi email. Cả hai cách đều do Ninh thực hiện theo yêu cầu, không xem dữ liệu tài chính.

---

❓ **Q5 – Tài khoản quản lý đầu tiên:** Trước khi gửi lời mời đầu tiên, Ninh muốn lập tài khoản quản lý bằng luồng nào?

➡️ **A (Bờm đề xuất):** Trước khi mở hệ thống, Ninh cung cấp email quản lý cho người triển khai; hệ thống gửi liên kết thiết lập một lần chỉ tới email đó để Ninh xác minh email và tự đặt mật khẩu. **B:** Người triển khai tạo sẵn tài khoản với mật khẩu tạm, bàn giao riêng cho Ninh; lần đăng nhập đầu tiên Ninh phải đổi mật khẩu và xác minh email quản lý.

---

❓ **Q6 – Hạn liên kết đặt lại mật khẩu:** Liên kết đặt lại mật khẩu tài khoản ghi chép gửi qua email nên dùng được bao lâu kể từ lúc gửi?

➡️ **A (Bờm đề xuất):** 30 phút. **B:** 24 giờ.

## Nhật ký trả lời

Ninh trả lời sáu dòng “A”, áp dụng theo thứ tự Q1–Q6. Các lựa chọn này đã được chuyển vào [phiếu luồng người dùng](user-flows.md).

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Khi lời mời còn hạn, mã xác minh sai hoặc hết hạn có thể xin mã mới tới đúng email được mời; mã cũ hết hiệu lực, hạn mời 3 ngày không đổi. | Xử lý khi lời mời hết hạn trong lúc nhập mã sẽ hỏi tiếp. |
| Q2 | Khi tự tải dữ liệu mới lúc quay lại điện thoại thứ hai, giữ bản giao dịch nhập dở; khi lưu vẫn áp dụng quy tắc phát hiện bản cũ của cùng giao dịch. | Cách hiển thị dữ liệu mới và bản nhập dở chưa chốt. |
| Q3 | Nếu mất mạng trước khi biết kết quả lưu, báo trạng thái chưa rõ và giữ nội dung; khi có mạng lại, tự đối chiếu kết quả trước khi thử gửi lại, không báo thành công sớm và không tạo bản trùng. | Nhấn lưu lặp và rời trang trong trạng thái chưa rõ sẽ hỏi tiếp. |
| Q4 | Khi người dùng đã đăng xuất và mất email cũ, Ninh xác minh đúng người qua kênh độc lập với email cũ đã biết từ trước, rồi xác minh email mới; nếu thiếu kênh hoặc không xác minh được thì chưa đổi email. Ninh chỉ làm theo yêu cầu, không xem dữ liệu tài chính. | Bằng chứng xác minh cụ thể của kênh độc lập chưa chốt. |
| Q5 | Trước khi mở hệ thống, Ninh cung cấp email quản lý cho người triển khai; liên kết thiết lập một lần chỉ gửi tới email đó, Ninh xác minh email và tự đặt mật khẩu. | Xử lý liên kết thiết lập hết hạn/chưa hoàn tất sẽ hỏi tiếp. |
| Q6 | Liên kết đặt lại mật khẩu tài khoản ghi chép qua email có hạn 30 phút kể từ lúc gửi. | Cách báo hết hạn và gửi lại liên kết chưa chốt. |

## Phụ thuộc cho vòng sau

- Sau Q1, xác định cách báo khi mã mới vẫn sai hoặc lời mời hết hạn trong lúc nhập mã.
- Sau Q3, xác định cách xử lý nhấn lưu lặp và thao tác rời trang khi trạng thái lưu chưa rõ.
- Sau Q4, làm rõ khi Ninh không thể liên hệ hoặc gặp đúng người yêu cầu.
- Sau Q5, làm rõ tính một lần của luồng thiết lập và quyền truy cập nếu chưa hoàn tất.
