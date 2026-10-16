# Feature Spec: CM-19 – Xóa ví đủ điều kiện

**BA gate hiện hành (16/10/2026):** `BA_READY` sau CapmmPM `/root` đối chiếu độc lập nguồn, BR/AC và 20 mục; xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md). Thiết kế kỹ thuật và `TECH_READY` còn chờ.

**Story:** [story.md](story.md) · **Nguồn chia lát:** [CM-19](../capmoney-overall/issues/19-xoa-vi-du-dieu-kien.md)  
**BA gate tại lúc lập bản nháp:** chưa review; được cập nhật bởi dòng gate hiện hành ở trên.

## 1. Executive Summary / User Story statement

Chủ ví muốn xóa hẳn ví không còn cần dùng khi ví có số dư 0 và chưa từng có giao dịch; ví có lịch sử được giữ để dữ liệu tài chính không mất. **Ninh đã xác nhận:** B19, R12. [Nghiệp vụ](../../docs/requirements/core-business.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

| Actor | Quyền | Nguồn |
| --- | --- | --- |
| Chủ tài khoản ghi chép | Xem điều kiện và xóa ví của chính mình | R12, B19 |
| Chủ tài khoản khác; tài khoản quản lý; khách | Không xem hoặc xóa ví này qua UI hay đường xử lý trực tiếp | R08, R12; quyền bước 3 |

## 3. Screen Definition & Entry Points

Từ chi tiết hoặc danh sách ví của chủ, mở thao tác xóa ví. **BA đề xuất:** nêu lý do nếu ví không đủ điều kiện và yêu cầu xác nhận trước khi xóa vĩnh viễn; bố cục/câu chữ chưa được Ninh duyệt riêng. Ví ngừng sử dụng cũng phải qua cùng điều kiện B19.

## 4. Main Flows (Happy Path)

1. Chủ mở ví của mình với số dư hiện tại 0 và chưa từng có khoản thu/chi hoặc lần chuyển nào.
2. Chủ chọn xóa và xác nhận. Hệ thống kiểm lại chủ, số dư và lịch sử tại thời điểm xóa.
3. Ví không còn trong danh sách sau tải lại; ví khác cùng lịch sử của chúng giữ nguyên. [B19; CM-19]

## 5. Alternative & Exceptional Flows

- Ví có số dư khác 0, kể cả âm, không xóa được. Ví từng có giao dịch còn hiệu lực hoặc đã xóa mềm không xóa được dù hiện có số dư 0. [B19]
- Ví bị tạo giao dịch hoặc đổi số dư giữa lúc mở màn và lúc xác nhận phải bị từ chối theo trạng thái mới. **Tech đề xuất** kiểm nguyên tử để giữ B19.
- Yêu cầu lặp cho ví đã xóa không làm xóa ví khác; thông báo kết quả an toàn. Mất kết nối không được báo đã xóa khi chưa biết kết quả.

## 6. Field-level Specifications & Validations

Chỉ chọn định danh ví thuộc phiên của chủ. Số dư hiện tại là giá trị theo B01, không phải số dư ban đầu riêng lẻ. Điều kiện “chưa từng có” tính cả khoản thu/chi và lần chuyển, kể cả bản đã xóa mềm. Không cần nhập số tiền hoặc tên mới trong luồng này. [B01, B19]

## 7. System Parameters & Constants (`PARAM.*`)

Không có thời hạn hay ngưỡng sản phẩm riêng được Ninh chốt cho thao tác xóa ví. **Tech đề xuất** cách kiểm truy vết giao dịch và chống thao tác lặp; không biến thời gian lưu hoặc số lần thử thành quyết định sản phẩm.

## 8. Business Rules (`BR-*`)

- `BR-19-01`: ví chỉ xóa hẳn khi số dư hiện tại bằng 0 và chưa từng có giao dịch. **Ví dụ:** ví mới số dư đầu 0, chưa giao dịch thì xóa được; ví số dư đầu -1 VND thì không. [B19]
- `BR-19-02`: giao dịch đã xóa mềm vẫn tính là từng có. **Ví dụ:** ví nhận khoản thu 10.000 VND rồi xóa mềm khoản thu, số dư trở về 0 nhưng ví vẫn không xóa được. [B19]
- `BR-19-03`: xóa một ví không đổi dữ liệu ví khác. **Ví dụ:** xóa ví trống A, ví B cùng lịch sử của B giữ nguyên sau tải lại. [CM-19, R12]

## 9. State Machine & Transition Rules (`STATE.*`)

`ví tồn tại đủ điều kiện → đã xóa vĩnh viễn`; mọi trạng thái không đủ điều kiện giữ nguyên khi yêu cầu bị từ chối. `Ngừng sử dụng` là trạng thái khác, không tự đồng nghĩa với xóa; #18 quản lý bật/ngừng ví. Xóa hẳn không có luồng hoàn tác trong CM-19. [B04, B06, B19]

## 10. Data Fallback & Offline Handling

CapMoney bản đầu chỉ dùng khi có Internet. Không xóa cục bộ khi offline. Nếu phản hồi không rõ, tải lại/đối chiếu trạng thái ví trước khi cho kết luận; cơ chế kỹ thuật cần Tech thiết kế. [R11]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.WALLET_NOT_EMPTY`, `ERR.WALLET_HAS_HISTORY`, `ERR.WALLET_UNAVAILABLE` là **mã đề xuất** cho các nhánh kiểm thử; câu chữ chưa được duyệt. Không tiết lộ ví của chủ khác qua khác biệt thông báo.

## 12. Security & Privacy Constraints

Chủ ví lấy từ phiên server; không tin owner ID do trình duyệt gửi. Kiểm quyền và điều kiện B19 ở cùng ranh giới xóa để tránh mất lịch sử khi có ghi giao dịch đồng thời. Tài khoản quản lý không được xem dữ liệu tài chính. [R12, B19]

## 13. Third-party Integrations

Không áp dụng dịch vụ ngoài riêng cho xóa ví; đây là thao tác trên dữ liệu nội bộ. Hợp đồng lưu trữ cần Tech thiết kế sau `BA_READY`.

## 14. Audit Log & History Requirements

Không có yêu cầu Ninh chốt về log xóa ví riêng. Ví đủ điều kiện không có lịch sử giao dịch; không được xóa nhật ký của ví khác. Log vận hành nếu có là quyết định kỹ thuật và không lộ dữ liệu tài chính.

## 15. NFR (Performance, Concurrency Bounds)

Bản đầu khoảng 10 người dùng, web iPhone/Android. Không có SLA riêng. **Điểm an toàn:** giao dịch hoặc thay đổi số dư đến gần thời điểm xác nhận không được khiến ví không đủ điều kiện bị xóa. [R05, R14, B19]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: chủ xóa được ví số dư 0 chưa từng có thu/chi/chuyển; sau tải lại ví không còn, ví khác không đổi. [B19; CM-19]
- `AC2`: ví số dư khác 0 hoặc từng có giao dịch còn hiệu lực không bị xóa. [B19]
- `AC3`: ví từng có giao dịch đã xóa mềm không bị xóa dù số dư hiện tại 0. [B19]
- `AC4`: tài khoản khác, quản lý và khách không thể xem/xóa ví; thử yêu cầu lặp hay thay đổi điều kiện sát lúc xóa không xóa sai dữ liệu. [R12, B19; điều kiện an toàn Tech]

## 17. Figma Mapping / Visual References

Không áp dụng Figma vì chưa có thiết kế được duyệt. Ảnh CapMoney cũ chỉ tham khảo, không chốt pixel/hành vi. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

CM-06 cấp ví; CM-12 và CM-15 cấp lịch sử thu/chi/chuyển đã xóa mềm cần xét điều kiện B19. CM-18 cấp trạng thái ngừng/bật ví. Các cạnh Dev theo [Story nguồn](../capmoney-overall/issues/19-xoa-vi-du-dieu-kien.md), không chặn soạn BA.

## 19. Q&A Log & Resolved Decisions

**Ninh đã xác nhận:** B19 cùng quyền R12 đủ chốt quy tắc xóa. **Chưa rõ nhưng không thay đổi B19:** bố cục/câu chữ và cơ chế đối chiếu mất phản hồi; xem [Q&A](qna.md). Không có câu hỏi sản phẩm chặn bản BA hiện tại.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA 20 mục/AC1–AC4; chờ PM review | [CM-19](../capmoney-overall/issues/19-xoa-vi-du-dieu-kien.md), [B19](../../docs/requirements/core-business.md), [handoff](handoff.md) |
| 16/10/2026 | PM rà bản sau quyết định và chỉnh nguồn; `BA_READY` cho Story này, `TECH_READY` còn chờ. | [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) |
