# Feature Spec: Story 31 – Chặn bản cũ của khoản thu/chi

**Story:** [story.md](story.md) · **BA gate:** bản nháp, chờ PM review; chưa `BA_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản dùng hai điện thoại, tôi muốn sửa, xóa hoặc khôi phục từ bản cũ của **cùng khoản thu/chi** bị chặn để không ghi đè bản mới; phần vừa nhập được giữ cho tôi tự đối chiếu. [B18, B28, B30](../../docs/requirements/core-business.md), [F22](../../docs/requirements/user-flows.md); [issue #31](../capmoney-overall/issues/31-chan-ban-cu-thu-chi.md).

## 2. Actor & Permission Matrix

Chỉ chủ tài khoản ghi chép có quyền thử thao tác trên khoản thu/chi của mình và xem bản mới nhất. Tài khoản khác/quản lý không xem hoặc thao tác dữ liệu tài chính này. [R08, R12](../../docs/requirements/00-intake.md).

## 3. Screen Definition & Entry Points

Điểm vào là form sửa #11 và thao tác xóa/khôi phục #12 trên hai điện thoại. Khi gặp bản cũ, cần hiển thị dữ liệu mới để người dùng so sánh với phần vừa nhập, cho nhập lại trên bản mới hoặc bỏ. Bố cục so sánh chưa được Ninh chốt. [F22]

## 4. Main Flows (Happy Path)

1. A và B tải cùng bản T. A sửa/xóa/khôi phục T và được xác nhận lưu.
2. B chưa tải bản mới, thử sửa/xóa/khôi phục từ bản T cũ.
3. Hệ thống chặn thao tác B, báo dữ liệu đã thay đổi, giữ phần B vừa nhập; bản A và số dư giữ nguyên. B tải bản mới rồi tự nhập lại hoặc bỏ. [B30, F22]

## 5. Alternative & Exceptional Flows

- A sửa, B xóa bản cũ: B bị chặn theo B28. A xóa, B sửa bản còn hiệu lực cũ: B cũng bị chặn; bản đã xóa phải khôi phục trước khi sửa theo B22.
- A và B cùng sửa nhưng B tải bản mới **trước Lưu** rồi chủ động nhập lại: đây là thao tác mới trên bản hiện hành, vẫn phải qua kiểm tra bình thường; không tự hợp nhất.
- Hai giao dịch **khác nhau** sửa gần đồng thời thuộc #30; không chặn chỉ vì số dư ví thay đổi bởi giao dịch khác. [B27]

## 6. Field-level Specifications & Validations

Trường khoản thu/chi của #11: loại, ngày, số tiền, ví, danh mục, ghi chú. Bản cũ được hiểu ở mức **một giao dịch**, không phải toàn ví; cách biểu diễn phiên bản là Tech quyết định. [B03, B07, B14, B30]

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.CONFLICT_SCOPE = cùng giao dịch của cùng chủ`; `PARAM.RESOLUTION = người dùng tự đối chiếu, không hợp nhất`. Không có hạn giữ bản nhập hoặc mã phiên bản kỹ thuật do Ninh chốt. [B30, F22]

## 8. Business Rules (`BR-*`)

- `BR-31-01` **Ninh đã xác nhận:** thao tác sửa từ bản cũ của cùng T bị chặn. Ví dụ T thu 100.000; A sửa thành 120.000, B từ bản 100.000 sửa thành 90.000 → B nhận báo thay đổi; T vẫn 120.000. [B18, B30]
- `BR-31-02` **Ninh đã xác nhận:** xóa/khôi phục từ bản cũ cũng bị chặn. Ví dụ A sửa T, B xóa từ bản cũ → T không bị xóa; B phải tải lại. [B28, B30]
- `BR-31-03` **Ninh đã xác nhận:** giữ phần B vừa nhập để tự so sánh, nhập lại hoặc bỏ; không tự ghi đè/hợp nhất. Ví dụ B nhập ghi chú “B”, xung đột xảy ra thì “B” vẫn có thể xem trong phiên nhưng chưa được lưu. [F22]

