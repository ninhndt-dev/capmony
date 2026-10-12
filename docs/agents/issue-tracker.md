# Issue tracker: Markdown trong dự án

Ninh chọn lưu công việc ngay trong dự án. Spec và ticket nằm tại `.scratch/` và được giữ trong phiên bản dự án.

## Quy ước

- Mỗi chức năng có thư mục `.scratch/<feature-slug>/`.
- Đặc tả của chức năng là `.scratch/<feature-slug>/spec.md`.
- Một ticket một file: `.scratch/<feature-slug>/issues/<NN>-<slug>.md`, đánh số từ `01`.
- Ticket ghi `Status:` gần đầu file theo [trạng thái chuẩn](triage-labels.md) và `Blocked by:` cho điều kiện phụ thuộc.
- Trao đổi thêm ở cuối file dưới `## Comments`.
- Chỉ tạo spec/ticket sau khi yêu cầu đủ rõ. Phiếu tiếp nhận ở `docs/requirements/` là đầu vào, chưa phải spec sẵn sàng code.

Khi skill nói “publish to issue tracker”, ghi file theo quy ước trên. Khi nói “fetch ticket”, đọc file được dẫn. Các quyết định mơ hồ giữ trạng thái `needs-info`.

Với `wayfinder`, dùng `.scratch/<effort>/map.md` và một file con cho mỗi câu hỏi/ticket. Trạng thái `claimed`/`resolved` và `Blocked by:` tuân theo hướng dẫn của skill tương ứng.
