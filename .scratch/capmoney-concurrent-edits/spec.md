# Feature Spec: Story 30 – Giữ hai sửa đổi khác nhau gần đồng thời

**Story:** [story.md](story.md) · **BA gate:** bản nháp, chờ PM review; chưa `BA_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản dùng hai điện thoại, tôi muốn sửa hai giao dịch **khác nhau** gần đồng thời mà cả hai thay đổi đã báo lưu đều còn và số dư đúng. [B27](../../docs/requirements/core-business.md); [issue #30](../capmoney-overall/issues/30-giu-hai-sua-doi-dong-thoi.md).

## 2. Actor & Permission Matrix

Chỉ chủ tài khoản ghi chép sửa/đọc giao dịch của mình từ hai phiên. Tài khoản ghi chép khác và quản lý không sửa/đọc dữ liệu tài chính này. [R08, R12, R56](../../docs/requirements/00-intake.md).

## 3. Screen Definition & Entry Points

Điểm vào là form sửa thu/chi #11 và sửa chuyển #14 trên hai điện thoại. Story kiểm kết quả đồng thời; không thêm form hoặc trường. Lịch sử để đối chiếu do #10/#13 cung cấp.

## 4. Main Flows (Happy Path)

1. Cùng tài khoản có giao dịch T1 và T2 khác nhau; A sửa T1, B sửa T2 trong hai phiên.
2. Cả hai yêu cầu Lưu chồng thời gian. Nếu cả hai báo thành công, tải lại thấy T1 và T2 đều ở bản mới.
3. Số dư các ví liên quan phản ánh đủ hai thay đổi từ dữ liệu sau sửa. [B01, B27]

## 5. Alternative & Exceptional Flows

- Hai điện thoại sửa **cùng** một giao dịch từ bản cũ: #31/#32 chặn bản cũ, không áp B27 như hai giao dịch khác nhau. [B18, B30]
- Nếu sửa làm ví ngừng khác 0 hoặc đổi sang ví ngừng, áp B13/B20/B21; đồng thời không vượt kiểm tra hợp lệ.
- Nếu một thao tác chưa chắc đã lưu, không báo thành công; nhánh đối chiếu theo F09/#33–#35.

## 6. Field-level Specifications & Validations

Các trường được phép sửa ở #11/#14: thu/chi gồm loại, ngày, tiền, ví, danh mục, ghi chú; chuyển gồm ngày, tiền, hai ví, ghi chú. Ngày không tương lai, tiền nguyên VND dương, ví hợp lệ. [B03, B07–B09, B14]

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.EDIT_SCOPE = hai giao dịch khác nhau cùng chủ`; `PARAM.CURRENCY = VND`. Chưa có thời gian chồng yêu cầu hay SLA số giây được Ninh chốt; Tech chứng minh bằng thử đồng thời có kiểm soát.

## 8. Business Rules (`BR-*`)

- `BR-30-01` **Ninh đã xác nhận:** sửa hai giao dịch khác nhau được báo lưu đều được giữ. Ví dụ ví đầu 100.000, T1 thu 20.000 và T2 chi 10.000; A sửa T1 thành thu 30.000, B sửa T2 thành chi 15.000, cả hai thành công → ví 115.000. [B01, B27]
- `BR-30-02` **Ninh đã xác nhận:** chuyển tiền sửa như một đơn vị ảnh hưởng đủ các ví cũ/mới; không có nửa lần chuyển. [R51, B08, B27]
- `BR-30-03` **Ninh đã xác nhận:** bản cũ của **cùng giao dịch** bị chặn theo B30, không ghi đè. [B18, B30]

## 9. State Machine & Transition Rules (`STATE.*`)

T1 và T2 có vòng đời sửa độc lập `CURRENT → SUBMITTED → SAVED/UNKNOWN`. Nếu cả hai `SAVED`, đọc lại cả hai phiên bản mới và số dư đủ hai delta. Với cùng T, `STALE` chuyển sang trạng thái cần tải lại theo #31/#32.

## 10. Data Fallback & Offline Handling

Không sửa offline. Mất phản hồi sau Lưu giữ trạng thái chưa rõ theo F09; không thực hiện lại tự do khi chưa đối chiếu. [R11, F09]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.EDIT_STALE` thuộc #31/#32; `ERR.EDIT_UNCONFIRMED` là nhãn BA đề xuất cho thao tác chưa chắc kết quả. Không thêm câu chữ sản phẩm đã được Ninh chốt.

## 12. Security & Privacy Constraints

Kiểm chủ cho giao dịch và mọi ví liên quan ở thời điểm lưu. Hai phiên cùng chủ không cấp quyền tới dữ liệu của chủ khác. [R08, R12]

## 13. Third-party Integrations

Không áp dụng tích hợp ngoài riêng; giao dịch và số dư thuộc nơi lưu CapMoney. Cơ chế đồng thời do Tech thiết kế.

## 14. Audit Log & History Requirements

Giao dịch T1/T2 sau sửa hiện bản mới trong lịch sử theo #10/#13; không tạo thêm dòng chỉ vì sửa. Số dư sau từng dòng chưa cần. [B07–B08, B17]

## 15. NFR (Performance, Concurrency Bounds)

Thử hai điện thoại/phiên cùng chủ với thao tác sửa chồng thời gian, gồm thu/chi và chuyển. Không có cam kết độ trễ hoặc nhiều hơn hai phiên do Ninh chốt. [R56, B27, S05](../../docs/requirements/goal-and-scope.md).

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: ví đầu 100.000, T1 thu 20.000, T2 chi 10.000; A sửa T1 thành 30.000, B sửa T2 thành 15.000 gần đồng thời; nếu cả hai báo lưu, tải lại T1=30.000, T2=15.000, ví=115.000 VND. [B01, B27]
- `AC2`: một thao tác sửa thu/chi và một sửa chuyển **khác nhau** cùng ảnh hưởng một ví; sau cả hai xác nhận, lịch sử và số dư mọi ví phản ánh đủ cả hai delta, không có nửa lần chuyển. [B01, B27, R51]
- `AC3`: hai sửa đổi trên cùng giao dịch từ bản cũ đi theo #31/#32; không ghi đè bản mới. [B18, B30]
- `AC4`: tài khoản khác/quản lý không sửa hoặc đọc T1/T2 qua UI hay đường xử lý trực tiếp. [R08, R12]

## 17. Figma Mapping / Visual References

Không có Figma được duyệt; ảnh cũ chỉ tham khảo theo R02. Dùng form sửa ở #11/#14.

## 18. Dependencies (Cross-feature / Cross-story)

`HARD` #11 và #14 theo [issue #30](../capmoney-overall/issues/30-giu-hai-sua-doi-dong-thoi.md); #10/#13 cấp lịch sử; #31/#32 cho trường hợp cùng bản; #33–#35 cho kết quả chưa rõ.

## 19. Q&A Log & Resolved Decisions

[Q&A](qna.md): B27 và B30 đã phân biệt giao dịch khác nhau/cùng giao dịch. Không còn câu hỏi sản phẩm chặn bản BA; hợp đồng đồng thời và số dư nguyên tử là `TECH_GAP`.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA bước 9; chưa PM review/gate. | [Issue #30](../capmoney-overall/issues/30-giu-hai-sua-doi-dong-thoi.md), B01/B27/B30 |
