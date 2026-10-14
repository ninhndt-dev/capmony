# Chuyển quản lý CapMoney sang Redmine

**Quyết định của Ninh ngày 14/10/2026:** dùng **Redmine qua dịch vụ Planio** làm hệ thống quản lý công việc CapMoney; Ninh đã gửi URL dự án [CapMoney trên Planio](https://tanninh.planio.com/projects/capmoney). Truy cập không xác thực chuyển sang trang đăng nhập; API dự án trả `401`, nên **chưa xác minh được dự án/cấu hình/issue và chưa tạo/sửa Story trên Planio**. Tracker Markdown vẫn ghi trạng thái hiện hành cho tới khi đối chiếu chuyển xong. Dona trong [workflow gốc](workflow-bot-capmm-original.md) là nguồn tham khảo quy trình, không phải một hệ thống đã kết nối với CapMoney.

## Cấu hình đề xuất để kiểm trên Redmine thực tế

| Phần | Đề xuất | Ranh giới |
| --- | --- | --- |
| Dự án | Một dự án riêng tên CapMoney, quyền xem theo thành viên dự án | ID dự án, quyền và URL chỉ ghi sau khi xác minh trên Redmine. |
| Loại issue | `Story` cho 35 lát đã duyệt; `Task`/`Bug`/`Test` chỉ tạo khi đến đúng bước | Nếu instance chưa có loại này, kiểm quyền quản trị và cấu hình trước khi nhập. Không gọi 35 Story là ticket Dev. |
| Trạng thái và gate | Trạng thái issue theo cấu hình Redmine; bằng chứng `BA_READY`, `TECH_READY`, `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED`, `QA_PASS` ghi ở trường riêng nếu có, hoặc comment năm trường cùng link tài liệu | Không đồng nhất `Open` với `BA_READY`, không dùng ID trạng thái của workflow Dona cũ. Gate chỉ có hiệu lực theo [workflow CapMoney](capmm-end-to-end.md). |
| Phụ thuộc | Quan hệ `blocks`/`blocked` cho cạnh `HARD` khi instance hỗ trợ; `CONTRACT`/`SOFT` được mô tả rõ trong issue hoặc trường riêng | Không biến mọi liên kết thành blocker cứng. Kiểm quan hệ sau nhập. |
| Tài liệu | Story liên kết đến spec, Q&A, technical design và handoff trong repo Git | Giữ Git là lịch sử tài liệu và bằng chứng; Redmine là nơi quản lý công việc sau khi chuyển xong. |

Redmine hỗ trợ tracker, trạng thái và workflow theo quyền; API issue có `tracker_id`, `status_id`, `parent_issue_id`, `fixed_version_id` và custom fields; issue relations có loại `blocks`/`blocked`. Planio [xác nhận API tương thích Redmine](https://plan.io/api/). Việc này cho phép ánh xạ workflow gốc, nhưng phải kiểm đúng cấu hình và quyền của instance Ninh dùng. Nguồn: [thiết lập issue](https://www.redmine.org/projects/redmine/wiki/redmineissuetrackingsetup), [API issue](https://www.redmine.org/projects/redmine/wiki/rest_issues), [API quan hệ](https://www.redmine.org/projects/redmine/wiki/Rest_IssueRelations).

Planio [công bố gói Bronze miễn phí](https://plan.io/pricing/) gồm một dự án, hai người dùng Planio và 1 GB; đây là giới hạn người **dùng tracker**, không phải mười người sử dụng ứng dụng CapMoney. Chọn Bronze làm điểm bắt đầu nếu Ninh chưa cần thêm người dùng tracker. Giá và giới hạn là thông tin ngày 14/10/2026, cần xem lại tại lúc đăng ký. [Trang đăng ký Bronze](https://accounts.plan.io/signup/Bronze?locale=en) yêu cầu Ninh tự đặt tên miền phụ, email và mật khẩu. Không nhập thông tin tài khoản của Ninh thay Ninh.

## Dữ liệu sẽ chuyển

| Nguồn | Đích | Quy tắc |
| --- | --- | --- |
| 35 Story ứng viên tại [tracker Markdown](../../.scratch/capmoney-overall/issues/) | 35 Story trong dự án Redmine | Giữ mã nguồn `#01`–`#35` trong subject hoặc trường tham chiếu. Dùng [manifest](redmine-story-manifest.csv) để tìm trùng trước khi tạo và ghi ID Redmine sau khi xác minh. Tất cả file hiện có `Status: needs-triage`; đây không phải gate. |
| `story.md`, `spec.md`, `qna.md`, `technical-design.md`, `handoff.md` trong `.scratch/<feature>/` | Link trên Story tương ứng | Không thay nội dung có lịch sử Git bằng comment rời rạc. |
| Ticket Dev cấp dưới | Task con của Story sau hai gate | Hiện chưa có ticket Dev đủ điều kiện; không tạo chỉ để hoàn tất việc chuyển. |

## Trình tự chuyển

1. Ninh bật REST API trong Planio và cấp API key qua file bí mật cục bộ theo hướng dẫn trong chat; **không gửi mật khẩu/token trong chat hoặc ghi Git**. Kiểm dự án CapMoney là riêng tư và quyền tạo/sửa Story. Chưa có plugin Planio trong Codex; Ninh chọn phương án dùng API để Bờm chuyển và đối chiếu trực tiếp.
2. Đọc project, tracker, status, custom field, quyền và danh sách issue hiện có. Ghi ánh xạ thực tế; không đoán ID từ workflow Dona. Chọn một Story để nhập thử và kiểm subject, mô tả, link tài liệu, gate và quyền xem.
3. Khi bản thử đúng, nhập các Story còn lại. Có thể dùng CSV import của Redmine hoặc API, tùy quyền/cấu hình instance; [Redmine có hướng dẫn nhập CSV](https://www.redmine.org/projects/redmine/wiki/HowTo_import_issues). Đối chiếu đủ 35 mã nguồn với 35 ID Redmine, phụ thuộc và trạng thái. #01–#06 chưa có gate mới; #07–#09 đã `BA_READY` nhưng còn `STORY_TECH_GAP`; các Story còn lại chưa tự đạt gate.
4. Sau khi dữ liệu Redmine được kiểm đủ, cập nhật [tracker](../agents/issue-tracker.md), [workflow](capmm-end-to-end.md), [AGENTS.md](../../AGENTS.md) và [README](../../README.md) để Redmine thành nguồn trạng thái. Markdown tiếp tục giữ spec và bằng chứng; không duy trì hai trạng thái mâu thuẫn.

## Điều còn thiếu

- API key được cấp qua file cục bộ và quyền đọc/tạo/sửa issue trong dự án [CapMoney](https://tanninh.planio.com/projects/capmoney).
- Bảng tracker, trạng thái, custom field và quan hệ của instance thực tế.
- Ánh xạ ID thật, kết quả bản nhập thử và đối chiếu sau chuyển.
