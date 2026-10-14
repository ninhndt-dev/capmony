# Story 08: Danh mục mặc định và ghi khoản thu

**Lifecycle:** design
**Status:** needs-triage
**Blocked by:** Story #06 và #07 cho đường tích hợp và kiểm chứng ghi thu; `BA_READY` đã được PM xác nhận, còn phụ thuộc triển khai sẽ được phân loại trước kế hoạch Dev.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** mỗi tài khoản ghi chép mới có cùng mười danh mục mặc định dùng chung thu/chi (B16, R45); khoản thu gắn một danh mục và ví đang sử dụng của chính mình, số tiền nguyên VND dương, ngày hôm nay hoặc trước đó, ghi chú tùy chọn (R25, R43, B02, B04, B12, F14). Số dư hiện tại tăng theo khoản thu còn hiệu lực bất kể ngày ghi (B01). Từ nút chung khi có ≥2 ví, form khoản thu chưa chọn ví và người dùng tự chọn trước Lưu (R70); rời form trước Lưu rồi quay lại cùng phiên có Internet thì form trống, không tạo khoản thu hay đổi số dư (R71). Nguồn: [nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md), [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nhật ký #08](../../docs/requirements/42-step-9-income-round-1.md).
- **Story ứng viên đã được duyệt cách chia:** [issue #08](../capmoney-overall/issues/08-danh-muc-mac-dinh-va-ghi-thu.md). [Spec 20 mục](spec.md) diễn đạt hành vi, BR và AC ở bước 9.
- **Ngoài phạm vi:** ghi chi (#09), màn lịch sử (#10), sửa/xóa/khôi phục giao dịch (#11–#12), thêm/đổi tên/xóa danh mục (#20–#21), chuyển tiền (#13), luồng đối chiếu lưu chưa rõ chuyên biệt (#33, #35), kiểm chứng hai giao dịch đồng thời (#29). Các quy tắc dùng chung vẫn là ràng buộc khi thiết kế.
- **Q&A đã trả lời:** Q1=A → R70, Q2=B → R71; [Q&A](qna.md). Quyết định áp dụng cho **khoản thu #08**, không tự áp cho khoản chi #09.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt ngày 14/10/2026**: CapmmBA đề xuất, CapmmPM đã đối chiếu 20 mục, BR-08-01..08 có ví dụ, AC1–AC8 và nguồn R70–R71; không còn câu hỏi sản phẩm chặn #08 | [spec](spec.md), [Q&A](qna.md), [nhật ký trả lời](../../docs/requirements/42-step-9-income-round-1.md), [handoff](handoff.md) | CapmmPM đã rà bản sau câu trả lời |
| `TECH_READY` | **Chưa đạt — `STORY_TECH_GAP` ngày 14/10/2026**. Reviewer kỹ thuật độc lập và CapmmPM đã rà; thiết kế đúng phạm vi BA nhưng còn gap nền tảng/contract | [technical design](technical-design.md), [handoff PM](handoff.md): khởi tạo danh mục với #04; ghi thu, kết quả thao tác và số dư nhất quán dưới đồng thời; hợp đồng #33/#35, tiền/ngày/quyền và fault injection | `/root/tech_independent` khác người lập thiết kế; CapmmPM rà phạm vi |
| `READY_FOR_IMPLEMENTATION` | Chưa đánh giá cho từng ticket | Chưa chia ticket Dev | Chưa có |
| `DEV_REVIEWED` | Chưa chạy | Chưa có code/PR | Chưa có |
| `QA_PASS` | Chưa chạy | Chưa có build/test thực chạy | Chưa có |

## Phụ thuộc và việc tiếp theo

- **#06 và #07 → #08:** `HARD` cho tích hợp/kiểm chứng đường ghi thu và demo hai ví; không chặn việc soạn thiết kế #08. #06 chưa đạt gate workflow mới; #07 `BA_READY` nhưng còn `STORY_TECH_GAP`. Có thể xét mock theo `CONTRACT` cho phần độc lập sau khi hợp đồng được chốt, chưa tự đổi cạnh issue.
- **#04 → #08:** `HARD` cho phiên, quyền chủ và khởi tạo danh mục lúc tài khoản dùng được; hợp đồng nền tảng chưa chốt.
- **Tiếp theo:** Tech chứng minh hợp đồng và spike trong [technical design](technical-design.md), reviewer độc lập rà lại rồi PM kiểm phạm vi trước `TECH_READY`. `Lifecycle: design` giữ nguyên; chưa chia ticket Dev, chưa `READY_FOR_IMPLEMENTATION`.
