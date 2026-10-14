# Technical Design: Story #07 – Thêm ví

**Trạng thái Tech:** `STORY_TECH_GAP` – bản thiết kế để review độc lập; **chưa `TECH_READY`**, chưa giao Dev. `BA_READY` đã được CapmmPM ghi trong [story](story.md) và [handoff](handoff.md). Chưa có code, nền tảng đã chọn hoặc kết quả chạy thử.

## Nguồn và ranh giới hệ thống

- Nguồn hành vi: [spec #07, AC1–AC8](spec.md), [Q&A R68–R69](qna.md), [R66–R69 trong phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ](../../docs/requirements/core-business.md), [luồng người dùng](../../docs/requirements/user-flows.md). Dùng [đối chiếu Tech #06](../capmoney-first-wallet/technical-options.md) làm nền cho cùng thao tác tạo/đọc ví, nhưng **không** áp F18 giữ bản nhập dở của ví đầu tiên cho ví thêm.
- #07 nhận một tài khoản ghi chép đã xác thực và có ít nhất một ví; tạo ví tiếp theo, đọc lại danh sách/tên/số dư của các ví thuộc tài khoản. #04 cung cấp phiên và quyền; #06 cung cấp nền tảng tạo/đọc ví. Không thiết kế sửa ví (#16), ngừng/xóa (#18–#19), giao dịch hay màn lịch sử (#08–#10), hoặc điều hướng F20.
- Ranh giới tin cậy: trình duyệt có thể gửi tên, số dư ban đầu và định danh thao tác Lưu; hệ thống xác định chủ từ phiên hợp lệ, kiểm dữ liệu/quyền ở phía hệ thống và ghi vào nơi lưu bền. Danh sách và kết quả đối chiếu chỉ trả về cho đúng chủ. Không tin email, mã chủ ví, số dư hiện tại hoặc trạng thái “đã lưu” do trình duyệt tự gửi.

## Hợp đồng và luồng dữ liệu

1. **Đọc:** phiên ghi chép hợp lệ chỉ nhận các ví của chính mình. Tải lại và đăng nhập cùng tài khoản trên điện thoại thứ hai có Internet phải đọc được các ví đã xác nhận lưu. Danh sách không dùng bộ nhớ của một trình duyệt làm nguồn sự thật; không rò tên hoặc số dư của tài khoản khác. Số dư hiện tại trước giao dịch đầu bằng số dư ban đầu; sau này lấy theo quy tắc số dư chung, không đóng băng giá trị lúc tạo.
2. **Nhập:** tên bắt buộc do người dùng nhập, không sinh tên mặc định. Cả UI và phía hệ thống từ chối tên rỗng hoặc chỉ có khoảng trắng; chấp nhận chữ tiếng Việt có dấu. R66 không đặt ràng buộc tên duy nhất. Quy tắc cắt khoảng trắng/độ dài và cách xử lý tên trùng là đề xuất Tech cần PM rà trước khi chốt; không tự biến tên trùng thành lỗi nghiệp vụ. Số dư đầu để trống là 0 VND; nếu nhập thì là số nguyên VND có dấu, âm/0/dương đều hợp lệ. Kiểm cú pháp và miền số ở phía hệ thống, không làm tròn phần lẻ hay âm thầm đổi giá trị. Cần quyết định định dạng nhập/phân cách hiển thị sao cho ví dụ `-50.000 VND` được hiểu nhất quán; giới hạn kỹ thuật nếu có phải được nêu cho PM, không tự đặt trần sản phẩm.
3. **Trước Lưu:** dữ liệu nhập chỉ là bản tạm trên biểu mẫu. Rời biểu mẫu thêm ví rồi quay lại trong cùng phiên phải mở trống theo R68; đóng hẳn trình duyệt trước Lưu cũng mở trống theo F21. Không tạo ví hoặc giữ bản nháp ví thêm để khôi phục. Ví đã lưu trước đó luôn còn trong danh sách. Quy tắc này **khác** bản nháp ví đầu tiên F18.
4. **Sau Lưu:** xác nhận thành công chỉ khi có kết quả ghi bền cho đúng thao tác và có thể đọc lại ví mới. Tạo ví ghi số dư ban đầu như mốc của ví, không sinh bản ghi khoản thu. Chỉ một ví từ một thao tác Lưu dù bấm lặp, retry hoặc hai yêu cầu đến gần đồng thời; một thao tác tạo ví mới, độc lập của cùng chủ vẫn được tạo ví khác theo R15. Không dùng tên hoặc số dư làm khóa chống trùng vì hai ví độc lập có thể giống nhau.
5. **Kết quả chưa rõ:** trước khi gửi, trình duyệt giữ một định danh thao tác ổn định qua lần đóng/mở cho thao tác đã bấm Lưu; phía hệ thống có trạng thái/kết quả theo đúng chủ và định danh đó. Khi mở lại bằng cùng tài khoản có Internet, tự đối chiếu đúng thao tác theo R69. Nếu xác nhận đã ghi, báo đã lưu và đọc đúng một ví; nếu có bằng chứng chắc chắn không ghi, báo chưa lưu và mở form trống; nếu chưa phân định được, hiện đang kiểm tra và chặn gửi lại **cùng thao tác**. Không kết luận “chưa lưu” chỉ vì danh sách tạm chưa thấy ví hoặc lần tra cứu bị timeout. Bản ghi nhận thao tác đã gửi phải tách khỏi bản nháp chưa Lưu; R68 không xóa dấu vết cần thiết để R69 hoạt động.

## Mô hình dữ liệu ở mức khái niệm

- **Ví:** định danh ổn định, chủ tài khoản, tên, tiền tệ VND, số dư ban đầu và dữ liệu cần để đọc số dư hiện tại. Tạo mới và đọc lại phải giữ số nguyên VND chính xác; không biểu diễn tiền bằng số thực có thể làm tròn. Số dư đầu không phải một khoản thu.
- **Thao tác Lưu:** định danh riêng cho mỗi lần tạo ví có chủ, nội dung được nhận, trạng thái xử lý cuối cùng hoặc còn chờ, và tham chiếu đến **một** ví nếu đã ghi. Cùng định danh với nội dung khác phải bị từ chối, không tái dùng để tạo ví thứ hai. Cần một ràng buộc ghi bền/atomic tương đương để hai yêu cầu đồng thời không tạo hai ví, và để trạng thái “đã lưu” không tồn tại khi ví chưa ghi. Chưa chốt bảng, cột, API, thời hạn lưu thao tác hay công nghệ.
- Dấu vết thao tác ở trình duyệt chỉ giúp nhận ra lần Lưu đã gửi khi mở lại; nơi lưu phía hệ thống mới quyết định kết quả. Thiết kế phải kiểm được trường hợp phản hồi mất sau khi đã ghi, phiên được mở lại, hoặc dấu vết phía trình duyệt còn nhưng hệ thống chưa từng nhận yêu cầu. Việc xác nhận **chắc chưa lưu** cần bằng chứng kết thúc an toàn; sự vắng mặt của ví hay thao tác tại một lần đọc chưa đủ nếu yêu cầu cũ còn có thể tới muộn.

## Đồng thời, lỗi và phục hồi

| Tình huống | Hành vi thiết kế cần bảo đảm |
| --- | --- |
| Tên trống/toàn khoảng trắng, tiền lẻ hoặc chữ, giá trị ngoài miền biểu diễn đã công bố | Từ chối trước khi ghi; không tạo ví, không báo thành công. UI giải thích trường cần sửa; phía hệ thống vẫn kiểm khi gọi trực tiếp. |
| Không có Internet trước khi gửi | Không tạo ví hoặc lưu offline. Báo chưa thể hoàn tất; nếu không chứng minh được yêu cầu đã/không được gửi, giữ trạng thái chưa rõ và đối chiếu khi có Internet. |
| Hai yêu cầu đồng thời hoặc retry cùng định danh thao tác | Chỉ một ví; mọi lần xem kết quả của cùng chủ nhận cùng kết quả cuối cùng. Không dựa vào khóa nút UI để bảo đảm điều này. |
| Phản hồi mất/timeout sau khi hệ thống đã ghi, rồi đóng browser | Giữ định danh thao tác; khi mở lại tự đọc kết quả chính thao tác ấy, báo đã lưu và hiển thị đúng một ví. |
| Yêu cầu vẫn xử lý hoặc kết quả chưa phân định | Hiện “đang kiểm tra”, không báo đã/chưa lưu, không gửi lại cùng thao tác với định danh mới. Có đường đối chiếu lại khi có Internet; không mắc kẹt mãi mà không có cách xác định an toàn. |
| Phiên hết hạn/đổi tài khoản trong lúc chờ | Yêu cầu đăng nhập lại; chỉ chủ thao tác xem kết quả. Không hiển thị dữ liệu tài chính hoặc đối chiếu thao tác của tài khoản trước cho tài khoản khác trên cùng máy. |

Việc hoàn tất chắc chắn “chưa lưu” và giải quyết một thao tác treo phải được chứng minh bằng cơ chế nền tảng được chọn; timeout ở trình duyệt không phải bằng chứng chưa ghi. Sau khi kết quả đã xác định, có thể xóa dấu vết chờ trên thiết bị mà không xóa ví đã lưu. Hai lần tạo ví **độc lập** không bị gộp vì trùng tên/số dư.

## Quyền, an toàn và dữ liệu nhạy cảm

- Kiểm phiên và vai trò trên mọi đường tạo, đọc danh sách và đối chiếu kết quả thao tác. Tài khoản quản lý, khách chưa đăng nhập và tài khoản ghi chép khác không được tạo/đọc ví của chủ. Dùng chủ trong phiên phía hệ thống cho cả ví lẫn thao tác Lưu; không cho đoán định danh thao tác để đọc trạng thái người khác.
- Nếu giữ định danh thao tác trên thiết bị qua đóng/mở, không giữ mật khẩu hoặc dữ liệu tài chính chưa Lưu trong dấu vết này; gắn dấu vết với đúng tài khoản và xóa/ẩn an toàn khi đổi tài khoản. Chính sách phiên/bảo vệ trình duyệt phụ thuộc #04 và cần kiểm chứng thực tế.
- Nhật ký kỹ thuật nếu cần chỉ ghi dấu vết đủ chẩn đoán, không biến thành lịch sử thu/chi hoặc làm lộ số dư/tên ví. Chưa có yêu cầu sản phẩm về audit log riêng cho #07.

## Kiểm thử, dữ liệu thử và điểm quan sát

| AC | Bằng chứng cần có khi có build, ngoài kiểm thử đơn vị |
| --- | --- |
| AC1–AC2 | Tài khoản A đã có ví tạo thêm hai ví, tải lại vẫn đọc cả ví cũ/mới; tên rỗng/toàn khoảng trắng bị từ chối cả UI lẫn gọi trực tiếp; tên có dấu được đọc lại. Hai thao tác độc lập cùng tên vẫn theo quy tắc tên đã được PM duyệt. |
| AC3–AC4 | Lần lượt bỏ trống, 0, số nguyên dương/âm; đọc lại số dư đầu và số dư hiện tại trước giao dịch đúng số nguyên VND, không sinh bản ghi khoản thu. Từ chối phần lẻ/chữ, thử biên biểu diễn đã được Tech chọn; màn lịch sử chỉ kiểm tích hợp khi #10 có. |
| AC5 | Tài khoản A/B và quản lý: B/quản lý không đọc hoặc tạo thay A qua màn hay đường gọi trực tiếp. A tải lại và đăng nhập điện thoại thứ hai có Internet đọc được ví đã lưu; không thấy dữ liệu của B. |
| AC6–AC7 | Trên Safari iPhone và Chrome Android, đóng browser **trước Lưu** rồi mở lại, hoặc rời biểu mẫu ví thêm **trước Lưu** rồi quay lại cùng phiên: form trống, không phát sinh ví, các ví đã lưu vẫn còn. Đối chứng #06: form ví đầu tiên rời rồi quay lại vẫn theo F18. |
| AC8 | Chặn/mô phỏng phản hồi sau khi đã ghi rồi đóng/mở: tự đối chiếu ra đúng một ví. Chắc chắn không ghi: báo chưa lưu và form trống. Xử lý chậm/lỗi đối chiếu: hiện đang kiểm tra, không báo sai hoặc tạo trùng; thử lại khi có Internet. Thử hai request đồng thời cùng định danh, và hai thao tác độc lập tạo hai ví. |

Cần test tích hợp qua web → xử lý phía hệ thống → nơi lưu → đọc lại, với khả năng điều khiển điểm lỗi trước/sau ghi bền và đồng thời. Chưa có build, môi trường, lệnh test hoặc log thực chạy; các dòng trên là kế hoạch kiểm chứng, **không phải kết quả QA**.

## Phụ thuộc, rủi ro và quyết định nền tảng cần review riêng

1. **#04 / tiền đề phiên và quyền:** Tech có thể khảo sát #07 khi chờ #04, nhưng chưa có hợp đồng được duyệt để mở Dev/tích hợp; phiên ghi chép, phân quyền và đổi tài khoản phải được kiểm. Không thể chứng minh AC5 nếu chưa có. Stack xác thực vẫn là ứng viên cần spike, không mặc định Better Auth hay nhà cung cấp nào đã được chọn.
2. **#06 / `HARD` cho Dev và kiểm chứng tích hợp #07:** dùng chung quy tắc tên, VND nguyên chính xác, chủ ví, tạo một lần, nơi lưu và đọc lại. #07 thêm khác biệt R68 (không giữ bản nháp) và R69 (đối chiếu sau Lưu cho ví thêm). Tech có thể thiết kế chung khi #06 chưa xong, nhưng không coi #06 đã đạt gate mới. Chỉ xét đổi cạnh cho phần việc độc lập sang `CONTRACT` khi hợp đồng #06 đã chốt và Tech/PM rà lại. Không viết lại hai luồng tạo ví bằng hai cơ chế chống trùng trái nhau.
3. **Spike bắt buộc trước `TECH_READY`:** chọn và thử nền tảng lưu bền/phiên; chứng minh ghi một lần dưới đồng thời, đối chiếu sau timeout/đóng browser, bằng chứng chắc chưa ghi, đọc từ điện thoại thứ hai, phân quyền chủ/manager và biểu diễn VND chính xác. Ghi quyết định nền tảng chung qua review riêng trước khi khóa hợp đồng triển khai. Không chọn framework, schema hoặc API cụ thể chỉ từ tài liệu này.
4. **Quy tắc còn phải PM duyệt:** chuẩn hóa/giới hạn tên, tên trùng, định dạng nhập tiền và giới hạn biểu diễn kỹ thuật, cách hiển thị lỗi/trạng thái. Nếu một đề xuất làm thay đổi khả năng tạo nhiều ví hoặc trải nghiệm đáng kể, trả BA/PM để hỏi Ninh. Chưa có câu hỏi sản phẩm chặn từ R68–R69.

## Kết quả review độc lập và PM

**Review kỹ thuật độc lập đã thực hiện:** `/root/tech_independent` rà **tài liệu thiết kế** sau lượt lập của CapmmTech và kết luận `STORY_TECH_GAP`. Đây không phải spike, kiểm thử hệ thống hay bằng chứng công nghệ đã đáp ứng. CapmmPM đã đối chiếu phạm vi thiết kế với spec BA và giữ `STORY_TECH_GAP`; chưa `TECH_READY`. Xem [handoff PM](handoff.md).

| Mức | Finding của reviewer và việc CapmmTech cần giải | Bằng chứng cần có trước khi xin review lại |
| --- | --- | --- |
| **P0** | Chưa có cơ chế chứng minh kết quả cuối **“chắc chưa lưu”** khi yêu cầu cũ có thể tới muộn. Thiết kế trạng thái kết thúc âm phải bảo đảm yêu cầu cũ về sau không thể tạo ví hoặc đổi kết quả thành “đã lưu”. Timeout/không tìm thấy ví chỉ được giữ là chưa rõ. | Spike với lịch trình request bị trì hoãn, hủy/kết thúc trước và sau khi xử lý bắt đầu, rồi cho request cũ tới muộn; không bao giờ báo chưa lưu trước khi có rào chắn an toàn được kiểm chứng. |
| **P0** | Chưa chứng minh **idempotency atomic** giữa nhận thao tác, ghi ví và công bố kết quả, nhất là hai request đồng thời cùng định danh hoặc crash giữa các bước. Cùng định danh + khác nội dung phải bị từ chối; thao tác độc lập vẫn tạo được ví khác. | Bằng chứng từ lựa chọn lưu bền và test fault injection/đồng thời: một thao tác tạo tối đa một ví, kết quả đối chiếu gắn đúng ví hoặc giữ chưa rõ, không có trạng thái “đã lưu” khi ví chưa tồn tại. |
| **P1** | Vòng đời dấu vết thao tác chờ trên trình duyệt còn thiếu: tạo trước khi gửi, tồn tại sau đóng/mở, mất dấu vết/xóa storage, đổi tài khoản/phiên, nhiều tab và dọn sau kết quả cuối. Khi thao tác cũ còn chưa rõ, tab/lần mở mới không được sinh ID mới cho cùng ý định Lưu; sau kết quả cuối vẫn cho phép thao tác tạo ví độc lập. Không được làm mất khả năng đối chiếu khi đã bấm Lưu hoặc để lộ trạng thái của chủ cũ. | Hợp đồng phục hồi theo từng trường hợp, kiểm Safari iPhone/Chrome Android; test đóng/mở, đổi tài khoản, nhiều tab, mất dấu vết và thao tác đang chờ. Chỉ ghi giới hạn thực tế sau khi thử; nếu không bảo đảm R69 trong trường hợp sản phẩm yêu cầu, trả BA/PM. |
| **P1** | Phụ thuộc #04 (phiên, vai trò, chủ dữ liệu) và foundation #06 (tạo/đọc ví, định danh thao tác, tiền) chưa có hợp đồng chung đã kiểm; không được dùng hai cơ chế khác nhau cho ví đầu và ví thêm. | Quyết định kiến trúc chung được review riêng, thử tài khoản A/B/quản lý và đối chiếu R67/R69; PM phân loại phụ thuộc trước khi lập ticket. |
| **P1** | Cần chốt biểu diễn/tuần tự hóa VND nguyên, cách đọc lại chính xác và miền giá trị kỹ thuật, gồm số âm, 0, biên lớn và định dạng có dấu phân cách. Không làm tròn, tràn hoặc âm thầm đặt giới hạn sản phẩm. | Test vòng nhập → xử lý → lưu → đọc ở biên; ghi giới hạn/cách báo lỗi cho PM rà trước khi khóa thiết kế. |
| **P1** | Kế hoạch kiểm thử cần điểm fault injection quan sát được ở trước khi nhận, sau khi nhận, trước/sau ghi bền, mất phản hồi, crash/restart và hai yêu cầu cùng lúc; chưa có build hoặc harness nên AC8 mới là giả thuyết. | Chỉ ra cách dựng tình huống và thu bằng chứng web → xử lý → nơi lưu → đọc lại; log/kết quả chạy thực tế sau khi có nền tảng, gồm điện thoại thứ hai và phân quyền. |

Đây là **backlog Tech/spike**, không phải quyết định schema, API hay stack. Sau khi P0 có hợp đồng khả thi và P1 được kiểm chứng hoặc có giới hạn được PM/BA xử lý, CapmmTech cập nhật thiết kế, xin `/root/tech_independent` hoặc reviewer khác người viết rà lại và CapmmPM kiểm phạm vi. Chỉ khi có bằng chứng đó mới cân nhắc `TECH_READY` theo [workflow](../../docs/workflows/capmm-end-to-end.md); chưa chia ticket Dev.
