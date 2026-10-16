# Feature Spec: CM-25 – Đổi email tài khoản ghi chép

**BA gate hiện hành (16/10/2026):** `BA_READY` sau CapmmPM `/root` đối chiếu độc lập nguồn, BR/AC và 20 mục; xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md). Thiết kế kỹ thuật và `TECH_READY` còn chờ.

**Story:** [story.md](story.md) · **Nguồn chia lát:** [CM-25](../capmoney-overall/issues/25-doi-email-ghi-chep.md)  
**BA gate tại lúc lập bản nháp:** chưa review; được cập nhật bởi dòng gate hiện hành ở trên.

## 1. Executive Summary / User Story statement

Chủ tài khoản ghi chép đang đăng nhập được đổi email sau khi xác minh email mới. Phiên cũ kết thúc, người đó đăng nhập bằng email mới và thấy nguyên dữ liệu tài chính của mình. [Quyền bước 3](../../docs/requirements/actors-and-roles.md), [luồng](../../docs/requirements/user-flows.md).

## 2. Actor & Permission Matrix

| Actor | Quyền | Nguồn |
| --- | --- | --- |
| Chủ tài khoản ghi chép đang đăng nhập | Yêu cầu đổi email của chính mình | quyền bước 3 |
| Chủ email mới | Xác minh email trước khi đổi có hiệu lực | quyền bước 3 |
| Tài khoản khác; quản lý; khách | Không đổi email hoặc xem dữ liệu tài chính của chủ | R12; quyền bước 3 |

## 3. Screen Definition & Entry Points

Từ khu vực tài khoản ghi chép sau đăng nhập, mở đổi email và nhập email mới; xác minh qua email mới. **BA đề xuất:** trạng thái chờ xác minh và báo kết quả; bố cục/copy chưa được duyệt riêng. Người đã đăng xuất và mất email cũ dùng CM-26, không qua luồng tự đổi.

## 4. Main Flows (Happy Path)

1. Chủ đang đăng nhập yêu cầu đổi email của tài khoản mình.
2. Email mới được xác minh; trước xác minh, email cũ vẫn có hiệu lực.
3. Đổi có hiệu lực cho cùng tài khoản và dữ liệu; mọi phiên cũ kết thúc. Chủ đăng nhập lại bằng email mới và thấy nguyên ví/giao dịch của mình. [quyền bước 3; CM-25]

## 5. Alternative & Exceptional Flows

- Xác minh sai/hết hạn hoặc gửi email lỗi: chưa đổi email; không báo đã đổi thành công. Thời hạn/hình thức xác minh là thiết kế Tech/PM chưa chốt.
- Email mới đang thuộc tài khoản khác không được hợp nhất, chiếm quyền hoặc chuyển dữ liệu. Cách báo xung đột cần Tech/PM thiết kế kín.
- Phiên hết hiệu lực hoặc request từ tài khoản khác/quản lý/khách bị từ chối ở server. [R12]

## 6. Field-level Specifications & Validations

Email mới do chủ nhập rồi chứng minh quyền dùng; email hiện hành lấy từ phiên/dữ liệu server. Email quản lý riêng của Ninh và email ghi chép cá nhân Ninh là hai địa chỉ khác nhau. Cú pháp/chuẩn hóa/TTL xác minh do Tech đề xuất, PM rà. [quyền bước 3]

## 7. System Parameters & Constants (`PARAM.*`)

Không có TTL/giới hạn số lần đổi email được Ninh chốt. Tech cần chọn TTL và giới hạn an toàn, nêu nguồn và tác động trải nghiệm trước `TECH_READY`.

## 8. Business Rules (`BR-*`)

- `BR-25-01`: email mới chưa xác minh chưa thành email đăng nhập. **Ví dụ:** A nhập email mới nhưng chưa mở thư, vẫn đăng nhập bằng email cũ. [quyền bước 3]
- `BR-25-02`: đổi thành công kết thúc mọi phiên cũ và giữ dữ liệu của đúng tài khoản. **Ví dụ:** A trên điện thoại thứ hai bị từ chối yêu cầu tiếp; A đăng nhập email mới vẫn thấy ví của A. [quyền bước 3; CM-25]
- `BR-25-03`: đổi email không đổi chủ dữ liệu. **Ví dụ:** B không xem/sửa ví của A dù biết email cũ hoặc mới. [R12]

