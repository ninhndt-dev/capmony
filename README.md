# CapMoney

CapMoney là web quản lý tài chính cá nhân ưu tiên điện thoại, được xây lại từ yêu cầu Ninh đã xác nhận. Bản đầu hướng tới khoảng 10 người dùng, dữ liệu riêng cho từng người, nhiều ví và tiền VND. [Mục tiêu và phạm vi](docs/requirements/goal-and-scope.md), [quyền](docs/requirements/actors-and-roles.md) và [danh mục chức năng](docs/requirements/system-functions.md) là nguồn yêu cầu; ảnh ứng dụng cũ chỉ là tham khảo giao diện.

**Tình trạng mã tại 16/10/2026:** đã có hạ tầng dev/test và phần thiết lập, đăng nhập quản lý của CM-01 trên nhánh `feat/cm-01-manager-setup`. Chưa có chức năng ví, thu/chi hoặc tài khoản ghi chép; trang `/` hiện là placeholder. Chưa triển khai production. Trạng thái issue mới nhất cần đọc trên [Planio](https://tanninh.planio.com/projects/capmoney/issues); bảng dưới đây mô tả **bằng chứng trong repo**, không tự thay trạng thái tracker hay cấp gate.

| Phần | Đã có trong repo | Còn để nghiệm thu |
| --- | --- | --- |
| INFRA-01 / #47, #48 | Java/Spring, React/Vite, Compose dev/test, Caddy, PostgreSQL, Redis, Mailpit; QA hạ tầng và MVC đã ghi nhận | VPS, domain, TLS và vận hành thật chưa dựng |
| CM-01.1 / #43 | CLI phát hành link thiết lập qua Mailpit; QA ticket #43 đã ghi 6/6 | SMTP/email thật khi có hạ tầng vận hành |
| CM-01.2 / #44 | Form thiết lập một quản lý, token một lần/hạn 72 giờ; có bằng chứng chạy T07–T12; [Planio đã đối chiếu](.scratch/capmoney-manager-setup/tracker-reconciliation-2026-10-16.md) về `In Progress/0%` | QA khác người code và PM đối chiếu trước `QA_PASS` #44 |
| CM-01.3 / #45 | Login, refresh, kiểm phiên DB, logout mọi thiết bị, giao diện quản lý tối thiểu; mã nền `4a4a1a4`, [sửa refresh](.scratch/capmoney-manager-setup/qa45-refresh-unknown-fix.md) ở `6676d5e` | `HARD` #44 → #45, code review, QA độc lập T13–T20; chưa cấp `DEV_REVIEWED`/`QA_PASS` |
| CM-01 cấp Story | Spec AC1–AC7 và test spec T01–T21 | Regression T21 và iPhone/Android vật lý theo CM01-Q4, rồi PM xét `QA_PASS` Story |

## Chạy môi trường dev/test

Cần Docker Engine và Docker Compose. Tại gốc repo:

```sh
./infra/scripts/stack.sh dev up
./infra/scripts/stack.sh dev ps
```

Mở `http://127.0.0.1:18080/`; các route hiện có là `/manager/setup`, `/manager/login`, `/manager`. API health ở `/api/health`, readiness DB + Redis ở `/api/ready/auth`. Môi trường `test` dùng `./infra/scripts/stack.sh test up` và cổng `28080`. Dừng bằng `./infra/scripts/stack.sh dev stop` hoặc `test stop`. Script giữ container/network/volume; không dùng `down` hay reset dữ liệu. `up` giữ container cũ (`--no-recreate`), vì vậy build mã mới cần một project thử cô lập để tránh nhầm phiên bản đang chạy. Chi tiết biến môi trường, secret, kiểm thử và giới hạn nằm trong [runbook hạ tầng](infra/README.md).

Không có đăng ký công khai. Người triển khai phát hành link đầu tiên bằng CLI trong môi trường đã cấu hình; xem [runbook bootstrap](.scratch/capmoney-manager-setup/bootstrap-runbook.md). Test dùng `manager@example.test` và Mailpit, không cần email thật.

## Kiểm mã

- Node 24: `npm ci`, `npm run typecheck`, `npm run build` kiểm web; `npm test` kiểm script stack. Build Docker web cũng chạy TypeScript/Vite.
- Java 25: `cd apps/api && ./mvnw -B test` chạy bộ test mặc định. Test tích hợp PostgreSQL/Redis chỉ bật khi `CAPMONEY_CONTAINER_TESTS=true` với Docker socket/Testcontainers; lệnh Docker đã chạy và kết quả nằm trong [bằng chứng Dev #45](.scratch/capmoney-manager-setup/qa45-dev-evidence.md).
- Test xanh chỉ chứng minh những case đã chạy. [Test spec CM-01](.scratch/capmoney-manager-setup/test-spec.md) và bằng chứng từng ticket ghi riêng phần QA còn chờ.

## Bản đồ tài liệu

[Mục lục docs](docs/README.md) giải thích nguồn nào là quyết định, thiết kế, bằng chứng hay lịch sử.

| Cần tìm | Đọc |
| --- | --- |
| Kiến trúc và phần đang chạy | [Tổng quan kỹ thuật](docs/project-overview.md), [ADR Java hiện hành](docs/adr/0003-java-spring-and-deployment.md) |
| Yêu cầu sản phẩm đã xác nhận | [Phiếu tiếp nhận](docs/requirements/00-intake.md), [mục tiêu](docs/requirements/goal-and-scope.md), [luồng](docs/requirements/user-flows.md), [chức năng](docs/requirements/system-functions.md) |
| Quyết định công nghệ | [TS10–TS38](docs/requirements/tech-stack-round-2.md); ADR 0001/0002 chỉ còn giá trị lịch sử ở các phần bị thay |
| Story, ticket, test và gate CM-01 | [Story](.scratch/capmoney-manager-setup/story.md), [handoff](.scratch/capmoney-manager-setup/handoff.md), [bằng chứng #44](.scratch/capmoney-manager-setup/qa44-evidence.md), [bằng chứng Dev #45](.scratch/capmoney-manager-setup/qa45-dev-evidence.md) |
| Quy trình và tracker | [Workflow CapMM](docs/workflows/capmm-end-to-end.md), [issue tracker](docs/agents/issue-tracker.md), [hướng dẫn agent](AGENTS.md) |

Các file trong `docs/requirements/` và `.scratch/` có cả quyết định, bản nháp và nhật ký tại thời điểm lập. Khi một dòng trạng thái cũ khác với hiện trạng, đối chiếu handoff mới nhất, commit và Planio; không suy ra `QA_PASS` từ việc đã viết code.
