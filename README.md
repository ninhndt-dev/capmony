# CapMoney: khởi động lại từ tiếp nhận yêu cầu

**Trạng thái:** Bước 9/11, đặc tả từng module; 35 Story ứng viên đã ở [Planio](https://tanninh.planio.com/projects/capmoney/issues). Stack hiện hành: Java Spring, React/Vite/Tailwind, JWT cookie, Redis; Docker chỉ dev/test, production cài trực tiếp trên VPS theo [vòng 2](docs/requirements/tech-stack-round-2.md). INFRA-01.1 / #47 đã có code Java trên `310ad7f`, review độc lập và8/8 case QA thực chạy pass, nhưng **gate cuối chờ Ninh xử lý IF-Q6: JUnit5 đã chọn khácJUnit6 mà Spring7 yêu cầu**; xem [báo cáo Java](.scratch/capmoney-infrastructure/qa-evidence-java.md). [Handoff hiện hành](.scratch/capmoney-infrastructure/handoff.md) là nơi đối chiếu gate. Planio chưa được đọc/cập nhật trong phiên này vì chưa có API key; không suy trạng thái Closed. CM-01 #43–#45 chưa giao code; #43 cần PM xét readiness riêng sau khi #47 hoàn tất. Node/Hono `493f3d4` và QA IF-T03 fail chỉ là lịch sử.

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
Tiếp tục CapMoney bước 9/11 trên nhánh hiện tại. Đọc AGENTS.md, README.md, docs/agents/workflow.md, docs/workflows/capmm-end-to-end.md và docs/requirements/tech-stack-round-2.md. Đọc handoff, code-review-java.md và qa-evidence-java.md trong .scratch/capmoney-infrastructure để đối chiếu kết quả trên commit310ad7f; không dùng QA Node/Hono làm bằng chứng. Giải IF-Q6 vềJUnit5/6 trước khi chốt gate #47; không tự sửa lựa chọn của Ninh. Đồng bộ #46/#47 lên Planio khi có quyền truy cập an toàn. Sau khi #47 hoàn tất, PM kiểm readiness riêng CM-01.1 / #43 dựa BA/Tech reviews trước khi giao Dev; #43 chặn #44, #44 chặn #45. Không xóa tài nguyên; test sạch dùng môi trường mới, giữ môi trường cũ. Khi cần quyết định sản phẩm của Ninh, hỏi bằng lựa chọn A/B.
```

## Bộ skills

27 skills thuộc hai nhóm `engineering` và `productivity` của [mattpocock/skills](https://github.com/mattpocock/skills) được đặt tại `.agents/skills/` ở commit `49dd158d1076134a641b33efb035946536778336`. Đây là bản có thể chỉnh trong dự án; xem [nguồn và cách cập nhật](docs/agents/upstream.md). Chỉ dùng một bản skills cho agent để tránh trùng lệnh.

## Tài liệu cũ

Bản source cũ được lưu trong lịch sử phiên bản của `CapMoney_Source.zip`. Nó dùng làm nguồn đối chiếu lời Ninh đã xác nhận, không còn là code nền hay đặc tả đã duyệt của lần khởi động này.
