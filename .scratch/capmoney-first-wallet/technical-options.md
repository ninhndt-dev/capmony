# Bước 9: đối chiếu kỹ thuật tạo ví đầu tiên (#06)

**Trạng thái:** Tech đề xuất ngày 14/10/2026; chờ quyết định Q2 về kết quả Lưu ví sau đóng trình duyệt và PM rà bản cuối. Chưa chọn công nghệ hoặc giao Dev.

**Phạm vi:** [Đặc tả #06](spec.md), [ticket #06](../capmoney-overall/issues/06-tao-vi-dau-tien.md), R12/R23/R30/R66, B01/B10/B15, F02/F13/F18/F21. #04 cấp tài khoản ghi chép; #07 tạo ví tiếp theo; #08–#09 ghi giao dịch.

## Hợp đồng cần chứng minh

- Chỉ phiên tài khoản ghi chép hợp lệ mới được tạo/đọc ví. Chủ ví lấy từ phiên phía hệ thống, không tin email hoặc chủ ví do trình duyệt gửi. Tài khoản khác và tài khoản quản lý không thể đọc/sửa ví này qua màn hình hoặc đường gọi trực tiếp. Còn phụ thuộc spike xác thực #04.
- Tên ví là bắt buộc theo R66. Kiểm tra cả phía hệ thống để không tạo ví không tên qua đường gọi trực tiếp. Giới hạn độ dài, khoảng trắng và tên trùng là quy tắc Tech đề xuất để PM rà; R66 không chốt tên duy nhất.
- Số dư đầu bỏ trống thành **0 VND**; khi có giá trị thì là số nguyên VND, chấp nhận âm, 0 và dương. Kiểm ở phía hệ thống và dùng biểu diễn số nguyên chính xác từ nhập đến lưu/đọc lại; từ chối phần lẻ hoặc văn bản không hợp lệ, không làm tròn. Không áp trần sản phẩm mới chỉ vì kiểu dữ liệu của ứng viên lưu trữ; nếu có giới hạn kỹ thuật, nêu rõ cho PM rà.
- Khi lưu ví đầu tiên, số dư hiện tại bằng số dư ban đầu cho đến khi có giao dịch; số dư đầu là mốc, không tạo khoản thu giả trong lịch sử. Tài khoản mới chưa có ví hoặc giao dịch cũ. Ghi nhận thành công phải đọc lại được sau tải trang và trên điện thoại khác của cùng tài khoản khi có Internet.
- Một thao tác Lưu được nhận diện nhất quán qua bấm lặp, hai yêu cầu gần đồng thời và thử lại sau mất phản hồi: chỉ một ví được tạo, các lần thử lại trả về cùng kết quả đã ghi. Hai thao tác tạo ví độc lập của cùng chủ vẫn được phép, theo R15. Không báo thành công khi chưa xác nhận nơi lưu đã ghi.
- Bản nháp **chưa bấm Lưu** chỉ giữ khi chuyển trang rồi quay lại trong cùng phiên ứng dụng có Internet. Đóng hẳn trình duyệt rồi mở lại không khôi phục bản nháp đó. Không nhầm bản nháp với thao tác Lưu đã gửi nhưng còn chưa rõ kết quả. Trên mobile phải thử cả chuyển trang trong app, tạm ngưng tab và đóng/mở hẳn trình duyệt.

## Nhánh sau khi đã bấm Lưu và đóng trình duyệt

- F21 đã chốt trường hợp đóng **trước** khi bấm Lưu ví; F25/R64 có câu hỏi gốc về **giao dịch** sau Lưu. Ninh đang được hỏi Q2 cho ví đầu tiên; chưa tự gắn hành vi F25 cho ví như một yêu cầu đã xác nhận.
- Dù Ninh chọn A hay B, Tech phải giữ định danh/trạng thái của thao tác Lưu đủ để không tạo ví trùng khi phản hồi mất hoặc xử lý còn chờ. Không được suy từ danh sách ví chưa thấy ví mới rằng lần Lưu trước chắc chắn thất bại.

## Kiểm chứng trước `ready-for-agent`

- Dùng hai tài khoản ghi chép và tài khoản quản lý, tạo ví có tên/số dư trống, âm/0/dương; thử tên trống, phần lẻ, văn bản, tải lại và gọi đường trực tiếp. Đọc lại số dư đầu, số dư hiện tại và lịch sử để kiểm B01/R30; chỉ chủ ví thấy dữ liệu.
- Kiểm chuyển trang cùng phiên và đóng/mở trình duyệt **trước Lưu** trên Safari iPhone và Chrome Android; chưa có ví trong nơi lưu trước Lưu, bản nháp theo đúng F18/F21.
- Kiểm hai lần gửi gần đồng thời cùng thao tác, mất phản hồi sau khi nơi lưu đã ghi và thử lại; chỉ một ví cho thao tác đó. Kiểm một thao tác tạo ví khác vẫn tạo được ví khác khi đến #07.
- Sau khi Ninh trả lời Q2, thêm ca đóng/mở **sau Lưu** tương ứng vào đặc tả và ticket. #06 vẫn `needs-triage` vì #04, quy tắc kỹ thuật tên/tiền, cơ chế lưu một lần và hành vi Q2 chưa được chứng minh thực tế.
