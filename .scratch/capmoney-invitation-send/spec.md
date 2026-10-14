# Bước 9: đặc tả chi tiết gửi lời mời tài khoản ghi chép

**Ticket:** [03 – Gửi lời mời tài khoản ghi chép](../capmoney-overall/issues/03-gui-loi-moi.md)

**Status:** needs-info

**Rà soát:** BA → PM → Tech → PM đã rà phần độc lập ngày 14/10/2026. Cách gửi lại lời mời chưa dùng, còn hạn đang chờ Ninh chọn; chưa giao Dev.

## Problem Statement

Ninh cần tự cấp quyền bắt đầu tài khoản ghi chép cho khoảng 10 người mà không mở đăng ký công khai. Người được mời phải nhận liên kết ở đúng email Ninh đã chọn; tài khoản quản lý riêng không được dùng để đọc dữ liệu tài chính của họ.

## Solution

Sau khi đăng nhập tài khoản quản lý, Ninh nhập email người muốn mời và gửi lời mời qua email. Chỉ tài khoản quản lý của Ninh được yêu cầu gửi; lời mời ban đầu có hạn 3 ngày. Người nhận sẽ tự xác minh email và hoàn tất tài khoản ở ticket #04. Nếu lời mời hết hạn, Ninh có thể mời lại ở ticket #05.

## User Stories

1. Là Ninh, tôi muốn gửi lời mời tới email của người tôi chọn để người đó có đường thiết lập tài khoản riêng.
2. Là Ninh, tôi muốn dùng tài khoản quản lý đã đăng nhập để gửi lời mời, kể cả khi mời email ghi chép thứ hai của mình.
3. Là người được mời, tôi muốn email chứa liên kết tới lời mời của mình để bắt đầu xác minh ở ticket #04.
4. Là người được mời, tôi muốn lời mời còn hiệu lực trong 3 ngày để có thời gian bắt đầu thiết lập.
5. Là người chưa được mời, tôi muốn đường đăng ký công khai bị từ chối để không ai tự lập tài khoản ghi chép.
6. Là chủ dữ liệu tài chính, tôi muốn việc Ninh gửi lời mời không làm lộ dữ liệu của mình cho tài khoản quản lý.
7. Là Ninh, tôi muốn biết khi yêu cầu gửi chưa được xác nhận để không tưởng rằng email đã được gửi thành công.
8. Là người đã có tài khoản, tôi muốn không có lời mời mới có thể dùng để lập thêm tài khoản từ email đang hoạt động của mình.

## Implementation Decisions

### Ninh đã xác nhận

- Chỉ tài khoản quản lý riêng của Ninh được gửi lời mời qua email; không có tự đăng ký hay người quản lý dự phòng. Tài khoản ghi chép cá nhân của Ninh dùng email khác và không có quyền gửi lời mời. [R13, R21, R28; người dùng và quyền bước 3]
- Lời mời người dùng hết hạn sau 3 ngày. Ninh có thể gửi lời mời mới tới cùng email sau khi lời mời cũ hết hạn; bản đầu không cần hủy lời mời trước hạn. [R41, R47; bước 3, F03]
- Người nhận xác minh đúng email được mời trước khi lập tài khoản; mã xác minh và việc hoàn tất thuộc ticket #04–#05. [F01, F07, F26]
- Tài khoản quản lý không được xem dữ liệu tài chính của người được mời. Ninh có thể tự mời email thứ hai để lập tài khoản ghi chép cá nhân riêng. [người dùng và quyền bước 3]

### Tech đề xuất, cần kiểm chứng trước khi giao Dev

