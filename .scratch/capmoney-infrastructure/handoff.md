# Handoff INFRA-01

**Hiện hành15/10/2026:** INFRA-01/#47 Java đạt DEV_REVIEWED và QA_PASS trên code310ad7f,8/8 case độc lập. Ninh đã chọnJUnit6 tại IF-Q6, giữJava25/Boot4.1. Chưa đồng bộ Planio trong phiên này vì chưa cóAPI key; không khẳng định issueClosed. #43 chưa được giaoDev, cần PM xét readiness riêng.

## CapmmPM — INFRA-01/#47 QA_PASS sau Ninh giải IF-Q6

**DONE:** Đọc đủ IF-J-T01–08 tại [QA Java](qa-evidence-java.md) và35 artifact sanitised trong evidence/java-final, đối chiếu5 AC, review hai trục và code310ad7f/imagecf524a3c1e829ad0bc34b774b6f08f5c647dcf7ca1ef2e0a1b477649400ff949. Reviewer độc lập đã đóng P1 silentDB; QA chạy stop/start/pause cùngPID, ingress, network/secret, marker, Mailpit, Spring Boot/Testcontainers và stopped-start regression. Ninh chọn A qua giao diện15/10: JUnit6, giữJava25/Boot4.1; cập nhật TS36/thiết kế. Spec reviewer xác nhận gap đã giải, code không thay nên không test lặp thuần tài liệu.

**RESULT:** `DEV_REVIEWED` và `QA_PASS` đạt riêng INFRA-01/#47 ngày15/10/2026. Toàn bộ5 AC có case thực chạy; không còn case bắt buộc fail/blocked/not-run. Đây là nghiệm thu hạ tầng dev/test, không phải sản phẩm hoặc CM-01. IF-Q6 đã giải.

**NEED HUMAN:** Chỉ còn quyền truy cập Planio an toàn để đồng bộ tracker; phiên này không cóAPI key, không yêu cầu gửi key vào chat. Không còn câu hỏi kỹ thuật chặn nghiệm thu #47.

**PENDING:** Đồng bộ bằng chứng/trạng thái #46/#47 trên Planio khi có quyền truy cập. #43 cần PM kiểm readiness riêng trước Dev; chưa có code CM-01. Yêu cầu mới về bố cục BE đang lập đề xuất, chưa chuyển thành cấu trúc được Ninh chốt hoặc thay đổi snapshot đã QA.

**DETAIL:** [TS36/IF-Q6](../../docs/requirements/tech-stack-round-2.md), [review](code-review-java.md), [QA](qa-evidence-java.md), [test spec](test-spec.md). Mọi tài nguyên giữ nguyên; trạng thái Closed của Planio chưa được xác minh/cập nhật.

— Codex `/root` trong vai CapmmPM; reviewers `/root/review_standards`, `/root/review_spec`; QA độc lập `/root/java_qa`

## Lịch sử trước câu trả lời — IF-Q6 chờ quyết định tương thích JUnit

**DONE:** QA độc lập đã chạy IF-J-T01–08 thành công trên `310ad7f`; P1 timeout đã sửa/retest. Rà dependency cuối cùng cùng reviewer Tech phát hiện TS36 chọnJUnit5 nhưng Boot4.1.1 BOM/test classpath thực tếJUnit6.0.3. Spring7 nâng baselineJUnit6, nên không tự downgrade hay coi Ninh đã duyệtJUnit6. [Quyết định đang chờ](../../docs/requirements/tech-stack-round-2.md), [Q&A IF-Q6](qna.md), [review](code-review-java.md), [QA execution](qa-evidence-java.md).

**RESULT:** `TEST_EXECUTION_PASS` 8/8 case hạ tầng trên buildJUnit6. **Gate cuối #47 giữ chờ IF-Q6**; kết luận DEV_REVIEWED ở mục trước cần bổ sung điều kiện tương thích này, chưa dùng để đóng #47 hoặc cấp Story QA_PASS. Không hủy bằng chứng chạy hoặc kết luận đã đóng P1 timeout.

