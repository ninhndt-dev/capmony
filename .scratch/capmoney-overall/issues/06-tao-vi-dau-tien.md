# 06: Tạo ví đầu tiên

**What to build:** Người chưa có ví được dẫn tạo ví trước khi ghi thu/chi; phải nhập tên ví, bỏ trống số dư đầu thì dùng 0 VND. Rời trang rồi quay lại cùng phiên giữ bản nhập dở; đóng trình duyệt trước khi bấm Lưu thì mở lại biểu mẫu trống.

**Blocked by:** 04 – Nhận lời mời và đăng nhập cá nhân

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Khi chưa có ví, luồng ghi chép dẫn người dùng tạo ví đầu tiên; bỏ trống số dư ban đầu dùng 0 VND.
- [ ] Tên ví bắt buộc trước khi Lưu, áp dụng cả ví thêm ở #07; tên trống không tạo ví.
- [ ] Số dư ban đầu nhập vào là số nguyên VND, có thể âm; số lẻ hoặc không phải số nguyên bị từ chối. Sau Lưu, số dư hiện tại của ví mới đúng bằng số dư đầu và việc tạo ví không tạo bản ghi khoản thu.
- [ ] Chỉ chủ tài khoản tạo/đọc được ví của mình; tải lại trang vẫn thấy ví, tài khoản khác hoặc tài khoản quản lý không thể tạo ví thay hoặc đọc ví ấy. Quyền sửa ví kiểm ở #16.
- [ ] Rời trang rồi quay lại cùng phiên giữ bản nhập dở; đóng trình duyệt trước khi bấm Lưu thì mở lại biểu mẫu trống.
- [ ] Ví chỉ được tạo khi Lưu thành công; bấm lặp hoặc thử lại cùng thao tác Lưu chỉ tạo một ví, còn thao tác tạo ví độc lập vẫn được phép.
- [ ] Đã bấm Lưu ví đầu tiên rồi đóng trình duyệt khi kết quả chưa rõ: mở lại cùng tài khoản có Internet tự đối chiếu đúng thao tác; chỉ báo đã/chưa lưu khi chắc, nếu chắc chưa lưu thì biểu mẫu trống, nếu chưa rõ thì hiện đang kiểm tra và chặn lưu trùng. [R67]

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
- Đặc tả bước 9: [tạo ví đầu tiên](../../capmoney-first-wallet/spec.md), [đối chiếu kỹ thuật](../../capmoney-first-wallet/technical-options.md). Ninh đã xác nhận [R66 về tên ví](../../../docs/requirements/39-step-9-first-wallet-round-1.md) và [R67 về kết quả Lưu ví sau đóng trình duyệt](../../../docs/requirements/40-step-9-first-wallet-round-2.md). BA → PM → Tech → PM đã rà ngày 14/10/2026; PM xác nhận phạm vi #06 chỉ tạo/đọc ví. Còn spike #04/tạo một lần/tự đối chiếu trước khi giao Dev.
