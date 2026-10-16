# Handoff: Story 11 – Sửa khoản thu/chi

## CapmmBA — bản nháp bước 9, 16/10/2026 (ảnh chụp trước PM review)

**DONE:** Đối chiếu [issue nguồn](../capmoney-overall/issues/11-sua-thu-chi.md) với [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ B01–B31](../../docs/requirements/core-business.md), [luồng F01–F26](../../docs/requirements/user-flows.md), [nhóm H01–H12](../../docs/requirements/system-functions.md), [quyền](../../docs/requirements/actors-and-roles.md); tạo [story](story.md), [spec 20 mục](spec.md) và [Q&A](qna.md). Tách quyết định đã có nguồn khỏi câu hỏi mới và chi tiết thiết kế.

**RESULT:** Bản nháp BA để review, **chưa `BA_READY`**. Không có `TECH_READY`, ticket Dev hoặc `QA_PASS`.

**NEED HUMAN:** None.

**DETAIL:** CapmmPM kiểm traceability `B01–B03, B07, B12, B14, B20–B21, B30; R43, R59; F22`, từng BR/AC, phụ thuộc và [câu hỏi](qna.md); nếu câu trả lời thay đổi hành vi, hỏi Ninh rồi ghi nguồn trước gate.

**PENDING:** Sau BA gate, CapmmTech lập `technical-design.md`, reviewer độc lập rà và PM kiểm phạm vi. Không biến dependency Dev thành lý do bỏ qua BA review.

— CapmmBA

## CapmmPM — rà BA độc lập, 16/10/2026

**DONE:** Đối chiếu [spec](spec.md), [Q&A](qna.md), Story nguồn và quyết định R/S/B/F/H; kiểm 20 mục, BR có ví dụ, AC quan sát được, quyền, ranh giới và phụ thuộc. Xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).

**RESULT:** `BA_READY` cho CM-11; Tech design chưa có review, nên chưa `TECH_READY`. Không có code/test/`QA_PASS` từ review này.

**NEED HUMAN:** None cho AC hiện tại; nếu thiết kế nảy sinh lựa chọn hành vi mới, BA phải hỏi Ninh và rà lại gate.

**PENDING:** CapmmTech lập `technical-design.md`, reviewer khác người thiết kế kiểm kỹ thuật, rồi CapmmPM rà phạm vi trước `TECH_READY`.

**DETAIL:** [Bảng kết luận CM-11](../capmoney-overall/step9-ba-review-2026-10-16.md), [spec](spec.md); chưa có build/test thực chạy.

— CapmmPM `/root`
