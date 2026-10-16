# Feature Spec: Story 33 – Đối chiếu Lưu thu/chi chưa rõ trong phiên

**Story:** [story.md](story.md) · **BA gate:** bản nháp, chờ PM review; chưa `BA_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản vừa bấm Lưu khoản thu/chi mà mất phản hồi, tôi muốn thấy kết quả chưa rõ, giữ nội dung trong phiên và được tự đối chiếu khi có Internet lại, để không bị báo thành công sai hoặc tạo khoản trùng. [F09](../../docs/requirements/user-flows.md), [R64](../../docs/requirements/00-intake.md); [issue #33](../capmoney-overall/issues/33-doi-chieu-luu-thu-chi-chua-ro-trong-phien.md).

## 2. Actor & Permission Matrix

Chỉ chủ tài khoản ghi chép xem trạng thái Lưu, đối chiếu và đọc khoản thu/chi cùng số dư của mình. Tài khoản khác và quản lý không xem ý định hoặc kết quả tài chính. [R08, R12]

## 3. Screen Definition & Entry Points

Từ biểu mẫu thu #08 hoặc chi #09 **sau khi đã bấm Lưu**, trong cùng phiên web. Trạng thái chờ có thể thấy khi quay lại biểu mẫu/trang liên quan nếu người dùng đã rời đi; vị trí và câu chữ do UX đề xuất. [R64]

## 4. Main Flows (Happy Path)

1. Người dùng nhập khoản thu/chi hợp lệ, bấm Lưu; kết nối/ phản hồi mất trước khi biết hệ thống đã ghi hay chưa.
2. UI báo **chưa rõ/đang kiểm tra**, giữ nội dung trong phiên, không báo thành công và không cho Lưu lặp **đúng thao tác còn chờ**. [F09, R64]
3. Khi có Internet, ứng dụng tự đối chiếu đúng thao tác. Nếu chắc đã lưu, báo đã lưu và đọc đúng một giao dịch/số dư; nếu chắc chưa lưu, báo chưa lưu để người dùng có thể thử lại; nếu vẫn chưa rõ, tiếp tục trạng thái chờ. [F09, R64]

## 5. Alternative & Exceptional Flows

- Người dùng có Internet được rời biểu mẫu làm việc khác. Khi quay lại, nếu còn chưa rõ thì thấy đang kiểm tra; nếu đã xác định thì thấy kết quả đã lưu/chưa lưu. [R64]
- Mất Internet lâu không biến “chưa rõ” thành “chắc chưa lưu” chỉ do hết thời gian; timeout phía người dùng không chứng minh yêu cầu cũ sẽ không được ghi sau đó. Đây là ràng buộc Tech để giữ F09.
- Hai thao tác **khác nhau** đã báo lưu vẫn phải được giữ theo B24/#29; chặn đúng ý định đang chờ không chặn mọi giao dịch khác của tài khoản.
- Đóng trình duyệt khi còn chưa rõ chuyển sang #35/F25; Story này chỉ kiểm trong phiên.

## 6. Field-level Specifications & Validations

Trường thu/chi hợp lệ do #08/#09 định nghĩa. Đối chiếu phải phân biệt **đúng thao tác đã bấm Lưu**, không suy từ số tiền/ngày trùng nhau rằng hai ý định là một; cách định danh là Tech. Trạng thái kết quả gồm chưa rõ, chắc đã lưu, chắc chưa lưu. [F09, F25]

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.CURRENCY = VND`; `PARAM.OUTCOME = {UNKNOWN, SAVED, NOT_SAVED_CONFIRMED}`; `PARAM.DEDUP_SCOPE = đúng thao tác`. Không có thời gian timeout để tự xác nhận thất bại do Ninh chốt. [F09, R64]

## 8. Business Rules (`BR-*`)

- `BR-33-01` **Ninh đã xác nhận:** chưa biết kết quả thì báo chưa rõ, giữ nội dung trong phiên. Ví dụ bấm Lưu thu 120.000 rồi mất mạng; UI không hiện “đã lưu” và form vẫn giữ 120.000. [F09]
- `BR-33-02` **Ninh đã xác nhận:** tự đối chiếu khi có mạng, chặn Lưu lặp đúng thao tác khi còn chờ. Ví dụ bấm Lưu hai lần cho cùng ý định thu 120.000 khi đang kiểm tra → tối đa một khoản được đọc lại. [F09, R64]
- `BR-33-03` **Ninh đã xác nhận:** người dùng được làm việc khác khi có Internet; quay lại thấy trạng thái/kết quả của thao tác. Ví dụ rời form để xem ví rồi quay lại khi còn chờ → vẫn thấy đang kiểm tra. [R64]
- `BR-33-04` **Ninh đã xác nhận:** chỉ báo đã lưu khi đã xác nhận; số dư không bị cộng/trừ hai lần. [B01, F09]

