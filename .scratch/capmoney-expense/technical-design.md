# Technical Design: Story #09 – Ghi khoản chi

**Trạng thái CapmmTech:** bản thiết kế để review từ `BA_READY` đã được CapmmPM xác nhận trong [story](story.md) và [handoff](handoff.md). **`STORY_TECH_GAP`; chưa `TECH_READY`** vì nền tảng và hợp đồng dùng chung chưa được chứng minh. Chưa có code ứng dụng, quyết định stack/schema/API, build hoặc test thực chạy.

## Nguồn, phạm vi và ranh giới tin cậy

- Nguồn hành vi: [spec #09, BR-09-01..07 và AC1–AC8](spec.md), [Q&A](qna.md), [R72–R73](../../docs/requirements/43-step-9-expense-round-1.md), [nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md), [issue #09](../capmoney-overall/issues/09-ghi-chi.md). [Thiết kế #08](../capmoney-income/technical-design.md) là nền đề xuất cho danh mục, giao dịch, số dư và lưu một lần nhưng còn `STORY_TECH_GAP`; [thiết kế #07](../capmoney-add-wallet/technical-design.md) ghi các gap ví/phiên/VND và request đến muộn.
- #09 dùng **chính danh sách danh mục thu/chi riêng của chủ** từ #08 và các ví đã lưu từ #06/#07 để tạo, đọc lại một khoản **chi** và số dư hiện tại. Không cấp lại danh mục mặc định, không tạo một danh sách “danh mục chi” thứ hai. Không xây màn lịch sử #10, sửa/xóa/khôi phục #11–#12, chuyển tiền #13, quản lý danh mục #20–#21, điều hướng đầy đủ khi mọi ví ngừng #18, kiểm chứng đồng thời hai điện thoại #29 hoặc phục hồi Lưu chưa rõ #33/#35. Các quy tắc chung vẫn ràng buộc hợp đồng #09.
- Trình duyệt gửi ý định chi và lựa chọn, nhưng phía tin cậy lấy chủ/vai trò từ phiên #04 và tự xác nhận ví, danh mục, ngày, số tiền, trạng thái tài khoản tại lúc ghi. Không tin chủ, số dư dự kiến, tiền tệ hoặc kết quả “đã lưu” do trình duyệt khai. Nơi lưu bền quyết định khoản chi và số dư; cách tổ chức cụ thể chờ architecture review.

## Hợp đồng chung và khác biệt của khoản chi

| Khái niệm | Bất biến cho #09 | Nguồn / điểm nối |
| --- | --- | --- |
| Chủ và quyền | Chỉ tài khoản ghi chép đã xác thực tạo/đọc khoản chi, danh mục, ví và số dư của chính mình. B, quản lý và khách không đọc hay ghi hộ A; kết quả thao tác Lưu cũng chỉ thuộc chủ. | #04, R08/R12/R63; cùng ranh giới quyền của #08. |
| Danh mục | Một danh mục bắt buộc của chủ từ danh sách chung thu/chi, gồm bộ mặc định #08 và danh mục người ấy quản lý sau này. Gắn theo định danh ổn định để đổi tên về sau không mất liên kết; mục đã dùng vẫn chọn được theo B05. | #08 cấp danh sách và khởi tạo mười mục đúng một lần; #20/#21 quản lý về sau. Không tái seed lúc vào form chi. |
| Ví | Khoản chi mới chỉ gắn ví **đang sử dụng** của chủ ở thời điểm Lưu, kể cả người dùng đổi ví trên form. Ví khác chủ hoặc đã ngừng bị từ chối dù dữ liệu UI cũ còn giữ. | #06/#07 cấp ví và số dư đầu, #18 đổi trạng thái. |
| Khoản chi | Một giao dịch còn hiệu lực, loại chi, đúng chủ/ví/danh mục, số nguyên VND dương, ngày lịch hôm nay hoặc trước đó, ghi chú tùy chọn và định danh ổn định để đọc lại. | Cùng khái niệm giao dịch #08, nhưng tác động B01 là **trừ** tiền; #10 trình bày sau này. |
| Số dư hiện tại | B01: số dư đầu + thu/chuyển đến còn hiệu lực − chi/chuyển đi còn hiệu lực. Mỗi khoản chi #09 đã xác nhận trừ **đúng một lần** khỏi đúng ví, bất kể ngày ghi. Số dư có thể âm; không kiểm “đủ tiền” để chặn chi. | Dùng cùng cơ chế tính/duy trì số dư của #08/#13/#16; R39/B01/B02. |
| Thao tác Lưu | Cùng một ý định Lưu có định danh ổn định gắn chủ và nội dung; retry/bấm lặp không tạo thêm khoản chi hoặc trừ số dư lần hai. Ý định độc lập dù nội dung giống nhau vẫn là giao dịch riêng. | Chung hợp đồng #08 và các lát phục hồi #33/#35; chưa chọn cơ chế lưu. |

Số **tiền nhập** phải nguyên VND `> 0`; **số dư kết quả** có thể âm. Ví dụ số dư 50.000 VND trừ khoản chi 120.000 VND ngày hôm qua ra -70.000 VND. Không làm tròn, tràn, đổi dấu hoặc tự đặt trần sản phẩm. Biểu diễn VND chính xác từ nhập → xử lý → lưu → đọc, kể cả tổng âm; miền kỹ thuật và cách nhập có dấu phân cách cần PM rà sau spike chung #07/#08. Ngày giao dịch là **ngày lịch**, không được ở tương lai; mốc “hôm nay” khi thiết bị và hệ thống khác múi giờ cần quyết định nhất quán, không tự suy mặc định ngày.

## Luồng đọc, bản nháp và Lưu bình thường

1. Form chi đọc ví đang sử dụng và danh mục chung của đúng chủ. Vào từ ví A đang sử dụng thì A được chọn sẵn và có thể đổi sang ví B đang sử dụng của mình (F14). Vào từ nút chung có **ít nhất hai** ví đang sử dụng thì form **chưa chọn ví** và người dùng phải chọn trước Lưu (R72). Với đúng một ví từ nút chung, cách trình bày do UX/PM rà, nhưng Lưu vẫn cần ví hợp lệ. R72 là quyết định riêng cho **chi**; không tự áp cho chuyển tiền hoặc tạo ví.
2. Trước khi bấm Lưu, nội dung chỉ là bản nháp của form, không có khoản chi/số dư mới. Rời form chi sang trang khác rồi quay lại cùng phiên có Internet thì form trống (R73); đóng hẳn trình duyệt trước Lưu rồi mở lại cũng trống (F21). **F08 khác trường hợp này:** nếu dữ liệu từ điện thoại thứ hai tự tải khi người dùng vẫn ở form đang mở, phần nhập dở phải được giữ; cập nhật danh sách/số dư không được tự biến thành sự kiện rời form. Nếu ví/danh mục vừa thay đổi trạng thái, kiểm lại khi Lưu và giải thích lỗi, không âm thầm gửi bản cũ.
3. UI kiểm trường để phản hồi dễ hiểu; phía tin cậy kiểm lại trực tiếp. Thiếu ví/danh mục, ví đã ngừng/khác chủ, danh mục khác chủ, tiền 0/âm/phần lẻ/chữ/ngoài miền biểu diễn, ngày tương lai đều không tạo giao dịch hay đổi số dư. Ghi chú được trống. Không cho kiểm “số dư sau chi âm” thành lỗi. Danh mục từng dùng vẫn hợp lệ theo B05.
4. Khi nhận Lưu, phía tin cậy xác nhận phiên và trạng thái **hiện hành** của tài khoản, ví, danh mục, số tiền và ngày. Một ranh giới ghi bền phải giữ khoản chi, kết quả ý định Lưu và tác động số dư nhất quán: sau báo thành công, bản ghi đọc được và B01 phản ánh đúng một lần trừ. Chưa chốt tính số dư từ giao dịch hay duy trì giá trị vật hóa; phương án được chọn phải ngăn lost update và tương thích thu #08, chuyển #13, sửa/xóa/khôi phục sau này. Không để UI cộng/trừ cục bộ làm nguồn sự thật.
5. Chỉ báo “đã lưu” sau xác nhận bền cho **đúng thao tác** và đọc lại đúng khoản chi/số dư. Trước đó không báo thành công. Nếu phản hồi chậm/mất hoặc chưa phân định, giữ định danh/trạng thái của thao tác để #33/#35 đối chiếu; không tạo định danh mới để retry cùng ý định. R73/F21 chỉ xóa bản nháp **trước Lưu**, không xóa dấu vết của thao tác đã gửi. Không kết luận “chưa lưu” từ timeout hoặc việc danh sách chưa hiện giao dịch.

## Lỗi, đồng thời và bảo vệ dữ liệu

| Tình huống | Hành vi hợp đồng |
| --- | --- |
| Đầu vào sai, ví/danh mục không thuộc chủ hoặc ví vừa ngừng | Từ chối toàn bộ; số khoản chi và số dư giữ nguyên. Lỗi không tiết lộ sự tồn tại hay chi tiết dữ liệu của chủ khác. |
| Số dư ví không đủ hoặc âm sau chi | Vẫn ghi nếu mọi điều kiện khác hợp lệ; số dư mới có thể âm, chính xác theo B01/R39. |
| Hai request cùng ý định đến gần đồng thời hoặc retry | Tối đa một khoản chi và một lần trừ; cùng định danh với nội dung khác bị từ chối. Khóa nút UI không thay bảo đảm bền/atomic. |
| Hai ý định khác nhau gần đồng thời | Cả hai khoản đã báo lưu còn đọc được, tổng số dư trừ đủ cả hai; #29 kiểm chuyên biệt trên hai điện thoại và nhiều loại giao dịch. |
| Đã ghi nhưng mất phản hồi; request cũ có thể tới muộn | Giữ trạng thái chưa rõ để đối chiếu đúng ý định ở #33/#35. Chỉ báo “chắc chưa lưu” khi có cơ chế bảo đảm request cũ không thể ghi về sau; gap P0/P1 chung #07/#08. |
| Phiên hết hạn, tài khoản đã xóa, đổi tài khoản cùng thiết bị | Không tạo/đọc khoản chi bằng phiên cũ; chỉ đúng chủ sau xác thực được thấy kết quả pending. Phụ thuộc #04/R63. |

Quyền được kiểm trên đường đọc danh mục/ví/số dư, tạo và đọc khoản chi, và đọc kết quả thao tác, không chỉ khi render UI. Dấu vết thao tác trên thiết bị phải gắn đúng chủ, tách bản nháp chưa Lưu, và không lộ khoản chi/số dư cho tài khoản B hay quản lý đăng nhập sau đó. Log kỹ thuật nếu cần chỉ đủ chẩn đoán; không phải lịch sử #10 hay audit log sản phẩm đã xác nhận.

## Kế hoạch kiểm chứng AC1–AC8 (chưa chạy)

| AC | Bằng chứng cần thu khi có build và nền tảng được duyệt |
| --- | --- |
| AC1 | A đã có danh mục #08: form chi đọc **cùng định danh/danh sách** đã dùng cho thu, kể cả mục đã dùng; không phát sinh bộ mục chi riêng hay seed lại. B không chọn được danh mục A qua UI hoặc đường trực tiếp. |
| AC2 | Hai ví A/B đang sử dụng: vào từ A chọn sẵn A, đổi B; từ nút chung chưa chọn ví và Lưu bị chặn, chọn B rồi chỉ B giảm. Ví ngừng không chọn/lưu được, kể cả gửi trực tiếp. |
| AC3 | Lưu chi nguyên VND dương với ngày hôm nay/ngày cũ, ghi chú trống/có nội dung; sau xác nhận/tải lại/điện thoại khác đọc đúng chủ, ví, danh mục, ngày, tiền, ghi chú và số dư. Không cần màn lịch sử #10. |
| AC4 | Ví A có 50.000 VND, ví B không đổi; lưu chi 120.000 VND ngày hôm qua vào A, tải lại A là **-70.000 VND**, không báo thiếu tiền. Kiểm bản ghi chỉ góp một lần. |
| AC5 | Thử thiếu ví/danh mục, ví ngừng/khác chủ, danh mục khác chủ, tiền 0/âm/phần lẻ/chữ/biên, ngày mai; qua UI và đường trực tiếp đều không có khoản chi mới hay đổi số dư. Thử thay trạng thái sau khi mở form, trước commit. |
| AC6 | A/B/quản lý/khách: B và quản lý không tạo/đọc khoản chi, danh mục hay số dư A, kể cả khi đổi tài khoản cùng điện thoại hoặc đoán định danh thao tác. |
| AC7 | Safari iPhone và Chrome Android: nhập chi 120.000 VND vào “Ngân hàng”, rời form **trước Lưu** rồi quay lại cùng phiên; đóng trình duyệt **trước Lưu** rồi mở lại. Form trống, số dư không đổi, không có chi mới, dữ liệu đã lưu vẫn còn. **Đối chứng F08:** khi form vẫn mở và điện thoại khác tạo dữ liệu làm tự tải, phần nhập dở vẫn được giữ. |
| AC8 | Trên Lưu bình thường, quan sát trước/sau xác nhận bền: không báo thành công sớm; sau báo thành công đọc lại một khoản chi và số dư đúng AC3–AC4. Retry cùng ý định kiểm bất biến chung; mất mạng, rời form **sau Lưu**, đóng/mở và phục hồi là case #33/#35. |

Khi có build, kiểm luồng web mobile → xử lý tin cậy → nơi lưu → đọc lại, dùng A/B/quản lý và hai ví, có điểm quan sát số giao dịch, trạng thái thao tác và số dư. Cần fault injection trước/sau ghi bền, mất phản hồi, request tới muộn, same/different operation và cập nhật ví/danh mục/tài khoản sát commit; thực thi chuyên biệt cùng #29/#33/#35. Bảng này là **kế hoạch**, không phải kết quả test hay `QA_PASS`.

## Phụ thuộc, gap và review cần làm

| Cạnh / gap | Phân loại hiện tại | Điều kiện để xét lại |
| --- | --- | --- |
| #08 → #09: danh mục chung, mô hình giao dịch, số dư và Lưu một lần | **HARD** cho Dev/tích hợp AC1–AC8; `FOUNDATION_GAP` | #08 giải P0 seed danh mục theo ID chủ và commit giao dịch/số dư, P1 idempotency/VND/ngày/quyền; Tech/PM chốt hợp đồng chung. Chỉ xét chuyển phần độc lập sang `CONTRACT` khi hợp đồng được review và mock có thể kiểm, không tự đổi issue. |
| #04 → #09: phiên, vai trò, chủ và tài khoản bị xóa | **HARD** qua #08; `PLATFORM_GAP` | Quyết định auth/quyền được review; thử A/B/quản lý, phiên hết hạn, đổi tài khoản và trạng thái tài khoản lúc commit. |
| #06 → #09: ví/số dư đầu/VND; #07 → #09: ví thứ hai/đổi ví | **HARD** qua #08 cho đường tích hợp; `CONTRACT_GAP` | #06 có đường ví đọc được, #07 giải `STORY_TECH_GAP` và có hai ví để kiểm AC2; dùng một hợp đồng số dư chung. |
| #09 → #10 | **Downstream**, `SCOPE_BOUNDARY` | Khoản chi lưu đủ thông tin/định danh để #10 trình bày/lọc/sắp xếp sau này; AC3 đọc lại không đòi xây màn lịch sử. |
| #09 → #29 | **Downstream verification**, `SHARED_INVARIANT` | Thiết kế #09 không mất cập nhật; #29 chứng minh hai giao dịch độc lập gần đồng thời trên hai điện thoại và tổng số dư. |
| #09 → #33 → #35 | **Downstream recovery**, `PLATFORM_GAP` | #09 dùng cùng định danh/trạng thái thao tác từ đầu; #33 kiểm trong phiên, #35 sau đóng/mở, gồm “chắc chưa lưu” an toàn. Không dùng phạm vi sau để báo thành công sớm ở AC8. |

**Spike/architecture review trước `TECH_READY`:** (1) giải P0/P1 của [review #08](../capmoney-income/technical-design.md): seed mười danh mục theo ID chủ, hợp đồng giao dịch/số dư atomic, ghi một lần và quyền ở thời điểm commit; (2) chứng minh phép **trừ** VND nguyên và kết quả âm chính xác qua nhập/lưu/đọc, biên tràn, không có kiểm đủ tiền; (3) test cùng/different ý định đồng thời, commit/crash/mất phản hồi và request tới muộn, có cơ chế terminal “chắc chưa lưu” không thể bị lật; (4) thống nhất ngày lịch và múi giờ với #08, kiểm sát ranh giới hôm nay/ngày mai; (5) thử riêng R73 và F08 để không reset form đang mở khi tự tải, cùng R72 trên nút chung. Ghi quyết định nền tảng chung và bằng chứng spike, xin reviewer kỹ thuật **khác người lập** rà, rồi CapmmPM kiểm phạm vi. Nếu giới hạn kỹ thuật buộc đổi R72/R73, quyền, tiền hoặc ngày đã chốt, trả BA/PM hỏi Ninh.

**Kết luận CapmmTech:** AC1–AC8 đã có hợp đồng và kế hoạch kiểm chứng ở mức thiết kế; `FOUNDATION_GAP`/`PLATFORM_GAP`/`CONTRACT_GAP` còn mở. Chưa có quyết định kiến trúc được duyệt, test run, ticket Dev hoặc bằng chứng `TECH_READY`.

## Review kỹ thuật độc lập và việc cần chứng minh

Ngày 14/10/2026, agent `/root/tech_independent` (khác agent lập thiết kế `/root/tech_income`) rà **tài liệu** với spec AC1–AC8, R72–R73, #08 và workflow. `RESULT: STORY_TECH_GAP`. Reviewer xác nhận số tiền chi phải dương dù số dư được âm, ranh giới F08/R73 và việc tách AC8 bình thường khỏi kiểm chứng phục hồi #33/#35; review này **không** phải kết quả spike hoặc kiểm thử hệ thống.

| Mức | Finding | Bằng chứng cần có trước khi xin review lại |
| --- | --- | --- |
| P0 | B01 và idempotency dùng chung #08 chưa có ranh giới ghi bền: một `(chủ, operation ID)` chỉ tạo một khoản chi và một tác động số dư; hai ID độc lập cùng ví, kể cả thu+chi, không được mất cập nhật. Cùng ID khác nội dung bị từ chối; không kiểm đủ tiền khi khoản chi làm số dư âm. | Quyết định ledger hay số dư vật hóa với cơ chế atomic/constraint tương đương, spike retry/đồng thời/crash và đọc số bản ghi, kết quả thao tác, số dư trước/sau commit. |
| P0 | #08 còn `STORY_TECH_GAP` về danh mục chung, giao dịch/số dư, quyền và kết quả thao tác; đây là `HARD` cho Dev/tích hợp #09. #04/#06/#07 cũng chặn đường tích hợp qua phiên, ví và demo hai ví. | Foundation #08 cùng hợp đồng #04/#06/#07 được review và có bằng chứng; PM chỉ đổi phân loại cạnh nếu hợp đồng cho phần độc lập được duyệt. |
| P1 | Chủ, ví đang dùng, danh mục có thể chọn và tài khoản còn hợp lệ phải được kiểm **ở cùng ranh giới commit**, tránh dữ liệu đổi giữa mở form và Lưu hoặc lộ dữ liệu người khác qua lỗi/đọc kết quả. | Thử ví bị ngừng, danh mục bị xóa, tài khoản bị xóa sát commit; A/B/quản lý/khách gọi trực tiếp create/read/status, chứng minh không có bản ghi treo hay rò thông tin. |
| P1 | #09 phải giữ ID thao tác và ràng buộc nội dung từ trước submit, tách pending marker khỏi bản nháp R73/F21 để #33/#35 đối chiếu. “Chắc chưa lưu” phải an toàn trước request tới muộn; retention, nhiều tab/restart/đổi tài khoản chưa được chứng minh. | Hợp đồng chung #07/#08/#33/#35, fault injection response mất, request muộn và nhiều tab; không sinh key mới cho cùng ý định khi key cũ chưa phân định. |
| P1 | Biểu diễn VND nguyên xuyên nhập/truyền/lưu/đọc và ngày lịch theo múi giờ còn mở; cần kiểm tổng âm, biên tràn và nửa đêm khác múi giờ. | Quyết định parse/serialize, miền số và cách xác định “hôm nay”; test số âm lớn/ngày sát ranh giới. Giới hạn ảnh hưởng R43 hoặc tiền phải được PM/BA rà. |
| P1 | Kế hoạch test chưa có điểm quan sát/fault injection thực tế; UI hay một lần đọc số dư không đủ chứng minh một lần trừ. | Quan sát operation state, số giao dịch và số dư qua same/different keys (chi+chi, thu+chi), commit bị ngắt, response mất, đầu vào/trạng thái đổi sát commit và khoản chi vượt số dư. |

Đây là **backlog Tech/spike**, chưa chọn stack, schema hoặc API. CapmmTech cần cập nhật hợp đồng sau bằng chứng nền tảng, xin reviewer kỹ thuật độc lập rà lại, rồi CapmmPM kiểm phạm vi trước `TECH_READY`. Chưa tạo ticket Dev.
