# Story INFRA-01: Môi trường Docker dev/test

**Lifecycle:** implementing (#47 Java/Redis đã READY_FOR_IMPLEMENTATION; Dev đang làm)
**Planio:** [#46 – INFRA-01](https://tanninh.planio.com/issues/46), issue hạ tầng riêng; [#47 – INFRA-01.1](https://tanninh.planio.com/issues/47) là ticket DevOps cấp dưới. Đã tạo và đọc lại ngày 14/10/2026.
**Blocked by:** Dev triển khai #47 trên Java, code review và QA độc lập; bản code/QA Node/Hono không dùng cho Java.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** React web + Java Spring API riêng, Docker cho dev/test, kiến trúc và review trước Docker/FE/BE, PostgreSQL volume trên VPS chạy thật, Caddy, Redis cho throttle, Tailwind và production cài trực tiếp theo [TS01/TS06–TS08](../../docs/requirements/tech-stack-round-1.md) và [TS10–TS38](../../docs/requirements/tech-stack-round-2.md). TS09/ADR 0002 thuộc stack lịch sử.
- **Phạm vi:** môi trường Docker Compose dev/test cô lập với Caddy, web React/Vite placeholder, API health Java Spring, PostgreSQL volume, Redis và Mailpit. [Spec 20 mục](spec.md) định nghĩa AC-IF01-1–AC-IF01-5. Không triển khai hành vi CM-01 hoặc dữ liệu tài chính trong ticket này.
- **Thiết kế đã review cho INFRA-01:** [ADR 0003](../../docs/adr/0003-java-spring-and-deployment.md), [sơ đồ Java](architecture-java-proposal.md), [review Tech độc lập](java-tech-review.md) và [thiết kế chi tiết](technical-design-java.md). ADR còn là đề xuất kiến trúc toàn dự án; hợp đồng #47 đã qua gate Story/ticket, chưa có bằng chứng Java chạy/QA.

## Gate và bằng chứng

| Gate | Hiện tại | Bằng chứng | Reviewer |
| --- | --- | --- | --- |
| BA_READY | **Đạt ngày 14/10/2026 cho bản Java/Redis:** PM độc lập đối chiếu 20 mục/5 AC với TS10–TS38 và IF-Q1–IF-Q5; gate Node/Hono cũ chỉ là lịch sử | [spec](spec.md), [Q&A](qna.md), [handoff](handoff.md) | `/root/pm_ba_rebaseline` |
| TECH_READY | **Đạt ngày 14/10/2026 cho INFRA Java/Redis:** thiết kế V1–V8 bao phủ 5 AC; reviewer Tech độc lập đã kiểm lại P1 outage và P2, PM đối chiếu phạm vi/stack | [ADR 0003](../../docs/adr/0003-java-spring-and-deployment.md), [thiết kế Java](technical-design-java.md), [review Tech](java-tech-review.md), [handoff PM](handoff.md) | `/root/tech_adr_rereview` và `/root/pm_ba_rebaseline` |
| READY_FOR_IMPLEMENTATION | **#47 đạt ngày 14/10/2026 cho Java/Redis:** BA/Tech độc lập rà ticket/test spec, PM giải P2 TS22 và xét 5 AC/test data/phụ thuộc; readiness Node/Hono chỉ là lịch sử | [ticket](issues/01-docker-dev-test.md), [test spec Java](test-spec.md), [handoff](handoff.md) | `/root/ba_review`, `/root/tech_adr_rereview`, `/root/pm_ba_rebaseline` |
| DEV_REVIEWED | commit `493f3d4` đã review cho Node/Hono, **không áp dụng cho Java**; patch P1 sau QA chưa commit | [dev evidence](dev-evidence.md), [qa evidence](qa-evidence.md) | cần code/review mới sau gate |
| QA_PASS | **chưa đạt**: IF-T03 fail trên Node/Hono, QA dừng khi đổi stack | [test spec](test-spec.md), [qa evidence](qa-evidence.md) | QA Java phải chạy lại |

## Phụ thuộc và việc tiếp theo

CM-01.1 / Planio #43 tiếp tục tạm dừng. Planio có quan hệ #47 `blocks` #43 (relation #8); readiness #47 không giải cạnh cho đến khi #47 hoàn tất/QA_PASS. ADR, BA/PM spec, Tech độc lập/PM thiết kế và BA/Tech/PM ticket Java đã qua gate; Planio #46/#47 đã đồng bộ `In Progress` và Dev đang làm. Stack Node/Hono đã dừng, volume thử vẫn giữ. VPS/domain/SMTP thật sẽ xử lý ở giai đoạn vận hành.
