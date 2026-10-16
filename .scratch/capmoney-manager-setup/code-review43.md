# Code review #43

Baseline `1f44012`; current #43 source prior immutable commit,15/10/2026. Review trong phạm vi ticketAC1/6/T01–06, không cấp QA hoặc readiness#44.

## Spec — backend

Reviewer `/root`, khác tác giả backend `/root/review_spec`. Kiểm CLI nonweb/disabledgate, configemail/origin, migration singletonseed, reserve/finalize rowlock vàgenerationCAS, SMTPngoàitransaction, acceptedAtchụpmộtlần, clockguard, immutable72h, stalegeneration không hồi sinh, emailkhác/alreadymanager failclosed. Secret/token không logstdout; digest-onlyDB. Yêu cầu phân loại SMTPexplicitrejection đã sửa dùng providerexceptions và nestedchain; timeoutunknown. Không thêm setup/loginendpoint hoặc entity44/45. KhôngP0/P1 còn mở từ review mã; bằng chứng runtime/fault đầy đủ vẫn doQA xác nhận.

## Standards / web Spec

Reviewer độc lập `/root/review_standards` đã rà backend/webStandards và webSpec: khôngP0/P1, webprivacy/accessibility đúng ranh giới#43. P2 không chặn: tên `refusedBeforeSend` và comment chỉ nói từ chối kết nối nhưng hàm còn nhậnSMTP4xx/5xx; hành vi đúng, có thể đổi tên khi chạm vùng này lần tới. Web tác giả `/root`; Spec của web do reviewer độc lập xác nhận.
