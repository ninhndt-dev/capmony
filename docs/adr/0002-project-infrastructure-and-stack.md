# ADR 0002: Tech stack và hạ tầng toàn CapMoney

**Status: bị thay một phần ngày 14/10/2026; chỉ giữ làm lịch sử kiến trúc Node/Hono.** ADR từng được Tech độc lập `/root/infra_tech_review`, PM `/root/cm01_pm_ba` rà và Ninh trả lời “continue” theo TS09. Sau đó Ninh chọn [TS10–TS38](../requirements/tech-stack-round-2.md): BE Java Spring, Tailwind, JWT/Redis và cài trực tiếp trên VPS. Các phần API, dữ liệu truy cập, auth/email, FE styling, production và test bên dưới **không còn là hợp đồng triển khai**. Docker Node/Hono #47 đã có commit `493f3d4` nhưng QA IF-T03 fail rồi dừng vì đổi stack; không có `QA_PASS`. Cần ADR thay thế, Tech/PM review và gate mới trước khi code Java hoặc tiếp tục #43.

## Quyết định lịch sử tại TS09 (đọc cùng thông báo thay đổi ở trên)

| Lớp | Chọn cho toàn dự án | Nguồn/giới hạn |
| --- | --- | --- |
| Web mobile first | React + TypeScript + Vite, một ứng dụng trình duyệt; React Router cho các màn, CSS Modules/semantic HTML cho UI | TS01 xác nhận React web tách API; thư viện công cụ là đề xuất Tech |
| API | Node.js 24 LTS + TypeScript + Hono chạy HTTP service riêng; Zod kiểm dữ liệu ở ranh giới; hợp đồng HTTP phiên bản hóa dưới `/api` | TS01 xác nhận dịch vụ riêng; Hono/Zod là đề xuất Tech, phải review |
| Dữ liệu | PostgreSQL 17 trên volume riêng; `pg`/SQL migration có version, transaction cho biến động tài chính, tham số hóa truy vấn | TS08 xác nhận PostgreSQL trong VPS; driver/migration là đề xuất Tech |
| Xác thực | Cơ chế tài khoản/phiên riêng dùng chung, bắt đầu từ hợp đồng CM-01; token phiên opaque chỉ lưu hash, cookie `HttpOnly`/`Secure`/host-only; quyền kiểm ở API mỗi thao tác, không tự đăng ký | TS03 xác nhận hướng; chi tiết từng Story phải qua BA/Tech/PM gate, không tự mở quyền quản lý |
| Email | SMTP qua Nodemailer, Mailpit dev/test; nhà cung cấp SMTP thật cấu hình sau | TS04 xác nhận giao thức/hộp thử; Nodemailer là đề xuất Tech |
| Edge và triển khai | Docker Compose: Caddy ở biên, web tĩnh, API, PostgreSQL; Mailpit chỉ dev/test. Production trên một VPS riêng, chỉ Caddy mở cổng 80/443; Caddy cấp HTTPS khi có domain | TS06–TS08 xác nhận Docker dev/test và VPS/PG production; Caddy/Compose là đề xuất Tech |
| Kiểm tra và phát hành | TypeScript check/lint, Vitest cho logic, test tích hợp PostgreSQL/Mailpit, Playwright cho trình duyệt; GitHub Actions kiểm tra và build image gắn commit; triển khai production có bước thủ công sau QA | Đề xuất Tech; không tự tạo account/chi phí hoặc tự deploy |

**Một origin cho trình duyệt:** người dùng mở `https://<domain>/`; Caddy **giữ nguyên** prefix `/api/*` và chuyển vào API Hono cũng định tuyến `/api/*`. Các đường khác tới web tĩnh có SPA fallback để `/manager/setup` và deep link vẫn mở được. Web và API là hai service/container, browser chỉ gọi cùng origin. Cookie phiên host-only của domain này có `Path=/` nên không cần cookie đa subdomain/CORS cho ứng dụng bản đầu. API so sánh `Origin` của mọi POST/PUT/PATCH/DELETE với `APP_ORIGIN=https://<domain>` cấu hình riêng, từ chối thiếu/sai Origin và kiểm phiên/quyền trong handler. Lệnh bootstrap CM-01 chỉ là CLI chạy nội bộ bằng quyền người triển khai, **không có HTTP endpoint**.

