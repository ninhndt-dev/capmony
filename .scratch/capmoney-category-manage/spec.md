# Feature Spec: CM-20 – Thêm và đổi tên danh mục

**Story:** [story.md](story.md) · **Nguồn chia lát:** [CM-20](../capmoney-overall/issues/20-them-va-doi-ten-danh-muc.md)  
**BA gate:** bản BA bước 9; chưa được PM review, chưa có `BA_READY`.

## 1. Executive Summary / User Story statement

Chủ tài khoản cần thêm danh mục riêng và đổi tên danh mục đã có; các giao dịch cũ hiện tên mới mà vẫn thuộc đúng danh mục. [R32, R45, R54, B05, B16; nghiệp vụ](../../docs/requirements/core-business.md).

## 2. Actor & Permission Matrix

| Actor | Quyền | Nguồn |
| --- | --- | --- |
| Chủ tài khoản ghi chép | Thêm/đổi tên danh mục của mình và chọn danh mục cho khoản thu/chi | R12, R32, B05 |
| Tài khoản khác; quản lý; khách | Không xem hoặc sửa danh mục của chủ | R08, R12; quyền bước 3 |

## 3. Screen Definition & Entry Points

Từ phần danh mục của tài khoản ghi chép, có thao tác thêm và đổi tên danh mục. Tên mới xuất hiện trong bộ chọn thu/chi và lịch sử. **BA đề xuất:** form một ô tên và phản hồi lỗi tại chỗ; bố cục/copy chưa được Ninh duyệt riêng.

## 4. Main Flows (Happy Path)

1. Chủ thêm danh mục mới vào danh sách riêng; danh mục dùng chung cho thu/chi và chọn được khi ghi giao dịch. [R32, R45]
2. Chủ đổi tên một danh mục đã dùng; các giao dịch cũ của chủ hiển thị tên mới sau tải lại, liên kết danh mục không mất. [R54, B05]
3. Danh mục đổi tên vẫn chọn được cho khoản thu/chi mới. [R49, B05]

## 5. Alternative & Exceptional Flows

- Danh mục của người khác hoặc phiên không hợp lệ không sửa được; yêu cầu trực tiếp cũng bị chặn. [R12]
- Tên rỗng/không có nội dung sau chuẩn hóa không nên lưu theo **đề xuất BA/Tech**, nhưng chính sách chiều dài, trùng tên và chuẩn hóa cần PM/Tech chốt; không tự coi tên trùng là lỗi sản phẩm.
- Đổi tên đồng thời với ghi giao dịch không làm mất liên kết danh mục; lỗi lưu không được báo thành công sớm. Đây là điều kiện nhất quán Tech cần chứng minh.

## 6. Field-level Specifications & Validations

Trường tên danh mục do chủ nhập khi thêm/đổi. Định danh danh mục phải giữ ổn định qua đổi tên để giao dịch cũ hiện tên mới. Bộ mười danh mục ban đầu thuộc CM-08; CM-20 dùng cùng danh sách chung thu/chi của chủ. [B05, B16, R45, R54]

## 7. System Parameters & Constants (`PARAM.*`)

Không có ngưỡng độ dài, số danh mục, hoặc quy tắc tên duy nhất do Ninh chốt. Tech/PM cần chọn giới hạn biểu diễn và kiểm nhập phù hợp, ghi rõ nếu ảnh hưởng trải nghiệm. Không tự tạo `PARAM` sản phẩm giả.

## 8. Business Rules (`BR-*`)

- `BR-20-01`: danh mục của mỗi chủ là riêng nhưng dùng chung cho thu và chi. **Ví dụ:** A thêm “Đi lại”, A chọn nó cho cả thu và chi; B không thấy “Đi lại” của A. [R12, R32, R45]
- `BR-20-02`: đổi tên danh mục đã dùng cập nhật tên hiển thị của giao dịch cũ mà không đổi liên kết. **Ví dụ:** khoản chi gắn “Đi lại”, sau đổi thành “Di chuyển” thì lịch sử hiện “Di chuyển”. [R54, B05]
- `BR-20-03`: danh mục đã dùng vẫn chọn được cho giao dịch mới. **Ví dụ:** “Di chuyển” trong ví dụ trên có trong bộ chọn khoản thu và chi. [R49, B05]

