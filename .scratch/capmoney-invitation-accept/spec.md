# Bước 9: đặc tả chi tiết nhận lời mời và đăng nhập cá nhân

**Rebaseline 16/10/2026:** Đặc tả hành vi này đã qua chuỗi rà cũ; chưa có `BA_READY`/`TECH_READY` theo workflow mới. Stack Java Spring/React/Vite đã chốt ở [TS10–TS38](../../docs/requirements/tech-stack-round-2.md); [technical-options](technical-options.md) chỉ là khảo sát lịch sử.

**Ticket:** [04 – Nhận lời mời và đăng nhập cá nhân](../capmoney-overall/issues/04-nhan-loi-moi.md)

**Status:** needs-triage

**Rà soát:** BA → PM → Tech → PM đã rà ngày 14/10/2026. Còn kiểm chứng OTP/xác thực; chưa giao Dev.

## Problem Statement

Người được Ninh mời cần tự tạo tài khoản ghi chép bằng đúng email nhận lời mời, sau đó đăng nhập để dùng dữ liệu tài chính riêng. Họ không được tự đăng ký nếu không có lời mời hoặc nhận quyền quản lý của Ninh.

## Solution

Người nhận mở liên kết mời còn hạn, chứng minh quyền dùng email được mời bằng mã gửi tới email đó, tự đặt mật khẩu và hoàn tất tài khoản ghi chép. Sau đó họ đăng nhập bằng email và mật khẩu, thấy dữ liệu tài chính khởi đầu trống của mình; tài khoản quản lý của Ninh vẫn tách biệt.

## User Stories

1. Là người được mời, tôi muốn mở liên kết còn hạn để bắt đầu lập tài khoản ghi chép.
2. Là người được mời, tôi muốn nhận mã tại đúng email của lời mời để chứng minh email thuộc quyền dùng của mình.
3. Là người được mời, tôi muốn nhập mã hợp lệ và tự đặt mật khẩu để không cần Ninh biết mật khẩu của tôi.
4. Là người được mời, tôi muốn đăng nhập bằng email và mật khẩu vừa đặt để truy cập tài khoản riêng.
5. Là người được mời, tôi muốn đăng xuất rồi đăng nhập lại để xác nhận tài khoản vẫn dùng được.
6. Là chủ tài khoản ghi chép mới, tôi muốn dữ liệu tài chính bắt đầu trống và không thấy dữ liệu của Ninh hay người khác.
7. Là người dùng khác, tôi muốn liên kết đã hoàn tất hoặc hai lần bấm gần đồng thời không tạo tài khoản thứ hai hay chuyển dữ liệu của tôi sang người mới.
8. Là Ninh, tôi muốn tài khoản ghi chép vừa lập không thể vào khu vực quản lý hoặc gửi lời mời để quyền quản lý chỉ thuộc tài khoản riêng của tôi.
9. Là người không có lời mời hợp lệ, tôi muốn không có đường tự đăng ký để việc cấp tài khoản vẫn do Ninh kiểm soát.

## Implementation Decisions

### Ninh đã xác nhận

- Chỉ Ninh dùng tài khoản quản lý để gửi lời mời; người nhận tự thiết lập tài khoản ghi chép bằng liên kết còn hạn, mã xác minh gửi đúng email được mời rồi tự đặt mật khẩu. [R13, R21–R22, R28, F01]
- Lời mời có hạn 3 ngày; xác minh mã không kéo dài hạn. Nếu lời mời hết hạn giữa lúc nhập mã, không thể hoàn tất bằng lời mời cũ. Lỗi mã và nhánh lời mời hết hạn thuộc #05. [R41, F01, F26]
- Người nhận đăng nhập bằng email và mật khẩu; tài khoản ghi chép chỉ xem/sửa dữ liệu tài chính của chính người đó và không có quyền quản lý. Tài khoản quản lý và tài khoản ghi chép cá nhân của Ninh là hai tài khoản dùng hai email khác nhau. [R08–R09, R12, R22; người dùng và quyền bước 3]
- Bản mới khởi đầu với dữ liệu tài chính trống, không nhập lịch sử từ app cũ. Mười danh mục mặc định là yêu cầu bản đầu nhưng thuộc ticket #08, không phải điều kiện riêng để #04 hoàn tất. [R03, B16; H05]
- R65 cho phép Ninh gửi lại cùng URL mời chưa dùng, còn hạn tới cùng email khi người nhận không thấy thư; việc này không đổi đường nhận/xác minh tại #04. [R65]

### Tech đề xuất, cần kiểm chứng trước khi giao Dev

