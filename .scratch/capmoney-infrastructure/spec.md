# Feature Spec: INFRA-01 – Docker dev/test trước FE/BE

**Story:** [story](story.md) · **Nguồn hiện hành:** [TS01/TS04/TS06–TS08](../../docs/requirements/tech-stack-round-1.md), [TS10–TS38](../../docs/requirements/tech-stack-round-2.md), [ADR 0003 đề xuất](../../docs/adr/0003-java-spring-and-deployment.md), [sơ đồ Java đề xuất](architecture-java-proposal.md).
**Rebaseline BA/PM 14/10/2026:** Bản 20 mục này đã đối chiếu Java Spring/Redis/Tailwind và Docker chỉ cho dev/test. Gate BA/TECH cũ là lịch sử Node/Hono; PM độc lập đã xác nhận `BA_READY` cho bản Java/Redis. Tech design/review và PM vẫn cần trước khi ticket #47 được xét readiness. QA cũ IF-T03 fail, không `QA_PASS`.

## 1. Executive Summary / User Story statement

Là người triển khai CapMoney, tôi cần khởi động một môi trường dev/test mô phỏng ranh giới web, API, dữ liệu và email của kiến trúc đã duyệt, để các ticket CM-01 và Story khác có chỗ chạy/kiểm tra mà không chạm dữ liệu thật. Ninh đã chọn hạ tầng trước FE/BE theo TS07.

## 2. Actor & Permission Matrix

| Actor | Được làm | Không được làm |
| --- | --- | --- |
| Người triển khai trên máy dev | Khởi động/dừng/reset **dữ liệu thử**; xem health và Mailpit local | Dùng dữ liệu thật, lộ secret vào Git, mở DB/API/Mailpit ra Internet |
| Người dùng web thử | Mở trang placeholder và health proxy trên loopback | Truy cập trực tiếp DB, API container, Mailpit từ máy ngoài |
| Ninh | Duyệt kiến trúc/ticket và xem bằng chứng test | Chưa cần tài khoản/VPS/SMTP thật ở ticket này |

Không áp dụng quyền xem/sửa tài chính hoặc tài khoản quản lý vì INFRA-01 chưa có nghiệp vụ sản phẩm.

## 3. Screen Definition & Entry Points

Không có màn sản phẩm. Một trang web placeholder tối thiểu ở `/` và SPA deep link `/manager/setup` dùng để chứng minh route; nội dung phải ghi rõ đây là trang thử hạ tầng, không giả làm luồng thiết lập hoàn chỉnh. API chỉ có `/api/health` trả trạng thái tối thiểu, không tiết lộ secret/DB URL.

## 4. Main Flows (Happy Path)

1. Người triển khai chuẩn bị secret và dữ liệu test ngoài Git, chạy Docker Compose dev/test.
2. Caddy local trên loopback nhận `/`, deep link và `/api/health`; chuyển tới web React/Vite và API Java Spring MVC với `/api` giữ nguyên theo ADR 0003 đề xuất.
3. API kiểm kết nối PostgreSQL và trạng thái Redis cho đường xác thực; Mailpit nhận một thư thử từ đường CLI/kiểm thử nội bộ; DB test giữ một marker qua restart/recreate container.
4. Người triển khai xem log/health và lưu bằng chứng commit/image, không đánh dấu chức năng sản phẩm đã chạy.

## 5. Alternative & Exceptional Flows

- Thiếu secret, PostgreSQL/Redis chưa sẵn sàng hoặc SMTP từ chối: khởi động, readiness hoặc phép thử riêng cho lỗi vận hành có thể quan sát; không dùng giá trị production, không im lặng báo thành công.
- Client gửi header IP giả, Origin thiếu/sai hoặc truy cập cổng API/DB trực tiếp: ranh giới mạng/chính sách phải từ chối theo test spec; vì chưa có auth POST sản phẩm, điểm kiểm security dùng probe POST dành riêng cho test và không phát hành thành endpoint sản phẩm.
- Khởi động lại web/API/PostgreSQL/Redis: API không chết vĩnh viễn do dịch vụ phụ trợ tạm mất; readiness phản ánh lỗi và phục hồi sau khi dịch vụ trở lại. Volume PostgreSQL giữ marker; reset dữ liệu thử là thao tác riêng và không lẫn production.

## 6. Field-level Specifications & Validations

Không có biểu mẫu nghiệp vụ. `APP_ORIGIN` local phải là URL loopback Caddy; DB và Redis dùng dữ liệu/cấu hình thử tách test; SMTP local trỏ Mailpit; cấu hình sai/thiếu không được rơi về secret production. Không ghi secret vào log/`docker compose config` hoặc image.

## 7. System Parameters & Constants (`PARAM.*`)

