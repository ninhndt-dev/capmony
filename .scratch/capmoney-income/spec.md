# Feature Spec: Story 08 – Danh mục mặc định và ghi khoản thu

**Story:** [story.md](story.md)
**BA gate:** `BA_READY` được CapmmPM xác nhận ngày 14/10/2026 sau Q1=A → R70 và Q2=B → R71; xem [story](story.md) và [handoff](handoff.md). `TECH_READY` chưa đánh giá.

## 1. Executive Summary / User Story statement

Là chủ tài khoản ghi chép, tôi muốn có sẵn danh mục và ghi một khoản thu vào ví của mình, để số dư hiện tại tăng đúng và còn đọc được sau tải lại. Phạm vi lát cắt là danh mục mặc định + tạo/đọc khoản thu qua web; [issue #08](../capmoney-overall/issues/08-danh-muc-mac-dinh-va-ghi-thu.md), [H05–H06](../../docs/requirements/system-functions.md).

## 2. Actor & Permission Matrix

| Actor | Danh mục và khoản thu của chính mình | Dữ liệu người khác |
| --- | --- | --- |
| Tài khoản ghi chép đã đăng nhập | Đọc mười danh mục mặc định; chọn danh mục, tạo khoản thu vào ví đang sử dụng, đọc lại số dư | Không được đọc/tạo/sửa |
| Tài khoản ghi chép khác | Chỉ dữ liệu của chính tài khoản ấy | Không được đọc/tạo/sửa |
| Tài khoản quản lý của Ninh | Không tạo/đọc khoản thu hoặc danh mục tài chính của người dùng | Không được đọc/tạo/sửa |

Nguồn quyền: [R08, R12](../../docs/requirements/00-intake.md), [actors](../../docs/requirements/actors-and-roles.md). Sửa/xóa thuộc Story khác.

## 3. Screen Definition & Entry Points

- **Ninh đã xác nhận:** mở luồng ghi thu/chi từ nút chung hoặc từ ví đang sử dụng. Vào từ ví thì ví đó được chọn sẵn; có thể đổi sang ví đang sử dụng khác. [F14](../../docs/requirements/user-flows.md)
- Story này kiểm lựa chọn **thu** và biểu mẫu có ví, số tiền, ngày, danh mục, ghi chú; không chốt bố cục, nhãn nút hay số màn. Từ nút chung khi có ≥2 ví đang sử dụng, form khoản thu ban đầu **chưa chọn ví**; người dùng tự chọn trước Lưu. [R70](../../docs/requirements/42-step-9-income-round-1.md)
- Chưa có ví: dẫn tạo ví đầu tiên trước ghi (F02/#06). Mọi ví đã ngừng: dẫn tạo/bật lại rồi quay về ghi (F20; tích hợp đầy đủ thuộc #18).

## 4. Main Flows (Happy Path)

1. Tài khoản ghi chép mới nhìn thấy đúng mười danh mục mặc định trong phạm vi của mình, dùng được cho khoản thu. [B16]
2. Chủ tài khoản mở ghi từ nút chung hoặc ví đang sử dụng; chọn **thu** và ví đang sử dụng của mình, chọn danh mục, nhập số nguyên VND dương, ngày hôm nay hoặc ngày đã qua, có thể để trống ghi chú. Nếu vào từ nút chung khi có ≥2 ví, form chưa chọn ví nên phải tự chọn; nếu vào từ ví, ví ấy được chọn sẵn và đổi được. [F14, R70, R25, R43, B02, B04, B12]
3. Bấm Lưu; chỉ khi kết quả đã xác nhận lưu, khoản thu thuộc đúng ví và số dư hiện tại của ví tăng đúng số tiền, kể cả ngày giao dịch trước hôm nay. Đọc lại sau tải trang vẫn thấy đúng. [B01, F09; S05]

## 5. Alternative & Exceptional Flows

- Ví vào từ ví A được chọn sẵn; đổi sang ví B đang sử dụng trước Lưu thì chỉ số dư B tăng. Không cho chọn ví đã ngừng. [F14, B04]
- Từ nút chung với ≥2 ví đang sử dụng, form khoản thu chưa chọn ví; bấm Lưu khi chưa chọn không tạo khoản thu hoặc đổi số dư. [R70]
- Không có ví: F02; tất cả ví ngừng: F20. Việc quay về form sau tạo/bật lại đầy đủ kiểm ở #18.
- Số tiền 0/âm/phần lẻ, thiếu danh mục/ví, ngày tương lai hoặc ví không được phép: không tạo khoản thu, không đổi số dư. [B02, R25, R43, B04, R12]
- Rời form khoản thu **trước Lưu** rồi quay lại cùng phiên có Internet: form trống; phần nhập dở không tạo khoản thu hay đổi số dư. [R71] Đóng hẳn trình duyệt trước Lưu: mở lại form trống, dữ liệu đã lưu vẫn còn. [F21]
- Nếu đã bấm Lưu và kết quả chưa được xác nhận, không báo thành công. Mất mạng, rời form sau Lưu và đóng/mở khi kết quả chưa rõ tuân theo F09/F25/R64; các ca đối chiếu và phục hồi được đặc tả, kiểm chứng tích hợp ở #33/#35. [F09, F25, R64]

## 6. Field-level Specifications & Validations

| Trường | Ninh đã xác nhận | Chưa rõ ở mức thiết kế |
| --- | --- | --- |
| Loại | Story này là **thu**; thu/chi dùng chung luồng, chi thuộc #09. [R07, F14] | Vị trí điều khiển chọn loại. |
| Ví | Bắt buộc là ví đang sử dụng của chủ; vào từ ví thì chọn sẵn và đổi được. Từ nút chung với ≥2 ví đang sử dụng, form khoản thu chưa chọn ví và phải tự chọn trước Lưu. [B04, F14, R12, R70] | Cách trình bày khi từ nút chung chỉ có một ví đang sử dụng do UX đề xuất; không đổi quy tắc ví hợp lệ trước Lưu. |
| Số tiền | Số nguyên VND **> 0**; không chấp nhận 0, âm, phần lẻ. [R16, B02] | Dấu phân cách, giới hạn kỹ thuật; không tự đặt mức tối đa sản phẩm. |
| Ngày | Hôm nay hoặc trước đó, không có ngày tương lai. [R38, R43] | Cách nhập/hiển thị ngày và mặc định ngày; không suy mặc định hôm nay. |
| Danh mục | Bắt buộc; chọn từ danh sách riêng của chủ, chung thu/chi. [R25, R45, B16] | Có tự chọn sẵn danh mục hay không; BA đề xuất để người dùng chọn rõ, không thành quy tắc đã chốt. |
| Ghi chú | Tùy chọn, có thể trống. [B12] | Độ dài và cách trình bày. |

## 7. System Parameters & Constants (`PARAM.*`)

- `PARAM.CURRENCY = VND` cho bản đầu. [R16]
- `PARAM.INCOME_AMOUNT_DOMAIN = số nguyên VND > 0`. [B02]
- `PARAM.INCOME_DATE_DOMAIN = hôm nay hoặc ngày trước đó`. [R43]
- `PARAM.DEFAULT_CATEGORIES = {Lương, Thưởng, Ăn uống, Đi lại, Mua sắm, Nhà ở, Hóa đơn, Sức khỏe, Giải trí, Khác}` cho **mỗi người mới**. [B16]
- Không có ngưỡng hiệu năng, độ dài ghi chú hay hạn giữ bản nháp được Ninh chốt riêng.

## 8. Business Rules (`BR-*`)

- `BR-08-01` **Ninh đã xác nhận:** mỗi người dùng mới có đúng mười danh mục B16 trong danh sách riêng, dùng chung cho thu và chi. **Ví dụ:** A mới có “Lương” và “Ăn uống”; B mới cũng có hai tên ấy nhưng A không đọc hay dùng dữ liệu danh mục của B. [B16, R12, R45]
- `BR-08-02` **Ninh đã xác nhận:** khoản thu phải gắn một danh mục của chủ tài khoản; danh mục từng dùng giữ lại và vẫn chọn được. **Ví dụ:** A ghi thu 200.000 VND với “Lương”; khoản thu có danh mục. Nếu “Lương” đã từng dùng, việc đổi/xóa sau này theo #20/#21, không làm khoản thu mất danh mục. [R25, B05, B16]
- `BR-08-03` **Ninh đã xác nhận:** khoản thu chỉ gắn ví đang sử dụng của chủ; vào từ ví thì chọn sẵn ví đó nhưng đổi được. **Ví dụ:** vào từ ví “Tiền mặt”, đổi sang ví “Ngân hàng” trước Lưu thì khoản thu gắn “Ngân hàng”; “Tiền mặt” không tăng. [F14, B04, R12]
- `BR-08-04` **Ninh đã xác nhận:** số tiền khoản thu là nguyên VND dương, ngày hôm nay hoặc trước đó, ghi chú có thể trống. **Ví dụ:** 125.000 VND ngày hôm qua, ghi chú trống là hợp lệ; 0, -1, 1,5 VND hoặc ngày mai bị từ chối. [B02, R43, B12]
- `BR-08-05` **Ninh đã xác nhận:** mỗi khoản thu còn hiệu lực cộng một lần vào số dư hiện tại của đúng ví, không phụ thuộc ngày ghi. **Ví dụ:** ví có số dư đầu -50.000 VND, ghi thu 120.000 VND cho hôm qua thì số dư hiện tại là 70.000 VND; tải lại vẫn là 70.000 VND. [B01, R30, B10]
- `BR-08-06` **Ninh đã xác nhận, quy tắc liên Story:** chưa xác nhận Lưu thì không báo thành công; một thao tác Lưu chưa rõ không được tạo bản trùng khi đối chiếu/thử lại. **Ví dụ:** bấm Lưu 120.000 VND rồi mất mạng: khi chưa biết kết quả, không báo đã lưu; sau đối chiếu nếu đã lưu, chỉ có một khoản 120.000 VND. #08 kiểm đường Lưu bình thường và không báo thành công sớm; các nhánh đối chiếu/phục hồi thuộc #33/#35. [F09, F25, R64]
- `BR-08-07` **Ninh đã xác nhận:** từ nút chung khi có ≥2 ví đang sử dụng, biểu mẫu **khoản thu** chưa chọn ví; người dùng tự chọn một ví hợp lệ trước Lưu. **Ví dụ:** có “Tiền mặt” và “Ngân hàng”, vào ghi thu từ nút chung thì chưa ví nào được chọn; Lưu 120.000 VND lúc đó bị chặn, chọn “Ngân hàng” rồi Lưu thì chỉ ví này tăng. [R70]
- `BR-08-08` **Ninh đã xác nhận:** rời biểu mẫu **khoản thu** trước Lưu rồi quay lại trong cùng phiên có Internet thì form trống; không tạo khoản thu hoặc đổi số dư từ phần nhập dở. **Ví dụ:** nhập 120.000 VND vào “Ngân hàng” rồi chuyển trang trước Lưu; quay lại thấy form trống, số dư “Ngân hàng” giữ nguyên. [R71]

## 9. State Machine & Transition Rules (`STATE.*`)

- `STATE.DRAFT`: đang nhập khoản thu, chưa bấm Lưu → chưa có khoản thu, số dư không đổi. Rời form rồi quay lại cùng phiên có Internet → form trống (R71); đóng trình duyệt trước Lưu → form trống sau mở lại (F21). Đây là hai nhánh trước Lưu khác F09/R64 sau Lưu.
- `STATE.SUBMITTED_UNKNOWN`: đã bấm Lưu, kết quả chưa rõ → hiện trạng thái kiểm tra, không báo thành công hoặc cho lưu lặp đúng thao tác; khi có Internet tự đối chiếu. [F09, R64]
- `STATE.SAVED`: kết quả xác nhận lưu → khoản thu đọc được và cộng đúng một lần vào số dư ví. [B01, F09]
- `STATE.CONFIRMED_NOT_SAVED`: sau đóng/mở nếu chắc chưa lưu → báo chưa lưu, form trống; nếu vẫn chưa rõ giữ trạng thái chờ. [F25]
- Sửa, xóa, khôi phục trạng thái khoản thu thuộc #11–#12.

## 10. Data Fallback & Offline Handling

- Chỉ dùng khi có Internet; không có ghi thu offline để đồng bộ sau. [R11]
- Dữ liệu tài khoản mới không nhập từ CapMoney cũ. [R03]
- F09/R64 giữ nội dung trong phiên khi kết quả Lưu chưa rõ; F25 mở lại sau đóng trình duyệt tự đối chiếu đúng thao tác. Chi tiết hợp đồng lưu/truy vấn cho Tech; #33/#35 là lát cắt kiểm chứng lỗi mạng chuyên biệt.

## 11. Error Messages & UI Registry (`ERR.*`)

- `ERR.INCOME_INVALID_AMOUNT`: báo số tiền phải là nguyên VND dương; không lưu. `ERR.INCOME_FUTURE_DATE`: báo ngày tương lai không hợp lệ. `ERR.INCOME_MISSING_CATEGORY`: yêu cầu chọn danh mục. `ERR.INCOME_INVALID_WALLET`: yêu cầu ví đang sử dụng của chủ. Đây là **mã BA đề xuất để truy vết**, chưa phải mã/API/câu chữ đã duyệt. [B02, R43, R25, B04]
- Trạng thái “đang kiểm tra”, “đã lưu”, “chưa lưu” phải phân biệt theo F09/F25/R64. Câu chữ và bố cục do UX/Tech đề xuất, không tự xem là quyết định sản phẩm.

## 12. Security & Privacy Constraints

- Chỉ chủ tài khoản ghi chép tạo, xem danh mục/khoản thu và đọc số dư ví của mình; quản lý không xem dữ liệu tài chính. Kiểm quyền cả tại đường thao tác trực tiếp, không chỉ trên UI. [R08, R12; actors]
- Ví và danh mục người khác không được gắn vào khoản thu, kể cả yêu cầu gửi trực tiếp hoặc biểu mẫu giữ dữ liệu cũ. Hợp đồng xác thực/ủy quyền do Tech thiết kế.

## 13. Third-party Integrations

Không áp dụng tích hợp dịch vụ ngoài riêng cho Story #08. Danh mục và khoản thu thuộc dữ liệu tài khoản; nền tảng lưu trữ/API chưa được chọn.

## 14. Audit Log & History Requirements

- Khoản thu đã lưu là giao dịch có ngày, số tiền, ví, danh mục và ghi chú tùy chọn; lịch sử chung/lọc ví và thứ tự dòng thuộc #10. [R07, R25, F15–F16]
- Bản đầu không cần hiện số dư sau từng dòng lịch sử. [B17] Không có yêu cầu nhật ký kiểm toán riêng đã được Ninh xác nhận; log kỹ thuật nếu cần không thay thế khoản thu người dùng đọc được.

## 15. NFR (Performance, Concurrency Bounds)

- Web mobile trên iPhone/Android, khoảng 10 người; dữ liệu đã xác nhận lưu phải đọc lại đúng chủ sau tải lại và trên điện thoại khác có Internet. [R05–R06, R14, R27; S05]
- Hai giao dịch **khác nhau** gần đồng thời đều được giữ, số dư tính đúng. [B24] Kiểm chứng đồng thời hai điện thoại chuyên biệt ở #29/P02; #08 không tự khẳng định kết quả test ấy.
- Chưa có số liệu thời gian đáp ứng hoặc phiên bản trình duyệt được chốt; Tech đề xuất sau gate BA.

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: tài khoản ghi chép mới thấy đúng mười tên B16 trong danh sách riêng và chọn được từng tên cho khoản thu; tài khoản khác không đọc được danh sách ấy. [B16, R12]
- `AC2`: từ một ví đang sử dụng, mở ghi thu thấy ví ấy được chọn sẵn và đổi sang ví đang sử dụng khác của mình được. Từ nút chung với ≥2 ví đang sử dụng, mở ghi thu **chưa chọn ví**, phải tự chọn ví đang sử dụng trước Lưu; ví chưa chọn thì không tạo khoản thu hoặc đổi số dư. [F14, R70]
- `AC3`: lưu khoản thu vào ví đang sử dụng của chủ với danh mục của chủ, số nguyên VND dương, ngày hôm nay hoặc trước đó, ghi chú trống hoặc có nội dung; chứng minh bản ghi đã lưu thuộc đúng ví/danh mục và số dư đọc lại đúng. Không đòi màn lịch sử của #10. [R25, R43, B01–B02, B04, B12]
- `AC4`: ví có số dư trước Lưu -50.000 VND; lưu khoản thu 120.000 VND ngày hôm qua vào ví ấy thì sau xác nhận và tải lại, số dư hiện tại 70.000 VND; ví khác không đổi. [B01]
- `AC5`: thiếu ví/danh mục, ví đã ngừng hoặc không thuộc chủ, tiền 0/âm/phần lẻ, ngày tương lai thì không lưu khoản thu và không đổi số dư. [B02, B04, R12, R25, R43]
- `AC6`: tài khoản B và tài khoản quản lý không tạo/đọc khoản thu, danh mục hoặc số dư của tài khoản A qua giao diện hay đường thao tác trực tiếp. [R08, R12]
- `AC7`: nhập khoản thu 120.000 VND vào ví “Ngân hàng”, **rời form trước khi bấm Lưu** rồi quay lại cùng phiên có Internet: form trống, không có khoản thu mới và số dư “Ngân hàng” không đổi. Đóng hẳn trình duyệt trước Lưu rồi mở lại cùng tài khoản có Internet cũng mở form trống; khoản đã lưu trước đó vẫn còn. [R71, F21]
- `AC8`: trên đường Lưu bình thường, chỉ báo khoản thu đã lưu sau khi kết quả được xác nhận; sau thông báo đó, bản ghi và số dư đọc lại đúng như AC3–AC4. Khi chưa được xác nhận, không báo thành công. Ca mất mạng, Lưu chưa rõ, rời form sau Lưu và đóng/mở được kiểm ở #33/#35 theo F09/F25/R64. [B01, F09]

## 17. Figma Mapping / Visual References

Không áp dụng Figma mapping: repo chưa có Figma được duyệt. Ảnh CapMoney cũ chỉ tham khảo giao diện, không là nguồn hành vi hay tiêu chí pixel. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

- #06 và #07 cấp ví đã lưu để tạo khoản thu và thử đổi ví. Issue #08 ghi cả hai; PM/Tech chốt phân loại `HARD`/`CONTRACT` cho Dev sau khi thiết kế.
- #04 cấp phiên/quyền tài khoản qua #06/#07. #09 dùng nền khoản thu để thêm chi; #10 hiển thị lịch sử; #20–#21 quản lý danh mục; #18 kiểm đầy đủ trường hợp mọi ví ngừng; #29 kiểm đồng thời. #33/#35 đặc tả và kiểm chứng tích hợp các nhánh F09/F25/R64 sau Lưu chưa rõ, gồm mất mạng và đóng/mở; chúng không là điều kiện để kiểm AC #08 trên đường Lưu bình thường.
- Phụ thuộc chức năng không tự chặn BA soạn spec. #07 hiện `BA_READY` nhưng còn `STORY_TECH_GAP`; #06 chưa có gate mới. [Story #07](../capmoney-add-wallet/story.md), [spec #06](../capmoney-first-wallet/spec.md)

## 19. Q&A Log & Resolved Decisions

- **Ninh đã xác nhận:** B01–B02, B04–B05, B12, B16, F02, F09, F14, F20–F21, F25, R25, R43, R45, R64. Q1=A → R70 và Q2=B → R71 xác nhận riêng cho **khoản thu #08**; xem [nhật ký #08](../../docs/requirements/42-step-9-income-round-1.md), [Q&A](qna.md), [core](../../docs/requirements/core-business.md), [flows](../../docs/requirements/user-flows.md), [intake](../../docs/requirements/00-intake.md).
- **Chưa rõ ngoài phạm vi #08:** không suy R70–R71 cho **khoản chi #09**. Cách trình bày khi nút chung chỉ có một ví là chi tiết UX, nhưng Lưu vẫn đòi một ví hợp lệ của chủ.
- **BA/Tech đề xuất:** mã `ERR.*`, câu chữ, bố cục, giới hạn kỹ thuật trường nhập và hợp đồng lưu. Chưa phải quyết định của Ninh.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 14/10/2026 | Tạo bản BA bước 9 cho Story #08, 20 mục, BR có ví dụ, AC quan sát được và hai câu hỏi mở; chưa đề xuất gate. | [Issue #08](../capmoney-overall/issues/08-danh-muc-mac-dinh-va-ghi-thu.md), [core](../../docs/requirements/core-business.md), [flows](../../docs/requirements/user-flows.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
| 14/10/2026 | Theo PM review bản nháp: làm rõ lựa chọn Q1-B, giới hạn AC8 ở Lưu bình thường, chuyển kiểm chứng lỗi mạng/đóng mở sang #33/#35, và giữ màn lịch sử ngoài AC3. Chưa đạt BA gate. | [Q&A](qna.md), [handoff](handoff.md) |
| 14/10/2026 | Ninh chọn Q1=A và Q2=B → R70–R71. Bổ sung BR-08-07..08, AC2/AC7; chỉ áp dụng cho khoản thu #08. BA đề xuất `BA_READY`, chờ PM review bản sau câu trả lời. | [Nhật ký #08](../../docs/requirements/42-step-9-income-round-1.md), [Q&A](qna.md) |

PM đã góp ý bản nháp và review lại sau câu trả lời Q1–Q2; `BA_READY` đã đạt với bằng chứng trong [handoff](handoff.md). [Thiết kế kỹ thuật #08](technical-design.md) đã được review ở mức tài liệu nhưng còn `STORY_TECH_GAP`; chưa có code, build hay test thực chạy.
