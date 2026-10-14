# Bước 9: đặc tả chi tiết tạo ví đầu tiên

**Ticket:** [06 – Tạo ví đầu tiên](../capmoney-overall/issues/06-tao-vi-dau-tien.md)

**Status:** needs-info

**Rà soát:** BA → PM → Tech đã rà phạm vi ngày 14/10/2026; chờ Ninh chọn Q2 và PM rà bản cuối. Chưa giao Dev.

## Problem Statement

Người dùng mới có tài khoản ghi chép nhưng chưa có ví nên chưa thể bắt đầu ghi thu/chi. Họ cần tạo ví đầu tiên với số dư khởi đầu đúng, không mất phần đang nhập khi tạm chuyển trang trong cùng phiên và không thấy dữ liệu của người khác.

## Solution

Khi người dùng chưa có ví và đi tới luồng ghi thu/chi, CapMoney dẫn họ tạo ví trước. Người dùng nhập tên ví bắt buộc và có thể nhập số dư ban đầu; để trống số dư thì dùng 0 VND. Số dư ban đầu là số nguyên VND có thể âm. Chỉ khi Lưu thành công, ví mới xuất hiện với số dư hiện tại bằng số dư ban đầu và thuộc riêng tài khoản đó. Trước Lưu, rời trang rồi quay lại trong cùng phiên có Internet giữ nội dung đang nhập; đóng hẳn trình duyệt rồi mở lại thì biểu mẫu trống.

## User Stories

1. Là người dùng chưa có ví, tôi muốn được dẫn tạo ví trước khi ghi thu/chi để bắt đầu ghi chép.
2. Là người dùng, tôi muốn tự đặt tên ví khi tạo để nhận ra ví của mình.
3. Là người dùng, tôi muốn biết tên ví còn trống nên chưa thể Lưu để điền tên trước khi tạo.
4. Là người dùng, tôi muốn bỏ trống số dư ban đầu và nhận 0 VND để không phải nhập số khi chưa có số dư.
5. Là người dùng, tôi muốn nhập số dư ban đầu là số nguyên dương, 0 hoặc âm để phản ánh số tiền đang có hoặc đang nợ.
6. Là người dùng, tôi muốn số dư hiện tại của ví mới đúng bằng số dư ban đầu để bắt đầu ghi chép với mốc đúng.
7. Là người dùng, tôi muốn số dư ban đầu không xuất hiện như một khoản thu để lịch sử không có giao dịch giả.
8. Là người dùng, tôi muốn tạm rời biểu mẫu rồi quay lại trong cùng phiên có Internet và tiếp tục phần đang nhập mà chưa tạo ví.
9. Là người dùng, tôi muốn mở lại sau khi đóng hẳn trình duyệt trước Lưu với biểu mẫu trống và dữ liệu đã lưu trước đó vẫn còn.
10. Là chủ ví, tôi muốn chỉ tài khoản của mình đọc và sửa được ví đã tạo để dữ liệu tài chính được giữ riêng.
11. Là người dùng, tôi muốn bấm Lưu lặp hoặc thử lại cùng một thao tác mà không bị tạo ví trùng.

## Implementation Decisions

### Ninh đã xác nhận

- Người chưa có ví được dẫn tạo ví đầu tiên **trước khi ghi thu/chi**; F02 chưa chốt thời điểm chuyển màn ngay sau đăng nhập. [F02]
- Khi tạo ví đầu tiên hoặc ví thêm, người dùng **phải nhập tên ví trước khi Lưu**; không tự đặt tên nếu để trống. [R66]
- Số dư ban đầu có thể bỏ trống và dùng 0 VND; khi nhập thì phải là số nguyên VND, có thể âm. [R23, F13, B10, B15]
- Số dư ban đầu là mốc của ví, không phải khoản thu; trước giao dịch đầu tiên, số dư hiện tại bằng số dư ban đầu. [R30, B01]
- Chỉ chủ tài khoản được xem/sửa dữ liệu ví. Bản mới bắt đầu không có dữ liệu cũ. [R03, R12]
- Trước khi Lưu thành công, rời trang rồi quay lại trong cùng phiên có Internet giữ bản nhập dở nhưng chưa tạo ví; đóng hẳn trình duyệt trước Lưu thì mở lại biểu mẫu trống. [F18, F21]

