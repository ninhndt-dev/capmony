# CapMoney

Các thuật ngữ nghiệp vụ đã rõ trong quá trình tiếp nhận yêu cầu.

## Ngôn ngữ

**Ví**:
Một đơn vị theo dõi số dư riêng thuộc dữ liệu tài chính của một người dùng.

**Số dư ban đầu**:
Mốc số tiền khởi đầu của một ví, không phải khoản thu trong lịch sử giao dịch.

**Số dư hiện tại**:
Số dư ban đầu của ví cộng các khoản thu và tiền chuyển đến còn hiệu lực, trừ các khoản chi và tiền chuyển đi còn hiệu lực, không phụ thuộc ngày ghi giao dịch.

**Danh mục**:
Nhãn gắn với khoản thu hoặc chi; trong dữ liệu của mỗi người, cùng một danh sách danh mục dùng cho cả hai loại.

**Chuyển tiền**:
Một thao tác liên kết hai ví của cùng một người, trừ tiền ở ví gửi và cộng tiền ở ví nhận.

**Ví đã ngừng sử dụng**:
Ví luôn có số dư bằng 0 khi đang ngừng, giữ lịch sử cũ nhưng không còn được chọn cho giao dịch hoặc chuyển tiền mới. Người dùng có thể bật dùng lại ví này trước thao tác có thể làm số dư khác 0.

**Khoản thu/chi đã xóa**:
Khoản thu hoặc chi được tách khỏi lịch sử thông thường và không còn ảnh hưởng đến số dư, nhưng vẫn có thể khôi phục.

**Tài khoản quản lý lời mời**:
Tài khoản riêng của Ninh để cấp lời mời và hỗ trợ người mất email lấy lại tài khoản; không phải tài khoản Ninh dùng để ghi chép tài chính cá nhân.

**Tài khoản ghi chép cá nhân**:
Tài khoản mà một người dùng để ghi chép dữ liệu tài chính của chính mình. Với Ninh, tài khoản này tách khỏi tài khoản quản lý lời mời.

**Xóa tài khoản**:
Việc người dùng chủ động kết thúc tài khoản và xóa vĩnh viễn toàn bộ dữ liệu của tài khoản đó. Khác với xóa một giao dịch, vốn còn có thể khôi phục.
