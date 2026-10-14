# 05: Xử lý mã sai hoặc lời mời hết hạn

**What to build:** Mã sai/hết hạn có thể xin lại khi lời mời còn hạn, mã cũ hết hiệu lực; lời mời quá 3 ngày, kể cả lúc nhập mã, phải để Ninh mời lại.

**Blocked by:** 03 – Gửi lời mời tài khoản ghi chép, 04 – Nhận lời mời và đăng nhập cá nhân

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Khi lời mời còn hạn, mã sai hoặc hết hạn có thể xin mã mới tới cùng email; mã cũ hết hiệu lực và hạn lời mời không đổi.
- [ ] Từ đúng mốc 72 giờ kể từ lần gửi đầu được nhà cung cấp tiếp nhận, kể cả trong lúc nhập mã, không thể hoàn tất; người nhận được hướng dẫn liên hệ Ninh.
- [ ] Ninh gửi được lời mời mới tới cùng email sau khi lời mời cũ hết hạn.
- [ ] Mã sai bị từ chối; một lần nhập sai không tự làm mất hiệu lực mã còn hạn theo giới hạn Tech chọn. Sau khi xin mã mới, chỉ mã mới đúng email/lời mời được dùng.
- [ ] Không thể hoàn tất bằng liên kết/mã cũ tại và sau mốc 72 giờ, kể cả đã mở liên kết hoặc kiểm mã đúng trước mốc.
- [ ] Lời mời mới sau hạn không làm liên kết/mã cũ dùng lại được; người không có quyền quản lý không thể tự mời mới.
- [ ] Xin mã mới lặp/gần đồng thời hoặc trong lúc hoàn tất không tạo hai mã cùng hợp lệ hay hai tài khoản; lỗi gửi/timeout không được báo gửi thành công khi kết quả chưa rõ. Khi lời mời còn hạn, người nhận vẫn thử xin mã mới an toàn được sau lỗi gửi, không bị khóa đến hết hạn vì mã cũ đã vô hiệu.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
- Đặc tả bước 9: [mã sai và lời mời hết hạn](../../capmoney-invitation-recovery/spec.md); [đối chiếu kỹ thuật](../../capmoney-invitation-recovery/technical-options.md). BA → PM → Tech → PM đã rà ngày 14/10/2026; PM xác nhận AC/kiểm thử phục hồi sau lỗi gửi mã. Còn spike mã, mốc hạn, gửi lỗi và đồng thời trước khi giao Dev. Gửi lại cùng liên kết còn hạn theo R65 thuộc #03.
