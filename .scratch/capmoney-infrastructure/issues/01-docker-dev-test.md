# INFRA-01.1: Dựng môi trường Docker dev/test

**Parent:** [Planio #46 – INFRA-01](https://tanninh.planio.com/issues/46).
**Planio child:** [#47 – INFRA-01.1](https://tanninh.planio.com/issues/47), đã tạo và đọc lại ngày 14/10/2026.
**Status:** `ready-for-agent`; `READY_FOR_IMPLEMENTATION` đạt ngày 14/10/2026 sau BA/TECH gate, Tech và PM review ticket, đọc lại Planio. Planio hiện `In Progress` khi DevOps bắt đầu; status tracker không thay gate.
**Blocked by:** None; ADR 0002/`BA_READY`/`TECH_READY` đã đạt ở mức thiết kế.
**Integration blocked by:** None để dựng hạ tầng thử; không phụ thuộc CM-01 code. #47 `blocks` CM-01 #43 trên Planio (relation #8).
**Owner / review:** Codex `/root` trong vai DevOps Dev; reviewer code khác người thực hiện và PM rà; QA độc lập chạy build.

## What it delivers

Người triển khai chạy một lệnh để có môi trường Docker dev/test gồm web placeholder, API health, PostgreSQL bền qua restart và Mailpit; truy cập web/API qua một Caddy origin local, còn các dịch vụ nội bộ không mở cổng ra ngoài. Môi trường này là đường thử hạ tầng cho các ticket sản phẩm về sau, chưa có tính năng CM-01.

## Acceptance criteria tham chiếu

- [ ] `AC-IF01-1` – Compose/secret/dữ liệu thử.
- [ ] `AC-IF01-2` – route cùng origin, SPA deep link, API/DB health.
- [ ] `AC-IF01-3` – port/network, IP/Origin probe fail-closed.
- [ ] `AC-IF01-4` – PostgreSQL volume và cô lập reset.
- [ ] `AC-IF01-5` – SMTP Mailpit và lỗi gửi.

Nội dung AC ở [spec](../spec.md); không chép lại thành AC mới. Năm mã AC ≤5 theo workflow.

## Hợp đồng, dữ liệu thử và bằng chứng

- Theo [ADR 0002](../../../docs/adr/0002-project-infrastructure-and-stack.md) và [technical-design](../technical-design.md): React/Vite web placeholder, Hono/Node 24 API health, Caddy giữ `/api`, các mạng `web_net/api_net/data_net/mail_net`, Caddy IP cố định, PostgreSQL 17 volume, Mailpit dev/test; secret ngoài Git. Bootstrap manager chỉ là CLI nội bộ ở ticket CM-01 sau.
- Dữ liệu: DB tên thử riêng, marker không nhạy cảm, email `manager@example.test`; không dùng tài khoản, tiền hay SMTP thật. `APP_ORIGIN` là loopback Caddy. Dữ liệu test có project/volume riêng với dev.
- Demo V1–V6 trong [technical-design](../technical-design.md) bằng lệnh và log đã che secret trên commit/build cụ thể; test browser `/`, `/manager/setup`, `/api/health`, port/network, Origin/IP giả, marker sau recreate, thư Mailpit/lỗi SMTP. Probe POST chỉ nằm trong test runner, không có trong image sản phẩm; kiểm Mailpit qua CLI/test runner nội bộ, không cần host port.
- Chỉ nhận `DEV_REVIEWED` sau reviewer khác người code và PM rà scope; `QA_PASS` chỉ từ người kiểm thử độc lập thực chạy, không suy từ tài liệu hoặc spike CM-01.

## Rủi ro

Không mở host port cho API/DB/Mailpit; dev Caddy chỉ bind loopback. Nếu không chứng minh được socket peer Caddy và header IP theo topology thật, dừng auth nhạy cảm và trả Tech. Volume không phải backup; production VPS/TLS/SMTP thật ngoài ticket. Không đánh dấu CM-01 #43 ready từ việc ticket này xong.