- Kiểm quyền ở phía hệ thống cho từng yêu cầu gửi, kể cả gọi đường xử lý trực tiếp. Phiên không hợp lệ hoặc tài khoản ghi chép không được gửi; chỉ ẩn nút trên giao diện là chưa đủ.
- Lời mời gửi tới đúng email Ninh nhập, với một mốc hết hạn 72 giờ gắn với lần gửi đầu được nhà cung cấp tiếp nhận. Thông báo gửi thành công chỉ xuất hiện sau khi nhà cung cấp tiếp nhận yêu cầu; chưa được xem là bằng chứng email đã tới hộp thư.
- Nếu nhà cung cấp trả lỗi hoặc kết quả chưa rõ, không báo gửi thành công sai. Ninh được thử lại an toàn; bấm lặp hoặc hai yêu cầu gần đồng thời không làm xuất hiện nhiều lời mời có thể dùng cho cùng trường hợp.
- Không phát hành lời mời có thể dùng cho email đang gắn với tài khoản ghi chép còn hoạt động. Việc chặn hoàn tất tạo tài khoản thứ hai là tiêu chí của #04; cách báo khi Ninh nhập email ấy còn là lựa chọn thiết kế, chưa được Ninh xác nhận riêng.
- [Phương án kỹ thuật](technical-options.md) ghi các điều cần kiểm chứng về xác thực, gửi email, đồng thời và giới hạn tần suất. Đây chưa phải schema, API hoặc stack đã chốt.

### Chưa rõ, chờ Ninh chọn

- Khi lời mời **chưa dùng và còn hạn**, người nhận không thấy email: Ninh được gửi lại cùng liên kết với hạn gốc (A, Bờm đề xuất) hay phải chờ hết hạn mới mời lại (B)? Câu này khác việc xin lại mã xác minh khi lời mời còn hạn [F07] và mời mới sau khi lời mời hết hạn [F03]. Chưa chốt hành vi hoặc tiêu chí nghiệm thu cho nhánh này.

## Testing Decisions

- Kiểm luồng web từ phiên quản lý của Ninh tới email nhận được và mốc hết hạn 3 ngày gắn với lần gửi đầu trong môi trường thử. Việc mở liên kết và từ chối tại mốc hết hạn được kiểm ở #04–#05 bằng đồng hồ điều khiển được; không chờ ba ngày thật.
- Gọi đường gửi trực tiếp khi chưa đăng nhập, khi dùng tài khoản ghi chép của Ninh hoặc người khác: đều bị từ chối; người ngoài không có đường tự đăng ký.
- Thử email người nhận đã có tài khoản: #03 không phát hành lời mời có thể dùng; kiểm không tạo tài khoản thứ hai hoặc đổi dữ liệu cũ thuộc #04. Thử mời email ghi chép thứ hai của Ninh: được gửi theo quyền quản lý.
- Giả lập lỗi/timeout gửi email, nhấn gửi lặp và hai yêu cầu gần đồng thời: không báo thành công sai, không tạo nhiều lời mời có thể dùng; có thể thử lại an toàn. Chưa kiểm kết quả nhánh gửi lại lời mời còn hạn cho tới khi Ninh trả lời.
- Việc nhận lời mời, nhập mã, đặt mật khẩu và đọc dữ liệu trống được kiểm đầu cuối ở #04; mã sai/hết hạn và lời mời hết hạn ở #05. Điểm kiểm thử phát hành vẫn là web trên iPhone và Android với ít nhất hai tài khoản riêng. [S05]

## Out of Scope

- Tạo tài khoản và đăng nhập của người được mời (#04); mã xác minh sai/hết hạn, lời mời hết hạn và mời mới sau hạn (#05).
- Danh sách email hoặc trạng thái mọi lời mời, hủy lời mời trước hạn, khóa tài khoản và quyền xem dữ liệu tài chính của người khác.
- Chốt nhà cung cấp, schema/API hoặc viết code ứng dụng ở bước đặc tả này.

## Further Notes

- Nguồn: [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [người dùng và quyền](../../docs/requirements/actors-and-roles.md), [luồng người dùng](../../docs/requirements/user-flows.md), [H02](../../docs/requirements/system-functions.md) và [đặc tả tổng thể](../capmoney-overall/spec.md).
- Ticket #03 còn phụ thuộc #01. Dù Ninh trả lời câu hỏi đang mở, phương án xác thực chỉ mời của #01 và các ràng buộc gửi email vẫn phải được Tech chứng minh trước khi chuyển `ready-for-agent`.
