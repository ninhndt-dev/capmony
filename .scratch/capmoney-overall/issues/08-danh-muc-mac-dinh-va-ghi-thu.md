# 08: Danh mục mặc định và ghi khoản thu

**What to build:** Người dùng có mười danh mục mặc định; ghi khoản thu từ nút chung hoặc từ ví, được chọn sẵn ví khi vào từ ví và đổi sang ví khác được; đọc lại số dư tăng đúng.

**Blocked by:** 06 – Tạo ví đầu tiên, 07 – Thêm ví

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Mỗi tài khoản mới có đúng mười danh mục mặc định đã chốt, dùng được cho khoản thu.
- [ ] Ghi khoản thu từ nút chung hoặc từ ví; vào từ ví được chọn sẵn nhưng đổi sang ví khác được.
- [ ] Khoản thu dùng số nguyên VND dương, ngày hôm nay hoặc trước đó, danh mục bắt buộc, ghi chú tùy chọn; số dư đọc lại tăng đúng.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
- [Story #08](../../capmoney-income/story.md), [spec 20 mục](../../capmoney-income/spec.md) và [Q&A](../../capmoney-income/qna.md) đã ghi R70–R71: khoản thu từ nút chung với ít nhất hai ví chưa chọn ví; rời form thu trước Lưu rồi quay lại cùng phiên thì form trống. CapmmPM đã xác nhận `BA_READY` ngày 14/10/2026. R70–R71 không tự áp cho khoản chi #09; màn lịch sử #10 và đối chiếu Lưu chưa rõ #33/#35 là lát riêng. Chưa có ticket Dev; giữ `needs-triage`.
- [Technical design #08](../../capmoney-income/technical-design.md) đã được reviewer độc lập `/root/tech_independent` và CapmmPM rà ngày 14/10/2026: `STORY_TECH_GAP`, chưa `TECH_READY`. Cần chứng minh khởi tạo danh mục với #04, ghi thu + kết quả thao tác + số dư nhất quán dưới đồng thời, hợp đồng #33/#35, VND/ngày/quyền tại commit và fault injection. #04/#06/#07 chặn tích hợp/kiểm chứng; chưa chia ticket Dev, giữ `needs-triage`.
