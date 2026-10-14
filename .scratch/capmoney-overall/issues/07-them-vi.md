# 07: Thêm ví

**What to build:** Người dùng tạo thêm ví của riêng mình và đọc lại danh sách, số dư từng ví.

**Blocked by:** 06 – Tạo ví đầu tiên

**Status:** needs-info

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Người dùng thêm được nhiều ví, mỗi ví có số dư riêng đúng theo số dư ban đầu.
- [ ] Mỗi ví thêm phải có tên do người dùng nhập trước khi Lưu; không tự đặt tên nếu bỏ trống. [R66]
- [ ] Tải lại trang vẫn đọc được các ví vừa lưu; tài khoản khác không thấy hoặc sửa được chúng.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
- R66 áp dụng cả ví đầu tiên và ví thêm; xem [vòng làm rõ #06](../../../docs/requirements/39-step-9-first-wallet-round-1.md). Đặc tả riêng #07 vẫn phải qua BA → PM → Tech → PM ở bước 9.
- Áp dụng [workflow CapMM](../../../docs/workflows/capmm-end-to-end.md) từ Story #07. [Story](../../capmoney-add-wallet/story.md), [spec 20 mục](../../capmoney-add-wallet/spec.md) và [Q&A](../../capmoney-add-wallet/qna.md) đang ở `needs-info`: Ninh chưa chọn Q1 (rời biểu mẫu ví thêm trước Lưu) và Q2 (đã bấm Lưu ví thêm rồi đóng trình duyệt khi kết quả chưa rõ). PM đã rà phần BA độc lập; chưa `BA_READY`, chưa thiết kế Tech hoặc ticket Dev.
