# Feature Spec: Story 10 – Lịch sử chung và lọc theo ví

**BA gate hiện hành (16/10/2026):** `BA_READY` sau CapmmPM `/root` đối chiếu độc lập nguồn, BR/AC và 20 mục; xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md). Thiết kế kỹ thuật và `TECH_READY` còn chờ.

**Story:** [story.md](story.md)  
**BA gate tại lúc lập bản nháp:** chưa review; được cập nhật bởi dòng gate hiện hành ở trên.

## 1. Executive Summary / User Story statement

Là chủ tài khoản ghi chép, tôi xem khoản thu/chi của mọi ví theo ngày và lọc một ví để kiểm lại dòng tiền và số dư hiện tại. Lát này chỉ hiện dữ liệu đã lưu; chuyển tiền được bổ sung ở #13. Nguồn: [Story nguồn #10](../capmoney-overall/issues/10-lich-su-chung-va-loc-vi.md), [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

Chủ tài khoản ghi chép đã đăng nhập được thao tác dữ liệu tài chính của chính mình; tài khoản ghi chép khác và tài khoản quản lý không được đọc hay sửa. Người chưa đăng nhập không được truy cập. [R08, R12; quyền bước 3]

## 3. Screen Definition & Entry Points

Từ khu vực lịch sử chung, chủ tài khoản xem tất cả khoản thu/chi còn hiệu lực; bộ lọc chọn một ví của mình. Một ví đang ngừng vẫn có lịch sử cũ theo B04. Bố cục danh sách và cách đặt bộ lọc chưa chốt.

## 4. Main Flows (Happy Path)

1. Có từ hai ví và các khoản thu/chi đã lưu, mở lịch sử chung: thấy giao dịch còn hiệu lực của tất cả ví.
2. Chọn ví A: chỉ thấy khoản thuộc A, không mất dữ liệu ví B; bỏ lọc quay lại lịch sử chung. [F15]
3. Ngày giao dịch mới nhất đứng trước; cùng ngày bản tạo sau đứng trước. Mỗi dòng thể hiện số tiền; số dư **hiện tại** của ví đọc lại đúng B01 sau tải lại. [F16, R59, B17]

## 5. Alternative & Exceptional Flows

- Danh sách không có giao dịch hoặc ví không có giao dịch: trạng thái trống, không hiện dữ liệu ví khác.
- Chọn ví đã ngừng: vẫn đọc lịch sử cũ, không cho phép dùng nó cho giao dịch mới trong lát này. [B04]
- Giao dịch đã xóa mềm thuộc mục riêng của #12, không nằm trong lịch sử thông thường. [R50]
- Chuyển tiền xuất hiện một dòng trong lịch sử chung/lọc cả ví gửi và nhận sau #13; không nhận đây là AC độc lập của #10. [F17, R60]

## 6. Field-level Specifications & Validations

