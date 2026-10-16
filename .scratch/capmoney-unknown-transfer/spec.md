# Feature Spec: Story 34 – Đối chiếu Lưu chuyển tiền chưa rõ trong phiên

**Story:** [story.md](story.md) · **BA gate:** bản nháp, chờ PM review; chưa `BA_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản vừa bấm Lưu lần chuyển nhưng mất phản hồi, tôi muốn tự đối chiếu khi có Internet mà không tạo thêm lần chuyển hoặc tác động chỉ một ví. [F09](../../docs/requirements/user-flows.md), [R64, R51](../../docs/requirements/00-intake.md); [issue #34](../capmoney-overall/issues/34-doi-chieu-luu-chuyen-chua-ro-trong-phien.md).

## 2. Actor & Permission Matrix

Chỉ chủ tài khoản ghi chép được xem trạng thái, đối chiếu lần chuyển và đọc số dư hai ví của mình. Quản lý/tài khoản khác không đọc hay đối chiếu dữ liệu tài chính này. [R08, R12]

## 3. Screen Definition & Entry Points

Từ form chuyển tiền #13 **sau khi bấm Lưu**, trong cùng phiên web; trạng thái vẫn có thể thấy khi quay lại sau làm việc khác. Bố cục và câu chữ do UX đề xuất. [R64]

## 4. Main Flows (Happy Path)

1. Chủ chọn hai ví đang sử dụng khác nhau, nhập số tiền nguyên VND dương, bấm Lưu; mất phản hồi trước khi biết kết quả.
2. Ứng dụng báo chưa rõ, giữ nội dung trong phiên, chặn Lưu lặp đúng lần chuyển đang chờ; người dùng có Internet được làm việc khác. [F09, R64]
3. Khi có Internet, ứng dụng tự đối chiếu. Chắc đã lưu → báo thành công, một dòng lịch sử X→Y, X trừ/Y cộng đúng một lần; chắc chưa lưu → báo chưa lưu; còn chưa rõ → tiếp tục kiểm tra. [F17, F09, R51, R60]

## 5. Alternative & Exceptional Flows

- Yêu cầu chuyển cũ có thể tới muộn: không dùng timeout hoặc chưa thấy dòng lịch sử làm bằng chứng chắc chưa lưu; cần hàng rào kỹ thuật trước khi cho retry. Đây là điều Tech phải chứng minh để đáp ứng F09.
- Hai **lần chuyển khác nhau** cùng tài khoản vẫn được giữ nếu đã báo lưu theo B24/#29; chặn chỉ đúng lần chuyển đang chờ.
- Đóng trình duyệt trước khi có kết quả chắc chuyển sang #35/F25; đóng **trước Lưu** theo F21.

## 6. Field-level Specifications & Validations

Trường của lần chuyển do #13 xác định: hai ví khác nhau của cùng chủ đang sử dụng, tiền nguyên VND >0, ngày không tương lai, phí 0, ghi chú tùy chọn. Trạng thái đối chiếu áp dụng cho **một lần chuyển liên kết**, không tách vế. [B02, B04, B09, B12, R42–R43, R51]

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.TRANSFER_FEE = 0`; `PARAM.OUTCOME = {UNKNOWN, SAVED, NOT_SAVED_CONFIRMED}`; `PARAM.DEDUP_SCOPE = đúng lần chuyển`. Không có timeout được Ninh chọn để tự phân định kết quả. [R42, F09]

## 8. Business Rules (`BR-*`)

- `BR-34-01` **Ninh đã xác nhận:** mất phản hồi thì không báo chuyển thành công sớm; giữ nội dung trong phiên. Ví dụ chuyển 50.000 X→Y rồi mất mạng, trạng thái là chưa rõ, không hiện số dư như giao dịch đã xác nhận. [F09]
- `BR-34-02` **Ninh đã xác nhận:** tự đối chiếu khi có mạng, chặn Lưu lặp đúng lần chuyển. Ví dụ gửi lặp cùng ý định 50.000 X→Y trong lúc chờ → tối đa một lần chuyển có hiệu lực. [F09, R64]
- `BR-34-03` **Ninh đã xác nhận:** lần chuyển là một đơn vị; khi đã lưu, X giảm/Y tăng một lần, lịch sử chung/lọc ví chỉ một dòng. [R51, R55, R60]
- `BR-34-04` **Ninh đã xác nhận:** khi có Internet người dùng được làm việc khác; quay lại thấy đang kiểm tra hoặc kết quả đã phân định. [R64]