## 9. State Machine & Transition Rules (`STATE.*`)

`STATE.CURRENT(T,v)` → A lưu `v+1`; B còn `STATE.STALE(T,v)` → B thử ghi → `STATE.CONFLICT` không đổi bản đã lưu/số dư. Sau B tải `v+1` và nhập lại, B có thể gửi một ý định mới. Ký hiệu v chỉ diễn đạt thứ tự, không chốt schema/API.

## 10. Data Fallback & Offline Handling

Không cho sửa/xóa/khôi phục offline; thiếu Internet không được xem là đã lưu. Nếu mất phản hồi lúc gửi, đối chiếu theo F09/#33/#35 trước khi quyết định bản nào mới nhất. [R11, F09]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.TRANSACTION_CHANGED`: mã BA đề xuất, báo dữ liệu đã thay đổi và cần tải lại; giữ phần nhập. Không dùng thông báo thành công cho thao tác bị chặn. Câu chữ/điểm hiển thị do UX đề xuất. [B30, F22]

## 12. Security & Privacy Constraints

Kiểm chủ giao dịch, ví và danh mục ở đường xử lý, kể cả lúc trả bản mới để so sánh; không lộ bản tài khoản khác. [R08, R12]

## 13. Third-party Integrations

Không áp dụng dịch vụ ngoài. Cơ chế phát hiện bản cũ và ghi nguyên tử là thiết kế Tech, không suy ra API/version token đã chốt.

## 14. Audit Log & History Requirements

Thao tác B bị chặn không tạo bản sửa/xóa/khôi phục trong lịch sử và không đổi số dư. Mục đã xóa/lịch sử thường do #12/#10 quản lý. Không có nhật ký kiểm toán riêng được Ninh chốt.

## 15. NFR (Performance, Concurrency Bounds)

Phải chặn cả khi A/B bấm gần đồng thời; không có cửa sổ mà cả hai cùng ghi đè thành công từ bản cũ. Mốc thời gian và cơ chế khóa do Tech chứng minh. [B18, B30, R56]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: A và B cùng mở T thu 100.000; A sửa thành 120.000 và xác nhận; B sửa từ bản cũ thành 90.000 → B thấy dữ liệu thay đổi/cần tải lại, vẫn thấy phần mình nhập; sau tải lại T=120.000 và số dư theo 120.000. [B18, F22]
- `AC2`: A sửa T, B xóa T từ bản cũ → xóa bị chặn, T vẫn còn hiệu lực và số dư không đổi so với sau A. [B28, B30]
- `AC3`: A thay đổi trạng thái T đã xóa, B khôi phục từ bản cũ → B bị chặn; không tạo bản khôi phục thứ hai hoặc ghi đè. [B30]
- `AC4`: B chọn tải bản mới và tự nhập lại thì có thể thử một thao tác mới; B chọn bỏ thì không tạo thay đổi. Không tự hợp nhất. [F22]
- `AC5`: tài khoản khác/quản lý không đọc bản mới hoặc thao tác T qua UI/đường trực tiếp. [R08, R12]

## 17. Figma Mapping / Visual References

Không có Figma được duyệt; ảnh cũ chỉ tham khảo theo R02. Màn so sánh là UX đề xuất trong phạm vi F22.

## 18. Dependencies (Cross-feature / Cross-story)

`HARD` #11 và #12 theo [issue #31](../capmoney-overall/issues/31-chan-ban-cu-thu-chi.md); #28 cho tự tải; #30 phân biệt sửa hai giao dịch khác nhau; #33/#35 cho kết quả Lưu chưa rõ.

## 19. Q&A Log & Resolved Decisions

[Q&A](qna.md): B18/B28/B30 và F22 đã chốt chặn, giữ bản nhập, tự so sánh. Không có câu hỏi Ninh chặn BA; hình thức so sánh và cơ chế version là UX/Tech.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA bước 9; chưa PM review/gate. | [Issue #31](../capmoney-overall/issues/31-chan-ban-cu-thu-chi.md), B18/B28/B30, F22 |
