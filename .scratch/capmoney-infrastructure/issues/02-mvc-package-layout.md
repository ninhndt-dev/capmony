# INFRA-01.2: Sắp xếp BE một Maven project theo tính năng và MVC

**Parent:** Planio#46 INFRA-01. **Planio child:** [#48](https://tanninh.planio.com/issues/48), Task3, Closed8/100% sauQA, đã tạo/đọc lại đúng parent46/project4 ngày15/10/2026.
**Status:** done — QA_PASS; READY_FOR_IMPLEMENTATION đạt15/10/2026 sau BA/Tech ticket reviews và PM đối chiếu; publish/readback child Planio trước Dev.
**Owner:** CapmmDev `/root`; reviewer khác người code và QA độc lập sau code review.
**Blocked by:** Không còn HARD từ setup: Planio đã kết nối project4/user1, #47 Closed được đọc lại; sync-result.json ghi bằng chứng. Chỉ chờ readiness của ticket này.
**Integration blocked by:** None; #43 không phải dependency của refactor.

## Giao được gì

Code hạ tầng hiện có được tổ chức theo một Maven project, package config/security/health và lớp controller/service/model. Không làm tài khoản/CM-01/tiền, không tạo schema hay module rỗng, không đổi HTTP contract/dependency/timeout đã chốt.

## AC tham chiếu — đúng3 mã

- AC-MVC01-1 — layout và phân trách nhiệm; xem spec.md addendum.
- AC-MVC01-2 — HTTP/trust/Origin/probe/lỗi/phục hồi giữ nguyên.
- AC-MVC01-3 — build/test/running evidence mới; giữ file/lịch sử/tài nguyên và dữ liệu cũ.

## Hợp đồng và implementation

Tuân [mvc-refactor-design.md](../mvc-refactor-design.md). Interface health()/authReadiness() ở HealthService trả enum HealthStatus; controller chỉ đổi enum sang HTTP200/503 và JSON status/no-store. Giữ root ApiApplication để scan config/security/health. IngressFilter logic không đổi. Probe test-only ở test/security, Docker đóng đúng BOOT-INF/classes path mới; không có ở runtime.

Di chuyển file bằng rename, giữ Git history. Do thư mục target cũ có thể chứa class đã di chuyển, **không clean/xóa target**: host Maven test dùng bản sao source/pom/wrapper mới trong .local/build/<run-id> hoặc output mới được review; Docker build có workdir mới. So hash source bản sao với commit trước khi chạy. Không dùng kết quả từ class cũ để qua test.

## Test data và kiểm

Secret ngẫu nhiên giả file0600/dir0700, marker test không nhạy cảm, manager@example.test. Tạo Compose project mới với port/subnet/volume/secret riêng, giữ stack cũ; up no-recreate, stop, không down/rm/reset.

- Maven/JUnit6 unit + Spring Boot/Testcontainers thật, reuse và Ryuk disabled; config/trust semantics không đổi.
- Build runtime/probe; so class/config main; runtime không test/probe, probe thực chạy được.
- HTTP200/503/JSON/no-store/OpenAPI/deep link; PG stop/pause + Redis stop và cùngPID/StartedAt phục hồi.
- Matrix ingress/Origin/peer/secret/headers cùng marker; network/secret/persistence/Mailpit/stopped-start hồi quy để bảo vệ AC cũ.
- Lệnh/exit code, commit/image/container/volume ID, HTTP/body/timing và kết quả từng AC ghi trong evidence mới. QA_PASS310ad7f không chuyển sang code mới.

## Gate và rủi ro

BA scope independent `/root/java_qa`: khôngP0/P1; Tech design independent `/root/review_spec`: khôngP0/P1; PM root đã đối chiếu và chốt BA_READY/TECH_READY extension trong mvc-refactor-design.md. Ticket này còn cần BA/Tech rà dữ liệu/dependency/3AC, PM cấp READY_FOR_IMPLEMENTATION. Sau code, code-review hai trục và PM trước QA độc lập.

Rủi ro scan/constructor access, class stale, package probe hoặc JSON/status mapping sai: context test + ZIP + HTTP và outage matrix phải bắt được; có lỗi thì sửa và retest, không bỏ qua. Đóng follow-up mới chỉ sau QA trên build mới; #43 vẫn cần readiness riêng.

## Review ticket và PM readiness

Reviewer BA/PM độc lập `/root/java_qa` và Tech `/root/review_spec` đều không thấy P0/P1, xác nhận3AC/data/contracts/dependency/owner và fresh build seam. PM `/root` đối chiếu kết nối/sync47đãClosed và hai review, cấp READY_FOR_IMPLEMENTATION15/10/2026 cho đúng follow-up này. Planio child phải được tạo/đọc lại trước giao source. Không cấp gate#43 hoặc QA mới.
