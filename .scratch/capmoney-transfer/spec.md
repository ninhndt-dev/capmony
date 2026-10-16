# Feature Spec: Story 13 – Chuyển tiền giữa hai ví

**Story:** [story.md](story.md)  
**BA gate:** chưa đánh giá; bản nháp từ nguồn bước 8 và [R74](../../docs/requirements/44-step-9-transfer-round-1.md), chờ CapmmPM đối chiếu. Không có `BA_READY`/`TECH_READY`.

## 1. Executive Summary / User Story statement

Là chủ tài khoản, tôi chuyển VND giữa hai ví đang dùng của mình bằng một thao tác, phí 0, để cả hai số dư và lịch sử luôn nhất quán. Nguồn: [Story nguồn #13](../capmoney-overall/issues/13-chuyen-tien-giua-vi.md), [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md).

## 2. Actor & Permission Matrix

Chủ tài khoản ghi chép đã đăng nhập được thao tác dữ liệu tài chính của chính mình; tài khoản ghi chép khác và tài khoản quản lý không được đọc hay sửa. Người chưa đăng nhập không được truy cập. [R08, R12; quyền bước 3]

## 3. Screen Definition & Entry Points

Có ít nhất hai ví đang sử dụng, mở thao tác chuyển tiền từ khu vực ví/giao dịch. Điểm đặt nút và thứ tự trường là chi tiết UX. Khi mở từ nút chung, cả ví gửi và ví nhận đều trống theo [R75](../../docs/requirements/45-step-9-transfer-wallet-selection.md); người dùng phải tự chọn đủ hai ví trước Lưu. Điểm vào từ một ví cụ thể chưa được Ninh chốt riêng.

## 4. Main Flows (Happy Path)

1. Chọn hai ví đang sử dụng khác nhau thuộc cùng tài khoản; nhập số nguyên VND dương, ngày hôm nay hoặc trước đó, ghi chú tùy chọn. Không cần danh mục, phí cố định 0. [B02, B04, B09, B12, B25, R42–R43]
2. Lưu thành công một lần chuyển như một đơn vị: ví gửi giảm, ví nhận tăng đúng cùng số tiền, kể cả ví gửi thành âm. [R51, B01, B23]
3. Đọc lại sau tải trang: lịch sử chung một dòng đủ ví gửi → ví nhận và số tiền; lọc từng ví trong hai ví cũng một dòng. [F17, R60]

## 5. Alternative & Exceptional Flows

- Cùng ví gửi/nhận, ví người khác, ví ngừng, tiền không dương/lẻ, ngày tương lai: từ chối và cả hai số dư giữ nguyên. [B02, B04, B09, R43, R12]
- Chuyển lớn hơn số dư ví gửi vẫn hợp lệ, số dư âm được phép. [B23]
- Rời biểu mẫu **trước Lưu** rồi quay lại cùng phiên có Internet: form mở trống; phần nhập dở không tạo lần chuyển hay đổi số dư hai ví. [R74](../../docs/requirements/44-step-9-transfer-round-1.md)
- Từ nút chung, chưa chọn đủ hai ví hoặc chọn trùng một ví: không tạo lần chuyển hay đổi số dư. [R75, B09]
- Kết quả Lưu chưa rõ khi mất mạng: không báo thành công sớm hoặc tạo lần chuyển trùng; ca đối chiếu trong phiên #34, sau mở lại #35. [F09, F25, R64]

## 6. Field-level Specifications & Validations

| Trường | Quy tắc |
| --- | --- |
| Ví gửi/nhận | Hai ví khác nhau, đang dùng, cùng chủ. Từ nút chung, ban đầu cả hai trống và bắt buộc tự chọn trước Lưu. [B04, B09, R12, R75] |
| Số tiền | Nguyên VND >0; vượt số dư ví gửi vẫn được. [B02, B23] |
| Ngày | Hôm nay hoặc trước đó. [R43] |
| Phí | Luôn 0; không nhập phí khác. [R42] |
| Ghi chú | Tùy chọn, có thể để trống. [B12] |
| Danh mục | Không áp dụng. [B25] |

## 7. System Parameters & Constants (`PARAM.*`)

`PARAM.TRANSFER_FEE = 0 VND`; `PARAM.TRANSFER_AMOUNT = nguyên VND >0`; `PARAM.TRANSFER_DATE ≤ hôm nay`. [R42–R43, B02]

## 8. Business Rules (`BR-*`)

- `BR-13-01` **Ninh đã xác nhận:** Một lần chuyển là một đơn vị: trừ ví gửi và cộng ví nhận đúng một lần. Ví A=50, B=10, chuyển 80: A=-30, B=90. [R51, B01, B23] **Ví dụ:** A=50, B=10 chuyển 80 thì A=-30 và B=90 trong cùng một kết quả.
- `BR-13-02` **Ninh đã xác nhận:** Chỉ ví khác nhau, hoạt động và cùng chủ; A→A hoặc A→ví người khác bị từ chối, không đổi số dư. [B04, B09, R12] **Ví dụ:** Chọn A làm cả ví gửi lẫn ví nhận hoặc chọn ví của tài khoản khác thì không tạo lần chuyển.
- `BR-13-03` **Ninh đã xác nhận:** Phí 0, không danh mục, số tiền nguyên VND dương và ngày không tương lai. [R42–R43, B02, B25] **Ví dụ:** Nhập 80 VND hôm nay là hợp lệ dù A chỉ có 50; nhập 0 hoặc ngày mai bị từ chối.
- `BR-13-04` **Ninh đã xác nhận:** Lịch sử chung/lọc A/lọc B đều ghi một dòng cùng lần chuyển với đủ A→B và số tiền. [F17, R60] **Ví dụ:** Một lần A→B 80 xuất hiện một dòng khi xem chung, lọc A hoặc lọc B.
- `BR-13-05` **Ninh đã xác nhận:** trước khi bấm Lưu, rời biểu mẫu chuyển rồi quay lại cùng phiên có Internet thì form trống; ví A=50/B=10 không đổi nếu bản nháp 80 VND bị bỏ. [R74](../../docs/requirements/44-step-9-transfer-round-1.md) **Ví dụ:** Nhập A→B 80 rồi rời form trước Lưu: quay lại form trống, A/B giữ nguyên.

- `BR-13-06` **Ninh đã xác nhận:** từ nút chung, ví gửi và ví nhận ban đầu trống; chưa chọn đủ hai ví không thể Lưu. **Ví dụ:** mở form chung rồi bấm Lưu ngay không tạo giao dịch; chọn A→B hợp lệ mới có thể Lưu. [R75](../../docs/requirements/45-step-9-transfer-wallet-selection.md)

## 9. State Machine & Transition Rules (`STATE.*`)

`mở từ nút chung → chưa chọn đủ hai ví → không thể Lưu` theo R75; `nhập nháp → rời trước Lưu → form trống` theo R74; `đã bấm Lưu → đang xác định kết quả → đã lưu một lần` hoặc `chắc chưa lưu` theo F09/F25/R64. Khi chưa rõ không cho gửi lặp cùng thao tác. Trạng thái xóa/khôi phục lần chuyển thuộc #15.

## 10. Data Fallback & Offline Handling

Không nhập offline [R11]. Rời form trước Lưu trong cùng phiên có Internet thì form trống [R74]; đóng trình duyệt trước Lưu theo F21. Mất mạng **sau Lưu** phải hiển thị chưa rõ và tự đối chiếu khi có mạng, không tạo nửa lần chuyển; #34/#35 đặc tả nhánh phục hồi. R74 không xóa trạng thái đang đối chiếu của thao tác đã gửi.

## 11. Error Messages & UI Registry (`ERR.*`)

Các lỗi ví, tiền, ngày, quyền phải không đổi cả hai ví. Nếu một phần giao dịch/kết quả chưa xác định, không báo đã lưu; nội dung cụ thể do UX/Tech đề xuất.

## 12. Security & Privacy Constraints

Mọi thao tác đọc/ghi phải kiểm chủ sở hữu ở phía hệ thống, gồm cả khi nhập trực tiếp đường dẫn hoặc định danh thuộc tài khoản khác. Tài khoản quản lý chỉ quản lý lời mời/hỗ trợ, không có quyền dữ liệu tài chính. [R08, R12; quyền bước 3]

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho lát cắt này: đây là thao tác trên dữ liệu tài chính của tài khoản ghi chép. Nền tảng lưu trữ/API do Tech thiết kế sau BA gate; không suy từ bản nháp này.

## 14. Audit Log & History Requirements

Một lần chuyển cần định danh chung cho cả tác động gửi/nhận và một dòng lịch sử; Tech quyết định mô hình dữ liệu. Không tạo hai khoản thu/chi độc lập [R51, F17].

## 15. NFR (Performance, Concurrency Bounds)

Web ưu tiên iPhone/Android và chỉ dùng khi có Internet. Nhiều phiên của cùng tài khoản có thể hoạt động đồng thời; kết quả đã báo lưu phải bền sau tải lại. Chưa có ngưỡng độ trễ, số bản ghi hay phiên bản trình duyệt cụ thể được Ninh chốt. [R06, R11, R14, R56; S05]

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1` Chuyển 80 VND từ ví A=50 sang B=10 tạo A=-30/B=90, phí 0, không danh mục; tải lại giữ số dư đúng và không có tác động một phía.
- `AC2` Hai ví phải khác nhau, hoạt động, cùng chủ; tiền nguyên dương và ngày không tương lai; dữ liệu sai bị từ chối không đổi số dư.
- `AC3` Lịch sử chung và khi lọc từng ví hiển thị đúng một dòng đủ ví gửi → ví nhận và số tiền của lần chuyển.
- `AC4` Lưu bình thường không tạo trùng khi bấm lặp cùng thao tác; nếu kết quả chưa rõ, không báo thành công trước đối chiếu (#34/#35).
- `AC5` Nhập một lần chuyển nhưng rời form trước Lưu rồi quay lại cùng phiên có Internet: form trống, không có dòng chuyển mới, cả hai số dư giữ nguyên. [R74]

AC là điều kiện quan sát được để PM/QA rà; chưa có test thực chạy, chưa `QA_PASS`.

- `AC6` Mở chuyển tiền từ nút chung: cả hai trường ví ban đầu trống; bấm Lưu khi chưa chọn đủ hai ví không tạo lần chuyển/đổi số dư; chọn hai ví đang sử dụng khác nhau rồi nhập đủ trường hợp lệ mới có thể Lưu. [R75]

## 17. Figma Mapping / Visual References

Ảnh ứng dụng cũ chỉ là tham khảo theo R02; chưa có Figma/màn hình được Ninh chốt. BA mô tả điểm vào và trạng thái quan sát được, còn bố cục/câu chữ do UX đề xuất và PM kiểm trước triển khai.

## 18. Dependencies (Cross-feature / Cross-story)

#07 cấp ví thứ hai; #10 cấp lịch sử để kiểm một dòng. Phụ thuộc Dev/tích hợp, BA có thể soạn độc lập.

Ngoài phạm vi: Sửa lần chuyển #14; xóa/khôi phục #15; lưu đồng thời #29; mất mạng/đối chiếu #34/#35; ví ngừng #17/#18. Không suy mặc định ví từ R70–R73; dùng quyết định riêng R75 cho điểm vào chung.

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md). Quy tắc sản phẩm đã có nguồn `R24, R42, R43, R51, R60, R74; B01–B02, B04, B09, B12, B23, B25; F17`. Ninh đã xác nhận R74 cho nhánh rời form **trước Lưu**; mặc định ví ở điểm vào chung đã được chốt tại R75; điểm vào từ một ví riêng chưa được chốt. Không mở gate chỉ vì đã viết spec.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 16/10/2026 | Bản nháp bước 9 cho Story #13; chờ PM rà BA, Tech thiết kế và reviewer độc lập. | [Issue nguồn](../capmoney-overall/issues/13-chuyen-tien-giua-vi.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
| 16/10/2026 | Ninh chọn R75: cả hai trường ví trống khi mở từ nút chung; thêm BR-13-06/AC6. | [Vòng #11](../../docs/requirements/45-step-9-transfer-wallet-selection.md) |
| 16/10/2026 | Ninh chọn mở trống khi rời biểu mẫu chuyển trước Lưu trong cùng phiên; ghi R74, BR-13-05, AC5; không đổi nhánh đã bấm Lưu. | [Vòng #13](../../docs/requirements/44-step-9-transfer-round-1.md) |
