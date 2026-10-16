# Danh mục chức năng hệ thống và ưu tiên

**Bước:** 6/11 – Chức năng hệ thống

**Trạng thái:** Hoàn tất bước 6 ngày 13/10/2026. PM đã rà soát danh mục và thứ tự, góp ý tách thao tác với khoản thu/chi khỏi lần chuyển tiền; Ninh chọn A xác nhận bản tổng hợp và chuyển sang bước 7. Đây chưa phải đặc tả chức năng hoặc ticket triển khai.

**Nguồn:** [Phiếu tiếp nhận](00-intake.md), [mục tiêu và phạm vi](goal-and-scope.md), [người dùng và quyền](actors-and-roles.md), [nghiệp vụ chính](core-business.md), [luồng người dùng](user-flows.md).

## Ninh đã xác nhận là cần trong bản đầu

Mọi nhóm dưới đây đều thuộc bản đầu và phải hoạt động đúng trước khi phát hành dùng thật theo S02; **ưu tiên** ở bước này chỉ xác định thứ tự chuẩn bị, xây dựng và kiểm chứng, không loại nhóm nào khỏi bản đầu.

| Mã | Nhóm chức năng | Nội dung đã xác nhận | Nguồn |
| --- | --- | --- | --- |
| H01 | Thiết lập và truy cập quản lý | Thiết lập một lần tài khoản quản lý của Ninh qua email đã cung cấp; liên kết thiết lập 3 ngày, tự xin lại trước khi hoàn tất; đăng nhập riêng, đổi email quản lý khi đang đăng nhập. Khi còn dùng được email quản lý đã xác minh nhưng quên mật khẩu, Ninh tự đặt lại qua liên kết riêng có hạn 30 phút; liên kết hết hạn thì tự xin mới ngay trên màn hình báo hết hạn, mọi phiên cũ của tài khoản quản lý kết thúc. | F11, F23–F24; R57–R58, R61; bước 3 |
| H02 | Mời và lập tài khoản ghi chép | Ninh gửi lời mời qua email; người được mời xác minh mã trên email đó và đặt mật khẩu; lời mời 3 ngày, mã có thể xin lại khi lời mời còn hạn, lời mời hết hạn phải do Ninh mời lại. Không tự đăng ký. | R13, R21, R28, R41; F01, F03, F07, F26 |
| H03 | Truy cập tài khoản ghi chép | Đăng nhập email/mật khẩu, tự đặt lại mật khẩu qua liên kết 30 phút, đổi email sau xác minh email mới; kết thúc các phiên cũ theo quy tắc đã chốt. | R22, R29; F04, F12; bước 3 |
| H04 | Ví | Tạo nhiều ví, nhập/sửa số dư ban đầu hoặc bỏ trống để dùng 0 VND, xem số dư hiện tại, đổi tên, ngừng/bật lại và xóa ví đủ điều kiện; giữ lịch sử ví đã ngừng. | R15, R23, R40, R48, R53; B01, B04, B06, B10, B15, B19, B26, B31; F02, F13, F18, F20–F21 |
| H05 | Danh mục | Cấp sẵn mười danh mục cho mỗi người, dùng chung cho thu/chi; thêm, đổi tên, xóa danh mục chưa từng dùng; danh mục đã dùng giữ lại. | R25, R32, R36, R45, R49; B05, B11, B16 |
| H06 | Ghi thu/chi | Ghi khoản thu/chi vào ví đang sử dụng, ngày hôm nay hoặc trước đó, số nguyên VND dương, danh mục bắt buộc, ghi chú tùy chọn; cập nhật số dư theo giao dịch còn hiệu lực. | R07, R38–R39, R43; B01–B02, B12; F14, F20 |
| H07 | Chuyển tiền giữa ví | Chuyển giữa hai ví khác nhau đang sử dụng của cùng người; phí 0, không cần danh mục, tác động hai ví như một đơn vị và cho số dư âm. | R24, R42, R51, R55; B08–B09, B23, B25; F17 |
| H08 | Lịch sử và chỉnh sửa | Lịch sử chung, lọc theo ví, ngày mới nhất trước; cùng ngày thì giao dịch tạo sau trước, sửa không đổi vị trí. Lọc ví gửi hoặc ví nhận đều hiện một dòng đủ ví gửi → ví nhận và số tiền; lịch sử chung cũng một dòng cho lần chuyển. Xem số tiền và số dư hiện tại, sửa khoản thu/chi hoặc lần chuyển; cập nhật số dư khi sửa giao dịch cũ. | R07, R59–R60; B01, B03, B07–B08, B14, B17, B20–B21; F15–F17 |
| H09 | Xóa và khôi phục giao dịch | Xác nhận trước khi xóa mềm; mục riêng cho giao dịch đã xóa, khôi phục không hạn thời gian; chuyển tiền xóa/khôi phục cả hai ví như một đơn vị, giữ quy tắc ví đã ngừng và bản giao dịch cũ. | R26, R33, R37, R46, R50, R55; B13, B22, B30; F19 |
| H10 | Dùng nhiều điện thoại và xử lý lưu chưa rõ | Tải dữ liệu mới khi quay lại, giữ bản nhập dở trong phiên, chặn thao tác từ bản giao dịch cũ; các thao tác khác nhau đã báo lưu đều được giữ; mất mạng lúc lưu thì đối chiếu kết quả trước khi thử lại, không tạo trùng. Khi kết quả chưa rõ, người dùng có Internet được làm việc khác, khi quay lại/mở lại thấy đang kiểm tra nếu chưa rõ hoặc kết quả đã lưu/chưa lưu nếu đã xác định, và không thể lưu lặp đúng thao tác còn chờ. | R11, R27, R56, R64; B18, B24, B27–B30; F06, F08–F09, F21–F22, F25 |
| H11 | Hỗ trợ người dùng mất email | Theo yêu cầu của người dùng, Ninh tra đúng email cũ, gọi lại số đã lưu từ trước ngoài CapMoney và nhận diện chắc người quen; không có số hoặc không chắc đúng người thì chưa đổi. Email mới phải được xác minh; Ninh không xem dữ liệu tài chính. | R62; F10; bước 3 |
| H12 | Tự xóa tài khoản ghi chép | Cảnh báo không khôi phục, nhập lại mật khẩu, xác nhận cuối; xóa tài khoản và dữ liệu ngay. Điện thoại khác bị từ chối đọc/sửa ngay, báo và đưa về đăng nhập khi tải lại/quay lại/thao tác tiếp; Ninh có thể mời lại cùng email để lập tài khoản dữ liệu trống. | R63; F05; bước 3 |

