# 02: Xin lại liên kết thiết lập quản lý hết hạn

**What to build:** Khi thiết lập quản lý ban đầu chưa hoàn tất và liên kết đã hết hạn, Ninh tự xin liên kết mới chỉ tới email quản lý đã khai; liên kết cũ hết hiệu lực. Sau khi thiết lập xong, không thể xin thêm liên kết thiết lập.

**Blocked by:** 01 – Thiết lập và đăng nhập quản lý

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Khi thiết lập quản lý ban đầu chưa hoàn tất, liên kết không thể hoàn tất thiết lập từ đúng mốc 72 giờ kể từ lúc hệ thống ghi nhận gửi thành công.
- [ ] Ninh tự xin liên kết mới chỉ tới email quản lý đã khai; liên kết cũ hết hiệu lực.
- [ ] Trang xin lại không cho thay email đích; sau khi đã thiết lập tài khoản quản lý, trang không cấp thêm liên kết hoặc tài khoản quản lý.
- [ ] Ca kiểm chứng dùng môi trường khởi tạo riêng, không đặt lại dữ liệu người dùng thật.
- [ ] Hai yêu cầu xin lại gần đồng thời hoặc email đến lệch thứ tự chỉ để một liên kết hiện hành dùng được; lỗi gửi mail không báo thành công sai và cho thử lại an toàn.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
- Đặc tả chi tiết bước 9: [Xin lại liên kết thiết lập quản lý hết hạn](../../capmoney-manager-link-renewal/spec.md). Giữ `needs-triage` tới khi Tech kiểm chứng phương án xác thực/email cùng #01 và PM rà lại.
- Rà soát BA → PM → Tech → PM ngày 14/10/2026: hành vi khớp F23–F24; tiêu chí mốc 72 giờ được sửa cho khớp đặc tả. Cơ chế thay liên kết, kết quả gửi email chưa rõ và giới hạn xin lại còn chờ kiểm chứng kỹ thuật, nên chưa chuyển `ready-for-agent`.
- Bản [thiết kế Java nháp 16/10/2026](../../capmoney-manager-link-renewal/technical-design-java.md) ghi hợp đồng đề xuất dựa trên code CM-01 và các điểm BA/PM/Tech cần rà; chưa đạt `BA_READY`, `TECH_READY` hoặc `READY_FOR_IMPLEMENTATION`.
- Theo yêu cầu Ninh triển khai tiếp, code CM-02 đã được viết và [tự kiểm](../../capmoney-manager-link-renewal/implementation-evidence.md). Vẫn cần review/gate và nghiệm thu độc lập; không suy `Closed` từ build hoặc tự kiểm.
