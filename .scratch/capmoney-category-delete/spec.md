# Feature Spec: CM-21 – Xóa danh mục chưa dùng

**Story:** [story.md](story.md) · **Nguồn chia lát:** [CM-21](../capmoney-overall/issues/21-xoa-danh-muc-chua-dung.md)  
**BA gate:** bản BA bước 9; chưa được PM review, chưa có `BA_READY`.

## 1. Executive Summary / User Story statement

Chủ tài khoản được xóa hẳn danh mục chưa từng gắn vào giao dịch, kể cả danh mục mặc định. Danh mục đã dùng được giữ và vẫn chọn được để lịch sử không mất nghĩa. [B05, B11, B16; nghiệp vụ](../../docs/requirements/core-business.md).

## 2. Actor & Permission Matrix

| Actor | Quyền | Nguồn |
| --- | --- | --- |
| Chủ tài khoản ghi chép | Xóa danh mục chưa từng dùng của mình | R12, B11 |
| Tài khoản khác; quản lý; khách | Không xem hoặc xóa danh mục của chủ | R08, R12; quyền bước 3 |

## 3. Screen Definition & Entry Points

Từ danh sách danh mục riêng của chủ, mở thao tác xóa. **BA đề xuất:** hiện điều kiện không thể xóa nếu đã dùng và yêu cầu xác nhận trước xóa hẳn; bố cục/câu chữ chưa được duyệt riêng. Không có đường xóa danh mục của người khác.

## 4. Main Flows (Happy Path)

1. Chủ chọn một danh mục mặc định hoặc tự tạo chưa từng gắn giao dịch.
2. Hệ thống kiểm lại quyền và lịch sử sử dụng tại thời điểm xóa; nếu chưa từng dùng thì xóa hẳn.
3. Danh mục không còn trong danh sách của chủ sau tải lại; dữ liệu của người khác không đổi. [B11]

## 5. Alternative & Exceptional Flows

- Danh mục đã từng gắn vào khoản thu/chi không xóa được, kể cả khi giao dịch sau đó bị xóa mềm. Đây là diễn giải trực tiếp của “chưa từng gắn” ở B11, cần PM rà cùng B05.
- Nếu giao dịch mới gắn danh mục giữa lúc mở màn và lúc xác nhận xóa, thao tác phải bị từ chối; không được làm giao dịch mồ côi. **Tech đề xuất** kiểm nguyên tử.
- Mất phản hồi hoặc bấm lặp không được xóa nhầm danh mục khác; không báo thành công trước khi biết kết quả.

## 6. Field-level Specifications & Validations

Đầu vào là định danh danh mục thuộc chủ. Trạng thái “đã dùng” dựa trên việc từng gắn giao dịch, không dựa trên tên hay chỉ số giao dịch đang hiển thị. Danh mục mặc định và tự tạo theo cùng quy tắc. [B05, B11, B16]

## 7. System Parameters & Constants (`PARAM.*`)

Không có thời hạn khôi phục hoặc giới hạn số danh mục được Ninh chốt cho xóa danh mục. CM-21 xóa hẳn khi đủ điều kiện theo B11; Tech chọn cơ chế lưu/kiểm lịch sử sử dụng.

## 8. Business Rules (`BR-*`)

- `BR-21-01`: danh mục chưa từng gắn giao dịch được xóa hẳn, kể cả mặc định. **Ví dụ:** “Giải trí” mặc định chưa được dùng biến mất sau xóa và tải lại. [B11, B16]
- `BR-21-02`: danh mục đã dùng không xóa được và vẫn chọn được. **Ví dụ:** “Ăn uống” có một khoản chi, dù khoản chi bị xóa mềm, vẫn giữ để có thể khôi phục và vẫn nằm trong bộ chọn. [B05, B11]
- `BR-21-03`: xóa danh mục A không tác động giao dịch/danh mục khác. **Ví dụ:** khoản thu gắn “Lương” vẫn hiện đúng sau xóa “Giải trí” chưa dùng. [R12, B05]

