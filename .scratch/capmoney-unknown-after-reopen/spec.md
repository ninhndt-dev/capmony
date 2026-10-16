# Feature Spec: Story 35 – Đối chiếu Lưu giao dịch sau khi mở lại

**Story:** [story.md](story.md) · **BA gate:** bản nháp, chờ PM review; chưa `BA_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản đã bấm Lưu thu/chi hoặc chuyển tiền rồi đóng trình duyệt khi kết quả chưa rõ, tôi muốn mở lại cùng tài khoản có Internet để tự đối chiếu **đúng thao tác**, thấy kết quả chắc chắn hoặc tiếp tục trạng thái chờ mà không tạo giao dịch trùng. [F25](../../docs/requirements/user-flows.md), [R64](../../docs/requirements/00-intake.md); [issue #35](../capmoney-overall/issues/35-doi-chieu-luu-chua-ro-sau-mo-lai.md).

## 2. Actor & Permission Matrix

Chỉ chủ tài khoản ghi chép sau khi đăng nhập lại hoặc còn phiên hợp lệ được thấy/đối chiếu thao tác của mình. Tài khoản khác/quản lý không đọc trạng thái, nội dung hay giao dịch tài chính. [R08, R12]

## 3. Screen Definition & Entry Points

Điểm vào là mở lại web CapMoney **bằng cùng tài khoản** sau khi đã bấm Lưu và đóng trình duyệt lúc chưa rõ. Trạng thái chờ/kết quả phải có đường xem khi quay lại; vị trí thông báo và route là UX/Tech đề xuất. Không áp dụng cho bản nháp **chưa bấm Lưu** theo F21.

## 4. Main Flows (Happy Path)

1. Người dùng bấm Lưu thu/chi hoặc lần chuyển, mất kết quả rồi đóng hẳn trình duyệt.
2. Mở lại bằng cùng tài khoản có Internet; ứng dụng tự đối chiếu **đúng thao tác đó**. [F25]
3. Nếu chắc đã lưu, báo đã lưu, tải một giao dịch và số dư đúng. Nếu chắc chưa lưu, báo chưa lưu và mở form trống để nhập lại. Nếu vẫn chưa rõ, báo đang kiểm tra, cho làm việc khác có Internet và chặn Lưu lặp đúng thao tác. [F25, R64]

## 5. Alternative & Exceptional Flows

- Mở lại khi chưa có Internet: chưa thể xác nhận kết quả, không báo thành công hoặc chắc thất bại; tiếp tục đối chiếu khi có Internet. [R11, F25]
- Đăng nhập **tài khoản khác** trên cùng điện thoại: không lộ trạng thái/chi tiết ý định của chủ trước. Khi trở lại đúng tài khoản, vẫn có đường đối chiếu theo F25. Cách lưu/khôi phục định danh giữa các phiên là Tech.
- Nếu request cũ vẫn có thể đến muộn, không gọi là “chắc chưa lưu” vì chưa thấy giao dịch hoặc hết thời gian; đây là ràng buộc kỹ thuật để giữ F09/F25.
- Ví đầu tiên/ví thêm sau Lưu chưa rõ có quyết định **riêng** R67/R69; Story này chỉ thu/chi/chuyển tiền. Đóng trước Lưu thuộc F21.

## 6. Field-level Specifications & Validations

Không nhập trường mới để đối chiếu. Ý định đã bấm Lưu cần được nhận diện đúng theo chủ và thao tác, gồm loại giao dịch, ví liên quan, ngày/số tiền từ form #08/#09/#13; không dùng sự giống nhau của nội dung làm định danh. Cách lưu ID xuyên đóng/mở là `TECH_GAP`.

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.OUTCOME = {UNKNOWN, SAVED, NOT_SAVED_CONFIRMED}`; `PARAM.REOPEN_SCOPE = cùng tài khoản, giao dịch đã bấm Lưu`; `PARAM.UNSAVED_DRAFT = không khôi phục sau đóng browser`. Không có thời gian chờ tối đa được Ninh chốt để tự kết luận thất bại. [F21, F25]

## 8. Business Rules (`BR-*`)

- `BR-35-01` **Ninh đã xác nhận:** mở lại cùng tài khoản có Internet thì tự đối chiếu thao tác đã bấm Lưu. Ví dụ bấm Lưu thu 100.000, đóng khi mất phản hồi, mở lại thì ứng dụng tìm đúng kết quả thao tác ấy. [F25]
- `BR-35-02` **Ninh đã xác nhận:** chắc đã lưu → báo đã lưu và một giao dịch; chắc chưa lưu → báo chưa lưu, form trống. Ví dụ chi 40.000 đã ghi trước đóng → sau mở chỉ một khoản chi, số dư trừ 40.000 một lần. [F25, B01]
- `BR-35-03` **Ninh đã xác nhận:** còn chưa rõ → tiếp tục báo kiểm tra, cho làm việc khác khi có Internet và chặn thử lại đúng thao tác. Ví dụ mở lại khi hệ thống vẫn xử lý chuyển X→Y → không tạo lần chuyển thứ hai. [R64, F25]
- `BR-35-04` **Ninh đã xác nhận:** đóng trình duyệt **trước** bấm Lưu thì mở lại form trống, không có ý định nào cần đối chiếu. [F21]

