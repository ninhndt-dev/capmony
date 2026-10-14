# CapMoney: khởi động lại từ tiếp nhận yêu cầu

**Trạng thái:** Hoàn tất chia 35 Story ứng viên ở bước 8/11; đang ở bước 9. Cả 35 đã được chuyển và đối chiếu trên [Planio](https://tanninh.planio.com/projects/capmoney/issues); [manifest](docs/workflows/redmine-story-manifest.csv) nối ID với file nguồn. Đặc tả #01–#06 đã qua BA → PM → Tech → PM theo quy trình trước workflow CapMM. [Story #07](.scratch/capmoney-add-wallet/story.md), [Story #08](.scratch/capmoney-income/story.md) và [Story #09](.scratch/capmoney-expense/story.md) đã đạt `BA_READY` nhưng thiết kế đều còn `STORY_TECH_GAP`; #09 đã được reviewer kỹ thuật độc lập và PM rà sau R72–R73. Chưa có **Story sản phẩm** nào đủ gate giao Dev. [INFRA-01.1 / Planio #47](.scratch/capmoney-infrastructure/issues/01-docker-dev-test.md) là ticket hạ tầng riêng đã đạt `READY_FOR_IMPLEMENTATION`: web/API placeholder và Docker dev/test đã dựng, Dev self-check qua; code review/QA độc lập đang chờ, chưa có tính năng sản phẩm.

Dự án bắt đầu lại từ nhu cầu Ninh sẽ xác nhận trong các vòng phỏng vấn. Bản code trước đây đã được bỏ khỏi cây dự án này. Ninh đã chọn **hỏi lại toàn bộ từ đầu**; những câu trả lời cũ chỉ dùng làm bối cảnh, không tự động thành yêu cầu mới. Xem [phiếu tiếp nhận](docs/requirements/00-intake.md).

## Bắt đầu ở đâu

1. Đọc [phiếu tiếp nhận](docs/requirements/00-intake.md), [mục tiêu và phạm vi đã xác nhận](docs/requirements/goal-and-scope.md), [người dùng và quyền](docs/requirements/actors-and-roles.md), [nghiệp vụ chính đã xác nhận](docs/requirements/core-business.md), [luồng người dùng đã xác nhận](docs/requirements/user-flows.md) và [danh mục chức năng đã xác nhận](docs/requirements/system-functions.md).
2. Từ đặc tả tổng thể đã xác nhận, 35 lát và quan hệ phụ thuộc đã được chia ở bước 8 theo `to-tickets`; hiện chúng là Story ứng viên. Ticket Dev cấp dưới chỉ được chia sau các gate theo [quy trình](docs/agents/workflow.md).
3. Tiếp tục đặc tả chi tiết từng module ở bước 9 trước khi chuyển ticket sang `ready-for-agent`.
4. Từng module đi qua BA → PM → Tech → PM → Dev → PM → QA. Chỉ triển khai sau khi yêu cầu của module đủ rõ; dữ liệu và hành vi phải được kiểm tra thực tế.

Hướng dẫn vận hành agent nằm trong [AGENTS.md](AGENTS.md). Trạng thái công việc ở Planio; tài liệu và bằng chứng trong Git theo [quy ước tracker](docs/agents/issue-tracker.md).

Từ Story #07, dùng [workflow CapMM end to end](docs/workflows/capmm-end-to-end.md): BA lập spec 20 mục → PM kiểm `BA_READY` → Tech thiết kế và review độc lập → PM kiểm `TECH_READY` rồi mới chia ticket Dev. [Workflow gốc của Ninh](docs/workflows/workflow-bot-capmm-original.md) được giữ làm nguồn đối chiếu. Các Story #01–#06 chưa tự đạt gate mới.

## Lệnh tiếp tục cho Codex

Sau khi mở repo và cập nhật nhánh `main`, dán yêu cầu sau vào Codex:

```text
Tiếp tục dự án CapMoney từ trạng thái hiện tại trên nhánh main. Đọc AGENTS.md, README.md, docs/agents/workflow.md và docs/workflows/capmm-end-to-end.md trước. Hiện ở bước 9/11: Story #07 Thêm ví, Story #08 Ghi khoản thu và Story #09 Ghi khoản chi đều BA_READY nhưng còn STORY_TECH_GAP, chưa TECH_READY. Xử lý spike/hợp đồng nền tảng #04/#06/#08 cho quyền, danh mục, ví, tiền VND, atomic/idempotent của khoản thu/chi và số dư dưới đồng thời, kết quả “chắc chưa lưu” trước request tới muộn và phục hồi #33/#35. Đối chiếu technical-design.md và handoff.md của từng Story; reviewer kỹ thuật độc lập rà lại, PM kiểm phạm vi trước TECH_READY. Chỉ chia ticket Dev sau hai gate và chỉ code khi ticket READY_FOR_IMPLEMENTATION. Hỏi tôi bằng lựa chọn A/B trên giao diện khi phát sinh quyết định sản phẩm mới.
```

## Bộ skills

27 skills thuộc hai nhóm `engineering` và `productivity` của [mattpocock/skills](https://github.com/mattpocock/skills) được đặt tại `.agents/skills/` ở commit `49dd158d1076134a641b33efb035946536778336`. Đây là bản có thể chỉnh trong dự án; xem [nguồn và cách cập nhật](docs/agents/upstream.md). Chỉ dùng một bản skills cho agent để tránh trùng lệnh.

## Tài liệu cũ

Bản source cũ được lưu trong lịch sử phiên bản của `CapMoney_Source.zip`. Nó dùng làm nguồn đối chiếu lời Ninh đã xác nhận, không còn là code nền hay đặc tả đã duyệt của lần khởi động này.
