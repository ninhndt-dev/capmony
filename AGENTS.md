# Hướng dẫn agent của CapMoney

Đọc `README.md` và `docs/requirements/00-intake.md` trước khi làm việc. Dự án đang ở **bước 9: đặc tả chi tiết từng module** sau khi Ninh duyệt cách chia 35 Story ứng viên ở bước 8; chúng đã được chuyển sang Planio. Ninh đã chọn hỏi lại mọi yêu cầu từ đầu; không coi cả sáu quyết định cũ là đã chốt cho lần khởi động này. Không tạo app/API, schema hoặc ticket triển khai từ tài liệu hay code cũ.

**Thay đổi stack hiện hành:** Ninh đã chốt BE Java Spring và các lựa chọn chi tiết trong [vòng 2](docs/requirements/tech-stack-round-2.md). [ADR 0002](docs/adr/0002-project-infrastructure-and-stack.md) về Node/Hono bị thay một phần. [INFRA-01.1 / Planio #47](.scratch/capmoney-infrastructure/issues/01-docker-dev-test.md) và CM-01 #43 **tạm dừng gate** để lập ADR/thiết kế Java mới rồi BA → PM → Tech → PM rà lại. Commit Node/Hono `493f3d4` là lịch sử; QA cũ fail IF-T03 và không chứng minh stack Java. Chưa giao code mới trước gate.

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

Chỉ gọi phần mềm đã chạy khi web được kiểm chứng trên iPhone và Android; kiểm chứng API nếu API thuộc phạm vi được Ninh xác nhận. Ninh chọn ảnh CapMoney chỉ để tham khảo giao diện; không đặt tiêu chí giống từng pixel cho dự án mới.

Chi tiết trách nhiệm và 11 bước của Ninh ở `docs/agents/workflow.md`.
