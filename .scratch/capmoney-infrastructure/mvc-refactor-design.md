# Thiết kế follow-up INFRA-01: sắp xếp BE theo MVC

Ngày15/10/2026. **TECH_READY cho phần mở rộng15/10/2026; ticket follow-up đã READY_FOR_IMPLEMENTATION sau review ticket và PM15/10/2026; cần publish/readback Planio trước Dev.** Chưa đổi Java source. Thực hiện sau khi kết nối và đồng bộ Planio theo thứ tự Ninh yêu cầu.

## Nguồn và phạm vi

Ninh trả lời “ok tiến hành setup planio và sau đó sửa cấu trúc thư mục nhé” sau [đề xuất một Maven project/package theo tính năng](../../docs/requirements/backend-structure-proposal.md). Đây là xác nhận triển khai đề xuất đó; không chia thêm nhiều Maven library. Giữ Spring MVC, Java25/Boot4.1/JUnit6 và các HTTP contract đã QA trên310ad7f. Chỉ đổi tổ chức code hạ tầng hiện có, không viết CM-01 hoặc schema/tính năng mới.

Đây là follow-up của Story INFRA-01 đã có spec20 mục. [Addendum BA](spec.md#bổ-sung-ba--bố-cục-mvc-ngày15102026) xác định yêu cầu mới và ba AC. #47 vẫn chứng minh snapshot310ad7f; không sửa kết quả lịch sử hoặc gán QA_PASS cũ cho refactor.

## Module và interface

`health` là Module: interface là hai trạng thái health/readiness với timeout/lỗi/phục hồi đã có; implementation gồm kiểm JDBC/Redis. Controller chuyển HTTP sang interface của Module; service trả trạng thái, không trả ResponseEntity. Không thêm interface Java chỉ để có một implementation chuyển tiếp.

```text
com.capmoney.api
├── ApiApplication                     # root scan giữ nguyên
├── config/SecurityConfiguration       # wiring Spring Security
├── security/IngressFilter             # giữ nguyên logic trust/Origin
└── health
    ├── controller/HealthController    # route, status, JSON, cache header
    ├── service/HealthService          # query/ping, lỗi, trạng thái dependency
    └── model/HealthStatus             # UP / DB_UNAVAILABLE / REDIS_UNAVAILABLE
```

- `HealthService` nhận JdbcTemplate và RedisConnectionFactory bằng constructor. `health()`/`authReadiness()` trả HealthStatus; code chuỗi tương ứng `UP`, `db_unavailable`, `redis_unavailable` giữ nguyên. Các timeout trong application.yaml không đổi.
- `HealthController` nhận HealthService, giữ `/api/health`, `/api/ready/auth`, status200/503, `Cache-Control: no-store`, JSON `{status: code}`. Map hiện tại đủ cho response một trường; không tạo DTO chỉ chuyển tiếp hoặc entity/repository rỗng cho health.
- SecurityConfiguration sang `config`, import IngressFilter từ `security`; filter sang `security` với logic nguyên trạng. ApiApplication root vẫn `com.capmoney.api`, không thêm ComponentScan bao trùm package khác.
- ProbeController test-only sang test package `com.capmoney.api.security`, giữ route/marker; Dockerfile chỉ cập nhật vị trí class vào `BOOT-INF/classes/com/capmoney/api/security`. Runtime JAR tuyệt đối không chứa test class. Probe dùng cùng main classes/config.
- Unit outage tests sang `health/service/HealthServiceTest`, kiểm interface trạng thái. Test HTTP/cache giữ qua Spring integration và request runtime. ContainerDependenciesTest có thể ở root test để tìm ApiApplication, đổi import controller; Mailpit runner vẫn test-only, tên đơn giản không đổi để tools command tiếp tục chạy.
- Package model này chỉ giữ enum trạng thái health, không phải schema DB. Repository/dto của module sản phẩm chỉ có khi ticket đã đủ gate. React vẫn là giao diện.

## Di chuyển file và bằng chứng bảo toàn

Di chuyển bằng rename, giữ nội dung/lịch sử trong Git; không xóa file hoặc thư mục tài nguyên, không clean/reset. Những thay đổi nội dung chỉ là package/import, phân trách nhiệm health và đóng gói test probe. Giữ snapshot, container và volume cũ. Môi trường thử mới có project/port/subnet/secret riêng; up no-recreate và stop, không down/rm.

## Điểm kiểm chứng

1. Maven/JUnit6 unit và Spring Boot Test/Testcontainers thật với reuse/Ryuk disabled; không tự xóa container. Build runtime/probe trên commit mới, main class bytes/config giống nhau, runtime không test/probe.
2. HTTP giữ đủ contract200/503/no-store/OpenAPI/deep link; DB stop/pause và Redis stop trả503/phục hồi tự động cùng JVM. Dùng test timeout đã có với project mới.
3. Matrix trust/Origin/header/secret/peer của IF-J-T05 chạy lại vì package/filter/đóng gói đổi. Marker không đổi khi bị từ chối; valid request200.
4. Smoke mạng/secret/persistence/Mailpit và stopped-start regression trên image mới; không gọi snapshot mới QA_PASS nếu case bắt buộc chưa chạy. Spec QA mới dẫn AC-MVC01-1–3 và những case IF-J liên quan, không thay report lịch sử310ad7f.

## Rủi ro và xử lý

Mất component scan hoặc constructor access → Spring context test phải fail; probe đóng gói sai → runtime/probe ZIP và request thật bắt được; filter order đổi → ingress matrix bắt được; enum/status ánh xạ sai → HTTP JSON giữ nguyên được kiểm thực tế. Không đổi dependency hay timeout trong refactor để giảm số biến cần kiểm.

## Gate và phụ thuộc

BA/PM kiểm phạm vi/addendum; reviewer Tech khác người thiết kế kiểm thiết kế này, sau đó PM ghi TECH_READY cho phần mở rộng và mới tạo child follow-up trên Planio dưới#46. Ticket tối đa3 mã AC-MVC01, có data, hợp đồng, dependency và người review; chỉ giao Dev khi READY_FOR_IMPLEMENTATION và bước setup/sync Planio hoàn tất. Độc lập#43, không tự cấp gate của CM-01.

## Review và gate phần mở rộng

- BA/PM scope reviewer độc lập `/root/java_qa`: khôngP0/P1, addendum đủ BA_READY, không có CM-01/schema/tính năng mới.
- Tech reviewer độc lập `/root/review_spec`: khôngP0/P1 thiết kế, xác nhận package scan/probe/HTTP và kiểm hồi quy đủ rõ.
- PM `/root` đối chiếu nguồn Ninh,3 AC, phạm vi và review: BA_READY/TECH_READY cho extension đạt15/10/2026. Đây không phải readiness ticket hoặc QA mới.
- Bổ sung test seam bảo toàn: Maven target cũ có class stale sau di chuyển; dùng bản sao source/pom/wrapper vào build directory mới và so hash trước test, không clean/xóa target cũ. Docker workdir mới không copy host target.
- Setup-first đã giải: Planio project4/user1 xác minh, #47Closed và #46InProgress đọc lại trong `.scratch/capmoney-planio-setup/sync-result.json`.
