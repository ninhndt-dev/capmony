# Feature Spec: Story 32 – Chặn bản cũ của lần chuyển

**BA gate hiện hành (16/10/2026):** `BA_READY` sau CapmmPM `/root` đối chiếu độc lập nguồn, BR/AC và 20 mục; xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md). Thiết kế kỹ thuật và `TECH_READY` còn chờ.

**Story:** [story.md](story.md) · **BA gate tại lúc lập bản nháp:** chưa PM review; được thay bằng gate hiện hành ở trên.

## 1. Executive Summary / User Story statement

Là chủ tài khoản dùng hai điện thoại, tôi muốn sửa, xóa hoặc khôi phục **cùng lần chuyển** từ bản cũ bị chặn như một đơn vị để giữ hai ví nhất quán; phần vừa nhập được giữ cho tôi tự đối chiếu. [B30](../../docs/requirements/core-business.md), [F22](../../docs/requirements/user-flows.md); [issue #32](../capmoney-overall/issues/32-chan-ban-cu-lan-chuyen.md).

## 2. Actor & Permission Matrix

Chỉ chủ tài khoản ghi chép được thao tác và đọc lần chuyển/ví của mình. Tài khoản khác và tài khoản quản lý không có quyền xem/sửa dữ liệu tài chính. [R08, R12, R51](../../docs/requirements/00-intake.md).

## 3. Screen Definition & Entry Points

Từ form sửa lần chuyển #14 hoặc xóa/khôi phục #15 trên hai điện thoại. Thông báo xung đột phải cho người dùng xem phần vừa nhập và bản mới để so sánh; bố cục cụ thể chưa chốt. [F22]

## 4. Main Flows (Happy Path)

1. A và B mở cùng lần chuyển X→Y tại cùng bản.
2. A sửa/xóa/khôi phục và được xác nhận. B thử thao tác từ bản cũ.
3. B bị chặn, được báo dữ liệu đổi và tải lại; phần B nhập được giữ để tự nhập lại hoặc bỏ. Lần chuyển bản A và số dư **cả hai ví** giữ nguyên. [B30, F22, R51]

## 5. Alternative & Exceptional Flows

- A đổi số tiền hoặc ví, B xóa bản cũ: B không được xóa nửa lần chuyển. A xóa, B sửa bản cũ: B bị chặn; muốn sửa bản đã xóa phải khôi phục trước. [B22, B30]
- Hai lần chuyển **khác nhau** được sửa gần đồng thời thuộc #30; không chặn chỉ vì chung một ví. [B27]
- Nếu có ví ngừng, B13/B20 áp dụng khi B tự nhập lại trên bản mới; không dùng xung đột để bỏ qua điều kiện ví.

## 6. Field-level Specifications & Validations

Các trường sửa của #14: ngày, số tiền nguyên VND dương, ví gửi/nhận khác nhau và đang sử dụng khi đổi, ghi chú. Phiên bản bản cũ ở mức **một lần chuyển liên kết**, không phải riêng vế trừ/cộng. Cách lưu phiên bản là Tech quyết định. [B02, B08–B09, B14, R51]

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.CONFLICT_SCOPE = cùng lần chuyển của cùng chủ`; `PARAM.TRANSFER_ATOMICITY = hai ví như một đơn vị`; `PARAM.RESOLUTION = tự đối chiếu`. Không có API/version token do Ninh duyệt.

## 8. Business Rules (`BR-*`)

- `BR-32-01` **Ninh đã xác nhận:** thao tác từ bản cũ bị chặn cho **toàn bộ** lần chuyển. Ví dụ A sửa chuyển X→Y từ 20.000 thành 30.000, B từ bản 20.000 xóa → B bị chặn, X/Y vẫn phản ánh 30.000. [B30, R51]
- `BR-32-02` **Ninh đã xác nhận:** giữ phần B vừa nhập, cho tự so sánh/nhập lại/bỏ, không hợp nhất. Ví dụ B nhập ghi chú “học phí” trước khi A đổi ví nhận; sau xung đột, chữ vẫn thấy trong phiên nhưng chưa Lưu. [F22]
- `BR-32-03` **Ninh đã xác nhận:** xóa/khôi phục tác động hai ví như một đơn vị; thao tác từ bản cũ không tạo nửa lần chuyển. [R55, B30] **Ví dụ:** A xóa lần chuyển X→Y; B khôi phục từ bản cũ bị chặn, số dư X/Y cùng giữ theo trạng thái mới của A.

## 9. State Machine & Transition Rules (`STATE.*`)

`STATE.CURRENT(transfer,v)` → A lưu bản mới `v+1`; B còn `STATE.STALE(v)` → B gửi → `STATE.CONFLICT`, không đổi v+1 và hai số dư. Sau tải v+1, B quyết định nhập lại hoặc bỏ. v là ký hiệu BA, không chốt schema.

## 10. Data Fallback & Offline Handling

Không sửa/xóa/khôi phục offline. Khi mất phản hồi, không kết luận thao tác thành công hay thất bại trước đối chiếu F09/#34/#35. [R11, F09]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.TRANSFER_CHANGED`: mã BA đề xuất cho bản cũ, báo cần tải lại và giữ phần nhập; không báo đã lưu. Nội dung hiển thị cụ thể do UX/Tech đề xuất. [B30, F22]

## 12. Security & Privacy Constraints

Kiểm chủ ở lần chuyển và cả hai ví tại đường xử lý; không lộ dữ liệu ví người khác qua báo xung đột hoặc bản mới. [R08, R12]

## 13. Third-party Integrations

Không áp dụng dịch vụ ngoài. Cơ chế kiểm bản cũ/atomicity thuộc Tech design.

## 14. Audit Log & History Requirements

Thao tác B bị chặn không tạo dòng lịch sử mới, không đổi lần chuyển đã lưu. Lịch sử chung/lọc ví vẫn một dòng đủ X→Y theo F17/R60. [F17, R60]

## 15. NFR (Performance, Concurrency Bounds)

Kiểm hai phiên gửi gần đồng thời trên cùng lần chuyển; một bản cũ không được vượt kiểm tra và ghi nửa vế. Không có SLA số giây do Ninh chốt. [B30, R51, R56]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: A/B cùng mở lần chuyển 20.000 X→Y; A sửa thành 30.000 và được xác nhận, B xóa từ bản 20.000 → B thấy báo thay đổi/cần tải lại, chuyển còn 30.000, hai số dư theo 30.000. [B30, R51]
- `AC2`: A đổi ví nhận của lần chuyển rồi lưu; B từ bản cũ sửa ghi chú → B bị chặn, phần ghi chú B vừa nhập vẫn thấy để tự đối chiếu; không có vế chuyển cũ/mới trộn nhau. [B30, F22]
- `AC3`: A xóa/khôi phục lần chuyển, B từ bản cũ khôi phục/xóa → B bị chặn, lịch sử và hai ví theo duy nhất bản A. [B30, R55]
- `AC4`: B tải bản mới rồi tự nhập lại hoặc bỏ, không tự hợp nhất. [F22]
- `AC5`: tài khoản khác/quản lý không xem hoặc thao tác lần chuyển/ví thử qua UI hoặc đường trực tiếp. [R08, R12]

## 17. Figma Mapping / Visual References

Không có Figma đã duyệt; ảnh CapMoney chỉ tham khảo theo R02. Bố cục so sánh do UX đề xuất.

## 18. Dependencies (Cross-feature / Cross-story)

`HARD` #14 và #15 theo [issue #32](../capmoney-overall/issues/32-chan-ban-cu-lan-chuyen.md); #30 phân biệt hai lần chuyển khác nhau; #34/#35 đối chiếu khi kết quả chưa rõ.

## 19. Q&A Log & Resolved Decisions

[Q&A](qna.md): B30/F22 và R51/R55 đã chốt hành vi; không có câu hỏi Ninh chặn BA. Phát hiện bản cũ, atomicity và màn so sánh là Tech/UX cần review.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA bước 9; chưa PM review/gate. | [Issue #32](../capmoney-overall/issues/32-chan-ban-cu-lan-chuyen.md), B30, F22, R51/R55 |
| 16/10/2026 | PM rà bản sau quyết định và chỉnh nguồn; `BA_READY` cho Story này, `TECH_READY` còn chờ. | [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) |
