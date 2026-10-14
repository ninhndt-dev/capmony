# Issue tracker: Planio và tài liệu Markdown

Ninh chọn [Planio / Redmine](https://tanninh.planio.com/projects/capmoney) làm nguồn trạng thái công việc hiện hành. Spec, Q&A, thiết kế, kết quả review và bằng chứng gate ở `.scratch/` trong Git. [Báo cáo chuyển](../workflows/redmine-migration.md) và [manifest 35 Story](../workflows/redmine-story-manifest.csv) nối ID Planio với file nguồn.

35 Story ứng viên đã được chuyển và đọc lại trên Planio ngày 14/10/2026. Instance chỉ có loại `Task` và `Support`; theo lựa chọn của Ninh, Story ứng viên dùng `Task` với tiền tố `[Story ứng viên] CM-XX`. Trạng thái `Open` của Planio không chứng minh gate.

Theo [workflow CapMM](../workflows/capmm-end-to-end.md), 35 file tại `.scratch/capmoney-overall/issues/` là **nguồn Story ứng viên** đã được Ninh duyệt cách chia ở bước 8. `Status: needs-triage` trong các file này là ảnh chụp lịch sử trước khi chuyển Planio, không phải trạng thái hiện hành. Chúng chưa phải ticket Dev. Chỉ tạo ticket triển khai cấp dưới trên Planio sau `BA_READY` và `TECH_READY`; mỗi ticket phải đạt `READY_FOR_IMPLEMENTATION` riêng trước khi giao Dev.

## Quy ước

- Mỗi chức năng có thư mục `.scratch/<feature-slug>/`.
- Đặc tả của chức năng là `.scratch/<feature-slug>/spec.md`.
- Một công việc có issue Planio để theo dõi trạng thái và liên kết tới tài liệu nguồn trong Git. Lưu file đặc tả/ticket chi tiết ở `.scratch/<feature-slug>/issues/<NN>-<slug>.md` khi workflow cần bằng chứng có phiên bản.
- Tài liệu ghi gate có bằng chứng, câu hỏi mở và phụ thuộc `HARD`/`CONTRACT`/`SOFT`; issue Planio dẫn tới đúng file và mã AC. [Nhãn triage](triage-labels.md) trong tài liệu chỉ hỗ trợ điều phối, không thay gate hay trạng thái Planio.
- Cập nhật trạng thái hiện hành trên Planio. Không duy trì một trạng thái Markdown khác dễ mâu thuẫn.
- Chỉ tạo spec/ticket sau khi yêu cầu đủ rõ. Phiếu tiếp nhận ở `docs/requirements/` là đầu vào, chưa phải spec sẵn sàng code.

Khi skill nói “publish to issue tracker”, tạo/cập nhật issue Planio và liên kết tài liệu đã ghi trong Git. Khi nói “fetch ticket”, đọc issue Planio cùng file nguồn được dẫn. Quyết định mơ hồ được ghi rõ là chưa rõ; không tự nâng gate.

Với `wayfinder`, dùng `.scratch/<effort>/map.md` và một file con cho mỗi câu hỏi/ticket. Trạng thái `claimed`/`resolved` và `Blocked by:` tuân theo hướng dẫn của skill tương ứng.
