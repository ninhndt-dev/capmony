# Q&A INFRA-01

| Mã | Nội dung | Trạng thái và nguồn |
| --- | --- | --- |
| IF-Q1 | Web/API một ứng dụng hay hai dịch vụ? | **Ninh đã xác nhận TS01/B:** React web và API riêng. |
| IF-Q2 | Docker cho dev/test hay chạy thật? | **Ninh đã xác nhận TS06:** dev/test dùng Docker, chạy thật dùng VPS riêng. |
| IF-Q3 | Hạ tầng trước FE/BE ở mức nào? | **Ninh đã xác nhận TS07/A:** sơ đồ/ADR/môi trường/thứ tự, review xong mới dựng Docker. |
| IF-Q4 | DB production ở đâu? | **Ninh đã xác nhận TS08/A:** PostgreSQL trên VPS riêng với volume. |
| IF-Q5 | Có tiếp tục theo bundle ADR 0002? | **Ninh trả lời “continue” sau câu hỏi duyệt A/B**, ghi TS09: tiếp tục theo phương án A và chuyển sang ticket Docker dev/test khi đó. Đây là lịch sử Node/Hono; lựa chọn TS10–TS38 thay phần BE, CSS và production deployment. |

## Rebaseline tech stack đã được Ninh xác nhận

- **Dev/test:** Java 25/Spring Boot 4.1/Maven/Spring MVC/JPA/Liquibase/Security/Mail, PostgreSQL, Redis/Lettuce cho giới hạn thử đăng nhập, React/Vite/Router/Tailwind/TanStack Query, Caddy, Mailpit và Docker dev/test. Nguồn [TS10–TS38](../../docs/requirements/tech-stack-round-2.md).
- **Production về sau:** Ubuntu 24.04, Temurin 25, Java/PostgreSQL/web cài trực tiếp, API chạy `systemd`, Caddy HTTPS; Docker Compose production không còn là lựa chọn hiện hành. Ninh chọn chưa có backup bản đầu và chấp nhận rủi ro mất dữ liệu nếu VPS/volume hỏng. Đây là bối cảnh kiến trúc, chưa thuộc ticket #47 dev/test.
- **Tech đề xuất chờ review:** cùng origin, ranh giới Caddy→Spring, cách chia mạng, health/readiness Redis và Redis trên cùng VPS; không tự gán là Ninh đã xác nhận. Xem [ADR 0003](../../docs/adr/0003-java-spring-and-deployment.md) và [Tech review](java-tech-review.md).

## Chưa rõ không chặn dev/test

- VPS/domain/SMTP thật và chi phí vận hành; không cần để chạy dữ liệu thử local.
- Cách Spring xác thực TCP peer/IP do Caddy chuyển, secret edge và đường CSRF là chi tiết Tech phải chứng minh trong ticket, không tự coi đã chạy. ADR 0003 đang chờ reviewer rà lại sau ba P1.
