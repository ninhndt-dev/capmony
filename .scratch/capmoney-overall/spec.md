# CapMoney – đặc tả tổng thể bản đầu

**Bước:** 7/11 – Đặc tả tổng thể
**Status:** needs-info
**Trạng thái:** PM → Tech đã rà soát và đạt; Ninh chọn A/A xác nhận đặc tả tổng thể và điểm kiểm thử chính ngày 13/10/2026, chuyển sang bước 8. Sáu quyết định R59–R64 được bổ sung sau đó; 35 ticket đã được chia để qua đặc tả chi tiết bước 9, chưa triển khai.

**Nguồn quyết định:** [Phiếu tiếp nhận](../../docs/requirements/00-intake.md), [mục tiêu và phạm vi](../../docs/requirements/goal-and-scope.md), [người dùng và quyền](../../docs/requirements/actors-and-roles.md), [nghiệp vụ chính](../../docs/requirements/core-business.md), [luồng người dùng](../../docs/requirements/user-flows.md), [danh mục chức năng và ưu tiên](../../docs/requirements/system-functions.md), [thuật ngữ](../../GLOSSARY.md). Mã R, S, B, F, H và P dưới đây dẫn về các tài liệu đó.

## Problem Statement

Ninh cần ghi chép tài chính hằng ngày trên điện thoại cùng một nhóm tổng cộng khoảng 10 người dùng, mỗi người chỉ thấy dữ liệu của mình. Việc nhập khoản thu/chi, theo dõi nhiều ví và sửa sai phải giữ số dư đúng; cùng tài khoản dùng trên nhiều điện thoại không được làm mất hoặc nhân đôi giao dịch đã báo lưu. [R04–R16, R27, S01, B01, B24]

## Solution

Một web ưu tiên màn hình điện thoại iPhone và Android, dùng khi có Internet, khởi đầu với dữ liệu tài chính trống. Ninh dùng tài khoản quản lý riêng để mời người dùng; mỗi người đăng nhập tài khoản ghi chép riêng, quản lý ví, danh mục, thu/chi, chuyển tiền và lịch sử của mình. Người dùng tự sửa, xóa mềm, khôi phục giao dịch và tự xóa tài khoản. Hệ thống giữ đúng quyền truy cập, số dư và trạng thái lưu khi dùng nhiều điện thoại. Tất cả H01–H12 thuộc bản đầu; thứ tự bản demo ở bước 6 chỉ định thứ tự kiểm chứng. [R01–R03, R06–R12, H01–H12, P01–P03]

## User Stories

