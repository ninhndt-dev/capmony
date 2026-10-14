# 09: Ghi khoản chi

**What to build:** Ghi khoản chi hợp lệ, kể cả khi số dư thành âm; số dư đọc lại theo B01.

**Blocked by:** 08 – Danh mục mặc định và ghi khoản thu

**Status:** needs-triage

**Bước:** 8/11 – Cách chia đã được Ninh duyệt; đặc tả chi tiết và rà soát module ở bước 9 trước khi Dev nhận việc.

## Acceptance criteria

- [ ] Ghi khoản chi hợp lệ bằng chính danh sách danh mục dùng chung cho thu/chi của ticket 08, ngày và số nguyên VND dương; ghi chú có thể để trống.
- [ ] Số dư ví đọc lại giảm đúng, được phép âm; ngày tương lai và số tiền không dương bị từ chối.

## Comments

- Nguồn: đặc tả tổng thể bước 7 và đề xuất bước 8 đã được Ninh xác nhận. Kiểm thử hành vi qua web và đọc lại dữ liệu; chưa chọn schema/API/công nghệ.
- Bước 9: Ninh chọn Q1=A/Q2=A riêng khoản chi, ghi thành [R72–R73](../../../docs/requirements/43-step-9-expense-round-1.md). [Story #09](../../capmoney-expense/story.md), [Q&A](../../capmoney-expense/qna.md) và [spec BA 20 mục](../../capmoney-expense/spec.md) đã được CapmmPM rà sau trả lời; `BA_READY` đạt ngày 14/10/2026. R70–R71 chỉ áp dụng cho khoản thu #08; #10 và #33/#35 là lát riêng. Chưa có ticket Dev; giữ `needs-triage`.
- [Technical design #09](../../capmoney-expense/technical-design.md) đã được reviewer độc lập `/root/tech_independent` và CapmmPM rà ngày 14/10/2026: `STORY_TECH_GAP`, chưa `TECH_READY`. Cần chứng minh ghi chi + kết quả thao tác + số dư B01 atomic/idempotent dưới retry và thu+chi đồng thời; #08 là nền `HARD` còn gap. Cần chốt quyền/trạng thái tại commit, operation ID/#33/#35, VND/ngày và fault injection. Chưa chia ticket Dev; giữ `needs-triage`.
- Lượt Tech tiếp theo có [đề xuất hợp đồng sổ chung #08–#09](../../capmoney-income/shared-ledger-proposal.md) cho ghi chi và số dư dưới đồng thời. Reviewer độc lập đã rà bản đầu ở mức tài liệu; phần bổ sung về phản hồi muộn và tìm kết quả cuối chưa được review lại. Chưa chọn nền tảng/chưa có spike; giữ `STORY_TECH_GAP` và chưa giao Dev.
