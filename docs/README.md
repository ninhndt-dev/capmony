# Bản đồ tài liệu CapMoney

Đọc [README gốc](../README.md) để biết trạng thái mã và lệnh chạy; [tổng quan kỹ thuật](project-overview.md) mô tả những phần **đã hiện thực** tại 16/10/2026. Thư mục này còn chứa nguồn yêu cầu, thiết kế và quy trình. Một file được viết sớm có thể là ảnh chụp lịch sử dù tiêu đề vẫn còn trong repo.

| Nhóm | Nguồn cần đọc | Cách dùng |
| --- | --- | --- |
| Quyết định sản phẩm | [Intake](requirements/00-intake.md), [mục tiêu](requirements/goal-and-scope.md), [actor/quyền](requirements/actors-and-roles.md), [nghiệp vụ](requirements/core-business.md), [luồng](requirements/user-flows.md), [chức năng](requirements/system-functions.md) | Phân biệt Ninh đã xác nhận, đề xuất và chưa rõ; không suy yêu cầu mới từ code hoặc ảnh cũ. |
| Công nghệ | [TS10–TS38](requirements/tech-stack-round-2.md), [ADR 0003](adr/0003-java-spring-and-deployment.md) | Java/Spring, React/Vite, JWT/Redis, Docker dev/test và hướng VPS; production vẫn chưa triển khai. [ADR 0001](adr/0001-manager-auth-foundation.md)/[0002](adr/0002-project-infrastructure-and-stack.md) giữ lịch sử các lựa chọn đã bị thay. |
| Thiết kế và bằng chứng tính năng | [Story CM-01](../.scratch/capmoney-manager-setup/story.md), [test spec](../.scratch/capmoney-manager-setup/test-spec.md), [handoff](../.scratch/capmoney-manager-setup/handoff.md) | Đối chiếu AC, ticket, build, người review, QA và blocker. Các module khác nằm trong `.scratch/<feature>/`. |
| Đặc tả chức năng bước 9 | [35 Story nguồn](../.scratch/capmoney-overall/issues/), [PM review CM-10–CM-35](../.scratch/capmoney-overall/step9-ba-review-2026-10-16.md), [nhật ký quyết định](requirements/00-intake.md) | 35/35 Story có spec; CM-10–CM-35 đạt 26/26 `BA_READY` sau R79. Gate kỹ thuật và QA nằm ở từng Story, không suy từ spec. |
| Vận hành dev/test | [Runbook Compose](../infra/README.md), [bootstrap CM-01](../.scratch/capmoney-manager-setup/bootstrap-runbook.md) | Lệnh chạy, secret, CLI và kết quả; không áp dụng làm runbook VPS production. |
| Quy trình | [Workflow CapMM](workflows/capmm-end-to-end.md), [11 bước](agents/workflow.md), [tracker](agents/issue-tracker.md) | Gate theo Story/ticket và trạng thái Planio; Markdown evidence không tự cập nhật issue. |

Khi cập nhật một chức năng, sửa spec/test spec nếu hành vi hoặc tiêu chí đã đổi, ghi build/case vào handoff/evidence của tính năng, rồi cập nhật phần tóm tắt trong [README gốc](../README.md) và [tổng quan kỹ thuật](project-overview.md) nếu phạm vi chạy thay đổi. Chỉ ghi gate khi có người kiểm và bằng chứng theo workflow; trạng thái live của issue cần đối chiếu trên Planio.