## 9. State Machine & Transition Rules (`STATE.*`)

`STATE.SUBMITTED_UNKNOWN` được giữ qua đóng/mở → `STATE.RECONCILING` khi đúng chủ có Internet → `STATE.SAVED` / `STATE.NOT_SAVED_CONFIRMED` chỉ khi có bằng chứng chắc, hoặc quay lại `STATE.SUBMITTED_UNKNOWN`. Retry đúng ý định bị chặn trong `UNKNOWN`; kết quả đã xác nhận không được nhân đôi. F21 là nhánh riêng `DRAFT_NOT_SUBMITTED → EMPTY_FORM`.

## 10. Data Fallback & Offline Handling

Không gửi/đối chiếu offline; khi mở lại thiếu Internet thì chỉ giữ trạng thái chưa rõ, thử khi có Internet. Không phục hồi nội dung bản nháp **chưa Lưu** theo F21; sau **chắc chưa lưu** mở form trống theo F25. Nội dung trong phiên trước đóng theo F09 không có nghĩa khôi phục form sau đóng.

## 11. Error Messages & UI Registry (`ERR.*`)

`ERR.RECONCILIATION_PENDING`, `ERR.SAVE_CONFIRMED`, `ERR.NOT_SAVED_CONFIRMED` là nhãn BA đề xuất; UI phải phân biệt ba kết quả. Không dùng câu “chưa lưu” khi chỉ timeout. Câu chữ và vị trí hiển thị chưa được Ninh chốt. [F25, R64]

## 12. Security & Privacy Constraints

Định danh ý định và kết quả phải gắn với đúng chủ; chuyển tài khoản trong cùng trình duyệt không được lộ dữ liệu. Đối chiếu chỉ cấp cho phiên của chủ, kể cả đường truy vấn trực tiếp. [R08, R12]

## 13. Third-party Integrations

Không áp dụng dịch vụ ngoài riêng. Lưu vết ý định và đối chiếu bền qua đóng/mở là hợp đồng Tech cần thiết kế/chứng minh.

## 14. Audit Log & History Requirements

Sau kết quả đã lưu, lịch sử thu/chi hoặc một dòng chuyển tiền đọc lại đúng một lần; số dư mọi ví liên quan không nhân đôi. Nếu chắc chưa lưu, không có dòng từ ý định ấy. Lịch sử chung/lọc ví do #10/#13 cung cấp. [B01, F17, R60]

## 15. NFR (Performance, Concurrency Bounds)

Đối chiếu qua đóng/mở, nhiều tab/phiên và cùng tài khoản hai điện thoại phải giữ đúng phạm vi ý định, không tạo bản trùng khi request đến muộn. Không có SLA phân định hoặc hạn giữ kết quả được Ninh chọn; Tech/PM cần quyết định và kiểm thử. [F25, R56]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: bấm Lưu thu 100.000 rồi đóng trước khi biết kết quả; mở lại cùng tài khoản có Internet tự đối chiếu. Nếu đã lưu, báo đã lưu, lịch sử đúng một khoản và số dư cộng 100.000 đúng một lần. [F25, B01]
- `AC2`: bấm Lưu chi 40.000 hoặc chuyển 50.000 rồi đóng; nếu chứng minh chắc chưa lưu, báo chưa lưu, form trống, không có giao dịch hoặc đổi số dư từ ý định ấy. [F25]
- `AC3`: khi chưa thể xác định, mở lại hiện đang kiểm tra, cho làm việc khác khi có Internet và chặn Lưu lặp đúng thao tác; request cũ đến muộn không tạo bản trùng. [F25, R64]
- `AC4`: với chuyển X→Y đã lưu, chỉ một dòng lịch sử, X trừ/Y cộng một lần; không có nửa lần chuyển. [R51, R60]
- `AC5`: đóng **trước** Lưu rồi mở lại cùng tài khoản thì form trống, không hiện thao tác chờ giả; tài khoản khác không thấy ý định của chủ trước. [F21, R08, R12]

## 17. Figma Mapping / Visual References

Không có Figma được duyệt; ảnh cũ chỉ tham khảo theo R02. UX đề xuất vị trí trạng thái chờ sau mở lại, không tự thêm quy tắc sản phẩm.

## 18. Dependencies (Cross-feature / Cross-story)

`HARD` #33 và #34 theo [issue #35](../capmoney-overall/issues/35-doi-chieu-luu-chua-ro-sau-mo-lai.md); #08/#09/#13 cho giao dịch, #10 cho lịch sử. Tech phải chứng minh định danh bền, kết quả âm an toàn và idempotency trước `TECH_READY`.

## 19. Q&A Log & Resolved Decisions

[Q&A](qna.md): F25/R64 đã chốt ba nhánh và quyền rời đi; F21 giới hạn bản nháp. Không có câu hỏi Ninh chặn bản BA; phương thức giữ định danh qua browser, nhiều tab và request muộn là `TECH_GAP`.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Soạn bản BA bước 9; chưa PM review/gate. | [Issue #35](../capmoney-overall/issues/35-doi-chieu-luu-chua-ro-sau-mo-lai.md), F21/F25, R64 |
