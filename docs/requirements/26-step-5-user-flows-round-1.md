# Bước 5, vòng 1: vào tài khoản và bắt đầu dùng

**Trạng thái:** Đã trả lời ngày 13/10/2026. Câu hỏi đã được BA, Tech và PM rà soát trước khi hỏi; khi hỏi, các lựa chọn A là đề xuất của Bờm. Ninh đã chọn “a hết” cho Q1–Q6.

**Nguồn đã chốt:** [Phiếu tiếp nhận](00-intake.md), [mục tiêu và phạm vi](goal-and-scope.md), [người dùng và quyền](actors-and-roles.md), [nghiệp vụ chính](core-business.md).

❓ **Q1 – Xác minh email được mời:** Sau khi mở liên kết mời còn hạn, người được mời nên chứng minh quyền dùng đúng email được mời bằng bước bổ sung nào trước khi đặt mật khẩu? Việc xác minh không kéo dài hạn lời mời 3 ngày.

➡️ **A (Bờm đề xuất):** Nhập mã xác minh gửi tới đúng email được mời. **B:** Mở thêm một liên kết xác minh mới gửi tới đúng email đó; cả hai cách đều yêu cầu truy cập email được mời.

---

❓ **Q2 – Lần đầu chưa có ví:** Khi người dùng mới đăng nhập và chưa có ví, có dẫn họ tạo ví đầu tiên trước khi ghi thu/chi không?

➡️ **A (Bờm đề xuất):** Có; đưa đến bước tạo ví đầu tiên và cho nhập số dư ban đầu tùy chọn. **B:** Hiện trang tổng quan trống, người dùng tự chọn tạo ví khi muốn.

---

❓ **Q3 – Mở lời mời đã hết hạn:** Nếu người được mời mở liên kết quá 3 ngày, có hiện thông báo hết hạn và hướng dẫn liên hệ Ninh để nhận lời mời mới không?

➡️ **A (Bờm đề xuất):** Có; Ninh gửi lại lời mời theo quy tắc đã chốt. **B:** Trang hết hạn cho gửi yêu cầu tới Ninh; chỉ Ninh quyết định và gửi lời mời mới, không tự cấp tài khoản.

---

❓ **Q4 – Đặt lại mật khẩu tài khoản ghi chép:** Sau khi người dùng đặt lại mật khẩu tài khoản ghi chép cá nhân qua email, các phiên đang đăng nhập trên điện thoại khác có cần kết thúc không?

➡️ **A (Bờm đề xuất):** Có; người dùng đăng nhập lại bằng mật khẩu mới trên mọi điện thoại. **B:** Các phiên cũ tiếp tục dùng được.

---

❓ **Q5 – Xác nhận xóa tài khoản ghi chép:** Trước khi người dùng xóa vĩnh viễn tài khoản ghi chép cá nhân và toàn bộ dữ liệu ngay, có cần nhập lại mật khẩu và xác nhận lần cuối không? Tài khoản quản lý của Ninh không thuộc luồng tự xóa này.

➡️ **A (Bờm đề xuất):** Có; màn hình nêu rõ dữ liệu không khôi phục được rồi yêu cầu nhập mật khẩu và xác nhận. **B:** Chỉ cần bấm xác nhận cuối, không nhập lại mật khẩu.

---

❓ **Q6 – Xem dữ liệu mới từ điện thoại khác:** Khi cùng tài khoản đang mở trên hai điện thoại, lúc quay lại ứng dụng trên điện thoại thứ hai sau khi điện thoại thứ nhất lưu giao dịch, dữ liệu mới có tự tải lại không?

➡️ **A (Bờm đề xuất):** Có, khi quay lại ứng dụng có Internet thì tự tải dữ liệu mới. **B:** Người dùng phải tự bấm làm mới.

## Nhật ký trả lời

Ninh trả lời **“a hết”** sau khi nhận đủ sáu câu hỏi, áp dụng A cho Q1–Q6.

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Người được mời mở liên kết còn hạn, nhập mã xác minh gửi tới đúng email được mời rồi đặt mật khẩu; xác minh không kéo dài hạn mời 3 ngày. | Cách xử lý mã sai/hết hạn sẽ hỏi tiếp. |
| Q2 | Người dùng mới chưa có ví được dẫn tạo ví đầu tiên trước khi ghi thu/chi; số dư ban đầu có thể bỏ trống. | Khi bỏ trống, giá trị áp dụng và cách trở lại khi thoát giữa chừng sẽ hỏi sau. |
| Q3 | Mở lời mời quá 3 ngày thì thấy thông báo hết hạn và hướng dẫn liên hệ Ninh; Ninh gửi lời mời mới. | Không có luồng tự yêu cầu cấp lời mời trong ứng dụng ở nhánh này. |
| Q4 | Sau khi đặt lại mật khẩu tài khoản ghi chép qua email, mọi phiên đăng nhập của tài khoản đó kết thúc; đăng nhập lại bằng mật khẩu mới. | Hạn của liên kết đặt lại mật khẩu chưa rõ. |
| Q5 | Trước khi xóa vĩnh viễn tài khoản ghi chép và toàn bộ dữ liệu, màn hình nêu rõ không khôi phục được, yêu cầu nhập lại mật khẩu và xác nhận cuối. | Tài khoản quản lý của Ninh không thuộc luồng này; trạng thái trên điện thoại khác sẽ hỏi sau. |
| Q6 | Khi quay lại ứng dụng trên điện thoại thứ hai có Internet, dữ liệu mới lưu từ điện thoại thứ nhất được tự tải. | Bản nhập chưa lưu khi tải dữ liệu mới sẽ hỏi tiếp. |

Các lựa chọn đã được chuyển vào [phiếu luồng người dùng](user-flows.md).

## Phụ thuộc cho vòng sau

- Hỏi khi mã xác minh sai/hết hạn, khi lời mời cũng sắp hết hạn và hạn của liên kết đặt lại mật khẩu.
- Hỏi bản nhập chưa lưu khi tự tải lại và khi mạng mất trong lúc lưu giao dịch.
- Hỏi việc khởi tạo tài khoản quản lý đầu tiên, xác minh người mất email và trạng thái điện thoại khác sau khi xóa tài khoản ở vòng phù hợp.
