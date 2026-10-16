# Feature Spec: CM-27 – Tự xóa tài khoản ghi chép

**Story:** [story.md](story.md) · **Nguồn chia lát:** [CM-27](../capmoney-overall/issues/27-tu-xoa-tai-khoan-ghi-chep.md)  
**BA gate:** bản BA bước 9; chưa được PM review, chưa có `BA_READY`.

## 1. Executive Summary / User Story statement

Chủ tài khoản ghi chép được tự xóa vĩnh viễn tài khoản và toàn bộ dữ liệu sau cảnh báo, nhập lại mật khẩu và xác nhận cuối. Quyền trên điện thoại khác bị chặn ngay; Ninh có thể mời lại cùng email để lập tài khoản mới trống. [F05, R63; luồng](../../docs/requirements/user-flows.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

| Actor | Quyền / giới hạn | Nguồn |
| --- | --- | --- |
| Chủ tài khoản ghi chép đang đăng nhập | Xem cảnh báo, nhập lại mật khẩu, xác nhận xóa **tài khoản của mình** | F05; quyền bước 3 |
| Tài khoản quản lý | Không dùng luồng tự xóa này, không đọc dữ liệu của chủ | F05, R12 |
| Tài khoản khác/khách | Không xóa hoặc đọc tài khoản của chủ | R12 |
| Ninh sau khi tài khoản đã xóa | Có thể mời lại cùng email, tạo tài khoản mới với dữ liệu trống | quyền bước 3 |

## 3. Screen Definition & Entry Points

Từ khu vực tài khoản ghi chép đã đăng nhập, mở tự xóa. Màn phải nêu rõ không khôi phục, yêu cầu nhập lại mật khẩu và xác nhận cuối; đây là **Ninh đã xác nhận** F05. **BA đề xuất:** tách nút mở luồng và xác nhận cuối để tránh chạm nhầm; copy/bố cục cụ thể chưa chốt.

## 4. Main Flows (Happy Path)

1. Chủ xem cảnh báo xóa vĩnh viễn, nhập lại mật khẩu đúng, thực hiện xác nhận cuối.
2. Hệ thống xóa ngay tài khoản cùng mọi ví, danh mục, khoản thu/chi, lần chuyển, kể cả giao dịch đã xóa mềm; không có hoàn tác. [F05; quyền bước 3]
3. Mọi yêu cầu đọc/sửa từ điện thoại khác bị từ chối từ thời điểm xóa. Khi tải lại, quay lại ứng dụng hoặc thao tác tiếp, điện thoại khác báo tài khoản đã xóa và đưa về đăng nhập. [R63]
4. Ninh có thể mời lại cùng email; người được mời lập tài khoản mới không có dữ liệu cũ. [quyền bước 3]

## 5. Alternative & Exceptional Flows

- Chưa nhập lại mật khẩu, mật khẩu sai hoặc chưa xác nhận cuối: không xóa gì. Phiên không hợp lệ cũng không được xóa. [F05]
- Xóa từng khoản thu/chi là xóa mềm theo R46; CM-27 xóa **toàn bộ tài khoản** vĩnh viễn, không biến các giao dịch cũ thành mục có thể khôi phục.
- Nếu một điện thoại khác gửi yêu cầu đọc/sửa gần thời điểm xóa, từ thời điểm xóa hiệu lực yêu cầu phải bị từ chối; Tech phải xác định ranh giới nguyên tử. Giao dịch đang chờ không được tạo lại dữ liệu sau xóa.
- Mất phản hồi sau xác nhận cuối: không được báo giữ tài khoản hoặc báo xóa chắc khi chưa đối chiếu; đường đăng nhập/phiên cũ không được lộ dữ liệu.

## 6. Field-level Specifications & Validations

Mật khẩu nhập lại thuộc chính tài khoản ghi chép hiện hành. Không cho trình duyệt chọn ID tài khoản khác để xóa. Dữ liệu cần xóa gồm ví, danh mục, giao dịch thu/chi và chuyển (còn hiệu lực/đã xóa mềm), cùng bản ghi quyền/phiên gắn tài khoản. Chi tiết lưu trữ do Tech kiểm kê; không giả định đã có mọi bảng dữ liệu. [F05, R63; CM-27]

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.UNDO_WINDOW = 0`: không có thời gian hoàn tác sau xác nhận cuối theo quyền bước 3. Không có thời gian chờ hay lịch giữ bản sao sản phẩm được chốt; bản đầu không yêu cầu backup khôi phục sau sự cố theo R52, nhưng điều này không giảm yêu cầu xóa dữ liệu tài khoản theo CM-27.

## 8. Business Rules (`BR-*`)

- `BR-27-01`: chỉ chủ đã xem cảnh báo, nhập lại mật khẩu và xác nhận cuối được xóa tài khoản mình. **Ví dụ:** nhập sai mật khẩu rồi bấm xác nhận thì ví/giao dịch vẫn còn. [F05]
- `BR-27-02`: sau xác nhận cuối, tài khoản và dữ liệu bị xóa vĩnh viễn, không hoàn tác. **Ví dụ:** khoản chi đã xóa mềm cũng không còn mục khôi phục. [quyền bước 3; R46]
- `BR-27-03`: phiên điện thoại khác mất quyền ngay; cùng email được mời lại là tài khoản trống. **Ví dụ:** B đang mở lịch sử của A, sau A xóa thì request tiếp của B bị từ chối; lời mời mới không hiện ví A cũ. [R63; quyền bước 3]

## 9. State Machine & Transition Rules (`STATE.*`)

`đang hoạt động → xem cảnh báo → xác minh lại mật khẩu → xác nhận cuối → đã xóa vĩnh viễn`. Bỏ dở/sai mật khẩu quay lại trạng thái hoạt động, không xóa. Sau trạng thái đã xóa không có phục hồi; lời mời cùng email tạo **tài khoản mới**, không hồi sinh ID/dữ liệu cũ. [F05, R63]

## 10. Data Fallback & Offline Handling

Không xóa offline. Nếu kết quả xác nhận cuối chưa rõ, client không được cho dùng phiên để đọc/sửa khi server đã xóa; khi có mạng đối chiếu và báo trạng thái. R52 không yêu cầu backup sau sự cố nặng, không phải cho phép giữ dữ liệu người dùng đã tự xóa trong ứng dụng. [R11, R52, R63]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.PASSWORD_RECHECK_FAILED`, `ERR.ACCOUNT_DELETED`, `ERR.DELETE_UNCONFIRMED` là mã **đề xuất**. Thông báo điện thoại khác phải có ý tài khoản đã xóa và đưa về đăng nhập theo R63; câu chữ/bố cục cụ thể chưa được Ninh duyệt.

## 12. Security & Privacy Constraints

Server kiểm đúng phiên/chủ và mật khẩu mới nhập, không tin ID hay trạng thái xác nhận từ client. Thu hồi mọi phiên; kiểm quyền ở mỗi request để token chưa hết hạn cũng không đọc/sửa sau xóa. Không để retry, job hoặc dữ liệu chờ khôi phục tạo lại tài khoản/dữ liệu. Tài khoản quản lý không có quyền tự xóa tài khoản ghi chép của người khác qua luồng này. [R12, R63, F05]

## 13. Third-party Integrations

Không áp dụng dịch vụ ngoài riêng cho thao tác xóa tài khoản theo yêu cầu hiện hành. Nếu sau này hệ thống giữ dữ liệu ở dịch vụ khác, Tech phải kiểm phạm vi xóa trước `TECH_READY`; không tự giả định có tích hợp đó.

## 14. Audit Log & History Requirements

Ninh đã chốt xóa toàn bộ dữ liệu tài khoản ngay, gồm lịch sử tài chính đã xóa mềm. Chưa có yêu cầu lưu nhật ký sản phẩm sau xóa; Tech phải rà log/backup/định danh phiên để không giữ dữ liệu trái nghĩa “toàn bộ dữ liệu”, rồi PM đối chiếu. [F05; quyền bước 3]

## 15. NFR (Performance, Concurrency Bounds)

Khoảng 10 người dùng, web iPhone/Android; chưa có SLA. “Ngay” ở R63 đòi mọi yêu cầu đọc/sửa **từ thời điểm xóa hiệu lực** bị chặn, kể cả trên điện thoại khác; cập nhật gần đồng thời không được làm dữ liệu xuất hiện lại. [R05, R14, R63]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: chỉ chủ ghi chép thấy cảnh báo không khôi phục, nhập lại mật khẩu đúng và xác nhận cuối mới xóa được; sai/bỏ dở giữ nguyên tài khoản. [F05]
- `AC2`: sau xác nhận cuối, tài khoản, ví, danh mục, giao dịch còn hiệu lực và đã xóa mềm không còn; không có hoàn tác. [CM-27; quyền bước 3]
- `AC3`: từ thời điểm xóa, điện thoại khác không đọc/sửa được; khi tải lại/quay lại/thao tác tiếp được báo tài khoản đã xóa và đưa về đăng nhập. [R63]
- `AC4`: quản lý/tài khoản khác không dùng luồng này để xóa hoặc xem dữ liệu; Ninh mời lại cùng email thì tài khoản mới trống, không nối dữ liệu cũ. [R12; quyền bước 3]

## 17. Figma Mapping / Visual References

Không áp dụng Figma; chưa có thiết kế được duyệt, ảnh cũ chỉ tham khảo. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

CM-07/#09 cấp ví, giao dịch; CM-12/#15 cấp dữ liệu đã xóa mềm và lần chuyển; CM-18/#20 cấp trạng thái ví và danh mục. CM-04/CM-03 cấp tài khoản và khả năng mời lại cùng email. [CM-27](../capmoney-overall/issues/27-tu-xoa-tai-khoan-ghi-chep.md)

## 19. Q&A Log & Resolved Decisions

**Ninh đã xác nhận:** F05, R63, quyền xóa vĩnh viễn và mời lại cùng email. **Chưa rõ:** copy cảnh báo/điện thoại khác và kiểm kê log/dữ liệu ngoài cơ sở chính là việc UX/Tech/PM; xem [Q&A](qna.md). Không thu hẹp “toàn bộ dữ liệu” khi chưa được Ninh duyệt.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA 20 mục/AC1–AC4; chờ PM review | [CM-27](../capmoney-overall/issues/27-tu-xoa-tai-khoan-ghi-chep.md), [F05/R63](../../docs/requirements/user-flows.md), [handoff](handoff.md) |
