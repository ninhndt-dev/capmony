# Bước 9: phương án kỹ thuật cho ticket #01

**Trạng thái:** Tech đề xuất, chưa chốt stack và chưa giao Dev. Không tạo tài nguyên ngoài dự án hoặc phát sinh chi phí.
**Phạm vi:** [Thiết lập và đăng nhập tài khoản quản lý](spec.md); các quyết định sản phẩm F11, F23–F24 và ticket #01 giữ nguyên.

## Phương án Tech đề xuất

- Một ứng dụng web **Next.js App Router** chạy phần giao diện và xử lý phía server; khi triển khai có thể dùng **Vercel Functions**. Quyền quản lý phải được kiểm tra tại phía server cho mỗi lần truy cập, không dựa vào việc ẩn nút trên giao diện. [Next.js: Authentication](https://nextjs.org/docs/app/guides/authentication).
- **PostgreSQL** cho tài khoản, phiên và về sau là dữ liệu tài chính; **Neon** là ứng viên dịch vụ quản lý PostgreSQL. Cần tách dữ liệu thử và dữ liệu thật. Việc chỉ có một quản lý và hoàn tất liên kết một lần cần được bảo đảm dưới hai yêu cầu đồng thời. [Neon: Connect](https://neon.com/docs/connect/connect-from-any-app).
- **Resend** là ứng viên gửi email thiết lập, lời mời và đặt lại mật khẩu. Trước khi gửi cho người dùng thật, cần xác minh tên miền gửi và cấu hình DNS; Ninh chưa cần cung cấp tên miền ở bước đặc tả. [Resend: Verified Domains](https://resend.com/docs/dashboard/domains/introduction).
- Secret xác thực, kết nối dữ liệu và khóa gửi mail chỉ ở môi trường server, tách môi trường thử/thật; không đưa vào Git hoặc mã chạy trên trình duyệt. Nếu dùng Vercel, dùng biến môi trường nhạy cảm. [Vercel: Sensitive Environment Variables](https://vercel.com/docs/environment-variables/sensitive-environment-variables).

## Xác thực còn cần kiểm chứng

**Better Auth** là ứng viên cho mật khẩu và phiên, **chưa được chốt**. Tài liệu hỗ trợ PostgreSQL và `disableSignUp`, nhưng chưa chứng minh luồng CapMoney: người ngoài không tự đăng ký, Ninh/người được mời xác minh email và tự đặt mật khẩu, chỉ một quản lý. [Better Auth: Installation](https://better-auth.com/docs/installation), [Options](https://better-auth.com/docs/reference/options).

- Không dùng lệnh `create-admin` làm luồng thiết lập: tài liệu cho biết lệnh tạo quản lý và mặc định đánh dấu email đã xác minh, trong khi Ninh phải tự xác minh và đặt mật khẩu qua liên kết. [Better Auth: Admin](https://better-auth.com/docs/plugins/admin).
- Không bật Admin plugin mặc định: quyền mặc định gồm xem danh sách, khóa, đóng vai và sửa tài khoản, vượt quyền Ninh đã chốt. Nếu về sau dùng plugin, phải giới hạn quyền và chứng minh mọi đường gọi trực tiếp ngoài phạm vi đều bị từ chối; phương án ưu tiên cho #01 là không dùng plugin này. [Better Auth: Admin](https://better-auth.com/docs/plugins/admin).
- Trước khi khóa thư viện và giao Dev, Tech cần kiểm chứng trong môi trường thử: tự đăng ký bị từ chối; đường phía server tạo đúng tài khoản chưa xác minh từ liên kết hợp lệ khi đăng ký công khai bị tắt; Ninh tự xác minh và đặt mật khẩu; hai lần hoàn tất đồng thời chỉ một lần thành công; phiên và quyền phía server đúng; không có endpoint quản lý vượt phạm vi. Nếu Better Auth không đáp ứng, chọn cách xác thực khác trước khi viết code sản phẩm.
- Hạn liên kết thiết lập là **72 giờ** theo F23; hạn đặt lại mật khẩu là **30 phút** theo R58/F12 và các phiên cũ phải kết thúc. Không được giữ mặc định thư viện nếu khác hai quyết định này. [Better Auth: Options](https://better-auth.com/docs/reference/options), [Session Management](https://better-auth.com/docs/concepts/session-management).

## Kiểm thử và điều kiện vận hành

- Dùng hộp thư thử đọc được nội dung, đồng hồ điều khiển được và dữ liệu khởi tạo riêng để thử trước/đúng/sau mốc hạn, liên kết dùng lại và hai lần hoàn tất đồng thời. Sau đó thử web trên iPhone và Android như Ninh đã xác nhận ở bước 7.
- Chưa tạo tài khoản nhà cung cấp hoặc chọn gói trả phí. [Vercel Hobby](https://vercel.com/docs/plans/hobby) chỉ dành cho mục đích cá nhân phi thương mại; nếu dự án có mục đích thương mại thì cần gói phù hợp. [Resend Free](https://resend.com/pricing) có giới hạn gửi và [Neon Free](https://neon.com/blog/neon-free-plan-1-gb-per-project) có giới hạn tài nguyên; hạn mức có thể đổi trước khi triển khai thật.
- Khi chuẩn bị mở hệ thống, cần email quản lý riêng của Ninh, tên miền gửi và quyền cập nhật DNS, tài khoản nhà cung cấp thuộc người chịu trách nhiệm vận hành, cùng lựa chọn gói phù hợp. Đây là đầu vào triển khai, chưa phải câu trả lời Ninh phải cung cấp ở bước 9.

## Điểm chặn trước `ready-for-agent`

1. Tech chứng minh phương án xác thực đáp ứng luồng chỉ mời và bootstrap một quản lý, hoặc đổi ứng viên.
2. Tech chốt hạ tầng email, dữ liệu, phiên và cách kiểm thử đầu cuối có thể chạy được; PM rà lại phạm vi/quyền.
3. Không đổi trạng thái ticket #01 chỉ vì đã ghi một nhà cung cấp vào phương án này.
