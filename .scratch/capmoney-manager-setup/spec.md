# Feature Spec: CM-01 – Thiết lập và đăng nhập tài khoản quản lý

**Story:** [story.md](story.md) · **Planio:** [issue 8](https://tanninh.planio.com/issues/8)
**BA gate:** `BA_READY` ngày 14/10/2026: BA lập bản 20 mục, PM độc lập `/root/cm01_pm_ba` rà bản có AC1–AC6 và nguồn; xem [handoff](handoff.md). Bản đặc tả cũ không tự đạt gate mới.

## 1. Executive Summary / User Story statement

Ninh cần tài khoản quản lý lời mời riêng để về sau mời người dùng. CM-01 cho phép Ninh nhận liên kết thiết lập gửi tới email quản lý đã cung cấp trước khi mở hệ thống, xác minh quyền dùng email bằng liên kết, đặt mật khẩu, đăng nhập và mở khu vực quản lý. Chỉ có một tài khoản quản lý, không có tự đăng ký. [F11, F23, Story #01]

## 2. Actor & Permission Matrix

| Actor | Thiết lập | Đăng nhập và xem khu vực quản lý | Căn cứ |
| --- | --- | --- | --- |
| Ninh, chủ email quản lý đã cấu hình | Dùng liên kết còn hạn, chưa dùng; đặt mật khẩu một lần | Có sau thiết lập, bằng phiên quản lý hợp lệ | F11, F23; quyền bước 3 |
| Người ngoài hoặc email khác | Không thể yêu cầu đổi email đích, tự đăng ký hoặc dùng liên kết sai/hết hạn | Không được truy cập trực tiếp | R13; Story #01 |
| Tài khoản ghi chép cá nhân | Không cấp quyền quản lý | Không được truy cập khu vực quản lý | R12; quyền bước 3; kiểm tích hợp sâu khi #04 có tài khoản ghi chép |

Email quản lý và tài khoản ghi chép cá nhân của Ninh là hai tài khoản dùng hai email khác nhau. [quyền bước 3]

## 3. Screen Definition & Entry Points

- **Ninh đã xác nhận hành vi:** liên kết đến qua email, Ninh mở để xác minh và đặt mật khẩu, sau đó đăng nhập. [F11, F23]
- **BA đề xuất giao diện tối thiểu:** trang từ liên kết có ô mật khẩu và xác nhận mật khẩu; trang đăng nhập có email và mật khẩu; khu vực quản lý hiển thị email tài khoản đang đăng nhập. Bố cục, nhãn và hình ảnh chưa được Ninh duyệt riêng.
- Không có form nhập email để tạo quản lý hay nút đăng ký công khai. Trang/khu vực quản lý phải chặn truy cập bằng URL trực tiếp khi chưa có phiên hợp lệ.

## 4. Main Flows (Happy Path)

1. Người triển khai cấu hình email quản lý riêng Ninh đã khai trong môi trường khởi tạo; hệ thống phát hành liên kết đầu tiên và gửi tới đúng email đó. Không cần Ninh nhập email đích trên web. [F11]
2. Ninh mở liên kết còn hạn và chưa dùng. Hệ thống kiểm liên kết trước khi cho hoàn tất thiết lập. [F11, F23]
3. Ninh nhập và xác nhận mật khẩu theo chính sách bảo mật được Tech duyệt; hệ thống tạo duy nhất một tài khoản quản lý đã xác minh email và tiêu thụ liên kết như một thao tác. [F11, F23]
4. Ninh đăng nhập bằng email quản lý và mật khẩu vừa đặt. Phiên hợp lệ mở khu vực quản lý tối thiểu; tải lại trang vẫn mở được. [R22, Story #01]

## 5. Alternative & Exceptional Flows

- Liên kết sai mục đích, không tồn tại, đã dùng hoặc hết hạn: không hoàn tất, không tạo quản lý. Liên kết hết hạn dẫn luồng xin lại thuộc #02; CM-01 chỉ báo không thể dùng liên kết cũ. [F23–F24]
- Mật khẩu và xác nhận không khớp hoặc không đạt chính sách bảo mật Tech đã duyệt: chưa tạo quản lý, giữ khả năng sửa và thử lại khi liên kết còn hợp lệ. Chính sách cụ thể là quyết định an toàn kỹ thuật, không gán là quyết định sản phẩm của Ninh.
- Hai lần hoàn tất cùng liên kết gần đồng thời hoặc bấm xác nhận lặp: tối đa một lần thành công, chỉ một quản lý tồn tại; lần còn lại được báo liên kết không còn dùng được. [Story #01]
- Sai email/mật khẩu khi đăng nhập: không tạo phiên quản lý. Phiên hết hạn, bị thu hồi hoặc không có: từ chối mọi đường xem dữ liệu quản lý và đưa về đăng nhập.
- Gửi email không được nhà cung cấp nhận: không báo đã gửi thành công; Tech phải có trạng thái an toàn để không phát sinh hai liên kết hợp lệ.

## 6. Field-level Specifications & Validations

| Trường | Quy tắc | Nguồn / trạng thái |
| --- | --- | --- |
| Email quản lý | Cấu hình riêng bởi người triển khai; người dùng web không chọn/đổi trong CM-01 | **Ninh đã xác nhận** F11; email thật là đầu vào vận hành |
| Liên kết thiết lập | Một lần, đúng email/mục đích, hết hạn 3 ngày từ lúc gửi | **Ninh đã xác nhận** F11/F23 về 3 ngày từ lúc gửi; cách xác định lúc gửi bằng phản hồi nhà cung cấp là **diễn giải Tech/BA**, xem mục 7 |
| Mật khẩu mới | Ninh tự đặt; không lưu hoặc log dạng đọc được | **Ninh đã xác nhận** tự đặt; ngưỡng bảo mật cụ thể là **Tech đề xuất** |
| Xác nhận mật khẩu | Khớp mật khẩu mới trước khi gửi | **BA đề xuất** để tránh gõ nhầm |
| Email + mật khẩu đăng nhập | Phải khớp tài khoản quản lý đã thiết lập | **Ninh đã xác nhận** R22/F11 |

## 7. System Parameters & Constants (`PARAM.*`)

- `PARAM.SETUP_LINK_TTL = 72 giờ` kể từ lúc nhà cung cấp nhận yêu cầu gửi và hệ thống ghi nhận đã gửi. Đây là cách Tech/BA đo mốc “kể từ lúc gửi” của F23; không đồng nhất với lúc email tới hộp thư người nhận.
- `PARAM.MANAGER_ACCOUNT_LIMIT = 1`. [F11; quyền bước 3]
- Thời hạn phiên, ngưỡng khóa/giới hạn đăng nhập và độ dài mật khẩu chưa được Ninh chốt; Tech chọn giá trị an toàn, PM rà tác động sử dụng. Không dùng mặc định thư viện khi trái hành vi đã xác nhận.

## 8. Business Rules (`BR-*`)

- `BR-01-01`: chỉ email quản lý đã cấu hình được nhận liên kết đầu tiên; không có tự đăng ký. **Ví dụ:** người ngoài nhập email khác hoặc gọi trực tiếp đường đăng ký không thể tạo quản lý. [F11, R13]
- `BR-01-02`: chỉ liên kết còn hạn, chưa dùng, đúng mục đích mới cho Ninh đặt mật khẩu để lập quản lý. **Ví dụ:** mở ở giờ 71:59 còn có thể hoàn tất; tại/qua mốc 72:00 thì không thể. [F23]
- `BR-01-03`: một lần thiết lập chỉ tạo tối đa một quản lý. **Ví dụ:** hai yêu cầu hoàn tất cùng liên kết đến gần nhau chỉ một yêu cầu thành công; đọc dữ liệu cuối chỉ có một tài khoản quản lý. [Story #01]
- `BR-01-04`: chỉ phiên quản lý hợp lệ xem khu vực quản lý, kể cả tải lại hoặc gõ URL trực tiếp. **Ví dụ:** sau đăng nhập tải lại vẫn thấy email quản lý; trình duyệt chưa đăng nhập không xem được. [Story #01]

## 9. State Machine & Transition Rules (`STATE.*`)

- Hành vi công khai: chưa có quản lý → liên kết được gửi → liên kết hợp lệ mở được biểu mẫu → hoàn tất thành công tạo đúng một quản lý và liên kết không dùng lại được.
- Trước khi hoàn tất: liên kết hết hạn hoặc bị thay thế thì không còn dùng được. Thay liên kết hết hạn là #02, không phải CM-01. [F24]
- Sau khi có quản lý: luồng thiết lập đầu tiên đóng vĩnh viễn; quản lý có thể đăng nhập, có phiên hợp lệ hoặc hết hạn. Mô hình dữ liệu/trạng thái nội bộ do Tech thiết kế.

## 10. Data Fallback & Offline Handling

- CapMoney bản đầu yêu cầu Internet; không thiết lập tài khoản hay đăng nhập offline. [R11]
- Bản mới bắt đầu dữ liệu trống, không nhập tài khoản/dữ liệu từ app cũ. [R03]
- Khi gửi/hoàn tất chưa rõ kết quả, không báo thành công nếu chưa xác nhận; thử lại phải không tạo quản lý thứ hai. Tech thiết kế cách báo và đối chiếu, PM kiểm trải nghiệm.

## 11. Error Messages & UI Registry (`ERR.*`)

- `ERR.SETUP_LINK_INVALID`: liên kết không còn dùng được; không lộ email đích hoặc bí mật. Nếu hết hạn, hướng sang luồng #02 khi #02 có mặt.
- `ERR.PASSWORD_INVALID`: cho sửa mật khẩu/xác nhận khi liên kết còn hợp lệ; điều kiện cụ thể theo chính sách Tech được PM duyệt.
- `ERR.LOGIN_FAILED`: không cho biết email hay mật khẩu sai; không tạo phiên.
- `ERR.ACCESS_DENIED`: khách chưa đăng nhập hoặc không có quyền quản lý không thấy nội dung khu vực quản lý.
- Câu chữ/vị trí chính xác là đề xuất UX, chưa được Ninh duyệt riêng.

## 12. Security & Privacy Constraints

- Token liên kết phải khó đoán, đúng mục đích, không hiện trong log, không tái sử dụng; mật khẩu không lưu hoặc ghi log dạng đọc được. Quyền và phiên phải kiểm ở server trên mỗi đường truy cập, không chỉ ẩn UI. [F11, Story #01]
- Không có public signup hoặc endpoint quản trị vượt phạm vi; không tự bật quyền xem danh sách người dùng, dữ liệu tài chính, khóa hay đóng vai tài khoản. [R12; quyền bước 3]
- Secret email, mật khẩu cơ sở dữ liệu, khóa phiên và email quản lý thật chỉ ở môi trường server, không ở Git hoặc trình duyệt.

## 13. Third-party Integrations

- Cần nơi gửi email và lưu bền trạng thái tài khoản/liên kết/phiên. Nhà cung cấp cụ thể chưa được Ninh xác nhận; Tech chọn và review độc lập trước Dev. [technical-options](technical-options.md)
- Ninh đã chọn kiểm bằng hộp thư thử và dữ liệu thử trước; thử email thật khi có hạ tầng. [Q&A CM01-Q1](qna.md)

## 14. Audit Log & History Requirements

- Không có yêu cầu sản phẩm về lịch sử thao tác quản lý trong CM-01. Log vận hành nếu Tech cần chỉ ghi sự kiện an toàn, không ghi token, mật khẩu hay nội dung nhạy cảm.
- Không áp dụng lịch sử giao dịch tài chính; CM-01 không tạo hoặc đọc dữ liệu ghi chép.

## 15. NFR (Performance, Concurrency Bounds)

- Bản đầu phục vụ khoảng 10 người, web ưu tiên điện thoại iPhone và Android; chưa có SLA hoặc ngưỡng thời gian phản hồi được Ninh chốt. [R05, R14]
- Bắt buộc an toàn dưới xác nhận lặp và hai yêu cầu hoàn tất gần đồng thời; thử hạn bằng đồng hồ điều khiển được và dữ liệu khởi tạo riêng. [Story #01]
- Không dùng dữ liệu thật để thử hủy/tạo lại quản lý; kiểm thử phải dùng môi trường cô lập.

## 16. Acceptance Criteria (`AC1..ACn`)

- `AC1`: chỉ email quản lý đã cấu hình nhận liên kết thiết lập đầu tiên; không có form hoặc API cho người ngoài tự chọn email/tạo quản lý. [F11]
- `AC2`: liên kết đúng mục đích, chưa dùng, còn trong 72 giờ cho đặt mật khẩu; liên kết sai/đã dùng/hết hạn không tạo quản lý. [F11, F23]
- `AC3`: Ninh đặt mật khẩu và đăng nhập được; phiên quản lý hợp lệ mở được khu vực quản lý tối thiểu sau tải lại. Đăng nhập sai không tạo phiên; khách, phiên hết hạn hoặc bị thu hồi truy cập URL trực tiếp đều bị chặn ở server. [R22, Story #01]
- `AC4`: sau khi đã thiết lập, không thể thiết lập thêm quản lý qua liên kết cũ, email khác hoặc đường gọi trực tiếp; tài khoản quản lý không tự có quyền ngoài phạm vi. [R12/R13, Story #01]
- `AC5`: bấm xác nhận lặp hoặc hai yêu cầu hoàn tất cùng liên kết gần đồng thời chỉ tối đa một thành công và cuối cùng đúng một tài khoản quản lý. [Story #01]
- `AC6` (**điều kiện an toàn kỹ thuật, PM rà trải nghiệm**): nhà cung cấp từ chối yêu cầu gửi hoặc kết quả gửi chưa rõ thì hệ thống không báo đã gửi thành công; thử lại an toàn và không để hai liên kết thiết lập còn hiệu lực cùng lúc. Việc email thực sự tới hộp thư chỉ được xác nhận bằng kiểm hộp thư thử/thật, không suy từ phản hồi nhà cung cấp.

`BA_READY` cần PM đối chiếu nguồn và 20 mục; `QA_PASS` cần case thực chạy trên build. Hai trạng thái này không suy từ tài liệu hiện có.

## 17. Figma Mapping / Visual References

Không áp dụng Figma vì repo chưa có Figma được duyệt. Ảnh CapMoney cũ chỉ tham khảo giao diện, không chốt bố cục/pixel hay hành vi của CM-01. [R02]

## 18. Dependencies (Cross-feature / Cross-story)

- CM-01 không bị Story khác chặn. CM-02 phụ thuộc khả năng phát hành/tiêu thụ liên kết; CM-03 dùng phiên/quyền quản lý; CM-04 là tài khoản ghi chép riêng. Chỉ CM-01 được triển khai trong vòng này.
- Hợp đồng nền tảng email, lưu trữ và phiên của CM-01 sẽ ảnh hưởng #02/#03/#04; Tech cần chốt ranh giới và kiểm tương thích trước khi PM chia ticket Dev.

## 19. Q&A Log & Resolved Decisions

- **Ninh đã xác nhận:** F11/F23–F24 về email thiết lập, liên kết một lần 3 ngày, đặt mật khẩu, đăng nhập; R12/R13 về tách tài khoản quản lý và giới hạn quyền. Xem [intake](../../docs/requirements/00-intake.md), [actors](../../docs/requirements/actors-and-roles.md), [flows](../../docs/requirements/user-flows.md).
- **Ninh đã xác nhận CM01-Q1:** dùng hộp thư và dữ liệu thử để kiểm trước, email thật sau khi có hạ tầng. [Q&A](qna.md)
- **BA/Tech đề xuất:** khu vực quản lý tối thiểu, xác nhận mật khẩu, thông báo lỗi, kỹ thuật gửi mail/phiên. **Chưa rõ cho vận hành:** email và tên miền thật; không cần để chốt BA hoặc thử cô lập.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| Trước 14/10/2026 | Đặc tả hành vi CM-01 qua BA → PM → Tech → PM theo quy trình cũ; chưa đạt gate workflow mới | Lịch sử Git của file này, [Story nguồn](../capmoney-overall/issues/01-thiet-lap-quan-ly.md) |
| 14/10/2026 | Chuyển thành spec 20 mục để xét `BA_READY`, tách quyết định của Ninh khỏi đề xuất UI/Tech, ghi CM01-Q1 và AC1–AC5 | [story](story.md), [Q&A](qna.md), [workflow](../../docs/workflows/capmm-end-to-end.md) |
| 14/10/2026 | Theo PM review độc lập: mở AC3 cho đăng nhập sai/phiên hết hạn, thêm AC6 cho gửi thất bại/chưa rõ và xác định mốc 72 giờ theo phản hồi nhà cung cấp | [handoff](handoff.md) khi PM chốt gate |
| 14/10/2026 | PM độc lập rà lại 20 mục/AC1–AC6 và xác nhận `BA_READY`; sửa nhãn nguồn mục 6, giữ diễn giải thời điểm gửi là Tech/BA đề xuất | [handoff](handoff.md) |
