# Feature Spec: Story 29 – Giữ giao dịch mới gần đồng thời

**BA gate hiện hành (16/10/2026):** `BA_READY` sau CapmmPM `/root` đối chiếu độc lập nguồn, BR/AC và 20 mục; xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md). Thiết kế kỹ thuật và `TECH_READY` còn chờ.

**Story:** [story.md](story.md) · **BA gate tại lúc lập bản nháp:** chưa PM review; được thay bằng gate hiện hành ở trên.

## 1. Executive Summary / User Story statement

Là một người dùng trên hai điện thoại, tôi muốn hai giao dịch **khác nhau** được lưu gần đồng thời đều tồn tại nếu đã báo thành công, với số dư đúng. Bao phủ thu, chi và chuyển tiền. [B24](../../docs/requirements/core-business.md); [issue #29](../capmoney-overall/issues/29-giu-giao-dich-moi-dong-thoi.md).

## 2. Actor & Permission Matrix

Chỉ chủ tài khoản ghi chép được tạo và đọc các giao dịch của mình trên A/B. Tài khoản khác và quản lý không đọc/tạo dữ liệu tài chính của chủ. [R08, R12, R56](../../docs/requirements/00-intake.md).

## 3. Screen Definition & Entry Points

Hai phiên web đã đăng nhập cùng tài khoản mở form thu/chi (#08/#09) hoặc chuyển (#13). Story kiểm kết quả khi cùng bấm Lưu; không tạo màn nhập mới. Lịch sử/số dư đọc lại do #10/#13 cung cấp.

## 4. Main Flows (Happy Path)

1. A và B chuẩn bị hai **ý định giao dịch khác nhau** hợp lệ có thể tác động cùng một ví.
2. Cả hai bấm Lưu gần đồng thời. Nếu cả hai được báo đã lưu, cả hai bản ghi phải đọc lại được sau tải trang.
3. Số dư mỗi ví bằng số dư đầu cộng/trừ đủ hai tác động, kể cả chuyển tiền tác động hai ví như một đơn vị. [B01, B24, R51]

## 5. Alternative & Exceptional Flows

- Một giao dịch được xác nhận, giao dịch kia còn chưa rõ: chỉ giao dịch được xác nhận mới được báo thành công; giao dịch còn chờ theo #33/#34, không suy ra thất bại. [F09]
- Hai lần gửi của **cùng ý định** không được tính là hai giao dịch độc lập; đối chiếu/chống trùng thuộc #33/#34. [F09]
- Ví bị ngừng hoặc quyền thay đổi trước khi Lưu: yêu cầu không hợp lệ bị từ chối; không dùng B24 để vượt B04/R12.

## 6. Field-level Specifications & Validations

Trường và kiểm tra của thu/chi ở #08/#09; chuyển ở #13. Số tiền là nguyên VND >0, ngày không tương lai, ví thuộc chủ và đang sử dụng; chuyển dùng hai ví khác nhau, phí 0. [B02, B04, B09, R42–R43]

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.TRANSACTION_SCOPE = cùng chủ, hai ý định khác nhau`; `PARAM.CURRENCY = VND`; `PARAM.TRANSFER_FEE = 0`. “Gần đồng thời” là trường hợp hai yêu cầu chồng thời gian, chưa có ngưỡng mili giây do Ninh chốt; Tech thiết kế kiểm thử cạnh tranh thực.

## 8. Business Rules (`BR-*`)

- `BR-29-01` **Ninh đã xác nhận:** hai giao dịch khác nhau được báo lưu đều được giữ. Ví dụ ví đầu 100.000, A ghi thu 40.000, B ghi chi 30.000; cả hai thành công thì thấy hai bản, số dư 110.000. [B01, B24]
- `BR-29-02` **Ninh đã xác nhận:** lần chuyển là một đơn vị trừ ví gửi/cộng ví nhận. Ví dụ A chuyển 20.000 từ X sang Y, B ghi thu 10.000 vào X; cả hai thành công thì X thay đổi -10.000, Y +20.000. [R51, B01, B24]
- `BR-29-03` **Ninh đã xác nhận:** chỉ bản đã được xác nhận lưu mới được báo thành công; mất phản hồi phải đối chiếu, không nhân đôi. [F09] **Ví dụ:** A bấm Lưu chi 30.000 VND rồi mất phản hồi; UI báo chưa rõ, đối chiếu trước khi kết luận, không tạo hai khoản chi.

## 9. State Machine & Transition Rules (`STATE.*`)

Mỗi ý định ở `STATE.PENDING` rồi riêng rẽ thành `STATE.SAVED` hoặc `STATE.UNKNOWN`; nếu cả hai `SAVED`, kết quả cộng đủ cả hai. Không có trạng thái hợp lệ “đã báo lưu nhưng mất bản”. Trạng thái chưa rõ xử lý ở #33/#34.

## 10. Data Fallback & Offline Handling

Không hỗ trợ tạo giao dịch offline. Mất mạng giữa Lưu và phản hồi giữ kết quả chưa rõ và tự đối chiếu khi có mạng theo F09; không xem timeout là thất bại chắc chắn.

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.SAVE_UNCONFIRMED`: mã BA đề xuất cho kết quả chưa rõ, không báo thành công. Lỗi trường/ví theo #08/#09/#13. Câu chữ và thời điểm hiển thị do UX/Tech đề xuất.

## 12. Security & Privacy Constraints

Mọi yêu cầu phải xét chủ tài khoản của từng ví/giao dịch; tính đồng thời không được trộn dữ liệu người khác. Quản lý không xem tài chính. [R08, R12]

## 13. Third-party Integrations

Không áp dụng dịch vụ ngoài riêng; nơi lưu và hợp đồng ghi nguyên tử thuộc Tech design.

## 14. Audit Log & History Requirements

Sau hai xác nhận, lịch sử thể hiện hai giao dịch riêng theo #10/#13; số dư hiện tại phản ánh cả hai, chưa cần số dư sau từng dòng. [B01, B17]

## 15. NFR (Performance, Concurrency Bounds)

Kiểm bằng hai phiên/điện thoại cho cùng tài khoản, kể cả hai yêu cầu chồng thời gian trên cùng ví. Không có SLA hay số lượng song song lớn hơn đã chốt. [R56, B24, S05](../../docs/requirements/goal-and-scope.md).

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: ví 100.000 VND, A ghi thu 40.000 và B ghi chi 30.000 gần đồng thời; nếu cả hai báo đã lưu, sau tải lại thấy hai giao dịch và số dư 110.000 VND. [B01, B24]
- `AC2`: X=100.000, Y=0; A chuyển 20.000 X→Y, B ghi thu 10.000 vào X gần đồng thời; cả hai báo lưu thì X=90.000, Y=20.000, lịch sử có một dòng chuyển và một dòng thu. [B01, B24, R51, F17]
- `AC3`: nếu một thao tác chưa có kết quả chắc, UI không báo thành công cho nó; đối chiếu thuộc #33/#34 và không được tạo trùng. [F09]
- `AC4`: tài khoản khác hoặc quản lý không xem hay tạo giao dịch vào các ví thử, kể cả gửi yêu cầu trực tiếp. [R08, R12]

## 17. Figma Mapping / Visual References

Không có Figma đã duyệt; ảnh cũ chỉ tham khảo. Story không bổ sung màn hình riêng. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

`HARD` #08 và #13 theo [issue #29](../capmoney-overall/issues/29-giu-giao-dich-moi-dong-thoi.md); #09 cho ví dụ thu/chi, #10 để kiểm lịch sử; #33/#34 cho kết quả chưa rõ. Không giao Dev trước hợp đồng ghi và số dư được Tech review.

## 19. Q&A Log & Resolved Decisions

[Q&A](qna.md): B24 đã chốt bảo toàn hai giao dịch khác nhau; không có câu hỏi Ninh chặn bản BA. Cơ chế atomic/idempotency là `TECH_GAP` cần chứng minh.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA bước 9; chưa PM review/gate. | [Issue #29](../capmoney-overall/issues/29-giu-giao-dich-moi-dong-thoi.md), B01/B24, F09 |
| 16/10/2026 | PM rà bản sau quyết định và chỉnh nguồn; `BA_READY` cho Story này, `TECH_READY` còn chờ. | [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) |
