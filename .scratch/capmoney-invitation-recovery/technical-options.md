# Bước 9: đối chiếu kỹ thuật mã sai và lời mời hết hạn (#05)

**Trạng thái:** Tech đề xuất; BA → PM → Tech → PM đã rà ngày 14/10/2026. Còn spike trước khi chọn công nghệ hoặc giao Dev.

**Phạm vi:** [Đặc tả #05](spec.md), [ticket #05](../capmoney-overall/issues/05-loi-moi-het-han-va-ma-sai.md), F01/F03/F07/F26. Gửi lại **cùng URL còn hạn** theo R65 thuộc #03; hoàn tất tài khoản bằng mã đúng thuộc #04.

## Hợp đồng cần chứng minh

- Hai đồng hồ độc lập: lời mời hết hạn đúng 72 giờ từ lúc yêu cầu gửi đầu được nhà cung cấp tiếp nhận theo #03; mã có hạn riêng do Tech đề xuất. Mã không thể hợp lệ lâu hơn lời mời. Kiểm thời gian tại phía hệ thống khi cấp/thay mã và khi hoàn tất tài khoản; việc mở trang hoặc kiểm mã đúng trước hạn không giữ quyền hoàn tất sau hạn.
- Mã phải thuộc đúng lời mời đang có hiệu lực, email đích cố định của lời mời và mục đích lập tài khoản. Mã từ lời mời cũ, luồng đăng nhập hoặc đặt lại mật khẩu không được dùng. Với lời mời còn hạn, yêu cầu **mã mới** vô hiệu mã trước; một lần nhập sai không được tự suy ra là yêu cầu mã mới. Giới hạn lần thử có thể vô hiệu mã khi vượt ngưỡng, nhưng phải giữ đường xin mã mới trong phần hạn lời mời còn lại.
- Một lời mời chỉ có một mã đang có hiệu lực. Hai yêu cầu xin mã mới gần đồng thời phải có thứ tự duy nhất khi ghi trạng thái; không để cả hai mã dùng được. Việc kiểm mã đúng đơn lẻ không tiêu thụ lời mời. Nếu xin mã mới chạy cùng lúc với **hoàn tất tài khoản**, chỉ một kết quả hợp lệ: hoàn tất thắng thì không cấp mã mới; thay mã thắng thì mã cũ không hoàn tất được. Lời mời đã hoàn tất hoặc hết hạn không thể được tái kích hoạt.
- Gửi mã là tác dụng bên ngoài, không thể nguyên tử cùng lần ghi trạng thái lời mời. Phải xử lý gửi thất bại, timeout và phản hồi không rõ mà không báo thành công sai hoặc khóa người dùng đến hết hạn. Thử lại **cùng lần gửi** giữ định danh và nội dung; người nhận chủ động xin **mã mới** là lần cấp mới, làm mã cũ vô hiệu theo F07. Cần giữ trạng thái để email đến muộn không làm mã cũ dùng lại được.
- Giới hạn yêu cầu gửi mã, số lần thử mã và thời gian chờ phải chia sẻ giữa các phiên xử lý và không khóa vĩnh viễn người được mời. Tech chọn ngưỡng cụ thể để PM duyệt trải nghiệm. Không đưa mã, liên kết hoặc email nhạy cảm vào log công khai.

## Ứng viên cần spike, chưa được chọn

- [Better Auth Email OTP](https://better-auth.com/docs/plugins/email-otp) nêu `expiresIn` mặc định 300 giây, `allowedAttempts` mặc định 3, và `resendStrategy: "rotate"` mặc định cấp mã mới. Cách `"reuse"` gửi lại mã cũ và kéo dài hạn của mã, nên không đáp ứng F07 cho yêu cầu mã mới. Các mặc định này không phải quyết định sản phẩm của Ninh.
- Endpoint OTP của plugin nhận email và loại mã; tài liệu chưa chứng minh ràng buộc mã với **một lời mời CapMoney cụ thể** hoặc tính nguyên tử với hoàn tất tài khoản. Spike phải thử bằng nơi lưu thực tế, hai lời mời cùng email theo thời gian, gọi endpoint trực tiếp, thay mã đồng thời, và lỗi giữa xác minh với tạo tài khoản. Nếu không đạt hợp đồng, dùng cách mã do CapMoney quản lý hoặc chọn giải pháp xác thực khác trước khi giao Dev.
- [Resend idempotency keys](https://resend.com/docs/dashboard/emails/idempotency-keys) chỉ lưu 24 giờ. Chỉ dùng cho thử lại cùng email/payload trong cửa sổ đó; CapMoney phải giữ định danh/trạng thái của lời mời và lần cấp mã suốt 72 giờ. Không coi phản hồi provider là giao dịch chung với dữ liệu.
- [Better Auth Rate Limit](https://better-auth.com/docs/concepts/rate-limit) mặc định lưu bộ đếm trong bộ nhớ tiến trình; nếu dùng, phải chứng minh cấu hình nơi lưu chung và bảo vệ cả đường cấp mã tùy biến. Ngưỡng an toàn và UX khi chạm ngưỡng còn cần Tech/PM chốt.

## Kiểm chứng trước `ready-for-agent`

- Hộp thư thử, đồng hồ điều khiển được và nơi lưu thực tế: thử mã sai, mã hết hạn, xin mã mới, mã cũ bị từ chối, mã mới tới đúng email; thử tại trước, đúng và sau mốc 72 giờ.
- Thử hai lần xin mã mới đồng thời; thử xin mã mới khi một yêu cầu khác hoàn tất tài khoản; thử bấm lặp, timeout provider, email đến muộn và gửi thất bại. Chỉ một mã/tài khoản hợp lệ và thông báo gửi phải phản ánh đúng trạng thái biết được.
- Thử mời mới cùng email sau khi lời mời cũ hết hạn: mã/liên kết cũ tiếp tục bị từ chối; lời mời mới có mốc hạn riêng. Đường công khai không cho người nhận tự tạo lời mời.
- #05 còn `needs-triage`, phụ thuộc #03/#04 và spike xác thực chỉ mời ở #01. PM phải rà lại ngưỡng/UX và các bằng chứng trên trước khi chuyển `ready-for-agent`.
