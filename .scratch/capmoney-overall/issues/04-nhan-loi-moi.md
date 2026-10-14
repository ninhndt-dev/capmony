# 04: Nhận lời mời và đăng nhập cá nhân

**What to build:** Người nhận dùng lời mời còn hạn, xác minh mã đúng email, đặt mật khẩu và đăng nhập; tài khoản mới chỉ thấy dữ liệu trống của mình và không có quyền quản lý.

**Blocked by:** 03 – Gửi lời mời tài khoản ghi chép

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Người được mời xác minh mã gửi đúng email được mời, đặt mật khẩu và đăng nhập.
- [ ] Liên kết mời và mã phải còn hợp lệ tại lúc hoàn tất; không đổi email đích hoặc dùng liên kết chuyển tiếp để lập tài khoản nếu chưa chứng minh quyền dùng đúng email được mời.
- [ ] Hoàn tất lặp hoặc gần đồng thời chỉ tạo tối đa một tài khoản ghi chép; liên kết đã hoàn tất hoặc email đang có tài khoản không tạo tài khoản thứ hai.
- [ ] Sau đăng xuất và đăng nhập lại bằng email, mật khẩu, tài khoản mới chỉ thấy dữ liệu tài chính khởi đầu trống của mình; không thấy dữ liệu tài khoản khác.
- [ ] Tài khoản ghi chép không truy cập khu vực quản lý hoặc gửi lời mời qua đường xử lý trực tiếp.
- [ ] Mã của lời mời khác hoặc mã dùng cho mục đích khác, và đường hoàn tất trực tiếp không có lời mời hợp lệ, không lập được tài khoản hoặc cấp phiên ghi chép.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
- Đặc tả bước 9: [nhận lời mời và đăng nhập cá nhân](../../capmoney-invitation-accept/spec.md); [đối chiếu kỹ thuật](../../capmoney-invitation-accept/technical-options.md). BA → PM → Tech → PM đã rà ngày 14/10/2026. Cơ chế mã và xác thực vẫn cần chứng minh trước khi giao Dev. Mười danh mục mặc định thuộc ticket #08; không coi dữ liệu tài chính trống là không có danh mục.
