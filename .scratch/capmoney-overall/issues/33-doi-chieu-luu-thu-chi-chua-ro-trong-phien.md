# 33: Đối chiếu lần lưu thu/chi chưa rõ trong phiên

**What to build:** Khi mất mạng sau bấm Lưu khoản thu/chi mà kết quả chưa rõ, người dùng thấy trạng thái chờ, giữ nội dung trong phiên và tự được đối chiếu khi có Internet lại mà không lưu trùng.

**Blocked by:** 08 – Danh mục mặc định và ghi khoản thu, 09 – Ghi khoản chi

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Khi mất mạng sau khi bấm Lưu khoản thu hoặc chi và chưa biết kết quả, ứng dụng báo chưa rõ, giữ nội dung khi phiên còn mở và không báo thành công sớm.
- [ ] Khi có Internet, ứng dụng tự đối chiếu trước khi cho thử lại; đúng thao tác đang chờ không thể bị bấm Lưu lặp để tạo bản trùng.
- [ ] Khi có Internet, người dùng được rời biểu mẫu làm việc khác; lúc quay lại, nếu chưa xác định kết quả thì thấy đang kiểm tra, nếu đã xác định thì thấy đã lưu/chưa lưu. Số dư đọc lại không bị tính hai lần.

## Comments

- Nguồn: F09, R64 và cách chia bước 8. Kiểm thử qua web, mất/kết nối lại Internet và đọc lại số dư; chưa chọn schema/API/công nghệ.
