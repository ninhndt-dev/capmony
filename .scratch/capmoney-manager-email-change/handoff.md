# Handoff: CM-23 – Đổi email quản lý

## CapmmBA — CM-23 (ảnh chụp trước PM review)

**DONE:** Soạn [spec 20 mục](spec.md), [Story](story.md) và [Q&A](qna.md) từ [Story bước 8](../capmoney-overall/issues/23-doi-email-quan-ly.md) cùng các quyết định R12; quyền bước 3; H01.

**RESULT:** BA draft; chưa `BA_READY`, chưa `TECH_READY` hoặc gate triển khai. Chưa có code/build/test cho CM-23.

**NEED HUMAN:** None

**PENDING:** CapmmPM review nguồn, BR ví dụ, AC và ranh giới; PM/Tech xử lý: Phương thức/TTL xác minh email và báo xung đột chưa chốt; Tech/PM đề xuất, BA hỏi Ninh nếu đổi quyền. Sau BA review, CapmmTech thiết kế và xin reviewer kỹ thuật độc lập.

**DETAIL:** [spec](spec.md), [Q&A](qna.md), [Story](story.md), [nguồn](../capmoney-overall/issues/23-doi-email-quan-ly.md); chưa chạy kiểm thử hay cập nhật Planio.

— `/root/spec_quality` (CapmmBA draft, 16/10/2026)

## CapmmPM — rà BA độc lập, 16/10/2026

**DONE:** Đối chiếu [spec](spec.md), [Q&A](qna.md), Story nguồn và quyết định R/S/B/F/H; kiểm 20 mục, BR có ví dụ, AC quan sát được, quyền, ranh giới và phụ thuộc. Xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).

**RESULT:** `BA_READY` cho CM-23; Tech design chưa có review, nên chưa `TECH_READY`. Không có code/test/`QA_PASS` từ review này.

**NEED HUMAN:** None cho AC hiện tại; nếu thiết kế nảy sinh lựa chọn hành vi mới, BA phải hỏi Ninh và rà lại gate.

**PENDING:** CapmmTech lập `technical-design.md`, reviewer khác người thiết kế kiểm kỹ thuật, rồi CapmmPM rà phạm vi trước `TECH_READY`.

**DETAIL:** [Bảng kết luận CM-23](../capmoney-overall/step9-ba-review-2026-10-16.md), [spec](spec.md); chưa có build/test thực chạy.

— CapmmPM `/root`
