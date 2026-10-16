# Bước 9: đặc tả chi tiết mã sai và lời mời hết hạn

**Rebaseline 16/10/2026:** Đặc tả hành vi này đã qua chuỗi rà cũ; chưa có `BA_READY`/`TECH_READY` theo workflow mới. Stack Java Spring/React/Vite đã chốt ở [TS10–TS38](../../docs/requirements/tech-stack-round-2.md); [technical-options](technical-options.md) chỉ là khảo sát lịch sử.

**Ticket:** [05 – Xử lý mã sai hoặc lời mời hết hạn](../capmoney-overall/issues/05-loi-moi-het-han-va-ma-sai.md)

**Status:** needs-triage

**Rà soát:** BA → PM → Tech → PM đã rà ngày 14/10/2026. PM xác nhận AC và ca kiểm thử phục hồi sau lỗi gửi mã; ticket vẫn `needs-triage` để spike, chưa giao Dev.

## Problem Statement

Người được Ninh mời có thể nhập sai mã hoặc để mã/lời mời hết hạn trước khi hoàn tất tài khoản. Họ cần đường tiếp tục đúng quyền mà không mở thêm thời gian của lời mời cũ hoặc tự tạo lời mời mới.

## Solution

Khi lời mời vẫn còn hạn, người nhận nhập sai hoặc mã hết hạn có thể xin mã mới gửi tới đúng email được mời. Mã trước mất hiệu lực, còn hạn 3 ngày của lời mời giữ nguyên. Nếu lời mời đã hết hạn, kể cả trong lúc đang nhập mã, không thể hoàn tất; ứng dụng hướng dẫn người nhận liên hệ Ninh. Chỉ Ninh dùng tài khoản quản lý mời mới tới cùng email sau khi lời mời cũ hết hạn.

## User Stories

1. Là người được mời, tôi muốn biết mã sai không được chấp nhận để không tưởng đã chứng minh quyền dùng email.
2. Là người được mời có lời mời còn hạn, tôi muốn xin mã mới khi mã sai hoặc hết hạn để tiếp tục lập tài khoản.
3. Là người được mời, tôi muốn mã mới chỉ gửi tới email của lời mời để người khác không đổi người nhận.
4. Là người được mời, tôi muốn mã cũ hết hiệu lực sau khi mã mới được cấp để không có hai mã cùng dùng được.
5. Là Ninh, tôi muốn việc xin mã mới không kéo dài hạn lời mời 3 ngày để quyền mời vẫn theo thời hạn đã chọn.
6. Là người mở lời mời đã hết hạn, tôi muốn biết cần liên hệ Ninh để được mời mới.
7. Là người đang nhập mã khi lời mời hết hạn, tôi muốn ứng dụng từ chối hoàn tất bằng lời mời cũ và hướng dẫn bước tiếp theo.
8. Là Ninh, tôi muốn mời mới cùng email sau khi lời mời cũ hết hạn, còn liên kết/mã cũ vẫn không dùng lại được.
9. Là người không có quyền quản lý, tôi muốn không thể tự gửi lời mời mới khi lời mời của mình hết hạn.

## Implementation Decisions

### Ninh đã xác nhận

- Lời mời người dùng hết hạn sau 3 ngày. Mở lời mời quá hạn thì báo hết hạn, hướng dẫn liên hệ Ninh; chỉ Ninh gửi lời mời mới tới cùng email sau hạn. [R41; quyền bước 3; F03]
- Khi lời mời còn hạn nhưng mã sai hoặc hết hạn, người được mời được xin mã mới tới **đúng email được mời**; mã cũ hết hiệu lực, hạn 3 ngày của lời mời không đổi. [F07]
- Nếu lời mời hết hạn trong lúc nhập mã, không hoàn tất bằng lời mời cũ; chỉ Ninh mời mới. Việc đã nhập mã hoặc xin mã mới trước đó không kéo dài hạn. [F01, F26]
- R65 cho phép Ninh gửi lại **cùng liên kết chưa dùng, còn hạn** khi người nhận không thấy email; đây là #03, khác việc mời mới **sau hạn** trong #05. [R65]

### Tech đề xuất, cần kiểm chứng trước khi giao Dev