## 9. State Machine & Transition Rules (`STATE.*`)

`email cũ hiện hành → chờ xác minh email mới → email mới hiện hành`; lỗi/hết hạn giữ email cũ. Cùng tài khoản/chủ dữ liệu tiếp tục tồn tại sau chuyển; mọi phiên trước chuyển bị thu hồi. [CM-25; quyền bước 3]

## 10. Data Fallback & Offline Handling

Không đổi email offline. Khi gửi/hoàn tất chưa rõ, đọc lại email hiện hành của tài khoản sau khi có Internet; không báo thành công sớm. [R11; đề xuất an toàn]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.EMAIL_VERIFICATION_INVALID`, `ERR.EMAIL_IN_USE`, `ERR.SESSION_INVALID` là mã **đề xuất**. Không lộ chủ email khác hay dữ liệu tài chính qua lỗi; copy chưa chốt.

## 12. Security & Privacy Constraints

Kiểm chủ ở server; xác minh quyền dùng email mới trước khi thay email đăng nhập. Thu hồi mọi phiên cũ sau đổi và không chuyển ví/giao dịch sang tài khoản khác. Quản lý không có quyền xem dữ liệu hoặc tự đổi email ghi chép của người khác trong luồng này. [R12; quyền bước 3]

## 13. Third-party Integrations

Email xác minh qua SMTP Java theo [stack](../../docs/requirements/tech-stack-round-2.md); nhà cung cấp thật chưa chọn. Hợp đồng gửi/xác minh do Tech review.

## 14. Audit Log & History Requirements

Không có yêu cầu hiển thị lịch sử đổi email riêng. Lịch sử tài chính của chủ giữ nguyên sau đổi; log bảo mật tối thiểu là đề xuất Tech, không chứa token/dữ liệu không cần thiết.

## 15. NFR (Performance, Concurrency Bounds)

Khoảng 10 người, web iPhone/Android; chưa có SLA. Hai yêu cầu đổi email gần đồng thời không làm một tài khoản có hai email đăng nhập hiệu lực hoặc lẫn dữ liệu. [R05, R14, R12]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: chủ đang đăng nhập xác minh email mới trước khi email đăng nhập đổi có hiệu lực. [quyền bước 3]
- `AC2`: sau đổi, mọi phiên cũ kết thúc; email mới đăng nhập được và thấy nguyên dữ liệu A, email cũ không còn đăng nhập được. [CM-25]
- `AC3`: B/quản lý/khách không đổi email hoặc xem/sửa dữ liệu của A qua UI hay đường trực tiếp. [R12]
- `AC4`: xác minh sai/hết hạn hoặc email xung đột không đổi email và không hợp nhất dữ liệu/tài khoản. [điều kiện an toàn Tech/PM]

## 17. Figma Mapping / Visual References

Không áp dụng Figma; ảnh cũ chỉ tham khảo. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

CM-04 cấp tài khoản/phiên ghi chép; CM-08 có dữ liệu để kiểm giữ nguyên; CM-24 đặt lại mật khẩu tới email hiện hành; CM-26 là ngoại lệ người mất email đã đăng xuất. [CM-25](../capmoney-overall/issues/25-doi-email-ghi-chep.md)

## 19. Q&A Log & Resolved Decisions

**Ninh đã xác nhận:** quyền tự đổi khi đang đăng nhập, xác minh email mới, thu hồi phiên cũ, giữ riêng dữ liệu. **Chưa rõ:** phương thức/TTL xác minh, xử lý email xung đột và copy; xem [Q&A](qna.md). Nếu phương thức tạo thay đổi quyền/sản phẩm, hỏi Ninh trước gate.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA 20 mục/AC1–AC4; chờ PM review | [CM-25](../capmoney-overall/issues/25-doi-email-ghi-chep.md), [quyền](../../docs/requirements/actors-and-roles.md), [handoff](handoff.md) |
| 16/10/2026 | PM rà bản sau quyết định và chỉnh nguồn; `BA_READY` cho Story này, `TECH_READY` còn chờ. | [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) |
