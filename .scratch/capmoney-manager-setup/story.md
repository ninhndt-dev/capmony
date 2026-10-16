# Story CM-01: Thiết lập và đăng nhập tài khoản quản lý

**Planio:** [CM-01 / issue 8](https://tanninh.planio.com/issues/8)
**Lifecycle tại 16/10/2026:** triển khai #44/#45 đã có mã; #43 Closed/QA 6/6 trên `f63efe4`. #44 có bằng chứng tự kiểm nhưng còn QA độc lập/PM; #45 có mã trên `4a4a1a4` nhưng còn gate #44, code review và QA. Story chưa `QA_PASS`.
**Status nguồn bước 8:** needs-triage; trạng thái công việc hiện hành ở Planio.
**Blocked by:** #43 QA_PASS đã giải HARD tới #44. #44 chưa có QA độc lập/PM chốt nên HARD #44 → #45 chưa được giải chính thức, dù code #45 đã được triển khai theo yêu cầu của Ninh. Thiết bị thật ở nghiệm thu cuối Story theo CM01-Q4. Email quản lý thật, tên miền gửi và hạ tầng production là điều kiện mở vận hành thực tế, không chặn thử bằng dữ liệu riêng.

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
| READY_FOR_IMPLEMENTATION | #43 và #44 đã được PM cấp theo thứ tự sau QA ticket trước; #45 **chưa được cấp** vì #44 chưa QA độc lập/PM chốt. Code #45 đã có không tự giải HARD | [ticket #43](issues/01-send-first-setup-link.md), [#44](issues/02-complete-manager-setup.md), [#45](issues/03-manager-login-session.md), [handoff](handoff.md) | #45 còn chờ |
| DEV_REVIEWED | #43/#44 có handoff review; #45 đã triển khai nhưng chưa được reviewer khác người code/PM xác nhận | [bằng chứng Dev #45](qa45-dev-evidence.md), [handoff](handoff.md) | #45 còn chờ |
| QA_PASS | #43 có QA ticket 6/6; #44 có [bằng chứng tự chạy](qa44-evidence.md) nhưng chưa QA độc lập; #45 có [bằng chứng Dev](qa45-dev-evidence.md) nhưng chưa QA ticket; Story còn T21 và thiết bị thật | [test spec](test-spec.md), [handoff](handoff.md) | #44/#45/Story còn chờ |

## Việc tiếp theo

Ninh đã chọn A duyệt ba lát cắt dọc và chuỗi `HARD` CM-01.1 → CM-01.2 → CM-01.3. Planio có [#43](https://tanninh.planio.com/issues/43), [#44](https://tanninh.planio.com/issues/44), [#45](https://tanninh.planio.com/issues/45) và quan hệ `blocks`. Bước tiếp theo là QA/PM độc lập chốt #44 trên bằng chứng và build được pin, sau đó reviewer/QA kiểm #45 (T13–T20), cuối cùng regression T21 và thiết bị vật lý để xét Story. [Handoff](handoff.md) chứa nhật ký theo thời điểm; trạng thái live của issue phải đọc trên Planio. Không dùng QA stack Node/Hono làm bằng chứng cho CM-01 Java.
