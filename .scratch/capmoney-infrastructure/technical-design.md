# Technical Design: INFRA-01 – Docker dev/test

**Lịch sử, không còn TECH_READY hiện hành:** thiết kế này dành cho Node/Hono theo [ADR 0002](../../docs/adr/0002-project-infrastructure-and-stack.md). Ninh đã chọn Java Spring/Redis trong [TS10–TS38](../../docs/requirements/tech-stack-round-2.md); cần thiết kế và review kỹ thuật mới trước khi dùng làm hợp đồng triển khai. Compose Node/Hono từng chạy nhưng QA IF-T03 fail và đã dừng.

## Hợp đồng môi trường

- Monorepo có `apps/web` và `apps/api` tối thiểu, chỉ trả placeholder/health để thử route. Không đưa hành vi CM-01 hoặc dữ liệu tài chính vào INFRA-01. Image Node 24 cho API, Vite/React cho web, PostgreSQL 17, Mailpit và Caddy pin version/digest khi thực hiện.
- Compose dev/test: `web_net` Caddy↔web; `api_net` Caddy↔API; `data_net` API↔PG; `mail_net` API↔Mailpit. Chỉ Caddy publish `127.0.0.1:<port>`; Mailpit UI nếu cần quan sát thì đi qua đường chỉ cho local/test được chứng minh, không mở public. API/DB/Mailpit không có host port.
- Caddy giữ path `/api/*` vào Hono; đường web khác tới static server với SPA fallback. API `/api/health` chỉ trả JSON trạng thái và kiểm kết nối DB; endpoint health không tạo hay đọc dữ liệu tài chính. Lệnh thử SMTP là CLI nội bộ hoặc test runner, không có HTTP endpoint công khai.
- Config tách `.env.example` chỉ chứa tên biến/giá trị giả, secret thật ở file ngoài Git quyền hạn chế. Compose secrets cho PG password/SMTP khi cần; `docker compose config` không được in giá trị. Dev/test có project name, volume, network và DB riêng, không gắn credential/URL production.
- Caddy trực tiếp nhận TCP client; ghi đè `X-Real-IP` và `X-Forwarded-For` theo TCP peer. `api_net` có subnet pin, Caddy có `ipv4_address` cố định. API dùng [Hono `getConnInfo`](https://hono.dev/docs/helpers/conninfo) trên Node để đọc TCP peer thật, chỉ tin IP header khi peer khớp IP Caddy đã cấu hình. Yêu cầu **cả `X-Real-IP` và `X-Forwarded-For` chứa đúng một IP hợp lệ và bằng nhau**; thiếu/sai/nhiều IP thì fail-closed. Test probe POST nội bộ dùng cùng helper IP/Origin dự kiến cho auth, không đưa probe thành endpoint sản phẩm hoặc dựa vào header client tự gửi. API từ chối thao tác nhạy cảm nếu sai nguồn/Origin; health GET không cấp quyền.

## Kiểm chứng phải chạy

| Mã | Cách kiểm | Kết quả cần có |
| --- | --- | --- |
| V1 | `docker compose config`, scan Git/image/log | Không thấy secret thật, không có production DB URL |
| V2 | `docker compose up --build`, GET `/`, `/manager/setup`, `/api/health` qua Caddy | Web/deep link/API đúng route, DB lỗi trả health không healthy |
| V3 | `docker inspect` network/ports, thử kết nối từ ngoài các container và host | Chỉ Caddy loopback host, web không chạm DB, API/DB/Mailpit không public |
| V4 | Probe POST test-only với `Origin` thiếu/sai; spoof `X-Real-IP`/`X-Forwarded-For`, peer giả, header nhiều IP | API từ chối Origin thiếu/sai trước đổi trạng thái; Caddy ghi đè IP client gửi; API fail-closed khi peer/header sai |
| V5 | Ghi marker vào PG test, restart/recreate rồi đọc lại; reset môi trường test | Marker còn qua restart; test reset không tác động dev |
| V6 | Gửi thư thử nội bộ đến Mailpit, cắt SMTP/thử timeout | Hộp thử nhận một thư; lỗi báo không chắc đã gửi/không báo thành công |

Kết quả V1–V6 phải có lệnh, exit code, commit/build và log đã che bí mật; chưa chạy nên không ghi `TECH_READY`/`QA_PASS` từ bảng này. Reviewer Tech kiểm ranh giới trước khi PM giao Dev; reviewer code khác người Dev và QA độc lập chạy trên build sau đó.

## Rủi ro/phụ thuộc

- Docker engine/network local trong sandbox có thể cần quyền; nếu không chạy được, không báo test pass. Caddy IP tĩnh trong Docker `api_net` phải được cấp đúng subnet và thử trong môi trường thật, nếu không chọn cơ chế peer tin cậy khác qua review trước khi Dev auth.
- Một VPS production là điểm lỗi đơn; volume không backup. Production Compose/TLS/SMTP thật không thuộc ticket dev/test và phải có gate/QA sau khi Ninh cung cấp VPS/domain.
- CM-01 #43 vẫn tạm dừng readiness đến khi technical-design auth/email cập nhật ranh giới API và PM cấp lại gate.
- INFRA-01 kiểm một API instance và DB/network; hai instance đồng thời dùng chung DB/session/throttle sẽ được chạy ở CM-01 [T08/T14](../capmoney-manager-setup/test-spec.md), không tính là đã chứng minh ở ticket hạ tầng.