| Thành phần | Quy tắc đã xác nhận | Chi tiết còn để thiết kế |
| --- | --- | --- |
| Bộ lọc ví | Một ví của chính tài khoản hoặc tất cả ví; ví ngừng vẫn đọc lịch sử. [F15, B04] | Kiểu điều khiển và trạng thái trống. |
| Ngày giao dịch | Ngày mới nhất trước; cùng ngày theo thời điểm tạo, tạo sau đứng trước. Sửa không đổi vị trí trong cùng ngày. [F16, R59] | Định dạng ngày/giờ hiển thị. |
| Dòng thu/chi | Hiện số tiền của từng giao dịch; số dư hiện tại của ví đọc riêng. [B17] | Cách trình bày dấu thu/chi. |

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.HISTORY_ORDER = ngày giao dịch giảm dần, rồi thứ tự tạo giảm dần trong cùng ngày`; sửa không đổi khóa thứ tự tạo. `PARAM.HISTORY_BALANCE = số dư hiện tại`, không có số dư sau từng dòng. [R59, B17]

## 8. Business Rules (`BR-*`)

- `BR-10-01` **Ninh đã xác nhận:** Lịch sử chung gồm khoản thu/chi còn hiệu lực của mọi ví thuộc chủ; lọc ví chỉ còn giao dịch thuộc ví đó. Ví A có thu 100, ví B có chi 20: lọc A không thấy chi 20. [F15, R12] **Ví dụ:** A có thu 100 VND, B có chi 20 VND; lọc A chỉ hiện khoản thu 100.
- `BR-10-02` **Ninh đã xác nhận:** Ngày mới hơn trước; trong một ngày, giao dịch tạo sau trước. Tạo X rồi Y cùng ngày: Y đứng trên X dù X được sửa sau. [F16, R59] **Ví dụ:** Tạo X rồi Y cùng ngày, Y hiện trên X; sửa X không đưa X lên trên.
- `BR-10-03` **Ninh đã xác nhận:** Mỗi dòng có số tiền; số dư hiện tại của ví tính theo B01, không yêu cầu số dư sau từng dòng. Ví đầu 0 thu 100 chi 20: số dư hiện tại 80. [B01, B17] **Ví dụ:** Ví đầu 0, thu 100 và chi 20 thì số dư hiện tại 80; dòng thu không cần hiện số dư sau dòng.
- `BR-10-04` **Ninh đã xác nhận:** Giao dịch xóa mềm không ở lịch sử thường; ví ngừng vẫn đọc lịch sử cũ. [R50, B04] **Ví dụ:** Khoản thu đã xóa không ở danh sách thường; ví ngừng vẫn đọc được khoản cũ.

## 9. State Machine & Transition Rules (`STATE.*`)

`đã lưu/còn hiệu lực → hiển thị` trong lịch sử thường; `đã xóa mềm → không hiển thị` tại đây và được đọc ở mục #12; `ví hoạt động/ngừng → vẫn đọc được lịch sử`. Thứ tự tạo không đổi khi sửa [R59].

## 10. Data Fallback & Offline Handling

Không dùng offline [R11]. Khi tải lịch sử lỗi, không hiển thị dữ liệu cũ như bản cập nhật đã xác nhận; cách báo lỗi cụ thể để UX/Tech đề xuất. Tải lại khi có Internet phải đọc dữ liệu đã lưu; tự tải trên điện thoại thứ hai thuộc #28. [F06]

## 11. Error Messages & UI Registry (`ERR.*`)

Không được lộ lịch sử/số dư của tài khoản khác hoặc cho lọc bằng định danh ví người khác; trạng thái từ chối cần rõ nhưng câu chữ chưa chốt. Lỗi tải dữ liệu không được biến thành trạng thái “không có giao dịch”.

## 12. Security & Privacy Constraints

Mọi thao tác đọc/ghi phải kiểm chủ sở hữu ở phía hệ thống, gồm cả khi nhập trực tiếp đường dẫn hoặc định danh thuộc tài khoản khác. Tài khoản quản lý chỉ quản lý lời mời/hỗ trợ, không có quyền dữ liệu tài chính. [R08, R12; quyền bước 3]

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho lát cắt này: đây là thao tác trên dữ liệu tài chính của tài khoản ghi chép. Nền tảng lưu trữ/API do Tech thiết kế sau BA gate; không suy từ bản nháp này.

## 14. Audit Log & History Requirements

Không tạo/sửa/xóa giao dịch trong #10; đọc lịch sử không làm đổi bản ghi hoặc số dư. Thứ tự tạo cần bảo toàn qua sửa theo R59. Mô hình lưu chi tiết thuộc Tech.

## 15. NFR (Performance, Concurrency Bounds)

Web ưu tiên iPhone/Android và chỉ dùng khi có Internet. Nhiều phiên của cùng tài khoản có thể hoạt động đồng thời; kết quả đã báo lưu phải bền sau tải lại. Chưa có ngưỡng độ trễ, số bản ghi hay phiên bản trình duyệt cụ thể được Ninh chốt. [R06, R11, R14, R56; S05]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1` Lịch sử chung đọc được thu/chi còn hiệu lực của hai ví thuộc cùng tài khoản; lọc ví A chỉ còn khoản của A, bỏ lọc thấy cả hai.
- `AC2` Ngày giao dịch mới trước; hai khoản cùng ngày thì khoản tạo sau đứng trước và vị trí không đổi khi chỉ sửa khoản trước.
- `AC3` Dòng hiển thị số tiền; số dư hiện tại của mỗi ví đọc lại bằng B01 sau tải trang, không cần số dư sau từng dòng.
- `AC4` Ví ngừng vẫn đọc được lịch sử cũ; bản xóa mềm không ở lịch sử thường; tài khoản khác và quản lý không đọc được lịch sử.

AC là điều kiện quan sát được để PM/QA rà; chưa có test thực chạy, chưa `QA_PASS`.

## 17. Figma Mapping / Visual References

Ảnh ứng dụng cũ chỉ là tham khảo theo R02; chưa có Figma/màn hình được Ninh chốt. BA mô tả điểm vào và trạng thái quan sát được, còn bố cục/câu chữ do UX đề xuất và PM kiểm trước triển khai.

## 18. Dependencies (Cross-feature / Cross-story)

#08 và #09 là dữ liệu thu/chi để kiểm lịch sử tích hợp; không chặn BA viết đặc tả.

Ngoài phạm vi: Sửa thu/chi #11, xóa/khôi phục #12, chuyển tiền và một dòng chuyển #13, tự tải điện thoại thứ hai #28. Bản này không chốt số dư sau từng dòng (B17).

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md). Quy tắc sản phẩm đã có nguồn `R07, R59–R60; B01, B17; F15–F17`; các câu hỏi còn mở là câu hỏi/đề xuất, **chưa được Ninh xác nhận**. Không mở gate chỉ vì đã viết spec.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Bản nháp bước 9 cho Story #10; chờ PM rà BA, Tech thiết kế và reviewer độc lập. | [Issue nguồn](../capmoney-overall/issues/10-lich-su-chung-va-loc-vi.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
| 16/10/2026 | PM rà bản sau quyết định và chỉnh nguồn; `BA_READY` cho Story này, `TECH_READY` còn chờ. | [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) |