- Khi cấp mã mới cho một lời mời còn hạn, mã cũ không còn dùng được; mã mới gắn với đúng lời mời/email và không kéo dài thời điểm hết hạn của lời mời. Mã của lời mời cũ không hợp lệ cho lời mời mới tới cùng email.
- Khi cấp mã và lúc hoàn tất tài khoản, kiểm lại hạn lời mời. Trước mốc 72 giờ từ lần gửi đầu được nhà cung cấp tiếp nhận còn hợp lệ; đúng và sau mốc bị từ chối, kể cả khi liên kết được mở hoặc mã từng đúng trước đó. Lời mời mới sau hạn có thời hạn riêng, không hồi sinh liên kết/mã cũ.
- Chỉ nhập sai một mã chưa tự vô hiệu mã vẫn còn hạn; Tech chọn giới hạn lần thử an toàn và đường xin mã mới. Kiểm mã đúng riêng lẻ chưa hoàn tất tài khoản hoặc tiêu thụ lời mời. Nếu xin mã mới và hoàn tất tài khoản xảy ra đồng thời, kết quả phải nhất quán theo thứ tự được hệ thống ghi nhận.
- Chống yêu cầu xin mã mới lặp/gần đồng thời, lỗi hoặc timeout gửi email; không báo mã mới đã gửi thành công nếu kết quả chưa rõ. Cách lưu và giới hạn mã, thời gian chờ do Tech chọn rồi PM rà.
- [Đối chiếu kỹ thuật](technical-options.md) ghi điều kiện xác thực/mã cần chứng minh. Chưa chọn framework, schema hoặc API.

### Chưa rõ ở mức thiết kế/Tech

- Thời hạn từng mã, số lần nhập sai, nội dung thông báo và thời gian chờ xin lại mã chưa được Ninh chọn riêng. Tech không được dùng mặc định thư viện nếu nó đổi F07/F26; chỉ hỏi Ninh bằng A/B trên giao diện nếu phát sinh lựa chọn sản phẩm mới.

## Testing Decisions

- Dùng hộp thư thử và đồng hồ điều khiển được: trước mốc 72 giờ, nhập mã sai bị từ chối nhưng có thể xin mã mới; mã mới đến đúng email, dùng được với lời mời hiện hành, mã trước bị từ chối. Mã hết hạn cũng có đường xin mã mới khi lời mời còn hạn. Thử nhập sai một lần rồi nhập đúng mã còn hạn theo giới hạn Tech chọn.
- Kiểm lời mời vừa trước, đúng và sau mốc 72 giờ từ lần gửi đầu. Nếu hết hạn khi đang nhập hoặc xác nhận mã, không tạo tài khoản; hiện trạng thái hết hạn và hướng dẫn liên hệ Ninh. Mã từng đúng trước mốc không làm lời mời còn hiệu lực sau mốc.
- Ninh đăng nhập quản lý gửi lời mời mới tới cùng email sau hạn: liên kết và mã của lời mời cũ tiếp tục bị từ chối; lời mời mới có mốc hạn riêng. Tài khoản ghi chép hoặc người chưa đăng nhập không thể tự mời mới.
- Gửi lại cùng liên kết còn hạn theo R65 không gia hạn lời mời; kiểm lần gửi lại ở #03. Đường mã đúng và hoàn tất một tài khoản ở #04; #05 kiểm các nhánh từ chối/phục hồi nêu trên.
- Thử xin mã mới lặp/gần đồng thời, xin mã mới trong lúc hoàn tất tài khoản, và gửi mail lỗi/chưa rõ: không để hai mã cùng hợp lệ, hai tài khoản được tạo hoặc báo thành công sai. Sau lỗi gửi mã, người nhận vẫn có đường thử xin mã mới an toàn khi lời mời còn hạn; không bị khóa đến hết hạn vì mã cũ đã vô hiệu. Điểm kiểm thử phát hành là web trên iPhone và Android theo S05.

## Out of Scope

- Phát hành lời mời lần đầu hoặc gửi lại cùng liên kết còn hạn (#03); đường mã đúng, đặt mật khẩu, đăng nhập và dữ liệu mới trống (#04).
- Hủy lời mời trước hạn, cho người nhận tự mời mới, hoặc gia hạn lời mời/mã để vượt quá mốc 3 ngày.
- Chốt công nghệ, schema/API hoặc viết code trước khi Tech/PM hoàn tất rà soát.

## Further Notes

- Nguồn: [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [người dùng và quyền](../../docs/requirements/actors-and-roles.md), [luồng F01/F03/F07/F26](../../docs/requirements/user-flows.md), [H02](../../docs/requirements/system-functions.md), [đặc tả tổng thể](../capmoney-overall/spec.md) và ticket #05.
- Ticket #05 phụ thuộc #03/#04. Còn phải chứng minh vòng đời mã, mốc hết hạn và sự nhất quán khi gửi lại/mời mới trước khi chuyển `ready-for-agent`.
