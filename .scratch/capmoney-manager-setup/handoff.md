## PM — READY_FOR_IMPLEMENTATION #43, 15/10/2026

Ninh yêu cầu hoàn tất toàn bộCM-01. BA/PM reviewer `/root/review_standards` xác nhận AC1/6 và seamsT01–06 khôngP0/P1; Tech reviewer `/root/review_spec` xác nhận hợp đồng singleton/CAS/acceptedAt/SMTP/hai CLI process đủ rõ. PM đối chiếu INFRA#47/#48QA_PASS và PlanioClosed, giảiHARD#47→#43; cập nhật JUnit6/MVC/secretfile/healthcontract trong thiết kế. Cấp READY_FOR_IMPLEMENTATION riêng#43; #44/#45 vẫn đợi QA ticket trước. Không suy QA từ tài liệu.

Dev kiểm theo seams trong test-spec đã review: CLI→PostgreSQL/Mailpit và browserentry; giữ mọi tài nguyên, khôngcleanup. Test trước implementation từng lát, reviewer và QA khác người code.

# Handoff CM-01: Thiết lập và đăng nhập quản lý

**Hiện hành 14/10/2026:** Ninh chọn [TS10–TS38](../../docs/requirements/tech-stack-round-2.md): Java Spring, JWT cookie/refresh/revocation và Redis throttle. Các mục bàn giao Next.js/Node/opaque session bên dưới là lịch sử; BA/PM đã cấp lại `BA_READY`, Tech độc lập/PM cấp `TECH_READY` cho CM-01 Java/JWT/Redis. Readiness #43–#45 còn chờ từng ticket; không có code CM-01 hoặc QA.

## CapmmBA reviewer — CM-01 / ba ticket Java #43–#45