## 9. State Machine & Transition Rules (`STATE.*`)

`STATE.SUBMITTED_UNKNOWN` sau Lưu mất phản hồi → khi có Internet tự đối chiếu → `STATE.SAVED` hoặc `STATE.NOT_SAVED_CONFIRMED` **chỉ với bằng chứng chắc**; nếu chưa đủ bằng chứng tiếp tục `STATE.SUBMITTED_UNKNOWN`. Chặn retry của cùng ý định trong `UNKNOWN`; rời/quay lại trong phiên không xóa trạng thái. Cách chứng minh terminal âm là `TECH_GAP`.

## 10. Data Fallback & Offline Handling

Bản đầu không ghi offline. Khi mất mạng, giữ nội dung trong phiên và trạng thái chưa rõ; có Internet lại thì tự đối chiếu trước khi cho thử lại. Đóng trình duyệt khi chưa rõ áp F25/#35, khác F21 đóng **trước Lưu**. [R11, F09, F21, F25]

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.SAVE_UNKNOWN`: “kết quả chưa rõ/đang kiểm tra”; `ERR.SAVE_NOT_CONFIRMED`: chỉ dùng khi chắc chưa lưu. Mã và câu chữ là BA/UX đề xuất; phải phân biệt với thông báo đã lưu. [F09, R64]

## 12. Security & Privacy Constraints

Trạng thái thao tác phải gắn với chủ tài khoản và đúng ý định; không để tài khoản khác xem nội dung/kết quả hoặc đối chiếu hộ. Không tin chỉ dấu “thành công” do trình duyệt tự suy. [R08, R12]

## 13. Third-party Integrations

Không có dịch vụ ngoài riêng. Nơi lưu/kênh đối chiếu do Tech thiết kế; không chốt schema/API từ bản BA.

## 14. Audit Log & History Requirements

Nếu đã lưu, lịch sử có đúng một khoản thu/chi; nếu chắc chưa lưu, không có khoản từ ý định ấy. Bản ghi chưa rõ không được hiển thị như giao dịch đã lưu. Lịch sử chung thuộc #10. [F09, B01]

## 15. NFR (Performance, Concurrency Bounds)

Chứng minh chống trùng cả khi phản hồi mất, request tới muộn, retry/gần đồng thời của cùng ý định. Không có SLA xác định kết quả do Ninh chốt; không ép timeout thành thất bại. Thử trên web iPhone/Android theo S05. [F09, B24]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: bấm Lưu thu 120.000 hoặc chi 70.000 rồi làm mất phản hồi; khi chưa biết kết quả, UI báo chưa rõ, giữ nội dung trong phiên, không báo đã lưu. [F09]
- `AC2`: khi Internet trở lại, ứng dụng tự đối chiếu **đúng thao tác**; nếu đã lưu thì chỉ một giao dịch và số dư đổi một lần; bấm Lưu lặp thao tác đang chờ không tạo bản thứ hai. [F09, R64, B01]
- `AC3`: nếu có bằng chứng chắc chưa lưu thì báo chưa lưu; nếu request cũ vẫn có thể ghi sau đó thì vẫn báo chưa rõ, không cho thử lại như một ý định mới dẫn tới trùng. [F09]
- `AC4`: người dùng có Internet rời form làm việc khác rồi quay lại; trạng thái chưa rõ hoặc kết quả đã/chưa lưu hiển thị đúng theo việc đối chiếu đã xong chưa. [R64]
- `AC5`: hai giao dịch **khác nhau** của cùng chủ không bị coi là một chỉ vì cùng số tiền; chúng được kiểm theo #29. Tài khoản khác/quản lý không xem trạng thái hoặc nội dung. [B24, R08, R12]

## 17. Figma Mapping / Visual References

Không có Figma duyệt; ảnh cũ chỉ tham khảo. UX đề xuất nhãn ba trạng thái, không tự coi là câu chữ Ninh đã chốt. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

`HARD` #08/#09 theo [issue #33](../capmoney-overall/issues/33-doi-chieu-luu-thu-chi-chua-ro-trong-phien.md); #10 cho lịch sử; #29 cho giao dịch độc lập; #35 cho tiếp tục sau đóng/mở. Hợp đồng ý định/đối chiếu và bằng chứng “chắc chưa lưu” cần Tech review trước `TECH_READY`.

## 19. Q&A Log & Resolved Decisions

[Q&A](qna.md): F09/R64 đã chốt hành vi trong phiên. Không có câu hỏi sản phẩm chặn bản BA; cách giữ định danh, xử lý request muộn và chứng minh kết quả âm là `TECH_GAP`.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA bước 9; chưa PM review/gate. | [Issue #33](../capmoney-overall/issues/33-doi-chieu-luu-thu-chi-chua-ro-trong-phien.md), F09/R64, B01/B24 |
