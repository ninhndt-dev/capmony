# Bước 6, vòng 1: ưu tiên chức năng và quyền đặt lại mật khẩu quản lý

**Trạng thái:** Đã trả lời ngày 13/10/2026. PM đã rà soát trước khi hỏi; Ninh chọn A, A, B cho Q1–Q3.

**Nguồn đã chốt:** [Danh mục chức năng bản đầu](system-functions.md), [mục tiêu và phạm vi](goal-and-scope.md), [luồng người dùng](user-flows.md).

Tất cả nhóm chức năng H01–H12 đã nằm trong bản đầu theo các quyết định trước. Các câu về **ưu tiên** chỉ hỏi thứ tự chuẩn bị và kiểm chứng; dù chọn A hay B, toàn bộ chức năng đã chốt vẫn phải hoạt động đúng trước khi dùng thật.

❓ **Q1 – Quên mật khẩu tài khoản quản lý:** R29 từng nói chung người dùng tự đặt lại mật khẩu qua email, nhưng bước 5 mới chốt chi tiết cho tài khoản ghi chép. Nếu Ninh vẫn dùng được email quản lý nhưng quên mật khẩu tài khoản quản lý, bản đầu xử lý thế nào?

➡️ **A (Bờm đề xuất):** R29 cũng áp dụng cho tài khoản quản lý: gửi liên kết tới email quản lý đã xác minh, Ninh tự đặt mật khẩu mới và các phiên cũ của tài khoản quản lý kết thúc. **B:** R29 chỉ áp dụng cho tài khoản ghi chép; bản đầu chưa có luồng tự đặt lại mật khẩu quản lý, nên nếu quên mật khẩu Ninh chưa thể tự lấy lại quyền quản lý trong ứng dụng.

---

❓ **Q2 – Luồng tiền nào cần có bản demo sớm hơn:** Sau khi có tài khoản, ví và điều kiện nền để ghi chép, Ninh muốn nhóm nào được xây dựng và xem thử trước?

➡️ **A (Bờm đề xuất):** Ghi thu/chi, số dư và lịch sử; sau đó chuyển tiền giữa ví. **B:** Chuyển tiền giữa ví; sau đó ghi thu/chi, số dư và lịch sử.

---

❓ **Q3 – Kiểm tra nhiều điện thoại:** Khi bắt đầu có chức năng lưu giao dịch, Ninh muốn kiểm tra đồng bộ và tránh lưu trùng trên hai điện thoại từ lúc nào?

➡️ **A (Bờm đề xuất):** Cùng vòng đầu tiên có giao dịch lưu được. **B:** Sau khi hoàn thành các luồng một điện thoại, nhưng trước khi nhóm nhỏ bắt đầu thử toàn bộ chức năng.

## Nhật ký trả lời

Ninh trả lời ba dòng **A, A, B** theo thứ tự Q1–Q3. Các lựa chọn đã được chuyển vào [danh mục chức năng](system-functions.md).

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | R29 áp dụng cả tài khoản quản lý: khi còn dùng được email quản lý đã xác minh, Ninh có thể tự đặt lại mật khẩu qua liên kết gửi tới email đó; các phiên cũ của tài khoản quản lý kết thúc. | Hạn liên kết và xử lý liên kết hết hạn chưa chốt; không lấy mặc định 30 phút của tài khoản ghi chép. Mất email quản lý sau khi đăng xuất vẫn ngoài phạm vi bản đầu. |
| Q2 | Sau tài khoản và ví, ưu tiên bản demo ghi thu/chi, số dư và lịch sử trước chuyển tiền giữa ví. | Đây là thứ tự làm/xem thử, không loại chuyển tiền khỏi bản đầu. |
| Q3 | Kiểm tra đồng bộ và tránh lưu trùng trên hai điện thoại sau khi hoàn thành các luồng một điện thoại, nhưng trước khi nhóm nhỏ bắt đầu thử toàn bộ chức năng. | Cách chia công việc kỹ thuật để bảo đảm kiểm tra được trước nhóm thử sẽ xác định sau. |

## Phụ thuộc cho vòng sau

- Nếu Q1 là A, hỏi hạn liên kết đặt lại mật khẩu quản lý và các chi tiết cần để đặc tả; không lấy mặc định 30 phút của tài khoản ghi chép.
- Sau Q2–Q3, PM chốt bảng ưu tiên còn lại trong bản đầu, đối chiếu điều kiện nhóm thử S02–S13 trước khi trình Ninh xác nhận bước 6.
