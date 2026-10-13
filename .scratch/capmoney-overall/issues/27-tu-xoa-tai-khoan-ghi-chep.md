# 27: Tự xóa tài khoản ghi chép

**What to build:** Chủ tài khoản ghi chép tự xóa vĩnh viễn tài khoản cùng toàn bộ dữ liệu sau cảnh báo, nhập lại mật khẩu và xác nhận cuối; phiên điện thoại khác mất quyền ngay, còn lời mời mới cùng email tạo tài khoản trống.

**Blocked by:** 07 – Thêm ví, 09 – Ghi khoản chi, 12 – Xóa mềm và khôi phục khoản thu/chi, 15 – Xóa và khôi phục lần chuyển, 18 – Ngừng ví 0 có lịch sử, bảo vệ số dư và quay lại ghi thu/chi, 20 – Thêm và đổi tên danh mục

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Chỉ chủ tài khoản ghi chép sau khi xem cảnh báo không khôi phục được, nhập lại mật khẩu và xác nhận lần cuối mới xóa được tài khoản; tài khoản quản lý không có luồng tự xóa này.
- [ ] Sau xác nhận cuối, tài khoản cùng mọi ví, danh mục và giao dịch còn hiệu lực hoặc đã xóa mềm bị xóa ngay, không có hoàn tác; mọi yêu cầu đọc/sửa từ điện thoại khác bị từ chối từ thời điểm xóa.
- [ ] Điện thoại khác báo tài khoản đã xóa và đưa về đăng nhập khi tải lại, quay lại ứng dụng hoặc thao tác tiếp theo; Ninh mời lại cùng email thì lập tài khoản mới với dữ liệu trống.

## Comments

- Nguồn: F05, R63, quyền xóa tài khoản ở bước 3 và cách chia bước 8. Kiểm thử qua hai điện thoại và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