**NEED HUMAN:** Ninh chọn A giữBoot4.1/Java25 và dùngJUnit6, hoặc B giữJUnit5 rồi review lại stack. Đây là thay đổi lựa chọn kỹ thuật đã chốt, không phải hỏi lại quyền thao tác. Chưa có API key Planio trong phiên để đồng bộ tracker; không yêu cầu gửi key vào chat.

**PENDING:** Sau câu trả lời, cập nhật TS36/thiết kế/gate tương ứng; nếu chọnA, đối chiếu đúng snapshot đã chạy và PM chốt. Nếu chọnB, Tech rebaseline trước code tiếp. #43 chưa giaoDev. Giữ mọi tài nguyên đã tạo.

**DETAIL:** Code310ad7f; toàn bộ evidence/review đã chuẩn bị, chỉ còn lựa chọnJUnit và đồng bộ Planio bên ngoài phiên.

— Codex `/root` trong vai CapmmPM; Spec reviewer `/root/review_spec`; QA độc lập `/root/java_qa`

## CapmmPM — INFRA-01.1 / #47 DEV_REVIEWED Java

**DONE:** Đối chiếu snapshot code `310ad7f`, [Dev evidence Java](dev-evidence-java.md) và [code review hai trục](code-review-java.md) trên toàn diff từ `b1aa9f4`. Standards reviewer `/root/review_standards` không có blocker; Spec reviewer `/root/review_spec` đã đóng P1 timeout PostgreSQL sau QA độc lập chạy green. Phạm vi chỉ infrastructure placeholder/health/readiness/SMTP test/network; không có tài khoản, schema nghiệp vụ, tiền hoặc production. Quyền bảo toàn tài nguyên được áp dụng vào scripts, wrapper và Testcontainers; phương pháp thay thế reset/recreate đã được Tech reviewer chấp nhận.

**RESULT:** `DEV_REVIEWED` đạt cho #47 Java trên `310ad7f` ngày 15/10/2026. P1 silent-DB được tái hiện trước sửa và retest sau sửa: health503 ~2.012s, readiness503 ~1.007s, tự phục hồi cùng PID/StartedAt. `QA_PASS` còn chờ tổng hợp đầy đủ IF-J-T01–08.

**NEED HUMAN:** Chưa có API key Planio trong phiên để đồng bộ trạng thái khi hoàn tất; không yêu cầu Ninh gửi key vào chat. Không chặn QA cục bộ.

**PENDING:** QA độc lập hoàn tất tám case trên image/commit cuối, PM kiểm evidence rồi mới kết luận QA_PASS. #43 chưa mở readiness. Không đóng issue Planio khi chưa đồng bộ qua API.

**DETAIL:** Code `310ad7f`, [review](code-review-java.md), [Dev evidence](dev-evidence-java.md), [QA evidence Java](qa-evidence-java.md), [test spec](test-spec.md). Trạng thái Planio được ghi ở lượt trước là In Progress; phiên này chưa đọc lại hoặc thay đổi Planio.

— Codex `/root` trong vai CapmmPM; reviewers `/root/review_standards`, `/root/review_spec`; tester độc lập `/root/java_qa`

## CapmmPM — INFRA-01.1 / #47 READY_FOR_IMPLEMENTATION Java-Redis

