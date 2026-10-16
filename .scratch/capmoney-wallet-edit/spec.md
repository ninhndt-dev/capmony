# Feature Spec: Story 16 – Sửa tên và số dư ban đầu của ví

**BA gate hiện hành (16/10/2026):** `BA_READY` sau CapmmPM `/root` đối chiếu bản sau R74–R78 hoặc chỉnh nhãn nguồn, BR/AC và 20 mục; xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md). Thiết kế kỹ thuật và `TECH_READY` còn chờ.

**Story:** [story.md](story.md)  
**BA gate tại lúc lập bản nháp:** chưa review; được cập nhật bởi dòng gate hiện hành ở trên.

## 1. Executive Summary / User Story statement

Là chủ tài khoản, tôi đổi tên hoặc số dư ban đầu của ví đã tạo; giao dịch cũ vẫn thuộc cùng ví và số dư hiện tại được tính lại. Nguồn: [Story nguồn #16](../capmoney-overall/issues/16-sua-thong-tin-vi.md), [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

Chủ tài khoản ghi chép đã đăng nhập được thao tác dữ liệu tài chính của chính mình; tài khoản ghi chép khác và tài khoản quản lý không được đọc hay sửa. Người chưa đăng nhập không được truy cập. [R08, R12; quyền bước 3]

## 3. Screen Definition & Entry Points

Từ ví của chính mình mở sửa thông tin; form có tên ví và số dư ban đầu đã lưu. Cách đặt nút/bố cục chưa chốt. Ví đã có giao dịch hoặc đã ngừng vẫn có thể đổi tên. [B26]

## 4. Main Flows (Happy Path)

1. Chủ chọn ví, đổi tên; lịch sử giao dịch cũ vẫn gắn đúng ví và hiện tên mới. [B26, B29]
2. Chủ sửa số dư ban đầu bằng số nguyên VND, có thể âm; Lưu thành công làm số dư hiện tại tính lại theo B01 mà không sinh khoản thu. [R30, R40, R44, B15]
3. Tải lại trang, tên mới và số dư mới vẫn đúng.

## 5. Alternative & Exceptional Flows

- Đổi tên thành trống không Lưu; tên và lịch sử cũ giữ nguyên. [R76](../../docs/requirements/46-step-9-wallet-edit-and-return.md)
- Giá trị số dư đầu không nguyên hoặc không phải VND hợp lệ bị từ chối; bản cũ giữ nguyên. [B15, R16]
- Nếu ví đang ngừng, đổi tên được; sửa số dư đầu làm số dư hiện tại khác 0 bị chặn, phải bật ví lại trước (#17). [B26, B31]
- Ví/tài khoản khác không được xem hoặc sửa. [R12]
- Sửa số dư đầu không tạo khoản thu và không đổi quyền/lịch sử của ví. [R30, B29]

## 6. Field-level Specifications & Validations

| Trường | Quy tắc |
| --- | --- |
| Tên ví | Chủ được đổi kể cả ví đã có lịch sử/ngừng; giao dịch cũ hiện tên mới; tên trống không Lưu. [B26, B29, R76] |
| Số dư ban đầu | Số nguyên VND, có thể âm; không phải khoản thu. [B10, B15, R30] |
| Số dư hiện tại | Tính lại bằng B01, không nhập trực tiếp. [B01, R44] |

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.INITIAL_BALANCE = số nguyên VND` kể cả âm; không có số dư tối thiểu hay giới hạn tên ví được Ninh chốt ở Story này. Tên bắt buộc khi tạo theo R66 và khi đổi theo R76; không tự gán quy tắc tên duy nhất hoặc ngưỡng độ dài.

## 8. Business Rules (`BR-*`)

- `BR-16-01` **Ninh đã xác nhận:** Đổi tên ví giữ định danh và lịch sử cũ; ví “A” có khoản thu 100 đổi thành “B”, khoản cũ hiện “B”. [B26, B29] **Ví dụ:** Đổi ví A có khoản thu cũ thành B: khoản thu ấy hiển thị tên B.
- `BR-16-02` **Ninh đã xác nhận:** Sửa số dư đầu làm số dư hiện tại đổi đúng phần chênh; ví đầu 50 có thu 100 thành số dư 150, sửa số dư đầu thành -20 thì số dư hiện tại 80. [B01, R44] **Ví dụ:** Số dư đầu 50 có thu 100 thành 150; đổi số dư đầu -20 thì còn 80.
- `BR-16-03` **Ninh đã xác nhận:** Số dư đầu có thể âm, chỉ nguyên VND, không sinh khoản thu lịch sử. [B10, B15, R30] **Ví dụ:** Số dư đầu -20 hợp lệ; 1,5 VND bị từ chối và không tạo khoản thu lịch sử.
- `BR-16-04` **Ninh đã xác nhận:** Ví đang ngừng luôn giữ số dư 0; sửa số dư đầu làm lệch 0 phải bật lại trước. [B31] **Ví dụ:** Ví ngừng 0 đổi tên được; tăng số dư đầu lên 1 VND bị chặn tới khi bật lại.

- `BR-16-05` **Ninh đã xác nhận:** tên trống khi đổi tên ví không được Lưu. **Ví dụ:** đổi ví “A” thành rỗng rồi bấm Lưu thì ví vẫn tên “A”, giao dịch cũ giữ nguyên. [R76](../../docs/requirements/46-step-9-wallet-edit-and-return.md)

## 9. State Machine & Transition Rules (`STATE.*`)

`ví hoạt động → sửa tên/số dư đầu → hoạt động`; `ví ngừng → đổi tên được`; `ví ngừng → sửa số dư đầu làm hiện tại ≠0` bị chặn tới khi bật lại. Không đổi định danh ví hoặc giao dịch [B26, B29, B31].

## 10. Data Fallback & Offline Handling

Không sửa offline [R11]. Khi Lưu chưa xác nhận, không coi tên/số dư mới là đã lưu; Tech cần hợp đồng kết quả thao tác. Tải lại khi có Internet đọc giá trị đã lưu và tính B01.

## 11. Error Messages & UI Registry (`ERR.*`)

Số dư đầu không nguyên, ví không thuộc chủ, ví ngừng bị lệch 0 do sửa: từ chối, tên/số dư đã lưu và lịch sử giữ nguyên. Câu chữ lỗi chưa chốt.

## 12. Security & Privacy Constraints

Mọi thao tác đọc/ghi phải kiểm chủ sở hữu ở phía hệ thống, gồm cả khi nhập trực tiếp đường dẫn hoặc định danh thuộc tài khoản khác. Tài khoản quản lý chỉ quản lý lời mời/hỗ trợ, không có quyền dữ liệu tài chính. [R08, R12; quyền bước 3]

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho lát cắt này: đây là thao tác trên dữ liệu tài chính của tài khoản ghi chép. Nền tảng lưu trữ/API do Tech thiết kế sau BA gate; không suy từ bản nháp này.

## 14. Audit Log & History Requirements

Giữ cùng định danh ví khi đổi tên/số dư ban đầu; mọi giao dịch cũ tiếp tục tham chiếu ví đó. Lịch sử không thêm dòng thu do sửa số dư đầu [R30, B29].

## 15. NFR (Performance, Concurrency Bounds)

Web ưu tiên iPhone/Android và chỉ dùng khi có Internet. Nhiều phiên của cùng tài khoản có thể hoạt động đồng thời; kết quả đã báo lưu phải bền sau tải lại. Chưa có ngưỡng độ trễ, số bản ghi hay phiên bản trình duyệt cụ thể được Ninh chốt. [R06, R11, R14, R56; S05]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1` Đổi tên ví đã có giao dịch; lịch sử cũ hiện tên mới, vẫn gắn đúng ví và tài khoản khác không sửa được.
- `AC2` Sửa số dư đầu từ 50 xuống -20 VND cho ví có thu 100 làm số dư hiện tại từ 150 xuống 80; tải lại vẫn đúng, không sinh khoản thu.
- `AC3` Số dư đầu không nguyên bị từ chối; số dư âm hợp lệ; không cho sửa trực tiếp số dư hiện tại.
- `AC4` Ví ngừng đổi tên được; nếu sửa số dư đầu làm hiện tại khác 0 thì bị chặn và yêu cầu bật lại trước (#17).

AC là điều kiện quan sát được để PM/QA rà; chưa có test thực chạy, chưa `QA_PASS`.

- `AC5` Đổi tên ví thành trống bị từ chối; sau tải lại tên ví và lịch sử cũ giữ nguyên. [R76](../../docs/requirements/46-step-9-wallet-edit-and-return.md)

## 17. Figma Mapping / Visual References

Ảnh ứng dụng cũ chỉ là tham khảo theo R02; chưa có Figma/màn hình được Ninh chốt. BA mô tả điểm vào và trạng thái quan sát được, còn bố cục/câu chữ do UX đề xuất và PM kiểm trước triển khai.

## 18. Dependencies (Cross-feature / Cross-story)

#10 để kiểm lịch sử giao dịch cũ sau đổi tên và số dư; #06/#07 để có ví. BA có thể đặc tả trước tích hợp.

Ngoài phạm vi: Tạo ví #06/#07; ngừng/bật lại #17/#18; xóa ví #19; lịch sử #10. Không sửa giao dịch trong lát này.

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md). Quy tắc sản phẩm đã có nguồn `R30, R40, R44; B01, B10, B15, B26, B29, B31`; các câu hỏi còn mở là câu hỏi/đề xuất, **chưa được Ninh xác nhận**. Không mở gate chỉ vì đã viết spec.

R76 đã chốt nhánh tên trống khi sửa; tên trùng và giới hạn độ dài chưa được Ninh chốt, không thêm AC riêng.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Bản nháp bước 9 cho Story #16; chờ PM rà BA, Tech thiết kế và reviewer độc lập. | [Issue nguồn](../capmoney-overall/issues/16-sua-thong-tin-vi.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
| 16/10/2026 | PM rà bản sau quyết định và chỉnh nguồn; `BA_READY` cho Story này, `TECH_READY` còn chờ. | [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) |
