# Đặc tả chi tiết bước 9: thiết lập và đăng nhập tài khoản quản lý

**Ticket:** [01 – Thiết lập và đăng nhập quản lý](../capmoney-overall/issues/01-thiet-lap-quan-ly.md)
**Status:** needs-triage
**Trạng thái rà soát:** BA → PM → Tech → PM đã rà soát, không còn lỗi chặn về hành vi/tiêu chí. Tech còn chọn hạ tầng email và cách lưu trạng thái/phiên; PM rà các quyết định đó trước khi giao Dev. Ticket vẫn `needs-triage`.

## Problem Statement

Ninh cần một tài khoản quản lý riêng để sau này mời người dùng và hỗ trợ lấy lại tài khoản. Lần đầu hệ thống mở chưa có tài khoản quản lý, nên Ninh phải tự thiết lập quyền quản lý từ email đã cung cấp trước đó mà người ngoài không thể tự đăng ký hoặc tạo thêm tài khoản quản lý.

## Solution

Người triển khai khởi tạo hệ thống với email quản lý Ninh đã cung cấp. Hệ thống gửi liên kết thiết lập một lần chỉ tới email này; liên kết hết hạn sau 3 ngày. Ninh mở liên kết còn hạn, xác minh quyền dùng email, tự đặt mật khẩu rồi đăng nhập tài khoản quản lý riêng. Thiết lập thành công chỉ tạo một tài khoản quản lý và liên kết cũ không thể dùng lại.

## User Stories

1. Là Ninh, tôi muốn liên kết thiết lập gửi tới email quản lý tôi đã cung cấp để chỉ tôi bắt đầu tạo tài khoản quản lý.
2. Là Ninh, tôi muốn mở liên kết còn hạn, xác minh email và tự đặt mật khẩu để truy cập bằng tài khoản riêng.
3. Là Ninh, tôi muốn đăng nhập bằng email quản lý và mật khẩu vừa đặt để sử dụng quyền quản lý đã được cấp.
4. Là người không có quyền quản lý, tôi muốn không thể tự đăng ký hoặc dùng email khác để tạo tài khoản quản lý để quyền cấp tài khoản chỉ thuộc Ninh.
5. Là Ninh, tôi muốn liên kết đã hoàn tất hoặc quá hạn không thể tạo thêm tài khoản quản lý để quyền quản lý chỉ có một chủ.
6. Là Ninh, tôi muốn sau đăng nhập thấy khu vực tài khoản quản lý của mình để biết quyền quản lý đã được thiết lập, ngay cả trước khi có chức năng mời người dùng.

## Implementation Decisions

- Luồng này chỉ áp dụng khi chưa hoàn tất thiết lập tài khoản quản lý đầu tiên; email đích là email Ninh cung cấp cho người triển khai trước khi mở hệ thống. Không có ô nhập email khác để tự chọn người quản lý. [F11]
- Liên kết thiết lập gửi qua email, chỉ có thể hoàn tất một lần và hết hạn sau 3 ngày kể từ lúc gửi. Mở liên kết hợp lệ chứng minh quyền dùng đúng email đích; Ninh tự đặt mật khẩu trước khi đăng nhập. [F11, F23]
- Tài khoản quản lý tách khỏi tài khoản ghi chép cá nhân của Ninh. Chức năng mời người dùng và hỗ trợ mất email là các ticket sau; #01 chỉ cần thiết lập và đăng nhập tài khoản quản lý. Không có quản lý dự phòng hoặc tự đăng ký. [bước 3, R13]
- Sau đăng nhập, Ninh có khu vực tài khoản quản lý tối thiểu hiển thị tài khoản đang đăng nhập. Khu vực này chỉ phiên quản lý hợp lệ xem được, kể cả khi tải lại; đường dẫn trực tiếp khi chưa đăng nhập bị từ chối. Không cần thao tác mời người dùng của #03 để chứng minh quyền ở #01.
- Việc hoàn tất thiết lập phải là một lần duy nhất ngay cả khi Ninh bấm xác nhận lặp hoặc có hai lần hoàn tất gần đồng thời: nhiều nhất một lần thành công và chỉ có một tài khoản quản lý.
- Nếu liên kết quá hạn trước khi thiết lập xong, không hoàn tất bằng liên kết cũ; xin liên kết mới thuộc ticket #02. Đặt lại mật khẩu quản lý thuộc #22, đổi email quản lý thuộc #23. [F24, R57–R58, R61]
- Email quản lý đã khai được cấu hình trước khi gửi liên kết; không thể tự thay bằng email nhập trên web. Các ràng buộc kỹ thuật tối thiểu do Tech đề xuất nằm ngay dưới đây; không lấy code iOS/API cũ làm nền.

