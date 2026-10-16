# Hướng dẫn agent của CapMoney

Đọc `README.md`, [tổng quan kỹ thuật](docs/project-overview.md) và `docs/requirements/00-intake.md` trước khi làm việc. Dự án đã **hoàn tất phần đặc tả chức năng bước 9** sau khi Ninh duyệt cách chia 35 Story ứng viên ở bước 8; chúng đã được chuyển sang Planio. 35/35 Story có spec; CM-01 và CM-07–CM-09 đã có BA review, CM-02–CM-06 được rà theo quy trình cũ, còn [CM-10–CM-35](.scratch/capmoney-overall/step9-ba-review-2026-10-16.md) có 26/26 `BA_READY` sau R79. Không tự suy `TECH_READY` từ kết quả BA. Ninh đã chọn hỏi lại mọi yêu cầu từ đầu; không coi sáu quyết định cũ là đã chốt cho lần khởi động này. Mã hạ tầng Java và CM-01 hiện có chỉ có giá trị trong phạm vi/gate đã ghi; không suy tính năng hoặc quyền mới từ code cũ hay Story ứng viên.

**Stack và snapshot hiện hành (16/10/2026):** Ninh đã chốt Java Spring, React/Vite, JWT cookie, Redis và Docker dev/test trong [vòng 2](docs/requirements/tech-stack-round-2.md). [ADR 0002](docs/adr/0002-project-infrastructure-and-stack.md) về Node/Hono là lịch sử ở các phần bị thay. INFRA #47/#48 và CM-01 #43 đã có QA ticket; #44 có bằng chứng tự kiểm nhưng chưa `QA_PASS` độc lập, Planio đã được [đối chiếu](.scratch/capmoney-manager-setup/tracker-reconciliation-2026-10-16.md) về `In Progress/0%`. #45 có code nền `4a4a1a4` và [sửa refresh](.scratch/capmoney-manager-setup/qa45-refresh-unknown-fix.md) ở `6676d5e`, nhưng chưa `DEV_REVIEWED`/`QA_PASS` và còn `HARD` #44 → #45. Đọc [Story](.scratch/capmoney-manager-setup/story.md), [handoff](.scratch/capmoney-manager-setup/handoff.md) và Planio trước khi đổi gate. Không dùng QA Node/Hono cũ hoặc test Dev của #45 để tự cấp nghiệm thu.

## Quyền thao tác Ninh xác nhận ngày 14/10/2026

Ninh cho phép các thao tác cần thiết để triển khai và kiểm thử trong dự án CapMoney mà không hỏi lại về quyền thực hiện từng bước. Quyền này không mở rộng sang dự án khác và không thay thế giới hạn sandbox của phiên.

**Không được xoá tài nguyên dự án:** không xoá file/thư mục, bản ghi dữ liệu, issue, nhánh, volume, container hoặc tài nguyên dịch vụ; không chạy reset/clean/drop/truncate, `docker compose down`, `docker compose down --volumes`, hoặc công cụ tương đương làm mất tài nguyên. Khi kiểm thử cần môi trường sạch, tạo môi trường hoặc dữ liệu thử mới và giữ môi trường cũ. Nếu thật sự cần xoá, phải xin phép riêng. Quy tắc này ưu tiên hơn cleanup/reset trong scripts và skills.

## Agent skills

Các skill được vendored trong `.agents/skills/`. Đọc `SKILL.md` tương ứng trước khi dùng. `setup-matt-pocock-skills` đã được áp dụng cho dự án; khi môi trường có lệnh slash tương ứng thì dùng lệnh đó, còn không thì tuân theo nội dung skill cục bộ. Không tự cài thêm một bản plugin trùng kỹ năng.

### Issue tracker

Trạng thái công việc hiện hành ở [Planio](https://tanninh.planio.com/projects/capmoney); spec và bằng chứng gate sống trong Markdown ở `.scratch/<feature>/`. Xem `docs/agents/issue-tracker.md` và [manifest 35 Story](docs/workflows/redmine-story-manifest.csv).

### Triage labels

Dùng năm trạng thái chuẩn của repo skills. Xem `docs/agents/triage-labels.md`.

### Domain docs

Một ngữ cảnh, `GLOSSARY.md` và `docs/adr/` ở gốc; chỉ tạo khi thuật ngữ hoặc quyết định đã rõ. Xem `docs/agents/domain.md`.

## Luồng làm việc

Từ Story #07 áp dụng [workflow CapMM end to end](docs/workflows/capmm-end-to-end.md) cùng [quy trình 11 bước](docs/agents/workflow.md). Gate `BA_READY`, `TECH_READY` và `READY_FOR_IMPLEMENTATION` cần bằng chứng trong file; các đặc tả #01–#06 không tự đạt gate mới. Không giao Dev trước khi từng ticket đủ điều kiện.

- Bước 1 đã có phiếu tiếp nhận, mười vòng hỏi đáp và các câu hỏi còn mở. Tiếp tục hỏi theo các vòng quyết định độc lập bằng `grill-with-docs`; ghi rõ **Ninh đã xác nhận**, **đề xuất**, **chưa rõ**.
- PM đối chiếu câu trả lời với mục tiêu Ninh vừa xác nhận và chỉ đánh dấu yêu cầu đã chốt khi có bằng chứng từ Ninh.
- Sau khi đủ yêu cầu, dùng `to-spec` và review đặc tả; tiếp theo `to-tickets` tạo các lát cắt nhỏ, mỗi ticket có tiêu chí nghiệm thu và quan hệ phụ thuộc.
- Với từng ticket đã đủ rõ: Tech chọn hợp đồng và điểm kiểm thử; PM review; Dev triển khai theo `implement`/`tdd` khi phù hợp; PM kiểm tra phạm vi; QA kiểm tra hành vi và báo kết quả.
- `implement-spec` chỉ dùng khi có spec/tickets đã được chia và môi trường cho phép phối hợp agent. `code-review` là điểm kết thúc trước khi tích hợp.

Ninh đã chọn Chromium/WebKit giả lập đủ để kiểm QA từng ticket CM-01; iPhone và Android vật lý thuộc nghiệm thu cuối Story theo CM01-Q4. Chỉ gọi phần mềm hoặc gate đã được xác nhận khi có bằng chứng đúng phạm vi; kiểm chứng API nếu API thuộc phạm vi được Ninh xác nhận. Ảnh CapMoney chỉ để tham khảo giao diện, không đặt tiêu chí giống từng pixel cho dự án mới.

Chi tiết trách nhiệm và 11 bước của Ninh ở `docs/agents/workflow.md`.
