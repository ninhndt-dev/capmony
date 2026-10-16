# Feature Spec: CM-23 – Đổi email quản lý

**BA gate hiện hành (16/10/2026):** `BA_READY` sau CapmmPM `/root` đối chiếu độc lập nguồn, BR/AC và 20 mục; xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md). Thiết kế kỹ thuật và `TECH_READY` còn chờ.

**Story:** [story.md](story.md) · **Nguồn chia lát:** [CM-23](../capmoney-overall/issues/23-doi-email-quan-ly.md)  
**BA gate tại lúc lập bản nháp:** chưa review; được cập nhật bởi dòng gate hiện hành ở trên.

## 1. Executive Summary / User Story statement

Ninh đang đăng nhập quản lý được đổi email quản lý sau khi chứng minh quyền dùng email mới. Khi đổi có hiệu lực, các phiên quản lý cũ kết thúc; quyền quản lý không chuyển sang tài khoản ghi chép cá nhân. [Quyền bước 3](../../docs/requirements/actors-and-roles.md), [CM-23](../capmoney-overall/issues/23-doi-email-quan-ly.md).

## 2. Actor & Permission Matrix

| Actor | Quyền | Nguồn |
| --- | --- | --- |
| Ninh với phiên quản lý hợp lệ | Yêu cầu và hoàn tất đổi email quản lý | quyền bước 3 |
| Người sở hữu email mới | Phải chứng minh quyền dùng email mới trước khi đổi có hiệu lực | quyền bước 3 |
| Tài khoản ghi chép; khách | Không đổi email quản lý hoặc nhận quyền quản lý | R12; quyền bước 3 |

## 3. Screen Definition & Entry Points

Từ khu vực quản lý sau đăng nhập, Ninh nhập email mới và làm bước xác minh qua email mới. **BA đề xuất:** hiển thị trạng thái đang chờ xác minh và kết quả; bố cục, nội dung mail và copy chưa được duyệt riêng. Không có đường đổi email quản lý cho người đã đăng xuất/mất email quản lý. [quyền bước 3]

## 4. Main Flows (Happy Path)

1. Ninh đang đăng nhập quản lý gửi yêu cầu đổi sang email mới.
2. Hệ thống gửi bước xác minh tới email mới; trước khi xác minh, email quản lý cũ vẫn có hiệu lực.
3. Sau xác minh hợp lệ, email mới thay email đăng nhập của cùng tài khoản quản lý, mọi phiên cũ kết thúc; Ninh đăng nhập lại bằng email mới. [CM-23; quyền bước 3]

## 5. Alternative & Exceptional Flows

- Không xác minh được email mới hoặc link/mã sai, hết hạn thì email cũ giữ nguyên. Thời hạn và hình thức xác minh chưa được Ninh chốt; Tech/PM phải thiết kế, không tự dùng mặc định thư viện.
- Email mới đã thuộc tài khoản ghi chép hoặc tài khoản khác không được gộp/chuyển quyền quản lý; cần Tech/PM định nghĩa thông báo và chặn xung đột.
- Phiên quản lý hết hiệu lực trước khi hoàn tất phải bị từ chối hoặc yêu cầu xác thực lại theo thiết kế bảo mật; không đổi email từ khách hay tài khoản ghi chép.

## 6. Field-level Specifications & Validations

Email mới là địa chỉ Ninh nhập và chứng minh quyền dùng; email cũ là email quản lý hiện hành, không do trình duyệt quyết định từ request. Email quản lý và email tài khoản ghi chép cá nhân Ninh phải khác nhau. Chính sách cú pháp/chữ hoa thường/chuẩn hóa email là thiết kế Tech cần PM rà. [Quyền bước 3]

## 7. System Parameters & Constants (`PARAM.*`)

Không có TTL hay giới hạn số lần đổi được Ninh chốt. Tech chọn TTL và giới hạn an toàn cho bước xác minh, PM rà tác động; không ghi thành yêu cầu Ninh đã xác nhận.

## 8. Business Rules (`BR-*`)

- `BR-23-01`: email mới phải xác minh trước khi có hiệu lực. **Ví dụ:** Ninh yêu cầu đổi nhưng chưa mở thư xác minh thì email cũ vẫn là email đăng nhập. [quyền bước 3]
- `BR-23-02`: đổi thành công kết thúc phiên cũ và giữ đúng một tài khoản quản lý. **Ví dụ:** điện thoại B đang đăng nhập quản lý bị từ chối yêu cầu tiếp theo; Ninh đăng nhập lại bằng email mới. [quyền bước 3; CM-23]
- `BR-23-03`: email quản lý không cấp quyền cho tài khoản ghi chép. **Ví dụ:** tài khoản ghi chép cá nhân vẫn chỉ xem dữ liệu của mình và không gửi lời mời. [R12; quyền bước 3]

