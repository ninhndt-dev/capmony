# Feature Spec: Story 17 – Ngừng và bật lại ví trống

**Story:** [story.md](story.md)  
**BA gate:** chưa đánh giá; bản nháp từ nguồn bước 8, chờ CapmmPM đối chiếu. Không có `BA_READY`/`TECH_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản, tôi ngừng một ví chưa từng có giao dịch và đang có số dư 0, rồi bật lại khi cần; ví ngừng không nhận giao dịch mới và luôn giữ số dư 0. Nguồn: [Story nguồn #17](../capmoney-overall/issues/17-ngung-va-bat-lai-vi-trong.md), [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

Chủ tài khoản ghi chép đã đăng nhập được thao tác dữ liệu tài chính của chính mình; tài khoản ghi chép khác và tài khoản quản lý không được đọc hay sửa. Người chưa đăng nhập không được truy cập. [R08, R12; quyền bước 3]

## 3. Screen Definition & Entry Points

Từ ví của mình, chọn ngừng khi điều kiện đạt; ví đã ngừng có thao tác bật lại. Vị trí điều khiển và câu chữ chưa chốt.

## 4. Main Flows (Happy Path)

1. Có ví chưa từng có giao dịch và số dư hiện tại 0; chủ ngừng ví. Sau tải lại ví ở trạng thái ngừng, không được chọn làm ví mới cho thu/chi/chuyển. [B04, R53]
2. Chủ bật dùng lại; ví trở lại trạng thái hoạt động và có thể dùng cho giao dịch mới. [B06]

## 5. Alternative & Exceptional Flows

- Ví còn số dư khác 0 không ngừng được, kể cả số âm. [R53, B01]
- Ví ngừng không được chọn làm ví đích khi sửa thu/chi/lần chuyển; phải bật lại trước. [B20]
- Sửa số dư ban đầu khiến ví ngừng khác 0 bị chặn; đổi tên ví ngừng được. [B31, B26]
- Ví có lịch sử nhưng số dư 0 thuộc #18; không dùng điều kiện “chưa từng có giao dịch” để phủ nhận #18. [R48]

## 6. Field-level Specifications & Validations

| Trường/trạng thái | Quy tắc |
| --- | --- |
| Số dư hiện tại | Phải bằng 0 khi ngừng; kể cả 0 do bù trừ. [R53, B01] |
| Lịch sử | Story #17 tập trung ví chưa từng có giao dịch; ví có lịch sử #18. |
| Trạng thái ví | Hoạt động ↔ ngừng; chỉ chủ đổi trạng thái. [B04, B06, R12] |

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.SUSPEND_BALANCE = 0 VND`. Không có hạn thời gian ngừng hoặc giới hạn số lần bật lại được Ninh chốt. [R53, B06]

## 8. Business Rules (`BR-*`)

- `BR-17-01` **Ninh đã xác nhận:** Chỉ ví có số dư hiện tại 0 mới ngừng; ví -1 hoặc +1 bị từ chối. [R53, B01] **Ví dụ:** Ví chưa từng có giao dịch, số dư 0 ngừng được; ví -1 không được ngừng.
- `BR-17-02` **Ninh đã xác nhận:** Ví ngừng không dùng cho giao dịch/chuyển mới hoặc làm ví mới trong sửa; phải bật lại trước. [B04, B20] **Ví dụ:** Ví ngừng A không nằm trong lựa chọn ví nhận khi tạo chuyển A→B.
- `BR-17-03` **Ninh đã xác nhận:** Ví ngừng luôn giữ 0; sửa số dư đầu làm lệch 0 bị chặn, đổi tên vẫn hợp lệ. [B31, B26] **Ví dụ:** Ví ngừng 0 đổi tên được; sửa số dư đầu thành 1 bị chặn.
- `BR-17-04` **Ninh đã xác nhận:** Bật lại không xóa ví/lịch sử và cho phép ghi mới; ví có lịch sử ngừng được ở #18. [B06, R48] **Ví dụ:** Bật lại ví A cho phép chọn A ở giao dịch mới; ví có lịch sử thuộc #18.

## 9. State Machine & Transition Rules (`STATE.*`)

