# Handoff CM-01: Thiết lập và đăng nhập quản lý

**Hiện hành 14/10/2026:** Ninh chọn [TS10–TS38](../../docs/requirements/tech-stack-round-2.md): Java Spring, JWT cookie/refresh/revocation và Redis throttle. Các mục bàn giao Next.js/Node/opaque session bên dưới là lịch sử; BA/PM đã cấp lại `BA_READY` cho bản Java/JWT, còn `TECH_READY` và readiness #43–#45 tạm dừng. Không có code CM-01 hoặc QA.

## CapmmPM — CM-01 / BA gate Java-JWT

**DONE:** Rà độc lập [spec 20 mục](spec.md), F11/F23–F24, R12/R13/R22/R57, TS10–TS38 và [CM01-Q1–Q3](qna.md) sau BA cập nhật. Phát hiện P1 ban đầu: AC7 đòi kiểm đổi/đặt lại mật khẩu, trong khi đặt lại thuộc #22 còn đổi mật khẩu chưa nằm trong CM-01; như vậy CM-01 không thể đạt `QA_PASS` riêng. BA đã chuyển yêu cầu R57 sang mục phụ thuộc #22, bỏ khỏi AC7. P2 về gọi nhầm đổi mật khẩu là phạm vi #22 đã sửa trong spec. Rà lại AC1–AC7: mục tiêu, actor/quyền, luồng thường/lỗi, quy tắc một quản lý, hạn 72 giờ, đăng xuất mọi thiết bị và thu hồi family khi refresh reuse đều có nguồn và tiêu chí kiểm được.

**RESULT:** `BA_READY` **đạt cho CM-01 Java/JWT ngày 14/10/2026**, không còn P0/P1 BA. Thời hạn refresh 7 ngày đã chốt; cách tính hạn family qua nhiều lần rotate là `TECH_GAP` cần Tech đề xuất và PM rà trải nghiệm trước `TECH_READY`. Review này không cấp `TECH_READY`, readiness #43–#45 hay `QA_PASS`.

**NEED HUMAN:** None cho gate BA. Email/tên miền/SMTP thật cần khi thử vận hành thật, không chặn thử dữ liệu riêng. Nếu Tech đề xuất gia hạn family vượt 7 ngày và PM coi đó là thay đổi sản phẩm, phải hỏi Ninh qua giao diện lựa chọn.

**PENDING:** Tech lập thiết kế Java/JWT/Redis, reviewer Tech khác người lập và PM kiểm `TECH_READY`; sau đó PM xét lại từng ticket #43–#45. #43 còn bị #47 chặn trước tích hợp; #44/#45 có phụ thuộc tuần tự. QA chưa có build Java để chạy.

**DETAIL:** [Spec AC1–AC7](spec.md), [Q&A](qna.md), [Story](story.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Không có code, build/test CM-01 Java hoặc email thật tại thời điểm review.

— `/root/pm_ba_rebaseline` trong vai CapmmPM độc lập, khác người lập BA

## CapmmBA — CM-01 / rebaseline Java-JWT

**DONE:** Đối chiếu F11/F23–F24, R22/R56/R57 và TS10–TS38; cập nhật [spec](spec.md) AC3/AC7, thời hạn access/refresh, cookie, thu hồi DB, Redis; ghi [CM01-Q2/Q3](qna.md) về phạm vi đăng xuất và refresh reuse. Tách quyết định Ninh đã xác nhận khỏi đề xuất Tech và đầu vào vận hành.

**RESULT:** BA draft cập nhật; **chưa `BA_READY` trở lại** vì PM chưa review AC1–AC7 sau khi Ninh đã chọn CM01-Q2/B và CM01-Q3/A. `TECH_READY` cũ không còn hiệu lực cho Java/JWT/Redis. #43 chưa ready; #44/#45 còn `HARD` blocker tuần tự.

**NEED HUMAN:** None cho CM01-Q2/Q3; Ninh đã chọn B: đăng xuất mọi điện thoại, và A: thu hồi family khi refresh reuse không phân biệt được. PM cần rà lại spec; hai câu trả lời này chưa duyệt thiết kế Tech.

**PENDING:** PM kiểm `BA_READY` trên AC7 đã sửa; Tech lập và reviewer khác người lập rà thiết kế CM-01 Java/JWT/Redis, rồi PM kiểm `TECH_READY`. PM cập nhật ticket #43–#45 và phụ thuộc #47 trước khi xét từng `READY_FOR_IMPLEMENTATION`.

**DETAIL:** [Nguồn quyết định TS10–TS38](../../docs/requirements/tech-stack-round-2.md), [spec](spec.md), [Q&A](qna.md), [story](story.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Chưa chạy build/test CM-01 Java, chưa gửi email thật.

— `/root/cm01_java_ba` trong vai CapmmBA; chờ PM độc lập review

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
