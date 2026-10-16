# Feature Spec: CM-22 – Đặt lại mật khẩu quản lý

**BA gate hiện hành (16/10/2026):** `BA_READY` sau CapmmPM `/root` đối chiếu bản sau R74–R78 hoặc chỉnh nhãn nguồn, BR/AC và 20 mục; xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md). Thiết kế kỹ thuật và `TECH_READY` còn chờ.

**Story:** [story.md](story.md) · **Nguồn chia lát:** [CM-22](../capmoney-overall/issues/22-dat-lai-mat-khau-quan-ly.md)  
**BA gate tại lúc lập bản nháp:** chưa review; được cập nhật bởi dòng gate hiện hành ở trên.

## 1. Executive Summary / User Story statement

Ninh quên mật khẩu quản lý nhưng còn dùng được email quản lý đã xác minh thì tự nhận liên kết riêng có hạn 30 phút để đặt mật khẩu mới; mọi phiên quản lý cũ kết thúc. [R57–R58, R61; phiếu tiếp nhận](../../docs/requirements/00-intake.md).

## 2. Actor & Permission Matrix

| Actor | Quyền | Nguồn |
| --- | --- | --- |
| Ninh còn dùng email quản lý đã xác minh | Xin liên kết và đặt lại mật khẩu quản lý | R57–R58 |
| Người không sở hữu email, tài khoản ghi chép, khách | Không dùng liên kết sai/hết hạn hoặc can thiệp tài khoản quản lý | R12, R13; quyền bước 3 |

## 3. Screen Definition & Entry Points

Từ màn đăng nhập quản lý có đường xin đặt lại; liên kết email mở màn đặt mật khẩu; màn hết hạn có thao tác xin liên kết mới. **Ninh đã xác nhận** đường xin mới ngay trên màn hết hạn theo R61. Bố cục/copy và cách nhập email ở màn đầu là UX/Tech chưa được duyệt riêng; không cho đổi email đích từ màn hết hạn.

## 4. Main Flows (Happy Path)

1. Ninh yêu cầu đặt lại khi còn dùng email quản lý đã xác minh; hệ thống gửi liên kết riêng đúng tài khoản này.
2. Ninh mở liên kết trước mốc 30 phút, nhập mật khẩu mới và hoàn tất.
3. Hệ thống kết thúc mọi phiên quản lý cũ; Ninh đăng nhập lại bằng mật khẩu mới. [R57–R58]

## 5. Alternative & Exceptional Flows

- Liên kết sai hoặc hết hạn không đổi mật khẩu; **Tech/PM đề xuất an toàn:** liên kết đã dùng cũng bị từ chối. Nếu hết hạn, Ninh tự xin liên kết mới từ màn báo hết hạn; chỉ email quản lý đã xác minh nhận, không tạo quản lý khác. [R61]
- Liên kết đặt lại không dùng thay liên kết thiết lập quản lý 72 giờ của CM-01/CM-02. [R58, F23–F24]
- Gửi mail lỗi/chưa rõ không báo đã gửi thành công; yêu cầu lặp hoặc hai lần hoàn tất gần đồng thời không cho một liên kết đổi mật khẩu hai lần. Đây là điều kiện an toàn Tech/PM cần rà.

## 6. Field-level Specifications & Validations

Email đích là email quản lý đã xác minh, không phải email nhập tùy ý từ URL. Mật khẩu mới do Ninh đặt, không ghi log/lưu dạng đọc được; chính sách độ dài/bảo vệ là quyết định Tech cần review. Liên kết có mục đích đặt lại quản lý và mốc 30 phút kể từ lúc gửi [R57–R58]; dùng một lần là **Tech/PM đề xuất an toàn**, cần thiết kế/review.

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.MANAGER_RESET_TTL = 30 phút kể từ lúc gửi` là quyết định R58. Mốc “gửi” cần Tech xác định bằng sự kiện gửi được nhà cung cấp tiếp nhận, PM rà tác động. Giới hạn tần suất xin link, độ dài mật khẩu và lưu vết kỹ thuật chưa được Ninh chốt.

## 8. Business Rules (`BR-*`)

- `BR-22-01`: chỉ tài khoản quản lý đã xác minh email được tự đặt lại khi còn dùng email ấy. **Ví dụ:** link đến email quản lý hiện hành; email ghi chép cá nhân của Ninh không nhận link quản lý. [R57; quyền bước 3]
- `BR-22-02`: link riêng hết hạn ở mốc 30 phút. **Ví dụ:** mở trước mốc thì còn cơ hội hoàn tất; đúng/sau mốc bị từ chối và có đường xin mới. [R58, R61]
- `BR-22-03`: sau đặt lại thành công, mọi phiên quản lý cũ hết quyền. **Ví dụ:** điện thoại B đang mở khu vực quản lý bị từ chối ở yêu cầu bảo vệ tiếp theo; Ninh dùng mật khẩu mới để đăng nhập lại. [R57, CM-22]

## 9. State Machine & Transition Rules (`STATE.*`)

`chưa yêu cầu → link đặt lại còn hạn → đã dùng` hoặc `hết hạn`; link đã dùng/hết hạn không quay lại hợp lệ. Link mới từ màn hết hạn có mốc riêng, không làm sống lại link cũ. Khi mật khẩu đổi, epoch/phiên quản lý cũ bị thu hồi; cơ chế theo thiết kế CM-01/Tech. [R57–R58, R61]

## 10. Data Fallback & Offline Handling

Không xin/hoàn tất đặt lại offline. Email lỗi hay kết quả chưa rõ không được báo thành công sai. Nếu hoàn tất chưa rõ, chỉ xác nhận sau khi hệ thống kiểm trạng thái tài khoản/link, không lặp thao tác đổi mật khẩu thiếu kiểm soát. [R11; đề xuất an toàn]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.RESET_LINK_INVALID`, `ERR.RESET_LINK_EXPIRED`, `ERR.RESET_SEND_UNCONFIRMED` là mã **đề xuất**. Màn hết hạn phải có đường xin mới theo R61; câu chữ chi tiết chưa chốt. Không tiết lộ email quản lý/token qua lỗi.

