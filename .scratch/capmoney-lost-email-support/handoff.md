# Handoff: CM-26 – Hỗ trợ người mất email

## CapmmBA — CM-26 (ảnh chụp trước câu trả lời R79 và PM review)

**DONE:** Soạn [spec 20 mục](spec.md), [Story](story.md) và [Q&A](qna.md) từ [Story bước 8](../capmoney-overall/issues/26-ho-tro-nguoi-mat-email.md) cùng các quyết định R62; F10; H11; quyền bước 3.

**RESULT:** BA draft; chưa `BA_READY`, chưa `TECH_READY` hoặc gate triển khai. Chưa có code/build/test cho CM-26.

**NEED HUMAN:** Ninh: chọn cách ghi bằng chứng xác minh ngoài ứng dụng theo Q26.1

**PENDING:** CapmmPM review nguồn, BR ví dụ, AC và ranh giới; Ninh/CapmmBA xử lý: Cách ghi bằng chứng Ninh đã gọi lại số biết trước, nhận diện và xác nhận yêu cầu chưa được Ninh chọn. Sau BA review, CapmmTech thiết kế và xin reviewer kỹ thuật độc lập.

**DETAIL:** [spec](spec.md), [Q&A](qna.md), [Story](story.md), [nguồn](../capmoney-overall/issues/26-ho-tro-nguoi-mat-email.md); chưa chạy kiểm thử hay cập nhật Planio.

— `/root/spec_quality` (CapmmBA draft, 16/10/2026)

## CapmmPM — rà BA sau R79, 16/10/2026

**DONE:** Ghi [R79](../../docs/requirements/48-step-9-lost-email-evidence.md) từ lựa chọn của Ninh; đối chiếu [spec 20 mục](spec.md), BR-26-01..04, AC1–AC5, [Q&A](qna.md), [Story nguồn](../capmoney-overall/issues/26-ho-tro-nguoi-mat-email.md) và quyền quản lý với R62/F10. Kiểm rõ bản ghi là lời xác nhận của Ninh, không phải chứng minh tự động cuộc gọi ngoài ứng dụng.

**RESULT:** `BA_READY` cho CM-26; không còn câu hỏi sản phẩm chặn AC hiện tại. Chưa có `TECH_READY`, code/build/test hoặc `QA_PASS`.

**NEED HUMAN:** None cho phạm vi AC hiện tại; nếu thiết kế đổi quy tắc xác minh hoặc phạm vi dữ liệu lưu, hỏi Ninh và rà lại BA gate.

**PENDING:** CapmmTech thiết kế xác minh email mới, liên kết bản ghi hỗ trợ với yêu cầu và kết quả đổi, quyền xem, thời hạn lưu và chống sửa; reviewer kỹ thuật khác người thiết kế rà, rồi CapmmPM kiểm phạm vi trước `TECH_READY`.

**DETAIL:** [PM review bước 9](../capmoney-overall/step9-ba-review-2026-10-16.md), [R79](../../docs/requirements/48-step-9-lost-email-evidence.md), [spec](spec.md).

— CapmmPM `/root`
