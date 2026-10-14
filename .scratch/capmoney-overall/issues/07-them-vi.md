# 07: Thêm ví

**What to build:** Người dùng tạo thêm ví của riêng mình và đọc lại danh sách, số dư từng ví.

**Blocked by:** 06 – Tạo ví đầu tiên (`HARD` cho Dev và kiểm chứng tích hợp #07; không chặn BA/Tech thiết kế)

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Người dùng thêm được nhiều ví, mỗi ví có số dư riêng đúng theo số dư ban đầu.
- [ ] Mỗi ví thêm phải có tên do người dùng nhập trước khi Lưu; không tự đặt tên nếu bỏ trống. [R66]
- [ ] Tải lại trang vẫn đọc được các ví vừa lưu; tài khoản khác không thấy hoặc sửa được chúng.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
- R66 áp dụng cả ví đầu tiên và ví thêm; xem [vòng làm rõ #06](../../../docs/requirements/39-step-9-first-wallet-round-1.md). Đặc tả riêng #07 vẫn phải qua BA → PM → Tech → PM ở bước 9.
- Áp dụng [workflow CapMM](../../../docs/workflows/capmm-end-to-end.md) từ Story #07. [Story](../../capmoney-add-wallet/story.md), [spec 20 mục](../../capmoney-add-wallet/spec.md) và [Q&A](../../capmoney-add-wallet/qna.md) đã có nguồn R66/R68/R69; PM đã xác nhận `BA_READY`. [Thiết kế kỹ thuật](../../capmoney-add-wallet/technical-design.md) được reviewer độc lập và PM rà, kết luận `STORY_TECH_GAP`: chưa chứng minh “chắc chưa lưu” an toàn trước request tới muộn, ghi một lần atomic dưới đồng thời và các hợp đồng nền tảng #04/#06. #06→#07 vẫn `HARD` cho Dev/kiểm chứng; F20/#18 là bối cảnh tích hợp phía sau, không chặn ngược #07 hoặc thành AC #07. Chưa đạt `TECH_READY`, chưa tạo ticket Dev.
- Lượt Tech tiếp theo có [đề xuất hợp đồng thao tác ví](../../capmoney-add-wallet/technical-contract-proposal.md) và [handoff](../../capmoney-add-wallet/handoff.md). Reviewer độc lập đã nêu lỗi tìm kiếm chỉ pending; bản đề xuất đã sửa để tìm cả kết quả cuối chưa được chủ xác nhận đã thấy. Chưa review lại bản sửa, chưa có spike/nền tảng đã duyệt; giữ `STORY_TECH_GAP`, `Status: needs-triage` và chưa giao Dev.
