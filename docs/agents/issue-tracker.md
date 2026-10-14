# Issue tracker: Markdown trong dự án

Ninh chọn lưu công việc ngay trong dự án. Spec và ticket nằm tại `.scratch/` và được giữ trong phiên bản dự án.

Ninh đã chọn [chuyển quản lý sang Redmine](../workflows/redmine-migration.md). Chưa có kết nối và chưa chuyển Story; quy ước Markdown dưới đây vẫn áp dụng cho tới khi đối chiếu xong dữ liệu trên Redmine.

Từ sau #06, theo [workflow CapMM](../workflows/capmm-end-to-end.md), 35 file tại `.scratch/capmoney-overall/issues/` là **Story ứng viên** đã được Ninh duyệt cách chia ở bước 8. Chúng chưa phải ticket Dev đạt gate. Ticket triển khai cấp dưới chỉ tạo ở `.scratch/<feature-slug>/issues/` sau `BA_READY` và `TECH_READY`; mỗi ticket phải đạt `READY_FOR_IMPLEMENTATION` riêng trước khi giao Dev. Nhãn `Status:` không thay bằng chứng gate.

## Quy ước

- Mỗi chức năng có thư mục `.scratch/<feature-slug>/`.
- Đặc tả của chức năng là `.scratch/<feature-slug>/spec.md`.
- Một ticket một file: `.scratch/<feature-slug>/issues/<NN>-<slug>.md`, đánh số từ `01`.
- Ticket ghi `Status:` gần đầu file theo [trạng thái chuẩn](triage-labels.md) và `Blocked by:` cho điều kiện phụ thuộc.
- Trao đổi thêm ở cuối file dưới `## Comments`.
- Chỉ tạo spec/ticket sau khi yêu cầu đủ rõ. Phiếu tiếp nhận ở `docs/requirements/` là đầu vào, chưa phải spec sẵn sàng code.

Khi skill nói “publish to issue tracker”, ghi file theo quy ước trên. Khi nói “fetch ticket”, đọc file được dẫn. Các quyết định mơ hồ giữ trạng thái `needs-info`.

Với `wayfinder`, dùng `.scratch/<effort>/map.md` và một file con cho mỗi câu hỏi/ticket. Trạng thái `claimed`/`resolved` và `Blocked by:` tuân theo hướng dẫn của skill tương ứng.