## 12. Security & Privacy Constraints

Link đủ khó đoán, gắn đúng tài khoản/mục đích; **Tech/PM đề xuất dùng một lần**; không log token/mật khẩu. Server kiểm link và thu hồi phiên cũ ngay khi đổi mật khẩu, kể cả access JWT còn hạn. Không mở đường khôi phục khi Ninh đã đăng xuất và mất email quản lý. [R57; quyền bước 3, TS33]

## 13. Third-party Integrations

Email gửi qua hạ tầng SMTP đã chọn trong [TS10–TS38](../../docs/requirements/tech-stack-round-2.md); nhà cung cấp email thật chưa chọn. Kiểm bằng hộp thư thử trước; email thật khi có hạ tầng. [Q&A CM-01](../capmoney-manager-setup/qna.md)

## 14. Audit Log & History Requirements

Không có lịch sử sản phẩm riêng do Ninh chốt. **Tech đề xuất** ghi sự kiện phát hành/tiêu thụ/thu hồi phiên tối thiểu cho vận hành, không ghi link hay mật khẩu.

## 15. NFR (Performance, Concurrency Bounds)

Bản đầu khoảng 10 người, web iPhone/Android, không có SLA riêng. Hai lần hoàn tất gần nhau hoặc link cũ sau link mới phải cho kết quả nhất quán; thu hồi phiên cần có hiệu lực ở yêu cầu bảo vệ tiếp theo. [R05, R14, R57]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: Ninh còn dùng email quản lý đã xác minh nhận liên kết riêng có hạn 30 phút; email ghi chép khác không nhận link quản lý. [R57–R58]
- `AC2`: link sai/hết hạn không đổi mật khẩu; link đã dùng bị từ chối theo điều kiện an toàn Tech/PM đề xuất; ở màn hết hạn Ninh xin được link mới tới đúng email quản lý, không tạo tài khoản mới. [R61]
- `AC3`: đặt lại thành công bằng link còn hạn kết thúc mọi phiên quản lý cũ, kể cả điện thoại khác; mật khẩu mới đăng nhập được, mật khẩu cũ không. [R57; CM-22]
- `AC4`: link thiết lập CM-01/CM-02 không dùng như link đặt lại và ngược lại; yêu cầu lặp/đồng thời không đổi mật khẩu hai lần từ một link. [R58; điều kiện an toàn]

## 17. Figma Mapping / Visual References

Không áp dụng Figma; ảnh cũ chỉ là tham khảo. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

CM-01 cấp manager, đăng nhập và cơ chế thu hồi phiên; CM-02 là liên kết thiết lập riêng. CM-23 đổi email quản lý và cần bảo đảm link chỉ tới email hiện hành đã xác minh. [CM-22](../capmoney-overall/issues/22-dat-lai-mat-khau-quan-ly.md)

## 19. Q&A Log & Resolved Decisions

**Ninh đã xác nhận:** R57–R58/R61. **Chưa rõ:** câu chữ hết hạn và cách nhập email yêu cầu đầu; giới hạn gửi và mật khẩu là thiết kế Tech/PM. Không được suy luồng phục hồi khi mất email quản lý. [Q&A](qna.md)

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA 20 mục/AC1–AC4; chờ PM review | [CM-22](../capmoney-overall/issues/22-dat-lai-mat-khau-quan-ly.md), [R57–R61](../../docs/requirements/00-intake.md), [handoff](handoff.md) |
| 16/10/2026 | PM rà bản sau quyết định và chỉnh nguồn; `BA_READY` cho Story này, `TECH_READY` còn chờ. | [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) |
