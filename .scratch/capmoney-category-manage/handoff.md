# Handoff: CM-20 – Thêm và đổi tên danh mục

## CapmmBA — CM-20 (ảnh chụp trước PM review)

**DONE:** Soạn [spec 20 mục](spec.md), [Story](story.md) và [Q&A](qna.md) từ [Story bước 8](../capmoney-overall/issues/20-them-va-doi-ten-danh-muc.md) cùng các quyết định R12, R32, R45, R49, R54; B05, B16; H05.

**RESULT:** Đã bổ sung R78; BA draft; chưa `BA_READY`, chưa `TECH_READY` hoặc gate triển khai. Chưa có code/build/test cho CM-20.

**NEED HUMAN:** None

**PENDING:** CapmmPM review nguồn, BR ví dụ, AC và ranh giới; PM/Tech; Ninh nếu thay đổi hành vi sản phẩm xử lý: Tên trùng, giới hạn/chuẩn hóa tên và câu chữ lỗi chưa chốt; không tự áp đặt tên duy nhất. Sau BA review, CapmmTech thiết kế và xin reviewer kỹ thuật độc lập.

**DETAIL:** [spec](spec.md), [Q&A](qna.md), [Story](story.md), [nguồn](../capmoney-overall/issues/20-them-va-doi-ten-danh-muc.md); chưa chạy kiểm thử hay cập nhật Planio.

— `/root/spec_quality` (CapmmBA draft, 16/10/2026)

## CapmmPM — rà BA độc lập sau sửa, 16/10/2026

**DONE:** Đối chiếu bản BA sau quyết định R74–R78 hoặc chỉnh nhãn nguồn với Story gốc, [spec](spec.md), [Q&A](qna.md), BR/AC và [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).

**RESULT:** `BA_READY` cho CM-20; chưa `TECH_READY`, chưa code/build/test/`QA_PASS`.

**NEED HUMAN:** None cho AC hiện tại; nếu thiết kế đưa ra lựa chọn sản phẩm mới, BA hỏi Ninh và rà gate.

**PENDING:** CapmmTech lập `technical-design.md`, reviewer khác người thiết kế rà, rồi CapmmPM kiểm phạm vi trước `TECH_READY`.

**DETAIL:** [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [spec](spec.md); chưa có test thực chạy.

— CapmmPM `/root`
