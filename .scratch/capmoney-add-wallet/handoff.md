# Handoff: Story 07 – Thêm ví

Mục tác nghiệp hiện hành cho cặp **CapmmBA + Story #07**. Các lượt sau sửa mục này; lịch sử nằm trong Git.

## CapmmBA — Story #07

**DONE:** Lập [story](story.md), [Q&A](qna.md) và [spec 20 mục](spec.md); sửa các ranh giới theo PM review trước đó. Đối chiếu [câu trả lời #07](../../docs/requirements/41-step-9-add-wallet-round-1.md): Q1=B → R68, Q2=A → R69; cập nhật các luồng và chốt AC7–AC8. PM phản hồi mục 8 thiếu ví dụ BR; BA đã bổ sung ví dụ dữ liệu cho BR-07-01..04 và nhánh R68/R69, không thêm quy tắc sản phẩm. Điểm vào UI vẫn là BA đề xuất.

**RESULT:** CapmmBA đã **đề xuất `BA_READY`** sau khi bổ sung ví dụ; CapmmPM đã xác nhận gate ở mục bên dưới. `TECH_READY` chưa đạt; `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED` và `QA_PASS` chưa đánh giá/chưa chạy.

**NEED HUMAN:** None. Không còn câu hỏi Q1–Q2 cần Ninh trả lời.

**PENDING:** Theo dõi các phát hiện Tech có thể ảnh hưởng hành vi đã xác nhận; nếu một giới hạn nền tảng làm đổi R68/R69, BA và PM phải quay lại làm rõ với Ninh. Chưa tạo ticket Dev.

**DETAIL:** Bằng chứng là [spec](spec.md), [Q&A](qna.md), [nhật ký R68–R69](../../docs/requirements/41-step-9-add-wallet-round-1.md), [Story ứng viên #07](../capmoney-overall/issues/07-them-vi.md), [workflow](../../docs/workflows/capmm-end-to-end.md). PM đã xác nhận `BA_READY`; [technical design](technical-design.md) đã được review ở mức tài liệu. Chưa có code, build hoặc test run.

— CapmmBA

## CapmmPM — Story #07

**DONE:** Đối chiếu [spec 20 mục](spec.md), [Q&A](qna.md), [R68–R69](../../docs/requirements/41-step-9-add-wallet-round-1.md) và nguồn R15/R66/F21; kiểm BR-07-01..04 có ví dụ, AC1–AC8, quyền tạo/đọc và ranh giới #10/#16/#18. Sau review của `/root/tech_independent`, rà [technical design](technical-design.md) với phạm vi BA; phân loại #06→#07 `HARD` cho Dev/demo, #04 là tiền đề phiên/quyền, F20/#18 không chặn ngược #07.

**RESULT:** `BA_READY` **đạt ngày 14/10/2026** và giữ hiệu lực. PM xác nhận Tech design bám phạm vi AC1–AC8 nhưng kết luận `STORY_TECH_GAP`, **chưa `TECH_READY`** theo [review kỹ thuật độc lập](technical-design.md); thiếu bằng chứng P0 về kết thúc “chắc chưa lưu” trước request tới muộn và idempotency/atomicity. `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED` và `QA_PASS` chưa đạt/chưa chạy.

**NEED HUMAN:** None.

**PENDING:** CapmmTech xử lý P0/P1 qua spike và hợp đồng nền tảng chung #04/#06: terminal “chắc chưa lưu”, ghi một lần dưới retry/đồng thời, dấu vết pending qua đóng/mở/nhiều tab, VND nguyên chính xác và test fault injection. Xin reviewer kỹ thuật độc lập rà lại và PM kiểm phạm vi trước khi xét `TECH_READY`. Chưa tạo ticket Dev hoặc giao code.

**DETAIL:** Bằng chứng PM review: [spec mục 8 và 16](spec.md), [Q&A](qna.md), [nhật ký R68–R69](../../docs/requirements/41-step-9-add-wallet-round-1.md), [technical design với kết luận `/root/tech_independent`](technical-design.md), [story gate](story.md) và [workflow](../../docs/workflows/capmm-end-to-end.md). Ngày 14/10/2026, Codex `/root` rà phạm vi [đề xuất hợp đồng tiếp theo](technical-contract-proposal.md) với AC7–AC8: R68 và R69 được tách đúng; việc cho tên ví trùng **chưa được Ninh chốt**, không đưa thành quy tắc. Đề xuất chưa được reviewer kiểm lại sau sửa P1 và chưa chứng minh P0. Review độc lập là rà tài liệu, chưa có code, build, spike hoặc test thực chạy. Tech có thể khảo sát khi chờ #04, nhưng #04 chưa có hợp đồng được duyệt để mở khóa Dev.

— CapmmPM

## CapmmTech — Story #07

**DONE:** Lập [technical design](technical-design.md) cho AC1–AC8 sau `BA_READY`; đối chiếu R66, R68–R69 và [nền tảng Tech #06](../capmoney-first-wallet/technical-options.md). Ghi ranh giới quyền, VND số nguyên, tạo một lần, đối chiếu sau đóng trình duyệt, lỗi/đồng thời, test tích hợp và phụ thuộc #04/#06. Bổ sung [đề xuất hợp đồng thao tác ví](technical-contract-proposal.md): định danh ý định, phân xử ghi/rào chắn âm, tìm lại cả kết quả cuối chưa được xác nhận đã thấy, chống phản hồi muộn khi đổi/xóa tài khoản và ma trận spike. Reviewer `/root/tech_independent` đã rà bản đầu ở mức tài liệu; các phát hiện P1 đã được sửa trong đề xuất, **chưa có lượt reviewer xác nhận lại bản sửa**. Chưa chọn stack, schema hoặc API.

**RESULT:** `STORY_TECH_GAP`; **chưa `TECH_READY`**. Reviewer kỹ thuật độc lập và PM đã rà bản thiết kế; chưa có spike, code, build hoặc test thực chạy.

**NEED HUMAN:** None. Nếu spike đưa ra giới hạn sản phẩm mới, CapmmTech báo BA/PM để hỏi Ninh bằng lựa chọn trên giao diện.

**PENDING:** CapmmTech xử lý các điểm P0/P1 trong [technical design](technical-design.md) bằng spike nền tảng chung về phiên/quyền, số nguyên VND, ghi bền một lần, kết quả “chắc chưa lưu” an toàn trước request đến muộn, dấu vết qua đóng/mở/nhiều tab và đọc lại trên điện thoại thứ hai. Xin review kỹ thuật độc lập và PM kiểm lại. Chỉ xét `TECH_READY` khi có bằng chứng theo [workflow](../../docs/workflows/capmm-end-to-end.md); chưa chia ticket Dev.

**DETAIL:** Nguồn là [spec #07](spec.md), [Q&A](qna.md), [R68–R69](../../docs/requirements/41-step-9-add-wallet-round-1.md), [story gate](story.md), [technical design](technical-design.md) và [đề xuất hợp đồng](technical-contract-proposal.md). Kiểm thử trong design là kế hoạch; **chưa chạy** test, chưa có build hay hạ tầng được cấp.

— CapmmTech
