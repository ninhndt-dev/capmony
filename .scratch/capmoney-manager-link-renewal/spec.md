# Đặc tả chi tiết bước 9: xin lại liên kết thiết lập quản lý hết hạn

**Rebaseline 16/10/2026:** Đặc tả hành vi này đã qua chuỗi rà cũ; chưa có `BA_READY`/`TECH_READY` theo workflow mới. Stack Java Spring/React/Vite đã chốt ở [TS10–TS38](../../docs/requirements/tech-stack-round-2.md); hợp đồng riêng của #02 vẫn cần thiết kế/review trước Dev.

**Thiết kế Java 16/10/2026:** [Bản nháp để review](technical-design-java.md) đối chiếu code CM-01 hiện hành; các lựa chọn chứng minh link hết hạn, quota và retry mail chưa được tính là quyết định sản phẩm/gate.

**Ticket:** [02 – Xin lại liên kết thiết lập quản lý hết hạn](../capmoney-overall/issues/02-xin-lai-lien-ket-quan-ly.md)
**Status:** needs-triage
**Trạng thái rà soát:** BA → PM → Tech → PM đã rà soát; không còn lỗi chặn về hành vi/tiêu chí. Tech còn chốt cách gửi, thay liên kết và giới hạn tần suất cùng #01 trước khi giao Dev; ticket vẫn `needs-triage`.

## Problem Statement

Nếu liên kết thiết lập quản lý đầu tiên hết hạn sau 3 ngày trước khi Ninh hoàn tất, Ninh vẫn cần cách nhận liên kết mới mà không tạo thêm quyền quản lý hoặc phải cho người lạ nhập email quản lý khác.

## Solution

Trên trang báo liên kết thiết lập đã hết hạn, Ninh yêu cầu gửi liên kết mới. Hệ thống chỉ gửi tới email quản lý đã khai trước khi mở hệ thống, làm liên kết cũ vô hiệu và cho Ninh hoàn tất thiết lập bằng liên kết mới. Khi tài khoản quản lý đã thiết lập xong, luồng này không cấp liên kết hay tài khoản quản lý nữa.

## User Stories

1. Là Ninh, tôi muốn biết liên kết thiết lập đã quá hạn để không thử đặt mật khẩu bằng liên kết không còn hiệu lực.
2. Là Ninh, tôi muốn tự yêu cầu liên kết mới ngay trên trang thiết lập để hoàn tất quyền quản lý ban đầu.
3. Là Ninh, tôi muốn liên kết mới chỉ đến email quản lý đã khai trước để không phải chọn lại người có quyền quản lý.
4. Là Ninh, tôi muốn liên kết cũ hết hiệu lực khi xin liên kết mới để không tồn tại hai đường thiết lập cùng lúc.
5. Là người không có quyền quản lý, tôi muốn không thể đổi email nhận liên kết hoặc dùng trang xin lại sau khi Ninh đã thiết lập xong để không tạo thêm quản lý.

## Implementation Decisions

- Luồng này chỉ áp dụng cho **liên kết thiết lập quản lý đầu tiên** đã hết hạn khi chưa hoàn tất; không phải liên kết đặt lại mật khẩu quản lý sau này. Liên kết thiết lập có hạn 3 ngày từ lúc gửi. [F23–F24]
- Trang hết hạn cho phép Ninh tự xin liên kết mới nhưng không có ô đổi email đích. Email nhận là email quản lý Ninh đã khai trước; liên kết cũ không còn dùng được. [F24]
- Liên kết mới phải tuân cùng quy tắc một lần, 3 ngày, xác minh email và tự đặt mật khẩu của ticket #01. Sau khi #01 hoàn tất, yêu cầu xin lại trong #02 không được cấp thêm liên kết hoặc tài khoản quản lý. [F11, F23–F24]
- Điều kiện thử liên kết hết hạn dùng môi trường khởi tạo riêng; không reset hoặc xóa dữ liệu người dùng thật. Cách gửi mail, lưu trạng thái liên kết và chống yêu cầu đồng thời do Tech chốt cùng [phương án kỹ thuật của #01](../capmoney-manager-setup/technical-options.md).
- Mốc hết hạn là 72 giờ từ lúc hệ thống ghi nhận gửi thành công: trước mốc còn hợp lệ; đúng và sau mốc bị từ chối. Một lần xin lại chỉ giữ tối đa một liên kết thiết lập hiện hành cho cùng email quản lý đã cấu hình; mọi liên kết cũ vô hiệu ngay cả khi email đến lệch thứ tự.
- Yêu cầu xin lại lặp hoặc gần đồng thời không tạo thêm quản lý hay để hai liên kết cùng có hiệu lực. Trang công khai cần giới hạn tần suất yêu cầu, không lộ email đích hoặc nội dung liên kết. Nếu gửi mail lỗi hoặc kết quả chưa rõ, không báo đã gửi thành công; người dùng có thể thử lại an toàn.

## Testing Decisions

- Dùng hộp thư thử và đồng hồ điều khiển được trong môi trường khởi tạo riêng; liên kết còn hợp lệ ngay trước 72 giờ, hết hiệu lực đúng mốc 72 giờ và sau mốc, rồi yêu cầu liên kết mới trên trang hết hạn.
- Kiểm tra liên kết mới chỉ tới email đã khai; liên kết cũ tiếp tục bị từ chối, liên kết mới hoàn tất được đúng một tài khoản quản lý. Thử truy cập trang xin lại bằng đường dẫn trực tiếp và không có cách nhập email quản lý khác.
- Sau khi quản lý đã hoàn tất thiết lập, yêu cầu xin lại không tạo mail liên kết thiết lập mới và không tạo thêm quản lý. Không dùng cách xóa tài khoản hoặc đặt lại dữ liệu thật để kiểm chứng.
- Thử hai lần xin lại gần đồng thời và mở email đến lệch thứ tự: chỉ liên kết hiện hành được hoàn tất. Thử lỗi gửi mail và kết quả gửi chưa rõ: không báo thành công sai, cho thử lại an toàn; yêu cầu lặp quá tần suất bị giới hạn nhưng không lộ email/token. Sau mọi ca, không có tài khoản quản lý thứ hai.
- Điểm kiểm thử là hành vi web, thư nhận được và quyền sau tải lại trên iPhone/Android theo quyết định kiểm thử bước 7; chưa chọn công cụ hoặc API cụ thể.

## Out of Scope

- Thiết lập liên kết đầu và đăng nhập (#01), đặt lại mật khẩu quản lý sau khi đã có tài khoản (#22), đổi email quản lý (#23), gửi lời mời người dùng (#03).
- Chọn schema/API hoặc công nghệ riêng cho #02 trước khi phương án xác thực chung của #01 được Tech kiểm chứng.

## Further Notes

- Nguồn: [F11/F23–F24](../../docs/requirements/user-flows.md), [H01](../../docs/requirements/system-functions.md), [đặc tả tổng thể](../capmoney-overall/spec.md) và ticket #02. Ninh đã xác nhận hành vi này ở bước 5; chưa có câu hỏi sản phẩm cần hỏi lại.
- Các ràng buộc gửi mail, thay liên kết hiện hành và giới hạn yêu cầu phải được Tech kiểm chứng cùng #01 trước khi #02 chuyển `ready-for-agent`; Tech còn chọn ngưỡng/cửa sổ giới hạn yêu cầu. Chưa chọn schema/API hoặc nhà cung cấp ngoài phương án đề xuất.
