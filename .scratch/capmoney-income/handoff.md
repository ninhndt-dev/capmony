# Handoff: Story 08 – Danh mục mặc định và ghi khoản thu

Các mục tác nghiệp hiện hành cho Story #08; mỗi actor sửa mục của mình, Git giữ lịch sử.

## CapmmBA — Story #08

**DONE:** Đối chiếu [issue #08](../capmoney-overall/issues/08-danh-muc-mac-dinh-va-ghi-thu.md), [intake](../../docs/requirements/00-intake.md), [nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md), [chức năng](../../docs/requirements/system-functions.md), [spec tổng thể](../capmoney-overall/spec.md), [#06](../capmoney-first-wallet/spec.md), [#07](../capmoney-add-wallet/story.md) và [workflow](../../docs/workflows/capmm-end-to-end.md). Soạn [story](story.md), [spec 20 mục](spec.md) và [Q&A](qna.md). Theo PM review bản nháp, cụ thể hóa Q1-B, đưa kiểm chứng mất mạng/đóng mở sang #33/#35 và bỏ phụ thuộc màn lịch sử #10 khỏi AC3. Ghi Ninh chọn **Q1=A, Q2=B** vào [nhật ký #08](../../docs/requirements/42-step-9-income-round-1.md), R70–R71 ở [intake](../../docs/requirements/00-intake.md), rồi cập nhật BR/AC cho khoản thu.

**RESULT:** CapmmBA đã **đề xuất `BA_READY`** cho Story #08 sau R70–R71; CapmmPM xác nhận gate ở mục bên dưới. Chưa `TECH_READY`, `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED` hoặc `QA_PASS`.

**NEED HUMAN:** None cho Q1–Q2; CapmmPM đã review độc lập bản [spec](spec.md) sau R70–R71.

**PENDING:** CapmmTech xử lý các gap trong [technical design](technical-design.md); PM/Tech xử lý phụ thuộc #06/#07 và nền tảng #04 trước kế hoạch Dev. Nếu giới hạn kỹ thuật đổi hành vi R70/R71, BA/PM phải quay lại hỏi Ninh. Chưa tạo ticket Dev.

**DETAIL:** [Spec](spec.md) mục 1–20; [Q&A](qna.md) ghi nguồn R70–R71; [story](story.md) ghi gate/phụ thuộc. [Thiết kế kỹ thuật](technical-design.md) đã được review ở mức tài liệu; chưa có code, build hoặc test thực chạy.

— CapmmBA

## CapmmPM — Story #08

**DONE:** Đối chiếu [spec 20 mục](spec.md), [Q&A](qna.md), [R70–R71](../../docs/requirements/42-step-9-income-round-1.md), [issue #08](../capmoney-overall/issues/08-danh-muc-mac-dinh-va-ghi-thu.md) và [workflow](../../docs/workflows/capmm-end-to-end.md). Kiểm BR-08-01..08 có ví dụ, AC2 khi vào từ nút chung/ví, AC7 khi rời form trước Lưu, quyền dữ liệu riêng và ranh giới #09/#10/#33/#35. Sau đó rà [technical design #08](technical-design.md) theo BA spec và kết luận reviewer kỹ thuật độc lập `/root/tech_independent` ngày 14/10/2026; thiết kế giữ đúng phạm vi khoản thu, không suy R70/R71 sang #09, không gộp màn lịch sử #10 hoặc ca phục hồi #33/#35 vào lát này. R71 chỉ reset khi rời form trước Lưu; F08 giữ phần nhập nếu dữ liệu tự tải lại khi form vẫn mở.

**RESULT:** `BA_READY` **đạt ngày 14/10/2026** cho Story #08. Q1=A → R70 và Q2=B → R71 được ghi đúng cho **khoản thu**; đề xuất UI/Tech không thành quyết định Ninh. PM đồng ý kết luận reviewer độc lập: **`STORY_TECH_GAP`, chưa `TECH_READY`**. P0: chưa chứng minh khởi tạo đúng mười danh mục, idempotent và nhất quán với #04; chưa chứng minh ghi khoản thu, kết quả thao tác và số dư B01 nhất quán/atomic dưới retry và đồng thời. P1: còn thiếu hợp đồng định danh/pending/phục hồi với #33/#35, miền và tuần tự hóa VND, ngày lịch/múi giờ, kiểm chủ và trạng thái ví/danh mục tại commit, kế hoạch fault injection có thể chạy. `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED`, `QA_PASS` chưa đạt/chưa chạy.

**NEED HUMAN:** None cho quyết định sản phẩm của #08 hiện tại. Các gap trên thuộc Tech/architecture; chỉ quay lại Ninh nếu phương án kỹ thuật làm đổi yêu cầu đã xác nhận. Khi đặc tả #09 phải làm rõ riêng nếu mặc định ví hoặc rời form khoản chi ảnh hưởng AC.

**PENDING:** CapmmTech xử lý P0/P1 trong [technical design](technical-design.md) và spike/architecture review: bằng chứng nhất quán #04/danh mục; atomicity và idempotency của giao dịch, operation result, số dư; contract #33/#35 và trường hợp request tới muộn; VND/ngày/quyền tại commit; fault injection. Reviewer khác người thiết kế rà lại, PM kiểm phạm vi rồi mới xét `TECH_READY`. #04/#06/#07 còn chặn tích hợp/kiểm chứng; chưa tạo ticket Dev.

**DETAIL:** [Spec mục 8/16/18](spec.md) tách AC đường Lưu bình thường khỏi ca đối chiếu #33/#35, màn lịch sử thuộc #10; [nhật ký R70–R71](../../docs/requirements/42-step-9-income-round-1.md) chỉ áp dụng cho khoản thu. Ngày 14/10/2026, Codex `/root` rà phạm vi [đề xuất hợp đồng chung](shared-ledger-proposal.md) với AC1–AC8: ba mô hình số dư và hai mô hình cấp danh mục là phương án Tech, chưa được chọn; quy tắc xác định “hôm nay” theo múi giờ có ảnh hưởng R43 nên cần BA/PM xem trước khi thành quyết định. [Technical design](technical-design.md) là đề xuất và danh sách gap, có ca đối chứng F08/R71; chưa có architecture decision được duyệt, code, build hoặc test thực chạy. Reviewer kỹ thuật độc lập là `/root/tech_independent`; PM chỉ rà phạm vi, không nhận review kỹ thuật đó là của PM.

— CapmmPM

## CapmmTech — Story #08

**DONE:** Lập [technical design](technical-design.md) truy vết AC1–AC8, R70–R71, quyền theo chủ, khởi tạo mười danh mục, VND/ngày, ghi thu và số dư B01; tách đường Lưu bình thường khỏi phục hồi #33/#35. Bổ sung [đề xuất hợp đồng sổ chung #08–#09](shared-ledger-proposal.md): hai phương án cấp danh mục, ba phương án số dư, bất biến ghi thu/chi một lần, quyền tại commit và ma trận spike. `/root/tech_independent` đã review kỹ thuật độc lập bản đề xuất ở mức tài liệu và giữ `STORY_TECH_GAP`; phần bổ sung về phản hồi muộn/tìm kết quả cuối chưa được reviewer xác nhận lại. CapmmPM đã rà phạm vi thiết kế trước đó.

**RESULT:** `STORY_TECH_GAP`, **chưa `TECH_READY`**. Chưa có quyết định nền tảng được duyệt, spike, code, build hoặc test thực chạy.

**NEED HUMAN:** None ở mức quyết định sản phẩm hiện tại. Nếu phương án kỹ thuật đổi R70/R71, miền tiền/ngày hoặc quyền đã xác nhận, CapmmTech báo BA/PM để hỏi Ninh.

**PENDING:** Giải P0 khởi tạo đúng mười danh mục gắn với #04 và ghi thu/kết quả thao tác/số dư nhất quán dưới retry/đồng thời; giải P1 hợp đồng #33/#35, VND/ngày, quyền tại commit và fault injection. Xin reviewer kỹ thuật độc lập rà lại, rồi PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev.

**DETAIL:** Xem [findings reviewer và bằng chứng cần có](technical-design.md), [đề xuất hợp đồng chung](shared-ledger-proposal.md), [spec](spec.md), [story](story.md) và [workflow](../../docs/workflows/capmm-end-to-end.md). Review tài liệu không phải kết quả test; không có build hay log QA.

— CapmmTech