**Điều kiện áp dụng cho mọi nhóm:** Web ưu tiên giao diện mobile trên iPhone và Android; dữ liệu tài chính riêng từng người, chỉ chủ tài khoản xem/sửa. Dùng thật cho khoảng 10 người sau khi Ninh và 2–3 người khác thử đủ chức năng, sửa xong các lỗi đã phát hiện theo S02–S13. Không có chức năng ghi offline. [R04–R16; S01–S13]

**Bổ sung H02 tại bước 9:** R65 cho phép Ninh gửi lại chính liên kết mời chưa dùng, còn hạn tới cùng email khi người nhận không thấy thư; hạn 3 ngày ban đầu không đổi. Quyết định này không thay đổi F07 (xin mã mới) hoặc F03/F26 (mời mới sau hạn). [Vòng làm rõ #03](38-step-9-invitation-send-round-1.md)

**Bổ sung H04 tại bước 9:** R66 yêu cầu người dùng nhập tên trước khi Lưu ví đầu tiên hoặc ví thêm; không tự đặt tên nếu để trống. [Vòng làm rõ #06](39-step-9-first-wallet-round-1.md)

**Bổ sung H04 tại bước 9:** R67 áp dụng cho ví đầu tiên đã bấm Lưu rồi đóng trình duyệt khi kết quả chưa rõ: mở lại cùng tài khoản có Internet thì tự đối chiếu đúng thao tác, chỉ báo kết quả khi chắc và chặn lưu trùng lúc chưa rõ. [Vòng làm rõ #06, Q2](40-step-9-first-wallet-round-2.md)

**Bổ sung H04 tại bước 9:** R68 quy định rời biểu mẫu **ví thêm** trước Lưu rồi quay lại cùng phiên thì biểu mẫu trống; R69 quy định đã bấm Lưu ví thêm rồi đóng khi chưa rõ thì mở lại tự đối chiếu đúng thao tác, chỉ báo khi chắc và chặn lưu trùng lúc chưa rõ. [Vòng làm rõ #07](41-step-9-add-wallet-round-1.md)

**Bổ sung H06 cho Story #08 tại bước 9:** R70 quy định khi mở ghi **khoản thu** từ nút chung với ít nhất hai ví đang sử dụng thì chưa chọn ví, người dùng tự chọn trước Lưu; vào từ ví vẫn theo F14. R71 quy định rời biểu mẫu khoản thu **trước Lưu** rồi quay lại cùng phiên có Internet thì form trống, không tạo khoản thu hay đổi số dư. Hai quyết định này chỉ xác nhận khoản thu; khoản chi được xác nhận riêng sau đó ở R72–R73. [Vòng làm rõ #08](42-step-9-income-round-1.md)

**Bổ sung H06 cho Story #09 tại bước 9:** R72 quy định khi mở ghi **khoản chi** từ nút chung với ít nhất hai ví đang sử dụng thì chưa chọn ví, người dùng tự chọn trước Lưu; vào từ ví vẫn theo F14. R73 quy định rời biểu mẫu khoản chi **trước Lưu** rồi quay lại cùng phiên có Internet thì form trống, không tạo khoản chi hay đổi số dư. Đây là xác nhận riêng cho khoản chi; không suy sang chuyển tiền hoặc tạo ví. [Vòng làm rõ #09](43-step-9-expense-round-1.md)

**Bổ sung H07 cho Story #13 tại bước 9:** R74 quy định rời biểu mẫu **chuyển tiền trước Lưu** rồi quay lại cùng phiên có Internet thì biểu mẫu trống; phần nhập dở không tạo lần chuyển hoặc đổi số dư hai ví. Quy tắc này không xóa trạng thái đối chiếu của lần chuyển đã bấm Lưu. [Vòng làm rõ #10](44-step-9-transfer-round-1.md)

**Bổ sung H07 cho Story #13:** R75 quy định khi mở chuyển tiền từ nút chung, cả ví gửi và ví nhận đều để trống; người dùng chọn đủ hai ví đang sử dụng, khác nhau, trước Lưu. [Vòng làm rõ #11](45-step-9-transfer-wallet-selection.md)

## Ninh đã xác nhận về thứ tự ưu tiên

| Mã | Quyết định | Bằng chứng |
| --- | --- | --- |
| P01 | Sau khi có tài khoản và ví, chuẩn bị bản demo ghi thu/chi, số dư và lịch sử trước chuyển tiền giữa ví. Cả hai nhóm vẫn bắt buộc trước khi dùng thật. | Ninh chọn A, Q2 vòng 1 bước 6 |
| P02 | Kiểm tra đồng bộ và tránh lưu trùng trên hai điện thoại sau khi hoàn thành các luồng một điện thoại, nhưng trước khi nhóm nhỏ bắt đầu thử toàn bộ chức năng. | Ninh chọn B, Q3 vòng 1 bước 6; S09–S10 |
| P03 | Sau bản demo ghi thu/chi, số dư và xem lịch sử trên một điện thoại, ưu tiên xem thử sửa, xóa và khôi phục giao dịch; sau đó chuyển tiền giữa ví. | Ninh chọn A, Q2 vòng 2 bước 6 |

## Thứ tự tổng hợp Ninh đã xác nhận

- (1) H01–H03: thiết lập, mời và đăng nhập → (2) H04–H05: ví và danh mục → (3) H06 cùng phần xem của H08: thu/chi, số dư và lịch sử → (4) phần sửa của H08 cùng H09: sửa, xóa, khôi phục **khoản thu/chi** → (5) H07: chuyển tiền, rồi hoàn tất sửa, xóa, khôi phục **lần chuyển** của H08–H09 → (6) H11–H12: hỗ trợ mất email và tự xóa tài khoản. Đây là thứ tự bản demo và kiểm chứng. H10 là yêu cầu xuyên suốt: kiểm tra dữ liệu riêng ở các nhóm liên quan; kiểm tra đồng bộ và tránh lưu trùng trên hai điện thoại sau các luồng một điện thoại, trước khi nhóm nhỏ thử toàn bộ chức năng. Vị trí triển khai kỹ thuật H10 sẽ xác định ở bước sau.

## Bờm đề xuất, chưa được Ninh xác nhận

- Không có đề xuất chức năng nào đang chờ Ninh xác nhận ở bước 6; các chi tiết còn mở được liệt kê bên dưới.

## Chưa rõ

- Nội dung thông báo trên màn hình liên kết đặt lại mật khẩu quản lý hết hạn chưa chốt; cách tự xin liên kết mới đã chốt ở R61.
- Vị trí triển khai kỹ thuật H10 chưa được chốt; P02 đã chốt thời điểm phải kiểm tra trên hai điện thoại.
- Chi tiết xử lý phiên sau khi xóa tài khoản, bằng chứng để Ninh xác minh người mất email, bố cục lịch sử và các trạng thái lưu chờ sẽ được làm rõ trước đặc tả/triển khai chức năng tương ứng. Quy tắc lọc ví và thứ tự cùng ngày đã chốt ở R59–R60.

## Xác nhận kết thúc bước 6

Ninh chọn **A — danh mục H01–H12 và thứ tự tổng hợp đúng, chuyển sang bước 7**. Các mục “Chưa rõ” tiếp tục được hỏi trước đặc tả chức năng liên quan; việc xác nhận bước 6 không tự chốt các chi tiết đó.

## Không thuộc bản đầu

Ngân sách, báo cáo tổng hợp, xuất dữ liệu, giao dịch định kỳ, mục tiêu tiết kiệm, ghi offline, dùng trên máy tính, sao lưu để khôi phục sau sự cố hệ thống nặng và màn hình gửi phản hồi trong ứng dụng chưa được yêu cầu cho bản đầu. [R10–R11, R17–R20, R34, R52; S04, S12]
