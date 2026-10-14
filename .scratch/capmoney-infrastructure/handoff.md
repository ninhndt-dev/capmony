# Handoff INFRA-01

**Hiện hành 14/10/2026:** Ninh chọn [TS10–TS38](../../docs/requirements/tech-stack-round-2.md); stack Node/Hono và gate #47 cũ bị tạm dừng. Planio #46/#47 đã chuyển `Open`, QA IF-T03 fail trước khi đổi stack. Các kết quả cũ dưới đây là bằng chứng lịch sử, không cấp readiness cho Java.

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
