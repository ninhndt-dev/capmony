# Code review Java — INFRA-01.1 / #47

Ngày 15/10/2026. Snapshot code cuối `310ad7f`; fixed point `b1aa9f4`. Diff `git diff b1aa9f4...310ad7f` gồm triển khai Java ban đầu `13f2935`, hoàn thiện `7dfe7b3`, sửa timeout `310ad7f`. Tài liệu/gate không tự biến thành kết quả chạy. Thay đổi AGENTS.md có sẵn của Ninh không nằm trong commit của lượt này.

## Standards

Reviewer độc lập `/root/review_standards` đọc AGENTS.md/README/workflow và toàn bộ Java main/config, web placeholder, Docker/Compose/scripts, wrapper và tests. **0 blocker.** Bản hiện hành giữ tài nguyên; container/file thử không bị tự cleanup, Testcontainers bắt buộc reuse và Ryuk disabled, stop/probe-stop và no-recreate. Không thấy baseline code smell cần chặn.

## Spec

Reviewer độc lập `/root/review_spec` đối chiếu spec 5 AC, technical-design V1–V8 và thay đổi phương pháp kiểm theo quyền bảo toàn tài nguyên của Ninh. Các finding WIP đã sửa: probe trùng subnet, stat GNU fallback in output dư, README thiếu helper dừng probe, wrapper cleanup. Reviewer xác nhận container kế tiếp dùng volume cũ khi container trước đã dừng, volume mới sạch/marker cũ không đổi và Caddy mới cùng secret/subnet giữ đúng mục tiêu kiểm thử.

Full review phát hiện **1 P1**: Hikari connection/validation timeout không giới hạn chờ đọc truy vấn PostgreSQL đang mở. Root tái hiện red với PostgreSQL pause và HTTP timeout5s trước sửa. `310ad7f` đặt PgJDBC connect1s/socket2s/cancel1s, JdbcTemplate query1s. Regression `infra/scripts/postgres-timeout.test.py` yêu cầu503 dưới5s cho cả hai endpoint, PID/StartedAt không đổi và tự phục hồi sau unpause; luôn unpause trong finally.

QA độc lập `/root/java_qa` đã chạy green trên final image: health503 trong2.012s, readiness503 trong1.007s, cùng PID50128/StartedAt2026-10-14T16:59:22.492555631Z. Reviewer `/root/review_spec` đã đọc JSON và đóng P1; không còn P0/P1 của Spec review. Không dùng review này thay QA_PASS của8 case.

## Bổ sung truy vết TS36 — gate chờ quyết định

Rà dependency cuối phát hiện JUnit thực chạy6.0.3 khác TS36 chốtJUnit5. Reviewer `/root/review_spec` xác nhận mismatch; kết luận không còn blocker Spec phía trên chỉ áp dụng lỗi hành vi/P1 timeout, **không chứng minh tuân thủ TS36**. Spring7 yêu cầuJUnit6; phương án giữ Boot4.1/JUnit6 đã chạy được nhưng cần Ninh xác nhận IF-Q6. PM giữ gate cuối #47; chưa đóng Story hoặc mở #43. Tám case QA đã chạy pass vẫn là kết quả kỹ thuật trên snapshot310ad7f.

## Kết luận sau IF-Q6 — 15/10/2026

Ninh chọn A qua giao diện: dùngJUnit6, giữ Java25/Boot4.1. TS36 và thiết kế đã cập nhật đúng nguồn; `/root/review_spec` xác nhận gap TS36 đóng, không cần test lại thay đổi thuần tài liệu vì snapshot310ad7f vốn đã dùng6.0.3. Standards0 blocker; SpecP1 timeout đã đóng và gap TS36 đã giải. PM có đủ review cùng evidenceQA8/8 để chốt DEV_REVIEWED/QA_PASS hạ tầng; không cấp gate CM-01.
