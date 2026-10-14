# Chuyển quản lý CapMoney sang Redmine / Planio

**Hoàn tất ngày 14/10/2026.** Ninh chọn [dự án CapMoney trên Planio](https://tanninh.planio.com/projects/capmoney) làm nơi quản lý trạng thái công việc. Dự án có ID `4`, là dự án riêng tư (`is_public: false`). Qua API, Bờm đã tạo và đọc lại đủ 35 Story ứng viên, đối chiếu mã `CM-01`–`CM-35`, tiêu đề, mô tả, loại và trạng thái; [manifest](redmine-story-manifest.csv) giữ ID Planio tương ứng. Tài liệu và bằng chứng gate vẫn ở Git. Dona trong [workflow gốc](workflow-bot-capmm-original.md) chỉ là nguồn tham khảo quy trình.

## Ánh xạ thực tế

| Thành phần | Cấu hình đã kiểm trên Planio | Cách dùng tại CapMoney |
| --- | --- | --- |
| Loại issue | Dự án có `Task` (ID `3`) và `Support` (ID `4`), không có `Story`; không có custom field issue | Theo lựa chọn của Ninh, 35 Story ứng viên dùng `Task`, tiêu đề bắt đầu `[Story ứng viên] CM-XX`. Chúng **chưa phải ticket Dev**. Ticket Dev cấp dưới cũng dùng `Task` khi đủ gate, nhưng tên và liên kết cha phải phân biệt rõ. |
| Trạng thái | `Open` (ID `5`), `In Progress` (`6`), `Feedback` (`7`), `Closed` (`8`) | Tất cả 35 Story đang `Open`. Trạng thái Planio không thay bằng chứng `BA_READY`, `TECH_READY`, `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED` hay `QA_PASS` trong tài liệu. |
| Tài liệu | Không có trường riêng | Mỗi issue chứa nội dung Story gốc và liên kết tới file nguồn trong Git; liên kết tài liệu tương đối trong file được đổi thành liên kết GitHub. |
| Phụ thuộc | API quan hệ `blocks` hoạt động | Đã ghi và đọc lại hai cạnh `HARD` có bằng chứng rõ: [CM-06](https://tanninh.planio.com/issues/13) chặn [CM-07](https://tanninh.planio.com/issues/14); [CM-08](https://tanninh.planio.com/issues/15) chặn [CM-09](https://tanninh.planio.com/issues/16). Các dòng `Blocked by` khác có trong mô tả nguồn; cần phân loại `HARD`/`CONTRACT`/`SOFT` theo workflow trước khi tạo quan hệ cứng. |

## Kết quả đối chiếu

- Có đúng 35 issue trong dự án, ID liên tiếp `8`–`42`; 35 mã `CM-XX` có 35 ID khác nhau và khớp [manifest](redmine-story-manifest.csv).
- Mỗi issue được đọc lại qua API sau khi tạo. Tiêu đề, mô tả, dự án, loại `Task` và trạng thái `Open` đều khớp nguồn sau chuẩn hóa xuống dòng do Planio chuyển `LF` thành `CRLF`.
- #01–#06 chưa có gate mới; #07–#09 đã `BA_READY` nhưng còn `STORY_TECH_GAP`; #10–#35 chưa qua gate mới. Việc chuyển tracker không nâng gate, không tạo ticket Dev và không viết code.
- `Status: needs-triage` trong 35 file Markdown là ảnh chụp lúc xuất bản ở bước 8. Từ đây, trạng thái công việc hiện hành được cập nhật trên Planio; gate và quyết định vẫn phải có bằng chứng trong tài liệu Git rồi dẫn từ issue.

## Vận hành tiếp

1. Tra Story qua [danh sách issue trên Planio](https://tanninh.planio.com/projects/capmoney/issues) hoặc mã trong [manifest](redmine-story-manifest.csv). Đọc file nguồn và spec trong Git để xem yêu cầu đã xác nhận.
2. BA → PM → Tech → PM giữ gate trong [workflow CapMM](capmm-end-to-end.md). Khi gate đạt, PM tạo ticket Dev cấp dưới trên Planio, ghi liên kết Story cha, spec, phụ thuộc và bằng chứng. Chỉ giao Dev khi ticket riêng đạt `READY_FOR_IMPLEMENTATION`.
3. `Open` hay `Closed` trên Planio chỉ là trạng thái quản lý; không suy ra nghiệm thu sản phẩm. Kết quả PM/QA phải ghi theo workflow và liên kết bằng chứng.
4. API key đã dùng qua file bí mật cục bộ; không lưu trong Git. Khi cần cập nhật Planio từ agent, Ninh cấp lại quyền qua kênh an toàn. [Planio API](https://plan.io/api/) tương thích [Redmine REST API](https://www.redmine.org/projects/redmine/wiki/rest_issues).