`hoạt động, số dư 0 → ngừng → hoạt động`; `hoạt động, số dư ≠0 → từ chối ngừng`. Khi ngừng, mọi thao tác có thể làm số dư khác 0 phải chặn hoặc yêu cầu bật lại [B13, B31].

## 10. Data Fallback & Offline Handling

Không đổi trạng thái offline [R11]. Nếu kết quả đổi trạng thái chưa rõ, không báo thành công sai; tải lại phải phản ánh trạng thái đã lưu. Tech chọn hợp đồng bảo vệ điều kiện 0 dưới thao tác đồng thời.

## 11. Error Messages & UI Registry (`ERR.*`)

Cần phân biệt ví không thuộc chủ, số dư khác 0, trạng thái đã ngừng/đang dùng và xung đột đồng thời; từ chối không đổi dữ liệu. Câu chữ cụ thể chưa chốt.

## 12. Security & Privacy Constraints

Mọi thao tác đọc/ghi phải kiểm chủ sở hữu ở phía hệ thống, gồm cả khi nhập trực tiếp đường dẫn hoặc định danh thuộc tài khoản khác. Tài khoản quản lý chỉ quản lý lời mời/hỗ trợ, không có quyền dữ liệu tài chính. [R08, R12; quyền bước 3]

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho lát cắt này: đây là thao tác trên dữ liệu tài chính của tài khoản ghi chép. Nền tảng lưu trữ/API do Tech thiết kế sau BA gate; không suy từ bản nháp này.

## 14. Audit Log & History Requirements

Giữ cùng ví khi ngừng/bật lại; không xóa số dư đầu hoặc lịch sử. Nếu từng có giao dịch xóa mềm, ví vẫn tính “đã có lịch sử” cho #18/#19 [B19].

## 15. NFR (Performance, Concurrency Bounds)

Web ưu tiên iPhone/Android và chỉ dùng khi có Internet. Nhiều phiên của cùng tài khoản có thể hoạt động đồng thời; kết quả đã báo lưu phải bền sau tải lại. Chưa có ngưỡng độ trễ, số bản ghi hay phiên bản trình duyệt cụ thể được Ninh chốt. [R06, R11, R14, R56; S05]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1` Ví chưa có giao dịch, số dư 0 được ngừng và sau tải lại không chọn được cho giao dịch/chuyển mới hoặc làm ví đích khi sửa.
- `AC2` Ví số dư -1 hoặc +1 không ngừng được; dữ liệu/số dư giữ nguyên.
- `AC3` Bật lại ví ngừng giữ cùng ví và cho chọn giao dịch mới; tài khoản khác không đổi trạng thái.
- `AC4` Đổi tên ví ngừng được; sửa số dư đầu làm ví ngừng khác 0 bị chặn đến khi bật lại.

AC là điều kiện quan sát được để PM/QA rà; chưa có test thực chạy, chưa `QA_PASS`.

## 17. Figma Mapping / Visual References

Ảnh ứng dụng cũ chỉ là tham khảo theo R02; chưa có Figma/màn hình được Ninh chốt. BA mô tả điểm vào và trạng thái quan sát được, còn bố cục/câu chữ do UX đề xuất và PM kiểm trước triển khai.

## 18. Dependencies (Cross-feature / Cross-story)

#08/#11/#14/#16 cần kiểm các thao tác với ví ngừng; phụ thuộc Dev/tích hợp không chặn BA đặc tả.

Ngoài phạm vi: Ví có lịch sử và số dư 0 #18; sửa thông tin ví #16; xóa ví #19. Bảo vệ commit khi đồng thời là điểm Tech phải chứng minh.

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md). Quy tắc sản phẩm đã có nguồn `R48, R53; B01, B04, B06, B13, B20, B26, B31`; các câu hỏi còn mở là câu hỏi/đề xuất, **chưa được Ninh xác nhận**. Không mở gate chỉ vì đã viết spec.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Bản nháp bước 9 cho Story #17; chờ PM rà BA, Tech thiết kế và reviewer độc lập. | [Issue nguồn](../capmoney-overall/issues/17-ngung-va-bat-lai-vi-trong.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
