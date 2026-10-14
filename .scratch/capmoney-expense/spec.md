# Feature Spec: Story 09 – Ghi khoản chi

**Story:** [story.md](story.md)
**BA gate:** `BA_READY` được CapmmPM xác nhận ngày 14/10/2026 sau Q1=A → R72 và Q2=A → R73; xem [story](story.md) và [handoff](handoff.md). `TECH_READY` chưa đánh giá.

## 1. Executive Summary / User Story statement

Là chủ tài khoản ghi chép, tôi muốn ghi khoản chi vào ví của mình bằng danh mục đã có, để số dư hiện tại giảm đúng và đọc lại được, kể cả khi số dư thành âm. Phạm vi là lát cắt ghi chi qua web theo [issue #09](../capmoney-overall/issues/09-ghi-chi.md), phụ thuộc nền danh mục/ghi thu của #08.

## 2. Actor & Permission Matrix

| Actor | Khoản chi, ví, danh mục của mình | Dữ liệu người khác |
| --- | --- | --- |
| Tài khoản ghi chép đã đăng nhập | Chọn ví/danh mục, tạo khoản chi, đọc lại số dư | Không đọc/tạo/sửa |
| Tài khoản ghi chép khác | Chỉ dữ liệu của chính mình | Không đọc/tạo/sửa |
| Tài khoản quản lý của Ninh | Không tạo/đọc dữ liệu tài chính của tài khoản ghi chép | Không đọc/tạo/sửa |

Nguồn [R08, R12](../../docs/requirements/00-intake.md), [actors](../../docs/requirements/actors-and-roles.md). Quyền sửa/xóa khoản chi kiểm ở Story #11–#12.

## 3. Screen Definition & Entry Points

- **Ninh đã xác nhận:** mở luồng ghi thu/chi từ nút chung hoặc ví đang sử dụng; nếu vào từ ví thì ví đó được chọn sẵn và có thể đổi sang ví đang sử dụng khác. [F14](../../docs/requirements/user-flows.md)
- Story này kiểm lựa chọn **chi** trên biểu mẫu có ví, số tiền, ngày, danh mục, ghi chú; không chốt bố cục/số màn. Từ nút chung có ≥2 ví đang sử dụng, form khoản chi ban đầu **chưa chọn ví**; người dùng tự chọn trước Lưu. Đây là [R72](../../docs/requirements/43-step-9-expense-round-1.md), xác nhận riêng với R70 của khoản thu.
- Chưa có ví: F02/#06 dẫn tạo ví đầu tiên. Mọi ví đã ngừng: F20 dẫn tạo/bật lại rồi trở về ghi chép; kiểm chứng đầy đủ tích hợp ở #18.

## 4. Main Flows (Happy Path)

1. Chủ tài khoản đã có ví đang sử dụng và danh mục riêng dùng chung cho thu/chi từ #08. [B04, B16]
2. Mở ghi từ nút chung hoặc một ví, chọn **chi**, chọn ví đang sử dụng và danh mục của mình, nhập số nguyên VND dương, ngày hôm nay hoặc trước đó, ghi chú có thể bỏ trống. Từ nút chung có ≥2 ví, form chưa chọn ví nên phải tự chọn; từ ví thì ví ấy được chọn sẵn và đổi được. [F14, R72, R25, R43, B02, B12]
3. Bấm Lưu; chỉ khi kết quả đã xác nhận lưu, khoản chi gắn đúng ví và số dư hiện tại giảm đúng số tiền, bất kể ngày giao dịch. Sau tải lại, số dư vẫn đúng. [B01, F09; S05]

## 5. Alternative & Exceptional Flows

- Vào từ ví A thì A được chọn sẵn; đổi sang ví B đang dùng trước Lưu thì chỉ B giảm. Ví đã ngừng không được chọn cho khoản chi mới. [F14, B04]
- Từ nút chung với ≥2 ví đang sử dụng, form chi chưa chọn ví; bấm Lưu khi chưa chọn không tạo khoản chi hoặc đổi số dư. [R72]
- Số dư được âm: số dư ví 50.000 VND, ghi chi 120.000 VND hợp lệ thì đọc lại -70.000 VND. Không buộc đủ tiền. [R39, B01–B02]
- Không có ví: F02; mọi ví đã ngừng: F20. Số tiền 0/âm/phần lẻ, thiếu danh mục/ví, ngày tương lai hoặc ví không thuộc chủ: không lưu và không đổi số dư. [B02, R25, R43, B04, R12]
- Rời form chi **trước Lưu** rồi quay lại cùng phiên có Internet: form trống, phần nhập dở không tạo khoản chi hay đổi số dư. [R73] Đóng hẳn trình duyệt trước Lưu: mở lại form trống, dữ liệu đã lưu còn. [F21]
- Sau khi bấm Lưu mà kết quả chưa được xác nhận, không báo thành công. Các ca mất mạng/đóng mở và đối chiếu thuộc #33/#35 theo F09/F25/R64; không suy kết quả từ việc chưa thấy giao dịch trên màn. [F09, F25, R64]

## 6. Field-level Specifications & Validations

| Trường | Ninh đã xác nhận | Chưa rõ/đề xuất |
| --- | --- | --- |
| Loại | Story này là **chi**; luồng ghi dùng cho thu/chi. [R07, F14] | Vị trí thao tác chọn loại do UX đề xuất. |
| Ví | Bắt buộc là ví đang sử dụng của chủ; vào từ ví chọn sẵn và đổi được. Từ nút chung có ≥2 ví đang sử dụng, form chi chưa chọn ví và phải tự chọn trước Lưu. [R12, B04, F14, R72] | Cách trình bày khi nút chung chỉ có một ví đang dùng do UX đề xuất; không đổi quy tắc ví hợp lệ trước Lưu. |
| Số tiền | Số nguyên VND > 0; không nhận 0, âm, phần lẻ. **Số dư sau chi** vẫn được âm. [B02, R39] | Dấu phân cách và giới hạn kỹ thuật; không đặt trần sản phẩm khi chưa có nguồn. |
| Ngày | Hôm nay hoặc ngày đã qua; không là ngày tương lai. [R38, R43] | Cách hiển thị và ngày mặc định do UX/Tech đề xuất. |
| Danh mục | Bắt buộc, chọn từ danh sách riêng của chủ dùng chung thu/chi. [R25, R45, B16] | Có tự chọn sẵn danh mục hay không là chi tiết UX chưa chốt; phải gắn danh mục trước Lưu. |
| Ghi chú | Tùy chọn, được để trống. [B12] | Độ dài/câu chữ chưa chốt. |

## 7. System Parameters & Constants (`PARAM.*`)

- `PARAM.CURRENCY = VND` cho bản đầu. [R16]
- `PARAM.EXPENSE_AMOUNT_DOMAIN = số nguyên VND > 0`. [B02]
- `PARAM.EXPENSE_DATE_DOMAIN = hôm nay hoặc ngày trước đó`. [R43]
- `PARAM.EXPENSE_MAY_MAKE_BALANCE_NEGATIVE = true`: khoản chi hợp lệ vẫn được lưu khi làm số dư hiện tại âm. Đây là diễn đạt hành vi R39/B01, không đặt giới hạn âm mới.
- Không có ngưỡng hiệu năng, hạn ghi chú hay giới hạn số tiền sản phẩm được Ninh chốt riêng.

## 8. Business Rules (`BR-*`)

- `BR-09-01` **Ninh đã xác nhận:** khoản chi phải dùng ví đang sử dụng của chủ và một danh mục của chính chủ trong danh sách chung thu/chi. **Ví dụ:** A chọn ví “Tiền mặt” và danh mục “Ăn uống” của A để chi 40.000 VND; không thể dùng ví hoặc danh mục của B. [R12, R25, R45, B04, B16]
- `BR-09-02` **Ninh đã xác nhận:** vào từ ví thì chọn sẵn ví ấy, nhưng đổi sang ví đang sử dụng khác được. **Ví dụ:** vào từ “Tiền mặt”, đổi sang “Ngân hàng” trước Lưu khoản chi 40.000 VND thì chỉ “Ngân hàng” giảm. [F14, B04]
- `BR-09-03` **Ninh đã xác nhận:** số tiền khoản chi là nguyên VND dương, ngày hôm nay hoặc trước đó, ghi chú có thể trống. **Ví dụ:** chi 125.000 VND hôm qua không ghi chú hợp lệ; 0, -1, 1,5 VND hoặc ngày mai bị từ chối. [B02, R43, B12]
- `BR-09-04` **Ninh đã xác nhận:** khoản chi còn hiệu lực trừ một lần khỏi số dư hiện tại của đúng ví, bất kể ngày ghi, và có thể làm số dư âm. **Ví dụ:** ví đang 50.000 VND, chi 120.000 VND ngày hôm qua thì số dư hiện tại là -70.000 VND; ví khác giữ nguyên. [B01, R39]
- `BR-09-05` **Ninh đã xác nhận, quy tắc liên Story:** chưa xác nhận Lưu thì không báo thành công; một thao tác Lưu chưa rõ không được tạo giao dịch trùng khi đối chiếu/thử lại. **Ví dụ:** bấm Lưu chi 120.000 VND rồi mất mạng; khi chưa rõ thì không báo đã lưu, sau đối chiếu nếu đã lưu thì chỉ có một khoản chi ấy. #09 kiểm đường Lưu bình thường; #33/#35 kiểm nhánh lỗi/phục hồi. [F09, F25, R64]
- `BR-09-06` **Ninh đã xác nhận:** từ nút chung khi có ≥2 ví đang sử dụng, form **khoản chi** chưa chọn ví; người dùng tự chọn ví hợp lệ trước Lưu. **Ví dụ:** có “Tiền mặt” và “Ngân hàng”, vào ghi chi từ nút chung thì chưa ví nào được chọn; Lưu chi 120.000 VND lúc đó bị chặn, chọn “Ngân hàng” rồi Lưu thì chỉ ví này giảm. [R72]
- `BR-09-07` **Ninh đã xác nhận:** rời form **khoản chi** trước Lưu rồi quay lại cùng phiên có Internet thì form trống; không tạo khoản chi hoặc đổi số dư từ bản nhập dở. **Ví dụ:** nhập chi 120.000 VND vào “Ngân hàng” rồi chuyển trang trước Lưu; quay lại thấy form trống, số dư “Ngân hàng” giữ nguyên. [R73]

## 9. State Machine & Transition Rules (`STATE.*`)

- `STATE.DRAFT`: đang nhập chi trước Lưu → chưa có khoản chi, số dư không đổi. Rời form rồi quay lại cùng phiên có Internet → form trống (R73); đóng hẳn trình duyệt trước Lưu → form trống khi mở lại (F21). Hai nhánh trước Lưu khác F09/R64 sau Lưu.
- `STATE.SUBMITTED_UNKNOWN`: đã bấm Lưu, kết quả chưa rõ → không báo thành công; F09/R64 yêu cầu đối chiếu và chặn lưu lặp đúng thao tác. Kiểm chứng chuyên biệt ở #33/#35.
- `STATE.SAVED`: kết quả xác nhận lưu → khoản chi thuộc đúng ví và trừ đúng một lần khỏi số dư hiện tại, có thể âm. [B01, R39]
- `STATE.CONFIRMED_NOT_SAVED`: sau đóng/mở nếu chắc chưa lưu → báo chưa lưu, form trống; nếu vẫn chưa rõ tiếp tục trạng thái kiểm tra/chặn trùng. [F25]
- Sửa, xóa, khôi phục khoản chi thuộc #11–#12.

## 10. Data Fallback & Offline Handling

- Bản đầu chỉ dùng có Internet, không ghi chi offline rồi đồng bộ sau. [R11]
- Tài khoản mới không nhập lịch sử CapMoney cũ. [R03]
- Trong phiên, khi đã Lưu mà kết quả chưa rõ, F09/R64 giữ nội dung và tự đối chiếu khi có mạng; sau đóng/mở F25 tự đối chiếu đúng thao tác. Hợp đồng và kiểm chứng lỗi mạng thuộc #33/#35. Rời form **trước Lưu** thuộc Q2, tách khỏi nhánh này.

## 11. Error Messages & UI Registry (`ERR.*`)

- `ERR.EXPENSE_INVALID_AMOUNT`: báo số tiền phải nguyên VND dương; `ERR.EXPENSE_FUTURE_DATE`: ngày tương lai không hợp lệ; `ERR.EXPENSE_MISSING_CATEGORY`: cần danh mục; `ERR.EXPENSE_INVALID_WALLET`: cần ví đang sử dụng của chủ. Đây là **mã BA đề xuất để truy vết**, chưa là mã API/câu chữ đã duyệt. [B02, R43, R25, B04]
- Trạng thái “đang kiểm tra/đã lưu/chưa lưu” theo F09/F25/R64; câu chữ, vị trí và xử lý trực quan do UX/Tech đề xuất.

## 12. Security & Privacy Constraints

- Chỉ chủ tài khoản ghi chép tạo khoản chi và đọc ví/danh mục/số dư của mình; tài khoản khác và tài khoản quản lý không xem dữ liệu tài chính. Kiểm quyền cả đường thao tác trực tiếp. [R08, R12; actors]
- Yêu cầu gửi trực tiếp với ví/danh mục người khác bị từ chối; không tin chủ sở hữu do trình duyệt khai. Cách xác thực/ủy quyền thuộc Tech.

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho Story #09. Nền tài khoản, ví và danh mục đến từ #04/#06/#07/#08; chưa chọn công nghệ/API.

## 14. Audit Log & History Requirements

- Khoản chi đã lưu là giao dịch có số tiền, ngày, ví, danh mục và ghi chú tùy chọn; màn lịch sử chung/lọc ví, thứ tự dòng và sửa/xóa thuộc #10–#12. [R07, R25, F15–F16]
- Bản đầu không cần số dư sau từng dòng lịch sử. [B17] Không có yêu cầu nhật ký kiểm toán riêng được Ninh chốt; log kỹ thuật nếu cần không thay bản ghi khoản chi.

## 15. NFR (Performance, Concurrency Bounds)

- Web mobile iPhone/Android, khoảng 10 người. Khoản đã báo lưu và số dư phải đọc lại đúng chủ sau tải lại và trên điện thoại khác có Internet. [R05–R06, R14, R27; S05]
- Hai giao dịch khác nhau gần đồng thời phải được giữ, số dư tính đúng. [B24] Kiểm chứng hai điện thoại chuyên biệt ở #29/P02; #09 không tự nhận kết quả test ấy.
- Chưa có ngưỡng thời gian đáp ứng hoặc phiên bản trình duyệt được Ninh chốt; Tech đề xuất sau BA gate.

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: trên tài khoản đã có danh mục từ #08, khi ghi **chi** người dùng chọn được cùng danh sách danh mục riêng đã dùng cho thu; tài khoản khác không dùng được danh mục của chủ. [R25, R45, B16]
- `AC2`: từ một ví đang sử dụng, mở ghi chi thấy ví ấy được chọn sẵn và đổi sang ví đang sử dụng khác của mình được. Từ nút chung với ≥2 ví đang sử dụng, mở ghi chi **chưa chọn ví**, phải tự chọn ví đang sử dụng trước Lưu; ví chưa chọn thì không tạo khoản chi hoặc đổi số dư. [F14, R72]
- `AC3`: lưu khoản chi vào ví đang sử dụng của chủ với danh mục của chủ, số nguyên VND dương, ngày hôm nay hoặc trước đó, ghi chú trống hoặc có nội dung; chứng minh bản ghi đã lưu thuộc đúng ví/danh mục và số dư đọc lại đúng, không đòi màn lịch sử #10. [R25, R43, B01–B02, B04, B12]
- `AC4`: ví trước Lưu có 50.000 VND, ghi chi 120.000 VND cho hôm qua; sau xác nhận và tải lại, số dư hiện tại là **-70.000 VND** và ví khác không đổi. Không chặn vì thiếu số dư. [B01, R39]
- `AC5`: ví đã ngừng hoặc không thuộc chủ, thiếu danh mục/ví, số tiền 0/âm/phần lẻ hay ngày tương lai thì không lưu khoản chi hoặc đổi số dư. [B02, B04, R12, R25, R43]
- `AC6`: tài khoản B và tài khoản quản lý không tạo/đọc khoản chi, danh mục hoặc số dư của A qua giao diện hay đường thao tác trực tiếp. [R08, R12]
- `AC7`: nhập khoản chi 120.000 VND vào ví “Ngân hàng”, **rời form trước khi bấm Lưu** rồi quay lại cùng phiên có Internet: form trống, không có khoản chi mới và số dư “Ngân hàng” không đổi. Đóng hẳn trình duyệt trước Lưu rồi mở lại cùng tài khoản có Internet cũng mở form trống; dữ liệu đã lưu trước đó vẫn còn. [R73, F21]
- `AC8`: trên đường Lưu bình thường, chỉ báo khoản chi đã lưu sau khi kết quả được xác nhận; sau thông báo đó bản ghi và số dư đọc lại đúng như AC3–AC4. Khi chưa xác nhận, không báo thành công. Mất mạng, kết quả chưa rõ, rời form sau Lưu hoặc đóng/mở kiểm ở #33/#35. [B01, F09]

## 17. Figma Mapping / Visual References

Không áp dụng Figma mapping: repo chưa có Figma được duyệt. Ảnh CapMoney cũ chỉ tham khảo giao diện, không là nguồn hành vi hay tiêu chí pixel. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

- #08 cấp bộ danh mục chung thu/chi và nền ghi giao dịch; issue #09 ghi phụ thuộc #08. #06/#07 cấp ví đã lưu, #04 cấp phiên/quyền qua các Story trước. PM/Tech quyết định cạnh `HARD`/`CONTRACT` cho Dev sau thiết kế.
- #10 cung cấp màn lịch sử; #11–#12 sửa/xóa/khôi phục; #18 kiểm đầy đủ khi mọi ví ngừng; #29 kiểm nhiều điện thoại. #33/#35 đặc tả và kiểm chứng F09/F25/R64 về kết quả Lưu chưa rõ, không là điều kiện cho AC #09 trên đường Lưu bình thường.
- [#08](../capmoney-income/story.md) hiện `BA_READY` nhưng `STORY_TECH_GAP`; chưa có cơ sở chia ticket Dev #09. Phụ thuộc không cản trở BA soạn spec #09.

## 19. Q&A Log & Resolved Decisions

- **Ninh đã xác nhận:** R07, R12, R25, R39, R43, R45; B01–B02, B04, B12, B16; F02, F09, F14, F20–F21, F25, R64. Q1=A → R72 và Q2=A → R73 xác nhận riêng cho **khoản chi #09**; xem [nhật ký #09](../../docs/requirements/43-step-9-expense-round-1.md), [Q&A](qna.md), [intake](../../docs/requirements/00-intake.md), [core](../../docs/requirements/core-business.md), [flows](../../docs/requirements/user-flows.md).
- **Chưa rõ ngoài phạm vi #09:** không suy R72–R73 cho chuyển tiền hoặc tạo ví. Cách trình bày khi nút chung chỉ có một ví là chi tiết UX; Lưu vẫn đòi ví hợp lệ của chủ.
- **BA/Tech đề xuất:** mã `ERR.*`, bố cục, câu chữ và giới hạn kỹ thuật trường nhập. Chưa là quyết định của Ninh.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 14/10/2026 | Tạo bản BA bước 9 cho Story #09, 20 mục, BR có ví dụ, AC quan sát được và hai câu hỏi mở riêng khoản chi; chưa đạt BA gate. | [Issue #09](../capmoney-overall/issues/09-ghi-chi.md), [core](../../docs/requirements/core-business.md), [flows](../../docs/requirements/user-flows.md), [workflow](../../docs/workflows/capmm-end-to-end.md), [R70–R71 riêng khoản thu](../../docs/requirements/42-step-9-income-round-1.md) |
| 14/10/2026 | Theo PM review bản nháp: rút Q1-B thành chọn sẵn ví đang sử dụng được tạo sớm nhất, vẫn đổi được; giữ AC và Q2. Q1–Q2 chờ Ninh, chưa đạt BA gate. | [Q&A](qna.md), [handoff](handoff.md) |
| 14/10/2026 | Ninh chọn Q1=A và Q2=A → R72–R73. Bổ sung BR-09-06..07, AC2/AC7; chỉ áp dụng cho khoản chi #09. BA đề xuất `BA_READY`, chờ PM review bản sau câu trả lời. | [Nhật ký #09](../../docs/requirements/43-step-9-expense-round-1.md), [Q&A](qna.md) |

PM đã góp ý bản nháp và review lại sau câu trả lời Q1–Q2; `BA_READY` đã đạt với bằng chứng trong [handoff](handoff.md). [Thiết kế kỹ thuật #09](technical-design.md) đã được review ở mức tài liệu nhưng còn `STORY_TECH_GAP`; chưa có code, build hoặc test thực chạy.
