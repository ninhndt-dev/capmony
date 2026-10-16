# Phiếu tiếp nhận yêu cầu CapMoney

**Ngày lập:** 12/10/2026  
**Trạng thái:** Đầu ra bước 1 đã được BA/PM rà soát; bước 2–7 đã được Ninh xác nhận; bước 8 đã chia 35 ticket và đang chuyển sang đặc tả chi tiết bước 9

**Loại tài liệu:** Đầu ra bước 1/11 – Thu thập yêu cầu

**Chủ sản phẩm:** Ninh

## Nhu cầu Ninh đã nêu

Ninh yêu cầu bỏ toàn bộ code ứng dụng đã làm và dùng bộ skills của `mattpocock/skills` để khởi động lại tại bước tiếp nhận yêu cầu. Cách làm trước đó mong muốn cấu trúc dự án rõ ràng và các module nhỏ qua BA → PM → Tech → PM → Dev → PM → QA. Các yêu cầu sản phẩm cụ thể sẽ được hỏi lại; ảnh iPhone CapMoney chỉ để tham khảo.

Các câu dưới đây được Ninh xác nhận trong lần làm trước. **Ngày 12/10/2026 Ninh chọn hỏi lại tất cả**, vì vậy bảng này chỉ là lịch sử để đặt câu hỏi, chưa phải yêu cầu của lần khởi động mới.

| Mã | Câu trả lời trong lần trước | Trạng thái lần này |
| --- | --- | --- |
| D01 | Phiên bản đầu nhằm để Ninh dùng thật | Đã hỏi lại ở vòng 2; xem R04, không mặc nhiên giữ câu cũ |
| D02 | Chỉ chủ tài khoản được xem và sửa dữ liệu của một ví | Vòng 4 xác nhận chỉ chủ dữ liệu được xem/sửa, nhưng mỗi người cần nhiều ví; xem R12, R15 |
| D03 | Ứng dụng điện thoại và REST API | Chưa xác nhận app/API; vòng 2 xác nhận web, xem R06 |
| D04 | Điện thoại iPhone/iOS | Vòng 4 xác nhận web cho cả iPhone và Android; xem R14 |
| D05 | Nhập offline, lưu trên máy và đồng bộ sau | Vòng 3 xác nhận bản đầu chỉ dùng khi có Internet; xem R11 |
| D06 | Giữ tám nhóm lõi; giao dịch định kỳ và mục tiêu tiết kiệm sau MVP | R07, R15, R24 và R25 xác nhận một số việc cho bản đầu; R17–R20 và R34 loại năm mục khỏi bản đầu; không giữ nguyên tám nhóm cũ |

Bản nháp trước diễn giải tám nhóm là **Tài khoản, Ví, Danh mục, Giao dịch thu/chi, Chuyển tiền, Ngân sách, Dashboard, Xuất Excel**. Danh sách này chỉ là bối cảnh, cần quyết định lại. Ảnh iPhone là nguồn cảm hứng thiết kế, không phải tiêu chí bắt buộc tái tạo bố cục/hành vi.

## Quyết định của lần khởi động mới

