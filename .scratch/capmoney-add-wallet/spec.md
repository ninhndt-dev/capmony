# Feature Spec: Story 07 – Thêm ví

**Story:** [story.md](story.md)
**Status:** needs-info
**BA gate:** **chưa đạt**; Q1–Q2 trong [Q&A](qna.md) chờ Ninh. CapmmPM đã rà phần độc lập và BA đã sửa ranh giới; PM chưa xác nhận gate. Không ghi `RESULT: BA_READY`.

## 1. Executive Summary / User Story statement

Người dùng đã có ví muốn tạo thêm ví của riêng mình và đọc lại danh sách, số dư từng ví để theo dõi nhiều nguồn tiền. Story này là lát cắt tạo **ví thứ hai trở đi**, dựa trên khả năng tạo ví đầu tiên ở #06. [R15, H04, Story #07]

## 2. Actor & Permission Matrix

| Actor | Tạo/xem ví thêm | Căn cứ |
| --- | --- | --- |
| Chủ tài khoản ghi chép đã đăng nhập | Tạo ví thêm, xem tên và số dư các ví của mình. | R12, R15; [người dùng và quyền](../../docs/requirements/actors-and-roles.md) |
| Tài khoản ghi chép khác | Không tạo hoặc đọc ví của người khác trong Story #07. | R08, R12 |
| Tài khoản quản lý của Ninh hoặc người chưa đăng nhập | Không tạo hoặc đọc ví của tài khoản ghi chép. | R12; quyền bước 3 |

Quyền sửa tên/số dư hoặc trạng thái ví thuộc các Story sau; Story #07 kiểm tạo và đọc lại.

## 3. Screen Definition & Entry Points

- **Ninh đã xác nhận:** một người có thể tạo nhiều ví. R15 chưa chọn vị trí thao tác trong giao diện. [R15]
- **BA đề xuất để mô tả luồng #07:** từ khu vực/danh sách ví có thao tác “Thêm ví”; Story này không xây điều hướng quay về ghi chép. Vị trí nút, nhãn và điều hướng cụ thể chưa được Ninh xác nhận. Không bắt buộc bố cục của ảnh cũ. [R02]
- Biểu mẫu tối thiểu có tên ví và số dư ban đầu tùy chọn. Tên phải do người dùng nhập trước khi Lưu. [R23, R66, H04]

## 4. Main Flows (Happy Path)

1. Chủ tài khoản ghi chép có ít nhất một ví mở thao tác thêm ví khi có Internet.
2. Nhập tên ví, tùy chọn nhập số dư ban đầu là số nguyên VND; để trống thì dùng 0 VND. [R23, R66, B10, B15, H04]
3. Bấm Lưu; chỉ sau khi Lưu thành công ví mới thuộc riêng tài khoản ấy và xuất hiện trong danh sách với số dư hiện tại bằng số dư ban đầu. Giá trị này là mốc, không tạo bản ghi khoản thu. [R12, R30, B01]
4. Tải lại, người dùng vẫn đọc được ví đã lưu; cùng tài khoản trên điện thoại khác có Internet thấy dữ liệu của mình. [R27, R56]

## 5. Alternative & Exceptional Flows

- **Ninh đã xác nhận:** tên bỏ trống không Lưu; số dư ban đầu bỏ trống dùng 0; giá trị âm nguyên hợp lệ, phần lẻ không hợp lệ. [R66, F13/H04, B10, B15]
- **Ninh đã xác nhận:** đóng hẳn trình duyệt **trước khi bấm Lưu** ví thì mở lại cùng điện thoại/tài khoản biểu mẫu trống; dữ liệu đã lưu vẫn còn. [F21]
- **Chưa rõ, chặn BA_READY:** rời trang rồi quay lại trong cùng phiên trước khi Lưu ví thêm theo Q1; đã bấm Lưu ví thêm rồi đóng khi kết quả chưa rõ theo Q2. F18 và R67 chỉ chốt cho ví đầu tiên. [Q&A](qna.md)
- **BA/Tech đề xuất, chưa chốt kỹ thuật:** bấm Lưu lặp cùng một thao tác không tạo ví trùng; hai thao tác tạo ví độc lập vẫn có thể tạo hai ví. Cần Tech chứng minh ở thiết kế.

## 6. Field-level Specifications & Validations

| Trường | Quy tắc đã chốt | Chưa rõ/đề xuất |
| --- | --- | --- |
| Tên ví | Người dùng phải nhập trước khi Lưu; không tự đặt tên mặc định. [R66] | Tên trùng, khoảng trắng, độ dài và cách báo lỗi chưa chốt riêng; không tự suy ra tên phải duy nhất. |
| Số dư ban đầu | Tùy chọn; bỏ trống = 0 VND. Nếu nhập, phải là số nguyên VND, cho giá trị âm, 0 hoặc dương. [R23, B10, B15, H04] | Cách trình bày dấu phân cách/giới hạn kỹ thuật do Tech đề xuất; không tự đặt mức tối đa sản phẩm. |

