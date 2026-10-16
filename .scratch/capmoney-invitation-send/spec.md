# Bước 9: đặc tả chi tiết gửi lời mời tài khoản ghi chép

**Rebaseline 16/10/2026:** Đặc tả hành vi này đã qua chuỗi rà cũ; chưa có `BA_READY`/`TECH_READY` theo workflow mới. Stack Java Spring/React/Vite đã chốt ở [TS10–TS38](../../docs/requirements/tech-stack-round-2.md); [technical-options](technical-options.md) chỉ là khảo sát lịch sử.

**Ticket:** [03 – Gửi lời mời tài khoản ghi chép](../capmoney-overall/issues/03-gui-loi-moi.md)

**Status:** needs-triage

**Rà soát:** BA → PM → Tech → PM đã rà quyết định R65 ngày 14/10/2026. Còn kiểm chứng xác thực và gửi email; chưa giao Dev.

## Problem Statement

Ninh cần tự cấp quyền bắt đầu tài khoản ghi chép cho khoảng 10 người mà không mở đăng ký công khai. Người được mời phải nhận liên kết ở đúng email Ninh đã chọn; tài khoản quản lý riêng không được dùng để đọc dữ liệu tài chính của họ.

## Solution

Sau khi đăng nhập tài khoản quản lý, Ninh nhập email người muốn mời và gửi lời mời qua email. Chỉ tài khoản quản lý của Ninh được yêu cầu gửi; lời mời ban đầu có hạn 3 ngày. Khi người nhận không thấy thư mà lời mời chưa dùng, còn hạn, Ninh có thể gửi lại chính liên kết đó tới cùng email, không kéo dài hạn. Người nhận tự xác minh email và hoàn tất tài khoản ở ticket #04. Nếu lời mời hết hạn, Ninh có thể mời mới ở ticket #05.

## User Stories

1. Là Ninh, tôi muốn gửi lời mời tới email của người tôi chọn để người đó có đường thiết lập tài khoản riêng.
2. Là Ninh, tôi muốn dùng tài khoản quản lý đã đăng nhập để gửi lời mời, kể cả khi mời email ghi chép thứ hai của mình.
3. Là người được mời, tôi muốn email chứa liên kết tới lời mời của mình để bắt đầu xác minh ở ticket #04.
4. Là người được mời, tôi muốn lời mời còn hiệu lực trong 3 ngày để có thời gian bắt đầu thiết lập.
5. Là người chưa được mời, tôi muốn đường đăng ký công khai bị từ chối để không ai tự lập tài khoản ghi chép.
6. Là chủ dữ liệu tài chính, tôi muốn việc Ninh gửi lời mời không làm lộ dữ liệu của mình cho tài khoản quản lý.
7. Là Ninh, tôi muốn biết khi yêu cầu gửi chưa được xác nhận để không tưởng rằng email đã được gửi thành công.
8. Là người đã có tài khoản, tôi muốn không có lời mời mới có thể dùng để lập thêm tài khoản từ email đang hoạt động của mình.
9. Là Ninh, tôi muốn gửi lại cùng liên kết tới email ban đầu khi người nhận không thấy thư và lời mời còn hạn, để họ có thể nhận thư mà không phải đợi hết hạn.
10. Là người được mời, tôi muốn hạn 3 ngày không thay đổi sau khi email được gửi lại để biết hai email dẫn tới cùng một lời mời.

## Implementation Decisions

### Ninh đã xác nhận

- Chỉ tài khoản quản lý riêng của Ninh được gửi lời mời qua email; không có tự đăng ký hay người quản lý dự phòng. Tài khoản ghi chép cá nhân của Ninh dùng email khác và không có quyền gửi lời mời. [R13, R21, R28; người dùng và quyền bước 3]
- Lời mời người dùng hết hạn sau 3 ngày. Ninh có thể gửi lời mời mới tới cùng email sau khi lời mời cũ hết hạn; bản đầu không cần hủy lời mời trước hạn. [R41, R47; bước 3, F03]
- Người nhận xác minh đúng email được mời trước khi lập tài khoản; mã xác minh và việc hoàn tất thuộc ticket #04–#05. [F01, F07, F26]
- Tài khoản quản lý không được xem dữ liệu tài chính của người được mời. Ninh có thể tự mời email thứ hai để lập tài khoản ghi chép cá nhân riêng. [người dùng và quyền bước 3]
- Nếu lời mời chưa dùng, còn hạn và người nhận không thấy thư, Ninh gửi lại **chính liên kết đó** tới cùng email; không tạo lời mời mới hoặc đổi mốc hết hạn của lần gửi đầu. Đây là quyết định R65, khác F07 xin mã xác minh mới và F03/F26 mời mới sau hạn. Không suy sang trường hợp người nhận đã mở liên kết, đang nhập mã dở. [R65]

### Tech đề xuất, cần kiểm chứng trước khi giao Dev

