# Nghiệp vụ chính

**Bước:** 4/11 – Nghiệp vụ chính

**Trạng thái:** Hoàn tất bước 4 ngày 13/10/2026. Sáu vòng đã được Ninh trả lời, BA/PM đã rà soát bản tổng hợp và Ninh trả lời “next” sau khi nhận bản tóm tắt để xác nhận.

**Nguồn:** [Phiếu tiếp nhận](00-intake.md), [mục tiêu và phạm vi](goal-and-scope.md), [người dùng và quyền](actors-and-roles.md), [vòng 1 bước 4](20-step-4-core-business-round-1.md), [vòng 2 bước 4](21-step-4-core-business-round-2.md), [vòng 3 bước 4](22-step-4-core-business-round-3.md), [vòng 4 bước 4](23-step-4-core-business-round-4.md), [vòng 5 bước 4](24-step-4-core-business-round-5.md) và [vòng 6 bước 4](25-step-4-core-business-round-6.md).

## Ninh đã xác nhận

| Mã | Quy tắc | Bằng chứng |
| --- | --- | --- |
| B01 | Số dư hiện tại của một ví bằng số dư ban đầu, cộng các khoản thu và tiền chuyển đến còn hiệu lực, trừ các khoản chi và tiền chuyển đi còn hiệu lực; ngày ghi giao dịch không làm thay đổi công thức này. Thêm, sửa, xóa hoặc khôi phục giao dịch cũ làm tính lại số dư hiện tại. | Ninh trả lời “A hết”, Q1 vòng 1 bước 4; R30, R33, R44, R51, R55 |
| B02 | Số tiền nhập cho mỗi khoản thu, chi hoặc chuyển giữa ví phải là số nguyên VND lớn hơn 0. Điều này không cấm số dư ví âm. | Ninh trả lời “A hết”, Q2 vòng 1 bước 4; R16, R39 |
| B03 | Khi sửa khoản thu/chi, người dùng có thể đổi loại từ thu sang chi hoặc ngược lại. | Ninh trả lời “A hết”, Q3 vòng 1 bước 4 |
| B04 | Ví đã ngừng sử dụng không thể được chọn cho giao dịch hoặc chuyển tiền mới; lịch sử cũ của ví vẫn xem được. Ví cần có số dư bằng 0 trước khi ngừng sử dụng. | Ninh trả lời “A hết”, Q4 vòng 1 bước 4; R48, R53 |
| B05 | Danh mục đã gắn vào giao dịch được giữ lại, chỉ cho đổi tên, không xóa; vẫn chọn được cho giao dịch mới. | Ninh trả lời “A hết”, Q5 vòng 1 bước 4; R49, R54 |
| B06 | Người dùng có thể bật dùng lại ví đã ngừng để ghi giao dịch mới; lịch sử cũ của ví được giữ. | Ninh trả lời “A hết”, Q1 vòng 2 bước 4 |
| B07 | Khoản thu/chi đã lưu sửa được ngày, số tiền, ví và danh mục; ngày không được ở tương lai, số dư các ví liên quan tính lại. Quyền đổi loại thu/chi đã ở B03. | Ninh trả lời “A hết”, Q2 vòng 2 bước 4; B01, R43 |
| B08 | Lần chuyển tiền đã lưu sửa được ngày, số tiền, ví gửi và ví nhận như một thao tác liên kết; ngày không được ở tương lai, số dư các ví liên quan tính lại. | Ninh trả lời “A hết”, Q3 vòng 2 bước 4; B01, R43, R51 |
| B09 | Ví gửi và ví nhận của một lần chuyển tiền phải khác nhau. | Ninh trả lời “A hết”, Q4 vòng 2 bước 4 |
| B10 | Số dư ban đầu của ví được nhập giá trị âm khi tạo hoặc sửa ví. | Ninh trả lời “A hết”, Q5 vòng 2 bước 4; R23, R39–R40 |
| B11 | Danh mục chưa từng gắn vào giao dịch có thể xóa hẳn, kể cả danh mục mặc định, trong danh sách riêng của người dùng. | Ninh trả lời “A hết”, Q6 vòng 2 bước 4; B05 |
| B12 | Khoản thu, chi và chuyển tiền có ghi chú tùy chọn, có thể để trống. | Ninh trả lời “A hết”, Q7 vòng 2 bước 4 |
| B13 | Nếu sửa, xóa hoặc khôi phục giao dịch cũ có thể làm số dư ví đã ngừng khác 0, phải bật lại ví trước thao tác; ví đang ngừng luôn có số dư 0. | Ninh trả lời “A hết”, Q1 vòng 3 bước 4; B04, B06 |
| B14 | Ghi chú của khoản thu, chi và chuyển tiền có thể sửa sau khi lưu, kể cả thêm hoặc xóa nội dung. | Ninh trả lời “A hết”, Q2 vòng 3 bước 4; B12 |
| B15 | Số dư ban đầu của ví chỉ dùng số nguyên VND, kể cả giá trị âm. | Ninh trả lời “A hết”, Q3 vòng 3 bước 4; B10 |
| B16 | Mỗi người dùng mới có cùng bộ mười danh mục mặc định dùng chung cho thu và chi: Lương, Thưởng, Ăn uống, Đi lại, Mua sắm, Nhà ở, Hóa đơn, Sức khỏe, Giải trí, Khác. Người dùng vẫn có quyền thêm, đổi tên và xóa danh mục chưa dùng theo các quy tắc trước. | Ninh trả lời “A hết”, Q4 vòng 3 bước 4; R32, R36, R45, B05, B11 |
| B17 | Bản đầu chưa cần hiện số dư sau từng dòng giao dịch trong lịch sử; cần số dư hiện tại của ví và số tiền từng giao dịch. | Ninh trả lời “A hết”, Q5 vòng 3 bước 4; R07, B01 |
| B18 | Nếu cùng một giao dịch được sửa trên hai điện thoại từ hai bản khác nhau, lần lưu sau báo dữ liệu đã thay đổi và yêu cầu tải lại; không tự ghi đè thay đổi mới hơn. | Ninh trả lời “A hết”, Q6 vòng 3 bước 4; R56 |
| B19 | Ví đang có số dư 0 và chưa từng có giao dịch nào có thể xóa vĩnh viễn. Giao dịch đã xóa mềm vẫn tính là từng có, nên ví đó không thuộc trường hợp này. | Ninh trả lời “A hết”, Q7 vòng 3 bước 4; R48, B04 |
| B20 | Khi sửa khoản thu/chi hoặc lần chuyển để đổi ví, chỉ được chọn ví đang sử dụng; ví đã ngừng phải được bật dùng lại trước khi chọn. | Ninh trả lời “Ahết”, Q1 vòng 4 bước 4; B04, B06, B13 |
| B21 | Giao dịch còn hiệu lực gắn ví đã ngừng được sửa ngày, ghi chú hoặc danh mục của khoản thu/chi nếu số dư ví vẫn bằng 0, không cần bật lại ví. | Ninh trả lời “Ahết”, Q2 vòng 4 bước 4; B13–B14 |
| B22 | Khoản thu, chi hoặc chuyển tiền đã xóa mềm phải khôi phục trước khi sửa; không sửa trực tiếp trong mục đã xóa. | Ninh trả lời “Ahết”, Q3 vòng 4 bước 4; R26, R50 |
| B23 | Cho chuyển tiền lớn hơn số dư hiện tại của ví gửi, khiến ví gửi có thể âm. | Ninh trả lời “Ahết”, Q4 vòng 4 bước 4; R39, B09 |
| B24 | Nếu cùng một tài khoản lưu hai giao dịch khác nhau gần như đồng thời trên hai điện thoại, cả hai khoản đã báo lưu phải được giữ và số dư tính đúng sau cả hai. | Ninh trả lời “Ahết”, Q5 vòng 4 bước 4; R56, S10 |
| B25 | Lần chuyển tiền giữa hai ví của cùng người không cần danh mục; danh mục gắn với khoản thu/chi. | Ninh trả lời “A hét”, Q1 vòng 5 bước 4; R25, R51 |
| B26 | Người dùng có thể đổi tên ví sau khi ví đã có giao dịch hoặc đã ngừng sử dụng; lịch sử ví vẫn giữ. | Ninh trả lời “A hét”, Q2 vòng 5 bước 4; R48 |
| B27 | Nếu hai điện thoại sửa hai giao dịch khác nhau gần như đồng thời, cả hai thay đổi đã báo lưu được giữ và số dư tính đúng. | Ninh trả lời “A hét”, Q3 vòng 5 bước 4; B18, B24 |
| B28 | Nếu một điện thoại đã sửa giao dịch, điện thoại kia định xóa chính giao dịch đó từ bản cũ, lần xóa báo dữ liệu đã thay đổi và yêu cầu tải lại; không xóa dựa trên bản cũ. | Ninh trả lời “A hét”, Q4 vòng 5 bước 4; B18 |
| B29 | Khi đổi tên ví, giao dịch cũ hiển thị tên ví mới và vẫn thuộc cùng ví. | Ninh trả lời “A hêa”, Q1 vòng 6 bước 4; B26 |
| B30 | Nếu hai điện thoại dùng bản khác nhau của cùng một giao dịch, mọi thao tác sửa, xóa hoặc khôi phục từ bản cũ đều báo thay đổi và yêu cầu tải lại; không tự ghi đè. | Ninh trả lời “A hêa”, Q2 vòng 6 bước 4; B18, B28 |
| B31 | Nếu sửa số dư ban đầu có thể khiến ví đang ngừng khác 0, người dùng phải bật dùng lại ví trước khi sửa; ví đang ngừng luôn có số dư 0. | Ninh xác nhận suy luận từ B13 bằng “A hêa”, Q3 vòng 6 bước 4; B13 |