## 7. System Parameters & Constants (`PARAM.*`)

- `PARAM.WALLET_CURRENCY = VND` cho bản đầu. [R16]
- `PARAM.OPENING_BALANCE_EMPTY = 0 VND`. [H04; F13 cho ví đầu tiên]
- `PARAM.OPENING_BALANCE_DOMAIN = số nguyên VND, gồm giá trị âm và 0`. [B10, B15]
- Không áp dụng tham số hạn thời gian hay phí cho việc tạo ví; chưa có nguồn xác nhận giá trị giới hạn tên/số dư.

## 8. Business Rules (`BR-*`)

- `BR-07-01`: một tài khoản ghi chép có thể có nhiều ví; ví mới thuộc chủ tài khoản ấy. [R12, R15]
- `BR-07-02`: tên ví do người dùng nhập là bắt buộc trước khi Lưu, không tự đặt tên. [R66]
- `BR-07-03`: số dư đầu bỏ trống = 0; giá trị nhập là số nguyên VND và được âm. [R23, B10, B15, H04]
- `BR-07-04`: trước giao dịch đầu tiên, số dư hiện tại của ví bằng số dư ban đầu; số dư đầu không tạo bản ghi khoản thu. [B01, R30]

## 9. State Machine & Transition Rules (`STATE.*`)