### Hợp đồng kỹ thuật tối thiểu Tech đề xuất

- Người triển khai nạp email quản lý qua cấu hình chỉ dành cho khởi tạo, cố định trước khi gửi liên kết. Web không nhận email quản lý mới; chỉ gửi liên kết thiết lập khi chưa có tài khoản quản lý.
- Liên kết không đoán được, gắn với đúng mục đích thiết lập và email đã cấu hình; hết hạn sau 72 giờ từ lúc hệ thống ghi nhận gửi thành công. Hệ thống từ chối liên kết hết hạn hoặc đã dùng và không ghi liên kết vào log.
- Mật khẩu không được lưu hoặc ghi log ở dạng đọc được. Chỉ cấp phiên sau xác thực; mỗi lần mở khu vực quản lý đều kiểm quyền ở phía hệ thống, kể cả truy cập đường dẫn trực tiếp. Phiên hợp lệ tiếp tục dùng được sau tải lại trang.
- Việc kiểm liên kết, tạo đúng một tài khoản quản lý và vô hiệu hóa liên kết phải cho kết quả như một thao tác không thể hoàn tất dở. Nhấn xác nhận lặp hoặc hai yêu cầu gần đồng thời: tối đa một thành công; yêu cầu còn lại bị từ chối, không có tài khoản quản lý thứ hai.
- Cách hiện thực cấu hình, lưu trạng thái, mật khẩu và phiên, cùng nhà cung cấp email/nơi triển khai sẽ do Tech chọn trước khi Dev nhận việc; phần này chưa chọn framework, schema hoặc API.

## Testing Decisions

- Kiểm tra tại luồng web từ email nhận được đến thiết lập, đăng nhập và quyền thực tế, rồi tải lại trang. Dùng môi trường khởi tạo riêng để thử liên kết hết hạn hoặc đã dùng, không xóa dữ liệu người dùng thật. [S05, F11, F23]
- Trường hợp thành công: đúng email Ninh đã khai nhận liên kết; Ninh hoàn tất trước 3 ngày, đặt mật khẩu và đăng nhập được tài khoản quản lý duy nhất; sau tải lại khu vực tài khoản quản lý vẫn mở được bằng phiên hợp lệ.
- Trường hợp từ chối: email khác không thể tự khởi tạo quản lý; liên kết đã hoàn tất hoặc hết hạn không tạo thêm quản lý; truy cập trực tiếp khu vực tài khoản quản lý khi chưa đăng nhập bị từ chối. Việc kiểm tra tài khoản ghi chép không có quyền quản lý thuộc ticket #04.
- Cho hai lần hoàn tất cùng liên kết gần đồng thời hoặc bấm xác nhận lặp: tối đa một lần thành công, lần còn lại bị từ chối; kiểm tra cuối cùng chỉ có một tài khoản và một quyền quản lý. Kiểm tra hạn bằng hộp thư thử và đồng hồ điều khiển được ở mốc trước, đúng và sau 72 giờ, trong môi trường khởi tạo riêng; không chờ ba ngày thật hoặc đặt lại dữ liệu người dùng thật.
- Điểm kiểm thử chính là hành vi web trên iPhone và Android và quyền đọc lại sau tải trang, đã được Ninh xác nhận ở bước 7. Bước này chưa chọn framework kiểm thử hay bộ test cụ thể.

## Out of Scope

- Xin lại liên kết thiết lập hết hạn (#02), mời người dùng (#03), đặt lại mật khẩu quản lý (#22), đổi email quản lý (#23), tài khoản ghi chép cá nhân của Ninh và khôi phục quản lý khi Ninh mất email sau đăng xuất.
- Chức năng mời người dùng và màn hình dữ liệu tài chính. Tech chọn nhà cung cấp email và nơi triển khai trước khi Dev nhận ticket #01.

## Further Notes

- Nguồn: [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [vai trò và quyền](../../docs/requirements/actors-and-roles.md), [luồng F11/F23–F24](../../docs/requirements/user-flows.md), [danh mục H01](../../docs/requirements/system-functions.md), [đặc tả tổng thể](../capmoney-overall/spec.md) và ticket #01.
- Mục này là bản tổng hợp cho module đầu tiên ở bước 9. Email quản lý thật của Ninh là đầu vào khi chuẩn bị mở hệ thống; Ninh chưa cần cung cấp ở bước đặc tả. Tech đã đề xuất ràng buộc tối thiểu; [phương án hạ tầng](technical-options.md) vẫn là đề xuất, cần kiểm chứng xác thực và chốt trước khi giao Dev. Chưa có code ứng dụng.