## 9. State Machine & Transition Rules (`STATE.*`)

`email cũ hiện hành → chờ xác minh email mới → email mới hiện hành`; nhánh xác minh thất bại/hết hạn giữ email cũ. Khi chuyển trạng thái thành công, phiên cũ bị thu hồi; không tạo manager thứ hai. Cơ chế xác minh/chuyển nguyên tử là việc Tech thiết kế.

## 10. Data Fallback & Offline Handling

Không đổi email offline. Nếu gửi xác minh hoặc hoàn tất chưa rõ, không báo đã đổi khi chưa xác nhận; đọc lại danh tính tài khoản sau khi có Internet. [R11; đề xuất an toàn]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.EMAIL_VERIFICATION_INVALID`, `ERR.EMAIL_IN_USE`, `ERR.MANAGER_SESSION_INVALID` là mã **đề xuất**, không phải copy chốt. Lỗi không lộ danh tính hoặc dữ liệu tài khoản khác.

## 12. Security & Privacy Constraints

Chỉ phiên quản lý hợp lệ thực hiện; email mới phải được chứng minh, không tin callback hoặc email từ trình duyệt. Thu hồi mọi phiên quản lý cũ khi đổi hiệu lực, kể cả JWT chưa hết hạn. Không được tạo quyền quản lý thứ hai hay quyền xem dữ liệu tài chính. [R12; quyền bước 3, TS33]

## 13. Third-party Integrations

Email xác minh đi qua SMTP Java theo [stack đã chốt](../../docs/requirements/tech-stack-round-2.md); nhà cung cấp thật chưa chọn. Hợp đồng gửi, xác minh và retry do Tech review.

## 14. Audit Log & History Requirements

Ninh chưa yêu cầu lịch sử thay đổi email trên giao diện. **Tech đề xuất** sự kiện bảo mật tối thiểu về yêu cầu/xác minh/thu hồi phiên, không lưu token hoặc nội dung nhạy cảm trong log.

## 15. NFR (Performance, Concurrency Bounds)

Khoảng 10 người dùng, web iPhone/Android; không có SLA riêng. Hai yêu cầu đổi gần đồng thời không được cho hai email quản lý cùng hiệu lực hoặc phiên cũ còn quyền sau chuyển. [R05, R14; CM-23]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: Ninh với phiên quản lý hợp lệ yêu cầu đổi email và email mới chỉ có hiệu lực sau xác minh; trước đó email cũ vẫn đăng nhập được. [quyền bước 3]
- `AC2`: sau xác minh, email mới đăng nhập được, email cũ không còn dùng để đăng nhập quản lý; mọi phiên quản lý cũ bị từ chối ở yêu cầu tiếp theo. [CM-23]
- `AC3`: tài khoản ghi chép/khách không đổi email quản lý; đổi email không cấp quyền quản lý cho tài khoản ghi chép của Ninh. [R12; quyền bước 3]
- `AC4`: xác minh sai/hết hạn hoặc xung đột email không làm đổi tài khoản quản lý hay hợp nhất dữ liệu/tài khoản. [điều kiện an toàn Tech/PM]

## 17. Figma Mapping / Visual References

Không áp dụng Figma; ảnh cũ chỉ tham khảo. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

CM-01 cấp phiên/manager và thu hồi; CM-04 cấp tài khoản ghi chép riêng để thử không gộp quyền. CM-22 gửi link đặt lại chỉ tới email quản lý hiện hành đã xác minh sau đổi. [CM-23](../capmoney-overall/issues/23-doi-email-quan-ly.md)

## 19. Q&A Log & Resolved Decisions

**Ninh đã xác nhận:** quyền tự đổi khi đang đăng nhập, xác minh email mới, hết phiên cũ và hai email của hai tài khoản Ninh tách biệt. **Chưa rõ:** phương thức/TTL xác minh, báo xung đột email và copy UI; xem [Q&A](qna.md). Nếu phương thức tạo lựa chọn sản phẩm mới, BA hỏi Ninh trước gate.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA 20 mục/AC1–AC4; chờ PM review | [CM-23](../capmoney-overall/issues/23-doi-email-quan-ly.md), [quyền](../../docs/requirements/actors-and-roles.md), [handoff](handoff.md) |
| 16/10/2026 | PM rà bản sau quyết định và chỉnh nguồn; `BA_READY` cho Story này, `TECH_READY` còn chờ. | [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) |
