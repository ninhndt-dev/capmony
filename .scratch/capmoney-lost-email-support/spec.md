# Feature Spec: CM-26 – Hỗ trợ người mất email

**Story:** [story.md](story.md) · **Nguồn chia lát:** [CM-26](../capmoney-overall/issues/26-ho-tro-nguoi-mat-email.md)  
**BA gate:** bản BA bước 9; chưa được PM review. Cách ghi bằng chứng Ninh đã gọi lại/nhận diện còn mở; chưa có `BA_READY`.

## 1. Executive Summary / User Story statement

Người dùng đã đăng xuất, mất email cũ có thể nhờ Ninh hỗ trợ đổi email đăng nhập. Ninh chỉ tra đúng email cũ theo yêu cầu, gọi lại số đã lưu trước ngoài CapMoney, nhận diện chắc người quen và xác nhận chính họ yêu cầu; email mới phải được xác minh trước khi đổi có hiệu lực. Ninh không xem dữ liệu tài chính. [R62, F10; phiếu tiếp nhận](../../docs/requirements/00-intake.md), [luồng](../../docs/requirements/user-flows.md).

## 2. Actor & Permission Matrix

| Actor | Quyền / giới hạn | Nguồn |
| --- | --- | --- |
| Người dùng đã đăng xuất, mất email cũ | Yêu cầu Ninh hỗ trợ; xác minh quyền dùng email mới | R62, F10 |
| Ninh với phiên quản lý hợp lệ | Tra đúng email cũ đã được cung cấp; chỉ đổi sau xác minh độc lập và email mới | R62; quyền bước 3 |
| Tài khoản quản lý | Không có danh sách toàn bộ người dùng và không đọc dữ liệu tài chính | R12; quyền bước 3 |
| Người lạ/tài khoản ghi chép khác | Không đổi email hoặc chiếm tài khoản | R12, R62 |

## 3. Screen Definition & Entry Points

Người dùng liên hệ Ninh ngoài ứng dụng. Trong khu vực quản lý, Ninh nhập **đúng email cũ người yêu cầu cung cấp**, thực hiện cuộc gọi lại ngoài CapMoney tới số đã lưu từ trước và chỉ sau khi nhận diện chắc mới đi tiếp tới xác minh email mới. **BA đề xuất:** giao diện chỉ cho tra cứu đích danh, hiện bước xác nhận điều kiện hỗ trợ; cách ghi bằng chứng cuộc gọi/nhận diện là câu hỏi chặn tại [Q&A](qna.md), chưa tự chốt.

## 4. Main Flows (Happy Path)

1. Người dùng đã đăng xuất và mất email cũ yêu cầu Ninh hỗ trợ, cung cấp email cũ.
2. Ninh đăng nhập quản lý, tra đúng email cũ, gọi lại số Ninh đã lưu **từ trước** ngoài ứng dụng; nhận diện chắc người quen và xác nhận chính người đó yêu cầu đổi. [R62]
3. Email mới được người đó xác minh; chỉ sau đó email đăng nhập của cùng tài khoản đổi, các phiên cũ kết thúc.
4. Người dùng đăng nhập bằng email mới và thấy nguyên dữ liệu riêng; Ninh không xem dữ liệu tài chính. [F10; quyền bước 3]

## 5. Alternative & Exceptional Flows

- Không có số đã biết trước, không gọi được hoặc Ninh không chắc đúng người: **chưa đổi email**; không dùng email cũ đã mất làm kênh xác minh thay thế. [R62]
- Email mới chưa xác minh, xác minh sai/hết hạn hoặc xung đột tài khoản: chưa đổi; không gộp dữ liệu hay chuyển quyền. [F10; điều kiện an toàn]
- Không có đường duyệt danh sách toàn bộ tài khoản, không có tìm kiếm gần đúng phục vụ hỗ trợ. Nếu chính Ninh mất email quản lý sau khi đăng xuất, bản đầu chưa có luồng phục hồi quyền quản lý. [quyền bước 3]
- Kết quả đổi chưa rõ không báo thành công; Tech cần thiết kế kiểm lại trạng thái trước khi người dùng thử đăng nhập.

## 6. Field-level Specifications & Validations

Email cũ do người yêu cầu cung cấp để tra đúng một tài khoản; email mới phải được xác minh. Số điện thoại xác minh là số Ninh giữ **ngoài CapMoney từ trước**, không phải số mới do người yêu cầu khai trong lần hỗ trợ. Cách ghi minh chứng gọi lại/nhận diện chưa được Ninh chốt. Hệ thống không cần lưu danh bạ điện thoại để thực hiện R62 nếu quy trình bằng chứng được xác nhận. [R62]

## 7. System Parameters & Constants (`PARAM.*`)

Không có TTL email xác minh hoặc giới hạn số lần hỗ trợ được Ninh chốt. Tech/PM chọn giới hạn an toàn và cách xác minh email mới; nếu ảnh hưởng quyền/hành vi, BA hỏi Ninh. Không đặt `PARAM` sản phẩm giả.

## 8. Business Rules (`BR-*`)