**DONE:** Đối chiếu [ticket #47 Java](issues/01-docker-dev-test.md) và [test spec IF-J-T01–T08](test-spec.md) với `BA_READY`/`TECH_READY` cấp Story, đúng 5 mã AC, hợp đồng V1–V8, dữ liệu thử, không có `HARD` blocker khởi động và hai [review BA/Tech độc lập](handoff.md). Giải P2 TS22: Ninh đã chọn TanStack Query + fetch, nhưng #47 chỉ có web placeholder không fetch dữ liệu sản phẩm; [thiết kế §2](technical-design-java.md) và ticket nay ghi pin/cài Query ở FE ticket đầu tiên gọi API. Không thêm hành vi hay AC mới.

**RESULT:** `READY_FOR_IMPLEMENTATION` **đạt cho #47 Java/Redis ngày 14/10/2026**; ticket `ready-for-agent`. Không còn P0/P1 ticket đã biết, P2 TS22 đã giải. `DEV_REVIEWED`/`QA_PASS` **chưa đạt**; Planio #47 đã đồng bộ `In Progress` và Dev đang làm.

**NEED HUMAN:** None cho #47 dev/test; không cần VPS/domain/SMTP thật. TS22 vẫn là quyết định đã xác nhận, chỉ dời lúc cài dependency tới FE ticket thích hợp.

**PENDING:** Dev chạy V1–V8 với commit/image xác định, reviewer code độc lập và PM rà, QA thực chạy IF-J-T01–T08. #47 chỉ giải cạnh HARD tới CM-01 #43 khi hoàn tất/QA_PASS, không từ readiness.

**DETAIL:** [Ticket](issues/01-docker-dev-test.md), [test spec](test-spec.md), [thiết kế](technical-design-java.md), [spec](spec.md), [Story](story.md), [TS22](../../docs/requirements/tech-stack-round-2.md). Đây là PM review tài liệu/gate; không chạy Java hoặc cập nhật Planio trong lượt này.

— `/root/pm_ba_rebaseline` trong vai CapmmPM sau BA/Tech ticket review độc lập

## CapmmTech reviewer — INFRA-01.1 / ticket #47 Java

**DONE:** Rà độc lập [ticket #47](issues/01-docker-dev-test.md) và [test spec IF-J-T01–T08](test-spec.md) với [spec 20 mục/AC-IF01-1–5](spec.md), [thiết kế Java V1–V8](technical-design-java.md) đã `TECH_READY`, [ADR 0003](../../docs/adr/0003-java-spring-and-deployment.md), TS10–TS38 và workflow. Năm AC được dẫn đúng, dữ liệu thử và phụ thuộc khởi động rõ, không đưa auth/tiền/production vào #47. Test bao phủ route, health/readiness và cùng PID qua outage, edge secret/peer/Origin, probe chỉ ở test image, volume reset cô lập và Mailpit lỗi/timeout; mọi case vẫn `not-run` đúng thực tế.

**RESULT:** Tech review ticket **không thấy P0/P1 chặn**; một P2 về truy vết TS22. Web #47 chỉ là placeholder và chưa tải dữ liệu sản phẩm bằng TanStack Query, nên **có thể hoãn pin/cài TanStack Query tới ticket FE đầu tiên thực sự gọi API**, không cần đưa dependency chưa dùng vào #47. Tuy nhiên [technical-design-java.md §2](technical-design-java.md) hiện nói pin Query ngay; PM cần sửa câu này hoặc bổ sung ghi chú hoãn trong ticket/thiết kế để hai nguồn nhất quán trước khi xét readiness. Review này không cấp `READY_FOR_IMPLEMENTATION` hoặc xác nhận Java đã build/chạy.

**NEED HUMAN:** None; TS22 đã được Ninh chọn, đây chỉ là phân bổ dependency theo ticket, PM quyết trong phạm vi đã chốt.

**PENDING:** PM xử lý P2 TS22 và xét `READY_FOR_IMPLEMENTATION` riêng cho #47; sau đó Dev ghi build/V1–V8, reviewer code khác người làm và QA chạy độc lập trên commit/image xác định. Không lấy kết quả Node/Hono cũ làm pass.

**DETAIL:** [Ticket](issues/01-docker-dev-test.md), [test spec](test-spec.md), [spec](spec.md), [thiết kế](technical-design-java.md), [Tech review](java-tech-review.md), [TS22](../../docs/requirements/tech-stack-round-2.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Review tài liệu, chưa code, test hay cập nhật Planio trong lượt này.

— `/root/tech_adr_rereview` trong vai CapmmTech reviewer độc lập

## CapmmBA reviewer — INFRA-01.1 / ticket #47 Java

**DONE:** Rà độc lập [ticket #47](issues/01-docker-dev-test.md) và [test spec Java](test-spec.md) với [spec 20 mục](spec.md), năm AC, TS10–TS38, [thiết kế V1–V8](technical-design-java.md) và [workflow](../../docs/workflows/capmm-end-to-end.md). Ticket tham chiếu đúng năm mã AC, có hợp đồng, dữ liệu thử, phụ thuộc, rủi ro và người nhận/reviewer. IF-J-T01–T08 bao phủ cả năm AC và tám chiều QA; dữ liệu marker PostgreSQL, email `manager@example.test`, secret/project dev-test riêng, origin loopback và bằng chứng outage/peer/Origin/SMTP được nêu. Web `/manager/setup` chỉ là placeholder; probe chỉ dành cho test. Không thấy hành vi tài khoản, JWT/CSRF sản phẩm, giao dịch hoặc production bị đưa vào #47.

**RESULT:** Review BA ticket **không thấy P0/P1 hoặc thiếu AC/test data chặn**. Có một điểm P2 về truy vết TS22: [thiết kế Java §2](technical-design-java.md) yêu cầu pin TanStack Query theo lựa chọn stack, nhưng ticket #47 chỉ liệt kê React/Vite/Router/Tailwind cho web placeholder. PM/Tech cần làm rõ pin Query trong #47 hay ghi rõ phần đó thuộc ticket FE sau; không biến thành hành vi sản phẩm hoặc AC mới. BA review này **không cấp `READY_FOR_IMPLEMENTATION`**; #47 vẫn `needs-triage`, chờ Tech review ticket và PM xét gate.

**NEED HUMAN:** CapmmPM và CapmmTech phân xử điểm P2 TS22 khi rà ticket #47; không có câu hỏi sản phẩm cần Ninh trả lời.

**PENDING:** CapmmTech rà hợp đồng Java và P2 TS22; CapmmPM đối chiếu hai review, quyết định `READY_FOR_IMPLEMENTATION` của #47 và cập nhật ticket nếu cần. Dev/QA Java chưa chạy; không kế thừa kết quả Node/Hono.

**DETAIL:** [Ticket](issues/01-docker-dev-test.md), [test spec IF-J-T01–T08](test-spec.md), [spec AC-IF01-1–5](spec.md), [TS10–TS38](../../docs/requirements/tech-stack-round-2.md), [thiết kế V1–V8](technical-design-java.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Đây là review tài liệu; chưa có build, test run hoặc cập nhật Planio trong lượt BA này.

— `/root/ba_review` trong vai CapmmBA reviewer độc lập

## CapmmPM — INFRA-01 / Tech gate Java-Redis và ticket #47

**DONE:** Đối chiếu [technical-design-java.md](technical-design-java.md) §1–7 và V1–V8 với [spec 20 mục/5 AC](spec.md), TS10–TS38, [ADR 0003](../../docs/adr/0003-java-spring-and-deployment.md), [Tech review độc lập](java-tech-review.md). Reviewer khác người thiết kế đã kiểm lại P1 outage: PostgreSQL/Redis mất sau startup phải trả HTTP 503 qua cùng JVM/PID, không 502 hay restart loop; phục hồi tự động. Secret, peer/header, Origin probe test-only, Mailpit, volume và phân tách production có phép kiểm. PM xác nhận INFRA chỉ dựng Java health/readiness + web placeholder, không làm CM-01 hay VPS. Rebaseline [ticket #47](issues/01-docker-dev-test.md) và [test spec Java](test-spec.md), giữ 5 mã AC.

**RESULT:** `TECH_READY` **đạt cho INFRA-01 Java/Redis ngày 14/10/2026** trên thiết kế và review nêu trên; không còn P0/P1 thiết kế đã biết. `READY_FOR_IMPLEMENTATION` của #47 **chưa cấp** tại lượt này: BA và Tech cần rà bản ticket Java mới theo workflow, rồi PM xét độc lập. Không có code/build/QA Java; Node/Hono IF-T03 fail vẫn là lịch sử.

**NEED HUMAN:** None cho thiết kế dev/test. Không suy rằng Ninh đã chọn Redis cùng VPS; đó là đề xuất Tech cho production sau.

**PENDING:** BA/Tech rà ticket #47 và test data/dependency, PM chốt readiness rồi giao Dev. Dev pin bản vá, build/test Java và ghi V1–V8; reviewer code và QA độc lập kiểm trên image/commit xác định. #47 hoàn tất mới giải cạnh HARD tới CM-01 #43.

**DETAIL:** [Spec](spec.md), [thiết kế](technical-design-java.md), [Tech review addendum](java-tech-review.md), [ticket](issues/01-docker-dev-test.md), [test spec](test-spec.md), [Story](story.md). Đây là review tài liệu; chưa chạy Java hay cập nhật Planio trong lượt này.

— `/root/pm_ba_rebaseline` trong vai CapmmPM sau Tech review

## CapmmPM — INFRA-01 / BA gate Java-Redis

**DONE:** Rà độc lập [spec 20 mục](spec.md) sau BA rebaseline, đối chiếu IF-Q1–IF-Q5, TS01/TS04/TS06–TS08 và TS10–TS38 với AC-IF01-1–5. Mục không có nghiệp vụ sản phẩm nêu rõ lý do không áp dụng. Actor, luồng bình thường/lỗi, ranh giới dev/test, volume không phải backup, kiểm DB/Redis outage và thư thử đều có tiêu chí quan sát được. Bản Node/Hono/IF-T03 fail được phân biệt rõ là lịch sử.

**RESULT:** `BA_READY` **đạt cho INFRA-01 Java/Redis ngày 14/10/2026**; không thấy P0/P1 BA còn mở. Đây là review tài liệu, không cấp `TECH_READY`, `READY_FOR_IMPLEMENTATION` cho #47 hay `QA_PASS`.

**NEED HUMAN:** None cho đặc tả môi trường dev/test; VPS/domain/SMTP thật là điều kiện vận hành sau.

**PENDING:** Reviewer Tech độc lập kiểm lại ADR 0003, Tech lập thiết kế INFRA Java, PM kiểm `TECH_READY`; sau đó mới xét ticket #47, chạy build/QA Java. Với AC-IF01-3, Tech phải chứng minh test-only probe không lọt vào sản phẩm và kiểm được peer/header/Origin thực.

**DETAIL:** [Spec mục 1–20](spec.md), [Q&A](qna.md), [nguồn TS10–TS38](../../docs/requirements/tech-stack-round-2.md), [Story](story.md). Không có build/test Java hay email thật tại thời điểm review.

— `/root/pm_ba_rebaseline` trong vai CapmmPM độc lập, khác người lập BA

## CapmmBA — INFRA-01 / rebaseline Java-Redis

**DONE:** Đối chiếu TS10–TS38 với spec 20 mục, sửa AC-IF01-1–5 cho Java Spring API, Redis dev/test, PostgreSQL/Mailpit/Caddy/React placeholder, lỗi DB/Redis và network/Origin probe. CSRF sản phẩm thuộc CM-01; ghi [Q&A](qna.md) tách lựa chọn Ninh đã xác nhận khỏi kiến trúc Tech đề xuất.

**RESULT:** BA draft đã cập nhật; **chưa `BA_READY` cho Java** vì chưa có PM review độc lập. Gate `TECH_READY` cũ và readiness #47 cũ chỉ là lịch sử Node/Hono. ADR 0003 đã có ba P1 từ reviewer, bản sửa cần review lại.

**NEED HUMAN:** None cho đặc tả dev/test. VPS/domain/SMTP thật và rủi ro chưa backup chỉ cần đối chiếu trước vận hành production.

**PENDING:** PM đối chiếu AC/nguồn và cấp hoặc trả `BA_READY`; reviewer Tech rà lại ADR, Tech cập nhật technical-design Java, PM kiểm `TECH_READY`; sau đó PM sửa ticket #47/test spec và xét `READY_FOR_IMPLEMENTATION`.

**DETAIL:** [Spec Java/Redis](spec.md), [ADR 0003 đề xuất](../../docs/adr/0003-java-spring-and-deployment.md), [Tech review](java-tech-review.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Chưa build/test Java.

— Codex `/root` trong vai CapmmBA; chờ PM độc lập review

## CapmmBA — INFRA-01

**DONE:** Lập [story](story.md), [Q&A](qna.md) và [spec 20 mục](spec.md) từ TS01–TS09/ADR 0002; AC-IF01-1–5 kiểm được.

**RESULT:** `BA_READY` đã đạt cho spec Node/Hono trước TS10; **tạm dừng hiệu lực** cho Java Spring, BA/PM phải rà lại 20 mục và AC.

**NEED HUMAN:** None cho đặc tả dev/test; VPS/domain/SMTP thật là đầu vào giai đoạn sau.

**PENDING:** Cập nhật spec từ TS10–TS38 sau ADR Java, rồi PM review.

**DETAIL:** [ADR 0002 lịch sử](../../docs/adr/0002-project-infrastructure-and-stack.md), [quyết định mới](../../docs/requirements/tech-stack-round-2.md), [workflow](../../docs/workflows/capmm-end-to-end.md).

— Codex `/root` trong vai CapmmBA

## CapmmTech — INFRA-01

**DONE:** Lập [technical-design](technical-design.md) theo ADR 0002; reviewer độc lập `/root/infra_tech_review` rà không P0/P1, nêu P2 về TCP peer thật/IP Caddy cố định/Origin probe đã đưa vào hợp đồng. PM độc lập `/root/cm01_pm_ba` yêu cầu bổ sung V4 Origin và tách nghĩa vụ hai API instance sang CM-01; đã sửa và được rà lại.

**RESULT:** `TECH_READY` đã đạt cho Node/Hono trước TS10; **tạm dừng hiệu lực**. Java Spring/JPA/Liquibase/JWT/Redis/Tailwind/direct VPS cần ADR và technical-design mới, reviewer Tech độc lập và PM.

**NEED HUMAN:** None cho môi trường dev/test; không cần VPS/domain/SMTP thật.

**PENDING:** Tech lập ADR thay thế và cập nhật thiết kế INFRA-01/CM-01; PM kiểm trước khi chia/giao ticket Java. CM-01 #43 tiếp tục chờ.

**DETAIL:** [spec](spec.md), [technical-design](technical-design.md), [ADR 0002](../../docs/adr/0002-project-infrastructure-and-stack.md), [architecture](architecture.md).

— Codex `/root` trong vai CapmmTech; reviewers `/root/infra_tech_review`, `/root/cm01_pm_ba`

## CapmmPM — INFRA-01.1 / Planio #47

**DONE:** Tạo và đọc lại [parent #46](https://tanninh.planio.com/issues/46), [child #47](https://tanninh.planio.com/issues/47), quan hệ #47 `blocks` #43 (relation #8). PM độc lập `/root/cm01_pm_ba` và Tech `/root/infra_tech_review` rà ticket đủ 5 AC, hợp đồng, dữ liệu thử, phụ thuộc. Hai trục `code-review` độc lập: `/root/infra_review_standards` rà chuẩn repo, `/root/infra_review_spec` rà AC-IF01-1–5; không P0/P1, các P2 về origin/secret/probe/Pool/trạng thái tài liệu/nhãn #46–#47 đã sửa và được kiểm lại. PM đối chiếu commit `493f3d4` với ticket #47: chỉ placeholder, health, Mailpit/PG/Caddy dev/test; không có CM-01 app logic hay production deploy. `git diff --cached --check` qua trước commit; build/test ở [dev evidence](dev-evidence.md).

**RESULT:** `READY_FOR_IMPLEMENTATION` và `DEV_REVIEWED` đã đạt **chỉ cho bản Node/Hono** ngày 14/10/2026 trên commit `493f3d4`. Sau TS10–TS38, **không còn hiệu lực cho #47 Java**; Planio #46/#47 `Open`. QA cũ IF-T03 fail; không có `QA_PASS`. Không mở gate CM-01 #43.

**NEED HUMAN:** None cho Docker dev/test.

**PENDING:** PM chờ BA/Tech rebaseline theo quyết định mới; sau đó xét lại ticket và gate. Không đóng #47/#46 từ bằng chứng Node/Hono.

**DETAIL:** [story](story.md), [spec](spec.md), [technical-design](technical-design.md), [ticket](issues/01-docker-dev-test.md), [test spec](test-spec.md), [dev evidence](dev-evidence.md), commit `493f3d4`.

— Codex `/root` trong vai CapmmPM; reviewers `/root/cm01_pm_ba`, `/root/infra_tech_review`, `/root/infra_review_standards`, `/root/infra_review_spec`

## CapmmDev — INFRA-01.1 / Planio #47

**DONE:** Triển khai Docker dev/test và self-check trên commit `493f3d4`; [dev evidence](dev-evidence.md) ghi TypeScript, 10 Vitest, 4 guard tests, build runtime/probe, Compose route/port và Mailpit. Sửa IP Caddy trùng API, ràng buộc APP_ORIGIN, secret dev/test riêng và cấm probe ở dev.

**RESULT:** Dev bàn giao bản Node/Hono; commit `493f3d4` là lịch sử. QA IF-T03 fail; patch P1 sau đó chưa commit và không chuyển thành Java. Không có `QA_PASS`.

**NEED HUMAN:** None.

**PENDING:** Chờ ticket Java đủ gate mới; không tiếp tục sửa API Node/Hono.

**DETAIL:** [ticket](issues/01-docker-dev-test.md), [dev evidence](dev-evidence.md), commit `493f3d4`.

— `/root/infra_dev` trong vai CapmmDev

## CapmmQA — INFRA-01.1 / Planio #47

**DONE:** QA độc lập `/root/infra_qa` đã chạy một phần IF-T01–08 trên Node/Hono commit `493f3d4`, lưu [qa evidence](qa-evidence.md) và trạng thái từng case trong [test spec](test-spec.md). Dừng khi Ninh đổi BE sang Java Spring; stack cũ đã được dừng, volume thử giữ lại.

**RESULT:** **Không `QA_PASS`**. IF-T03 fail P1: PostgreSQL dừng làm API thoát và health 502, không tự phục hồi; IF-T02/04 pass trên build cũ, các case khác partial/not-run. Kết quả Node/Hono không dùng cho Java.

**NEED HUMAN:** None để ghi nhận lỗi; các lựa chọn công nghệ đã được Ninh trả lời TS10–TS38.

**PENDING:** Sau ADR/spec/ticket Java qua gate và có build xác định, QA lập/điều chỉnh test spec rồi chạy lại toàn bộ AC.

**DETAIL:** [qa evidence](qa-evidence.md), [test spec](test-spec.md), [quyết định mới](../../docs/requirements/tech-stack-round-2.md).

— `/root/infra_qa` trong vai CapmmQA; Codex `/root` tổng hợp bàn giao
