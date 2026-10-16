# Feature Spec: Story 12 – Xóa mềm và khôi phục khoản thu/chi

**Story:** [story.md](story.md)  
**BA gate:** chưa đánh giá; bản nháp từ nguồn bước 8, chờ CapmmPM đối chiếu. Không có `BA_READY`/`TECH_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản, tôi xác nhận trước khi xóa mềm khoản thu/chi, xem ở mục đã xóa và khôi phục bất cứ lúc nào để số dư được tính lại đúng. Nguồn: [Story nguồn #12](../capmoney-overall/issues/12-xoa-va-khoi-phuc-thu-chi.md), [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

Chủ tài khoản ghi chép đã đăng nhập được thao tác dữ liệu tài chính của chính mình; tài khoản ghi chép khác và tài khoản quản lý không được đọc hay sửa. Người chưa đăng nhập không được truy cập. [R08, R12; quyền bước 3]

## 3. Screen Definition & Entry Points

Từ khoản thu/chi của mình trong lịch sử #10, chọn xóa và thấy bước xác nhận. Mục đã xóa riêng liệt kê khoản để khôi phục. Chưa chốt bố cục/câu chữ cụ thể. [F19, R50]

## 4. Main Flows (Happy Path)

1. Chủ mở khoản còn hiệu lực, chọn xóa, xác nhận; khoản rời lịch sử thường, vào mục đã xóa và ngừng tác động số dư. [F19, R33, R50]
2. Chủ mở mục đã xóa, chọn khôi phục; khoản trở lại lịch sử thường và tác động lại số dư, kể cả sau thời gian dài. [R26, R37, B01]
3. Đọc lại dữ liệu và số dư sau tải trang, không có bản trùng.

## 5. Alternative & Exceptional Flows

- Hủy tại hộp xác nhận: khoản và số dư không đổi. [F19]
- Bản đã xóa không sửa trực tiếp; cần khôi phục trước. [B22]
- Nếu xóa/khôi phục sẽ làm ví đang ngừng có số dư khác 0, chặn và hướng dẫn bật lại trước; không thay đổi nửa chừng. [B13]
- Thao tác từ bản cũ của khoản trên điện thoại khác bị chặn; kiểm tích hợp #31. [B30]
- Không có xóa vĩnh viễn từng giao dịch ở bản đầu. [R46]

## 6. Field-level Specifications & Validations

| Thành phần | Quy tắc |
| --- | --- |
| Xác nhận xóa | Có bước hỏi trước khi thực hiện; hủy không đổi dữ liệu. [F19] |
| Mục đã xóa | Chỉ bản xóa mềm của chủ; riêng lịch sử thường. [R50, R12] |
| Khôi phục | Không giới hạn thời gian; bản trở lại hiệu lực. [R37] |

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.RESTORE_WINDOW = không giới hạn thời gian`; `PARAM.DELETE_KIND = xóa mềm`, không có thao tác xóa vĩnh viễn từng khoản. [R37, R46]

## 8. Business Rules (`BR-*`)

- `BR-12-01` **Ninh đã xác nhận:** Xóa chỉ sau xác nhận; sau xóa khoản không ảnh hưởng B01 và không ở lịch sử thường. Ví đầu 0, thu 100: xóa thu đưa số dư về 0. [F19, R33, R50] **Ví dụ:** Ví đầu 0 có thu 100: xác nhận xóa làm số dư về 0, hủy xác nhận vẫn 100.
- `BR-12-02` **Ninh đã xác nhận:** Khôi phục tác động lại một lần vào đúng ví, không tạo khoản mới. Ví 0 có thu đã xóa 100: khôi phục trở lại số dư 100. [R26, B01] **Ví dụ:** Khôi phục khoản thu 100 đã xóa đưa đúng ví từ 0 về 100 một lần.
- `BR-12-03` **Ninh đã xác nhận:** Bản đã xóa phải khôi phục trước khi sửa; không xóa vĩnh viễn từng khoản. [B22, R46] **Ví dụ:** Khoản ở mục đã xóa không mở sửa và không có nút xóa vĩnh viễn từng khoản.
- `BR-12-04` **Ninh đã xác nhận:** Ví ngừng phải giữ số dư 0; xóa/khôi phục làm lệch 0 bị chặn cho tới khi bật lại. [B13] **Ví dụ:** Ví ngừng 0 có thu 100 và chi 100: xóa khoản chi bị chặn vì sẽ làm ví thành +100.

## 9. State Machine & Transition Rules (`STATE.*`)