1. Là Ninh, tôi muốn thiết lập tài khoản quản lý một lần bằng email đã cung cấp để chỉ mình tôi có quyền mời và hỗ trợ tài khoản. [F11, F23–F24]
2. Là Ninh, tôi muốn xin lại liên kết thiết lập đã hết hạn trước khi hoàn tất để vẫn thiết lập được tài khoản quản lý. [F24]
3. Là Ninh, tôi muốn đăng nhập tài khoản quản lý tách khỏi tài khoản ghi chép cá nhân để công việc quản lý không mở quyền xem dữ liệu tài chính người khác. [bước 3]
4. Là Ninh, tôi muốn gửi lời mời đến email người dùng để kiểm soát việc cấp tài khoản mà không mở tự đăng ký. [R13, R21, R28]
5. Là người được mời, tôi muốn xác minh mã gửi đúng email được mời rồi đặt mật khẩu để lập tài khoản riêng. [F01]
6. Là người được mời, tôi muốn xin mã mới khi mã sai hoặc hết hạn trong lúc lời mời còn hiệu lực để hoàn tất thiết lập. [F07]
7. Là người được mời có lời mời hết hạn, tôi muốn biết phải liên hệ Ninh để được mời lại, kể cả khi đang nhập mã. [F03, F26]
8. Là người dùng, tôi muốn đăng nhập bằng email và mật khẩu để truy cập dữ liệu của mình. [R22]
9. Là người dùng tài khoản ghi chép còn dùng được email, tôi muốn tự đặt lại mật khẩu qua liên kết có hạn 30 phút và đăng nhập lại sau khi mọi phiên cũ kết thúc. [F04, F12]
10. Là Ninh còn dùng được email quản lý, tôi muốn tự đặt lại mật khẩu quản lý qua liên kết riêng có hạn 30 phút, tự xin liên kết mới trên màn hình báo hết hạn và kết thúc các phiên cũ khi đặt lại thành công. [R57–R58, R61]
11. Là người dùng đang đăng nhập, tôi muốn đổi email sau khi xác minh email mới để tiếp tục dùng tài khoản của mình; các phiên cũ kết thúc. [bước 3]
12. Là người dùng đã đăng xuất và mất email cũ, tôi muốn nhờ Ninh đổi email sau khi xác minh đúng người và email mới để lấy lại tài khoản. [F10]
13. Là Ninh, tôi muốn hỗ trợ người mất email theo yêu cầu bằng tài khoản quản lý mà không xem dữ liệu tài chính của họ. [F10, H11]
14. Là người mới chưa có ví, tôi muốn được dẫn tạo ví đầu tiên để bắt đầu ghi thu/chi. [F02]
15. Là người dùng, tôi muốn tạo nhiều ví với số dư ban đầu tùy chọn để theo dõi các nguồn tiền riêng; bỏ trống thì dùng 0 VND. [R15, R23, F13]
16. Là người dùng, tôi muốn xem số dư hiện tại của mỗi ví để biết số tiền theo các giao dịch còn hiệu lực. [B01]
17. Là người dùng, tôi muốn sửa số dư ban đầu hoặc đổi tên ví để chỉnh thông tin của ví mà vẫn giữ lịch sử. [R40, B26, B29]
18. Là người dùng, tôi muốn ngừng ví có số dư 0 và bật lại khi cần; ví đã ngừng vẫn xem được lịch sử nhưng không nhận giao dịch mới. [B04, B06]
19. Là người dùng, tôi muốn xóa hẳn ví chưa từng có giao dịch và có số dư 0 để dọn danh sách ví. [B19]
20. Là người dùng mới, tôi muốn có sẵn mười danh mục đã chốt dùng chung cho thu và chi để ghi chép ngay. [B16]
21. Là người dùng, tôi muốn thêm và đổi tên danh mục của mình; tên mới cũng hiện trong giao dịch cũ. [R32, R54]
22. Là người dùng, tôi muốn xóa danh mục chưa từng dùng, kể cả danh mục mặc định; danh mục đã dùng được giữ lại và vẫn chọn được. [B05, B11]
23. Là người dùng, tôi muốn ghi khoản thu/chi vào ví đang sử dụng từ nút chung hoặc từ ví để cập nhật số dư và lịch sử. [R07, F14]
24. Là người dùng, tôi muốn chọn ngày hôm nay hoặc ngày đã qua, số nguyên VND dương, danh mục và ghi chú tùy chọn khi ghi thu/chi. [B02, B12, R25, R43]
25. Là người dùng không có ví đang sử dụng, tôi muốn được dẫn tạo ví mới hoặc bật lại ví đã ngừng rồi quay về ghi thu/chi. [F02, F20]
26. Là người dùng, tôi muốn chuyển tiền phí 0 giữa hai ví đang sử dụng khác nhau của mình, kể cả khi ví gửi sẽ âm, để phản ánh việc chuyển thực tế. [R42, B09, B23, B25]
27. Là người dùng, tôi muốn xem lịch sử chung, lọc theo ví và thấy ngày mới nhất trước; giao dịch tạo sau cùng ngày đứng trước và sửa không đổi vị trí. [F15–F16, R59]
28. Là người dùng, tôi muốn thấy một dòng nêu cả ví gửi và ví nhận cho mỗi lần chuyển tiền trong lịch sử chung và khi lọc theo cả hai ví. [F17, R60]
29. Là người dùng, tôi muốn sửa loại, ngày, số tiền, ví, danh mục và ghi chú của khoản thu/chi đã lưu để chữa sai bản ghi và tính lại số dư. [B01, B03, B07, B14]
30. Là người dùng, tôi muốn sửa ngày, số tiền, hai ví và ghi chú của lần chuyển như một đơn vị để số dư hai ví đều đúng. [B08, B14]
31. Là người dùng, tôi muốn được cảnh báo và xác nhận trước khi xóa mềm giao dịch; giao dịch đã xóa nằm ở mục riêng và ngừng tác động số dư. [R33, R50, F19]
32. Là người dùng, tôi muốn khôi phục giao dịch đã xóa bất cứ lúc nào để giao dịch lại tác động số dư; tôi khôi phục trước nếu muốn sửa nó. [R26, R37, B22]
33. Là người dùng, tôi muốn ứng dụng yêu cầu bật lại ví đã ngừng trước thao tác có thể làm số dư ví ấy khác 0 để ví ngừng luôn giữ số dư 0. [B13, B20–B21, B31]
34. Là người dùng trên điện thoại thứ hai, tôi muốn thấy dữ liệu đã lưu từ điện thoại thứ nhất khi quay lại ứng dụng, đồng thời giữ phần giao dịch đang nhập dở trong phiên. [F06, F08]
35. Là người dùng trên hai điện thoại, tôi muốn các giao dịch khác nhau đã báo lưu đều được giữ và số dư đúng. [B24, B27]
36. Là người dùng sửa/xóa/khôi phục từ bản giao dịch cũ, tôi muốn được báo dữ liệu đã thay đổi, giữ phần vừa nhập để tự so sánh với bản mới, thay vì ghi đè. [B18, B28, B30, F22]
37. Là người dùng bị mất mạng sau khi bấm Lưu, trước khi biết kết quả, tôi muốn thấy kết quả “chưa rõ” và được tự đối chiếu khi có mạng lại để không bị báo thành công sai hoặc lưu trùng. [F09]
38. Là người dùng đã đóng trình duyệt sau khi bấm Lưu mà kết quả chưa rõ, tôi muốn ứng dụng tự đối chiếu khi mở lại; khi xác nhận chưa lưu thì cho nhập lại từ biểu mẫu trống. [F25]
39. Là người dùng rời trang trong cùng phiên trước khi lưu ví đầu tiên, tôi muốn giữ phần nhập dở khi quay lại; nếu đã đóng hẳn trình duyệt trước khi bấm Lưu, tôi bắt đầu biểu mẫu mới. [F18, F21]
40. Là người dùng muốn rời CapMoney, tôi muốn thấy cảnh báo, nhập lại mật khẩu và xác nhận cuối trước khi tài khoản ghi chép cùng toàn bộ dữ liệu bị xóa vĩnh viễn. [F05, bước 3]
41. Là Ninh, tôi muốn mời lại người đã xóa tài khoản bằng cùng email để họ bắt đầu tài khoản mới với dữ liệu trống. [bước 3]

