# Handoff CM-01: Thiết lập và đăng nhập quản lý

## CapmmBA — CM-01

**DONE:** Chuyển đặc tả cũ thành [spec 20 mục](spec.md), lập [story](story.md) và [Q&A](qna.md); tách quyết định của Ninh F11/F23–F24/R12/R13/R22 khỏi đề xuất UI/Tech; bổ sung AC3 âm và AC6 sau PM review.

**RESULT:** BA đề xuất `BA_READY`; PM độc lập đã xác nhận ngày 14/10/2026. Tech và PM đã chốt `TECH_READY` sau đó; chưa có ticket Dev, build hay QA.

**NEED HUMAN:** None cho BA. Email quản lý/tên miền thật là đầu vào vận hành sau.

**PENDING:** BA chỉ rà lại nếu ticket Dev hoặc QA phát hiện mâu thuẫn với AC/nguồn quyết định.

**DETAIL:** [spec](spec.md), [story](story.md), [Q&A](qna.md), [intake](../../docs/requirements/00-intake.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Chưa chạy kiểm thử hay gửi email thật.

— Codex `/root` trong vai CapmmBA

## CapmmPM — CM-01 / BA gate

**DONE:** Reviewer độc lập `/root/cm01_pm_ba` đối chiếu đủ 20 mục với F11/F23–F24, R12/R13/R22 và CM01-Q1; yêu cầu mở AC3 cho đăng nhập sai/phiên hết hạn, thêm AC6 cho gửi email thất bại/chưa rõ, xác định diễn giải mốc gửi. BA đã sửa, PM rà lại không còn P0/P1 BA.

**RESULT:** `BA_READY` **đạt ngày 14/10/2026**. Tech và PM đã chốt `TECH_READY` sau đó; chưa ticket nào đạt `READY_FOR_IMPLEMENTATION`.

**NEED HUMAN:** None cho BA.

**PENDING:** PM chia ticket Dev sau khi Ninh duyệt độ hạt/phụ thuộc; BA rà ticket về AC khi cần.

**DETAIL:** [spec mục 6–8, 16](spec.md), [Q&A CM01-Q1](qna.md), [Story nguồn](../capmoney-overall/issues/01-thiet-lap-quan-ly.md). Review là rà tài liệu, chưa phải code/build/test thực chạy.

— `/root/cm01_pm_ba` trong vai CapmmPM độc lập

## CapmmTech — CM-01

**DONE:** Lập [technical-design](technical-design.md) và [ADR 0001](../../docs/adr/0001-manager-auth-foundation.md); thử PostgreSQL/SMTP/scrypt và HTTP/phiên trên dữ liệu tạm. Khép các P1 qua hai lượt phản hồi của reviewer Tech độc lập; sửa nhãn hai cổng cùng một tiến trình.

**RESULT:** Reviewer `/root/cm01_tech_review` ghi `TECH_REVIEW_PASS`, không còn P0/P1. PM độc lập `/root/cm01_pm_ba` rà tác động quota và phạm vi, chấp nhận ADR 0001. `TECH_READY` **đạt ngày 14/10/2026** ở cấp Story; chưa có `READY_FOR_IMPLEMENTATION`, code hoặc build.

**NEED HUMAN:** None để chia ticket. Email quản lý, tên miền và SMTP thật cần trước kiểm vận hành thật; Ninh đã chọn hộp thư thử trước.

**PENDING:** PM đề xuất lát cắt Dev và xin Ninh duyệt độ hạt/phụ thuộc theo `to-tickets`; tạo issue cấp dưới trên Planio, xét readiness riêng. Trong triển khai/QA phải thử nhiều tiến trình thật, Next.js route, quyền server, thiết bị và luồng Mailpit; chưa thử email thật.

**DETAIL:** Spike SQL tạm ở `/tmp/capmoney-cm01-spike.sql` và `/tmp/capmoney-cm01-issuer-spike.sql`; HTTP tạm ở `/tmp/capmoney-cm01-http-spike/spike.js`. Kết quả và giới hạn ghi trong [technical-design](technical-design.md). Quota cố định 10 yêu cầu/IP/15 phút và 60/account/15 phút tính cả thành công có thể làm Ninh phải chờ, kể cả kéo dài nếu bị thử sai liên tục; phiên hợp lệ còn dùng được. Các script tạm không phải code ứng dụng.

— Codex `/root` trong vai CapmmTech; review độc lập `/root/cm01_tech_review`, PM độc lập `/root/cm01_pm_ba`

## CapmmPM — CM-01 / Dev planning

**DONE:** Sau `BA_READY` và `TECH_READY`, đề xuất ba lát dọc theo `to-tickets`; Ninh chọn A duyệt ba ticket và chuỗi phụ thuộc CM-01.1 → CM-01.2 → CM-01.3 ngày 14/10/2026. Đã lập [CM-01.1](issues/01-send-first-setup-link.md), [CM-01.2](issues/02-complete-manager-setup.md), [CM-01.3](issues/03-manager-login-session.md) và [test-spec](test-spec.md). BA và Tech độc lập rà nội dung, không thấy P0/P1; sửa các P2 về ranh giới trang entry, dẫn thiết kế và quota.

**RESULT:** Đã tạo và đọc lại ba issue con Planio #43–#45 và quan hệ `blocks` #43→#44→#45. PM từng xác nhận CM-01.1 / #43 đạt `READY_FOR_IMPLEMENTATION` ngày 14/10/2026 theo thiết kế Next.js. **Sau đó Ninh chọn TS01/TS06–TS08 đổi kiến trúc toàn dự án và ưu tiên hạ tầng**, nên gate Tech và readiness #43 tạm dừng hiệu lực, Dev bị dừng trước khi viết code. CM-01.2 và CM-01.3 vẫn có `HARD` blocker.

**NEED HUMAN:** Ninh sẽ duyệt bản kiến trúc hạ tầng/ADR toàn dự án sau Tech/PM review. API key Planio đã được Ninh đặt trong file cục bộ quyền 600; không có trong Git hoặc chat.

**PENDING:** Tech lập sơ đồ, ADR, môi trường và thứ tự triển khai cho React web + API riêng, Docker dev/test và VPS/PostgreSQL production; reviewer Tech độc lập và PM rà, Ninh duyệt. Sau đó dựng Docker theo ticket/gate phù hợp rồi mới quay lại FE/BE CM-01.1. QA chạy trên build xác định; email thật/thiết bị thật cần điều kiện riêng.

**DETAIL:** [Story](story.md), [spec](spec.md), [thiết kế](technical-design.md), [test-spec](test-spec.md), [tracker](../../docs/agents/issue-tracker.md), Planio [#43](https://tanninh.planio.com/issues/43), [#44](https://tanninh.planio.com/issues/44), [#45](https://tanninh.planio.com/issues/45). Chưa có code ứng dụng, build hoặc kết quả test ứng dụng.

— Codex `/root` trong vai CapmmPM; BA review `/root/cm01_pm_ba`, Tech review `/root/cm01_tech_review`
