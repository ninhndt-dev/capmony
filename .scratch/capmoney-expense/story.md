# Story 09: Ghi khoản chi

**Lifecycle:** design
**Status:** needs-triage
**Blocked by:** Story #08 cho danh mục dùng chung và nền ghi giao dịch, ví/số dư; `BA_READY` đã được PM xác nhận, còn phụ thuộc triển khai sẽ được phân loại trước kế hoạch Dev.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** khoản chi gắn ví đang sử dụng của chủ và một danh mục trong danh sách chung thu/chi; số tiền là số nguyên VND dương, ngày hôm nay hoặc trước đó, ghi chú tùy chọn. Số dư hiện tại giảm đúng theo khoản chi còn hiệu lực, kể cả thành âm và bất kể ngày ghi. Từ nút chung có ≥2 ví thì form chi chưa chọn ví và người dùng tự chọn trước Lưu (R72); rời form chi trước Lưu rồi quay lại cùng phiên có Internet thì form trống, không tạo khoản chi hay đổi số dư (R73). [R25, R39, R43, R72–R73](../../docs/requirements/00-intake.md), [B01–B02, B04, B12, B16](../../docs/requirements/core-business.md), [F14](../../docs/requirements/user-flows.md), [nhật ký #09](../../docs/requirements/43-step-9-expense-round-1.md).
- **Story ứng viên:** [issue #09](../capmoney-overall/issues/09-ghi-chi.md), đã được Ninh duyệt cách chia ở bước 8. [Spec 20 mục](spec.md) nêu AC và ví dụ dữ liệu cho bước 9.
- **Ngoài phạm vi:** danh mục mặc định và ghi thu (#08), màn lịch sử (#10), sửa/xóa/khôi phục (#11–#12), chuyển tiền (#13), quản lý danh mục (#20–#21), kiểm chứng lỗi mạng/lưu chưa rõ chuyên biệt (#33/#35), đồng thời hai điện thoại (#29). Quy tắc dùng chung vẫn ràng buộc thiết kế.
- **Q&A đã trả lời:** Q1=A → R72, Q2=A → R73; [Q&A](qna.md). R70–R71 là xác nhận riêng cho khoản thu #08; R72–R73 riêng cho khoản chi #09, không suy sang chuyển tiền hoặc tạo ví.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt ngày 14/10/2026**: CapmmBA đề xuất, CapmmPM đã đối chiếu 20 mục, BR-09-01..07 có ví dụ, AC1–AC8 và nguồn R72–R73; không còn câu hỏi sản phẩm chặn #09 | [spec](spec.md), [Q&A](qna.md), [nhật ký trả lời](../../docs/requirements/43-step-9-expense-round-1.md), [handoff](handoff.md) | CapmmPM đã rà bản sau câu trả lời |
| `TECH_READY` | **Chưa đạt — `STORY_TECH_GAP` ngày 14/10/2026**. Reviewer kỹ thuật độc lập và CapmmPM đã rà; thiết kế đúng phạm vi BA nhưng còn gap nền tảng và hợp đồng #08 | [technical design](technical-design.md), [handoff PM](handoff.md): giao dịch chi, kết quả thao tác và số dư B01 nhất quán/atomic khi retry và thu+chi đồng thời; quyền/trạng thái tại commit, #33/#35, VND/ngày và fault injection | `/root/tech_independent` khác người lập thiết kế; CapmmPM rà phạm vi |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho từng ticket | Chưa chia ticket Dev | Chưa có |
| `DEV_REVIEWED` | Chưa chạy | Chưa có code/PR | Chưa có |
| `QA_PASS` | Chưa chạy | Chưa có build/test thực chạy | Chưa có |

## Phụ thuộc và việc tiếp theo

- **#08 → #09:** `HARD` cho Dev/tích hợp AC1–AC8 vì #08 cấp danh mục chung và nền giao dịch/số dư; #08 đã `BA_READY` nhưng còn `STORY_TECH_GAP`. Phụ thuộc không chặn thiết kế #09. Chỉ xét phần độc lập qua mock theo `CONTRACT` sau khi hợp đồng đã được review, chưa tự đổi cạnh issue.
- **#06/#07/#04:** ví đã lưu, khả năng có nhiều ví, phiên và quyền là tiền đề tích hợp gián tiếp qua #08.
- **Tiếp theo:** Tech chứng minh hợp đồng/spike trong [technical design](technical-design.md), reviewer độc lập rà lại rồi PM kiểm phạm vi trước `TECH_READY`. `Lifecycle: design` giữ nguyên; chưa chia ticket Dev, chưa `READY_FOR_IMPLEMENTATION`.
