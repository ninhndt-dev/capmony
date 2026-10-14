# Story CM-01: Thiết lập và đăng nhập tài khoản quản lý

**Planio:** [CM-01 / issue 8](https://tanninh.planio.com/issues/8)
**Lifecycle:** planning
**Status nguồn bước 8:** needs-triage; trạng thái công việc hiện hành ở Planio.
**Blocked by:** None cho đặc tả và triển khai CM-01. Email quản lý thật, tên miền gửi và hạ tầng thật là điều kiện mở vận hành thực tế, không phải điều kiện viết/kiểm luồng bằng dữ liệu thử.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** chỉ Ninh thiết lập tài khoản quản lý riêng từ email đã khai với người triển khai; liên kết gửi email dùng một lần, hết hạn sau 3 ngày; tự đặt mật khẩu, đăng nhập; không tự đăng ký hay có quản lý dự phòng. Nguồn: [intake](../../docs/requirements/00-intake.md), [quyền](../../docs/requirements/actors-and-roles.md), [F11/F23–F24](../../docs/requirements/user-flows.md), [Story nguồn](../capmoney-overall/issues/01-thiet-lap-quan-ly.md).
- **Phạm vi CM-01:** phát hành liên kết đầu tiên cho email quản lý cấu hình sẵn, hoàn tất thiết lập một lần, đăng nhập, duy trì phiên qua tải lại, kiểm quyền khu vực quản lý. [Spec 20 mục](spec.md) quy định AC1–AC6.
- **Ngoài phạm vi:** xin lại liên kết hết hạn (#02), mời người dùng (#03), tài khoản ghi chép (#04), đặt lại mật khẩu (#22), đổi email (#23), dữ liệu tài chính và triển khai thật cho Ninh.
- **Quyết định kiểm chứng mới:** Ninh chọn hộp thư và dữ liệu thử trước, kiểm email thật khi có hạ tầng; xem [Q&A](qna.md). Đây là cách kiểm thử, không đổi quy tắc sản phẩm.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| BA_READY | **Đạt ngày 14/10/2026**: 20 mục và AC1–AC6 có nguồn, PM rà không còn P0/P1 BA | [spec](spec.md), [Q&A](qna.md), [handoff](handoff.md) | `/root/cm01_pm_ba` độc lập |
| TECH_READY | **Đạt ngày 14/10/2026**: thiết kế, spike DB/SMTP/HTTP, ADR và PM scope review; chưa phải kiểm chứng app | [technical-design](technical-design.md), [ADR 0001](../../docs/adr/0001-manager-auth-foundation.md), [handoff](handoff.md) | `/root/cm01_tech_review` độc lập, `/root/cm01_pm_ba` PM độc lập |
| READY_FOR_IMPLEMENTATION | ba ticket dự thảo đã được Ninh duyệt cách chia; chưa có issue con Planio, chưa ticket nào ready | [CM-01.1](issues/01-send-first-setup-link.md), [CM-01.2](issues/02-complete-manager-setup.md), [CM-01.3](issues/03-manager-login-session.md), [handoff](handoff.md) | BA/Tech đã rà nội dung; PM gate từng ticket chờ Planio |
| DEV_REVIEWED | chưa có code | diff/commit và review | chưa chạy |
| QA_PASS | chưa có build | test spec, log chạy và thiết bị | chưa chạy |

## Việc tiếp theo

Ninh đã chọn A duyệt ba lát cắt dọc và chuỗi `HARD` CM-01.1 → CM-01.2 → CM-01.3. PM chờ Planio API key cục bộ để tạo và đọc lại issue cấp dưới, sau đó xét `READY_FOR_IMPLEMENTATION` từng ticket. Chưa có code ứng dụng hoặc build.
