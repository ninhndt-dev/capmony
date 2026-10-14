# Feature Spec: INFRA-01 – Docker dev/test trước FE/BE

**Story:** [story](story.md) · **Nguồn:** [TS01–TS09](../../docs/requirements/tech-stack-round-1.md), [ADR 0002](../../docs/adr/0002-project-infrastructure-and-stack.md), [sơ đồ](architecture.md).
**BA gate:** `BA_READY` ngày 14/10/2026; PM độc lập `/root/cm01_pm_ba` rà đủ 20 mục, TS01–TS09 và AC-IF01-1–5 sau khi sửa Origin. **Tech gate:** `TECH_READY` cùng ngày ở mức thiết kế sau reviewer Tech `/root/infra_tech_review` và PM rà lại V4/NFR; xem [handoff](handoff.md). Docker Compose #47 đã được Dev dựng và self-check; review/QA độc lập chưa chốt.

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
2. Caddy local trên loopback nhận `/`, deep link và `/api/health`; chuyển tới web/API theo ADR 0002.
3. API kiểm kết nối DB, Mailpit nhận một thư thử từ đường CLI/kiểm thử nội bộ; DB test giữ một marker qua restart/recreate container.
4. Người triển khai xem log/health và lưu bằng chứng commit/image, không đánh dấu chức năng sản phẩm đã chạy.

## 5. Alternative & Exceptional Flows

- Thiếu secret/DB chưa sẵn sàng/SMTP từ chối: khởi động hoặc health cho lỗi vận hành có thể quan sát; không dùng giá trị production, không im lặng báo thành công.
- Client gửi header IP giả, Origin thiếu/sai hoặc truy cập cổng API/DB trực tiếp: ranh giới mạng/chính sách phải từ chối theo test spec; vì chưa có auth POST sản phẩm, điểm kiểm security dùng probe POST dành riêng cho test và không phát hành thành endpoint sản phẩm.
- Khởi động lại web/API/DB: volume dữ liệu test vẫn giữ marker; reset dữ liệu thử là thao tác riêng và không lẫn production.

## 6. Field-level Specifications & Validations

Không có biểu mẫu nghiệp vụ. `APP_ORIGIN` local phải là URL loopback Caddy; DB name/user/password thử tách test; SMTP local trỏ Mailpit; cấu hình sai/thiếu không được rơi về secret production. Không ghi secret vào log/`docker compose config` hoặc image.

## 7. System Parameters & Constants (`PARAM.*`)

- `PARAM.IF01.PUBLIC_DEV_BIND = 127.0.0.1` (port cụ thể do Tech chọn, ghi trong README).
- `PARAM.IF01.API_PREFIX = /api` giữ nguyên qua Caddy tới Hono.
- `PARAM.IF01.PROD_PUBLIC_PORTS = 80,443` chỉ là hợp đồng production để kiểm khi có VPS, không phải test đã chạy.
- Image/service versions phải pin cụ thể trong lockfile/Compose; không dùng `latest` cho môi trường có bằng chứng QA.

## 8. Business Rules (`BR-*`)

- `BR-IF01-01`: chỉ Caddy được publish cổng local; API, DB và Mailpit không publish tới Internet. **Ví dụ:** request HTTP tới `/api/health` qua Caddy có phản hồi, kết nối trực tiếp tới cổng DB trên host bị từ chối.
- `BR-IF01-02`: dev/test/prod có nguồn dữ liệu và secret riêng. **Ví dụ:** test reset DB không đổi dữ liệu dev; không có production URL trong container test.
- `BR-IF01-03`: một origin ở browser với `/api` giữ prefix và SPA fallback. **Ví dụ:** tải lại `/manager/setup` trả trang web thử, `/api/health` trả JSON từ API.
- `BR-IF01-04`: volume giữ dữ liệu qua restart/recreate, nhưng không được gọi là backup. **Ví dụ:** marker thử còn sau `up --force-recreate`; mất volume không có cam kết khôi phục.

## 9. State Machine & Transition Rules (`STATE.*`)

`not-started → starting → healthy | degraded → stopped`. **API health** chỉ phản ánh API và kết nối DB; route web qua Caddy và SMTP/Mailpit được kiểm riêng theo AC-IF01-2/5, không suy từ `/api/health` là toàn stack healthy. Dừng container không xóa volume; lệnh reset test là chuyển trạng thái có chủ đích.

## 10. Data Fallback & Offline Handling

Không có offline sản phẩm theo R11. PostgreSQL test là nguồn trạng thái duy nhất cho môi trường này; khi DB lỗi, API health báo không sẵn sàng, không tự ghi tạm vào bộ nhớ. Volume không có tính năng backup theo S04/R52.

## 11. Error Messages & UI Registry (`ERR.*`)

Không áp dụng copy UI sản phẩm. Log/health dùng lỗi vận hành ngắn gọn như `db_unavailable`, `smtp_unavailable` nhưng không chứa secret/stack trace cho browser. Lệnh setup phải chỉ dẫn file cấu hình còn thiếu mà không in giá trị.

## 12. Security & Privacy Constraints

