# Handoff: Story 09 – Ghi khoản chi

Các mục tác nghiệp hiện hành cho Story #09; mỗi actor sửa mục của mình, Git giữ lịch sử.

## CapmmBA — Story #09

**DONE:** Đối chiếu [issue #09](../capmoney-overall/issues/09-ghi-chi.md), [intake](../../docs/requirements/00-intake.md), [nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md), [chức năng](../../docs/requirements/system-functions.md), [#08](../capmoney-income/story.md) và [nhật ký R70–R71](../../docs/requirements/42-step-9-income-round-1.md). Soạn [story](story.md), [spec 20 mục](spec.md) và [Q&A](qna.md); BR có ví dụ số dư âm, AC kiểm số dư đọc lại, quyền và ranh giới #10/#33/#35. Theo PM review bản nháp, rút Q1-B thành chọn sẵn ví đang sử dụng được tạo sớm nhất, vẫn đổi được. Ghi Ninh chọn **Q1=A, Q2=A** vào [nhật ký #09](../../docs/requirements/43-step-9-expense-round-1.md), R72–R73 ở [intake](../../docs/requirements/00-intake.md), rồi cập nhật BR/AC cho khoản chi.

**RESULT:** CapmmBA đã **đề xuất `BA_READY`** cho Story #09 sau R72–R73; CapmmPM xác nhận gate ở mục bên dưới. Chưa `TECH_READY`, `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED` hoặc `QA_PASS`.

**NEED HUMAN:** None cho Q1–Q2; CapmmPM đã review độc lập bản [spec](spec.md) sau R72–R73.

**PENDING:** CapmmTech xử lý các gap trong [technical design](technical-design.md), giữ ranh giới #10/#33/#35. #08 còn `STORY_TECH_GAP`; chưa chia ticket Dev. Nếu giới hạn kỹ thuật đổi hành vi R72/R73, BA/PM phải hỏi Ninh.

**DETAIL:** [Spec](spec.md) mục 1–20; [Q&A](qna.md) ghi nguồn R72–R73; [story](story.md) ghi gate và phụ thuộc. [Thiết kế kỹ thuật](technical-design.md) đã được review ở mức tài liệu; chưa có code, build hoặc test thực chạy.

— CapmmBA

## CapmmPM — Story #09

**DONE:** Rà bản sau trả lời của [spec 20 mục](spec.md), [Q&A](qna.md), [nhật ký R72–R73](../../docs/requirements/43-step-9-expense-round-1.md), [intake](../../docs/requirements/00-intake.md), [luồng](../../docs/requirements/user-flows.md), [issue #09](../capmoney-overall/issues/09-ghi-chi.md) và [workflow](../../docs/workflows/capmm-end-to-end.md). Kiểm BR-09-01..07 đều có ví dụ; AC2 yêu cầu từ nút chung có ≥2 ví thì chưa chọn ví, AC7 yêu cầu rời form trước Lưu rồi quay lại thấy trống. Kiểm quyền và số dư âm, ranh giới #08/#10/#33/#35, không áp R72–R73 sang chuyển tiền hoặc tạo ví. Sau đó rà [technical design #09](technical-design.md) theo BA spec và kết luận reviewer kỹ thuật độc lập `/root/tech_independent` ngày 14/10/2026; thiết kế giữ chung danh mục, cho số dư âm, tách R73 với F08 và không gộp lát khác vào #09.

**RESULT:** `BA_READY` **đạt ngày 14/10/2026** cho Story #09. Ninh chọn Q1=A → R72 và Q2=A → R73 riêng cho **khoản chi**; phương án B và chi tiết UX/Tech không thành quyết định Ninh. PM đồng ý kết luận reviewer độc lập: **`STORY_TECH_GAP`, chưa `TECH_READY`**. P0: chưa chứng minh ghi chi, kết quả thao tác và phép trừ số dư B01 atomic/idempotent khi retry và thu+chi đồng thời; nền #08 `HARD` còn gap. P1: còn thiếu hợp đồng kiểm chủ/ví/danh mục/trạng thái hiện hành tại commit, operation ID và phục hồi #33/#35, miền và tuần tự hóa VND, ngày lịch/múi giờ, fault injection có thể chạy. `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED`, `QA_PASS` chưa đạt/chưa chạy.

**NEED HUMAN:** None cho quyết định sản phẩm của #09 hiện tại. Các gap thuộc Tech/architecture; nếu giải pháp kỹ thuật buộc đổi R72/R73 hoặc hành vi đã chốt, BA/PM cần hỏi Ninh; không tự suy sang chuyển tiền.

**PENDING:** CapmmTech xử lý P0/P1 trong [technical design](technical-design.md) và spike/architecture review chung #08/#09: chứng minh atomicity/idempotency khoản thu/chi, operation result và số dư dưới đồng thời; quyền/trạng thái ở commit; contract #33/#35 và request tới muộn; VND/ngày; fault injection. Reviewer khác người thiết kế rà lại, PM kiểm phạm vi rồi mới xét `TECH_READY`. #08 còn `STORY_TECH_GAP`; chưa tạo ticket Dev.

**DETAIL:** [Spec](spec.md) mục 1–20, BR-09-01..07 và AC1–AC8; [R72–R73](../../docs/requirements/43-step-9-expense-round-1.md) chỉ cho khoản chi. F08 giữ phần nhập khi form còn mở và dữ liệu tự tải; R73 reset sau khi rời form trước Lưu. Màn lịch sử #10 và ca phục hồi Lưu chưa rõ #33/#35 là lát riêng. [Technical design](technical-design.md) là đề xuất và danh sách gap, chưa có architecture decision được duyệt, code, build hoặc test thực chạy. Reviewer kỹ thuật độc lập là `/root/tech_independent`; PM chỉ rà phạm vi, không nhận review kỹ thuật đó là của PM.

— CapmmPM

## CapmmTech — Story #09

**DONE:** Lập [technical design](technical-design.md) truy vết AC1–AC8, R72–R73, quyền theo chủ, danh mục chung #08, tiền chi nguyên dương và số dư có thể âm, ngày lịch, ghi chi và phép trừ B01; tách đường Lưu bình thường khỏi phục hồi #33/#35. `/root/tech_independent` đã review kỹ thuật độc lập ở mức tài liệu; CapmmPM đã rà phạm vi.

**RESULT:** `STORY_TECH_GAP`, **chưa `TECH_READY`**. Chưa có quyết định nền tảng được duyệt, spike, code, build hoặc test thực chạy.

**NEED HUMAN:** None ở mức quyết định sản phẩm hiện tại. Nếu phương án kỹ thuật đổi R72/R73, quyền hoặc miền tiền/ngày đã xác nhận, CapmmTech báo BA/PM để hỏi Ninh.

**PENDING:** Giải P0 phép trừ B01/operation result atomic, idempotent và phụ thuộc `HARD` #08; giải P1 quyền/trạng thái tại commit, hợp đồng #33/#35, VND/ngày và fault injection. Xin reviewer kỹ thuật độc lập rà lại, rồi PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev.

**DETAIL:** Xem [findings reviewer và bằng chứng cần có](technical-design.md), [spec](spec.md), [story](story.md) và [workflow](../../docs/workflows/capmm-end-to-end.md). Review tài liệu không phải kết quả test; không có build hay log QA.

— CapmmTech
