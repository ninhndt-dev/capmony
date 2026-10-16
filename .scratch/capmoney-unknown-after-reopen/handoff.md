# Handoff: Story 35 – Đối chiếu Lưu giao dịch sau khi mở lại

## CapmmBA drafter — Story 35, 16/10/2026 (ảnh chụp trước PM review)

**DONE:** Soạn [spec 20 mục](spec.md) với BR có ví dụ, AC quan sát được, nguồn R/B/F và ranh giới phụ thuộc; lập [Story](story.md) và [Q&A](qna.md).

**RESULT:** Bản nháp bước 9; **chưa `BA_READY`** vì chưa có PM độc lập review. `TECH_READY`, ticket Dev, `DEV_REVIEWED` và `QA_PASS` đều chưa đánh giá/chưa chạy.

**NEED HUMAN:** Không có câu hỏi Ninh chặn BA hiện đã biết; PM cần kiểm xem có nhánh sản phẩm phát sinh khi rà AC.

**PENDING:** PM đối chiếu nguồn/AC và ghi kết luận; Tech giải điểm còn mở: Giữ định danh ý định qua đóng/mở và đổi tài khoản, đối chiếu bền, kết quả âm an toàn. Sau đó reviewer Tech độc lập/PM xét thiết kế và phụ thuộc #33 và #34 là HARD; #08/#09/#13 cấp loại giao dịch.

**DETAIL:** [Story ứng viên #35](../capmoney-overall/issues/35-doi-chieu-luu-chua-ro-sau-mo-lai.md), [spec](spec.md), [Q&A](qna.md). Chưa có code, build hoặc QA chạy cho Story này.

— CapmmBA drafter

## CapmmPM — rà BA độc lập, 16/10/2026

**DONE:** Đối chiếu [spec](spec.md), [Q&A](qna.md), Story nguồn và quyết định R/S/B/F/H; kiểm 20 mục, BR có ví dụ, AC quan sát được, quyền, ranh giới và phụ thuộc. Xem [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md).

**RESULT:** `BA_READY` cho CM-35; Tech design chưa có review, nên chưa `TECH_READY`. Không có code/test/`QA_PASS` từ review này.

**NEED HUMAN:** None cho AC hiện tại; nếu thiết kế nảy sinh lựa chọn hành vi mới, BA phải hỏi Ninh và rà lại gate.

**PENDING:** CapmmTech lập `technical-design.md`, reviewer khác người thiết kế kiểm kỹ thuật, rồi CapmmPM rà phạm vi trước `TECH_READY`.

**DETAIL:** [Bảng kết luận CM-35](../capmoney-overall/step9-ba-review-2026-10-16.md), [spec](spec.md); chưa có build/test thực chạy.

— CapmmPM `/root`