## 9. State Machine & Transition Rules (`STATE.*`)

`chưa từng dùng → đã xóa hẳn` là đường xóa duy nhất. `chưa từng dùng → đã dùng` khi một giao dịch được ghi; từ `đã dùng` không có đường xóa danh mục, chỉ đổi tên ở CM-20. [B05, B11]

## 10. Data Fallback & Offline Handling

Không xóa offline. Khi kết quả chưa rõ, đọc lại danh sách của chủ và trạng thái sử dụng trước khi kết luận; không dùng trạng thái UI cũ để bỏ qua điều kiện. [R11; đề xuất Tech]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.CATEGORY_USED` và `ERR.CATEGORY_UNAVAILABLE` là mã **đề xuất**; nội dung cụ thể chưa được Ninh duyệt. Trường hợp không có quyền không tiết lộ danh mục của người khác.

## 12. Security & Privacy Constraints

Chủ lấy từ phiên server, kiểm quyền khi đọc/xóa. Cùng ranh giới kiểm “chưa từng dùng” và xóa phải chặn giao dịch mới tham chiếu danh mục bị xóa. Tài khoản quản lý không được đọc dữ liệu tài chính. [R12, B11]

## 13. Third-party Integrations

Không áp dụng dịch vụ ngoài riêng cho xóa danh mục; Tech thiết kế lưu trữ nội bộ sau gate BA.

## 14. Audit Log & History Requirements

Lịch sử giao dịch đã dùng danh mục phải giữ liên kết/tên của nó sau tải lại. Ninh chưa yêu cầu log xóa danh mục riêng; chỉ danh mục chưa từng dùng mới được xóa. [B05, B11]

## 15. NFR (Performance, Concurrency Bounds)

Bản đầu khoảng 10 người trên web iPhone/Android, không có SLA riêng. Tình huống ghi giao dịch cùng lúc với xóa cần kết quả nhất quán: giao dịch được lưu thì danh mục không thể bị xóa. [R05, R14, B11]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: chủ xóa được danh mục chưa từng dùng, cả mặc định và tự tạo; sau tải lại danh mục không còn. [B11]
- `AC2`: danh mục đã từng gắn giao dịch không bị xóa và vẫn chọn được cho giao dịch mới, kể cả khi giao dịch trước bị xóa mềm. [B05, B11]
- `AC3`: lịch sử giao dịch dùng danh mục đã dùng vẫn hiển thị đúng sau tải lại; danh mục khác không đổi. [B05; CM-21]
- `AC4`: tài khoản khác, quản lý và khách không xóa/đọc danh mục của chủ; yêu cầu xóa gần đồng thời với ghi giao dịch không tạo tham chiếu mồ côi. [R12, B11; điều kiện Tech]

## 17. Figma Mapping / Visual References

Không áp dụng Figma; chưa có thiết kế được duyệt và ảnh cũ chỉ tham khảo. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

CM-08 cấp danh mục mặc định và ghi khoản thu; CM-20 thêm/đổi tên danh mục. CM-12/CM-15 cung cấp trạng thái giao dịch xóa mềm để kiểm “đã từng dùng” trong dữ liệu đầy đủ. [CM-21](../capmoney-overall/issues/21-xoa-danh-muc-chua-dung.md)

## 19. Q&A Log & Resolved Decisions

**Ninh đã xác nhận:** B05/B11/B16. **Cần PM đối chiếu diễn giải:** giao dịch xóa mềm vẫn là “đã từng gắn”, nên danh mục giữ lại; xem [Q&A](qna.md). Câu chữ UI/chi tiết đồng thời là thiết kế, chưa phải yêu cầu Ninh chốt.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA 20 mục/AC1–AC4; chờ PM review | [CM-21](../capmoney-overall/issues/21-xoa-danh-muc-chua-dung.md), [nghiệp vụ](../../docs/requirements/core-business.md), [handoff](handoff.md) |
