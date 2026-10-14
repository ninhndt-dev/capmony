# Bước 8: đề xuất chia ticket dọc

**Trạng thái:** Cách chia 35 lát đã được Ninh duyệt bằng A cho Q1 và A cho Q2. Sau R59–R64, cả 35 ticket đã được xuất bản dưới `.scratch/capmoney-overall/issues/` với trạng thái `needs-triage` để qua đặc tả chi tiết bước 9. PM → Tech đã rà soát các ticket; BA cũng rà soát R62–R64 và 5 ticket mới. Các cạnh 16→10, 23→04, 24–26→08, 28→10 và 33→09 được sửa khi kiểm tra khả năng demo; #15 thêm #14 để kiểm tra quy tắc sửa sau khôi phục.

**Nguồn:** [Đặc tả tổng thể đã xác nhận](../../.scratch/capmoney-overall/spec.md), [danh mục chức năng và ưu tiên](system-functions.md), [quy trình](../agents/workflow.md).

Mỗi dòng dưới đây là một hành vi người dùng có thể trình diễn từ web đến dữ liệu đọc lại, kèm kiểm tra quyền và số dư liên quan. Mỗi ticket sẽ được giữ đủ nhỏ cho một vòng BA → PM → Tech → PM → Dev → PM → QA. Cột **Chặn bởi** chỉ nêu chức năng cần có trước về mặt hành vi, không dùng để ép thứ tự triển khai khi hai mục độc lập. Lịch demo vẫn theo P01–P03; kiểm tra hai điện thoại theo P02.

