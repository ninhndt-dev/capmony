# Feature Spec: Story 11 – Sửa khoản thu/chi

**Story:** [story.md](story.md)  
**BA gate:** chưa đánh giá; bản nháp từ nguồn bước 8, chờ CapmmPM đối chiếu. Không có `BA_READY`/`TECH_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản, tôi sửa khoản thu/chi còn hiệu lực đã lưu để chữa sai loại, ngày, số tiền, ví, danh mục hoặc ghi chú; số dư các ví liên quan và lịch sử vẫn đúng. Nguồn: [Story nguồn #11](../capmoney-overall/issues/11-sua-thu-chi.md), [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

Chủ tài khoản ghi chép đã đăng nhập được thao tác dữ liệu tài chính của chính mình; tài khoản ghi chép khác và tài khoản quản lý không được đọc hay sửa. Người chưa đăng nhập không được truy cập. [R08, R12; quyền bước 3]

## 3. Screen Definition & Entry Points

Mở một khoản thu/chi còn hiệu lực từ lịch sử #10 rồi vào thao tác sửa. Form hiện giá trị đã lưu để chủ tài khoản thay đổi. Vị trí nút và câu chữ chưa chốt.

## 4. Main Flows (Happy Path)

1. Chủ tài khoản chọn khoản còn hiệu lực của mình và mở sửa.
2. Có thể đổi thu↔chi, ngày, số nguyên VND dương, ví đang sử dụng thuộc mình, danh mục của mình, ghi chú kể cả xóa nội dung. [B03, B07, B14, B20]
3. Khi Lưu thành công, đọc lại khoản và số dư mọi ví cũ/mới theo B01; sửa không làm thay thứ tự tạo của bản trong cùng ngày. [B01, R59]

## 5. Alternative & Exceptional Flows

- Ngày tương lai, tiền 0/âm/lẻ, ví hoặc danh mục người khác, ví đích đã ngừng: từ chối, giữ bản đã lưu và số dư. [R43, B02, B20, R12]
- Với giao dịch cũ gắn ví đã ngừng, đổi ngày/ghi chú/danh mục mà số dư vẫn 0 thì được; đổi tác động khiến ví ngừng khác 0 phải bật lại trước. [B13, B21]
- Giao dịch đã xóa mềm phải khôi phục ở #12 trước khi sửa. [B22]
- Bản cũ từ điện thoại khác bị chặn, giữ phần nhập để đối chiếu; case tích hợp #31. [B30, F22]

## 6. Field-level Specifications & Validations

| Trường | Quy tắc |
| --- | --- |
| Loại | Thu hoặc chi, đổi được cả hai chiều. [B03] |
| Ngày | Hôm nay hoặc trước đó; không ngày tương lai. [B07, R43] |
| Số tiền | Số nguyên VND >0. [B02] |
| Ví | Thuộc chủ; nếu đổi ví thì ví mới đang sử dụng. [B20, R12] |
| Danh mục | Danh mục của chủ dùng chung thu/chi. [R25, R45] |
| Ghi chú | Tùy chọn; thêm, sửa hoặc xóa nội dung. [B12, B14] |

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.AMOUNT = số nguyên VND > 0`; `PARAM.DATE ≤ hôm nay`. Không có giới hạn số lần sửa hoặc độ dài ghi chú được Ninh chốt. [B02, R43]

## 8. Business Rules (`BR-*`)

- `BR-11-01` **Ninh đã xác nhận:** Sửa loại từ thu sang chi hoặc ngược lại hợp lệ. Ví đầu 0 có thu 100 đổi thành chi 100 thì số dư từ +100 thành -100. [B01, B03] **Ví dụ:** Ví đầu 0 có thu 100, đổi khoản ấy thành chi 100 thì số dư thành -100.
- `BR-11-02` **Ninh đã xác nhận:** Đổi ví rút tác động cũ khỏi ví A và áp tác động mới một lần vào ví B; không có trạng thái cả hai ví cùng nhận một khoản. [B01, B07] **Ví dụ:** Thu 100 ở A chuyển sang B thì A giảm 100 so với trước, B tăng 100.
- `BR-11-03` **Ninh đã xác nhận:** Giao dịch gắn ví ngừng chỉ được sửa thông tin không làm ví đó lệch 0; đổi sang ví ngừng khác bị chặn. [B13, B20–B21] **Ví dụ:** Ví ngừng 0 có khoản thu và chi bù nhau: sửa ghi chú không đổi số dư được; đổi ví để ví ngừng lệch 0 bị chặn.
- `BR-11-04` **Ninh đã xác nhận:** Sửa cùng ngày giữ thứ tự tạo; bản xóa mềm phải khôi phục trước khi sửa. [R59, B22] **Ví dụ:** Khoản X cùng ngày tạo trước Y vẫn đứng dưới Y sau sửa; X đã xóa phải khôi phục trước.

