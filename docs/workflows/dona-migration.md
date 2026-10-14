# Chuyển quản lý CapMoney sang Dona

**Trạng thái:** Ninh đề nghị chuyển ngày 14/10/2026; đang chờ URL dự án, quyền truy cập và kiểm tra cấu hình Dona thực tế. Chưa tạo hoặc sửa Story trên Dona. Trong lúc chờ, tracker Markdown vẫn là nơi ghi trạng thái hiện hành.

## Nguồn cần chuyển

| Dữ liệu hiện tại | Đích dự kiến trên Dona | Quy tắc |
| --- | --- | --- |
| 35 Story ứng viên ở [tracker Markdown](../../.scratch/capmoney-overall/issues/) | 35 Story thuộc một dự án CapMoney do Ninh chỉ định | Giữ số `#01`–`#35` trong tiêu đề hoặc trường tham chiếu; tìm theo số nguồn trước khi tạo để tránh trùng. Tất cả hiện mang nhãn `needs-triage`, không suy ra `BA_READY`. |
| `story.md`, `spec.md`, `qna.md`, `technical-design.md`, `handoff.md` trong `.scratch/<feature>/` | Liên kết từ Story tương ứng tới file trong repo/commit; nội dung cần hiển thị trên Dona chỉ chuyển sau khi biết khả năng của dự án | Tài liệu yêu cầu và bằng chứng gate vẫn có lịch sử Git; không thay bằng một comment không còn truy vết. |
| `BA_READY`, `STORY_TECH_GAP`, `TECH_READY` và các gate khác | Trường hoặc comment có bằng chứng theo cấu hình Dona thực tế | Không dùng mã trạng thái số trong workflow gốc khi chưa đọc cấu hình hiện hành. Một trạng thái giao việc không tự chứng minh gate. |
| `Blocked by`, phụ thuộc `HARD`/`CONTRACT`/`SOFT` | Quan hệ phụ thuộc hoặc trường/comment trên Dona | Giữ đúng ý nghĩa chặn khởi động và chặn tích hợp; kiểm tra từng quan hệ sau nhập. |
| Ticket Dev ở `.scratch/<feature>/issues/` | Task con của Story sau hai gate | Hiện chưa có ticket Dev đạt gate; không tạo task triển khai chỉ để hoàn tất việc chuyển tracker. |

## Trình tự chuyển

1. Ninh cung cấp URL dự án Dona và quyền tạo/sửa Story. Xác nhận Dona là hệ thống nào, dự án đích, các loại issue/trường/trạng thái có thật và cách truy cập an toàn. Không ghi token hoặc mật khẩu vào Git hay chat.
2. Đọc danh sách Story hiện có trên Dona để phát hiện trùng và ghi bảng ánh xạ `số Story trong repo → ID Dona`. Chọn một Story thử, đối chiếu tiêu đề, mô tả, link tài liệu, gate và quyền xem.
3. Sau khi bản thử đúng, chuyển 34 Story còn lại. Kiểm đủ 35 ánh xạ, phụ thuộc, nguồn yêu cầu và các trạng thái hiện có. #01–#06 chưa có gate theo workflow mới; #07–#09 đã `BA_READY` nhưng còn `STORY_TECH_GAP`; các Story còn lại chưa tự đạt gate.
4. Chỉ khi dữ liệu trên Dona được kiểm đủ, cập nhật [tracker](../agents/issue-tracker.md), [workflow áp dụng](capmm-end-to-end.md), [AGENTS.md](../../AGENTS.md) và [README](../../README.md) để Dona thành nơi quản lý trạng thái. Giữ Markdown làm tài liệu và bản đối chiếu có lịch sử Git, không duy trì hai trạng thái mâu thuẫn.

## Điều chưa có

- URL/ID dự án Dona và quyền truy cập phù hợp.
- Bảng trường, trạng thái, loại issue, cơ chế import/API của đúng Dona mà Ninh dùng.
- Bảng ánh xạ ID thật và bằng chứng đối chiếu sau chuyển.

Workflow gốc [Bot CapMM](workflow-bot-capmm-original.md) mô tả Dona cùng các mã trạng thái lịch sử, nhưng các mã đó không chứng minh cấu hình hiện tại của dự án CapMoney. [Workflow áp dụng](capmm-end-to-end.md) tiếp tục giữ gate theo bằng chứng cho tới khi việc chuyển hoàn tất.