## Implementation Decisions

Đây là các **quy tắc sản phẩm đã chốt**, chưa phải lựa chọn công nghệ, mô hình dữ liệu hay hợp đồng API.

- Web ưu tiên mobile trên iPhone và Android, chỉ dùng khi có Internet; VND là tiền tệ duy nhất. Mỗi người bắt đầu với dữ liệu trống và chỉ chủ tài khoản được xem/sửa dữ liệu tài chính. [R03, R06, R08, R11–R16]
- Ninh có tài khoản quản lý riêng dùng email khác tài khoản ghi chép cá nhân; chỉ tài khoản quản lý mời hoặc hỗ trợ người mất email. Không có người quản lý dự phòng hay tự đăng ký. [bước 3]
- Số dư hiện tại của ví = số dư ban đầu + thu + chuyển đến − chi − chuyển đi đối với giao dịch còn hiệu lực; ngày giao dịch không đổi công thức. Số dư ban đầu là số nguyên VND có thể âm; số tiền từng giao dịch là số nguyên VND dương. [B01–B02, B10, B15]
- Giao dịch chỉ dùng hôm nay hoặc ngày đã qua; số dư ví được phép âm. Ví ngừng sử dụng có số dư 0, giữ lịch sử và không được chọn cho giao dịch mới. [R39, R43, B04, B13]
- Thu/chi bắt buộc chọn danh mục trong danh sách riêng mỗi người. Chuyển tiền không cần danh mục, phí cố định 0 và tác động hai ví cùng lúc; một lần chuyển hiện một dòng trong lịch sử chung. [R42, R45, R51, B25, F17]
- Xóa giao dịch là xóa mềm, không còn ảnh hưởng số dư; khôi phục không giới hạn thời gian. Xóa tài khoản ghi chép là xóa vĩnh viễn ngay sau xác nhận cuối, khác với xóa giao dịch. [R26, R33, R37, F05]
- Mọi thao tác sửa, xóa hoặc khôi phục từ bản cũ của cùng giao dịch bị chặn; các giao dịch khác nhau lưu gần đồng thời không được mất. Kết quả lưu chưa biết phải đối chiếu trước khi gửi lại và không được tạo trùng. [B24, B27–B30, F09, F25]
- Tài khoản quản lý đặt lại mật khẩu qua liên kết riêng 30 phút; khi liên kết hết hạn, Ninh tự xin liên kết mới trên màn hình báo hết hạn, chỉ gửi tới email quản lý đã xác minh. Tài khoản ghi chép cũng có liên kết 30 phút. Liên kết thiết lập quản lý và lời mời người dùng đều có hạn 3 ngày, theo hai luồng riêng. [R41, R57–R58, R61, F12, F23–F24]
- Lịch sử ngày mới nhất trước; cùng ngày thì giao dịch tạo sau đứng trước, sửa không đổi vị trí. Một lần chuyển hiện một dòng đủ ví gửi → ví nhận và số tiền trong lịch sử chung cũng như khi lọc theo từng ví. [F15–F17, R59–R60]
- Thứ tự bản demo và kiểm chứng theo H01–H03 → H04–H05 → H06 + xem H08 → sửa H08 + H09 cho thu/chi → H07 + thao tác tương ứng cho lần chuyển → H11–H12. H10 xuyên suốt; kiểm tra hai điện thoại sau các luồng một điện thoại, trước nhóm thử toàn bộ chức năng. Không bỏ bất kỳ nhóm H01–H12 khỏi bản đầu. [P01–P03, xác nhận bước 6]

