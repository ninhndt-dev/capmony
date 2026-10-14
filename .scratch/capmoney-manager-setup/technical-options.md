# Bước 9: phương án kỹ thuật cho ticket #01 và #02

**Trạng thái:** Tech đề xuất; BA → PM → Tech → PM đã rà lại ngày 14/10/2026. Chưa chốt stack và chưa giao Dev. Không tạo tài nguyên ngoài dự án hoặc phát sinh chi phí.
**Phạm vi:** [Thiết lập và đăng nhập tài khoản quản lý](spec.md) (#01) cùng [xin lại liên kết thiết lập hết hạn](../capmoney-manager-link-renewal/spec.md) (#02); các quyết định sản phẩm F11, F23–F24 giữ nguyên.

## Phương án Tech đề xuất

- Một ứng dụng web **Next.js App Router** chạy phần giao diện và xử lý phía server; khi triển khai có thể dùng **Vercel Functions**. Quyền quản lý phải được kiểm tra tại phía server cho mỗi lần truy cập, không dựa vào việc ẩn nút trên giao diện. [Next.js: Authentication](https://nextjs.org/docs/app/guides/authentication).
- **PostgreSQL** cho tài khoản, phiên và về sau là dữ liệu tài chính; **Neon** là ứng viên dịch vụ quản lý PostgreSQL. Cần tách dữ liệu thử và dữ liệu thật. Việc chỉ có một quản lý và hoàn tất liên kết một lần cần được bảo đảm dưới hai yêu cầu đồng thời. [Neon: Connect](https://neon.com/docs/connect/connect-from-any-app).
- **Resend** là ứng viên gửi email thiết lập, lời mời và đặt lại mật khẩu. Trước khi gửi cho người dùng thật, cần xác minh tên miền gửi và cấu hình DNS; Ninh chưa cần cung cấp tên miền ở bước đặc tả. [Resend: Verified Domains](https://resend.com/docs/dashboard/domains/introduction).
- Secret xác thực, kết nối dữ liệu và khóa gửi mail chỉ ở môi trường server, tách môi trường thử/thật; không đưa vào Git hoặc mã chạy trên trình duyệt. Nếu dùng Vercel, dùng biến môi trường nhạy cảm. [Vercel: Sensitive Environment Variables](https://vercel.com/docs/environment-variables/sensitive-environment-variables).

## Xác thực còn cần kiểm chứng

**Better Auth** là ứng viên cho mật khẩu và phiên, **chưa được chốt**. Tài liệu hỗ trợ PostgreSQL và `disableSignUp`, nhưng chưa chứng minh luồng CapMoney: người ngoài không tự đăng ký, Ninh/người được mời xác minh email và tự đặt mật khẩu, chỉ một quản lý. [Better Auth: Installation](https://better-auth.com/docs/installation), [Options](https://better-auth.com/docs/reference/options).

- Không dùng lệnh `create-admin` làm luồng thiết lập: tài liệu cho biết lệnh tạo quản lý và mặc định đánh dấu email đã xác minh, trong khi Ninh phải tự xác minh và đặt mật khẩu qua liên kết. [Better Auth: Admin](https://better-auth.com/docs/plugins/admin).
- Không bật Admin plugin mặc định: quyền mặc định gồm xem danh sách, khóa, đóng vai và sửa tài khoản, vượt quyền Ninh đã chốt. Nếu về sau dùng plugin, phải giới hạn quyền và chứng minh mọi đường gọi trực tiếp ngoài phạm vi đều bị từ chối; phương án ưu tiên cho #01 là không dùng plugin này. [Better Auth: Admin](https://better-auth.com/docs/plugins/admin).
- **Điểm đã đối chiếu ngày 14/10/2026:** `disableSignUp: true` cũng chặn `auth.api.signUpEmail` gọi từ server, vì `auth.api` gọi cùng endpoint và endpoint kiểm cờ này trước khi tạo người dùng. Endpoint này còn yêu cầu mật khẩu ngay khi đăng ký; không thể mặc nhiên dùng nó để tạo tài khoản chưa có mật khẩu rồi để Ninh tự đặt sau. [Mã nguồn Better Auth](https://github.com/better-auth/better-auth/blob/main/packages/better-auth/src/api/routes/sign-up.ts), [API Better Auth](https://better-auth.com/docs/concepts/api).
- Spike xác thực phải thử ít nhất hai hướng trước khi chốt: (A) giữ đăng ký nội bộ, chặn hoàn toàn đường đăng ký công khai và chỉ gọi đăng ký phía server sau khi kiểm liên kết hợp lệ; (B) tìm đường tạo tài khoản/mật khẩu đáng tin cậy khác hoặc chọn thư viện khác. Hướng A chỉ khả thi nếu mọi đường gọi trực tiếp đều bị chặn, Ninh tự chọn mật khẩu sau khi chứng minh quyền dùng email, và tài khoản duy nhất được xác minh đúng cách. Chưa chọn hướng A hay B.
- Trước khi giao Dev, Tech phải chứng minh trong môi trường thử: tự đăng ký công khai bị từ chối kể cả gọi trực tiếp; liên kết hợp lệ dẫn đến đúng một quản lý với mật khẩu Ninh tự chọn và email đã xác minh; hai lần hoàn tất gần đồng thời chỉ một lần thành công; phiên và quyền phía server đúng; không có endpoint quản lý vượt phạm vi. Nếu Better Auth không đáp ứng, chọn cách xác thực khác trước khi viết code sản phẩm.
- Hạn liên kết thiết lập là **72 giờ** theo F23; hạn đặt lại mật khẩu là **30 phút** theo R58/F12 và các phiên cũ phải kết thúc. Không được giữ mặc định thư viện nếu khác hai quyết định này. [Better Auth: Options](https://better-auth.com/docs/reference/options), [Session Management](https://better-auth.com/docs/concepts/session-management).

## Ràng buộc kỹ thuật chung cho #01 và #02 cần kiểm chứng

- Tech đề xuất giữ trạng thái khởi tạo và thế hệ liên kết hiện hành trong nơi lưu chung bền vững. Chỉ liên kết đúng email quản lý đã cấu hình, đúng thế hệ, chưa dùng và còn hạn mới được hoàn tất. Tiêu thụ liên kết và tạo duy nhất một quản lý phải chịu ràng buộc dữ liệu cùng thao tác nguyên tử; xin lại đồng thời với xin lại hoặc hoàn tất không thể để hai liên kết có hiệu lực. Thử trước, đúng và sau mốc 72 giờ, kể cả email tới lệch thứ tự. Đây là định hướng cần chứng minh, chưa phải schema/API đã chốt.
- Resend hỗ trợ [idempotency key](https://resend.com/docs/dashboard/emails/idempotency-keys) trong 24 giờ cho cùng yêu cầu gửi; cơ chế đó không làm thao tác dữ liệu và gửi email thành một giao dịch nguyên tử. Tech phải chứng minh thử lại sau lỗi/timeout mà không báo đã gửi thành công sai hoặc tạo hai liên kết hiện hành. Phân biệt nhà cung cấp nhận yêu cầu gửi với email đã tới hộp thư; khi kết quả chưa rõ, giữ trạng thái chưa rõ hoặc thất bại và cho thử lại an toàn. Trường hợp email được nhận gửi nhưng trạng thái dữ liệu không hoàn tất cũng phải được thử.
- Trang xin lại liên kết của CapMoney cần giới hạn tần suất tại nơi lưu chung cho các phiên xử lý, có thời gian chờ và trần theo cửa sổ; không lộ email/token hoặc khóa Ninh vĩnh viễn. Không dựa vào bộ đếm mặc định [Better Auth Rate Limit](https://better-auth.com/docs/concepts/rate-limit) vì mặc định lưu trong bộ nhớ của từng phiên xử lý. Tech còn chọn ngưỡng cụ thể và kiểm tra cả nhiều yêu cầu/phiên xử lý; PM duyệt tác động lên trải nghiệm trước khi giao Dev.

## Kiểm thử và điều kiện vận hành

- Dùng hộp thư thử đọc được nội dung, đồng hồ điều khiển được và dữ liệu khởi tạo riêng để thử trước/đúng/sau mốc hạn, liên kết dùng lại và hai lần hoàn tất đồng thời. Sau đó thử web trên iPhone và Android như Ninh đã xác nhận ở bước 7.
- Chưa tạo tài khoản nhà cung cấp hoặc chọn gói trả phí. [Vercel Hobby](https://vercel.com/docs/plans/hobby) chỉ dành cho mục đích cá nhân phi thương mại; nếu dự án có mục đích thương mại thì cần gói phù hợp. [Resend Free](https://resend.com/pricing) có giới hạn gửi và [Neon Free](https://neon.com/blog/neon-free-plan-1-gb-per-project) có giới hạn tài nguyên; hạn mức có thể đổi trước khi triển khai thật.
- Khi chuẩn bị mở hệ thống, cần email quản lý riêng của Ninh, tên miền gửi và quyền cập nhật DNS, tài khoản nhà cung cấp thuộc người chịu trách nhiệm vận hành, cùng lựa chọn gói phù hợp. Đây là đầu vào triển khai, chưa phải câu trả lời Ninh phải cung cấp ở bước 9.

## Điểm chặn trước `ready-for-agent`

1. Tech chứng minh phương án xác thực đáp ứng luồng chỉ mời và bootstrap một quản lý, hoặc đổi ứng viên.
2. Tech chốt hạ tầng email, dữ liệu, phiên, thay liên kết, xử lý gửi lỗi/chưa rõ và ngưỡng giới hạn xin lại; chứng minh cách kiểm thử đầu cuối có thể chạy được. PM rà lại phạm vi/quyền và trải nghiệm giới hạn gửi lại.
3. Không đổi trạng thái ticket #01 chỉ vì đã ghi một nhà cung cấp vào phương án này.
