# Story CM-01: Thiết lập và đăng nhập tài khoản quản lý

**Planio:** [CM-01 / issue 8](https://tanninh.planio.com/issues/8)
**Lifecycle:** specification (rebaseline Java Spring/JWT/Redis theo TS10–TS38)
**Status nguồn bước 8:** needs-triage; trạng thái công việc hiện hành ở Planio.
**Blocked by:** PM review bản BA cập nhật sau khi Ninh chọn CM01-Q2/B trước `BA_READY`; ADR Java/Tech review/PM review trước `TECH_READY`; hạ tầng dev/test Java+Redis [INFRA-01.1 / #47](../capmoney-infrastructure/issues/01-docker-dev-test.md) trước tích hợp #43. Email quản lý thật, tên miền gửi và hạ tầng production chỉ là điều kiện mở vận hành thực tế, không chặn thử bằng dữ liệu riêng.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** chỉ Ninh thiết lập tài khoản quản lý riêng từ email đã khai với người triển khai; liên kết gửi email dùng một lần, hết hạn sau 3 ngày; tự đặt mật khẩu, đăng nhập; không tự đăng ký hay có quản lý dự phòng. Nguồn: [intake](../../docs/requirements/00-intake.md), [quyền](../../docs/requirements/actors-and-roles.md), [F11/F23–F24](../../docs/requirements/user-flows.md), [Story nguồn](../capmoney-overall/issues/01-thiet-lap-quan-ly.md).
- **Phạm vi CM-01:** phát hành liên kết đầu tiên cho email quản lý cấu hình sẵn, hoàn tất thiết lập một lần, đăng nhập, duy trì phiên qua tải lại, kiểm quyền khu vực quản lý và thu hồi token theo CM01-Q2/Q3. [Spec 20 mục](spec.md) quy định AC1–AC7.
- **Ngoài phạm vi:** xin lại liên kết hết hạn (#02), mời người dùng (#03), tài khoản ghi chép (#04), đặt lại mật khẩu (#22), đổi email (#23), dữ liệu tài chính và triển khai thật cho Ninh.
- **Quyết định kiểm chứng mới:** Ninh chọn hộp thư và dữ liệu thử trước, kiểm email thật khi có hạ tầng; xem [Q&A](qna.md). Đây là cách kiểm thử, không đổi quy tắc sản phẩm.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| BA_READY | **Chờ PM review:** BA đã cập nhật spec AC1–AC7 theo TS30–TS34 và Ninh đã chọn CM01-Q2/B, CM01-Q3/A; BA_READY cũ chỉ có giá trị lịch sử | [spec](spec.md), [Q&A](qna.md), [TS10–TS38](../../docs/requirements/tech-stack-round-2.md) | PM rà lại |
| TECH_READY | **Tạm dừng hiệu lực:** thiết kế Next.js/Node/opaque session/DB counter đã bị TS10–TS38 thay; cần ADR và review Java Spring/JWT/Redis | [technical-design cũ](technical-design.md), [ADR 0001](../../docs/adr/0001-manager-auth-foundation.md), [TS10–TS38](../../docs/requirements/tech-stack-round-2.md) | Reviewer Tech và PM phải rà lại |
| READY_FOR_IMPLEMENTATION | **Tạm dừng CM-01.1 / #43** do thiếu BA/TECH gate và hạ tầng Java #47; CM-01.2 / #44 và CM-01.3 / #45 vẫn `HARD` blocker tuần tự | [CM-01.1](issues/01-send-first-setup-link.md), [CM-01.2](issues/02-complete-manager-setup.md), [CM-01.3](issues/03-manager-login-session.md), [handoff](handoff.md) | PM phải xét lại từng ticket sau ADR/thiết kế mới |
| DEV_REVIEWED | chưa có code | diff/commit và review | chưa chạy |
| QA_PASS | chưa có build | test spec, log chạy và thiết bị | chưa chạy |

## Việc tiếp theo

Ninh đã chọn A duyệt ba lát cắt dọc và chuỗi `HARD` CM-01.1 → CM-01.2 → CM-01.3. Planio đã tạo/đọc lại [#43](https://tanninh.planio.com/issues/43), [#44](https://tanninh.planio.com/issues/44), [#45](https://tanninh.planio.com/issues/45) và hai quan hệ `blocks`. Sau TS10–TS38, Ninh đã chốt CM01-Q2/B và CM01-Q3/A; PM cần kiểm spec BA mới, Tech/PM lập và review thiết kế Java/JWT/Redis; #43 vẫn dừng trước code và chờ #47 để tích hợp, #44/#45 vẫn bị chặn tuần tự. Không dùng QA stack Node/Hono cho CM-01.