| # | Tên đề xuất | Chặn bởi | Kết quả có thể trình diễn |
| --- | --- | --- | --- |
| 01 | Thiết lập và đăng nhập quản lý | Không | Ninh dùng liên kết thiết lập một lần còn hạn gửi đến email đã khai, xác minh email, đặt mật khẩu và đăng nhập tài khoản quản lý riêng. |
| 02 | Xin lại liên kết thiết lập quản lý hết hạn | 01 | Khi thiết lập quản lý ban đầu chưa hoàn tất, Ninh tự xin liên kết mới chỉ tới email quản lý đã khai; liên kết cũ hết hiệu lực. Sau khi hoàn tất, không cấp thêm liên kết hay tài khoản quản lý. Dùng môi trường khởi tạo riêng để thử, không đặt lại dữ liệu người dùng thật. |
| 03 | Gửi lời mời tài khoản ghi chép | 01 | Ninh đăng nhập quản lý và gửi liên kết mời qua email; người khác không thể tự đăng ký hoặc gửi lời mời. Bổ sung bước 9 theo R65: Ninh gửi lại cùng liên kết chưa dùng, còn hạn tới cùng email, giữ hạn gốc khi người nhận không thấy thư. |
| 04 | Nhận lời mời và đăng nhập cá nhân | 03 | Người nhận dùng lời mời còn hạn, xác minh mã đúng email, đặt mật khẩu và đăng nhập; tài khoản mới chỉ thấy dữ liệu trống của mình và không có quyền quản lý. |
| 05 | Xử lý mã sai hoặc lời mời hết hạn | 03, 04 | Mã sai/hết hạn có thể xin lại khi lời mời còn hạn, mã cũ hết hiệu lực; lời mời quá 3 ngày, kể cả lúc nhập mã, phải để Ninh mời lại. |
| 06 | Tạo ví đầu tiên | 04 | Người chưa có ví được dẫn tạo ví; bỏ trống số dư đầu thì dùng 0 VND. Rời trang rồi quay lại cùng phiên giữ bản nhập dở; đóng trình duyệt trước khi bấm Lưu thì mở lại biểu mẫu trống. |
| 07 | Thêm ví | 06 | Người dùng tạo thêm ví của riêng mình và đọc lại danh sách, số dư từng ví. |
| 08 | Danh mục mặc định và ghi khoản thu | 06, 07 | Người dùng có mười danh mục mặc định; ghi khoản thu từ nút chung hoặc từ ví, được chọn sẵn ví khi vào từ ví và đổi sang ví khác được; đọc lại số dư tăng đúng. |
| 09 | Ghi khoản chi | 08 | Ghi khoản chi hợp lệ, kể cả khi số dư thành âm; số dư đọc lại theo B01. |
| 10 | Lịch sử chung và lọc theo ví | 08, 09 | Xem thu/chi theo ngày mới nhất, lọc ví, xem số tiền và số dư hiện tại. |
| 11 | Sửa khoản thu/chi | 10 | Sửa các trường đã chốt và đọc lại số dư các ví bị ảnh hưởng; chặn ngày tương lai. |
| 12 | Xóa mềm và khôi phục khoản thu/chi | 10, 11 | Xác nhận xóa, xem mục đã xóa, khôi phục bất cứ lúc nào; số dư được tính lại, giao dịch đã xóa phải khôi phục trước khi sửa. |
| 13 | Chuyển tiền giữa hai ví | 07, 10 | Chuyển phí 0 giữa hai ví khác nhau, cho số dư âm; lịch sử chung có một dòng và mỗi ví đổi số dư đúng một lần. |
| 14 | Sửa lần chuyển | 13 | Sửa ngày, số tiền, hai ví hoặc ghi chú của một lần chuyển như một đơn vị; số dư hai ví đọc lại đúng. |
| 15 | Xóa và khôi phục lần chuyển | 13, 14 | Xóa hoặc khôi phục cả lần chuyển, tác động hai ví như một đơn vị và giữ một dòng lịch sử khi còn hiệu lực. |
| 16 | Sửa thông tin ví | 10 | Đổi tên, sửa số dư ban đầu kể cả số âm; lịch sử giữ đúng ví và số dư tính lại. |
| 17 | Ngừng và bật lại ví trống | 08, 11, 14, 16 | Ví chưa có giao dịch, số dư 0 được ngừng và bật lại; ví ngừng không nhận giao dịch mới hoặc nhận giao dịch cũ khi sửa để đổi ví. Sửa số dư đầu làm ví ngừng khác 0 cũng bị chặn; người dùng phải bật lại trước. |
| 18 | Ngừng ví 0 có lịch sử, bảo vệ số dư và quay lại ghi thu/chi | 10, 11, 12, 14, 15, 17 | Ví đã có lịch sử nhưng số dư 0 được ngừng và vẫn xem lịch sử. Sửa/xóa/khôi phục thu/chi hoặc lần chuyển mà làm ví ngừng khác 0 bị chặn, yêu cầu bật lại trước; các thao tác không đổi số dư vẫn được phép. Khi mọi ví đã ngừng, luồng thu/chi dẫn tạo/bật lại ví rồi quay về biểu mẫu. |
| 19 | Xóa ví đủ điều kiện | 06, 12, 15 | Chỉ ví 0 đồng chưa từng có giao dịch, kể cả khoản thu/chi hoặc lần chuyển đã xóa mềm, mới xóa hẳn; ví khác được giữ. |
| 20 | Thêm và đổi tên danh mục | 08, 10 | Người dùng thêm danh mục của mình; đổi tên thì giao dịch cũ hiện tên mới và vẫn chọn được. |
| 21 | Xóa danh mục chưa dùng | 08, 20 | Danh mục chưa từng dùng, kể cả danh mục mặc định, được xóa; danh mục đã dùng không xóa được. |
| 22 | Đặt lại mật khẩu quản lý | 01 | Ninh còn dùng email quản lý tự đặt lại qua liên kết riêng 30 phút; mọi phiên quản lý cũ kết thúc. |
| 23 | Đổi email quản lý | 01, 04 | Ninh đang đăng nhập đổi email quản lý; email mới được xác minh trước khi có hiệu lực và các phiên cũ kết thúc. Có tài khoản ghi chép để kiểm tra quyền vẫn tách biệt. |
| 24 | Đặt lại mật khẩu ghi chép | 08 | Chủ tài khoản còn email tự đặt lại qua liên kết 30 phút; mọi phiên cũ kết thúc và dữ liệu tài chính đã ghi giữ nguyên. |
| 25 | Đổi email ghi chép | 08 | Chủ tài khoản đang đăng nhập đổi email sau xác minh email mới; phiên cũ kết thúc, dữ liệu tài chính đã ghi giữ nguyên. |
| 26 | Hỗ trợ người mất email | 01, 04, 08 | Theo yêu cầu, Ninh tra đúng email cũ và chỉ đổi sang email mới khi xác minh đủ; không thấy dữ liệu tài chính. |
| 27 | Tự xóa tài khoản ghi chép | 07, 09, 12, 15, 18, 20 | Sau cảnh báo, nhập lại mật khẩu và xác nhận, tài khoản cùng mọi ví, danh mục và giao dịch còn/đã xóa bị xóa ngay; phiên trên điện thoại khác mất quyền đọc/sửa, mời lại cùng email tạo tài khoản trống. |
| 28 | Tải dữ liệu trên điện thoại thứ hai | 10 | Cùng tài khoản trên hai điện thoại: quay lại khi có Internet thì tự thấy dữ liệu mới trong lịch sử, vẫn giữ phần giao dịch đang nhập dở trong phiên. |
| 29 | Giữ các giao dịch mới lưu gần đồng thời | 08, 13 | Hai điện thoại ghi hai giao dịch khác nhau, gồm thu/chi và lần chuyển, gần đồng thời; cả hai khoản đã báo lưu đều được giữ và số dư đúng. |
| 30 | Giữ hai giao dịch khác nhau được sửa gần đồng thời | 11, 14 | Hai điện thoại sửa hai giao dịch khác nhau, gồm thu/chi và lần chuyển, gần đồng thời; cả hai thay đổi đã báo lưu đều được giữ và số dư đúng. |
| 31 | Chặn sửa/xóa/khôi phục khoản thu/chi từ bản cũ | 11, 12 | Trên hai điện thoại, thao tác từ bản cũ của cùng khoản thu/chi bị chặn, giữ phần vừa nhập để tự đối chiếu, không ghi đè bản mới. |
| 32 | Chặn sửa/xóa/khôi phục lần chuyển từ bản cũ | 14, 15 | Áp dụng quy tắc bản cũ cho lần chuyển như một đơn vị; dữ liệu mới và số dư hai ví được giữ. |
| 33 | Đối chiếu lần lưu thu/chi chưa rõ trong phiên | 08, 09 | Mất mạng sau khi bấm Lưu khoản thu/chi: giữ nội dung trong phiên, tự đối chiếu khi có mạng lại, không báo thành công sớm hoặc tạo trùng. |
| 34 | Đối chiếu lần lưu chuyển tiền chưa rõ trong phiên | 13 | Khi mất mạng sau bấm Lưu lần chuyển, chỉ báo thành công sau đối chiếu; không ghi trùng ở cả hai ví. |
| 35 | Đối chiếu lần lưu giao dịch chưa rõ sau khi mở lại | 33, 34 | Đóng trình duyệt sau khi bấm Lưu giao dịch mà kết quả chưa rõ: mở lại tự đối chiếu; chắc chắn chưa lưu thì biểu mẫu trống, vẫn chưa rõ thì tiếp tục báo chưa rõ và chặn thử lại cùng thao tác. |