## Testing Decisions

- **Điểm kiểm thử chính Ninh đã xác nhận:** kiểm thử hành vi qua web trên thiết bị thật, từ thao tác người dùng đến dữ liệu và số dư đọc lại sau tải lại trang hoặc trên điện thoại thứ hai. Từ đầu đến cuối dùng ít nhất hai tài khoản tài chính riêng; iPhone và Android đều được kiểm chứng trước khi dùng thật. [S05; Q2 vòng 1 bước 7]
- Với tài khoản quản lý và lời mời: thử liên kết đúng/hết hạn, mã sai/hết hạn, cấp lại theo quyền, đặt lại mật khẩu và các phiên cũ. Kết quả đạt khi người ngoài không tự lập tài khoản hoặc truy cập dữ liệu người khác. [F01, F03–F04, F07, F23–F26, R57–R58]
- Với ví, danh mục và giao dịch: thử số dư đầu âm/0, thu, chi, chuyển vượt số dư, sửa giao dịch cũ, xóa/khôi phục, ví ngừng, danh mục đã dùng và lịch sử. Đối chiếu số dư đọc lại với B01 sau từng thao tác; một lần chuyển chỉ trừ ví gửi và cộng ví nhận mỗi bên một lần. Khi xóa/khôi phục có thể làm ví đã ngừng khác 0, phải bật ví lại trước. [B01–B31]
- Với hỗ trợ mất email và xóa tài khoản: thử Ninh chỉ tra đúng email cũ theo yêu cầu, gọi lại số đã lưu trước ngoài CapMoney và chỉ đổi khi nhận diện chắc người quen cùng email mới đã xác minh, không thấy dữ liệu tài chính; khi xóa tài khoản thì điện thoại khác mất quyền đọc/sửa ngay và được báo khi tải lại/quay lại/thao tác tiếp, cùng email được mời lại thành tài khoản dữ liệu trống. Câu chữ và bố cục thông báo còn để bước 9. [F05, F10, R62–R63, bước 3]
- Với nhiều điện thoại: thử hai tài khoản tách dữ liệu; một tài khoản trên hai điện thoại, hai giao dịch khác nhau gần đồng thời, cùng giao dịch bản cũ, mất mạng trước khi biết kết quả lưu, rời biểu mẫu làm việc khác khi có Internet và đóng/mở trình duyệt sau khi bấm Lưu. Kết quả đạt khi không mất giao dịch đã báo lưu, không lưu trùng, không ghi đè bản mới; khi quay lại/mở lại thấy trạng thái chờ nếu chưa rõ hoặc kết quả đã lưu/chưa lưu nếu đã xác định, và chặn lưu lặp đúng thao tác còn chờ. [B18, B24, B27–B30, F06, F08–F09, F25, R64]
- Nhóm thử Ninh và 2–3 người khác thử **toàn bộ** chức năng trước khi mở đến khoảng 10 người; ghi kết quả từng chức năng ít nhất một lần tính trên cả nhóm. Ninh quyết định thời điểm mở sau khi sửa hết lỗi đã phát hiện; chưa có hạn ngày cố định. [S02–S03, S06–S13]
- Repo hiện không có code ứng dụng hoặc bộ test tương tự để tái sử dụng. Điểm kiểm thử chi tiết, công cụ và bằng chứng cho từng module được Tech đề xuất ở bước sau; không suy ra stack kỹ thuật ở đặc tả này.

