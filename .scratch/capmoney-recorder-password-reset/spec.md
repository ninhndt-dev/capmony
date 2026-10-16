# Feature Spec: CM-24 – Đặt lại mật khẩu tài khoản ghi chép

**Story:** [story.md](story.md) · **Nguồn chia lát:** [CM-24](../capmoney-overall/issues/24-dat-lai-mat-khau-ghi-chep.md)  
**BA gate:** bản BA bước 9; chưa được PM review, chưa có `BA_READY`.

## 1. Executive Summary / User Story statement

Chủ tài khoản ghi chép còn dùng email đăng nhập tự đặt lại mật khẩu qua liên kết 30 phút; sau đó mọi phiên cũ kết thúc và dữ liệu tài chính của đúng tài khoản giữ nguyên. [R29, F04/F12; luồng](../../docs/requirements/user-flows.md).

## 2. Actor & Permission Matrix

| Actor | Quyền | Nguồn |
| --- | --- | --- |
| Chủ tài khoản ghi chép còn dùng email đã xác minh | Xin link, đặt lại mật khẩu của tài khoản mình | R29, F04/F12 |
| Tài khoản ghi chép khác; quản lý; khách | Không dùng link để xem/đổi tài khoản khác hoặc nhận quyền dữ liệu | R12; quyền bước 3 |

## 3. Screen Definition & Entry Points

Từ đăng nhập ghi chép có đường xin link; email mở form đặt mật khẩu mới. **BA đề xuất:** màn lỗi/hết hạn có hướng dẫn thử lại; đường xin mới cho ghi chép chưa được Ninh chốt cụ thể như R61 của quản lý. Bố cục/copy chưa chốt.

## 4. Main Flows (Happy Path)

1. Chủ còn dùng email yêu cầu link đặt lại gửi đúng email của tài khoản.
2. Trước mốc 30 phút, chủ mở link và đặt mật khẩu mới.
3. Mọi phiên ghi chép cũ kết thúc; đăng nhập bằng mật khẩu mới thấy nguyên ví, danh mục và giao dịch thuộc tài khoản đó. [R29, F04/F12]

## 5. Alternative & Exceptional Flows

- Link sai hoặc hết hạn không đổi mật khẩu; **Tech/PM đề xuất an toàn:** link đã dùng cũng bị từ chối. Trường hợp mất email cũ và đã đăng xuất chuyển qua CM-26 theo R62, không tự cho đổi email ở luồng này.
- Gửi mail lỗi hoặc kết quả chưa rõ không báo đã gửi; hoàn tất lặp/đồng thời không cho một link đổi mật khẩu hai lần. **Tech/PM đề xuất** điều kiện an toàn.
- Đặt lại ghi chép không ảnh hưởng manager hoặc tài khoản ghi chép khác, kể cả của chính Ninh. [R12; quyền bước 3]

## 6. Field-level Specifications & Validations

Email đích là email đăng nhập hiện hành đã xác minh của tài khoản ghi chép. Link có mục đích đặt lại tài khoản ghi chép [R29, F12]; dùng một lần là **Tech/PM đề xuất an toàn**, cần thiết kế/review. Mật khẩu mới do chủ đặt, không lưu dạng đọc được; chính sách mật khẩu/độ dài do Tech/PM duyệt.

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.RECORDER_RESET_TTL = 30 phút kể từ lúc gửi` theo F12. Mốc nhà cung cấp tiếp nhận gửi là diễn giải Tech cần PM rà. Giới hạn tần suất và chính sách mật khẩu chưa được Ninh chốt riêng.

## 8. Business Rules (`BR-*`)

- `BR-24-01`: link chỉ tới email tài khoản ghi chép của chủ. **Ví dụ:** A yêu cầu đặt lại thì B không nhận link có quyền đổi mật khẩu A. [R29, R12]
- `BR-24-02`: link quá 30 phút không đổi mật khẩu. **Ví dụ:** trước mốc 30 phút hoàn tất được nếu hợp lệ; đúng/sau mốc bị từ chối. [F12]
- `BR-24-03`: đặt lại thành công chấm dứt mọi phiên cũ nhưng giữ dữ liệu. **Ví dụ:** điện thoại thứ hai của A bị từ chối, A đăng nhập lại thấy cùng ví/số dư; B không đổi. [F04, R12]

## 9. State Machine & Transition Rules (`STATE.*`)

`chưa yêu cầu → link còn hạn → đã dùng` hoặc `hết hạn`; link hết hạn không trở lại hợp lệ theo R58/F12; link đã dùng không trở lại hợp lệ là **Tech/PM đề xuất an toàn**. Sau đổi mật khẩu, phiên trước thời điểm đổi bị thu hồi, tài khoản và dữ liệu giữ nguyên. [F04/F12]

## 10. Data Fallback & Offline Handling

Không xin/hoàn tất đặt lại offline. Gửi/hoàn tất chưa rõ không báo thành công sớm; server đối chiếu trạng thái trước khi cho kết luận. [R11; đề xuất an toàn]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.RECORDER_RESET_INVALID`, `ERR.RECORDER_RESET_EXPIRED`, `ERR.RECORDER_RESET_UNCONFIRMED` là mã **đề xuất**. Không dùng lỗi để xác nhận tài khoản/email của người khác. Câu chữ chưa chốt.