- `PARAM.IF01.PUBLIC_DEV_BIND = 127.0.0.1` (port cụ thể do Tech chọn, ghi trong README).
- `PARAM.IF01.API_PREFIX = /api` giữ nguyên qua Caddy tới Spring MVC.
- `PARAM.IF01.PROD_PUBLIC_PORTS = 80,443` chỉ là hợp đồng production để kiểm khi có VPS, không phải test đã chạy.
- Image/service versions phải pin cụ thể trong lockfile/Compose; không dùng `latest` cho môi trường có bằng chứng QA.

## 8. Business Rules (`BR-*`)

- `BR-IF01-01`: chỉ Caddy được publish cổng local; API, DB và Mailpit không publish tới Internet. **Ví dụ:** request HTTP tới `/api/health` qua Caddy có phản hồi, kết nối trực tiếp tới cổng DB trên host bị từ chối.
- `BR-IF01-02`: dev/test/prod có nguồn dữ liệu và secret riêng. **Ví dụ:** test reset DB không đổi dữ liệu dev; không có production URL trong container test.
- `BR-IF01-03`: một origin ở browser với `/api` giữ prefix và SPA fallback. **Ví dụ:** tải lại `/manager/setup` trả trang web thử, `/api/health` trả JSON từ API.
- `BR-IF01-04`: volume giữ dữ liệu qua restart/recreate, nhưng không được gọi là backup. **Ví dụ:** marker thử còn sau `up --force-recreate`; mất volume không có cam kết khôi phục.

## 9. State Machine & Transition Rules (`STATE.*`)

`not-started → starting → healthy | degraded → stopped`. **API health** phản ánh API và PostgreSQL; đường xác thực có chỉ báo readiness/degraded riêng khi Redis lỗi. Route web qua Caddy và SMTP/Mailpit được kiểm riêng theo AC-IF01-2/5; không suy từ một health endpoint là toàn stack healthy. Dừng container không xóa volume; lệnh reset test là chuyển trạng thái có chủ đích.

## 10. Data Fallback & Offline Handling

Không có offline sản phẩm theo R11. PostgreSQL test là nguồn trạng thái duy nhất cho môi trường này; khi DB lỗi, API health báo không sẵn sàng, không tự ghi tạm vào bộ nhớ. Volume không có tính năng backup theo S04/R52.

## 11. Error Messages & UI Registry (`ERR.*`)

Không áp dụng copy UI sản phẩm. Log/health dùng lỗi vận hành ngắn gọn như `db_unavailable`, `smtp_unavailable` nhưng không chứa secret/stack trace cho browser. Lệnh setup phải chỉ dẫn file cấu hình còn thiếu mà không in giá trị.

## 12. Security & Privacy Constraints

**Tech đề xuất cần review/test:** tách đường web/API/PostgreSQL/Redis/Mailpit trong Compose; Caddy nhận request trực tiếp, ghi đè IP header, API kiểm TCP peer và secret edge trước khi tin IP cho throttle, từ chối request nhạy cảm nếu sai. Chính xác mạng, header, secret và probe theo [ADR 0003 đề xuất](../../docs/adr/0003-java-spring-and-deployment.md), chưa phải bằng chứng Java đã chạy. Caddy bind loopback ở dev/test; production TLS sau khi có VPS/domain là phạm vi giai đoạn khác. Secret ngoài Git, không dùng mật khẩu thử chung production; không có endpoint HTTP khởi tạo manager.

## 13. Third-party Integrations

Docker Engine/Compose local và image pin version; Java 25/Temurin, Spring Boot 4.1/Maven, Spring Data JPA/Liquibase, Redis/Lettuce, React/Vite/Tailwind, Caddy, PostgreSQL và Mailpit theo TS10–TS38. INFRA-01 chỉ dựng placeholder/health và kết nối tối thiểu, chưa triển khai auth hay tiền. Không cần Vercel, Neon, VPS/domain hoặc SMTP thật trong ticket này. Nếu pull image, ghi version/hash và nguồn; không tạo tài khoản trả phí.

## 14. Audit Log & History Requirements

Không áp dụng audit người dùng/giao dịch vì chưa có nghiệp vụ. Cần lưu commit, image/Compose version, lệnh và log test đã che secret trong ticket/handoff. Không log token/mật khẩu thật.

## 15. NFR (Performance, Concurrency Bounds)

Môi trường cho phép chạy hai API instance test dùng chung PostgreSQL/Redis ở ticket CM-01 sau. INFRA-01 chứng minh một API instance và hợp đồng DB/Redis/network; phép kiểm **hai instance đồng thời** thuộc test CM-01 sau rebaseline. Health/readiness có timeout hữu hạn và hồi phục khi DB/Redis trở lại; startup không dựa duy nhất vào thứ tự Compose. Không có SLA production được Ninh chốt.

