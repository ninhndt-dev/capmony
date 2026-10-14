# Story INFRA-01: Môi trường Docker dev/test

**Lifecycle:** specification (rebaseline Java Spring theo TS10–TS38)
**Planio:** [#46 – INFRA-01](https://tanninh.planio.com/issues/46), issue hạ tầng riêng; [#47 – INFRA-01.1](https://tanninh.planio.com/issues/47) là ticket DevOps cấp dưới. Đã tạo và đọc lại ngày 14/10/2026.
**Blocked by:** reviewer Tech độc lập rà lại ba P1 của [ADR 0003](../../docs/adr/0003-java-spring-and-deployment.md), rồi Tech cập nhật thiết kế INFRA-01 và PM kiểm gate; ticket #47 Node/Hono không còn ready.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** React web + Java Spring API riêng, Docker cho dev/test, kiến trúc và review trước Docker/FE/BE, PostgreSQL volume trên VPS chạy thật, Caddy, Redis cho throttle, Tailwind và production cài trực tiếp theo [TS01/TS06–TS08](../../docs/requirements/tech-stack-round-1.md) và [TS10–TS38](../../docs/requirements/tech-stack-round-2.md). TS09/ADR 0002 thuộc stack lịch sử.
- **Phạm vi:** môi trường Docker Compose dev/test cô lập với Caddy, web React/Vite placeholder, API health Java Spring, PostgreSQL volume, Redis và Mailpit. [Spec 20 mục](spec.md) định nghĩa AC-IF01-1–AC-IF01-5. Không triển khai hành vi CM-01 hoặc dữ liệu tài chính trong ticket này.
- **Tech đề xuất đang review:** [ADR 0003](../../docs/adr/0003-java-spring-and-deployment.md), [sơ đồ Java](architecture-java-proposal.md), [review P1](java-tech-review.md). Chưa chấp nhận là hợp đồng triển khai.

## Gate và bằng chứng

| Gate | Hiện tại | Bằng chứng | Reviewer |
| --- | --- | --- | --- |
| BA_READY | **Đạt ngày 14/10/2026 cho bản Java/Redis:** PM độc lập đối chiếu 20 mục/5 AC với TS10–TS38 và IF-Q1–IF-Q5; gate Node/Hono cũ chỉ là lịch sử | [spec](spec.md), [Q&A](qna.md), [handoff](handoff.md) | `/root/pm_ba_rebaseline` |
| TECH_READY | **Tạm dừng hiệu lực**; ADR 0002 và thiết kế Node/Hono bị thay | [ADR 0002](../../docs/adr/0002-project-infrastructure-and-stack.md), [technical-design](technical-design.md) | Tech độc lập/PM phải rà lại |
| READY_FOR_IMPLEMENTATION | **#47 không còn ready** cho Java; trạng thái đạt trước đây chỉ áp dụng bản Node/Hono | [ticket](issues/01-docker-dev-test.md), [test spec](test-spec.md) | PM xét lại sau hai gate |
| DEV_REVIEWED | commit `493f3d4` đã review cho Node/Hono, **không áp dụng cho Java**; patch P1 sau QA chưa commit | [dev evidence](dev-evidence.md), [qa evidence](qa-evidence.md) | cần code/review mới sau gate |
| QA_PASS | **chưa đạt**: IF-T03 fail trên Node/Hono, QA dừng khi đổi stack | [test spec](test-spec.md), [qa evidence](qa-evidence.md) | QA Java phải chạy lại |

## Phụ thuộc và việc tiếp theo

CM-01.1 / Planio #43 tiếp tục tạm dừng. Planio có quan hệ #47 `blocks` #43 (relation #8); INFRA-01 không tự mở lại #43. Tech phải lập ADR Java Spring, BA/PM/Tech/PM rà lại spec và ticket hạ tầng, rồi mới triển khai Java. Stack Node/Hono đã dừng, volume thử vẫn giữ. VPS/domain/SMTP thật sẽ xử lý ở giai đoạn vận hành.