Mạng `web_net`, `api_net`, `data_net`, `mail_net` tách như ADR 0002. Caddy nhận request trực tiếp, ghi đè IP header; API chỉ tin socket peer Caddy và một IP hợp lệ, fail-closed nếu sai. Caddy loopback ở dev/test; production TLS qua Caddy sau khi có VPS/domain. Secret file ngoài Git; không dùng mật khẩu thử chung với production. Không có endpoint khởi tạo manager trên HTTP.

## 13. Third-party Integrations

Docker Engine/Compose local và image pin version; Mailpit local cho SMTP thử. Không cần Vercel, Neon, VPS/domain hoặc SMTP nhà cung cấp trong ticket này. Nếu pull image, ghi version/hash và nguồn; không tạo tài khoản trả phí.

## 14. Audit Log & History Requirements

Không áp dụng audit người dùng/giao dịch vì chưa có nghiệp vụ. Cần lưu commit, image/Compose version, lệnh và log test đã che secret trong ticket/handoff. Không log token/mật khẩu thật.

## 15. NFR (Performance, Concurrency Bounds)

Môi trường cho phép chạy hai API instance test dùng chung PostgreSQL ở ticket CM-01 sau. INFRA-01 chỉ chứng minh một API instance và DB/network contract; phép kiểm **hai instance đồng thời** thuộc test CM-01 theo [test-spec CM-01](../capmoney-manager-setup/test-spec.md) T08/T14, không suy đã đạt từ INFRA-01. Health có timeout hữu hạn; startup không dựa duy nhất vào thứ tự Compose. Không có SLA production được Ninh chốt.

## 16. Acceptance Criteria (`AC-IF01-*`)

- `AC-IF01-1`: trên máy dev, Compose pin version dựng được Caddy, web placeholder, API health, PostgreSQL và Mailpit từ dữ liệu/secret thử; `compose config`/image/Git không lộ giá trị secret.
- `AC-IF01-2`: qua **cùng origin loopback**, `/` và tải lại `/manager/setup` trả web thử, `/api/health` trả JSON API với prefix `/api` giữ nguyên; lỗi DB làm health không báo healthy.
- `AC-IF01-3`: chỉ Caddy được bind host loopback; API/DB/Mailpit không có port host public; các mạng cô lập đúng ADR. Probe POST **chỉ trong test, không thành endpoint sản phẩm** chứng minh API từ chối IP header giả, peer không phải Caddy và `Origin` thiếu/sai.
- `AC-IF01-4`: PostgreSQL test giữ marker qua restart/recreate container; test reset riêng không dùng dữ liệu dev/prod. Không tuyên bố đây là backup.
- `AC-IF01-5`: Mailpit nhận thư thử qua đường nội bộ; khi SMTP lỗi/timeout, phép thử báo lỗi rõ, không nói đã gửi. Không tạo endpoint sản phẩm để gửi mail thử.

## 17. Figma Mapping / Visual References

Không áp dụng: đây là môi trường hạ tầng, không phải màn sản phẩm. Ảnh CapMoney cũ chỉ tham khảo UI về sau.

## 18. Dependencies (Cross-feature / Cross-story)

ADR 0002 accepted và câu trả lời TS01–TS09 là đầu vào. INFRA-01 là enabler cho CM-01.1; không tự cấp lại `TECH_READY` CM-01 vì thiết kế auth/email phải chuyển sang API riêng và review lại. Sau INFRA-01, ticket sản phẩm vẫn qua gate độc lập.

## 19. Q&A Log & Resolved Decisions

Xem [Q&A](qna.md) IF-Q1–IF-Q5. **Ninh đã xác nhận:** hai service, Docker dev/test, VPS/PostgreSQL production, kiến trúc trước Docker và tiếp tục bundle ADR 0002. **Tech/BA đề xuất được duyệt ở mức kiến trúc:** Caddy/Hono/Vite/PG17, các mạng và cùng origin. **Chưa rõ cho vận hành:** VPS/domain/SMTP thật và chi phí; không chặn ticket dev/test.

## 20. Change History & Document Traceability

| Ngày | Thay đổi | Nguồn |
| --- | --- | --- |
| 14/10/2026 | Tạo spec 20 mục INFRA-01 từ TS01–TS09/ADR 0002; AC-IF01-1–5, chưa đánh dấu BA_READY | [Q&A](qna.md), [ADR 0002](../../docs/adr/0002-project-infrastructure-and-stack.md) |
| 14/10/2026 | Theo PM review: AC-IF01-3 nêu rõ probe POST Origin thiếu/sai; health API chỉ phản ánh API/DB | [handoff](handoff.md) |
| 14/10/2026 | Theo PM Tech review: hai API instance thuộc kiểm CM-01; giữ một instance trong INFRA-01, bổ sung V4 Origin trong technical-design | [technical-design](technical-design.md) |
| 14/10/2026 | PM độc lập xác nhận `BA_READY` và `TECH_READY` ở mức thiết kế sau reviewer Tech; cho phép tạo ticket Compose, chưa chứng minh V1–V6 | [handoff](handoff.md) |
