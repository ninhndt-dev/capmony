# Đề xuất hợp đồng kỹ thuật cho Story #07 – Thêm ví

**Trạng thái:** ĐỀ XUẤT của CapmmTech, chưa được duyệt; không thay `technical-design.md`, không chứng minh `TECH_READY`. Reviewer kỹ thuật độc lập `/root/tech_independent` đã rà **bản đề xuất ở mức tài liệu** và nêu các điểm P1 được làm rõ dưới đây. P0 chưa có spike chứng minh; chưa chọn stack, chưa có code, build hay kết quả test chạy thật.

**Nguồn:** [Story và gate #07](story.md), [spec #07, đặc biệt AC7–AC8](spec.md), [thiết kế và findings P0/P1](technical-design.md), [handoff](handoff.md), [đối chiếu #06](../capmoney-first-wallet/technical-options.md), [đối chiếu #04](../capmoney-invitation-accept/technical-options.md), [R68–R69](../../docs/requirements/41-step-9-add-wallet-round-1.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Đây là giả thuyết hợp đồng để làm spike và review, không phải quyết định sản phẩm mới.

## 1. Phạm vi và hai loại dữ liệu không được trộn

- **R68, trước bấm Lưu:** bản nhập dở của **ví thêm** chỉ tồn tại trên biểu mẫu hiện tại. Rời biểu mẫu rồi quay lại cùng phiên có Internet, hoặc đóng browser trước Lưu rồi mở lại, thì biểu mẫu trống và không phát sinh ví. Không phục hồi tên/số dư nhập dở. Quy tắc F18 của **ví đầu tiên #06** khác R68.
- **R69, sau bấm Lưu:** từ lúc người dùng bấm Lưu, một **ý định lưu** có định danh riêng phải còn đường đối chiếu sau đóng/mở. Dấu vết này không phải bản nháp R68. Khi chưa xác định kết quả, hiển thị đang kiểm tra và chặn một lần Lưu trùng của chính ý định ấy; khi xác định, trả đúng ví đã ghi hoặc biểu mẫu trống nếu chắc chưa ghi. #06 có nhánh R67 tương ứng và nên dùng cùng cơ chế nền tảng.
- Người dùng có thể tạo nhiều ví. Tên và số dư giống nhau không chứng minh hai lần bấm thuộc cùng ý định. Một ý định mới, độc lập, phải có định danh mới; một lần retry của ý định cũ phải giữ định danh cũ. **R15 chưa chốt có cho hai ví trùng tên hay không**; không dùng tên làm khóa chống trùng thao tác, cũng không coi tên trùng được phép là quyết định sản phẩm.

## 2. Định danh, thứ tự sự kiện và điểm quyết định

**Đề xuất:** mỗi ý định Lưu có một định danh khó đoán, gắn với chủ lấy từ phiên phía hệ thống, loại thao tác `tạo ví`, và nội dung đã được chuẩn hóa theo một quy tắc được duyệt. Dùng cùng định danh cho bấm Lưu lặp, retry sau timeout, hai request đồng thời từ cùng biểu mẫu và đối chiếu sau mở lại. Cùng định danh với nội dung khác phải bị từ chối, không sửa ý định cũ. Nội dung chuẩn hóa phải giữ chính xác VND nguyên và không âm thầm đổi tên/tiền; quy tắc chuẩn hóa còn chờ PM rà.

Để hỗ trợ recovery ngay cả khi request Lưu tới muộn, đề xuất thứ tự sau:

1. Người dùng bấm Lưu trên biểu mẫu hợp lệ. Trình duyệt gắn định danh cho **ý định**. Theo phương án này, phải xác nhận **ghi thành công dấu vết tối thiểu tại thiết bị trước khi phát bất kỳ request nào có thể tạo ví**; nếu ghi dấu vết thất bại, không phát request đó. Chỉ được thay thứ tự này nếu spike chứng minh một đường phục hồi phía hệ thống có thể tìm lại chính ý định dù mất hoàn toàn dấu vết thiết bị. Một điểm ghi nhận bền phía hệ thống xác nhận định danh, chủ và dấu vết nội dung của ý định. Chỉ sau khi điểm ghi nhận này được xác nhận mới phát công việc có thể ghi ví. Nếu chưa xác nhận, trình duyệt giữ kết quả **chưa rõ**, không cấp định danh khác cho lần retry của chính biểu mẫu đó.
2. Mỗi request Lưu/retry đi qua cùng nơi phân xử bền của ý định. Nơi này kiểm phiên và chủ, nội dung có khớp hay không, và trạng thái cuối. Việc đọc trạng thái để đối chiếu cũng dùng nguồn có tính nhất quán đủ mạnh; bản sao đọc trễ chỉ được phép trả **chưa rõ**.
3. Khi tạo thành công, việc ghi ví, ràng buộc duy nhất giữa ví và ý định, cùng kết quả cuối `ĐÃ_LƯU → đúng ví` phải là **một quyết định nguyên tử**. Không có khoảng đã công bố thành công mà ví chưa bền, hoặc ví đã bền nhưng retry có thể tạo ví thứ hai. Một thao tác độc lập dùng định danh khác được phép tạo ví khác.
4. Khi có yêu cầu kết thúc âm hoặc cần phục hồi ý định treo, nơi phân xử phải ghi **rào chắn kết thúc âm bền** cho chính định danh bằng cùng cơ chế phân xử với bước ghi ví. Kết quả `CHẮC_CHƯA_LƯU` chỉ được công bố sau khi rào chắn đó đã bền và mọi đường ghi của request cũ bắt buộc kiểm rào chắn tại điểm quyết định nguyên tử. Nếu ghi ví thắng cuộc đua, trả `ĐÃ_LƯU`; nếu rào chắn thắng, mọi request cũ đến muộn hoặc worker cũ tỉnh lại đều không được ghi ví.
5. Sau kết quả cuối, thiết bị có thể bỏ dấu vết chờ và mở một ý định mới **chỉ khi** vẫn còn đường tìm kết quả cuối nếu người dùng chưa được xác nhận đã thấy nó. Nguồn phía hệ thống phải cho đúng chủ tìm cả ý định đang chờ lẫn ý định cuối `ĐÃ_LƯU`/`CHẮC_CHƯA_LƯU` **chưa được xác nhận đã thấy kết quả**. Việc xác nhận đã thấy chỉ xảy ra sau khi kết quả đúng ý định đã được trình bày trong ngữ cảnh phiên/chủ còn hợp lệ và xác nhận ấy được ghi bền; nếu đóng browser hoặc mất phản hồi xác nhận trước đó thì lần mở sau có thể hiện lại cùng kết quả. Cách người dùng xác nhận đã thấy còn cần BA/PM rà, không tự coi phản hồi mạng là đã thấy. **Không** xóa rào chắn hoặc kết quả phía hệ thống chỉ vì trình duyệt đã dọn dấu vết hoặc đã xác nhận thấy. Cần chính sách giữ kết quả đủ để mọi request cũ còn khả năng tới đều bị chặn; thời hạn/điều kiện dọn bền chưa được quyết định.

Đây là giao thức ở mức tính chất, không chốt API, bảng hay engine. Phương án hiện thực có thể là transaction với ràng buộc duy nhất và tranh chấp trên một định danh, hoặc cơ chế khác chứng minh cùng tính chất. Một khóa chỉ đặt ở UI, một hàng đợi trong một process, lease hết hạn, hoặc tra danh sách ví không tự cung cấp các tính chất trên.

### Trạng thái quan sát được

| Trạng thái của ý định | Ý nghĩa và điều được phép báo | Chuyển hợp lệ |
| --- | --- | --- |
| `CHƯA_BẤM_LƯU` | Chỉ có bản nhập R68; rời form thì bỏ. Chưa có kết quả lưu. | Sang `ĐANG_GHI_NHẬN` khi bấm Lưu. |
| `ĐANG_GHI_NHẬN` | Định danh đã hình thành nhưng xác nhận ghi nhận bền chưa rõ. Không báo chưa lưu từ timeout; retry cùng định danh/đối chiếu. | Sang `CHỜ_QUYẾT_ĐỊNH` khi xác nhận, hoặc một kết quả cuối an toàn do nơi phân xử quyết định. |
| `CHỜ_QUYẾT_ĐỊNH` | Đã/đang được xử lý; browser có thể đóng. Hiện đang kiểm tra và chặn Lưu trùng cùng ý định. | Sang một trong hai trạng thái cuối qua điểm phân xử nguyên tử. |
| `ĐÃ_LƯU` | Bất biến cuối: đúng một ví bền gắn ý định, đọc lại được bởi đúng chủ. Retry/đối chiếu nhận cùng kết quả. | Không đảo ngược. |
| `CHẮC_CHƯA_LƯU` | Bất biến cuối: rào chắn bền đã thắng, không có ví từ ý định và request cũ không thể ghi sau đó. Mở form trống. | Không đảo ngược. |

Lỗi nhập liệu được chứng minh **trước mọi đường gửi có thể ghi** có thể trả lỗi sửa form theo quy tắc sản phẩm; một khi có khả năng request đã phát, không suy `CHẮC_CHƯA_LƯU` chỉ từ lỗi phía browser. Không có trạng thái cuối tự động chỉ vì hết thời gian chờ.

### Bất biến cần chứng minh

1. Với mỗi cặp **chủ + định danh**, nhiều nhất một kết quả cuối và nhiều nhất một ví. Nếu là `ĐÃ_LƯU`, tham chiếu tới đúng một ví tồn tại bền; nếu là `CHẮC_CHƯA_LƯU`, không request nào mang định danh ấy có thể tạo ví về sau.
2. Mọi đường có thể tạo ví, kể cả retry, worker sau crash/restart và request mạng đến muộn, phải đi qua cùng điểm phân xử và kiểm trạng thái cuối. Không có đường ghi phụ bỏ qua rào chắn. Số dư ban đầu là mốc VND nguyên, không tạo khoản thu.
3. Chủ do phiên phía hệ thống quyết định. Định danh đơn lẻ không là quyền truy cập. Chỉ chủ có phiên ghi chép hợp lệ mới tạo/đối chiếu/đọc ví; tài khoản B hay quản lý không xem được kết quả của A.
4. Nội dung của ý định bất biến sau khi ghi nhận; cùng định danh + nội dung khác không ghi thêm hoặc trả nhầm thành công cũ. Tính duy nhất theo **định danh**, không theo tên hay số dư.
5. Kết quả cuối phải có thể đọc từ nơi lưu có thẩm quyền sau mất phản hồi, đóng browser và đăng nhập lại bằng cùng chủ. Kết quả tra cứu timeout, đọc bản sao trễ hoặc không thấy ví trong danh sách đều là **chưa rõ**.
6. Việc tìm ý định để phục hồi không chỉ lọc trạng thái đang chờ: kết quả cuối chưa được chủ xác nhận đã thấy vẫn phải xuất hiện cho đúng chủ. Xác nhận thấy là trạng thái giao nhận riêng, không đổi `ĐÃ_LƯU`/`CHẮC_CHƯA_LƯU` và không được dùng làm bằng chứng an toàn để xóa fence chống replay.
7. Mọi phản hồi bất đồng bộ phải được đối chiếu với **định danh chủ và thế hệ phiên/ngữ cảnh tài khoản hiện tại** trước khi sửa màn hình, dọn dấu vết hay xác nhận đã thấy. Khi A đã chuyển sang B, phản hồi muộn của A không được render hoặc ghi đè màn B. Email không phải định danh chủ ổn định: tài khoản đã xóa rồi được mời lại với cùng email là một chủ khác.
8. Quyết định tạo ví phải kiểm chủ vẫn đang hoạt động tại cùng điểm thứ tự với việc xóa tài khoản, hoặc có fence tương đương. Nếu xóa tài khoản thắng trước, request cũ không được tạo lại ví/dữ liệu; nếu tạo ví thắng trước thì quy trình xóa phải xử lý ví ấy. Request mang định danh chủ cũ không được gắn vào tài khoản mới được mời lại dù email giống nhau.

## 3. Các cuộc đua minh họa

| Lịch trình | Kết quả hợp lệ cần giữ |
| --- | --- |
| Hai request `A1`, `A2` cùng chủ/định danh/nội dung tới đồng thời | Một request có thể quyết định; request kia đọc cùng kết quả hoặc chờ. Không có hai ví. |
| `A1` tạo ví và kết quả cuối bền; phản hồi mất; `A2` retry | `A2` trả đúng ví của `A1`; browser mở lại đối chiếu thành `ĐÃ_LƯU`. |
| Browser thấy timeout của `A1`; tra cứu chưa thấy ví; `A1` còn trên mạng | Vẫn `CHỜ_QUYẾT_ĐỊNH`. Không được báo chưa lưu từ hai quan sát này. |
| Rào chắn âm bền trước; `A1` tới muộn | `A1` bị chặn tại cùng điểm phân xử, không có ví; đối chiếu giữ `CHẮC_CHƯA_LƯU`. |
| Worker `A1` đã bắt đầu, sau đó yêu cầu kết thúc âm cạnh tranh | Nếu quyết định tạo ví bền trước thì `ĐÃ_LƯU`; nếu rào chắn bền trước thì worker không được commit. Không có kết quả thứ ba hoặc đảo trạng thái. |
| Worker giữ lease, bị dừng lâu; lease hết; rào chắn âm ghi bền; worker cũ tỉnh lại | Token/phiên bản fence phải bị kiểm lúc commit; worker cũ không ghi được. Hết lease đơn thuần không đủ. |
| Cùng định danh nhưng tên hoặc VND khác | Từ chối xung đột nội dung; không sửa ví đã ghi và không tạo ví khác. |
| Hai ý định độc lập có hai định danh, cùng tên và số dư | Không được gộp hai ý định thành một chỉ vì nội dung giống nhau. Kết quả tạo hay từ chối vì **tên trùng** còn chờ quy tắc sản phẩm; ca chứng minh hai ý định hợp lệ dùng tên khác nhau. |
| Request ghi nhận ý định đến muộn sau khi đã bền rào chắn âm cho định danh | Ghi nhận muộn không được mở lại ý định; mọi request Lưu phụ thuộc ghi nhận ấy đều bị chặn. |

**Điểm P0:** Muốn công bố âm an toàn, việc ghi âm và ghi ví phải có thứ tự quyết định chung. Nếu nền tảng không cho tranh chấp có thể kiểm chứng giữa hai hành động này, chỉ có thể giữ **chưa rõ**, chưa đáp ứng đầy đủ nhánh `chắc chưa lưu` của R69. `Abort` phía browser, ngắt kết nối, hết phiên, hoặc không tìm thấy bản ghi ở một lần đọc không phải rào chắn.

## 4. Phục hồi trên thiết bị và qua phiên

| Tình huống | Đề xuất hành vi và điều cần chứng minh |
| --- | --- |
| Đóng/mở browser sau Lưu; dấu vết trên thiết bị còn | Sau đăng nhập cùng chủ và có Internet, tự đối chiếu theo định danh; khi chưa rõ giữ đang kiểm tra, khi cuối thì hiện đúng kết quả. Dấu vết tối thiểu không chứa mật khẩu, tên ví hoặc số dư. |
| Đóng/mở, dấu vết bị xóa hoặc browser không giữ được | Nơi hệ thống phải có đường tìm các ý định đang chờ **và** ý định đã `ĐÃ_LƯU`/`CHẮC_CHƯA_LƯU` nhưng chủ chưa xác nhận đã thấy kết quả, dựa trên ghi nhận bền trước request ghi ví. Với một ý định thì tự đối chiếu và hiện đúng kết quả; với nhiều ý định thì trình bày từng định danh/kết quả cho đúng chủ, không đoán ghép theo tên/số dư hay tự chọn một kết quả. Nếu không thể phục hồi đúng ý định, báo BA/PM; không suy chắc chưa lưu. |
| Request ghi nhận chưa được xác nhận rồi browser chết | Không phát request ghi ví trước xác nhận ghi nhận. Nếu một bản ghi nhận tới muộn, nó chỉ là ý định chưa ghi; khi cần kết thúc âm, rào chắn phải chặn cả bản ghi nhận/retry tới muộn. Cần spike xác minh thứ tự này qua mạng thực. |
| Hai tab cùng một biểu mẫu/ý định | Cùng dùng một định danh và trạng thái pending được đồng bộ/đọc lại; cả hai chỉ hiển thị kết quả từ hệ thống. Đồng bộ cục bộ chỉ giúp UX; ràng buộc bền phía hệ thống vẫn quyết định. Nếu hai tab là hai biểu mẫu độc lập do người dùng chủ động mở, dùng hai định danh. |
| Mở form mới khi ý định cũ còn chưa rõ | Trước khi coi lần bấm là ý định độc lập, giao diện cần đối chiếu/hiện ý định cũ để người dùng không vô tình tạo trùng. Không thể tự suy hai biểu mẫu giống nhau là cùng ý định. Cách cho phép tạo ví khác trong khi một ý định cũ đang treo cần BA/PM rà UX. |
| Đổi tài khoản A → B hoặc hết phiên | Ẩn ngay dấu vết/trạng thái/tên/số dư của A; tăng thế hệ ngữ cảnh tài khoản. Mọi callback của A đến sau phải kiểm lại chủ và thế hệ trước khi render, dọn dấu vết hoặc xác nhận đã thấy; B không thể đối chiếu hay dùng định danh A. Khi A đăng nhập lại, tìm và đối chiếu ý định A. Phiên hết hạn không tự hủy một ghi đã được hệ thống nhận; quyền tại các điểm xử lý cần theo hợp đồng #04. |
| A bị xóa, rồi được mời lại bằng cùng email | Request muộn mang chủ cũ phải bị fence bởi trạng thái xóa tại điểm quyết định ghi; không tạo lại ví sau khi xóa thắng. Tài khoản mới có định danh chủ khác, không kế thừa ý định, ví hoặc quyền đọc kết quả của tài khoản cũ. Cần kiểm tích hợp với R63 và luồng xóa/mời lại, không suy chủ từ email. |
| Kết quả cuối rồi tạo ví tiếp theo | Dọn dấu vết chờ trên browser nhưng để kết quả cuối chưa được xác nhận đã thấy có thể tìm từ phía hệ thống; tạo định danh mới cho ý định mới. Việc xác nhận đã thấy chỉ sau khi hiển thị đúng chủ/đúng thế hệ; kết quả và fence vẫn giữ để chống replay. |

Ghi nhận phía hệ thống trước request có thể tạo ví là **đề xuất** để giảm phụ thuộc vào độ bền lưu trữ browser. Nó không tự giải quyết trường hợp người dùng xóa mọi dấu vết trước khi thao tác được ghi nhận, nhiều ý định đang chờ không phân biệt được, hoặc browser chặn lưu cục bộ. Các giới hạn này cần thử trên Safari iPhone và Chrome Android, rồi BA/PM xử lý nếu R69 không thể được bảo đảm trong phạm vi đã xác nhận.

## 5. Failure matrix cho spike

| Điểm lỗi được chủ động gây ra | Quan sát mong đợi trước khi có kết quả cuối | Bằng chứng kết quả cuối cần thu |
| --- | --- | --- |
| Trước ghi nhận ý định, không phát request ghi ví | Chưa rõ ở client nếu không chứng minh được chưa phát; không tạo ID mới khi retry cùng form | Rào chắn âm cho ID cũ chặn ghi nhận/request tới muộn; hoặc chứng cứ thứ tự cho thấy không có đường ghi. |
| Sau ghi nhận, trước quyết định tạo ví | Đang kiểm tra | Rào chắn thắng thì âm và request tới muộn bị chặn; ghi ví thắng thì dương. |
| Hai request ghi cùng định danh cùng lúc | Đang kiểm tra hoặc cùng kết quả cuối | Một ví tối đa; xung đột nội dung bị từ chối. |
| Crash ngay trước/sau commit quyết định | Đang kiểm tra trong lúc phục hồi | Sau restart chỉ một kết quả cuối nhất quán với sự tồn tại của ví. |
| Commit dương xong nhưng mất phản hồi | Đang kiểm tra trên browser | Mở lại cùng tài khoản tự tìm đúng ví, không tạo thêm. |
| Hủy/kết thúc âm khi request đã nhận hoặc worker bị treo | Đang kiểm tra cho tới khi fence bền | Cho worker cũ tiếp tục sau fence: không thể ghi; nếu ví đã thắng thì không thể công bố âm. |
| Đối chiếu timeout/đọc từ bản sao trễ | Đang kiểm tra | Không kết luận âm từ sự vắng mặt tạm thời. |
| Đóng browser, mở hai tab, xóa dấu vết khi kết quả còn chờ hoặc đã terminal nhưng chưa thấy | Không sinh ID mới cho retry cùng ý định; không suy kết quả từ danh sách ví | Tìm lại cả pending và kết quả cuối chưa được xác nhận đã thấy; nhiều ý định được phân biệt bằng định danh, xác nhận sau khi trình bày cho đúng chủ. |
| Đổi A → B khi response A đến muộn; xóa A rồi mời lại cùng email | B không thấy response hoặc dữ liệu A; request cũ không tạo dữ liệu sau khi xóa thắng | Callback kiểm chủ/thế hệ trước render và ack; ghi ví tranh chấp đúng thứ tự với xóa; chủ mới không đọc/nhận ý định của chủ cũ. |

Spike phải ghi lịch trình sự kiện, điểm commit/fence, trạng thái bền sau crash, số ví theo định danh, kết quả đọc lại trên phiên/điện thoại thứ hai và quyền A/B/quản lý. Cần chạy đường web → xử lý phía hệ thống → nơi lưu → đọc lại; mô phỏng chỉ ở UI hoặc test một request tuần tự không đủ. Bằng chứng phải chỉ ra cơ chế của nền tảng được chọn bảo đảm thứ tự tranh chấp, tính nguyên tử và kiểm fence tại lúc commit; nếu chỉ quan sát test pass ngẫu nhiên, P0 chưa được chứng minh. Chưa thực hiện các phép thử này.

## 6. Vấn đề còn mở trước khi xin review lại

1. Chọn và chứng minh nền tảng lưu bền/phiên có tính nguyên tử, ràng buộc duy nhất, thứ tự quyết định và đọc kết quả phù hợp. #04 chưa có hợp đồng phiên/quyền được duyệt; #06 chưa có nền tảng tạo/đọc ví đã kiểm. Cần quyết định kiến trúc chung rồi review riêng, không tạo hai giao thức cho R67 và R69.
2. Chốt và thử việc ghi thành công marker trước request có thể tạo ví hoặc một đường server recovery thay thế đã chứng minh; tìm lại đúng chủ cả ý định pending lẫn kết quả cuối chưa được xác nhận đã thấy. Chốt cách ack sau khi hiện kết quả, giới hạn/UX khi có nhiều ý định; không dùng tên/số dư để đoán.
3. Chốt vòng đời kết quả/tombstone và replay: giữ kết quả cuối trong discovery cho đến khi đúng chủ xác nhận đã thấy, đồng thời không dọn fence khi request cũ còn khả năng ghi. Một thời hạn chỉ an toàn nếu mọi đường ghi kiểm tuổi/phiên bản tại điểm commit bằng quy tắc được chứng minh; hết hạn cookie/lease hoặc dọn browser chưa đủ.
4. Chốt tính chính xác VND nguyên từ nhập, chuẩn hóa, ghi đến đọc; giới hạn biểu diễn kỹ thuật và lỗi biên; quy tắc tên/hiển thị còn PM rà. Không âm thầm biến giới hạn kỹ thuật thành quy tắc sản phẩm.
5. Thực hiện spike/fault injection theo matrix, gồm race với xóa tài khoản và response muộn sau đổi A/B; xin reviewer kỹ thuật độc lập rà **bằng chứng P0/P1 sau spike** và CapmmPM kiểm phạm vi. Review tài liệu đã diễn ra nhưng chưa thay được spike. Chỉ sau bằng chứng đó mới xem xét cập nhật `technical-design.md` và gate `TECH_READY` theo workflow. Hiện vẫn `STORY_TECH_GAP`; chưa có ticket Dev.