- `STATE.WALLET_FORM`: chưa Lưu thành công thì chưa có ví mới trong dữ liệu đã lưu; Lưu thành công mới tạo ví của chủ tài khoản và cho đọc trong danh sách. Đây là mô tả hành vi, không chốt mô hình dữ liệu. [R12, R15; đối chiếu #06]
- `STATE.BROWSER_CLOSED_BEFORE_SAVE`: biểu mẫu mở lại trống, ví cũ đã lưu không mất. [F21]
- `STATE.NAVIGATED_BEFORE_SAVE` và `STATE.SAVE_UNKNOWN_AFTER_BROWSER_CLOSE` của ví thêm **chờ Q1–Q2**; không sao chép F18/R67 của ví đầu tiên thành quyết định.
- Trạng thái ngừng/bật ví và xóa ví thuộc Story #18–#19.

## 10. Data Fallback & Offline Handling

- Bản đầu chỉ dùng khi có Internet; không có nhập ví offline rồi đồng bộ sau. [R11]
- Không lấy dữ liệu ví từ app cũ; mỗi tài khoản mới bắt đầu không có ví/giao dịch cũ. [R03]
- Cách giữ bản nháp khi rời trang và cách đối chiếu sau đóng trình duyệt với **ví thêm** đang chờ Q1–Q2; không mặc định có khôi phục bản nháp. [Q&A](qna.md)

## 11. Error Messages & UI Registry (`ERR.*`)

- Cần báo không thể Lưu khi tên trống hoặc số dư không phải số nguyên VND; câu chữ cụ thể là đề xuất UI, chưa được Ninh xác nhận. [R66, B15]
- Khi không có Internet, không hoàn tất thao tác tạo ví; cách hiển thị lỗi để Tech/UX đề xuất theo R11.
- Mã lỗi, thông báo cho Q1–Q2 và vị trí trình bày chưa chốt; chỉ bổ sung sau khi có quyết định. Không tự gán mã `ERR.*` cụ thể như một yêu cầu đã xác nhận.

## 12. Security & Privacy Constraints

- Chủ tài khoản ghi chép mới được tạo/đọc ví của mình; tài khoản khác và tài khoản quản lý không tạo hoặc đọc ví ấy trong Story #07. Quyền sửa thuộc #16. [R08, R12; quyền bước 3]
- Kiểm quyền tại mọi đường tạo/đọc, kể cả đường xử lý trực tiếp; đây là yêu cầu kiểm chứng quyền, còn hợp đồng và cách xác thực thuộc Tech.

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho Story #07: thao tác tạo/đọc ví dùng tài khoản ghi chép đã thiết lập ở #04. Nền tảng lưu trữ và API chưa được chọn; Tech xử lý sau `BA_READY`.

## 14. Audit Log & History Requirements

- **Ninh đã xác nhận:** số dư ban đầu là mốc, không tạo **bản ghi khoản thu**. [R30]
- **Ranh giới kiểm thử:** màn lịch sử thuộc Story #10; #07 kiểm không sinh bản ghi khoản thu mà không yêu cầu xây màn lịch sử.
- Không có yêu cầu riêng về nhật ký kiểm toán thao tác tạo ví đã được Ninh xác nhận. Nếu Tech cần log vận hành, không làm lộ dữ liệu tài chính giữa tài khoản và không xem đó là lịch sử thu/chi.

## 15. NFR (Performance, Concurrency Bounds)

- Bản đầu phục vụ khoảng 10 người, web ưu tiên iPhone và Android; dữ liệu đã báo lưu phải đọc lại đúng chủ tài khoản. [R05, R14, S05]
- Chưa có ngưỡng thời gian phản hồi hay tải đồng thời cụ thể được Ninh chốt. Tech đề xuất và kiểm thử rủi ro bấm Lưu lặp, hai yêu cầu gần đồng thời cùng thao tác, và hai thao tác tạo ví độc lập; không tự biến mức kỹ thuật thành yêu cầu sản phẩm.

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: tài khoản đã có ví tạo thêm được ví khác có tên do người dùng nhập; tải lại vẫn thấy cả các ví đã lưu. [R15, R66]
- `AC2`: tên trống không Lưu; không tự tạo tên mặc định. [R66]
- `AC3`: số dư đầu trống/0/nguyên dương/nguyên âm được ghi đúng; phần lẻ hoặc không phải số nguyên bị từ chối. [R23, B10, B15, H04]
- `AC4`: trước giao dịch, số dư hiện tại của ví mới bằng số dư đầu; việc tạo ví không sinh **bản ghi khoản thu**. Kiểm hiển thị lịch sử sau #10. [R30, B01]
- `AC5`: tài khoản khác và tài khoản quản lý không tạo/đọc ví của chủ; chủ đọc lại được ví của mình sau tải trang và trên điện thoại khác có Internet. Quyền sửa kiểm ở #16. [R12, R27, R56]
- `AC6`: đóng trình duyệt trước khi bấm Lưu ví thêm rồi mở lại cùng điện thoại/tài khoản thì biểu mẫu trống, các ví đã lưu vẫn còn. [F21]
- `AC7` **chờ Q1**: hành vi khi rời trang trước Lưu rồi quay lại cùng phiên; chưa có kết quả pass/fail để áp cho ví thêm.
- `AC8` **chờ Q2**: hành vi khi đã bấm Lưu ví thêm rồi đóng khi chưa rõ kết quả; chưa có kết quả pass/fail để áp cho ví thêm.

AC7–AC8 là chỗ giữ chỗ cho quyết định của Ninh, **không** dùng để đánh dấu `BA_READY` hoặc `QA_PASS`.

## 17. Figma Mapping / Visual References

Không áp dụng Figma mapping: repo chưa có Figma được Ninh duyệt. Ảnh CapMoney cũ chỉ để tham khảo giao diện, không phải tiêu chí pixel hay nguồn hành vi. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

- #06 cung cấp luồng tạo/đọc ví đầu tiên; #07 triển khai khả năng tạo ví tiếp theo. Phân loại cạnh phụ thuộc và điều kiện tích hợp do PM xác định; #06 chưa tự đạt gate mới.
- #04 cung cấp tài khoản ghi chép đã xác thực. [#06](../capmoney-first-wallet/spec.md) và [Story ứng viên #07](../capmoney-overall/issues/07-them-vi.md)
- **Bối cảnh F20, không phải AC #07:** khi mọi ví đã ngừng, luồng ghi thu/chi cần dẫn tạo ví mới hoặc bật ví lại và quay về ghi chép. Điều hướng và kiểm tích hợp thuộc #18 cùng các Story ghi thu/chi #08–#09; #07 chỉ cung cấp khả năng tạo/đọc ví thêm.

## 19. Q&A Log & Resolved Decisions

- **Ninh đã xác nhận:** R66 quy định tên bắt buộc cho ví đầu tiên và ví thêm. [Nhật ký R66](../../docs/requirements/39-step-9-first-wallet-round-1.md)
- **Chưa rõ:** Q1 giữ bản nháp khi rời trang, Q2 xử lý sau bấm Lưu rồi đóng browser đối với ví thêm. Đã gửi qua giao diện, chưa có trả lời được ghi trong repo. [Q&A](qna.md)
- **BA/Tech đề xuất:** các chi tiết giao diện, tên trùng, giới hạn và chống bấm Lưu lặp. Chưa chuyển thành quyết định của Ninh.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 14/10/2026 | Tạo bản BA bước 9 cho Story #07; ghi AC độc lập và hai câu hỏi chặn, chưa đạt `BA_READY`. | [Story ứng viên #07](../capmoney-overall/issues/07-them-vi.md), [intake](../../docs/requirements/00-intake.md), [core](../../docs/requirements/core-business.md), [flows](../../docs/requirements/user-flows.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
| 14/10/2026 | Sửa theo PM review: F20 chuyển sang phụ thuộc tích hợp #18; #07 chỉ kiểm tạo/đọc, không kiểm sửa; AC4 kiểm không sinh bản ghi khoản thu, màn lịch sử để #10. Q1–Q2 vẫn chờ. | CapmmPM rà độc lập Story #07; [handoff](handoff.md) |
| 14/10/2026 | Tách điểm vào “Thêm ví” khỏi nhãn Ninh đã xác nhận: R15 chỉ chốt nhiều ví; vị trí UI là BA đề xuất. Q1–Q2 vẫn chờ. | CapmmPM góp ý nhãn nguồn; [handoff](handoff.md) |

Chưa có code, test run hoặc thiết kế kỹ thuật. CapmmPM đã rà ba ranh giới trên; chưa xác nhận gate vì Q1–Q2 còn mở.