## 9. State Machine & Transition Rules (`STATE.*`)

`STATE.SUBMITTED_UNKNOWN` → tự đối chiếu có Internet → `STATE.SAVED` hoặc `STATE.NOT_SAVED_CONFIRMED` chỉ khi chắc; nếu request còn có thể tới muộn, giữ `UNKNOWN`. Cả hai tác động ví phải cùng một kết quả; trạng thái “chỉ một vế đã lưu” là lỗi Tech, không phải trạng thái sản phẩm hợp lệ. [R51, F09]

## 10. Data Fallback & Offline Handling

Không chuyển offline. Mất mạng sau Lưu giữ nội dung/trạng thái trong phiên; khi có mạng, tự đối chiếu trước khi thử lại. Đóng/mở sau Lưu chưa rõ thuộc #35. [R11, F09, F25]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.TRANSFER_UNKNOWN` và `ERR.TRANSFER_NOT_SAVED_CONFIRMED` là mã BA đề xuất, phân biệt chưa rõ/chắc chưa lưu; không báo thành công trước xác nhận. Câu chữ cụ thể do UX/Tech đề xuất.

## 12. Security & Privacy Constraints

Kết quả đối chiếu gắn với chủ và **cả hai ví**; không lộ số dư/ví người khác hoặc cho họ truy vấn ý định. [R08, R12]

## 13. Third-party Integrations

Không áp dụng dịch vụ ngoài riêng. Cơ chế ghi hai vế và đối chiếu do Tech thiết kế, chưa chốt schema/API.

## 14. Audit Log & History Requirements

Nếu đã lưu, lịch sử chung đúng một dòng X→Y, lọc X hay Y cũng một dòng; nếu chưa lưu chắc, không có dòng từ ý định ấy. Không hiển thị giao dịch chờ như đã lưu. [F17, R60]

## 15. NFR (Performance, Concurrency Bounds)

Chứng minh không có bản trùng, không có nửa lần chuyển khi request/lần đối chiếu chồng nhau hoặc có crash/mất phản hồi. Không có SLA thời gian phân định; Tech cần thử fault injection trước `TECH_READY`. [R51, F09, B24]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: X=100.000, Y=0; bấm Lưu chuyển 50.000 rồi mất phản hồi; khi chưa biết kết quả, UI báo chưa rõ, giữ nội dung, không báo đã lưu và không cho Lưu lặp đúng ý định. [F09, R64]
- `AC2`: nếu hệ thống đã ghi, sau tự đối chiếu báo đã lưu, lịch sử một dòng X→Y 50.000, X=50.000/Y=50.000; thử gửi lặp đúng ý định không đổi thêm số dư. [R51, R60, F09]
- `AC3`: nếu có bằng chứng chắc chưa lưu thì báo chưa lưu, X=100.000/Y=0; nếu request cũ còn có thể ghi muộn thì giữ chưa rõ và chặn retry đúng ý định. [F09]
- `AC4`: người dùng có Internet rời form làm việc khác rồi quay lại; thấy đang kiểm tra hoặc kết quả đã/chưa lưu theo trạng thái thực. [R64]
- `AC5`: tài khoản khác/quản lý không xem ý định/lần chuyển/số dư qua UI hoặc đường trực tiếp. [R08, R12]

## 17. Figma Mapping / Visual References

Không có Figma duyệt; ảnh cũ chỉ tham khảo theo R02. UX đề xuất trạng thái ba nhánh trên form chuyển.

## 18. Dependencies (Cross-feature / Cross-story)

`HARD` #13 theo [issue #34](../capmoney-overall/issues/34-doi-chieu-luu-chuyen-chua-ro-trong-phien.md); #29 cho ý định khác nhau, #35 cho đóng/mở. Tech phải chứng minh atomicity và kết quả chắc chưa lưu trước `TECH_READY`.

## 19. Q&A Log & Resolved Decisions

[Q&A](qna.md): F09/R64, R51/R60 đã chốt hành vi. Không có câu hỏi Ninh chặn bản BA; định danh ý định, atomicity và chứng minh trạng thái âm là `TECH_GAP`.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA bước 9; chưa PM review/gate. | [Issue #34](../capmoney-overall/issues/34-doi-chieu-luu-chuyen-chua-ro-trong-phien.md), F09/R64, R51/R60 |