- `BR-26-01`: hỗ trợ chỉ theo yêu cầu người mất email đã đăng xuất và chỉ tra đúng email cũ họ cung cấp. **Ví dụ:** người A nêu email cũ A; Ninh không xem danh sách B/C để chọn người. [F10; quyền bước 3]
- `BR-26-02`: Ninh phải gọi lại số đã lưu từ trước ngoài CapMoney và chắc đúng người. **Ví dụ:** người yêu cầu chỉ đưa số mới tại lúc hỗ trợ thì chưa đủ điều kiện đổi. [R62]
- `BR-26-03`: email mới được xác minh trước khi đổi; đổi thành công giữ dữ liệu riêng và kết thúc phiên cũ. **Ví dụ:** A đăng nhập email mới thấy ví A, điện thoại cũ của A bị từ chối, quản lý không thấy ví A. [F10; quyền bước 3]

## 9. State Machine & Transition Rules (`STATE.*`)

`yêu cầu ngoài ứng dụng → Ninh tra đúng email cũ → đã nhận diện chắc qua gọi lại → email mới đã xác minh → email đổi có hiệu lực`. Thiếu bất kỳ điều kiện nào thì tài khoản giữ email cũ. Sau đổi, phiên cũ bị thu hồi; dữ liệu vẫn thuộc cùng tài khoản. [R62, F10]

## 10. Data Fallback & Offline Handling

Cuộc gọi xác minh diễn ra ngoài ứng dụng, nhưng tra cứu/xác minh email mới/đổi tài khoản yêu cầu Internet. Nếu hệ thống hoặc email lỗi, giữ email cũ và trạng thái quyền cũ; không báo đổi thành công khi chưa xác nhận. [R11; F10]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.IDENTITY_NOT_CONFIRMED`, `ERR.NEW_EMAIL_UNVERIFIED`, `ERR.ACCOUNT_UNAVAILABLE` là mã **đề xuất**. Copy và cách báo cho Ninh/người yêu cầu chưa chốt; không lộ danh sách người dùng, dữ liệu tài chính hay số điện thoại đã lưu ngoài ứng dụng.

## 12. Security & Privacy Constraints

Chỉ phiên quản lý hợp lệ gọi đường hỗ trợ; không có quyền đọc ví, danh mục, giao dịch. Không tin số mới hoặc xác nhận từ người đang yêu cầu thay cho cuộc gọi lại số cũ đã biết. Email mới phải xác minh và không được thuộc tài khoản khác; thu hồi phiên cũ khi đổi. **Điểm chưa rõ:** dạng bằng chứng/ghi nhận Ninh đã làm bước ngoài hệ thống. [R12, R62]

## 13. Third-party Integrations

Cuộc gọi tới số đã biết là kênh **ngoài CapMoney** do Ninh thực hiện; không có tích hợp điện thoại được xác nhận. Email xác minh dùng SMTP Java theo [stack](../../docs/requirements/tech-stack-round-2.md); nhà cung cấp thật chưa chọn.

## 14. Audit Log & History Requirements

Ninh chưa chốt cách ghi bằng chứng gọi lại/nhận diện; đây là quyết định cần làm rõ trước BA gate. **Tech đề xuất:** chỉ lưu dấu vết tối thiểu đủ chứng minh thao tác đổi email, tránh ghi âm hoặc lưu số ngoài ứng dụng khi chưa được duyệt. Không log token/email mới chưa cần thiết.

## 15. NFR (Performance, Concurrency Bounds)

Khoảng 10 người, web iPhone/Android; chưa có SLA. Hai yêu cầu hỗ trợ cho cùng tài khoản gần nhau không được để hai email mới cùng có hiệu lực hoặc phiên cũ tiếp tục đọc dữ liệu sau đổi. [R05, R14, F10]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: quản lý chỉ tra đúng email cũ do người mất email cung cấp; không xem danh sách toàn bộ tài khoản hoặc dữ liệu tài chính của người đó. [F10; quyền bước 3]
- `AC2`: khi không có số Ninh đã biết trước ngoài CapMoney hoặc Ninh không chắc đúng người qua gọi lại, email đăng nhập không đổi. [R62]
- `AC3`: chỉ sau Ninh xác nhận đúng người/yêu cầu và email mới được xác minh thì đổi có hiệu lực; phiên cũ kết thúc, đăng nhập email mới thấy nguyên dữ liệu riêng, quản lý không thấy dữ liệu ấy. [R62, F10]
- `AC4`: khách/tài khoản ghi chép khác không thực hiện luồng hỗ trợ; email mới chưa xác minh hoặc xung đột không chiếm/chuyển tài khoản. [R12; điều kiện an toàn]

## 17. Figma Mapping / Visual References

Không áp dụng Figma; giao diện hỗ trợ chưa được duyệt và ảnh cũ chỉ tham khảo. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

CM-01 cấp phiên quản lý; CM-04 cấp tài khoản ghi chép; CM-08 cấp dữ liệu để kiểm quyền/giữ nguyên. CM-25 là đường tự đổi khi **còn đăng nhập**, khác CM-26. [CM-26](../capmoney-overall/issues/26-ho-tro-nguoi-mat-email.md)

## 19. Q&A Log & Resolved Decisions

**Ninh đã xác nhận:** R62, F10 và quyền quản lý giới hạn. **Câu hỏi sản phẩm chặn:** cách biểu thị bằng chứng Ninh đã gọi lại số có trước, nhận diện và xác nhận yêu cầu; xem [Q&A](qna.md). Phương thức xác minh email mới/copy UI là thiết kế cần review, không được thay điều kiện R62.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA 20 mục/AC1–AC4; giữ câu hỏi bằng chứng chưa chốt, chờ PM review | [CM-26](../capmoney-overall/issues/26-ho-tro-nguoi-mat-email.md), [R62](../../docs/requirements/00-intake.md), [handoff](handoff.md) |
