# Bước 9: đối chiếu kỹ thuật tạo ví đầu tiên (#06)

**Ảnh chụp lịch sử trước rebaseline Java:** Các câu hỏi về tính một lần của Lưu, số nguyên VND và đối chiếu R67 còn hữu ích, nhưng lựa chọn công nghệ trong bản này không thay [TS10–TS38](../../docs/requirements/tech-stack-round-2.md) và [ADR 0003](../../docs/adr/0003-java-spring-and-deployment.md). Tech cần thiết kế lại trên Java Spring/PostgreSQL và React/Vite, có reviewer độc lập/PM rà trước `TECH_READY`.

**Trạng thái tại 14/10/2026:** Tech đề xuất; BA → PM → Tech → PM đã rà. R67 đã được Ninh xác nhận; stack Java sau đó đã được chốt, còn cần thiết kế/spike Story trên stack mới trước khi giao Dev.

**Phạm vi:** [Đặc tả #06](spec.md), [ticket #06](../capmoney-overall/issues/06-tao-vi-dau-tien.md), R12/R23/R30/R66–R67, B01/B10/B15, F02/F13/F18/F21. #04 cấp tài khoản ghi chép; #07 tạo ví tiếp theo; #08–#09 ghi giao dịch.

## Hợp đồng cần chứng minh

- Chỉ phiên tài khoản ghi chép hợp lệ mới được tạo/đọc ví. Chủ ví lấy từ phiên phía hệ thống, không tin email hoặc chủ ví do trình duyệt gửi. Tài khoản khác và tài khoản quản lý không thể tạo ví thay hoặc đọc ví này qua màn hình hay đường gọi trực tiếp. Quyền sửa ví kiểm ở #16. Còn phụ thuộc spike xác thực #04.
- Tên ví là bắt buộc theo R66. Kiểm tra cả phía hệ thống để không tạo ví không tên hoặc toàn khoảng trắng qua đường gọi trực tiếp; tên tiếng Việt có dấu phải dùng được. Giới hạn độ dài, chuẩn hóa khoảng trắng và tên trùng là quy tắc Tech đề xuất để PM rà; R66 không chốt tên duy nhất.
- Số dư đầu bỏ trống thành **0 VND**; khi có giá trị thì là số nguyên VND, chấp nhận âm, 0 và dương. Kiểm ở phía hệ thống và dùng biểu diễn số nguyên chính xác từ nhập đến lưu/đọc lại; từ chối phần lẻ hoặc văn bản không hợp lệ, không làm tròn. Không áp trần sản phẩm mới chỉ vì kiểu dữ liệu của ứng viên lưu trữ; nếu có giới hạn kỹ thuật, nêu rõ cho PM rà.
- Khi lưu ví đầu tiên, số dư hiện tại bằng số dư ban đầu cho đến khi có giao dịch; số dư đầu là mốc, không tạo bản ghi khoản thu giả. Tài khoản mới chưa có ví hoặc giao dịch cũ. Ghi nhận thành công phải đọc lại được sau tải trang và trên điện thoại khác của cùng tài khoản khi có Internet.
- Một thao tác Lưu được nhận diện nhất quán qua bấm lặp, hai yêu cầu gần đồng thời và thử lại sau mất phản hồi: chỉ một ví được tạo, các lần thử lại trả về cùng kết quả đã ghi. Hai thao tác tạo ví độc lập của cùng chủ vẫn được phép, theo R15. Không báo thành công khi chưa xác nhận nơi lưu đã ghi.
- Bản nháp **chưa bấm Lưu** chỉ giữ khi chuyển trang rồi quay lại trong cùng phiên ứng dụng có Internet. Đóng hẳn trình duyệt rồi mở lại không khôi phục bản nháp đó. Không nhầm bản nháp với thao tác Lưu đã gửi nhưng còn chưa rõ kết quả. Trên mobile phải thử cả chuyển trang trong app, tạm ngưng tab và đóng/mở hẳn trình duyệt.

## Nhánh sau khi đã bấm Lưu và đóng trình duyệt

- F21 đã chốt trường hợp đóng **trước** khi bấm Lưu ví; F25/R64 có câu hỏi gốc về **giao dịch** sau Lưu. Ninh đã chốt R67 riêng cho **ví đầu tiên**: mở lại cùng tài khoản có Internet thì tự đối chiếu đúng thao tác, chỉ báo kết quả khi chắc, nếu chắc chưa lưu thì biểu mẫu trống, còn chưa rõ thì hiện đang kiểm tra và chặn lưu trùng.
- Tech phải giữ định danh/trạng thái của thao tác Lưu qua lần đóng/mở đủ để đối chiếu chính xác và không tạo ví trùng khi phản hồi mất hoặc xử lý còn chờ. Khi đã ghi thì trả về đúng ví; khi chưa ghi chắc chắn thì báo chưa lưu; khi chưa phân định được thì giữ trạng thái chờ. Không được suy từ danh sách ví chưa thấy ví mới rằng lần Lưu trước chắc chắn thất bại.

## Kiểm chứng trước `ready-for-agent`

- Dùng hai tài khoản ghi chép và tài khoản quản lý, tạo ví có tên/số dư trống, âm/0/dương; thử tên trống, phần lẻ, văn bản, tải lại và gọi đường trực tiếp. Đọc lại số dư đầu, số dư hiện tại và kiểm không có bản ghi khoản thu do tạo ví để xác nhận B01/R30; chỉ chủ ví thấy dữ liệu. Màn lịch sử thuộc #10.
- Kiểm chuyển trang cùng phiên và đóng/mở trình duyệt **trước Lưu** trên Safari iPhone và Chrome Android; chưa có ví trong nơi lưu trước Lưu, bản nháp theo đúng F18/F21.
- Kiểm hai lần gửi gần đồng thời cùng thao tác, mất phản hồi sau khi nơi lưu đã ghi và thử lại; chỉ một ví cho thao tác đó. Kiểm một thao tác tạo ví khác vẫn tạo được ví khác khi đến #07.
- Thử mất phản hồi sau khi nơi lưu đã ghi, đóng trình duyệt ngay sau Lưu và mở lại cùng tài khoản: đối chiếu ra đúng một ví và báo đã lưu. Thử yêu cầu chắc chắn không được ghi: báo chưa lưu, biểu mẫu trống. Thử yêu cầu còn xử lý hoặc kết quả chưa phân định được: hiện đang kiểm tra và chặn thử lại đúng thao tác cho tới khi đối chiếu được; thao tác độc lập vẫn theo quyền R15.
- #06 vẫn `needs-triage` vì #04, quy tắc kỹ thuật tên/tiền, cơ chế lưu một lần và R67 chưa được chứng minh thực tế.
