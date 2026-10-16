# Feature Spec: Story 14 – Sửa lần chuyển

**Story:** [story.md](story.md)  
**BA gate:** chưa đánh giá; bản nháp từ nguồn bước 8, chờ CapmmPM đối chiếu. Không có `BA_READY`/`TECH_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản, tôi sửa lần chuyển còn hiệu lực như một đơn vị để ngày, tiền, hai ví hoặc ghi chú đúng và mọi ví liên quan có số dư chính xác. Nguồn: [Story nguồn #14](../capmoney-overall/issues/14-sua-lan-chuyen.md), [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

Chủ tài khoản ghi chép đã đăng nhập được thao tác dữ liệu tài chính của chính mình; tài khoản ghi chép khác và tài khoản quản lý không được đọc hay sửa. Người chưa đăng nhập không được truy cập. [R08, R12; quyền bước 3]

## 3. Screen Definition & Entry Points

Từ một dòng chuyển tiền của chủ trong lịch sử #13, mở thao tác sửa. Form hiện ngày, số tiền, ví gửi, ví nhận, ghi chú đã lưu. Nút/bố cục chưa chốt.

## 4. Main Flows (Happy Path)

1. Chủ mở lần chuyển còn hiệu lực và đổi ngày, tiền, ví gửi/nhận hoặc ghi chú. Hai ví mới khác nhau, đang sử dụng, cùng chủ; tiền nguyên VND dương, ngày không tương lai. [B08–B09, B20, B14]
2. Lưu thành công một lần: rút tác động cũ và áp tác động mới như một đơn vị, đối chiếu số dư mọi ví cũ/mới theo B01. Lịch sử vẫn một dòng. [B01, F17]

## 5. Alternative & Exceptional Flows

- Ví trùng nhau/ngừng/ngoài chủ, ngày tương lai, tiền không dương/lẻ: từ chối, bản gốc và mọi số dư giữ nguyên. [B02, B04, B09, B20, R43]
- Ví ngừng gắn vào lần chuyển cũ: chỉ sửa ngày/ghi chú nếu không làm ví lệch 0; nếu đổi ví phải dùng ví đang hoạt động. [B13, B21]
- Bản đã xóa mềm cần khôi phục trước khi sửa (#15); bản cũ trên điện thoại khác bị chặn (#32). [B22, B30]
- Không có phí khác 0 hoặc danh mục. [R42, B25]

## 6. Field-level Specifications & Validations

| Trường | Quy tắc |
| --- | --- |
| Ví gửi/nhận | Có thể đổi, hai ví khác nhau và đang sử dụng khi chọn mới. [B08–B09, B20] |
| Số tiền | Nguyên VND dương; có thể lớn hơn số dư ví gửi. [B02, B23] |
| Ngày | Hôm nay hoặc trước đó. [R43] |
| Ghi chú | Tùy chọn, sửa/xóa được. [B12, B14] |
| Phí/danh mục | Phí 0, không danh mục. [R42, B25] |

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.TRANSFER_FEE = 0`; `PARAM.AMOUNT = nguyên VND >0`; `PARAM.DATE ≤ hôm nay`. Không có giới hạn số lần sửa được chốt. [R42–R43, B02]

## 8. Business Rules (`BR-*`)

- `BR-14-01` **Ninh đã xác nhận:** Sửa lần chuyển là một đơn vị, không để ví cũ hoặc mới nhận nửa tác động. A→B 50 đổi thành A→C 70: B hoàn lại 50, A giảm thêm 20, C tăng 70. [B01, B08] **Ví dụ:** Đổi A→B 50 thành A→C 70: B hoàn 50, A giảm thêm 20, C tăng 70.
- `BR-14-02` **Ninh đã xác nhận:** Hai ví sau sửa khác nhau, đang sử dụng, cùng chủ; ví cũ ngừng chỉ được sửa trường không đổi số dư. [B09, B13, B20–B21] **Ví dụ:** Đổi ví nhận thành chính ví gửi A bị chặn; A/B giữ số dư cũ.
- `BR-14-03` **Ninh đã xác nhận:** Tiền nguyên VND dương, ngày không tương lai, phí luôn 0; ghi chú được thay hoặc bỏ. [B02, R42–R43, B14] **Ví dụ:** Đổi số tiền 50 thành 70 hợp lệ; đổi thành 0 hoặc ngày mai bị từ chối.
- `BR-14-04` **Ninh đã xác nhận:** Lần chuyển vẫn một dòng lịch sử và bản đã xóa không sửa trực tiếp. [F17, B22] **Ví dụ:** Bản chuyển đã xóa không mở sửa; bản còn hiệu lực vẫn hiện một dòng sau sửa.