## Out of Scope

- Ngân sách, báo cáo tổng hợp, xuất dữ liệu, giao dịch định kỳ, mục tiêu tiết kiệm, chế độ ghi offline, dùng trên máy tính và màn hình gửi phản hồi trong ứng dụng. [R10–R11, R17–R20, R34, S12]
- Khôi phục từ bản sao lưu sau sự cố hệ thống nặng; Ninh đã chấp nhận rủi ro dữ liệu có thể không khôi phục trong trường hợp này. [S04, R52]
- Tự đăng ký, người quản lý dự phòng, danh sách toàn bộ tài khoản cho Ninh, khóa người dùng, hủy lời mời trước hạn, phục hồi quyền quản lý khi Ninh đã đăng xuất và mất email quản lý. [R13, R47, bước 3]
- Xóa vĩnh viễn từng giao dịch, số dư sau từng dòng lịch sử và phí chuyển khác 0. [R42, R46, B17]
- Nhập dữ liệu lịch sử hoặc lấy code iOS/API cũ làm nền; ảnh cũ chỉ là tham khảo giao diện. [R01–R03]

## Further Notes

**Chưa rõ, phải xử lý ở đặc tả chi tiết bước 9 trước khi giao Dev:**

1. Câu chữ, bố cục của luồng xác minh người mất email, thông báo tài khoản đã xóa và trạng thái lưu chưa rõ. Quy tắc hành vi đã chốt ở R62–R64.
2. Cách hiện lỗi/hết hạn liên kết và lưu trạng thái đang đối chiếu qua lần mở lại; không báo thành công sớm hoặc tạo trùng theo F09/F25, không cho lưu lặp đúng thao tác theo R64. Cách xin lại liên kết quản lý đã chốt ở R61.
3. Phiên bản thiết bị/trình duyệt cần hỗ trợ, chi tiết ghi bằng chứng thử nhóm nhỏ và thiết kế màn hình. [S05, S11]
4. Công nghệ, cách lưu dữ liệu, kiến trúc, hợp đồng API và mô hình dữ liệu chưa được chọn. Không dùng tài liệu hoặc code cũ để tự chốt các lựa chọn này.

Các điểm còn mở giữ `needs-info`; chưa gắn `ready-for-agent` trước đặc tả chi tiết bước 9. PM → Tech đã rà soát bản tổng thể; Ninh chọn **A, A** xác nhận nội dung và điểm kiểm thử chính, chuyển sang bước 8. Sau R59–R64, 35 ticket đã được xuất bản ở trạng thái `needs-triage` để qua bước 9. Các quyết định kỹ thuật và giao diện ở bước 9 chưa được chốt bởi xác nhận bản tổng thể.
