# Feature Spec: Story 28 – Tải dữ liệu trên điện thoại thứ hai

**Story:** [story.md](story.md) · **BA gate:** bản nháp, chờ PM review; chưa `BA_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản dùng hai điện thoại, tôi muốn khi quay lại ứng dụng có Internet thì thấy giao dịch mới đã lưu ở điện thoại kia. Nếu ứng dụng tự tải dữ liệu khi tôi **vẫn ở biểu mẫu**, phần đang nhập dở trong phiên không bị xóa. [F06, F08](../../docs/requirements/user-flows.md); [issue #28](../capmoney-overall/issues/28-tai-du-lieu-dien-thoai-thu-hai.md).

## 2. Actor & Permission Matrix

| Actor | Quyền trong Story |
| --- | --- |
| Chủ tài khoản ghi chép trên điện thoại A/B | Đọc dữ liệu đã lưu của chính mình; tiếp tục bản nhập dở chưa lưu trên B. |
| Tài khoản ghi chép khác; tài khoản quản lý | Không đọc giao dịch, ví hoặc bản nhập dở của chủ. |

Nguồn: [R08, R12, R27, R56](../../docs/requirements/00-intake.md).

## 3. Screen Definition & Entry Points

Điểm vào là đưa app/tab trên điện thoại B từ nền trở lại khi vẫn ở **cùng màn**, hoặc dữ liệu tự tải khi biểu mẫu vẫn mở. Lịch sử chung/lọc ví và số dư hiện tại là đầu ra của #10. “Quay lại ứng dụng” ở [F06/F08](../../docs/requirements/user-flows.md) không phải rời **biểu mẫu** sang trang khác rồi quay lại trước Lưu; việc ấy mở form trống cho thu/chi/chuyển theo [R71/R73/R74](../../docs/requirements/00-intake.md). Bố cục và sự kiện kỹ thuật nhận biết trở lại do Tech/UX đề xuất.

## 4. Main Flows (Happy Path)

1. Cùng tài khoản đăng nhập trên A và B; B đang ở **màn lịch sử**. A Lưu một giao dịch khác và nhận xác nhận.
2. B đưa app/tab vào nền rồi quay lại **màn lịch sử** khi có Internet; B tự thấy giao dịch A đã lưu và số dư đúng. [F06, B01]
3. Ca riêng: B đang nhập giao dịch nhưng chưa Lưu và **vẫn ở biểu mẫu**; A Lưu một giao dịch khác. Khi B đưa app/tab vào nền rồi quay lại cùng biểu mẫu, hoặc dữ liệu tự tải tại biểu mẫu, phần B đã nhập còn trong phiên. [F08]

## 5. Alternative & Exceptional Flows

- Khi không có Internet, không xác nhận đã tải bản mới; thử lại khi có Internet. Bản đầu không ghi offline. [R11, F06]
- Nếu dữ liệu mới là **cùng giao dịch** mà B đang sửa từ bản cũ, nội dung đang nhập vẫn còn, nhưng Lưu bản cũ bị chặn theo #31/#32; Story này không tự hợp nhất. [F08, F22, B30]
- Nếu B **điều hướng khỏi form trước Lưu** rồi quay lại cùng phiên, form thu/chi/chuyển mở trống theo R71/R73/R74; F08 không giữ bản nháp qua đường điều hướng này.
- Đóng hẳn trình duyệt **trước Lưu** không khôi phục bản nhập dở theo F21; giữ bản nháp khi tự tải trong phiên không thay đổi F21.

## 6. Field-level Specifications & Validations

Không có trường nhập mới. Khóa đối chiếu là cùng tài khoản đã đăng nhập; giao dịch hiển thị có ngày, số tiền, ví và dữ liệu theo loại do #10/#13 cung cấp. Bản nhập dở chỉ là các trường đã nhập trong form giao dịch hiện hành; phạm vi chính xác theo form #08/#09/#13. [F06, F08, F15–F17]

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.NETWORK = Internet required`; `PARAM.ACCOUNT_SCOPE = chủ tài khoản`; `PARAM.REFRESH_TRIGGER = quay lại ứng dụng`. Ninh chưa chốt khoảng polling, độ trễ hoặc hạn giữ bản nháp; Tech đề xuất và PM kiểm trước `TECH_READY`.

## 8. Business Rules (`BR-*`)

- `BR-28-01` **Ninh đã xác nhận:** hai điện thoại cùng tài khoản đọc lại cùng dữ liệu đã lưu. Ví dụ A ghi thu 100.000 VND và được báo lưu; B quay lại có Internet thì thấy đúng khoản đó. [R27, R56, F06]
- `BR-28-02` **Ninh đã xác nhận:** tự tải dữ liệu mới khi B **vẫn ở biểu mẫu** không xóa phần nhập trong phiên. Ví dụ B đang nhập ghi chú “đi chợ”, đưa tab vào nền; A Lưu khoản khác; B mở lại đúng tab/form thì ghi chú còn. Nếu B rời form sang trang khác trước Lưu, áp R71/R73/R74 và form mở trống. [F08, R71, R73, R74]
- `BR-28-03` **Ninh đã xác nhận:** tài khoản khác không nhận dữ liệu của chủ; số dư theo các giao dịch đã lưu. [R08, R12, B01]