| Mã | Quyết định | Nguồn |
| --- | --- | --- |
| R01 | Hỏi lại toàn bộ yêu cầu, không giữ sáu câu trả lời cũ làm đầu vào đã chốt | Ninh trả lời vòng 1 ngày 12/10/2026 |
| R02 | Ảnh CapMoney chỉ dùng để tham khảo giao diện | Ninh trả lời vòng 1 ngày 12/10/2026 |
| R03 | Không nhập lịch sử từ app CapMoney hiện tại; bắt đầu với dữ liệu trống | Ninh trả lời vòng 1 ngày 12/10/2026 |
| R04 | Bản đầu để sử dụng hằng ngày | Ninh trả lời Q1 vòng 2 ngày 12/10/2026 |
| R05 | Bản đầu phục vụ khoảng 10 người dùng | Ninh trả lời Q2 vòng 2 ngày 12/10/2026 |
| R06 | Bản đầu chạy trên web, giao diện ưu tiên mobile | Ninh trả lời Q3 vòng 2 ngày 12/10/2026 |
| R07 | Bản đầu có ghi thu/chi, xem lịch sử và số dư, sửa hoặc xóa giao dịch | Ninh trả lời Q1 vòng 3 ngày 12/10/2026 |
| R08 | Dữ liệu của mỗi người dùng là riêng | Ninh trả lời Q2 vòng 3 ngày 12/10/2026 |
| R09 | Mỗi người dùng đăng nhập riêng | Ninh trả lời Q3 vòng 3 ngày 12/10/2026 |
| R10 | Bản đầu chưa cần dùng trên máy tính | Ninh trả lời Q4 vòng 3 ngày 12/10/2026 |
| R11 | Bản đầu chỉ sử dụng khi có Internet | Ninh trả lời Q5 vòng 3 ngày 12/10/2026 |
| R12 | Chỉ người sở hữu được xem và sửa dữ liệu tài chính của mình | Ninh chọn Q1 vòng 4 ngày 12/10/2026 |
| R13 | Ninh kiểm soát việc cấp tài khoản cho người dùng; không mở tự đăng ký | Ninh chọn Q2 vòng 4 ngày 12/10/2026 |
| R14 | Bản web đầu dùng được trên điện thoại iPhone và Android | Ninh chọn Q3 vòng 4 ngày 12/10/2026 |
| R15 | Mỗi người cần theo dõi nhiều ví | Ninh chọn Q4 vòng 4 ngày 12/10/2026 |
| R16 | Bản đầu chỉ cần tiền tệ VND | Ninh chọn Q5 vòng 4 ngày 12/10/2026 |
| R17 | Bản đầu chưa cần ngân sách | Ninh chọn Q6a vòng 4 ngày 12/10/2026 |
| R18 | Bản đầu chưa cần báo cáo tổng hợp | Ninh chọn Q6b vòng 4 ngày 12/10/2026 |
| R19 | Bản đầu chưa cần xuất dữ liệu | Ninh chọn Q6c vòng 4 ngày 12/10/2026 |
| R20 | Bản đầu chưa cần giao dịch định kỳ | Ninh chọn Q6d vòng 4 ngày 12/10/2026 |
| R21 | Ninh gửi lời mời; người được mời tự thiết lập tài khoản | Ninh chọn Q1 vòng 5 ngày 12/10/2026 |
| R22 | Mỗi người đăng nhập bằng email và mật khẩu | Ninh chọn Q2 vòng 5 ngày 12/10/2026 |
| R23 | Khi tạo ví, người dùng có thể nhập số dư ban đầu | Ninh chọn Q3 vòng 5 ngày 12/10/2026 |
| R24 | Bản đầu cần chuyển tiền giữa các ví của cùng một người | Ninh chọn Q4 vòng 5 ngày 12/10/2026 |
| R25 | Mỗi khoản thu/chi cần gắn danh mục | Ninh chọn Q5 vòng 5 ngày 12/10/2026 |
| R26 | Giao dịch đã xóa có thể khôi phục | Ninh chọn Q6 vòng 5 ngày 12/10/2026 |
| R27 | Sau khi đăng nhập trên điện thoại khác, người dùng thấy lại dữ liệu đã ghi của mình | Ninh chọn Q7 vòng 5 ngày 12/10/2026 |
| R28 | Ứng dụng gửi liên kết mời đến email của người được mời | Ninh chọn đề xuất Q1 vòng 6 ngày 12/10/2026 |
| R29 | Người dùng tự đặt lại mật khẩu qua email | Ninh chọn đề xuất Q2 vòng 6 ngày 12/10/2026 |
| R30 | Số dư ban đầu là mốc khởi đầu của ví, không hiện như một khoản thu trong lịch sử | Ninh chọn đề xuất Q3 vòng 6 ngày 12/10/2026 |
| R31 | Lịch sử quyết định: có thể ghi phí chuyển; **được thay bởi R42 cho bản đầu** | Ninh chọn đề xuất Q4 vòng 6 ngày 12/10/2026, rồi đổi ở vòng 8 |
| R32 | Mỗi người có thể tự tạo và sửa danh mục thu/chi của mình | Ninh chọn đề xuất Q5 vòng 6 ngày 12/10/2026 |
| R33 | Giao dịch đã xóa ngừng ảnh hưởng số dư ngay; khôi phục thì ảnh hưởng trở lại | Ninh chọn đề xuất Q6 vòng 6 ngày 12/10/2026 |
| R34 | Bản đầu chưa cần mục tiêu tiết kiệm | Ninh chọn đề xuất Q7 vòng 6 ngày 12/10/2026 |
| R35 | Lịch sử làm rõ: phí chuyển để 0; **đã được làm rõ ở R42** | Ninh trả lời Q1 vòng 7 ngày 12/10/2026 |
| R36 | Có danh mục thu/chi mặc định để dùng ngay | Ninh trả lời Q2 vòng 7 ngày 12/10/2026 |
| R37 | Không giới hạn thời gian khôi phục giao dịch đã xóa | Ninh trả lời Q3 vòng 7 ngày 12/10/2026 |
| R38 | Cho phép ghi giao dịch vào ngày do người dùng chọn, không chỉ ngày hiện tại | Ninh trả lời “có ghi cho mọi ngày” ở Q4 vòng 7 ngày 12/10/2026; ngày tương lai sẽ hỏi rõ |
| R39 | Cho phép ví có số dư âm | Ninh trả lời Q5 vòng 7 ngày 12/10/2026 |
| R40 | Cho phép sửa số dư ban đầu của ví sau khi tạo | Ninh trả lời Q6 vòng 7 ngày 12/10/2026 |
| R41 | Liên kết mời người dùng hết hạn sau 3 ngày | Ninh trả lời Q7 vòng 7 ngày 12/10/2026 |
| R42 | Phí chuyển giữa ví cố định bằng 0 trong bản đầu; không nhập phí khác 0 | Ninh chọn B ở Q1 vòng 8 ngày 12/10/2026; thay R31 cho bản đầu |
| R43 | Bản đầu chỉ ghi giao dịch cho hôm nay hoặc ngày đã qua, không ghi ngày tương lai | Ninh chọn B ở Q2 vòng 8 ngày 12/10/2026 |
| R44 | Khi sửa số dư ban đầu của ví, tính lại số dư hiện tại và các số dư về sau theo giá trị mới | Ninh chọn A ở Q3 vòng 8 ngày 12/10/2026 |
| R45 | Trong phạm vi dữ liệu mỗi người, danh mục dùng chung một danh sách cho cả thu và chi | Ninh chọn B ở Q4 vòng 8 ngày 12/10/2026 |
| R46 | Bản đầu chưa cần thao tác xóa vĩnh viễn giao dịch | Ninh chọn B ở Q5 vòng 8 ngày 12/10/2026 |
| R47 | Bản đầu chưa cần hủy liên kết mời trước hạn 3 ngày | Ninh chọn B ở Q6 vòng 8 ngày 12/10/2026 |
| R48 | Có thể ngừng sử dụng ví đã có giao dịch, vẫn giữ lịch sử của ví | Ninh chọn A ở Q1 vòng 9 ngày 12/10/2026 |
| R49 | Danh mục đã dùng vẫn nằm trong danh sách chọn cho giao dịch mới | Ninh chọn B ở Q2 vòng 9 ngày 12/10/2026 |
| R50 | Giao dịch đã xóa nằm trong mục riêng để khôi phục, không hiện trong lịch sử thông thường | Ninh chọn A ở Q3 vòng 9 ngày 12/10/2026 |
| R51 | Chuyển tiền là một thao tác liên kết hai ví, cùng lúc trừ ví gửi và cộng ví nhận | Ninh chọn A ở Q4 vòng 9 ngày 12/10/2026 |
| R52 | Bản đầu chưa yêu cầu bản sao lưu để khôi phục dữ liệu khi hệ thống gặp sự cố | Ninh chọn B ở Q5 vòng 9 ngày 12/10/2026 |
| R53 | Ví cần có số dư bằng 0 trước khi ngừng sử dụng | Ninh chọn A ở Q1 vòng 10 ngày 12/10/2026 |
| R54 | Khi sửa tên danh mục, các giao dịch cũ hiển thị tên mới | Ninh chọn A ở Q2 vòng 10 ngày 12/10/2026 |
| R55 | Xóa hoặc khôi phục một lần chuyển tiền tác động cả ví gửi và ví nhận như một đơn vị | Ninh chọn A ở Q3 vòng 10 ngày 12/10/2026 |
| R56 | Một người có thể đăng nhập và dùng tài khoản trên nhiều điện thoại cùng lúc | Ninh chọn A ở Q4 vòng 10 ngày 12/10/2026 |
| R57 | R29 áp dụng cả tài khoản quản lý: khi Ninh còn dùng được email quản lý đã xác minh và quên mật khẩu, Ninh tự đặt lại qua liên kết gửi email đó; mọi phiên cũ của tài khoản quản lý kết thúc. Hạn liên kết được chốt sau ở R58. | Ninh chọn A ở Q1 vòng 1 bước 6 ngày 13/10/2026 |
| R58 | Liên kết đặt lại mật khẩu tài khoản quản lý hết hạn sau 30 phút kể từ lúc gửi; là liên kết riêng của tài khoản quản lý. | Ninh chọn A ở Q1 vòng 2 bước 6 ngày 13/10/2026 |
| R59 | Trong lịch sử, giao dịch cùng ngày xếp theo thứ tự tạo: tạo sau đứng trước; sửa giao dịch không đổi vị trí trong ngày. | Ninh trả lời “A hết”, Q1 vòng làm rõ 1 bước 8 ngày 13/10/2026 |
| R60 | Khi lọc lịch sử theo ví gửi hoặc ví nhận, mỗi lần chuyển đều hiện một dòng ghi đủ ví gửi → ví nhận và số tiền; trong lịch sử chung vẫn chỉ một dòng. | Ninh trả lời “A hết”, Q2 vòng làm rõ 1 bước 8 ngày 13/10/2026 |
| R61 | Liên kết đặt lại mật khẩu quản lý hết hạn thì Ninh tự xin liên kết mới ngay trên màn hình báo hết hạn; liên kết mới chỉ gửi tới email quản lý đã xác minh, không đổi email hoặc tạo tài khoản quản lý khác. | Ninh trả lời “A hết”, Q3 vòng làm rõ 1 bước 8 ngày 13/10/2026 |
| R62 | Ninh chỉ hỗ trợ người đã quen và nhận diện chắc khi họ đã đăng xuất, mất email cũ: gọi lại số điện thoại Ninh đã lưu từ trước ở ngoài CapMoney, nhận diện qua cuộc gọi và xác nhận yêu cầu đổi email. Không có số đã biết hoặc không chắc đúng người thì chưa đổi; email mới vẫn phải được xác minh. | Ninh trả lời “A hết”, Q1 vòng làm rõ 2 bước 8 ngày 13/10/2026 |
| R63 | Khi tài khoản ghi chép bị xóa, điện thoại khác bị từ chối mọi yêu cầu đọc/sửa ngay từ thời điểm xóa; lần tải lại, quay lại ứng dụng hoặc thao tác tiếp theo sẽ báo tài khoản đã xóa và đưa về đăng nhập. | Ninh trả lời “A hết”, Q2 vòng làm rõ 2 bước 8 ngày 13/10/2026 |
| R64 | Khi kết quả Lưu giao dịch chưa rõ, người dùng có thể rời biểu mẫu và làm việc khác nếu còn Internet. Khi quay lại hoặc mở lại, nếu vẫn chưa xác định được thì thấy trạng thái đang kiểm tra; nếu đã đối chiếu xong thì thấy kết quả đã lưu/chưa lưu theo F09/F25. Ứng dụng tự đối chiếu và chặn lưu lặp đúng thao tác đang chờ. | Ninh trả lời “A hết”, Q3 vòng làm rõ 2 bước 8 ngày 13/10/2026; diễn giải cùng F09/F25 |
| R65 | Khi lời mời chưa dùng, còn hạn 3 ngày nhưng người nhận không thấy email, Ninh dùng tài khoản quản lý gửi lại chính liên kết đó tới cùng email; mốc hết hạn của lần gửi đầu không đổi. Không tạo lời mời mới trong trường hợp này. | Ninh chọn A trên giao diện, Q1 [vòng làm rõ #03 bước 9](38-step-9-invitation-send-round-1.md) ngày 14/10/2026 |
| R66 | Khi tạo ví đầu tiên hoặc ví thêm, người dùng phải nhập tên ví trước khi Lưu; không tự đặt tên mặc định nếu để trống. | Ninh chọn A trên giao diện, Q1 [vòng làm rõ #06 bước 9](39-step-9-first-wallet-round-1.md) ngày 14/10/2026 |
| R67 | Sau khi bấm Lưu ví đầu tiên rồi đóng trình duyệt khi kết quả chưa rõ, mở lại bằng cùng tài khoản có Internet thì CapMoney tự đối chiếu đúng thao tác; chỉ báo đã lưu/chưa lưu khi biết chắc. Nếu chắc chưa lưu, mở biểu mẫu trống; nếu vẫn chưa rõ, hiện đang kiểm tra và chặn lưu trùng. | Ninh chọn A trên giao diện, Q2 [vòng làm rõ #06 bước 9](40-step-9-first-wallet-round-2.md) ngày 14/10/2026 |
| R68 | Khi tạo ví thêm, rời biểu mẫu trước khi bấm Lưu rồi quay lại trong cùng phiên có Internet thì mở lại biểu mẫu trống; chưa tạo ví từ phần nhập dở. Khác F18 của ví đầu tiên. | Ninh chọn B trên giao diện, Q1 [vòng làm rõ #07 bước 9](41-step-9-add-wallet-round-1.md) ngày 14/10/2026 |
| R69 | Sau khi bấm Lưu ví thêm rồi đóng trình duyệt khi kết quả chưa rõ, mở lại cùng tài khoản có Internet thì tự đối chiếu đúng thao tác; chỉ báo đã/chưa lưu khi chắc. Nếu chắc chưa lưu, biểu mẫu trống; nếu còn chưa rõ, hiện đang kiểm tra và chặn lưu trùng. | Ninh chọn A trên giao diện, Q2 [vòng làm rõ #07 bước 9](41-step-9-add-wallet-round-1.md) ngày 14/10/2026 |
| R70 | Khi mở ghi **khoản thu** từ nút ghi chung và có từ hai ví đang sử dụng, biểu mẫu ban đầu chưa chọn ví; người dùng tự chọn ví đang sử dụng trước khi Lưu. Khi vào từ ví, F14 vẫn chọn sẵn ví đó và cho đổi ví. Không suy quy tắc mặc định này cho khoản chi #09. | Ninh chọn A trên giao diện, Q1 [vòng làm rõ #08 bước 9](42-step-9-income-round-1.md) ngày 14/10/2026 |
| R71 | Khi đang nhập **khoản thu**, rời biểu mẫu trước khi bấm Lưu rồi quay lại trong cùng phiên có Internet, biểu mẫu mở trống; không tạo khoản thu hoặc đổi số dư từ phần nhập dở. Không suy quy tắc này cho biểu mẫu khoản chi #09. | Ninh chọn B trên giao diện, Q2 [vòng làm rõ #08 bước 9](42-step-9-income-round-1.md) ngày 14/10/2026 |
| R72 | Khi mở ghi **khoản chi** từ nút ghi chung và có từ hai ví đang sử dụng, biểu mẫu ban đầu chưa chọn ví; người dùng tự chọn ví đang sử dụng trước khi Lưu. Vào từ ví vẫn theo F14. Quyết định riêng cho khoản chi, không suy sang chuyển tiền hoặc tạo ví. | Ninh chọn A trên giao diện, Q1 [vòng làm rõ #09 bước 9](43-step-9-expense-round-1.md) ngày 14/10/2026 |
| R73 | Khi đang nhập **khoản chi**, rời biểu mẫu trước Lưu rồi quay lại trong cùng phiên có Internet thì biểu mẫu trống; phần nhập dở không tạo khoản chi hay đổi số dư. Quyết định riêng cho khoản chi, không suy sang chuyển tiền hoặc tạo ví. | Ninh chọn A trên giao diện, Q2 [vòng làm rõ #09 bước 9](43-step-9-expense-round-1.md) ngày 14/10/2026 |
| R74 | Khi đang nhập **lần chuyển tiền**, rời biểu mẫu trước Lưu rồi quay lại trong cùng phiên có Internet thì biểu mẫu trống; phần nhập dở không tạo lần chuyển hoặc đổi số dư hai ví. Quyết định riêng cho chuyển tiền, không áp dụng cho thao tác đã bấm Lưu mà kết quả chưa rõ. | Ninh chọn “Mở trống như thu/chi” trên giao diện [vòng làm rõ #10 bước 9](44-step-9-transfer-round-1.md) ngày 16/10/2026 |
| R75 | Khi mở biểu mẫu **chuyển tiền từ nút chung**, ban đầu cả ví gửi và ví nhận đều để trống; người dùng tự chọn hai ví đang sử dụng, khác nhau, của mình trước Lưu. | Ninh chọn “Cả hai để trống, tự chọn trước Lưu” trên giao diện [vòng làm rõ #11 bước 9](45-step-9-transfer-wallet-selection.md) ngày 16/10/2026 |
| R76 | Khi **đổi tên ví đã tạo**, tên trống không được Lưu; tên ví đã lưu và lịch sử giữ nguyên. Không chốt quy tắc tên duy nhất. | Ninh chọn “Không cho lưu tên trống” trên giao diện [vòng làm rõ #12 bước 9](46-step-9-wallet-edit-and-return.md) ngày 16/10/2026 |
| R77 | Khi tất cả ví đang ngừng và người dùng tạo hoặc bật lại một ví để quay về biểu mẫu ghi thu/chi, ví ấy được **chọn sẵn**; phần nhập thu/chi cũ trước khi rời form không được khôi phục. | Ninh chọn “Chọn sẵn ví vừa tạo/bật” trên giao diện [vòng làm rõ #12 bước 9](46-step-9-wallet-edit-and-return.md) ngày 16/10/2026 |
| R78 | Khi **thêm hoặc đổi tên danh mục**, tên trống không được Lưu; dữ liệu đã lưu giữ nguyên. Không chốt tên duy nhất, độ dài hay chuẩn hóa khoảng trắng. | Ninh chọn “Không cho Lưu tên trống” trên giao diện [vòng làm rõ #13 bước 9](47-step-9-category-name.md) ngày 16/10/2026 |
| R79 | Sau khi Ninh gọi lại số đã lưu từ trước ngoài ứng dụng, nhận diện chắc người mất email và xác nhận yêu cầu, CapMoney lưu người xác nhận, thời điểm và tài khoản được hỗ trợ để truy vết lần đổi email. Không yêu cầu lưu số điện thoại, ghi âm hoặc ghi chú nhận diện. | Ninh chọn “Lưu người xác nhận, thời điểm và tài khoản được hỗ trợ” trên giao diện [vòng làm rõ #14 bước 9](48-step-9-lost-email-evidence.md) ngày 16/10/2026 |

## Đề xuất, chưa được Ninh xác nhận

- Đề xuất của Bờm ở vòng 2 về ưu tiên ghi chép chính xác và dữ liệu bền vững là đề xuất lịch sử. Các tiêu chí phát hành đã được Ninh xác nhận riêng trong [phiếu mục tiêu và phạm vi](goal-and-scope.md); không suy ra yêu cầu khôi phục sau sự cố nặng từ đề xuất này.

## Những điều chưa được Ninh duyệt riêng

- Chi tiết bảo vệ tài khoản và xử lý sự cố cụ thể. Bản đầu chưa yêu cầu khôi phục từ bản sao lưu sau sự cố nặng theo S04/R52.
- Cách trình bày chi tiết xác nhận CM-26 và câu chữ báo phiên trên điện thoại khác sau khi tài khoản bị xóa. Mức bằng chứng cần lưu cho CM-26 đã chốt ở R79.
- Chi tiết hiển thị lỗi, hết hạn và trạng thái lưu chưa rõ; bố cục màn hình, tài sản ảnh và font. Quy tắc điều hướng khi đang đối chiếu đã chốt ở R64.
- Hợp đồng API, mô hình dữ liệu, công nghệ và nơi lưu dữ liệu. Xuất Excel không thuộc bản đầu theo R19.
- Cách ghi bằng chứng thử nhóm nhỏ, tiêu chí nghiệm thu chi tiết và phiên bản trình duyệt/thiết bị điện thoại cần hỗ trợ. Điều kiện mở rộng đã chốt tại [mục tiêu và phạm vi](goal-and-scope.md); thứ tự demo đã chốt tại [danh mục chức năng](system-functions.md).

Không chuyển các mục này thành yêu cầu đã chốt chỉ vì chúng từng xuất hiện trong tài liệu hoặc code cũ.

R65 chỉ xác nhận gửi lại khi người nhận không thấy email và lời mời chưa dùng, còn hạn. Trường hợp đã mở liên kết, đang nhập mã dở chưa được Ninh duyệt riêng; không suy từ R65 rằng lời mời hoặc mã được gia hạn.

## Việc đang chờ

Các vòng hỏi 1–10 trong thư mục này đã trả lời. BA và PM rà soát ngày 12/10/2026: phiếu tiếp nhận, nhật ký mười vòng và danh sách điều còn mở đủ để bắt đầu bước 2. Các điểm chưa rõ tiếp tục được hỏi ở bước phù hợp, chưa tự chuyển thành yêu cầu đã chốt. Ba vòng hỏi bước 2 đã được trả lời; các quyết định S01–S13, phần đề xuất và điều chưa rõ được ghi tại [phiếu mục tiêu và phạm vi](goal-and-scope.md). BA và PM đã rà soát đầu ra bước 2; Ninh đã xác nhận hiểu đúng và chuyển sang bước 3. Sáu vòng bước 3 đã trả lời và được BA/PM rà soát; xem [phiếu người dùng và quyền](actors-and-roles.md) để phân biệt quyền đã xác nhận với các chi tiết chưa rõ. Đặc biệt, quyền xóa vĩnh viễn ngay cả tài khoản và dữ liệu sau xác nhận cuối là quyết định mới, khác R46 chỉ nói về từng giao dịch. Ninh đã yêu cầu chuyển sang bước 4; các quyết định B01–B05 của [bước 4, vòng 1](20-step-4-core-business-round-1.md) đã được Ninh xác nhận bằng câu trả lời “A hết”. Các quyết định B06–B12 ở [vòng 2 bước 4](21-step-4-core-business-round-2.md) đã được Ninh xác nhận bằng câu trả lời “A hết” ngày 13/10/2026. Các quyết định B13–B19 ở [vòng 3 bước 4](22-step-4-core-business-round-3.md) đã được Ninh xác nhận bằng câu trả lời “A hết” ngày 13/10/2026. Các quyết định B20–B24 ở [vòng 4 bước 4](23-step-4-core-business-round-4.md) đã được Ninh xác nhận bằng câu trả lời “Ahết” ngày 13/10/2026. Các quyết định B25–B28 ở [vòng 5 bước 4](24-step-4-core-business-round-5.md) đã được Ninh xác nhận bằng câu trả lời “A hét”, được hiểu là “A hết”, ngày 13/10/2026. Các quyết định B29–B31 ở [vòng 6 bước 4](25-step-4-core-business-round-6.md) đã được Ninh xác nhận bằng câu trả lời “A hêa”, được hiểu là “A hết”, ngày 13/10/2026. Xem [phiếu nghiệp vụ chính](core-business.md); bản tóm tắt bước 4 đã được BA/PM rà soát và Ninh trả lời “next” để chuyển sang bước 5. Ninh đã chọn “a hết” cho [vòng 1 bước 5](26-step-5-user-flows-round-1.md) ngày 13/10/2026; các quyết định F01–F06 nằm ở [phiếu luồng người dùng](user-flows.md). Ninh chọn A cho Q1–Q6 của [vòng 2 bước 5](27-step-5-user-flows-round-2.md) ngày 13/10/2026; các quyết định F07–F12 đã được ghi vào cùng phiếu. Ninh trả lời “A hếta”, được hiểu là “A hết”, cho Q1–Q4 của [vòng 3 bước 5](28-step-5-user-flows-round-3.md) ngày 13/10/2026; các quyết định F13–F16 đã được ghi vào phiếu luồng người dùng. Ninh trả lời “A cả” cho Q1–Q4 của [vòng 4 bước 5](29-step-5-user-flows-round-4.md) ngày 13/10/2026; các quyết định F17–F20 đã được ghi vào phiếu luồng người dùng. Ninh chọn B, A, B, B cho Q1–Q4 của [vòng 5 bước 5](30-step-5-user-flows-round-5.md) ngày 13/10/2026; các quyết định F21–F24 đã được ghi vào phiếu luồng người dùng. Ninh chọn B cho Q1 của [vòng 6 bước 5](31-step-5-user-flows-round-6.md) ngày 13/10/2026; quyết định F25 đã được ghi vào phiếu luồng người dùng. Bản tổng hợp bước 5 đã được BA → Tech → PM rà soát; Ninh chọn A xác nhận đúng, gồm suy luận lời mời hết hạn giữa lúc nhập mã (F26), và chuyển sang bước 6. Các điểm chưa rõ vẫn được giữ riêng tại [phiếu luồng người dùng](user-flows.md). Ninh đã chọn A, A, B cho Q1–Q3 [vòng 1 bước 6](32-step-6-functions-round-1.md); R57 và ưu tiên P01–P02 nằm ở [danh mục chức năng](system-functions.md). Ninh chọn A, A cho Q1–Q2 [vòng 2 bước 6](33-step-6-functions-round-2.md); R58 và ưu tiên P03 đã được ghi nhận. PM đã rà soát danh mục và thứ tự bước 6; Ninh chọn A xác nhận [danh mục H01–H12 và thứ tự tổng hợp](system-functions.md), chuyển sang bước 7. Các chi tiết còn mở không được coi là đã chốt. Ninh chọn A, A cho Q1–Q2 [vòng 1 bước 7](34-step-7-overall-spec-round-1.md), xác nhận [đặc tả tổng thể](../../.scratch/capmoney-overall/spec.md) và điểm kiểm thử chính, chuyển sang bước 8; các mục `needs-info` vẫn mở trước ticket liên quan. Ninh chọn A cho Q1 rồi A cho Q2 ở [đề xuất bước 8](35-step-8-ticket-breakdown.md), xác nhận cách chia 35 lát, quan hệ phụ thuộc và thứ tự demo. Bờm đã xuất bản 12 ticket đủ rõ (01–09, 23–25) tại [tracker Markdown](../../.scratch/capmoney-overall/issues/) với trạng thái `needs-triage`; các chi tiết `needs-info` vẫn phải làm rõ trước ticket liên quan. Ninh trả lời “A hết” ở [vòng làm rõ 1 bước 8](36-step-8-open-details-round-1.md), xác nhận R59–R61. Ninh trả lời “A hết” cho [vòng làm rõ 2 bước 8](37-step-8-open-details-round-2.md), xác nhận R62–R64. Cả 35 ticket đã được xuất bản ở trạng thái `needs-triage` để qua đặc tả chi tiết bước 9; không có ticket nào đã được giao Dev.
