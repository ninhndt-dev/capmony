# Bước 9: đối chiếu kỹ thuật cho nhận lời mời (#04)

**Trạng thái:** Tech đề xuất, BA → PM → Tech → PM đã rà ngày 14/10/2026. Better Auth và Email OTP chỉ là ứng viên, chưa giao Dev.

**Phạm vi:** [Đặc tả nhận lời mời](spec.md), [ticket #04](../capmoney-overall/issues/04-nhan-loi-moi.md), F01/F26 và quyền tài khoản ghi chép. #03 phát hành/gửi lại liên kết; #05 xử lý mã sai/hết hạn và lời mời hết hạn.

## Hợp đồng cần chứng minh

- Email đích phải lấy từ lời mời hợp lệ, không tin địa chỉ email nhập hoặc sửa trong trình duyệt. Mã chỉ gửi tới đúng email đó và gắn với lời mời, mục đích lập tài khoản; không dùng mã từ lời mời khác hoặc luồng đăng nhập/đặt lại mật khẩu.
- Khi hoàn tất, kiểm lại liên kết và mã vẫn hợp lệ; Ninh đã chọn rằng xác minh không kéo dài hạn lời mời. Chỉ khi chứng cứ email hợp lệ và người được mời tự đặt mật khẩu thành công mới tạo tài khoản ghi chép, đánh dấu email đã xác minh và cấp phiên đúng quyền.
- Tiêu thụ lời mời/mã, tạo một tài khoản và cấp phiên phải nhất quán dưới bấm lặp, hai yêu cầu gần đồng thời hoặc lỗi giữa chừng. Đường nhập mã/hoàn tất gọi trực tiếp mà không có lời mời hợp lệ không tạo tài khoản. Email đã có tài khoản không bị chiếm, ghi đè hoặc tạo thêm tài khoản thứ hai; tài khoản đã xóa và được Ninh mời lại bắt đầu trống theo quyền bước 3.
- Phiên ghi chép không có quyền quản lý, kể cả khi gọi đường quản lý trực tiếp; không được đọc dữ liệu tài chính của tài khoản khác. Kiểm quyền phía hệ thống theo [Next.js Authentication](https://nextjs.org/docs/app/guides/authentication).

## Ứng viên cần spike, chưa được chọn

- [Better Auth Email OTP](https://better-auth.com/docs/plugins/email-otp) có khả năng gửi/kiểm mã, nhưng endpoint công khai nhận email và không tự ràng buộc với lời mời CapMoney. Phải chứng minh chặn mọi đường gửi mã hoặc tạo tài khoản ngoài lời mời. Không coi lệnh kiểm mã riêng là bằng chứng một lần để tạo tài khoản nếu chưa chứng minh việc tiêu thụ mã và tạo tài khoản nhất quán.
- Plugin có đường đăng nhập bằng OTP có thể tạo người dùng nếu không chặn đăng ký; phải kiểm thử gọi trực tiếp. [Better Auth email/password sign-up](https://github.com/better-auth/better-auth/blob/main/packages/better-auth/src/api/routes/sign-up.ts) cũng kiểm `disableSignUp` ngay cả khi gọi từ server, như đã ghi cho [#01](../capmoney-manager-setup/technical-options.md). Nếu các endpoint không đáp ứng, dùng cách mã xác minh riêng hoặc chọn giải pháp xác thực khác trước khi giao Dev.
- F07 yêu cầu xin mã mới làm mã cũ hết hiệu lực mà hạn lời mời không đổi. Không dùng cách gửi lại mã cũ hoặc kéo dài hạn của nó để thay quyết định này. Thời hạn mã, số lần thử, lưu mã an toàn và giới hạn yêu cầu do Tech chốt rồi PM rà, không mặc nhiên lấy mặc định thư viện.

## Kiểm chứng trước `ready-for-agent`

- Dùng môi trường riêng với nơi lưu thực tế, hộp thư thử và đồng hồ điều khiển được: email mời từ #03, mã tới đúng email, nhập mã, tự đặt mật khẩu, đăng nhập lại sau tải trang. Tài khoản mới chỉ có quyền ghi chép và chưa có ví/giao dịch cũ; mười danh mục mặc định thuộc #08.
- Thử đường công khai tạo tài khoản, gửi/kiểm mã và hoàn tất gọi trực tiếp khi không có lời mời; mã từ lời mời khác hoặc mục đích khác; bấm lặp và hai lần hoàn tất đồng thời. Chỉ một tài khoản/phiên đúng quyền được tạo; lời mời đã hoàn tất không dùng lại.
- Kiểm lỗi giữa xác minh mã, tạo tài khoản và cấp phiên: không để tài khoản hoạt động dở, không có phiên vượt quyền. #05 kiểm mã sai/hết hạn và lời mời hết hạn, kể cả hết hạn trong lúc nhập mã.
- #04 vẫn `needs-triage` và phụ thuộc #03 cùng spike xác thực chỉ mời của #01. Việc ghi một tên thư viện ở đây chưa chứng minh luồng và không chuyển ticket sang `ready-for-agent`.
