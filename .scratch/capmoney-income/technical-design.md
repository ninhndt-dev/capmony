# Technical Design: Story #08 – Danh mục mặc định và ghi khoản thu

**Trạng thái CapmmTech:** bản thiết kế để review; `BA_READY` đã được CapmmPM xác nhận trong [story](story.md) và [handoff](handoff.md). **`STORY_TECH_GAP`; chưa `TECH_READY`** vì hợp đồng nền tảng và các spike bên dưới chưa được chứng minh hoặc review độc lập. Chưa có code ứng dụng, build, môi trường kiểm thử hay test thực chạy.

## Nguồn, phạm vi và ranh giới tin cậy

- Nguồn hành vi là [spec #08, BR-08-01..08 và AC1–AC8](spec.md), [Q&A](qna.md), [R70–R71](../../docs/requirements/42-step-9-income-round-1.md), [quy tắc nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md) và [issue #08](../capmoney-overall/issues/08-danh-muc-mac-dinh-va-ghi-thu.md). [Technical design #07](../capmoney-add-wallet/technical-design.md) nêu các gap chung về phiên, VND và lưu một lần; chưa phải nền tảng đã duyệt.
- #08 cung cấp mười danh mục mặc định riêng cho tài khoản ghi chép mới, lựa chọn danh mục/ví và tạo khoản **thu**, đọc lại bản ghi đã lưu và số dư hiện tại. Không xây ghi chi #09, màn lịch sử/lọc/sắp xếp #10, sửa/xóa/khôi phục #11–#12, quản lý danh mục #20–#21, điều hướng đầy đủ mọi ví ngừng #18, kiểm chứng hai giao dịch độc lập đồng thời #29, hay phục hồi Lưu chưa rõ #33/#35. Hợp đồng dùng chung vẫn phải để các lát này nối được sau này.
- Trình duyệt chỉ gửi ý định ghi thu và lựa chọn của người dùng. Hệ thống lấy danh tính, vai trò và chủ dữ liệu từ phiên hợp lệ của #04; tự kiểm tra quyền trên ví, danh mục và khoản thu ở phía tin cậy ngay lúc ghi/đọc. Không tin chủ, loại tiền, số dư mới, trạng thái ví/danh mục hoặc kết quả “đã lưu” do trình duyệt tự khẳng định. Nơi lưu bền là nguồn sự thật cho kết quả và số dư; công nghệ/API/schema chưa được chọn.

## Hợp đồng dữ liệu ở mức khái niệm

| Khái niệm | Bất biến cần giữ | Điểm nối |
| --- | --- | --- |
| Tài khoản ghi chép | Có định danh chủ ổn định và vai trò từ phiên; tài khoản quản lý không sở hữu dữ liệu tài chính của người ghi chép. | #04 cấp phiên/quyền và vòng đời tài khoản. |
| Danh mục | Mỗi chủ mới có đúng mười danh mục B16 với tên `Lương`, `Thưởng`, `Ăn uống`, `Đi lại`, `Mua sắm`, `Nhà ở`, `Hóa đơn`, `Sức khỏe`, `Giải trí`, `Khác`. Danh sách thu/chi là **một** danh sách riêng của chủ; mỗi mục có định danh ổn định để khoản thu tham chiếu dù sau này đổi tên. | Khởi tạo cùng vòng đời tài khoản #04 hoặc trước khi tài khoản được dùng để ghi; #20/#21 được thêm/đổi tên/xóa mục chưa dùng theo B05/B11/R54. Không cấp lại mười mục ở mỗi lần đăng nhập/tải trang, không xóa thay đổi của chủ về sau. |
| Ví | Định danh và chủ ổn định, trạng thái đang sử dụng tại thời điểm Lưu, số dư đầu nguyên VND. Không gắn khoản thu mới vào ví của người khác hoặc ví đã ngừng. | #06/#07 tạo/đọc ví; #18 đổi trạng thái, #16 sửa số dư đầu. |
| Khoản thu | Một giao dịch còn hiệu lực của đúng chủ, loại thu, một ví, một danh mục của cùng chủ, số tiền nguyên VND dương, ngày lịch hợp lệ và ghi chú tùy chọn. Dùng định danh giao dịch ổn định cho đọc lại và #10/#11/#12. | #10 trình bày lịch sử; #11/#12 thay đổi hiệu lực/số liệu sau này. Không dùng bản ghi giả để diễn đạt số dư đầu. |
| Số dư hiện tại | Theo B01: số dư đầu + thu còn hiệu lực + chuyển đến còn hiệu lực − chi còn hiệu lực − chuyển đi còn hiệu lực, **không phụ thuộc ngày ghi**. Một khoản thu đã xác nhận góp đúng một lần vào đúng ví. | Hợp đồng tính/duy trì số dư phải dùng chung cho #08/#09/#13/#16 và các thao tác sửa/xóa/khôi phục; không để riêng một bản số dư do UI tự cộng. |
| Thao tác Lưu | Một ý định Lưu có định danh ổn định riêng với chủ, nội dung và kết quả cuối hoặc trạng thái chưa phân định; cùng ý định không được tạo hai giao dịch. Hai ý định khác nhau không bị gộp vì trùng số tiền/ngày/ví/danh mục. | Hợp đồng chung cho đường bình thường #08 và phục hồi #33/#35; chưa chốt cách lưu định danh/trạng thái. |

Khởi tạo danh mục cần chịu được hoàn tất tài khoản bị gọi lặp/gần đồng thời theo #04: vẫn đúng mười mục cho mỗi chủ mới và không lộ trạng thái tài khoản dùng được nhưng chưa có danh mục cần thiết. Cần chốt điểm nhất quán giữa hoàn tất tài khoản và cấp danh mục bằng architecture review, không suy ra schema hay giao dịch cơ sở dữ liệu cụ thể. Tài khoản mới “dữ liệu tài chính trống” ở #04 có thể có mười danh mục; chưa có ví/giao dịch không có nghĩa là thiếu danh mục.

## Luồng đọc, nhập và Lưu bình thường

1. Khi mở ghi thu, đọc danh sách ví **đang sử dụng** và danh mục của chủ từ nơi lưu qua phiên hợp lệ. Nếu vào từ ví A đang sử dụng, chọn sẵn A và cho đổi sang ví đang sử dụng khác của cùng chủ (F14). Nếu vào từ nút chung và có **ít nhất hai** ví đang sử dụng, ban đầu không chọn ví; người dùng phải chọn trước Lưu (R70). Với đúng một ví ở nút chung, cách trình bày còn là lựa chọn UX; bất cứ đường nào cũng phải có ví hợp lệ lúc Lưu. Không tự suy R70 cho #09.
2. Trước Lưu, khoản nhập là bản nháp của form, chưa tạo giao dịch hoặc thay đổi số dư. Rời form thu rồi quay lại cùng phiên có Internet phải mở form trống (R71); đóng hẳn trình duyệt trước Lưu cũng mở trống (F21). **Tự tải dữ liệu mới khi người dùng vẫn ở form không phải rời form**: F08 yêu cầu giữ phần đang nhập trong trường hợp ấy. Không lưu bản nháp #08 để tự khôi phục sau khi đã rời form. Nội dung và dấu vết của thao tác **đã bấm Lưu nhưng chưa rõ** là nhánh khác, không được xóa theo R71/F21; hợp đồng phục hồi thuộc #33/#35.
3. UI chặn trường thiếu/sai để giải thích cho người dùng; phía tin cậy kiểm lại trên mọi đường gọi trực tiếp. Ví bắt buộc còn đang sử dụng và thuộc chủ; danh mục bắt buộc thuộc cùng chủ, kể cả mục mặc định đã được dùng trước đây. Tiền là số nguyên VND `> 0`; từ chối 0, âm, phần lẻ, chữ, tràn miền biểu diễn; không làm tròn hay tự đổi giá trị. Ngày là **ngày lịch** hôm nay hoặc trước đó; không chấp nhận tương lai. Ghi chú có thể trống. Chuẩn nhập/hiển thị VND, miền kỹ thuật, cách xác định “hôm nay” khi thiết bị và hệ thống khác múi giờ, giới hạn ghi chú cần Tech đề xuất rồi PM rà; không đặt giới hạn sản phẩm ngầm.
4. Lúc Lưu, kiểm lại phiên/chủ/ví đang sử dụng/danh mục/tiền/ngày trên trạng thái hiện hành để chống dữ liệu cũ hoặc đầu vào giả. Ghi khoản thu và tác động số dư theo **một ranh giới nhất quán**: chỉ công bố thành công khi khoản thu đã bền và phép đọc sau đó phản ánh đúng B01. Có thể tính số dư từ các giao dịch còn hiệu lực hoặc duy trì giá trị vật hóa nếu cơ chế atomic/đối soát được chứng minh; đây là quyết định kiến trúc chung chưa chốt. Không đọc số dư cũ rồi ghi đè kiểu mất cập nhật. Ví khác giữ nguyên. Sau xác nhận, tải lại hoặc điện thoại khác cùng chủ có Internet đọc được kết quả.
5. Một thao tác Lưu bình thường chỉ báo “đã lưu” sau khi có xác nhận bền cho chính thao tác đó; bản ghi và số dư đọc lại đúng như AC3–AC4. Trước xác nhận, không báo thành công. Nếu phản hồi chậm/mất hoặc kết quả chưa phân định, giữ trạng thái chưa rõ và định danh thao tác; không suy “chưa lưu” từ timeout hoặc danh sách chưa thấy giao dịch, không phát sinh ý định mới để thử lại cùng thao tác. Nhánh đối chiếu, rời form sau Lưu, đóng/mở và kết quả “chắc chưa lưu” là phạm vi #33/#35 nhưng hợp đồng #08 phải tương thích từ đầu.

## Lỗi, đồng thời và an toàn dữ liệu

| Trường hợp | Kết quả bắt buộc / ranh giới |
| --- | --- |
| Thiếu ví/danh mục, ví ngừng, ví hoặc danh mục khác chủ, tiền không hợp lệ, ngày tương lai | Từ chối toàn bộ lần Lưu; không có khoản thu và không đổi số dư. Không tiết lộ dữ liệu của chủ khác qua thông báo lỗi. |
| Phiên hết hạn, tài khoản bị xóa hoặc đổi tài khoản trên cùng thiết bị | Không đọc/ghi dữ liệu tài chính bằng phiên cũ; kết quả thao tác chờ chỉ hiển thị cho đúng chủ sau xác thực. Hợp đồng #04/R63 quyết định xử lý phiên. |
| Hai lần gửi cùng ý định, gồm bấm lặp và request gần đồng thời | Tối đa một khoản thu và một tác động số dư; cùng định danh nhưng nội dung khác phải bị từ chối. Nút bị khóa trên UI không đủ làm bảo đảm. Chưa có cơ chế atomic được chứng minh. |
| Hai ý định giao dịch **khác nhau** gần đồng thời | Cả hai khoản đã báo lưu phải được giữ, số dư phản ánh đủ hai khoản theo B24; #29 kiểm chứng chuyên biệt với hai điện thoại và các loại giao dịch khác. |
| Hệ thống đã ghi nhưng phản hồi mất | Không ghi lại khoản thu bằng định danh mới; chỉ đối chiếu đúng thao tác để biết kết quả. #33/#35 đảm nhiệm kiểm chứng phục hồi. |
| Kết quả chưa rõ hoặc request cũ có thể tới muộn | Giữ trạng thái đang kiểm tra và chặn lặp đúng ý định. Chỉ nói “chắc chưa lưu” khi cơ chế nền tảng bảo đảm request cũ không còn khả năng ghi về sau; đây là gap nền tảng chung với #07. |

Quyền phải được kiểm trên đường đọc danh mục, đọc ví/số dư, tạo và đọc lại khoản thu, và xem kết quả thao tác; kiểm giao diện đơn thuần không đủ. Không nhận định danh chủ từ input để đổi phạm vi truy cập. Dấu vết phục hồi nếu được giữ trên thiết bị phải gắn đúng tài khoản và không làm lộ dữ liệu tài chính cho tài khoản B hay quản lý trên cùng máy. Log kỹ thuật chỉ đủ chẩn đoán, không thay lịch sử #10 hoặc làm lộ số tiền/danh mục. Chưa có yêu cầu audit log sản phẩm riêng.

## Kế hoạch kiểm chứng AC1–AC8 (chưa chạy)

| AC | Dữ liệu và bằng chứng cần thu khi có build |
| --- | --- |
| AC1 | Tạo A và B mới; mỗi người đọc đúng mười tên B16, dùng từng tên cho thu; không có bản trùng sau đăng xuất/đăng nhập hoặc hoàn tất tài khoản lặp. A không đọc/chọn danh mục của B, kể cả đường gọi trực tiếp. |
| AC2 | Có hai ví đang sử dụng A/B: vào từ A được chọn A, đổi B được; từ nút chung không chọn ví và bấm Lưu bị chặn; chọn B rồi chỉ B tăng. Kiểm ví đã ngừng không nằm trong lựa chọn hợp lệ và bị từ chối nếu gửi trực tiếp. |
| AC3 | Ghi thu với ngày hôm nay/ngày cũ, ghi chú trống/có chữ, danh mục của chủ, tiền nguyên VND dương; đọc lại định danh, ví, danh mục, ngày, tiền, ghi chú và số dư sau tải lại hoặc điện thoại khác. Không đòi màn lịch sử #10. |
| AC4 | Ví A từ `-50.000` VND, ví B độc lập; lưu thu `120.000` VND ngày hôm qua vào A, sau xác nhận/tải lại A là `70.000` VND, B không đổi. Đối chiếu bản ghi để chứng minh không cộng hai lần. |
| AC5 | Thử thiếu ví/danh mục, ví ngừng/khác chủ, danh mục khác chủ, tiền `0`, âm, phần lẻ, chữ và biên biểu diễn, ngày mai; mỗi lần kiểm không có bản ghi mới và số dư các ví không đổi. Kiểm cả UI và đường gọi trực tiếp. |
| AC6 | A, B, quản lý, khách: B/quản lý/khách không tạo hoặc đọc danh mục/khoản thu/số dư của A qua UI hoặc đường trực tiếp; đổi tài khoản cùng thiết bị không hiện dữ liệu A. |
| AC7 | Trên Safari iPhone và Chrome Android: nhập `120.000` VND vào “Ngân hàng”, rời form **trước Lưu** rồi quay lại cùng phiên; đóng browser **trước Lưu** rồi mở lại. Cả hai form trống, không có thu mới/số dư đổi, khoản cũ đã lưu vẫn đọc được. Đối chứng F08: khi điện thoại thứ hai làm form đang mở tự tải dữ liệu mới **mà người dùng không rời form**, phần đang nhập vẫn còn. |
| AC8 | Với đường Lưu bình thường, quan sát trạng thái trước/sau xác nhận bền: không báo thành công sớm; sau thông báo đọc lại đúng giao dịch/số dư. Gửi cùng ý định lặp gần đồng thời để kiểm bất biến dùng chung, nhưng mất mạng/đối chiếu/đóng mở và fault injection đầy đủ là case #33/#35. |

Test tích hợp cần đi từ web mobile → xử lý tin cậy → nơi lưu → đọc lại; có dữ liệu A/B/quản lý và khả năng quan sát trước/sau commit, phản hồi bị trì hoãn, request đồng thời. Khi #29/#33/#35 triển khai, bổ sung kiểm hai ý định độc lập, mất mạng, request đến muộn, đóng/mở và phục hồi. Các ca trên là **kế hoạch**, không phải kết quả pass hay bằng chứng `QA_PASS`.

## Phụ thuộc và phân loại gap

| Cạnh / gap | Phân loại hiện tại | Điều kiện tháo gỡ |
| --- | --- | --- |
| #04 → #08: phiên, vai trò, chủ dữ liệu và thời điểm tài khoản mới dùng được | **HARD** cho tích hợp/kiểm AC1, AC6; `PLATFORM_GAP` | Hợp đồng auth/quyền được review riêng; chứng minh A/B/quản lý, phiên hết hạn, tài khoản bị xóa, khởi tạo danh mục đúng một lần. #04 chưa có gate workflow mới. |
| #06 → #08: ví đã lưu, số dư đầu và quy tắc VND | **HARD** cho đường ghi thu/AC3–AC5; `CONTRACT_GAP` | Hợp đồng ví/chủ/VND/số dư chung được chốt và #06 có đường tích hợp. Có thể thiết kế phần danh mục độc lập nhưng không trình diễn đầy đủ #08 khi thiếu ví. |
| #07 → #08: ví thứ hai và đổi ví | **HARD** cho AC2 và demo hai ví; `CONTRACT_GAP` | #07 giải `STORY_TECH_GAP`, có tạo/đọc ví thêm và hai ví đang sử dụng. Chỉ cân nhắc `CONTRACT` cho phần có mock sau khi PM/Tech chốt hợp đồng; không tự đổi cạnh issue. |
| #08 → #10 | **Downstream**, không chặn AC3; `SCOPE_BOUNDARY` | #08 lưu dữ liệu giao dịch đủ ổn định để #10 hiển thị/lọc/sắp xếp sau này; #10 sở hữu màn lịch sử. |
| #08 → #29 | **Downstream verification**, `SHARED_INVARIANT` | #08 không tạo thiết kế mất cập nhật; #29 kiểm hai giao dịch khác nhau trên hai điện thoại và số dư chung sau cả hai. |
| #08 → #33 → #35 | **Downstream recovery**, `PLATFORM_GAP` | Dùng cùng định danh thao tác và ngữ nghĩa đã/chưa/chưa rõ; #33 kiểm mất mạng trong phiên, #35 kiểm sau đóng/mở. Không dùng việc chúng chưa xong để báo thành công sớm trên đường #08 bình thường. |

**Spike/architecture review trước khi xin `TECH_READY`:** (1) chọn nền tảng phiên và nơi lưu chung #04/#06/#07/#08, chứng minh quyền chủ và khởi tạo mười danh mục đúng một lần; (2) chứng minh biểu diễn/tuần tự hóa VND nguyên chính xác và phép tính số dư B01 không mất cập nhật khi ghi, kể cả ví âm; (3) chứng minh ghi khoản thu + kết quả thao tác là nhất quán/atomic dưới retry, đồng thời, crash và đọc lại; (4) cùng #07/#33/#35, chứng minh trạng thái “chắc chưa lưu” có rào chắn an toàn trước request tới muộn và dấu vết thao tác qua nhiều tab/đóng mở/đổi tài khoản; (5) thống nhất ngữ nghĩa ngày lịch và “hôm nay” trên iPhone/Android/hệ thống. Ghi phương án, giới hạn thực tế và bằng chứng fault injection trong quyết định kiến trúc chung, rồi để reviewer kỹ thuật **khác người lập** rà và CapmmPM kiểm phạm vi. Nếu giới hạn kỹ thuật làm đổi R70/R71, miền tiền/ngày hoặc quyền, trả BA/PM hỏi Ninh; không tự nâng đề xuất thành yêu cầu đã chốt.

**Kết luận CapmmTech:** thiết kế này truy vết AC1–AC8 và chỉ đủ để review tài liệu. Các `PLATFORM_GAP`/`CONTRACT_GAP` còn mở; chưa có architecture decision được duyệt, test run, ticket Dev hay bằng chứng `TECH_READY`.

## Review kỹ thuật độc lập và việc cần chứng minh

Ngày 14/10/2026, agent `/root/tech_independent` (khác agent lập thiết kế `/root/tech_income`) rà **tài liệu** với spec AC1–AC8, R70/R71 và các phụ thuộc. `RESULT: STORY_TECH_GAP`; review này không phải kết quả spike hay kiểm thử hệ thống. Reviewer xác nhận ranh giới khoản thu #08 với #09/#10/#33/#35 phù hợp, đồng thời nêu các điểm chặn:

| Mức | Finding | Bằng chứng cần có trước khi xin review lại |
| --- | --- | --- |
| P0 | Khởi tạo đúng mười danh mục theo **ID chủ ổn định**, không theo email, còn thiếu ranh giới nhất quán với kích hoạt tài khoản #04. Retry/đồng thời không được nhân danh mục; chủ không được dùng tài khoản trước khi bộ danh mục sẵn; đăng nhập lại hoặc người dùng sửa/xóa danh mục về sau không được tự cấp lại. | Quyết định kiến trúc và spike về seed idempotent, unique identity/atomic boundary hoặc cơ chế tương đương; thử hoàn tất tài khoản lặp và gần đồng thời, đọc bộ danh mục khi tài khoản vừa được dùng. |
| P0 | Ghi một khoản thu, kết quả thao tác và tác động số dư B01 chưa có cơ chế atomic được chứng minh. Hai ý định độc lập gần đồng thời phải cùng tồn tại và không mất cập nhật; retry cùng ý định chỉ có một bản ghi/tác động. | Chọn cách tính hoặc duy trì số dư chung, ranh giới commit và idempotency theo chủ/operation ID; fault injection trước/sau commit, hai khoản thu cùng ví đồng thời, đọc lại số bản ghi và số dư sau thành công. #29 kiểm chuyên biệt trên hai điện thoại về sau. |
| P1 | Đường Lưu #08 phải tương thích phục hồi #33/#35 ngay từ đầu: ID ổn định trước submit, cùng key+payload, pending/terminal, thời hạn giữ key, cơ chế “chắc chưa lưu” chống request đến muộn và dấu vết qua đóng/mở/đổi tài khoản/nhiều tab. | Hợp đồng dùng chung với #07/#33/#35 và thử request tới muộn, mất phản hồi, mở lại, hai tab; không tạo key mới cho cùng ý định khi key cũ chưa phân định. |
| P1 | VND nguyên và ngày lịch chưa có hợp đồng truyền tải/lưu: giới hạn biểu diễn, tràn khi cộng số dư âm/dương, dấu phân cách; xác định “hôm nay” khi thiết bị/hệ thống khác múi giờ. | Quyết định parse/serialize không làm tròn, test số biên và tổng; kiểm ngày sát nửa đêm, DST hoặc đổi múi giờ. Nếu cách chọn múi giờ đổi ý nghĩa R43, trả BA/PM hỏi Ninh. |
| P1 | Quyền và trạng thái hiện hành phải được kiểm ở lúc commit: owner/role từ #04, ví còn dùng, danh mục đúng chủ, tài khoản chưa xóa; không để lỗi trở thành cách dò ID của chủ khác. #04/#06/#07 vẫn chặn tích hợp. | Test A/B/quản lý với đường create/list/read/result; đổi trạng thái ví/danh mục hoặc xóa tài khoản giữa lúc mở form và ghi, rồi chứng minh không lưu sai. |
| P1 | Kế hoạch AC đã có nhưng thiếu điểm quan sát và fault injection thực tế. | Thu trạng thái thao tác, số bản ghi và số dư tại nơi lưu qua seed retry/parallel, commit bị ngắt, same/different keys, thay đổi ví/danh mục gần commit, VND/ngày biên; không suy pass từ UI. |
| P2 | R71 (rời form trước Lưu) và F08 (tự tải dữ liệu mới khi vẫn ở form) cần hai đường UI riêng; nếu cùng một sự kiện reset form, phần nhập có thể bị xóa trái F08. | Kiểm rời form/quay lại mở trống, đồng thời kiểm refresh dữ liệu từ điện thoại thứ hai khi form vẫn mở thì phần nhập giữ nguyên. |

Đây là **backlog Tech/spike**, không phải quyết định stack, schema hay API. Sau khi có bằng chứng và quyết định nền tảng được review riêng, CapmmTech cập nhật hợp đồng, xin reviewer kỹ thuật độc lập rà lại, rồi CapmmPM kiểm phạm vi trước khi xét `TECH_READY`. Chưa tạo ticket Dev hoặc code ứng dụng.
