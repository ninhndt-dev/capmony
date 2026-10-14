# Story CM-01: Thiết lập và đăng nhập tài khoản quản lý

**Planio:** [CM-01 / issue 8](https://tanninh.planio.com/issues/8)
**Lifecycle:** implementing #44; #43Closed/QA6/6 trênf63efe4. #44READY15/10/2026, #45 chờQA44.
**Status nguồn bước 8:** needs-triage; trạng thái công việc hiện hành ở Planio.
**Blocked by:** #43 không còn HARD: INFRA#47/#48QA_PASS vàClosed, BA/Tech recheck khôngP0/P1, PM cấpREADY_FOR_IMPLEMENTATION. #43QA_PASS giảiHARD#44; #45 chờQA44; thiết bị thật ở nghiệm thu cuốiStory theoCM01-Q4. Email quản lý thật, tên miền gửi và hạ tầng production chỉ là điều kiện mở vận hành thực tế, không chặn thử bằng dữ liệu riêng.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** chỉ Ninh thiết lập tài khoản quản lý riêng từ email đã khai với người triển khai; liên kết gửi email dùng một lần, hết hạn sau 3 ngày; tự đặt mật khẩu, đăng nhập; không tự đăng ký hay có quản lý dự phòng. Nguồn: [intake](../../docs/requirements/00-intake.md), [quyền](../../docs/requirements/actors-and-roles.md), [F11/F23–F24](../../docs/requirements/user-flows.md), [Story nguồn](../capmoney-overall/issues/01-thiet-lap-quan-ly.md).
- **Phạm vi CM-01:** phát hành liên kết đầu tiên cho email quản lý cấu hình sẵn, hoàn tất thiết lập một lần, đăng nhập, duy trì phiên qua tải lại, kiểm quyền khu vực quản lý và thu hồi token theo CM01-Q2/Q3. [Spec 20 mục](spec.md) quy định AC1–AC7.
- **Ngoài phạm vi:** xin lại liên kết hết hạn (#02), mời người dùng (#03), tài khoản ghi chép (#04), đặt lại mật khẩu (#22), đổi email (#23), dữ liệu tài chính và triển khai thật cho Ninh.
- **Quyết định kiểm chứng mới:** Ninh chọn hộp thư và dữ liệu thử trước, kiểm email thật khi có hạ tầng; xem [Q&A](qna.md). Đây là cách kiểm thử, không đổi quy tắc sản phẩm.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| BA_READY | **Đạt ngày 14/10/2026 cho bản Java/JWT:** PM độc lập đối chiếu 20 mục/AC1–AC7 với F11/F23–F24, R12/R13/R22, TS10–TS38, CM01-Q1–Q3; BA_READY cũ chỉ có giá trị lịch sử | [spec](spec.md), [Q&A](qna.md), [TS10–TS38](../../docs/requirements/tech-stack-round-2.md), [handoff](handoff.md) | `/root/pm_ba_rebaseline` |
| TECH_READY | **Đạt ngày 14/10/2026 cho Java/JWT/Redis:** reviewer độc lập kiểm lại ba P1 về mốc 72 giờ, logout đồng thời và khóa JWT; PM chấp nhận đề xuất 7 ngày tuyệt đối, quota và Redis fail closed trong phạm vi CM-01 | [thiết kế Java](technical-design-java.md), [review Tech](java-tech-review.md), [handoff PM](handoff.md) | `/root/cm01_tech_review` và `/root/pm_ba_rebaseline` |
| READY_FOR_IMPLEMENTATION | **#43–#45 chưa đạt:** ticket/test spec đang rebaseline Java và cần BA/Tech ticket review, PM xét riêng; #43 có HARD #47, #44 chờ #43, #45 chờ #44 | [CM-01.1](issues/01-send-first-setup-link.md), [CM-01.2](issues/02-complete-manager-setup.md), [CM-01.3](issues/03-manager-login-session.md), [handoff](handoff.md) | chưa cấp |
| DEV_REVIEWED | chưa có code | diff/commit và review | chưa chạy |
| QA_PASS | chưa có build | test spec, log chạy và thiết bị | chưa chạy |

## Việc tiếp theo

Ninh đã chọn A duyệt ba lát cắt dọc và chuỗi `HARD` CM-01.1 → CM-01.2 → CM-01.3. Planio đã tạo/đọc lại [#43](https://tanninh.planio.com/issues/43), [#44](https://tanninh.planio.com/issues/44), [#45](https://tanninh.planio.com/issues/45) và hai quan hệ `blocks`. Sau TS10–TS38, Ninh đã chốt CM01-Q2/B và CM01-Q3/A; BA/PM và Tech độc lập/PM đã cấp lại hai gate cấp Story. PM đang rebaseline ticket/test spec Java, từng ticket còn cần BA/Tech rà và PM xét riêng; #43 chờ #47 QA_PASS, #44/#45 bị chặn tuần tự. Không dùng QA stack Node/Hono cho CM-01.