- Email đích lấy từ lời mời hợp lệ, không tin địa chỉ sửa trong trình duyệt. Mã chỉ cấp cho đúng email, lời mời và mục đích lập tài khoản; mã của lời mời khác hoặc mã dùng cho mục đích khác không được dùng chéo. Mở liên kết được chuyển tiếp mà không chứng minh được quyền dùng email vẫn không tạo tài khoản.
- Tại lúc hoàn tất, hệ thống kiểm lại liên kết và mã còn hiệu lực; chỉ tạo tài khoản và cấp phiên ghi chép sau khi mã đúng và người nhận tự đặt mật khẩu. Đường nhập mã/hoàn tất trực tiếp không có lời mời hợp lệ bị từ chối.
- Hoàn tất lời mời, ghi nhận quyền dùng email, tạo tối đa một tài khoản ghi chép và vô hiệu hóa quyền hoàn tất bằng liên kết/mã cũ phải cho kết quả nhất quán dưới bấm lặp hoặc hai yêu cầu gần đồng thời. Email đã gắn với tài khoản hoạt động không thể tạo tài khoản thứ hai hoặc làm đổi dữ liệu cũ.
- Quyền quản lý phải được kiểm ở phía hệ thống cho mọi đường truy cập trực tiếp; phiên ghi chép mới không được mời người khác hoặc xem dữ liệu tài chính của tài khoản khác.
- Mã xác minh, phiên, mật khẩu và kết nối với phương án xác thực chung còn cần Tech thiết kế/kiểm chứng trên stack Java Spring, Spring Security, Spring Mail/SMTP và React/Vite đã được Ninh chọn ở [TS10–TS38](../../docs/requirements/tech-stack-round-2.md). [Đối chiếu kỹ thuật cũ](technical-options.md) là lịch sử Better Auth/Next.js, không cấp `TECH_READY` hay hợp đồng API cho stack hiện hành.

### Chưa rõ ở mức thiết kế/Tech

- Thời điểm gửi mã, độ dài/hạn mã, số lần thử và cách hiện lỗi chi tiết chưa được Ninh chọn riêng. Tech phải đề xuất cách bảo vệ và kiểm thử mà không đổi F01/F07/F26; nếu nảy sinh lựa chọn sản phẩm mới thì hỏi Ninh bằng A/B trên giao diện.

## Testing Decisions

- Đi qua web từ email mời tới liên kết còn hạn, mã gửi đúng email và mật khẩu do người nhận đặt; sau hoàn tất, đăng nhập, tải lại, đăng xuất rồi đăng nhập lại vẫn chỉ thấy tài khoản ghi chép của mình.
- Thử cùng liên kết được gửi lại theo R65: hai email có cùng URL vẫn chỉ dẫn tới một lời mời và tối đa một tài khoản. Bấm hoàn tất lặp hoặc hai lần gần đồng thời: tối đa một lần thành công, không tạo tài khoản thứ hai.
- Thử mở liên kết chuyển tiếp mà không có quyền dùng email được mời: không lập được tài khoản. Thử email đã có tài khoản hoạt động và liên kết đã hoàn tất: không chiếm/ghi đè tài khoản cũ hoặc tạo tài khoản thứ hai.
- Thử mã của lời mời khác hoặc mã dùng cho mục đích khác, và gọi đường gửi/nhập mã/hoàn tất trực tiếp không có lời mời hợp lệ: không tạo tài khoản, không cấp phiên vượt quyền.
- Kiểm tài khoản mới chưa có ví, giao dịch hay lịch sử nhập từ app cũ; không thấy dữ liệu tài chính của tài khoản khác. Tài khoản ghi chép không mở được khu vực quản lý, không gửi được lời mời dù gọi đường xử lý trực tiếp.
- Thử trước mốc 3 ngày với liên kết và mã hợp lệ tại lúc hoàn tất. Từ chối mã sai/hết hạn, lời mời hết hạn và mời lại sau hạn thuộc #05; các điều kiện đó không được bỏ qua khi hoàn tất #04.
- Điểm kiểm thử phát hành là web trên iPhone và Android với ít nhất hai tài khoản riêng, theo S05. Công cụ kiểm thử chưa chốt.

## Out of Scope

- Ninh gửi lần đầu hoặc gửi lại liên kết (#03); mã sai/hết hạn, xin mã mới, lời mời hết hạn hoặc mời mới sau hạn (#05).
- Tạo ví và cấp mười danh mục mặc định (#06–#08), giao dịch, đặt lại mật khẩu sau khi đã có tài khoản và đổi email đăng nhập.
- Chốt công nghệ, hợp đồng API, schema hoặc code trước khi Tech/PM hoàn tất rà soát.

## Further Notes

- Nguồn: [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [người dùng và quyền](../../docs/requirements/actors-and-roles.md), [luồng F01/F26](../../docs/requirements/user-flows.md), [H02/H03](../../docs/requirements/system-functions.md), [đặc tả tổng thể](../capmoney-overall/spec.md) và ticket #04.
- #04 phụ thuộc #03 và phương án xác thực chỉ mời của #01. Cần Tech kiểm chứng luồng email/mã, lập tài khoản duy nhất và quyền thực tế trước khi chuyển `ready-for-agent`; xem [đối chiếu kỹ thuật](technical-options.md).
