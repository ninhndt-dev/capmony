# Đề xuất kỹ thuật chung #08–#09: danh mục, khoản thu/chi và số dư

**Trạng thái:** ĐỀ XUẤT CapmmTech để spike và review; **chưa được duyệt**, không thay `technical-design.md`, `handoff.md` hoặc gate của Story. #08 và #09 đã `BA_READY`, còn `STORY_TECH_GAP`; chưa có code, build, kết quả spike/test thực chạy, quyết định nền tảng hoặc `TECH_READY`. Không tạo ticket Dev từ tài liệu này.

## Căn cứ và phạm vi

Nguồn hành vi: [B01, B02, B05, B11, B16, B24](../../docs/requirements/core-business.md), [F08, F09, F14, F21, F25](../../docs/requirements/user-flows.md), [R70–R73](../../docs/requirements/00-intake.md), [#08](story.md), [#09](../capmoney-expense/story.md), [hợp đồng #04](../capmoney-invitation-accept/technical-options.md), [hợp đồng #06](../capmoney-first-wallet/technical-options.md), [#33](../capmoney-overall/issues/33-doi-chieu-luu-thu-chi-chua-ro-trong-phien.md) và [#35](../capmoney-overall/issues/35-doi-chieu-luu-chua-ro-sau-mo-lai.md). Đây là **bất biến và phương án Tech đề xuất**, không phải quyết định sản phẩm mới. Không đặt schema, API, thư viện hay nền tảng lưu trữ.

Một chủ tài khoản ghi chép có định danh ổn định do phía tin cậy cấp, tách khỏi email, phiên và thiết bị. #04 phải giữ cùng định danh qua đăng nhập/đổi email, và cấp định danh **mới** nếu một tài khoản đã xóa được mời lập lại từ đầu. Chủ được lấy từ phiên hợp lệ ở mọi đường tạo/đọc; quản lý, khách và chủ khác không được truy cập dữ liệu tài chính. #06 đặt số dư đầu của ví, không tạo khoản thu giả. #08 và #09 dùng **một** danh sách danh mục và **một** quy tắc số dư. Số tiền của từng khoản thu/chi là số nguyên VND dương; số dư có thể âm. Ngày giao dịch không đổi cách tính số dư hiện tại.

## P0-A — đúng mười danh mục tại lúc tài khoản dùng được

Mốc nghiệp vụ cần chứng minh: khi tài khoản ghi chép mới được kích hoạt và có thể ghi giao dịch, chủ đó đọc được **đúng một bộ mười mục ban đầu**: `Lương`, `Thưởng`, `Ăn uống`, `Đi lại`, `Mua sắm`, `Nhà ở`, `Hóa đơn`, `Sức khỏe`, `Giải trí`, `Khác`. Mỗi mục thuộc chủ, có định danh ổn định và dùng chung cho thu/chi. Tài khoản không được lộ trạng thái “dùng được nhưng thiếu vài mục”. Tài khoản A và B có các mục riêng dù tên giống nhau.

Đề xuất ghi nhận việc **đã cấp bộ ban đầu** bằng một dấu mốc bền gắn với định danh chủ và phiên bản bộ khởi tạo, có cơ chế chống hai lần hoàn tất cùng chủ. Không dùng số mục hiện có hoặc tên hiện có để quyết định seed: #20/#21 cho phép đổi tên hoặc xóa hẳn mục chưa dùng, kể cả mặc định. Sau khi đã cấp, đăng nhập, retry hoàn tất, tải lại và đổi email không cấp lại mục bị sửa/xóa. Mục từng gắn giao dịch không thể xóa theo B05; lúc tạo giao dịch và lúc xóa mục phải phân xử cùng một ranh giới nhất quán, để không vừa xóa mục như “chưa dùng” vừa lưu giao dịch tham chiếu nó. Nếu xóa tài khoản và được mời lại, chủ mới nhận bộ mới, không khôi phục bộ của chủ cũ.

| Mô hình ứng viên | Cách giữ mốc kích hoạt và seed | Rủi ro phải spike |
| --- | --- | --- |
| A. Kích hoạt và cấp mười mục trong một lần commit | Trạng thái tài khoản dùng được, dấu mốc seed và đủ mười mục cùng xuất hiện; retry/đồng thời chỉ một lần thắng. | #04 có thể có ranh giới xác minh email/tạo phiên khác nơi lưu danh mục; cần chứng minh tất cả nằm trong một miền commit hoặc có cách không cấp phiên sớm. |
| B. Cấp trước, chỉ công bố tài khoản dùng được sau khi xác nhận đủ bộ | Có thể tiếp tục sau crash bằng cùng định danh chủ; bước kích hoạt chỉ mở sau khi seed hoàn tất, dấu mốc đã cấp bền và kiểm được. | Không để bộ dở bị đọc như tài khoản hoạt động; cạnh tranh hai luồng seed và retry phải hợp nhất thành đúng một bộ, không tạo mục trùng. |

Không chọn A/B chỉ từ khả năng của một thư viện. Nếu không cùng miền commit, phương án B cần một trạng thái chưa kích hoạt và quy trình phục hồi dở dang có bằng chứng; không dùng “tạo thiếu rồi lần đăng nhập sau bổ sung” vì sẽ tái cấp mục người dùng cố ý xóa. Điều kiện quan sát là bộ mười trước lần ghi đầu tiên, không phải luôn còn mười sau khi người dùng quản lý danh mục.

## P0-B — một ý định Lưu, một tác động và số dư B01

Đề xuất mô hình ý định: trước lần gửi đầu, sinh một định danh thao tác ổn định trong phạm vi chủ và gắn với **nội dung bất biến** của lần Lưu: loại thu/chi, ví, danh mục, số tiền, ngày lịch, ghi chú và các lựa chọn có ảnh hưởng kết quả. Hai ý định độc lập có định danh khác nhau dù nội dung giống hệt. Retry, hai tab hoặc yêu cầu gần đồng thời của **cùng** định danh và nội dung phải đi về một kết quả cuối; cùng định danh với nội dung khác bị từ chối, không được ghi đè kết quả cũ. Định danh không thay quyền: kết quả chỉ đọc được bởi đúng chủ sau xác thực.

Ở **một ranh giới ghi bền** phải phân xử: (1) định danh thao tác chỉ được chấp nhận một lần; (2) kiểm trạng thái hiện hành của chủ, ví, danh mục và đầu vào; (3) ghi đúng một giao dịch thu hoặc chi còn hiệu lực; (4) ghi kết quả cuối của ý định; (5) làm cho số dư đọc sau thành công phản ánh đúng một tác động. Không có trạng thái “đã báo lưu” mà thiếu giao dịch, thiếu kết quả hoặc số dư sai. Lỗi trước commit không để lại giao dịch/tác động nửa chừng. Nếu xử lý bị crash sau commit nhưng trước phản hồi, retry/đối chiếu thấy **cùng** kết quả đã lưu. Kết quả từ chối chắc chắn cũng cần được phân biệt với chưa phân định, để retry cùng ý định không lật ngược một lời báo “chưa lưu”.

Với mỗi ví: **số dư hiện tại = số dư đầu + tổng thu còn hiệu lực + tổng chuyển đến còn hiệu lực − tổng chi còn hiệu lực − tổng chuyển đi còn hiệu lực** (B01). Số tiền giao dịch dương; dấu cộng/trừ do loại giao dịch quyết định. Không kiểm “đủ tiền” khi ghi chi: ví 50.000 VND ghi chi 120.000 VND thành −70.000 VND. Ngày cũ vẫn thay số dư hiện tại ngay. Hợp đồng phải mở rộng được cho chuyển liên kết #13, sửa số dư đầu #16, sửa/xóa/khôi phục #11–#12 và kiểm đồng thời #29; không tính từ một tổng riêng chỉ dành cho #08 hoặc #09.

| Mô hình số dư ứng viên | Bất biến cần chứng minh | Điểm đổi chác / ca rủi ro |
| --- | --- | --- |
| 1. Tính từ số dư đầu và các tác động còn hiệu lực khi đọc | Giao dịch và kết quả ý định cùng commit; phép đọc chính xác lấy tập tác động nhất quán, không bỏ giao dịch vừa được báo lưu. | Chi phí đọc tăng khi nhiều giao dịch; sửa/xóa/khôi phục và chuyển phải tránh đếm đôi; nguồn đọc trễ có thể làm số dư sau thông báo sai. |
| 2. Duy trì số dư hiện tại cùng giao dịch | Giao dịch, kết quả và thay đổi số dư cùng commit; hai ý định khác nhau trên cùng ví phải tuần tự hóa hoặc dùng cơ chế cập nhật có điều kiện và retry an toàn. | Tránh kiểu hai request cùng đọc `b`, rồi lần lượt ghi `b+x`, `b−y` gây mất một tác động; sửa/xóa/khôi phục/chuyển phải cập nhật đủ ví một cách nguyên tử. Cần đối soát với công thức B01. |
| 3. Nhật ký tác động bất biến rồi dựng số dư | Mỗi thao tác có một tác động duy nhất; trạng thái hiệu lực của sửa/xóa/khôi phục quyết định tổng; kết quả đã lưu chỉ công bố khi đường đọc đáp ứng B01. | Phức tạp hơn ở phiên bản/sửa và công bố kết quả; cần chứng minh các phép đọc nhất quán và khả năng đối soát. |

Nếu ứng viên lưu trữ không thể commit cả giao dịch, kết quả và số dư vật hóa cùng ranh giới, mô hình 2 không hợp lệ nếu thiếu cơ chế tương đương đã chứng minh. Với mô hình 1/3, “thành công” vẫn cần đọc lại được giao dịch và B01 ngay theo hợp đồng quan sát; bản sao đọc trễ phải có rào chắn đọc phù hợp. Mọi mô hình cần ngăn lost update khi hai **ID khác nhau** cùng ví, gồm thu+thu, chi+chi và thu+chi, và không gộp chúng vì payload trùng. Không khẳng định mức cô lập hay tính năng của một công nghệ chưa được chọn.

## Kiểm quyền và trạng thái ngay lúc commit

Phía tin cậy lấy owner/role từ phiên, không nhận owner hay số dư mới từ trình duyệt. Ở ranh giới commit của ý định phải kiểm tài khoản còn hợp lệ, ví thuộc chủ và **đang sử dụng**, danh mục thuộc cùng chủ và còn có thể chọn, số tiền/ngày hợp lệ. Việc mở form với dữ liệu hợp lệ trước đó không cho quyền ghi nếu tài khoản vừa bị xóa, ví vừa ngừng hoặc danh mục chưa dùng vừa bị xóa. Thao tác đổi trạng thái ví/xóa danh mục/xóa tài khoản và ghi giao dịch phải có thứ tự phân xử rõ: bên thắng trước quyết định việc bên sau được phép làm; không được có khoản ghi tham chiếu đối tượng đã bị xóa hay ví ngừng ở thời điểm commit. Nếu danh mục đã được giao dịch gắn trước, B05 ngăn xóa hẳn và vẫn cho chọn sau đổi tên.

Đường đọc ví, danh mục, giao dịch và **kết quả ý định** cũng lọc theo chủ từ phiên. Cùng thiết bị đổi từ A sang B hoặc quản lý không được hiện pending/result, số tiền hoặc số dư của A; ID đoán được không trở thành kênh dò dữ liệu. Phản hồi bất đồng bộ của A chỉ được hiển thị nếu tài khoản/phiên đang hiển thị vẫn là A; phản hồi tới muộn sau lúc chuyển sang B phải bị bỏ khỏi màn hình B. Phiên cũ của tài khoản đã xóa không được đọc/ghi tiếp theo R63; request của A đến muộn không được tái tạo tài khoản hay dữ liệu đã xóa. Lời mời lập lại cùng email phải dùng định danh chủ mới. Lỗi trả về không tiết lộ đối tượng của chủ khác có tồn tại hay không. Cần chốt thứ tự giữa xóa tài khoản và request đã vào hệ thống nhưng chưa commit với hợp đồng #04.

## VND nguyên và ngày lịch

Đề xuất tiền được parse một lần thành **số nguyên VND chính xác**, giữ nguyên giá trị qua nhập, truyền, lưu, cộng/trừ và đọc lại; dấu phân cách trình bày không đi vào giá trị. Từ chối phần lẻ, ký tự sai, 0/âm cho **số tiền giao dịch**, tràn miền của từng số tiền hoặc tổng số dư; không làm tròn, không dùng biểu diễn có thể đổi một VND ở giá trị lớn. Số dư đầu của #06 nhận âm/0/dương và mặc định 0 khi bỏ trống. Miền kỹ thuật cụ thể phải được chứng minh cho cả giá trị đầu, từng giao dịch và tổng nhiều giao dịch âm/dương; nếu một giới hạn làm thay đổi hành vi sản phẩm đã chốt, PM/BA đưa Ninh quyết định.

Ngày giao dịch là **ngày lịch do người dùng chọn**, không phải thời điểm tạo request; lưu/đọc không được dịch sang ngày trước/sau do UTC hoặc múi giờ máy. R43 cấm ngày tương lai nên cần chốt một múi giờ có thẩm quyền để xác định “hôm nay” ở phía tin cậy, rồi dùng cùng quy tắc khi hiển thị/kiểm nhập trên iPhone và Android. Chưa có căn cứ chọn múi giờ thiết bị, tài khoản hay cố định cho sản phẩm; đây là quyết định mở. Spike phải thử hai máy khác múi giờ, sát nửa đêm, đổi múi giờ thiết bị và ngày có thay đổi giờ địa phương nếu miền áp dụng. Ngày đã chọn không được đổi chỉ vì người dùng mở lại từ múi giờ khác.

## Hợp đồng phục hồi cho #33/#35

R71/R73 và F21 chỉ xóa **bản nháp trước khi bấm Lưu** khi rời/đóng form; F08 giữ phần đang nhập nếu dữ liệu tự tải mà form vẫn mở. Sau khi bấm Lưu, giữ riêng định danh, nội dung cần đối chiếu và trạng thái ý định. F09/R64 cho rời form khi có Internet; #33 đối chiếu trong phiên, #35 đối chiếu sau đóng/mở bằng cùng tài khoản. Dấu vết này phải đủ bền qua nhiều tab/đóng mở và tách theo chủ; thiết kế lưu phía thiết bị hay phía tin cậy còn mở. Không tạo ID mới cho cùng lần Lưu đang chờ. Chỉ thông báo “đã lưu” sau khi kết quả bền của đúng ID và dữ liệu/số dư đọc được; timeout, thiếu dòng trên màn hình hoặc chưa thấy ở bản sao đọc **không** chứng minh “chưa lưu”.

Trạng thái tối thiểu ở mức ngữ nghĩa: **chưa phân định**, **đã lưu**, **chắc chưa lưu**. Để báo “chắc chưa lưu”, cần một rào chắn bền chứng minh mọi request cũ cùng ID đã kết thúc hoặc về sau chỉ có thể bị từ chối; một kết quả từ chối cuối phải được giữ và không được thay thành công khi request đến muộn. Nếu không chứng minh được rào chắn hoặc thời hạn giữ ID/kết quả, tiếp tục báo “đang kiểm tra”, chặn lưu lặp đúng ý định; không dựa vào hết thời gian chờ. Nơi tin cậy cần có đường tìm cho đúng chủ cả ý định đang chờ **và kết quả cuối chưa được người dùng nhận**, kể cả khi dấu vết trình duyệt mất; chỉ tìm pending sẽ bỏ sót lần lưu đã chốt ngay trước khi đóng. Cần quyết định cách xác nhận đã nhận, lưu giữ kết quả và xử lý nhiều ý định cùng lúc sao cho không ghép theo nội dung và request muộn không tạo khoản trùng, kể cả sau mở lại. Người dùng có thể tạo **ý định độc lập** theo quyền B24, nhưng UI không được ngụy trang retry của thao tác chưa rõ thành ý định mới. Khi chắc chưa lưu sau mở lại, #35 mở form trống; trong phiên còn mở #33 giữ nội dung theo F09 cho tới lúc đối chiếu.

## Ma trận lỗi, race và bằng chứng spike cần thu

Mọi dòng sau là **ca cần chạy sau khi có nền tảng thử**, chưa phải kết quả. Quan sát phía tin cậy tại trước/sau ranh giới commit: chủ, trạng thái seed/ý định, số giao dịch còn hiệu lực, tác động hoặc số dư theo B01 và phép đọc sau thông báo. Dữ liệu thử có A/B/quản lý, ví A/B, hai điện thoại/tab và đồng hồ/mạng điều khiển được.

| Ca spike | Quan sát đủ để phân biệt đúng/sai |
| --- | --- |
| #04 hoàn tất cùng chủ hai lần gần đồng thời; crash giữa cấp mục và kích hoạt | Chỉ một chủ hoạt động, đúng mười mục khi dùng được, không có bộ dở thấy được. Sau đổi tên/xóa mục chưa dùng rồi đăng nhập/retry, không xuất hiện lại mục cũ; chủ được mời lại sau xóa là bộ mới. |
| Hai request cùng ID, cùng nội dung; một request khác nội dung cùng ID | Đúng một giao dịch, một tác động, cùng kết quả cuối; nội dung khác bị từ chối. Thử response mất trước/sau commit và crash tại từng điểm ghi. |
| Hai ID độc lập cùng ví: thu+thu, chi+chi, thu+chi; cả cùng payload | Mỗi ID đã báo lưu có một giao dịch riêng; số dư là tổng đại số đầy đủ, không lost update. Thử ví đầu âm, chi vượt số dư và tổng quay qua 0. |
| Thay chủ/phiên, xóa tài khoản, ngừng ví, xóa danh mục sát commit | Phân xử nhất quán; không ghi khoản sai chủ, vào ví đã ngừng hoặc danh mục đã xóa. Nếu giao dịch thắng trước xóa danh mục, B05 chặn xóa mục đã dùng. Đọc result trực tiếp bằng B/quản lý/khách không rò dữ liệu. |
| Request bị trì hoãn, client timeout, hai tab, đóng/mở rồi request cũ tới | Không báo “chưa lưu” trước khi có rào chắn; retry cùng ID chỉ ra một kết quả. Sau terminal “chắc chưa lưu”, request cũ không thể ghi. #33/#35 tự đối chiếu đúng chủ. |
| VND ở miền biên và tổng sau nhiều khoản; ngày sát nửa đêm trên hai múi giờ | Giá trị đọc lại nguyên vẹn, không tràn/làm tròn; khoản chi âm đúng B01; ngày lịch giữ nguyên, ngày mai bị chặn theo quy tắc “hôm nay” đã chọn. |
| Đọc sau thông báo thành công, kể cả qua điện thoại thứ hai | Giao dịch và số dư đúng ngay theo hợp đồng; nếu hệ thống có bản sao đọc trễ, phải chứng minh cơ chế đọc không báo số dư cũ như kết quả cuối. |

## Quyết định còn mở trước khi xét `TECH_READY`

1. #04 và #08 dùng mô hình A hay B cho kích hoạt/seed; định danh chủ ổn định, ranh giới cấp phiên và xử lý chủ bị xóa/mời lại ra sao? Cần chứng cứ thử retry/crash/đồng thời và review kiến trúc chung.
2. Chọn mô hình số dư 1/2/3 hoặc mô hình tương đương; chứng minh ranh giới giao dịch–kết quả–tác động, chống lost update và cách mở rộng #13/#16/#11–#12. Cần spike tại nơi lưu ứng viên, không chỉ sơ đồ.
3. Chốt vòng đời operation ID, ràng buộc nội dung, điểm terminal “chắc chưa lưu”, rào request muộn, thời hạn giữ, nhiều tab và phân vùng theo owner cho #33/#35. Cần fault injection trước/sau commit và sau timeout.
4. Chốt miền VND nguyên, định dạng truyền/đọc và cách báo vượt miền; múi giờ xác định hôm nay/ngày lịch. PM/BA rà mọi giới hạn chạm hành vi đã xác nhận.
5. Chứng minh kiểm owner/current wallet/category/account tại commit và quyền đọc result qua #04/#06/#07; reviewer kỹ thuật **khác người lập** rà bằng chứng, sau đó CapmmPM kiểm phạm vi. Chỉ khi các gap được giải và gate có bằng chứng trong hiện vật chuẩn mới xét `TECH_READY`; tài liệu đề xuất này tự nó không nâng gate.