- Kiểm quyền ở phía hệ thống cho từng yêu cầu gửi, kể cả gọi đường xử lý trực tiếp. Phiên không hợp lệ hoặc tài khoản ghi chép không được gửi; chỉ ẩn nút trên giao diện là chưa đủ.
- Lời mời gửi tới đúng email Ninh nhập, với một mốc hết hạn 72 giờ gắn với lần gửi đầu được nhà cung cấp tiếp nhận. Thông báo gửi thành công chỉ xuất hiện sau khi nhà cung cấp tiếp nhận yêu cầu; chưa được xem là bằng chứng email đã tới hộp thư.
- Nếu nhà cung cấp trả lỗi hoặc kết quả chưa rõ, không báo gửi thành công sai. Ninh được thử lại an toàn; bấm lặp hoặc hai yêu cầu gần đồng thời không làm xuất hiện nhiều lời mời có thể dùng cho cùng trường hợp.
- Gửi lại theo R65 là một email mới chứa cùng URL của lời mời ban đầu, tới cùng email và giữ hạn gốc. Thử lại một lần gửi bị timeout là thao tác khác: nó không được tự biến thành yêu cầu gửi lại có chủ ý. Tech cần chứng minh cách giữ hoặc tái tạo URL cũ một cách kín ở phía hệ thống, qua cả khi khởi động lại và nhiều phiên xử lý.
- Không phát hành lời mời có thể dùng cho email đang gắn với tài khoản ghi chép còn hoạt động. Việc chặn hoàn tất tạo tài khoản thứ hai là tiêu chí của #04; cách báo khi Ninh nhập email ấy còn là lựa chọn thiết kế, chưa được Ninh xác nhận riêng.
- [Phương án kỹ thuật](technical-options.md) ghi các điều cần kiểm chứng về xác thực, gửi email, đồng thời và giới hạn tần suất. Đây chưa phải schema, API hoặc stack đã chốt.

### Chưa rõ, chưa được Ninh xác nhận riêng

- R65 không xác định thao tác gửi lại khi người nhận đã mở liên kết và đang nhập mã dở; nội dung thông báo cụ thể cũng chưa chốt. Điều này không thay đổi việc mã xác minh có thể xin mới theo F07 và lời mời hết hạn phải mời mới theo F03/F26.

## Testing Decisions

- Kiểm luồng web từ phiên quản lý của Ninh tới email nhận được và mốc hết hạn 3 ngày gắn với lần gửi đầu trong môi trường thử. Việc mở liên kết và từ chối tại mốc hết hạn được kiểm ở #04–#05 bằng đồng hồ điều khiển được; không chờ ba ngày thật.
- Gọi đường gửi trực tiếp khi chưa đăng nhập, khi dùng tài khoản ghi chép của Ninh hoặc người khác: đều bị từ chối; người ngoài không có đường tự đăng ký.
- Thử email người nhận đã có tài khoản: #03 không phát hành lời mời có thể dùng; kiểm không tạo tài khoản thứ hai hoặc đổi dữ liệu cũ thuộc #04. Thử mời email ghi chép thứ hai của Ninh: được gửi theo quyền quản lý.
- Giả lập lỗi/timeout gửi email, nhấn gửi lặp và hai yêu cầu gần đồng thời: không báo thành công sai, không tạo nhiều lời mời có thể dùng; có thể thử lại an toàn.
- Gửi lại có chủ ý khi lời mời chưa dùng, còn hạn: hai email trong hộp thư thử có **cùng URL**, cùng email đích và một mốc hết hạn gốc; không có lời mời mới. Thử gửi lại nhiều lần cách nhau hợp lệ và hai lần bấm gần đồng thời; thử lại do timeout không tự gửi thêm email như một yêu cầu gửi lại mới.
- Thử yêu cầu gửi lại sát mốc 72 giờ: nếu nhà cung cấp tiếp nhận sau mốc, không gia hạn hoặc báo rằng liên kết vẫn dùng được. Mở liên kết đúng/sau mốc hoặc sau khi hoàn tất vẫn bị từ chối ở #04–#05; email tới muộn không mở đường lập thêm tài khoản.
- Việc nhận lời mời, nhập mã, đặt mật khẩu và đọc dữ liệu trống được kiểm đầu cuối ở #04; mã sai/hết hạn và lời mời hết hạn ở #05. Điểm kiểm thử phát hành vẫn là web trên iPhone và Android với ít nhất hai tài khoản riêng. [S05]

## Out of Scope

- Tạo tài khoản và đăng nhập của người được mời (#04); mã xác minh sai/hết hạn, lời mời hết hạn và mời mới sau hạn (#05).
- Danh sách email hoặc trạng thái mọi lời mời, hủy lời mời trước hạn, khóa tài khoản và quyền xem dữ liệu tài chính của người khác.
- Chốt nhà cung cấp, schema/API hoặc viết code ứng dụng ở bước đặc tả này.

## Further Notes

- Nguồn: [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [người dùng và quyền](../../docs/requirements/actors-and-roles.md), [luồng người dùng](../../docs/requirements/user-flows.md), [H02](../../docs/requirements/system-functions.md) và [đặc tả tổng thể](../capmoney-overall/spec.md).
- Ticket #03 còn phụ thuộc #01. R65 đã được Ninh xác nhận, nhưng phương án xác thực chỉ mời của #01 và các ràng buộc gửi lại đúng URL, gửi email/đồng thời vẫn phải được Tech chứng minh trước khi chuyển `ready-for-agent`.
