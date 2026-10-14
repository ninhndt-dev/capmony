# QA plan — INFRA-01.2 / Planio #48 MVC refactor

15/10/2026. **PREPARED / NOT RUN**. Chờ snapshot code bất biến, hai reviewer và PM DEV_REVIEWED; không dùng QA_PASS #47 cho #48. Ba AC bảo trì trong [ticket](issues/02-mvc-package-layout.md)/[addendum](spec.md) giữ nguyên.

QA độc lập `/root/java_qa`; source baseline trước refactor5c55101. Khi nhận SHA mới, ghi commit/image/container/volume/hash/lệnh/exit code trước kết luận. Namespace `capmoney-mvc-qa` và `capmoney-mvc-qa-probe`, loopback18082/28082, api subnets172.30.61.0/24 và172.30.62.0/24; secret/volume/data tách mới `.local/qa-mvc-final/`. Không xóa/reset/clean/down/rm; giữ snapshot,target,stack cũ,successor,Testcontainers; có thể stop stack cũ để tiết kiệm RAM.

## Matrix kiểm

| Case | AC mới / phạm vi hồi quy | Thực thi bắt buộc | Trạng thái |
| --- | --- | --- | --- |
| MVC-T01 / IF-J-T01 | AC-MVC01-1,2,3 | Layout một Maven/root scan/config/security/health controller-service-model;6mainclasses. Host Maven trên bản sao mới source/pom/wrapper `.local/build/<id>` hash khớp source snapshot, không dùng target cũ; JUnit6/Spring Test/Testcontainers thật reuse/Ryuk disabled. Build runtime/probe,6mainclassbytes+config giống nhau, chỉ probe có controller security test. Runtime không test class. Linux guards, secret/Git/config/log/image layer scan, OpenAPI pin smoke. | NOT RUN |
| MVC-T02 / IF-J-T02 | AC-MVC01-1,2,3 | `/`, deep link, health/readiness JSON/status200/no-store; runtime probe404; OpenAPI200; controller chỉ HTTP/service giữ dependency check bằng code review + unit interface. | NOT RUN |
| MVC-T03 / IF-J-T03 | AC-MVC01-2,3 | PG stop: health+ready503 liên tục không502, cùngPID/StartedAt, phục hồi tự UP; Redis stop:health200/ready503 rồi UP cùngJVM. PG pause dùng tracked timeout regression hữu hạn; initial PG unavailable kiểm riêng retained API restart policy rồi recovery. | NOT RUN |
| MVC-T04 / IF-J-T04 | AC-MVC01-2,3 | Topology inspect chỉ Caddy loopback publish; web/data/rate/mail isolation; host direct API/PG/Redis/Mailpit refused. | NOT RUN |
| MVC-T05 / IF-J-T05 | AC-MVC01-2,3 | Caddy adapt; direct peer/secret/header missing/wrong/duplicate/comma/malformed/mismatch403 marker không đổi; Origin thiếu/sai403; forged headers qua Caddy được ghi đè; valid200; secret64hex/no newline, thay1byte403; Caddy successor đổi peer cùngsecret vẫn200. | NOT RUN |
| MVC-T06 / IF-J-T06 | AC-MVC01-3 | Marker PG restart+successor cùngvolume còn; fresh volume khôngmarker; dữ liệu môi trường cũ/dev không đổi; không production/backup claim. | NOT RUN |
| MVC-T07 / IF-J-T07 | AC-MVC01-2,3 | JavaMailSender test-only gửi Mailpit nội bộ nhận manager@example.test; refused/timeout hữu hạn không báo đã gửi; runtime không HTTP mail endpoint. SMTP runner3cases thực chạy, skip không coi pass. | NOT RUN |
| MVC-T08 / IF-J-T08 | AC-MVC01-2,3 | Stop/up no-recreate no-build cùngimage/project; lặp route/health/readiness/topology/marker/mail+PG/Redis failure; ghi startup transient/harness retry riêng nếu có; không sửa lịch sử #47. | NOT RUN |

## Harness và gate

Harness chuẩn bị tại `.local/mvc-qa-harness/` dựa trên #47, chỉnh namespace/path probe mới; chưa chạy. `fresh-maven.py <full-reviewed-SHA>` bắt buộc assert SHA và hash bản sao trước test. Bằng chứng mới sẽ lưu `evidence/mvc-final/` đã sanitize, report `mvc-refactor-qa-evidence.md`; không overwrite `evidence/java-final/` hoặc báo cáo #47. Mỗi case phải có kết quả thực, mọi case bắt buộc phải hoàn tất trước QA_PASS. Thiết bị/sản phẩm CM-01/production vẫn ngoài phạm vi.
