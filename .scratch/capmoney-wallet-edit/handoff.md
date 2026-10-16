# Handoff: Story 16 – Sửa tên và số dư ban đầu của ví

## CapmmBA — bản nháp bước 9, 16/10/2026 (ảnh chụp trước PM review)

**DONE:** Đối chiếu [issue nguồn](../capmoney-overall/issues/16-sua-thong-tin-vi.md) với [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); tạo [story](story.md), [spec 20 mục](spec.md) và [Q&A](qna.md). Tách quyết định đã có nguồn khỏi câu hỏi mới và chi tiết thiết kế.

**RESULT:** Đã bổ sung R76; bản nháp BA để review, **chưa `BA_READY`**. Không có `TECH_READY`, ticket Dev hoặc `QA_PASS`.

**NEED HUMAN:** None.

**DETAIL:** CapmmPM kiểm traceability `R30, R40, R44; B01, B10, B15, B26, B29, B31`, từng BR/AC, phụ thuộc và [câu hỏi](qna.md); nếu câu trả lời thay đổi hành vi, hỏi Ninh rồi ghi nguồn trước gate.

**PENDING:** Sau BA gate, CapmmTech lập `technical-design.md`, reviewer độc lập rà và PM kiểm phạm vi. Không biến dependency Dev thành lý do bỏ qua BA review.

— CapmmBA

## CapmmPM — rà BA độc lập sau sửa, 16/10/2026

**DONE:** Đối chiếu bản BA sau quyết định R74–R78 hoặc chỉnh nhãn nguồn với Story gốc, [spec](spec.md), [Q&A](qna.md), BR/AC và [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).

**RESULT:** `BA_READY` cho CM-16; chưa `TECH_READY`, chưa code/build/test/`QA_PASS`.

**NEED HUMAN:** None cho AC hiện tại; nếu thiết kế đưa ra lựa chọn sản phẩm mới, BA hỏi Ninh và rà gate.

**PENDING:** CapmmTech lập `technical-design.md`, reviewer khác người thiết kế rà, rồi CapmmPM kiểm phạm vi trước `TECH_READY`.

**DETAIL:** [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [spec](spec.md); chưa có test thực chạy.

— CapmmPM `/root`