### Tech đề xuất, cần kiểm chứng trước khi giao Dev

- Mọi đường tạo/đọc ví lấy chủ sở hữu từ phiên ghi chép đã xác thực; xác thực và phân quyền ở phía hệ thống, không tin thông tin chủ ví do trình duyệt gửi. Phụ thuộc #04.
- Một thao tác Lưu lặp hoặc thử lại chỉ tạo một ví; hai thao tác tạo ví độc lập vẫn có thể tạo hai ví theo R15. Không báo Lưu thành công nếu chưa biết kết quả. Đọc lại kết quả của đúng thao tác trước khi cho thử lại để tránh trùng.
- Xử lý số dư bằng số nguyên VND chính xác ở cả nhập, kiểm tra và lưu; từ chối phần lẻ hoặc giá trị không phải số nguyên, không làm tròn thầm. Không tự đặt giới hạn số dư sản phẩm chưa được chốt.
- Bản nháp chỉ giữ trong phiên ứng dụng đang mở, tách khỏi trạng thái thao tác Lưu đã gửi. Cách hiện và lưu trạng thái phải qua kiểm thử trên Safari iPhone và Chrome Android.
- [Đối chiếu kỹ thuật](technical-options.md) ghi các điểm cần chứng minh. Chưa chốt framework, schema hay API.

### Chưa rõ

- Quy tắc tên trùng, giới hạn độ dài, chuẩn hóa khoảng trắng và câu chữ báo lỗi chưa được Ninh chốt riêng; không suy ra tên ví phải duy nhất từ R66.
- Ranh giới F21/F25 đối với **ví đã bấm Lưu rồi đóng trình duyệt khi kết quả chưa rõ** đã được BA/PM đối chiếu: Ninh chưa chốt hành vi này cho ví; [Q2](../../docs/requirements/40-step-9-first-wallet-round-2.md) đang chờ Ninh chọn. Không tự coi đó là bản nháp chưa bấm Lưu hoặc tự khôi phục bản nháp ấy.

## Testing Decisions

- Trên web điện thoại iPhone và Android, từ tài khoản ghi chép mới chưa có ví, vào ghi thu/chi được dẫn tạo ví; trước khi có ví không lưu được thu/chi. Không khẳng định màn tạo ví bật ngay sau đăng nhập.
- Lưu ví có tên với số dư trống, 0, số nguyên dương và âm; đọc lại sau tải trang thấy số dư đầu và hiện tại đúng. Tên trống, số dư phần lẻ hoặc chữ không được lưu. Lịch sử không có khoản thu tương ứng số dư đầu.
- Chuyển trang trước Lưu rồi quay lại cùng phiên có Internet thấy tên/số dư đang nhập nhưng chưa có ví đã lưu; đóng hẳn trình duyệt trước Lưu rồi mở lại cùng tài khoản thì biểu mẫu trống.
- Thử hai tài khoản ghi chép và tài khoản quản lý: chỉ chủ ví đọc/sửa được ví của mình, kể cả gọi đường tạo/đọc trực tiếp và sau tải lại trang.
- Bấm Lưu lặp, hai yêu cầu gần đồng thời cùng thao tác, hoặc mất phản hồi: cuối cùng chỉ có một ví cho thao tác đó; hai thao tác độc lập vẫn được phép. Không báo thành công khi kết quả chưa rõ.

## Out of Scope

- Tạo ví thứ hai trở đi (#07), danh mục và ghi giao dịch (#08–#09), sửa tên/số dư ban đầu hoặc ngừng/xóa ví (#16–#19).
- Nhập lịch sử từ ứng dụng cũ, ghi khi không có Internet, hoặc chốt công nghệ/schema/API ở lượt đặc tả này.

## Further Notes

- Nguồn: [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [vòng tên ví R66](../../docs/requirements/39-step-9-first-wallet-round-1.md), [nghiệp vụ chính](../../docs/requirements/core-business.md), [luồng người dùng](../../docs/requirements/user-flows.md), [đặc tả tổng thể](../capmoney-overall/spec.md) và ticket #06.
- #06 phụ thuộc #04. Chưa chuyển `ready-for-agent` trước khi kiểm chứng xác thực/quyền, tiền chính xác, tạo một lần và ranh giới F21/F25.
