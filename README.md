# CapMoney: khởi động lại từ tiếp nhận yêu cầu

**Trạng thái:** Hoàn tất chia 35 Story ứng viên ở bước 8/11; đang ở bước 9. Cả 35 đã chuyển sang [Planio](https://tanninh.planio.com/projects/capmoney/issues); [manifest](docs/workflows/redmine-story-manifest.csv) nối ID với file nguồn. Story #07–#09 đạt `BA_READY` nhưng còn `STORY_TECH_GAP`; chưa có Story sản phẩm nào đủ gate giao Dev. Ninh đã [chọn lại tech stack](docs/requirements/tech-stack-round-2.md): BE Java Spring, FE React/Vite/Tailwind, Docker dev/test, production cài trực tiếp trên VPS. Commit Node/Hono `493f3d4` và [INFRA-01.1 / Planio #47](.scratch/capmoney-infrastructure/issues/01-docker-dev-test.md) là lịch sử thử hạ tầng; QA IF-T03 fail và đã dừng. Gate #47/#43 cần thiết kế/review lại trước khi viết code Java.

Dự án bắt đầu lại từ nhu cầu Ninh sẽ xác nhận trong các vòng phỏng vấn. Bản code trước đây đã được bỏ khỏi cây dự án này. Ninh đã chọn **hỏi lại toàn bộ từ đầu**; những câu trả lời cũ chỉ dùng làm bối cảnh, không tự động thành yêu cầu mới. Xem [phiếu tiếp nhận](docs/requirements/00-intake.md).

## Bắt đầu ở đâu

1. Đọc [phiếu tiếp nhận](docs/requirements/00-intake.md), [mục tiêu và phạm vi đã xác nhận](docs/requirements/goal-and-scope.md), [người dùng và quyền](docs/requirements/actors-and-roles.md), [nghiệp vụ chính đã xác nhận](docs/requirements/core-business.md), [luồng người dùng đã xác nhận](docs/requirements/user-flows.md) và [danh mục chức năng đã xác nhận](docs/requirements/system-functions.md).
2. Từ đặc tả tổng thể đã xác nhận, 35 lát và quan hệ phụ thuộc đã được chia ở bước 8 theo `to-tickets`; hiện chúng là Story ứng viên. Ticket Dev cấp dưới chỉ được chia sau các gate theo [quy trình](docs/agents/workflow.md).
3. Tiếp tục đặc tả chi tiết từng module ở bước 9 trước khi chuyển ticket sang `ready-for-agent`.
4. Từng module đi qua BA → PM → Tech → PM → Dev → PM → QA. Chỉ triển khai sau khi yêu cầu của module đủ rõ; dữ liệu và hành vi phải được kiểm tra thực tế.

Hướng dẫn vận hành agent nằm trong [AGENTS.md](AGENTS.md). Trạng thái công việc ở Planio; tài liệu và bằng chứng trong Git theo [quy ước tracker](docs/agents/issue-tracker.md).

Từ Story #07, dùng [workflow CapMM end to end](docs/workflows/capmm-end-to-end.md): BA lập spec 20 mục → PM kiểm `BA_READY` → Tech thiết kế và review độc lập → PM kiểm `TECH_READY` rồi mới chia ticket Dev. [Workflow gốc của Ninh](docs/workflows/workflow-bot-capmm-original.md) được giữ làm nguồn đối chiếu. Các Story #01–#06 chưa tự đạt gate mới.

## Lệnh tiếp tục cho Codex

Sau khi mở repo và kiểm tra nhánh đang làm việc, dán yêu cầu sau vào Codex:

```text
Tiếp tục CapMoney ở bước 9/11 trên nhánh hiện tại. Đọc AGENTS.md, README.md, docs/agents/workflow.md, docs/workflows/capmm-end-to-end.md và docs/requirements/tech-stack-round-2.md. Ninh đã chốt Java Spring API, React/Vite/Tailwind web, JWT cookie, Redis throttle, Docker dev/test và cài trực tiếp lên VPS về sau. Node/Hono, ADR 0001/0002 và QA cũ là lịch sử; QA IF-T03 fail. Rà ADR 0003 và sơ đồ Java qua reviewer Tech độc lập, PM; BA/PM cập nhật INFRA-01 và CM-01 rồi Tech/PM cấp lại gate. Planio #47 chặn #43, #43 chặn #44, #44 chặn #45. Không viết code Java trước khi ticket READY_FOR_IMPLEMENTATION. Khi cần quyết định của Ninh, hỏi bằng giao diện lựa chọn A/B, không yêu cầu gõ thủ công.
```

## Bộ skills

27 skills thuộc hai nhóm `engineering` và `productivity` của [mattpocock/skills](https://github.com/mattpocock/skills) được đặt tại `.agents/skills/` ở commit `49dd158d1076134a641b33efb035946536778336`. Đây là bản có thể chỉnh trong dự án; xem [nguồn và cách cập nhật](docs/agents/upstream.md). Chỉ dùng một bản skills cho agent để tránh trùng lệnh.

## Tài liệu cũ

Bản source cũ được lưu trong lịch sử phiên bản của `CapMoney_Source.zip`. Nó dùng làm nguồn đối chiếu lời Ninh đã xác nhận, không còn là code nền hay đặc tả đã duyệt của lần khởi động này.