**Cổng kiểm chứng sau các ticket:** Ninh và 2–3 người khác thử toàn bộ H01–H12 trên iPhone và Android, với ít nhất hai tài khoản riêng, ghi kết quả từng chức năng và sửa hết lỗi phát hiện trước khi Ninh mở đến khoảng 10 người. Đây là điều kiện phát hành S02–S13, chưa phải một ticket triển khai mới.

## Mức ưu tiên đã xác nhận và điều còn mở

- Bản demo đi từ tài khoản/ví đến thu/chi, số dư, lịch sử; sau đó sửa/xóa/khôi phục **thu/chi**, rồi chuyển tiền và các thao tác tương ứng với **lần chuyển**. Các ticket 16–27 vẫn thuộc bản đầu; vị trí triển khai của chúng có thể song song khi không có phụ thuộc thật. [P01, P03]
- Chỉ xác nhận các luồng hai điện thoại sau các luồng một điện thoại và trước nhóm thử toàn bộ chức năng. Ticket 28–35 có thể chuẩn bị kỹ thuật sớm, nhưng thời điểm xác nhận H10 theo P02. Mỗi ticket trước đó vẫn phải giữ dữ liệu riêng và không báo lưu thành công sai. [P02, H10]
- R59–R61 đã chốt cách xin lại liên kết quản lý (22), thứ tự cùng ngày (10) và lịch sử lần chuyển khi lọc ví (13). R62–R64 đã chốt cách xác minh người mất email (26), báo phiên sau xóa (27), và điều hướng khi lưu chờ (33–35). Cả 35 ticket đã được xuất bản; chưa gắn `ready-for-agent` trước đặc tả chi tiết và rà soát module ở bước 9.

## Ninh cần duyệt trước khi xuất bản ticket

❓ **Q1 – Kích thước 35 lát:** Mỗi mục như trên có vừa một lượt triển khai và demo không? Ticket 18 về ví có lịch sử là lát lớn nhất.

➡️ **A (Bờm đề xuất):** Giữ cách chia này; ticket 18 là một hành vi trọn vẹn cần giữ quy tắc số dư 0. **B:** Còn quá lớn, hãy tách thêm mục Ninh chỉ ra. **C:** Quá nhỏ, hãy gộp các mục Ninh chỉ ra.

---

❓ **Q2 – Quan hệ phụ thuộc và thứ tự demo:** Cột **Chặn bởi** và thứ tự demo theo P01–P03 có hợp lý không?

➡️ **A (Bờm đề xuất):** Đúng, duyệt cách chia; chỉ xuất bản ticket đã đủ rõ, còn chi tiết `needs-info` thì làm rõ trước ticket liên quan. **B:** Cần đổi; Ninh ghi mã ticket hoặc thứ tự muốn sửa.

## Nhật ký trả lời

Ninh trả lời **A** cho Q1 và tiếp tục trả lời **A** cho Q2. Ninh xác nhận giữ cách chia 35 lát, gồm ticket 18; cột **Chặn bởi** và thứ tự demo đúng ý. Các chi tiết `needs-info` không được chốt thêm bởi hai câu trả lời này.

Sau khi Ninh duyệt cách chia, Bờm đã xuất bản **một file cho mỗi ticket đã đủ rõ** theo [tracker Markdown](../agents/issue-tracker.md). Các mục từng thiếu quyết định được làm rõ ở R59–R64 trước khi xuất bản; không tự chuyển thành `ready-for-agent` trước bước 9.
