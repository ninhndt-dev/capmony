# Handoff INFRA-01

## CapmmBA — INFRA-01

**DONE:** Lập [story](story.md), [Q&A](qna.md) và [spec 20 mục](spec.md) từ TS01–TS09/ADR 0002; AC-IF01-1–5 kiểm được.

**RESULT:** `BA_READY` **đạt ngày 14/10/2026**: PM độc lập `/root/cm01_pm_ba` yêu cầu nêu Origin thiếu/sai trong AC-IF01-3, BA đã sửa; PM rà lại không còn P0/P1. Chưa có build/QA.

**NEED HUMAN:** None cho đặc tả dev/test; VPS/domain/SMTP thật là đầu vào giai đoạn sau.

**PENDING:** PM tạo ticket Compose sau Tech gate; Dev/QA chạy V1–V6 khi ticket riêng ready.

**DETAIL:** [ADR 0002](../../docs/adr/0002-project-infrastructure-and-stack.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Chưa chạy Docker.

— Codex `/root` trong vai CapmmBA

## CapmmTech — INFRA-01

**DONE:** Lập [technical-design](technical-design.md) theo ADR 0002; reviewer độc lập `/root/infra_tech_review` rà không P0/P1, nêu P2 về TCP peer thật/IP Caddy cố định/Origin probe đã đưa vào hợp đồng. PM độc lập `/root/cm01_pm_ba` yêu cầu bổ sung V4 Origin và tách nghĩa vụ hai API instance sang CM-01; đã sửa và được rà lại.

**RESULT:** `TECH_READY` **đạt ngày 14/10/2026 ở mức thiết kế** sau `BA_READY`. Cho phép PM tạo ticket Compose; V1–V6, Docker build và QA chưa chạy.

**NEED HUMAN:** None cho môi trường dev/test; không cần VPS/domain/SMTP thật.

**PENDING:** PM đã chia ticket #47 trên Planio; Dev dựng/kiểm Compose, reviewer code độc lập và QA chạy trên build xác định. CM-01 #43 tiếp tục chờ rebaseline riêng.

**DETAIL:** [spec](spec.md), [technical-design](technical-design.md), [ADR 0002](../../docs/adr/0002-project-infrastructure-and-stack.md), [architecture](architecture.md).

— Codex `/root` trong vai CapmmTech; reviewers `/root/infra_tech_review`, `/root/cm01_pm_ba`

## CapmmPM — INFRA-01.1 / Planio #47

**DONE:** Tạo và đọc lại [parent #46](https://tanninh.planio.com/issues/46), [child #47](https://tanninh.planio.com/issues/47), quan hệ #47 `blocks` #43 (relation #8). PM độc lập `/root/cm01_pm_ba` rà ticket [01](issues/01-docker-dev-test.md): không có P0/nội dung P1, đủ 5 AC, hợp đồng, dữ liệu thử, phụ thuộc và reviewer. Tech độc lập `/root/infra_tech_review` xác nhận không P0/P1 kỹ thuật; góp ý P2 cho IF-T05 đã sửa trong [test spec](test-spec.md).

**RESULT:** `READY_FOR_IMPLEMENTATION` **đạt cho #47 ngày 14/10/2026**; ticket `ready-for-agent`, Planio `In Progress` khi DevOps bắt đầu. Không mở gate CM-01 #43. Chưa có `DEV_REVIEWED` hoặc `QA_PASS`.

**NEED HUMAN:** None cho Docker dev/test.

**PENDING:** DevOps thực hiện đúng #47; reviewer code khác người thực hiện, PM kiểm phạm vi và QA độc lập chạy V1–V6 trên build xác định.

**DETAIL:** [story](story.md), [spec](spec.md), [technical-design](technical-design.md), [ticket](issues/01-docker-dev-test.md), [test spec](test-spec.md).

— Codex `/root` trong vai CapmmPM; reviewer độc lập `/root/cm01_pm_ba`, `/root/infra_tech_review`