## 9. State Machine & Transition Rules (`STATE.*`)

`còn hiệu lực → đang sửa → còn hiệu lực phiên bản mới` khi Lưu được xác nhận; `đã xóa mềm → không sửa` cho đến khôi phục #12. Phiên bản cũ trên máy khác không được ghi đè [B30/#31].

## 10. Data Fallback & Offline Handling

Không cho sửa khi offline [R11]. Mất mạng sau bấm Lưu mà kết quả chưa rõ tuân F09/F25/R64, các case đối chiếu ở #33/#35. Không báo đã sửa khi chưa xác định kết quả; giữ bản nhập dở trong phiên khi bị chặn do bản cũ [F22].

## 11. Error Messages & UI Registry (`ERR.*`)

Cần phân biệt dữ liệu không hợp lệ, ví ngừng, bản đã xóa và bản cũ; câu chữ do UX đề xuất. Khi từ chối, dữ liệu/số dư đã lưu giữ nguyên. Không tiết lộ có tồn tại giao dịch của người khác.

## 12. Security & Privacy Constraints

Mọi thao tác đọc/ghi phải kiểm chủ sở hữu ở phía hệ thống, gồm cả khi nhập trực tiếp đường dẫn hoặc định danh thuộc tài khoản khác. Tài khoản quản lý chỉ quản lý lời mời/hỗ trợ, không có quyền dữ liệu tài chính. [R08, R12; quyền bước 3]

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho lát cắt này: đây là thao tác trên dữ liệu tài chính của tài khoản ghi chép. Nền tảng lưu trữ/API do Tech thiết kế sau BA gate; không suy từ bản nháp này.

## 14. Audit Log & History Requirements

Giữ định danh và thứ tự tạo của khoản khi sửa [R59]; ghi nhận giá trị mới để đọc lại và tính B01. Nhu cầu log kỹ thuật chi tiết/chính sách lưu revision thuộc Tech, chưa được chốt là chức năng người dùng.

## 15. NFR (Performance, Concurrency Bounds)

Web ưu tiên iPhone/Android và chỉ dùng khi có Internet. Nhiều phiên của cùng tài khoản có thể hoạt động đồng thời; kết quả đã báo lưu phải bền sau tải lại. Chưa có ngưỡng độ trễ, số bản ghi hay phiên bản trình duyệt cụ thể được Ninh chốt. [R06, R11, R14, R56; S05]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1` Sửa từng trường loại, ngày, số tiền, ví, danh mục và ghi chú của khoản còn hiệu lực; đổi thu↔chi và bỏ ghi chú đều đọc lại đúng.
- `AC2` Đổi loại hoặc ví làm số dư ví cũ/mới cập nhật đúng B01, không tính đôi; sửa khoản cũ không đổi vị trí trong nhóm cùng ngày.
- `AC3` Ngày tương lai, tiền không dương/không nguyên, ví/danh mục người khác hoặc ví đích đã ngừng bị từ chối; bản đã lưu và số dư giữ nguyên.
- `AC4` Khoản đã xóa mềm không sửa được trước khi khôi phục; khoản gắn ví ngừng chỉ sửa thông tin không làm số dư ví lệch 0; quyền chủ được kiểm.

AC là điều kiện quan sát được để PM/QA rà; chưa có test thực chạy, chưa `QA_PASS`.

## 17. Figma Mapping / Visual References

Ảnh ứng dụng cũ chỉ là tham khảo theo R02; chưa có Figma/màn hình được Ninh chốt. BA mô tả điểm vào và trạng thái quan sát được, còn bố cục/câu chữ do UX đề xuất và PM kiểm trước triển khai.

## 18. Dependencies (Cross-feature / Cross-story)

#10 là đường xem giao dịch và đối chiếu lịch sử; #08/#09 là nền ghi giao dịch. Phụ thuộc Dev/tích hợp, không chặn BA soạn spec.

Ngoài phạm vi: Xóa/khôi phục #12; xung đột bản cũ đầy đủ #31; đối chiếu Lưu chưa rõ #33/#35; sửa lần chuyển #14.

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md). Quy tắc sản phẩm đã có nguồn `B01–B03, B07, B12, B14, B20–B21, B30; R43, R59; F22`; các câu hỏi còn mở là câu hỏi/đề xuất, **chưa được Ninh xác nhận**. Không mở gate chỉ vì đã viết spec.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Bản nháp bước 9 cho Story #11; chờ PM rà BA, Tech thiết kế và reviewer độc lập. | [Issue nguồn](../capmoney-overall/issues/11-sua-thu-chi.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
