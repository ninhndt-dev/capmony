# CapMoney: khởi động lại từ tiếp nhận yêu cầu

**Trạng thái:** Bước 1/11, đang tiếp nhận và xác nhận yêu cầu. Dự án này hiện chưa có code ứng dụng iOS hoặc REST API.

Dự án bắt đầu lại từ nhu cầu Ninh sẽ xác nhận trong các vòng phỏng vấn. Bản code trước đây đã được bỏ khỏi cây dự án này. Ninh đã chọn **hỏi lại toàn bộ từ đầu**; những câu trả lời cũ chỉ dùng làm bối cảnh, không tự động thành yêu cầu mới. Xem [phiếu tiếp nhận](docs/requirements/00-intake.md).

## Bắt đầu ở đâu

1. Đọc [phiếu tiếp nhận](docs/requirements/00-intake.md) và [vòng hỏi đầu](docs/requirements/01-interview-round-1.md).
2. Trả lời các câu hỏi của vòng hiện tại. Agent cập nhật tài liệu sau từng vòng theo `grill-with-docs` và `domain-modeling`.
3. Khi yêu cầu đã rõ, PM kiểm tra; sau đó `to-spec` viết đặc tả và `to-tickets` chia thành các module có thể nghiệm thu.
4. Từng module đi qua BA → PM → Tech → PM → Dev → PM → QA. Chỉ triển khai sau khi yêu cầu của module đủ rõ; dữ liệu và hành vi phải được kiểm tra thực tế.

Hướng dẫn vận hành agent nằm trong [AGENTS.md](AGENTS.md). Công việc được theo dõi dưới `.scratch/` bằng Markdown theo [quy ước tracker](docs/agents/issue-tracker.md).

## Bộ skills

27 skills thuộc hai nhóm `engineering` và `productivity` của [mattpocock/skills](https://github.com/mattpocock/skills) được đặt tại `.agents/skills/` ở commit `49dd158d1076134a641b33efb035946536778336`. Đây là bản có thể chỉnh trong dự án; xem [nguồn và cách cập nhật](docs/agents/upstream.md). Chỉ dùng một bản skills cho agent để tránh trùng lệnh.

## Tài liệu cũ

Bản source cũ được lưu trong lịch sử phiên bản của `CapMoney_Source.zip`. Nó dùng làm nguồn đối chiếu lời Ninh đã xác nhận, không còn là code nền hay đặc tả đã duyệt của lần khởi động này.