`còn hiệu lực → yêu cầu xác nhận → đã xóa mềm → khôi phục → còn hiệu lực`; hủy xác nhận giữ `còn hiệu lực`. Không có trạng thái xóa vĩnh viễn cho từng khoản [R46].

## 10. Data Fallback & Offline Handling

Không xóa/khôi phục offline [R11]. Nếu kết quả sau thao tác chưa rõ, không tự tuyên bố thành công; Tech cần hợp đồng đối chiếu có thể kiểm chứng với B01/R50. Không suy F25 tự áp cho xóa/khôi phục vì F25 nói về Lưu giao dịch; trường hợp này phải được Tech/PM làm rõ.

## 11. Error Messages & UI Registry (`ERR.*`)

Từ chối khi chưa xác nhận, không thuộc chủ, bản cũ hoặc thao tác làm ví ngừng khác 0; bản/số dư giữ nguyên. Câu chữ và UI cần UX đề xuất; không lộ dữ liệu người khác.

## 12. Security & Privacy Constraints

Mọi thao tác đọc/ghi phải kiểm chủ sở hữu ở phía hệ thống, gồm cả khi nhập trực tiếp đường dẫn hoặc định danh thuộc tài khoản khác. Tài khoản quản lý chỉ quản lý lời mời/hỗ trợ, không có quyền dữ liệu tài chính. [R08, R12; quyền bước 3]

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho lát cắt này: đây là thao tác trên dữ liệu tài chính của tài khoản ghi chép. Nền tảng lưu trữ/API do Tech thiết kế sau BA gate; không suy từ bản nháp này.

## 14. Audit Log & History Requirements

Giữ khoản gốc trong trạng thái xóa mềm để khôi phục vô thời hạn và giữ lịch sử đúng [R37, R50]; Tech xác định dữ liệu kiểm chứng, không suy xóa vật lý. Bản xóa mềm vẫn tính là từng có giao dịch khi xét xóa ví #19 [B19].

## 15. NFR (Performance, Concurrency Bounds)

Web ưu tiên iPhone/Android và chỉ dùng khi có Internet. Nhiều phiên của cùng tài khoản có thể hoạt động đồng thời; kết quả đã báo lưu phải bền sau tải lại. Chưa có ngưỡng độ trễ, số bản ghi hay phiên bản trình duyệt cụ thể được Ninh chốt. [R06, R11, R14, R56; S05]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1` Hủy xác nhận xóa giữ nguyên khoản và số dư; xác nhận xóa đưa khoản vào mục đã xóa, rời lịch sử thường và loại tác động số dư.
- `AC2` Khôi phục khoản xóa mềm bất cứ lúc nào đưa đúng khoản trở lại lịch sử, số dư tính lại một lần; tải lại vẫn đúng.
- `AC3` Không sửa khoản trong mục đã xóa; không có xóa vĩnh viễn từng khoản; khoản đã xóa vẫn cản xóa ví theo B19.
- `AC4` Xóa/khôi phục làm ví ngừng lệch 0 bị chặn đến khi bật lại; tài khoản khác/quản lý không thao tác dữ liệu của chủ.

AC là điều kiện quan sát được để PM/QA rà; chưa có test thực chạy, chưa `QA_PASS`.

## 17. Figma Mapping / Visual References

Ảnh ứng dụng cũ chỉ là tham khảo theo R02; chưa có Figma/màn hình được Ninh chốt. BA mô tả điểm vào và trạng thái quan sát được, còn bố cục/câu chữ do UX đề xuất và PM kiểm trước triển khai.

## 18. Dependencies (Cross-feature / Cross-story)

#10 cho danh sách; #11 cho kiểm không sửa trực tiếp bản đã xóa. Không chặn BA chuẩn bị spec.

Ngoài phạm vi: Xóa/khôi phục lần chuyển #15; xóa ví #19; chặn bản cũ #31; lưu chưa rõ cho tạo thu/chi #33/#35. Không gộp xóa vĩnh viễn tài khoản #27.

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md). Quy tắc sản phẩm đã có nguồn `R26, R33, R37, R46, R50; B01, B13, B22, B30; F19`; các câu hỏi còn mở là câu hỏi/đề xuất, **chưa được Ninh xác nhận**. Không mở gate chỉ vì đã viết spec.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Bản nháp bước 9 cho Story #12; chờ PM rà BA, Tech thiết kế và reviewer độc lập. | [Issue nguồn](../capmoney-overall/issues/12-xoa-va-khoi-phuc-thu-chi.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