## 9. State Machine & Transition Rules (`STATE.*`)

`STATE.B_BACKGROUND` → người dùng quay lại **cùng màn** có Internet → `STATE.REFRESHING` → `STATE.CURRENT` khi dữ liệu đã lưu được tải. Bản nhập dở tại form đang mở không chuyển thành bản đã lưu hoặc bị xóa bởi refresh. `STATE.NAVIGATED_AWAY_BEFORE_SAVE` của form thu/chi/chuyển → form trống khi quay lại theo R71/R73/R74. Nếu thiếu mạng, ở `STATE.WAITING_NETWORK`, không báo đã cập nhật. Xung đột khi Lưu cùng bản thuộc #31/#32.

## 10. Data Fallback & Offline Handling

Không áp dụng ghi hoặc đồng bộ offline; khi không có Internet, chỉ giữ nội dung ở **form đang mở** trong phiên và chờ tải dữ liệu. Rời form thu/chi/chuyển trước Lưu rồi quay lại mở trống theo R71/R73/R74; đóng trình duyệt trước Lưu mở trống theo F21. Không diễn giải việc tải mới thành phục hồi bản nháp đã bỏ.

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.REFRESH_UNAVAILABLE`: đề xuất báo chưa thể cập nhật khi thiếu mạng/lỗi tải; không nói dữ liệu đã mới. Câu chữ/hiển thị do UX/Tech đề xuất; Ninh chưa duyệt riêng.

## 12. Security & Privacy Constraints

Phiên của B phải thuộc cùng chủ với dữ liệu tải; không nhận owner ID từ form làm căn cứ quyền. Tài khoản khác và tài khoản quản lý không đọc được dữ liệu tài chính. [R08, R12]

## 13. Third-party Integrations

Không áp dụng dịch vụ ngoài riêng cho Story; dữ liệu đến từ nơi lưu CapMoney. Hợp đồng tải và cache do Tech thiết kế.

## 14. Audit Log & History Requirements

Lịch sử được tải là lịch sử đã lưu của #10/#13, không tạo giao dịch mới do thao tác refresh. Không có yêu cầu nhật ký kiểm toán riêng do Ninh xác nhận.

## 15. NFR (Performance, Concurrency Bounds)

Phục vụ web iPhone/Android, khoảng 10 người; dữ liệu đã báo lưu ở A phải thấy ở B khi quay lại có Internet. Chưa chốt ngưỡng thời gian. Trường hợp hai Lưu đồng thời thuộc #29/#30. [S01, S05](../../docs/requirements/goal-and-scope.md); [P02](../../docs/requirements/system-functions.md).

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: A và B cùng tài khoản; B ở lịch sử, đưa app/tab vào nền; A Lưu khoản thu 100.000 VND và được báo thành công; B quay lại **cùng màn lịch sử** có Internet thì tự thấy đúng một khoản và số dư đúng sau tải lại. [F06, B01]
- `AC2`: B đang nhập khoản thu/chi/chuyển **ở form vẫn mở**, đưa app/tab vào nền; A Lưu giao dịch khác; B trở lại **đúng form** có Internet hoặc dữ liệu tự tải khi form còn mở → các trường B đã nhập trong phiên vẫn còn, chưa có giao dịch mới từ bản nháp. [F08]
- `AC3`: B rời form thu/chi/chuyển **sang trang khác trước Lưu** rồi quay lại cùng phiên → form trống, không tạo giao dịch hay đổi số dư từ bản nháp. [R71, R73, R74]
- `AC4`: tài khoản C và tài khoản quản lý không thấy giao dịch/ví của A qua UI hoặc truy cập dữ liệu trực tiếp. [R08, R12]
- `AC5`: B thiếu Internet không được báo đã tải bản mới; khi Internet trở lại và quay lại, thấy bản đã lưu. [R11, F06]
- `AC6`: nếu A sửa cùng giao dịch B đang mở, tải mới không làm mất bản nhập dở; Lưu bản cũ được kiểm và chặn ở #31/#32. [F08, B30]

## 17. Figma Mapping / Visual References

Không có Figma đã duyệt; ảnh CapMoney chỉ tham khảo giao diện theo R02. UX đề xuất chỉ dấu cập nhật, không tạo quy tắc mới.

## 18. Dependencies (Cross-feature / Cross-story)

`HARD`: #10 để có lịch sử và số dư đọc lại; #08/#09/#13 cấp giao dịch; #31/#32 xử lý bản cũ cùng giao dịch. Phụ thuộc đầu tiên ở [issue #28](../capmoney-overall/issues/28-tai-du-lieu-dien-thoai-thu-hai.md), các phụ thuộc liên quan không tự đổi cạnh Planio.

## 19. Q&A Log & Resolved Decisions

[Q&A](qna.md): không có câu hỏi sản phẩm mới chặn BA; F06/F08/F21, R27/R56 và R71/R73/R74 phân biệt quay lại app/tab với rời form sang trang khác. Chu kỳ refresh, cache, dấu hiệu mạng là Tech/UX đề xuất.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA bước 9; chưa PM review/gate. | [Issue #28](../capmoney-overall/issues/28-tai-du-lieu-dien-thoai-thu-hai.md), F06/F08, B01, R27/R56 |