**Mạng và IP tin cậy:** dùng ba mạng Docker tách biệt: `web_net` chỉ Caddy↔web, `api_net` chỉ Caddy↔API, `data_net` chỉ API↔PostgreSQL; dev/test có thêm `mail_net` chỉ API↔Mailpit. API/DB/Mailpit không publish port ra ngoài. Caddy trực tiếp nhận client, không có CDN/proxy phía trước; Caddy ghi đè `X-Real-IP` và `X-Forwarded-For` bằng địa chỉ TCP peer của chính nó, không tin header IP client gửi. API chỉ nhận IP này khi socket peer đúng địa chỉ Caddy đã cấu hình trên `api_net`, giá trị là **một địa chỉ IP hợp lệ**, thiếu/sai thì fail-closed trước auth/throttle. Đổi topology/proxy phải Tech review lại nguồn IP.

## Lý do và đánh đổi

- Cấu trúc hai service đáp ứng TS01. Một repo với `apps/web`, `apps/api` và hợp đồng chung giữ kiểu dữ liệu và review một nơi; mỗi service build riêng. Web tĩnh sau Caddy, API Node có transaction PostgreSQL phù hợp thao tác tiền, ví và liên kết một lần.
- Docker Compose cho dev/test và một VPS sản xuất giúp cùng sơ đồ dịch vụ được kiểm trước khi chạy thật. PostgreSQL và counter phiên/giới hạn thử là trạng thái chung; không dựa vào bộ nhớ từng API instance.
- Một origin qua Caddy giảm phức tạp cookie/CORS của hai subdomain. Dịch vụ API vẫn là ranh giới mạng riêng, không trộn code FE/BE. Chỉ edge được public; DB/Mailpit không public.
- Một VPS là điểm lỗi đơn. Volume giữ dữ liệu qua restart container nhưng **không phải backup**; S04/R52 chưa yêu cầu khôi phục sau sự cố hạ tầng nặng. Rủi ro mất dữ liệu khi VPS/volume hỏng phải được nhắc lại trước khi dùng thật, không tuyên bố có khả năng khôi phục.
- TS02 ban đầu chọn Vercel/Neon, nhưng TS06/TS08 sau đó thay nơi chạy thật bằng VPS/PostgreSQL trên VPS. Không dùng gói Vercel Hobby cho bản thương mại TS05; không tạo hay trả phí Vercel/Neon.

## Quan hệ với ADR 0001 và gate

ADR này **thay** phần Next.js một ứng dụng và ứng viên Vercel của [ADR 0001](0001-manager-auth-foundation.md). Giữ nguyên các bất biến đã review của CM-01 về email cấu hình, một manager, link 72 giờ, generation/CAS, scrypt, session hash và quyền server, nhưng chuyển chúng sang API riêng sau Caddy. `technical-design.md` CM-01 phải được sửa theo ranh giới mới, Tech khác người lập review lại, PM rà lại phạm vi. `TECH_READY` cũ và `READY_FOR_IMPLEMENTATION` #43 đã tạm dừng; không giao Dev/đánh QA từ ADR đề xuất này.

## Điều kiện trước khi chấp nhận và triển khai

1. Reviewer Tech độc lập kiểm luồng request/cookie/IP/Origin, cô lập DB, persistence và tính khả thi Node/Hono/Caddy/PostgreSQL trong Compose; PM kiểm khớp TS01–TS08 và không đưa tính năng ngoài 35 Story.
2. Ninh đã xác nhận tiếp tục theo bundle (TS09). Nếu đổi lớp có ảnh hưởng CM-01, quay lại gate Tech/PM của Story/ticket.
3. PM tạo ticket hạ tầng riêng đủ gate để dựng Docker dev/test; thử route, health, volume, SMTP Mailpit và lỗi cấu hình. Sau đó mới rebaseline CM-01 #43 và viết FE/BE.
4. Production VPS/domain/SMTP thật, HTTPS và thiết bị iPhone/Android là điều kiện vận hành/QA sau; hiện chưa có quyền truy cập hay bí mật, không tuyên bố hệ thống đã deploy.

## Nguồn kiểm tra khả thi

- [Vite dùng với React](https://vite.dev/guide/), [Hono trên Node](https://hono.dev/docs/getting-started/nodejs), [Node 24 LTS](https://nodejs.org/en/blog/release/v24.18.0).
- [Docker Compose cho production một máy](https://docs.docker.com/compose/how-tos/production/), [Compose secrets](https://docs.docker.com/compose/how-tos/use-secrets), [Caddy reverse proxy](https://caddyserver.com/docs/caddyfile/directives/reverse_proxy) và [HTTPS tự động](https://caddyserver.com/docs/caddyfile/options).
- [PostgreSQL transaction/khóa dòng](https://www.postgresql.org/docs/current/sql-select.html), [node-postgres transaction](https://node-postgres.com/features/transactions), [Nodemailer SMTP](https://nodemailer.com/smtp).