**DONE:** Rà độc lập [#43](issues/01-send-first-setup-link.md), [#44](issues/02-complete-manager-setup.md), [#45](issues/03-manager-login-session.md) và [test spec Java](test-spec.md) với [spec 20 mục/AC1–AC7](spec.md), [CM01-Q1–Q3](qna.md), [TS10–TS38](../../docs/requirements/tech-stack-round-2.md), [thiết kế Java](technical-design-java.md) và [workflow](../../docs/workflows/capmm-end-to-end.md). #43 tham chiếu AC1/AC6; #44 AC2/AC4/AC5; #45 AC3/AC4/AC7, đều không quá năm mã. Dữ liệu thử bao phủ Mailpit, mốc 71:59/72:00, SMTP/CAS, hai JVM, Redis, JWT/cookie, CSRF/Origin, nhiều điện thoại và các nhánh Q2/Q3. #43 chờ #47 hoàn tất và `QA_PASS` INFRA-01 Java; #44/#45 chờ ticket trước hoàn tất và QA xác nhận phần liên quan, không chờ `QA_PASS` toàn Story CM-01. #22 đặt lại mật khẩu chỉ là phụ thuộc tương lai, không nằm trong AC/ticket.

**RESULT:** Review BA **không thấy P0/P1** hoặc AC/test data chặn ở ba ticket; không thấy hành vi sản phẩm mới bị gán cho Ninh. Một điểm P2 về câu chữ QA: [T06/T21](test-spec.md) viết “thiết bị thật khi có”, còn workflow yêu cầu case UI bắt buộc phải chạy trước `QA_PASS`; QA/PM cần đánh dấu `blocked`/`not-run` nếu thiết bị thật là điều kiện bắt buộc và chưa có, không suy `pass` từ viewport. Review này **không cấp `READY_FOR_IMPLEMENTATION`** cho ticket nào.

**NEED HUMAN:** CapmmPM và CapmmQA chốt cách ghi điều kiện thiết bị thật của T06/T21 trước khi xét `QA_PASS` Story; không có câu hỏi sản phẩm mới cần Ninh trả lời trong BA ticket review.

**PENDING:** CapmmTech rà hợp đồng Java từng ticket; CapmmPM xét readiness riêng sau khi kiểm phụ thuộc `HARD` và hai review. Dev/QA Java chưa chạy; #43 tiếp tục chờ INFRA-01 Java hoàn tất/`QA_PASS`, #44 và #45 chỉ mở theo kết quả ticket trước cùng QA phần liên quan.

**DETAIL:** [Ba ticket #43–#45](issues/01-send-first-setup-link.md), [test spec CM-J-T01–T21](test-spec.md), [spec](spec.md), [Q&A](qna.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Đây là review tài liệu; chưa có build, test run CM-01 Java hoặc cập nhật Planio ở lượt BA này.

— `/root/ba_review` trong vai CapmmBA reviewer độc lập

## CapmmTech reviewer độc lập — CM-01 / ticket Java #43–#45

**DONE:** Rà [#43](issues/01-send-first-setup-link.md), [#44](issues/02-complete-manager-setup.md), [#45](issues/03-manager-login-session.md) và [test spec Java](test-spec.md) với [thiết kế Java đã TECH_REVIEW_PASS](technical-design-java.md), [spec AC1–AC7](spec.md), [Q&A CM01-Q1–Q3](qna.md), ADR 0003 và TS10–TS38. Kiểm ranh giới ba lát dọc, số AC (2/3/3, đều ≤5), mốc `acceptedAt+72h`, singleton/transaction JPA, CSRF/edge/Redis, JWT family và logout-all, dữ liệu thử, ca đồng thời hai process/JVM và chuỗi `HARD` #47→#43→#44→#45. Không thấy vòng phụ thuộc QA cấp Story: QA xác nhận phần ticket trước để giải `HARD`; `QA_PASS` của Story chỉ sau #45 và regression T21.

**RESULT:** `TECH_TICKET_REVIEW_PASS` cho nội dung kỹ thuật cả ba ticket; không thấy P0/P1 Tech. #43 mô tả acceptedAt bất biến qua CAS và fail closed khi mốc không đáng tin; #44 kiểm đúng biên 71:59/72:00 và chỉ một manager; #45 tách refresh reuse thu hồi một family khỏi logout chủ động thu hồi mọi family. [Test spec](test-spec.md) có case CM-J-T01–T21 cho AC1–AC7, kể cả DB/Redis lỗi, Origin/CSRF, key rotation và race. Đây là review tài liệu; **không cấp `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED` hoặc `QA_PASS`**, không có build Java hay test ứng dụng đã chạy.

**NEED HUMAN:** None cho review Tech. PM vẫn xét readiness của từng ticket sau khi kiểm blocker; email/domain/thiết bị thật là đầu vào kiểm vận hành sau.

**PENDING:** #43 chờ #47 hoàn tất và QA xác nhận môi trường Java/Redis; #44 chờ phần #43 được QA xác nhận; #45 chờ phần #44 được QA xác nhận. PM/QA nên ghi rõ fixture cho T14 “tài khoản ghi chép” khi #04 chưa tồn tại (ví dụ test principal/JWT không phải manager nhưng ký bằng key thử), để case quyền chạy được mà không triển khai #04. Khi chạy T18–T19 ở giai đoạn #44 chỉ đánh phần setup; các nhánh login/refresh/logout chờ #45. T06/T13/T21 dùng viewport trước, thiết bị thật được ghi `not-run/blocked` nếu chưa có thay vì suy ra pass. Reviewer code và QA phải ghi commit/image, lệnh, môi trường và kết quả thực theo từng case.

**DETAIL:** [Thiết kế Java](technical-design-java.md), [review Tech Story](java-tech-review.md), [test spec](test-spec.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Các kết quả SQL/HTTP Node cũ không được dùng làm bằng chứng Java; ba ticket vẫn `blocked-by-hard` trên tài liệu hiện hành.

— `/root/cm01_tech_review` trong vai CapmmTech reviewer độc lập; không phải tác giả ticket hoặc thiết kế Java

## CapmmPM — CM-01 / TECH_READY Java-JWT-Redis

**DONE:** Đối chiếu [thiết kế Java §1–8](technical-design-java.md), [Tech review độc lập lần 3](java-tech-review.md), [spec AC1–AC7](spec.md), [CM01-Q1–Q3](qna.md) và TS10–TS38. Ba P1 đã được khép ở hợp đồng: `acceptedAt` ngay sau SMTP trả thành công rồi giữ mốc qua CAS, fail closed khi đồng hồ không kiểm được; logout chủ động bằng access còn hợp lệ được kiểm DB dưới khóa sẽ thu hồi mọi family, stale refresh đơn lẻ chỉ family; HS256/keyring/kid/rotate có lựa chọn cụ thể. PM chấp nhận **đề xuất Tech** family refresh có hạn tuyệt đối 7 ngày từ login, rotation không gia hạn, để kỳ hạn đã chọn có giới hạn rõ; đây không được ghi là lựa chọn cụ thể của Ninh. PM chấp nhận quota manager 10 lần/15 phút/IP, 60 lần/15 phút/tài khoản, setup 10 lần/15 phút/IP tính cả thành công; 429 có Retry-After, phiên đang dùng không bị chặn. Redis lỗi làm login/setup 503 trước hash/ghi DB, phiên đã có tiếp tục theo DB. Đây là chính sách Tech/PM cho CM-01 một quản lý; #04 nhiều người chung NAT phải rà quota riêng, không tái dùng mù.

**RESULT:** `TECH_READY` **đạt cho CM-01 Java/JWT/Redis ngày 14/10/2026**; không có P0/P1 Tech còn mở trong phạm vi Story. Đây là chốt hợp đồng thiết kế và tác động PM, **không cấp** `READY_FOR_IMPLEMENTATION` cho #43–#45 hoặc `QA_PASS`. Hạn family 7 ngày tuyệt đối là cách hiện thực giới hạn TS32, không khẳng định Ninh đã chọn cách tính đó; nếu phát sinh kỳ vọng phiên phải được gia hạn quá 7 ngày, quay BA hỏi Ninh trước khi đổi.

**NEED HUMAN:** None để thiết kế/test với dữ liệu riêng. Email/domain/SMTP thật và thiết bị thật chỉ cần khi đến gate vận hành/phát hành; không dùng chúng để tuyên bố QA hiện tại.

**PENDING:** PM rebaseline ba ticket và test spec từ Node/opaque session sang Java/JWT/Redis; BA/Tech rà từng ticket, PM xét readiness riêng sau khi kiểm `HARD` #47→#43→#44→#45. #47 chỉ giải chặn #43 khi hoàn tất/QA_PASS. Dev/QA phải chứng minh Boot/springdoc/CSRF/Caddy, race hai JVM, Redis và cookie trên build thật.

**DETAIL:** [Thiết kế](technical-design-java.md), [review](java-tech-review.md), [spec](spec.md), [Q&A](qna.md), [ticket](issues/01-send-first-setup-link.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Chưa chạy Java/Compose/QA CM-01.

— `/root/pm_ba_rebaseline` trong vai CapmmPM sau reviewer Tech độc lập

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

## PM — CM01-Q4, 15/10/2026

Ninh chọn Chromium/WebKit giả lập để qua QA từng ticket, giữ thiết bị thật ở nghiệm thu cuối CM-01; xem qnaCM01-Q4. QA43T06 dùng hai engine/viewport, ghi rõ emulation. Không còn blocker thiết bị thật cho riêng#43–45. Gate Story cuối vẫn cần iPhone/Android thật; tiếp tục mọi công việc độc lập trước đó.

## PM — DEV_REVIEWED #43, 15/10/2026

Đối chiếu đúngAC1/6, Dev backend `/root/review_spec`, Devweb `/root`; backendSpec reviewer `/root`, Standards toàncode/webSpec reviewer `/root/review_standards` khôngP0/P1. Freshintegration SMTP/PostgreSQL/twoJVM/Mailpitdigest và clock/CAS targetedtests green, webbuild +hai engineviewportgreen. Fullsuite cũ có positiveclockfixturefail doVMahead87ms; fixfixturekhôngđổi guard đãgreen riêng; QA phải rerunfullsnapshot và kiểm actualDBloss. Cấp DEV_REVIEWED riêng#43 đểQA thực thi, chưaQA_PASS/#44Ready. Xem dev43-evidence.md/code-review43.md/qa43-plan.md.

## PM — review nội dung #44/#45, 15/10/2026

BA `/root/review_standards` vàTech `/root/review_spec` xác nhận nội dung review14/10 vẫn phù hợpAC/data/seams sauMVC/JUnit6/Q4, khôngP0/P1. Chỉnh statusreference#47 vàmigrationimmutability/cookiefallback trước giaoDev. ChưaREADY#44/#45; chỉ cấp khiQA tickettrước giảiHARD. FixtureT14 khácrole chỉtestprincipal, không làmStory#04.

## PM — QA_PASS ticket #43 và READY_FOR_IMPLEMENTATION #44, 15/10/2026

PM đối chiếu qa43-evidence.md và artifacts: T01–06PASS/AC1+6đạt trênf63efe4, actualCLI/Mailpit/twoJVM/DBloss/slowproxy và2engineviewport; startupretryexit1 được giữ rõ, safeoperatorretryđạt. CM01-Q4cho emulationtừngticket, physicaldevices cònStoryfinal. Cấp QA_PASS **ticket43**, chưaStory.

#44 đủAC2/4/5/data/seams, BA `/root/review_standards` vàTech `/root/review_spec` reviewnội dung lại15/10 khôngP0/P1; HARD43 đã giải. PM cấp READY_FOR_IMPLEMENTATION riêng#44. Dev theo thiết kếJava/JUnit6/MVC, migrationaccountmới khôngsửa001bootstrap. #45chờ QA44.