## 16. Acceptance Criteria (`AC-IF01-*`)

- `AC-IF01-1`: trên máy dev, Compose pin version dựng được Caddy, web React/Vite placeholder, API health Java Spring, PostgreSQL, Redis và Mailpit từ dữ liệu/secret thử; `compose config`/image/Git không lộ giá trị secret.
- `AC-IF01-2`: qua **cùng origin loopback**, `/` và tải lại `/manager/setup` trả web thử, `/api/health` trả JSON Java với prefix `/api` giữ nguyên; lỗi PostgreSQL làm health không báo healthy, lỗi Redis làm readiness cho xác thực báo degraded; API hồi phục sau khi từng dịch vụ trở lại mà không cần build lại.
- `AC-IF01-3`: chỉ Caddy được bind host loopback; API/PostgreSQL/Redis/Mailpit không có port host public; các mạng cô lập theo thiết kế Tech đã review. Probe POST **chỉ trong test, không thành endpoint sản phẩm** chứng minh API từ chối IP header giả, peer/secret edge sai và `Origin` thiếu hoặc sai. CSRF cho setup/login/refresh/logout thuộc thiết kế và test CM-01, không được coi đã chứng minh từ probe hạ tầng.
- `AC-IF01-4`: PostgreSQL test giữ marker qua restart/recreate container; test reset riêng không dùng dữ liệu dev/prod. Không tuyên bố đây là backup.
- `AC-IF01-5`: Mailpit nhận thư thử qua đường nội bộ; khi SMTP lỗi/timeout, phép thử báo lỗi rõ, không nói đã gửi. Không tạo endpoint sản phẩm để gửi mail thử.

## 17. Figma Mapping / Visual References

Không áp dụng: đây là môi trường hạ tầng, không phải màn sản phẩm. Ảnh CapMoney cũ chỉ tham khảo UI về sau.

## 18. Dependencies (Cross-feature / Cross-story)

TS01/TS04/TS06–TS08 và TS10–TS38 là đầu vào; ADR 0002 chỉ là lịch sử stack cũ. INFRA-01 là enabler cho CM-01.1/Planio #43 qua #47; không tự cấp `TECH_READY` CM-01 vì thiết kế auth/email/JWT/Redis phải được review riêng. Sau INFRA-01, từng ticket sản phẩm vẫn qua gate độc lập.

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md) IF-Q1–IF-Q5 và [TS10–TS38](../../docs/requirements/tech-stack-round-2.md). **Ninh đã xác nhận:** hai service, Docker dev/test, Java Spring API, React/Vite/Tailwind, Caddy, PostgreSQL, Redis/Lettuce, Mailpit và production cài trực tiếp Ubuntu/Temurin/systemd/PostgreSQL lên VPS. **Tech đề xuất chờ review:** topology mạng/secret/probe, Redis trên cùng VPS và hợp đồng health/readiness. **Chưa rõ cho vận hành:** VPS/domain/SMTP thật; không chặn ticket dev/test.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 14/10/2026 | Tạo spec 20 mục INFRA-01 từ TS01–TS09/ADR 0002; AC-IF01-1–5, chưa đánh dấu BA_READY | [Q&A](qna.md), [ADR 0002](../../docs/adr/0002-project-infrastructure-and-stack.md) |
| 14/10/2026 | Theo PM review: AC-IF01-3 nêu rõ probe POST Origin thiếu/sai; health API chỉ phản ánh API/DB | [handoff](handoff.md) |
| 14/10/2026 | Theo PM Tech review: hai API instance thuộc kiểm CM-01; giữ một instance trong INFRA-01, bổ sung V4 Origin trong technical-design | [technical-design](technical-design.md) |
| 14/10/2026 | PM độc lập xác nhận `BA_READY` và `TECH_READY` ở mức thiết kế sau reviewer Tech; cho phép tạo ticket Compose, chưa chứng minh V1–V6 | [handoff](handoff.md) |
| 14/10/2026 | BA cập nhật 20 mục/AC-IF01-1–5 cho Java Spring/Redis theo TS10–TS38; chờ PM review, chưa cấp gate Java | [tech stack vòng 2](../../docs/requirements/tech-stack-round-2.md), [ADR 0003 đề xuất](../../docs/adr/0003-java-spring-and-deployment.md) |
| 14/10/2026 | PM độc lập đối chiếu 20 mục/5 AC với TS10–TS38 và IF-Q1–IF-Q5, xác nhận `BA_READY` cho Java/Redis; Tech gate còn chờ | [handoff](handoff.md), [story](story.md) |
