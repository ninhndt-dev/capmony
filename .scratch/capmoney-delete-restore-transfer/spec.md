# Feature Spec: Story 15 – Xóa mềm và khôi phục lần chuyển

**Story:** [story.md](story.md)  
**BA gate:** chưa đánh giá; bản nháp từ nguồn bước 8, chờ CapmmPM đối chiếu. Không có `BA_READY`/`TECH_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản, tôi xác nhận xóa mềm hoặc khôi phục cả lần chuyển như một đơn vị, để số dư hai ví và lịch sử không bị lệch. Nguồn: [Story nguồn #15](../capmoney-overall/issues/15-xoa-va-khoi-phuc-lan-chuyen.md), [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

Chủ tài khoản ghi chép đã đăng nhập được thao tác dữ liệu tài chính của chính mình; tài khoản ghi chép khác và tài khoản quản lý không được đọc hay sửa. Người chưa đăng nhập không được truy cập. [R08, R12; quyền bước 3]

## 3. Screen Definition & Entry Points

Từ một dòng chuyển còn hiệu lực trong lịch sử #13, chọn xóa và xác nhận; trong mục đã xóa riêng chọn lần chuyển để khôi phục. Bố cục/câu chữ chưa chốt.

## 4. Main Flows (Happy Path)

1. Chủ chọn xóa một lần chuyển còn hiệu lực; hỏi xác nhận trước. Xác nhận làm cả tác động gửi/nhận ngừng cùng lúc, chuyển một dòng sang mục đã xóa. [F19, R55, R50]
2. Chủ khôi phục bất cứ lúc nào; cả hai ví lại nhận đúng tác động một lần và lịch sử thường có một dòng. [R26, R37, R55]
3. Sau tải lại số dư cả hai ví theo B01.

## 5. Alternative & Exceptional Flows

- Hủy xác nhận giữ nguyên lần chuyển và hai số dư. [F19]
- Nếu xóa/khôi phục làm một ví đang ngừng lệch 0, chặn toàn thao tác tới khi bật lại. [B13]
- Bản đã xóa không sửa trước khôi phục; bản cũ trên máy khác bị chặn (#32). [B22, B30]
- Không xóa vĩnh viễn từng lần chuyển. [R46]

## 6. Field-level Specifications & Validations

| Thành phần | Quy tắc |
| --- | --- |
| Xác nhận xóa | Hỏi trước khi xóa cả lần chuyển. [F19] |
| Mục đã xóa | Một dòng/lần chuyển với cả ví gửi và ví nhận; không còn ở lịch sử thường. [R50, F17] |
| Khôi phục | Không giới hạn thời gian, toàn tác động hai ví. [R37, R55] |

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.RESTORE_WINDOW = không giới hạn`; `PARAM.DELETE_KIND = xóa mềm`; phí lần chuyển vẫn 0 [R37, R46, R42].

## 8. Business Rules (`BR-*`)

- `BR-15-01` **Ninh đã xác nhận:** Xóa sau xác nhận ngừng tác động cả hai ví. A=20, B=80 sau chuyển 30 từ A sang B: xóa đưa A=50, B=50. [R55, B01] **Ví dụ:** Sau chuyển 30, A=20/B=80; xóa hoàn A=50/B=50 cùng lúc.
- `BR-15-02` **Ninh đã xác nhận:** Khôi phục tác động lại đúng một lần lên cả hai ví và giữ một dòng lịch sử. [R55, F17] **Ví dụ:** Khôi phục lần chuyển ấy trở lại A=20/B=80 và vẫn chỉ một dòng lịch sử.
- `BR-15-03` **Ninh đã xác nhận:** Ví ngừng phải giữ 0; nếu tác động xóa/khôi phục làm lệch 0 thì chặn toàn bộ. [B13] **Ví dụ:** Nếu xóa làm ví ngừng 0 thành 30, toàn thao tác bị chặn.
- `BR-15-04` **Ninh đã xác nhận:** Bản xóa mềm không sửa trực tiếp, không xóa vĩnh viễn từng giao dịch. [B22, R46] **Ví dụ:** Lần chuyển trong mục đã xóa không sửa được và không có xóa vĩnh viễn từng lần.

## 9. State Machine & Transition Rules (`STATE.*`)

