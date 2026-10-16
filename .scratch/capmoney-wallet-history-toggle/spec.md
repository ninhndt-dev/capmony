# Feature Spec: Story 18 – Ngừng ví có lịch sử và bảo vệ số dư 0

**BA gate hiện hành (16/10/2026):** `BA_READY` sau CapmmPM `/root` đối chiếu bản sau R74–R78 hoặc chỉnh nhãn nguồn, BR/AC và 20 mục; xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md). Thiết kế kỹ thuật và `TECH_READY` còn chờ.

**Story:** [story.md](story.md)  
**BA gate tại lúc lập bản nháp:** chưa review; được cập nhật bởi dòng gate hiện hành ở trên.

## 1. Executive Summary / User Story statement

Là chủ tài khoản, tôi ngừng ví có lịch sử khi số dư hiện tại bằng 0, vẫn đọc lịch sử và ngăn mọi thao tác làm ví ngừng lệch 0; khi không còn ví dùng được, luồng ghi chép dẫn tôi tạo/bật ví. Nguồn: [Story nguồn #18](../capmoney-overall/issues/18-ngung-vi-co-lich-su.md), [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

Chủ tài khoản ghi chép đã đăng nhập được thao tác dữ liệu tài chính của chính mình; tài khoản ghi chép khác và tài khoản quản lý không được đọc hay sửa. Người chưa đăng nhập không được truy cập. [R08, R12; quyền bước 3]

## 3. Screen Definition & Entry Points

Từ ví có lịch sử và số dư 0 chọn ngừng. Ví ngừng vẫn mở lịch sử; từ nút ghi thu/chi chung khi mọi ví đã ngừng, hệ thống dẫn tạo ví mới hoặc bật ví rồi quay lại form. [B04, F20]

## 4. Main Flows (Happy Path)

1. Ví có giao dịch trước đây và hiện tại 0; chủ ngừng ví, vẫn đọc lịch sử cũ sau tải lại. [R48, R53]
2. Thao tác mới không chọn ví ngừng; sửa thông tin giao dịch cũ không đổi số dư ví ngừng vẫn được. [B04, B21]
3. Nếu tất cả ví ngừng, mở ghi thu/chi từ nút chung: chọn tạo ví mới hoặc bật ví cũ; sau hoàn tất quay về biểu mẫu với ví vừa tạo/bật được chọn sẵn; các trường nhập cũ đã bỏ theo R71/R73. [F20, R71, R73, R77]

## 5. Alternative & Exceptional Flows

- Sửa/xóa/khôi phục thu/chi hoặc chuyển mà làm ví ngừng khác 0 bị chặn toàn thao tác; bật lại trước khi thực hiện. [B13]
- Sửa ngày, ghi chú hoặc danh mục của khoản gắn ví ngừng được nếu số dư vẫn 0; đổi sang ví ngừng làm ví đó khác 0 bị chặn. [B20–B21]
- Sửa số dư đầu làm ví ngừng khác 0 cũng bị chặn. [B31]
- Ví số dư khác 0 không được ngừng; bật lại giữ lịch sử. [R53, B06]

- Sau tạo/bật ví từ luồng mọi ví ngừng, form mới chọn sẵn ví vừa tạo/bật và các trường nhập cũ trước khi rời form vẫn trống. [R77](../../docs/requirements/46-step-9-wallet-edit-and-return.md)

## 6. Field-level Specifications & Validations

| Thành phần | Quy tắc |
| --- | --- |
| Số dư khi ngừng | Bằng 0 và phải giữ 0 sau mọi thao tác. [R53, B13] |
| Lịch sử | Vẫn đọc được khi ví ngừng. [R48, B04] |
| Giao dịch cũ | Chỉ sửa trường không làm số dư ví ngừng lệch 0; xóa/khôi phục có tác động phải bật lại trước. [B13, B21] |
| Mọi ví ngừng | Nút ghi thu/chi dẫn tạo/bật rồi quay lại form. [F20] |

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.SUSPENDED_BALANCE = 0 VND`; không có thời gian ngừng tối đa. Kiểm lại số dư theo B01 trước khi đổi trạng thái/tác động giao dịch.

## 8. Business Rules (`BR-*`)

- `BR-18-01` **Ninh đã xác nhận:** Ví có lịch sử và số dư 0 được ngừng, lịch sử vẫn đọc. Ví đầu 0 có thu 100 và chi 100 có thể ngừng. [R48, R53, B04] **Ví dụ:** Ví đầu 0 có thu 100/chi 100 hiện 0 nên ngừng được và vẫn thấy hai dòng cũ.
- `BR-18-02` **Ninh đã xác nhận:** Ví ngừng luôn 0; xóa khoản chi 100 trong ví ví dụ sẽ làm +100 nên phải chặn đến khi bật lại. [B13] **Ví dụ:** Xóa khoản chi 100 của ví ngừng ấy sẽ làm ví +100 nên bị chặn.
- `BR-18-03` **Ninh đã xác nhận:** Sửa ngày/ghi chú/danh mục không đổi số dư vẫn được; chọn ví ngừng làm ví đích mới hoặc sửa số dư đầu làm lệch 0 thì chặn. [B20–B21, B31] **Ví dụ:** Sửa ghi chú của khoản thu không đổi số dư thì được; đổi ví mới sang ví ngừng bị chặn.
- `BR-18-04` **Ninh đã xác nhận:** Khi mọi ví ngừng, nút ghi thu/chi dẫn tạo/bật lại ví rồi quay về form; không tạo giao dịch trước khi có ví hoạt động. [F20] **Ví dụ:** Cả A/B đều ngừng: nút ghi thu dẫn tạo C hoặc bật A rồi trở về biểu mẫu.

- `BR-18-05` **Ninh đã xác nhận:** quay về ghi thu/chi chọn sẵn ví vừa tạo/bật, còn nội dung form cũ trước Lưu không khôi phục. **Ví dụ:** bật ví A rồi quay về ghi chi thì ví A đã chọn, số tiền cũ trống. [R77](../../docs/requirements/46-step-9-wallet-edit-and-return.md) [R71, R73]

## 9. State Machine & Transition Rules (`STATE.*`)

`hoạt động, lịch sử có, số dư 0 → ngừng → hoạt động`; trạng thái ngừng giữ lịch sử và số dư 0. Thao tác giao dịch tác động số dư khi ngừng bị chặn trước commit [B13].

## 10. Data Fallback & Offline Handling

Không ghi giao dịch offline [R11]. Sau bật/tạo ví từ luồng mọi ví ngừng, ví vừa tạo/bật được chọn sẵn theo R77; các trường nhập thu/chi cũ trước khi rời form mở trống theo R71/R73. Tech/UX chỉ chọn cách trình bày. Lỗi kết quả thao tác phải không báo thành công sai.

## 11. Error Messages & UI Registry (`ERR.*`)

Nêu rõ ví không đủ điều kiện ngừng hoặc cần bật lại trước khi sửa/xóa/khôi phục; không lộ dữ liệu ví người khác. Nếu một trong các ví bị ảnh hưởng không hợp lệ, toàn thao tác phải giữ nguyên số dư. [B13]

## 12. Security & Privacy Constraints

Mọi thao tác đọc/ghi phải kiểm chủ sở hữu ở phía hệ thống, gồm cả khi nhập trực tiếp đường dẫn hoặc định danh thuộc tài khoản khác. Tài khoản quản lý chỉ quản lý lời mời/hỗ trợ, không có quyền dữ liệu tài chính. [R08, R12; quyền bước 3]

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho lát cắt này: đây là thao tác trên dữ liệu tài chính của tài khoản ghi chép. Nền tảng lưu trữ/API do Tech thiết kế sau BA gate; không suy từ bản nháp này.

## 14. Audit Log & History Requirements

Trạng thái ngừng không xóa giao dịch/lịch sử; giao dịch đã xóa mềm vẫn tính là từng có khi xét xóa ví #19 [B19]. Tech cần đảm bảo mọi đường cập nhật B01 kiểm bất biến 0.

## 15. NFR (Performance, Concurrency Bounds)

Web ưu tiên iPhone/Android và chỉ dùng khi có Internet. Nhiều phiên của cùng tài khoản có thể hoạt động đồng thời; kết quả đã báo lưu phải bền sau tải lại. Chưa có ngưỡng độ trễ, số bản ghi hay phiên bản trình duyệt cụ thể được Ninh chốt. [R06, R11, R14, R56; S05]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1` Ví có lịch sử và số dư 0 ngừng được; sau tải lại vẫn đọc lịch sử; ví có số dư khác 0 không ngừng được.
- `AC2` Sửa/xóa/khôi phục thu/chi hoặc chuyển làm ví ngừng khác 0 bị chặn toàn thao tác; sau bật lại cùng thao tác hợp lệ được kiểm theo Story sở hữu.
- `AC3` Sửa ngày/ghi chú/danh mục không đổi số dư ví ngừng vẫn được; ví ngừng không làm ví mới trong sửa; sửa số dư đầu làm lệch 0 bị chặn.
- `AC4` Khi mọi ví ngừng, nút ghi thu/chi cho tạo ví mới hoặc bật lại rồi quay về biểu mẫu; không tạo khoản trước khi có ví hoạt động.

AC là điều kiện quan sát được để PM/QA rà; chưa có test thực chạy, chưa `QA_PASS`.

- `AC5` Khi mọi ví ngừng, tạo hoặc bật lại ví A rồi quay về ghi thu/chi: form chọn sẵn A, số tiền/ghi chú cũ đã nhập trước khi rời form không được khôi phục. [R77](../../docs/requirements/46-step-9-wallet-edit-and-return.md) [R71, R73]

## 17. Figma Mapping / Visual References

Ảnh ứng dụng cũ chỉ là tham khảo theo R02; chưa có Figma/màn hình được Ninh chốt. BA mô tả điểm vào và trạng thái quan sát được, còn bố cục/câu chữ do UX đề xuất và PM kiểm trước triển khai.

## 18. Dependencies (Cross-feature / Cross-story)

#10/#11/#12/#14/#15/#17 để kiểm lịch sử và mọi tác động giao dịch; phụ thuộc tích hợp, không chặn BA soạn spec.

Ngoài phạm vi: Ngừng ví trống #17; xóa ví #19; các thao tác sửa/xóa/khôi phục thuộc #11/#12/#14/#15. R71/R73 áp dụng reset form thu/chi trước Lưu, không tự thay đổi bởi #18.

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md). Quy tắc sản phẩm đã có nguồn `R48, R53; B01, B04, B06, B13, B20–B21, B31; F20`; các câu hỏi còn mở là câu hỏi/đề xuất, **chưa được Ninh xác nhận**. Không mở gate chỉ vì đã viết spec.

R77 đã chốt ví được chọn khi quay về theo F20; không còn câu hỏi sản phẩm về nhánh này.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Bản nháp bước 9 cho Story #18; chờ PM rà BA, Tech thiết kế và reviewer độc lập. | [Issue nguồn](../capmoney-overall/issues/18-ngung-vi-co-lich-su.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
| 16/10/2026 | PM rà bản sau quyết định và chỉnh nguồn; `BA_READY` cho Story này, `TECH_READY` còn chờ. | [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) |
