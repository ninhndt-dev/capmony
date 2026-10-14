# Story INFRA-01: Môi trường Docker dev/test

**Lifecycle:** planning
**Planio:** [#46 – INFRA-01](https://tanninh.planio.com/issues/46), issue hạ tầng riêng; [#47 – INFRA-01.1](https://tanninh.planio.com/issues/47) là ticket DevOps cấp dưới. Đã tạo và đọc lại ngày 14/10/2026.
**Blocked by:** ADR 0002 đã accepted ở mức kiến trúc; không có Story sản phẩm chặn.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** React web + API riêng (TS01), Docker cho dev/test (TS06), kiến trúc và review trước Docker/FE/BE (TS07), PostgreSQL volume ở VPS khi chạy thật (TS08), tiếp tục theo ADR 0002 (TS09). [Nhật ký quyết định](../../docs/requirements/tech-stack-round-1.md).
- **Phạm vi:** môi trường Docker Compose dev/test cô lập với Caddy, web placeholder tối thiểu, API health placeholder tối thiểu, PostgreSQL volume và Mailpit. [Spec 20 mục](spec.md) định nghĩa AC-IF01-1–AC-IF01-5. Không triển khai hành vi CM-01 hoặc dữ liệu tài chính trong ticket này.
- **Tech đã đề xuất và Ninh duyệt bundle:** [ADR 0002](../../docs/adr/0002-project-infrastructure-and-stack.md), [sơ đồ](architecture.md).

## Gate và bằng chứng

| Gate | Hiện tại | Bằng chứng | Reviewer |
| --- | --- | --- | --- |
| BA_READY | **Đạt 14/10/2026**: 20 mục/5 AC/TS01–TS09, P1 Origin đã sửa | [spec](spec.md), [Q&A](qna.md), [handoff](handoff.md) | `/root/cm01_pm_ba` độc lập |
| TECH_READY | **Đạt 14/10/2026 ở mức thiết kế**: ranh giới mạng/route/IP/Origin/DB/Mailpit và V1–V6 đủ để giao Compose | [ADR 0002](../../docs/adr/0002-project-infrastructure-and-stack.md), [technical-design](technical-design.md), [handoff](handoff.md) | `/root/infra_tech_review` độc lập, `/root/cm01_pm_ba` PM độc lập |
| READY_FOR_IMPLEMENTATION | **#47 đạt 14/10/2026** sau PM rà ticket, 5 AC, blocker và đọc lại Planio | [ticket](issues/01-docker-dev-test.md), [test spec](test-spec.md), [handoff](handoff.md) | `/root/cm01_pm_ba` rà độc lập; `/root` PM ghi gate |
| DEV_REVIEWED | Dev self-check đã có; code review độc lập đang xử lý P2, PM chưa chốt | [dev evidence](dev-evidence.md), diff/build/test và reviewer khác người code | `/root/infra_dev`, reviewer độc lập |
| QA_PASS | chưa có QA độc lập trên build/commit cuối | [test spec](test-spec.md), log chạy | chờ QA |

## Phụ thuộc và việc tiếp theo

CM-01.1 / Planio #43 đang tạm dừng readiness cho đến khi hạ tầng và Tech design CM-01 được cập nhật. Planio có quan hệ #47 `blocks` #43 (relation #8); INFRA-01 không tự mở lại #43. Không có câu hỏi sản phẩm chặn spec; VPS/domain/SMTP thật sẽ hỏi ở bước vận hành sau.
