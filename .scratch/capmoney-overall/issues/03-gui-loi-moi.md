# 03: Gửi lời mời tài khoản ghi chép

**What to build:** Ninh đăng nhập quản lý và gửi liên kết mời qua email; người khác không thể tự đăng ký hoặc gửi lời mời.

**Blocked by:** 01 – Thiết lập và đăng nhập quản lý

**Status:** needs-info

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Chỉ tài khoản quản lý của Ninh gửi được lời mời qua email người nhận.
- [ ] Người chưa được mời không thể tự lập tài khoản ghi chép.
- [ ] Email được mời nhận liên kết có hạn 3 ngày.
- [ ] Người chưa đăng nhập hoặc dùng tài khoản ghi chép, kể cả tài khoản cá nhân của Ninh, không gửi được lời mời qua đường xử lý trực tiếp.
- [ ] Không phát hành lời mời có thể dùng cho email đã gắn với tài khoản ghi chép đang hoạt động; việc chặn hoàn tất tạo tài khoản thứ hai thuộc #04.
- [ ] Lỗi hoặc kết quả gửi email chưa rõ không được báo gửi thành công; yêu cầu lặp/gần đồng thời không tạo nhiều lời mời có thể dùng.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
- Đặc tả bước 9: [gửi lời mời tài khoản ghi chép](../../capmoney-invitation-send/spec.md); [đối chiếu kỹ thuật](../../capmoney-invitation-send/technical-options.md). BA → PM → Tech → PM đã rà phần độc lập ngày 14/10/2026. `needs-info` vì đang chờ Ninh chọn cách gửi lại lời mời chưa dùng, còn hạn; không suy từ F07 hoặc F03. Sau câu trả lời vẫn cần Tech chứng minh xác thực/gửi email trước khi giao Dev.