## 12. Security & Privacy Constraints

Link khó đoán, gắn đúng tài khoản/mục đích; **Tech/PM đề xuất dùng một lần**; không log mật khẩu/token. Thu hồi mọi phiên cũ ở server, cả điện thoại khác. Không cho link ghi chép đổi mật khẩu quản lý hay đọc dữ liệu người khác. [R12, F04]

## 13. Third-party Integrations

Gửi email qua SMTP Java theo [stack](../../docs/requirements/tech-stack-round-2.md); nhà cung cấp thật chưa chốt. Hộp thư thử dùng cho kiểm trước.

## 14. Audit Log & History Requirements

Không có lịch sử reset mật khẩu trên UI do Ninh chốt. Tech có thể ghi sự kiện bảo mật tối thiểu, không ghi link/mật khẩu hoặc dữ liệu tài chính.

## 15. NFR (Performance, Concurrency Bounds)

Khoảng 10 người, web iPhone/Android; không có SLA riêng. Reset đồng thời hoặc yêu cầu ở hai điện thoại không được để phiên cũ tiếp tục đọc dữ liệu sau khi đổi thành công. [R05, R14, F04]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: chủ còn dùng email nhận link đặt lại đúng tài khoản ghi chép, có hạn 30 phút. [R29, F12]
- `AC2`: link sai/hết hạn không đổi mật khẩu; link đã dùng bị từ chối theo điều kiện an toàn Tech/PM đề xuất; link ghi chép không đổi tài khoản quản lý. [F12, R12]
- `AC3`: reset thành công kết thúc mọi phiên ghi chép cũ; mật khẩu mới đăng nhập được, mật khẩu cũ không, dữ liệu tài chính giữ nguyên sau tải lại. [F04; CM-24]
- `AC4`: reset A không ảnh hưởng quyền/phiên/dữ liệu của B hoặc tài khoản quản lý. [R12]

## 17. Figma Mapping / Visual References

Không áp dụng Figma; ảnh cũ chỉ tham khảo. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

CM-04 cấp tài khoản/đăng nhập ghi chép; CM-08 cấp dữ liệu danh mục/giao dịch để kiểm giữ nguyên sau reset. CM-26 hỗ trợ người đã đăng xuất mất email; không mở đường đó ở CM-24. [CM-24](../capmoney-overall/issues/24-dat-lai-mat-khau-ghi-chep.md)

## 19. Q&A Log & Resolved Decisions

**Ninh đã xác nhận:** R29, F04, F12. **Chưa rõ:** copy/đường xin lại link ghi chép khi hết hạn, giới hạn gửi và chính sách mật khẩu. R61 chỉ áp dụng cho quản lý, không suy cho ghi chép; xem [Q&A](qna.md).

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA 20 mục/AC1–AC4; chờ PM review | [CM-24](../capmoney-overall/issues/24-dat-lai-mat-khau-ghi-chep.md), [F04/F12](../../docs/requirements/user-flows.md), [handoff](handoff.md) |