## 9. State Machine & Transition Rules (`STATE.*`)

`còn hiệu lực → đang sửa → còn hiệu lực bản mới`; `đã xóa mềm → không sửa` tới khi khôi phục #15. Bản cũ bị chặn #32; không tạo một lần chuyển mới khi sửa.

## 10. Data Fallback & Offline Handling

Không sửa offline [R11]. Sau Lưu khi kết quả chưa rõ, không báo thành công sớm; Tech cần xác định hợp đồng đối chiếu cho sửa, không tự lấy F25 của tạo giao dịch làm quyết định đủ. Xung đột bản cũ giữ phần nhập theo F22.

## 11. Error Messages & UI Registry (`ERR.*`)

Từ chối toàn thao tác khi một ví không hợp lệ, ngày/tiền sai, ví ngừng bị ảnh hưởng, bản đã xóa hoặc bản cũ. Bản và số dư trước thao tác giữ nguyên. Không lộ ví tài khoản khác.

## 12. Security & Privacy Constraints

Mọi thao tác đọc/ghi phải kiểm chủ sở hữu ở phía hệ thống, gồm cả khi nhập trực tiếp đường dẫn hoặc định danh thuộc tài khoản khác. Tài khoản quản lý chỉ quản lý lời mời/hỗ trợ, không có quyền dữ liệu tài chính. [R08, R12; quyền bước 3]

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho lát cắt này: đây là thao tác trên dữ liệu tài chính của tài khoản ghi chép. Nền tảng lưu trữ/API do Tech thiết kế sau BA gate; không suy từ bản nháp này.

## 14. Audit Log & History Requirements

Giữ định danh một lần chuyển và lịch sử một dòng; Tech thiết kế transaction/revision để chứng minh cập nhật số dư cũ/mới nhất quán. Không suy mô hình hai giao dịch riêng [R51, F17].

## 15. NFR (Performance, Concurrency Bounds)

Web ưu tiên iPhone/Android và chỉ dùng khi có Internet. Nhiều phiên của cùng tài khoản có thể hoạt động đồng thời; kết quả đã báo lưu phải bền sau tải lại. Chưa có ngưỡng độ trễ, số bản ghi hay phiên bản trình duyệt cụ thể được Ninh chốt. [R06, R11, R14, R56; S05]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1` Sửa ngày, số tiền, ví gửi, ví nhận và ghi chú của lần chuyển còn hiệu lực; ghi chú xóa được; lần chuyển vẫn một dòng.
- `AC2` Đổi A→B 50 thành A→C 70 cập nhật chính xác số dư A/B/C theo B01 sau tải lại, không có tác động một phía.
- `AC3` Ví trùng/không thuộc chủ/đã ngừng khi chọn mới, ngày tương lai hoặc tiền không nguyên dương bị từ chối và không đổi mọi số dư.
- `AC4` Không sửa bản đã xóa trước khôi phục; thông tin không đổi số dư của lần chuyển gắn ví ngừng có thể sửa nếu ví vẫn 0; quyền chủ được kiểm.

AC là điều kiện quan sát được để PM/QA rà; chưa có test thực chạy, chưa `QA_PASS`.

## 17. Figma Mapping / Visual References

Ảnh ứng dụng cũ chỉ là tham khảo theo R02; chưa có Figma/màn hình được Ninh chốt. BA mô tả điểm vào và trạng thái quan sát được, còn bố cục/câu chữ do UX đề xuất và PM kiểm trước triển khai.

## 18. Dependencies (Cross-feature / Cross-story)

#13 cung cấp một lần chuyển và số dư hai ví để kiểm; không chặn BA soạn spec.

Ngoài phạm vi: Tạo chuyển #13; xóa/khôi phục #15; đồng thời hai sửa khác nhau #30; bản cũ #32; đối chiếu Lưu chưa rõ cho tạo lần chuyển #34/#35.

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md). Quy tắc sản phẩm đã có nguồn `B01–B02, B04, B08–B09, B12, B14, B20–B21, B30; R42–R43; F17`; các câu hỏi còn mở là câu hỏi/đề xuất, **chưa được Ninh xác nhận**. Không mở gate chỉ vì đã viết spec.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Bản nháp bước 9 cho Story #14; chờ PM rà BA, Tech thiết kế và reviewer độc lập. | [Issue nguồn](../capmoney-overall/issues/14-sua-lan-chuyen.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