## 9. State Machine & Transition Rules (`STATE.*`)

`chưa có danh mục riêng → danh mục đã thêm → tên đã đổi`, cùng một danh mục giữ định danh và lịch sử sử dụng. Đổi tên không phải xóa/tạo lại; xóa danh mục chưa dùng thuộc CM-21. [B05, B11, R54]

## 10. Data Fallback & Offline Handling

Không thêm/đổi tên offline trong bản đầu. Nếu mất phản hồi, ứng dụng đối chiếu danh sách của chủ trước khi báo kết quả; không làm xuất hiện hai danh mục vì một thao tác lặp theo **đề xuất an toàn Tech**. [R11]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.CATEGORY_UNAVAILABLE` và `ERR.CATEGORY_NAME_INVALID` là mã **đề xuất**, không phải copy được Ninh chốt. Lỗi không tiết lộ danh mục của tài khoản khác; tên cũ giữ nguyên nếu đổi tên thất bại.

## 12. Security & Privacy Constraints

Mọi đường thêm/đổi/đọc lấy chủ từ phiên server; không tin owner ID gửi từ trình duyệt. Tài khoản quản lý không có quyền đọc danh mục/dữ liệu tài chính của người khác. [R12; quyền bước 3]

## 13. Third-party Integrations

Không áp dụng dịch vụ ngoài riêng; thao tác dùng dữ liệu nội bộ. Tech chọn hợp đồng lưu trữ sau gate BA.

## 14. Audit Log & History Requirements

Lịch sử giao dịch phải hiện tên mới cho giao dịch cũ. Ninh chưa yêu cầu nhật ký riêng cho các lần đổi tên; log vận hành nếu cần là thiết kế Tech và không mang dữ liệu nhạy cảm không cần thiết. [R54]

## 15. NFR (Performance, Concurrency Bounds)

Bản đầu khoảng 10 người, web iPhone/Android; chưa có SLA riêng. Đổi tên và ghi giao dịch gần đồng thời không được tạo tham chiếu danh mục bị mất; Tech cần chứng minh. [R05, R14, B05]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: chủ thêm danh mục riêng và chọn được danh mục đó cho khoản thu và chi mới sau tải lại. [R32, R45; CM-20]
- `AC2`: đổi tên danh mục đã dùng làm giao dịch cũ hiện tên mới sau tải lại; giao dịch vẫn gắn đúng danh mục. [R54, B05]
- `AC3`: danh mục đã đổi tên vẫn chọn được cho giao dịch mới. [R49, B05]
- `AC4`: tài khoản khác, quản lý và khách không xem/đổi tên danh mục của chủ dù gọi đường xử lý trực tiếp. [R12]

## 17. Figma Mapping / Visual References

Không áp dụng Figma vì chưa có thiết kế được duyệt; ảnh cũ chỉ tham khảo. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

CM-08 cấp mười danh mục và đường chọn chung; CM-10 cấp lịch sử để kiểm tên mới. CM-21 phụ thuộc trạng thái đã/chưa dùng danh mục; không được xóa danh mục đã dùng. [CM-20](../capmoney-overall/issues/20-them-va-doi-ten-danh-muc.md)

## 19. Q&A Log & Resolved Decisions

**Ninh đã xác nhận:** R32/R45/R49/R54 và B05/B16. **Chưa rõ:** tên trùng, giới hạn/chuẩn hóa tên và câu chữ lỗi; xem [Q&A](qna.md). Các chi tiết này không cho phép áp đặt quy tắc tên duy nhất khi chưa được duyệt.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA 20 mục/AC1–AC4; chờ PM review | [CM-20](../capmoney-overall/issues/20-them-va-doi-ten-danh-muc.md), [nghiệp vụ](../../docs/requirements/core-business.md), [handoff](handoff.md) |