Các quy tắc R07–R56 khác đã chốt trong [phiếu tiếp nhận](00-intake.md) tiếp tục có hiệu lực; bảng này chỉ ghi phần được làm rõ ở bước 4.

## Bờm đề xuất, chưa được Ninh xác nhận

- Không có đề xuất nghiệp vụ chính nào ở sáu vòng đã hỏi còn chờ Ninh chọn.

## Chưa rõ

- Cách nhập, hiển thị và thông báo cụ thể, xử lý mất mạng hoặc bấm lưu lặp, các luồng lỗi/phục hồi và xác minh tài khoản thuộc bước 5 về luồng người dùng.
- Chi tiết như thiết kế màn hình, mô hình dữ liệu, hợp đồng API hoặc công nghệ chưa được xác định ở bước 4.

## Tóm tắt đã xác nhận khi kết thúc bước 4

- Số dư ví bằng số dư ban đầu cộng/trừ các khoản thu, chi và chuyển tiền còn hiệu lực. Mỗi khoản thu, chi hoặc chuyển phải là số nguyên VND lớn hơn 0; số dư ban đầu là số nguyên VND và có thể âm, ví cũng có thể âm. Giao dịch chỉ ghi cho hôm nay hoặc ngày đã qua; sửa, xóa hoặc khôi phục giao dịch cũ làm tính lại số dư hiện tại. Bản đầu chưa cần số dư sau từng dòng lịch sử.
- Ví có thể đổi tên; giao dịch cũ hiển thị tên mới. Ví chỉ ngừng sử dụng khi số dư bằng 0, giữ lịch sử và có thể bật lại. Khi đang ngừng, ví không nhận giao dịch mới và luôn giữ số dư 0; muốn đổi ví cho giao dịch hay sửa số dư ban đầu khiến ví khác 0 phải bật lại trước. Thông tin giao dịch cũ không làm đổi số dư vẫn có thể sửa. Ví 0 đồng, chưa từng có giao dịch có thể xóa vĩnh viễn.
- Khoản thu/chi bắt buộc có danh mục; mỗi người có mười danh mục mặc định dùng chung cho thu và chi: Lương, Thưởng, Ăn uống, Đi lại, Mua sắm, Nhà ở, Hóa đơn, Sức khỏe, Giải trí, Khác. Người dùng được thêm/đổi tên và xóa danh mục chưa từng dùng. Danh mục đã dùng không bị xóa; tên mới hiện cả ở giao dịch cũ. Khoản thu/chi được sửa loại, ngày, số tiền, ví, danh mục và ghi chú. Giao dịch đã xóa mềm được khôi phục không hạn thời gian nhưng phải khôi phục trước khi sửa; bản đầu không xóa vĩnh viễn từng giao dịch.
- Chuyển tiền là một thao tác liên kết hai ví khác nhau của cùng người, phí bằng 0 và không cần danh mục; có thể có ghi chú. Khi tạo hoặc đổi ví của lần chuyển, hai ví được chọn phải đang sử dụng. Có thể chuyển vượt số dư ví gửi, sửa, xóa hoặc khôi phục toàn bộ lần chuyển như một đơn vị.
- Một tài khoản dùng được trên nhiều điện thoại. Hai giao dịch khác nhau được thêm hoặc sửa gần như đồng thời đều được giữ nếu đã báo lưu, và số dư tính đúng; thao tác từ bản cũ của cùng một giao dịch bị chặn và yêu cầu tải lại.

Các chi tiết trong mục “Chưa rõ” vẫn để hỏi ở bước phù hợp; xác nhận bản tóm tắt không tự biến chúng thành quyết định đã chốt.

## Xác nhận kết thúc bước 4

Sau khi BA và PM rà soát và Bờm đưa bản tóm tắt để Ninh xác nhận, Ninh trả lời **“next”**. Ghi nhận đây là xác nhận chuyển sang bước 5 với các quyết định đã liệt kê ở trên; các mục “Chưa rõ” vẫn để mở ở bước phù hợp.