`còn hiệu lực → xác nhận → xóa mềm → khôi phục → còn hiệu lực`; hủy xác nhận không đổi. Không có trạng thái một phía bị xóa/một phía còn hiệu lực [R55].

## 10. Data Fallback & Offline Handling

Không xóa/khôi phục offline [R11]. Nếu kết quả request chưa rõ, không báo đã xóa/khôi phục trước khi xác minh; Tech phải thiết kế đối chiếu cho thao tác này. Không suy F25 (Lưu giao dịch) bao phủ đủ xóa/khôi phục.

## 11. Error Messages & UI Registry (`ERR.*`)

Lỗi quyền, bản cũ, ví ngừng hoặc trạng thái bản không hợp lệ phải giữ cả hai số dư và trạng thái lần chuyển. Không cho một phía thành công.

## 12. Security & Privacy Constraints

Mọi thao tác đọc/ghi phải kiểm chủ sở hữu ở phía hệ thống, gồm cả khi nhập trực tiếp đường dẫn hoặc định danh thuộc tài khoản khác. Tài khoản quản lý chỉ quản lý lời mời/hỗ trợ, không có quyền dữ liệu tài chính. [R08, R12; quyền bước 3]

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho lát cắt này: đây là thao tác trên dữ liệu tài chính của tài khoản ghi chép. Nền tảng lưu trữ/API do Tech thiết kế sau BA gate; không suy từ bản nháp này.

## 14. Audit Log & History Requirements

Giữ định danh của lần chuyển trong trạng thái xóa mềm để khôi phục bất cứ lúc nào, và để xóa ví #19 biết ví đã từng có chuyển kể cả bản xóa. [R37, B19]

## 15. NFR (Performance, Concurrency Bounds)

Web ưu tiên iPhone/Android và chỉ dùng khi có Internet. Nhiều phiên của cùng tài khoản có thể hoạt động đồng thời; kết quả đã báo lưu phải bền sau tải lại. Chưa có ngưỡng độ trễ, số bản ghi hay phiên bản trình duyệt cụ thể được Ninh chốt. [R06, R11, R14, R56; S05]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1` Hủy xác nhận xóa giữ nguyên hai ví; xác nhận xóa chuyển một lần chuyển sang mục đã xóa, không còn ở lịch sử thường và đảo tác động cả hai ví.
- `AC2` Khôi phục sau bất kỳ thời gian nào đưa lại đúng một dòng lịch sử và tính tác động cả hai ví một lần; tải lại vẫn đúng B01.
- `AC3` Không sửa lần chuyển đã xóa trước khôi phục; bản xóa vẫn tính là lịch sử từng dùng ví khi xét xóa ví #19.
- `AC4` Nếu bất kỳ ví ngừng nào sẽ lệch 0 vì xóa/khôi phục thì chặn toàn thao tác; tài khoản khác/quản lý không thao tác được.

AC là điều kiện quan sát được để PM/QA rà; chưa có test thực chạy, chưa `QA_PASS`.

## 17. Figma Mapping / Visual References

Ảnh ứng dụng cũ chỉ là tham khảo theo R02; chưa có Figma/màn hình được Ninh chốt. BA mô tả điểm vào và trạng thái quan sát được, còn bố cục/câu chữ do UX đề xuất và PM kiểm trước triển khai.

## 18. Dependencies (Cross-feature / Cross-story)

#13 tạo lần chuyển; #14 xác định không sửa bản đã xóa. Phụ thuộc Dev/tích hợp.

Ngoài phạm vi: Xóa/khôi phục thu/chi #12; tạo/sửa chuyển #13/#14; xóa ví #19; bản cũ #32. Không có xóa vĩnh viễn từng lần chuyển.

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md). Quy tắc sản phẩm đã có nguồn `R26, R33, R37, R46, R50–R51, R55; B01, B13, B22, B30; F17, F19`; các câu hỏi còn mở là câu hỏi/đề xuất, **chưa được Ninh xác nhận**. Không mở gate chỉ vì đã viết spec.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Bản nháp bước 9 cho Story #15; chờ PM rà BA, Tech thiết kế và reviewer độc lập. | [Issue nguồn](../capmoney-overall/issues/15-xoa-va-khoi-phuc-lan-chuyen.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
