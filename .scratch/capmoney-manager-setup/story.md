# Story CM-01: Thiết lập và đăng nhập tài khoản quản lý

**Planio:** [CM-01 / issue 8](https://tanninh.planio.com/issues/8)
**Lifecycle:** design (quay lại do TS01 thay kiến trúc)
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
| TECH_READY | **Tạm dừng hiệu lực:** đã đạt trên phương án một ứng dụng Next.js ngày 14/10/2026, nhưng TS01 đổi sang React web + API riêng; phải review kiến trúc mới | [technical-design cũ](technical-design.md), [ADR 0001](../../docs/adr/0001-manager-auth-foundation.md), [TS01–TS05](../../docs/requirements/tech-stack-round-1.md) | Reviewer Tech và PM phải rà lại |
| READY_FOR_IMPLEMENTATION | **Tạm dừng CM-01.1 / #43** do phụ thuộc `TECH_READY`; CM-01.2 / #44 và CM-01.3 / #45 vẫn `HARD` blocker | [CM-01.1](issues/01-send-first-setup-link.md), [CM-01.2](issues/02-complete-manager-setup.md), [CM-01.3](issues/03-manager-login-session.md), [handoff](handoff.md) | PM phải xét lại từng ticket sau ADR mới |
| DEV_REVIEWED | chưa có code | diff/commit và review | chưa chạy |
| QA_PASS | chưa có build | test spec, log chạy và thiết bị | chưa chạy |

## Việc tiếp theo

Ninh đã chọn A duyệt ba lát cắt dọc và chuỗi `HARD` CM-01.1 → CM-01.2 → CM-01.3. Planio đã tạo/đọc lại [#43](https://tanninh.planio.com/issues/43), [#44](https://tanninh.planio.com/issues/44), [#45](https://tanninh.planio.com/issues/45) và hai quan hệ `blocks`. Sau đó Ninh đổi hướng kiến trúc toàn dự án theo TS01 và ưu tiên hạ tầng Docker; Dev #43 đã được dừng trước khi viết code. PM giữ các ticket, quay lại Tech/PM review rồi mới phục hồi readiness. Chưa có build hoặc QA.
