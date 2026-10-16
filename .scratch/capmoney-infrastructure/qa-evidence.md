# QA evidence: INFRA-01.1 / Planio #47

**Ngày:** 14/10/2026. **Người chạy:** `/root/infra_qa`, độc lập với Dev. **Trạng thái:** dừng, **không QA_PASS**. Ninh đổi BE API sang Java Spring khi QA đang chạy; thiết kế Node/Hono và build dưới đây bị thay thế, không tiếp tục reset/rebuild/test. Đây là bằng chứng lịch sử cho commit `493f3d4`, không chứng nhận stack mới.

## Build và môi trường

- Repo HEAD lúc bắt đầu: `493f3d4`; `git status --short` sạch. Docker Compose `v2.35.1`; chỉ dùng project `capmoney-test` cho các thao tác QA. `capmoney-dev` có volume riêng và không bị xóa.
- `./infra/scripts/stack.sh test up` exit `0`; runtime API image ban đầu `sha256:ba5edba287f55e29ece25391e58eed132946acf1ce4c7808c6782c4b3a2b396c`, web `sha256:611df921bf586b7caf797aa72af7639cbdc2ccc69f23a0e7e53efd1d44ff9c47`. Caddy/Mailpit/PostgreSQL dùng tag trong Compose. API ban đầu nghe cổng 3000, Caddy host `127.0.0.1:28080`.
- Khi kiểm marker, `docker rm -f capmoney-test-postgres-1 && ./infra/scripts/stack.sh test up` exit `0`. Lệnh `up` có `--build` và đã lấy bản sửa `apps/api/src/db.ts` **chưa commit** do workspace dùng chung; API image đổi thành `sha256:8e9841aed74f1ea7e5e852437d07b4152600c9f25aede828f6a0c4d7acd2bc3c`. Từ điểm này, không quy kết kết quả API cho commit `493f3d4`. Không rebuild/reset sau yêu cầu dừng.
- Mailcheck image sẵn có `sha256:38e41c6b4f3976db8c989f6c525e9d2a299ab72aa6c5abd3ea76e0245d075e6d` (chưa có chứng cứ gắn image này với commit xác định).

## Kết quả từng case

| Case | Kết quả thực | Lệnh/bằng chứng, exit code |
| --- | --- | --- |
| IF-T01 / V1 | **Partial** | `./infra/scripts/stack.sh test up` exit 0; `.local/secrets/test/pg-password` mode 600 và Git ignore; kiểm boolean giá trị secret trong `stack.sh test config`, `docker image inspect`, `docker history`, `git ls-files` đều `False`. `./infra/scripts/stack.test.sh` exit 0 (4 guard cases). `APP_ORIGIN=http://evil.invalid ./infra/scripts/stack.sh test config` exit 2 trước Docker. Runtime image `docker run ... test ! -e /work/apps/api/dist/test/probe-server.js` exit 0. Chưa hoàn tất scan mọi container log/image layer; không gọi pass. Chuỗi `NODE_ENV=production` trong image là chế độ Node, không phải URL DB production. |
| IF-T02 / V2 | **Pass trên build ban đầu** | `curl -i` qua `127.0.0.1:28080` cho `/`, `/manager/setup`, `/api/health` đều exit 0/HTTP 200; deep link trả HTML placeholder, health trả `{"status":"healthy"}`. |
| IF-T03 / V2 error | **Fail trên build ban đầu** | `docker stop capmoney-test-postgres-1` exit 0; sau đó `curl -i /api/health` exit 0 nhưng HTTP **502**, API container đã thoát do lỗi `pg` Pool idle `error` chưa bắt (Node stack trace trong `docker logs capmoney-test-api-1`). `docker start capmoney-test-postgres-1` exit 0 nhưng health vẫn HTTP 502 vì API không tự hồi phục. Dev đã được báo P1; chưa retest bản sửa đã commit. |
| IF-T04 / V3 | **Pass trên topology ban đầu** | `docker inspect capmoney-test-{caddy,web,api,postgres,mailpit}-1`: Caddy ở `web_net,api_net` và bind duy nhất `127.0.0.1:28080`; web chỉ `web_net`; API `api_net,data_net,mail_net`; PG chỉ `data_net`; Mailpit chỉ `mail_net`; bốn service sau không có HostConfig PortBindings. Socket host 3000/5432/1025/8025 đều kết nối thất bại, 28080 thành công. `docker exec capmoney-test-web-1 getent hosts postgres` exit 2. |
| IF-T05 / V4 | **Not-run** | Chưa chạy probe Origin/IP và kiểm peer thật; dừng vì đổi stack. Kiểm runtime image không có `probe-server.js` ở IF-T01 chỉ là một phần yêu cầu. |
| IF-T06 / V5 | **Partial** | `docker exec ... psql` tạo/đọc marker `if_t06_493f3d4` exit 0. Xóa riêng container PG test rồi `stack.sh test up` exit 0; marker vẫn đọc được, dev health `curl -i http://127.0.0.1:18080/api/health` HTTP 200 trước/sau. `capmoney-dev_pg_data` và `capmoney-test_pg_data` là volume khác nhau. **Chưa chạy `test reset`**, nên chưa chứng minh reset không tác động dev. Lệnh up làm API image đổi sang bản chưa commit như ghi trên. |
| IF-T07 / V6 | **Partial** | Dùng mailcheck image sẵn có: `docker run --rm --network capmoney-test_mail_net -e SMTP_HOST=mailpit -e SMTP_PORT=1025 -e SMTP_TO=manager@example.test capmoney-test-mailcheck node apps/api/dist/test/send-test-mail.js` exit 0, in `smtp_test_received_in_mailpit`. `docker stop capmoney-test-mailpit-1`, chạy lại lệnh gửi exit 1, in `smtp_unavailable_or_result_unknown`; `docker start capmoney-test-mailpit-1` exit 0. Chưa kiểm timeout riêng và chưa gắn image mailcheck với commit. |
| IF-T08 | **Not-run** | Chưa chạy hồi quy đầy đủ sau restart; dừng vì đổi stack. |

## Kết luận

`IF-T03` fail, `IF-T01/06/07` partial, `IF-T05/08` not-run. Các kết quả trên không cấp `QA_PASS` hay cho phép đóng #47. Cần BA/Tech/PM cập nhật ADR/spec/ticket cho Java Spring, Dev dựng build có commit xác định và QA chạy lại bộ case phù hợp sau gate mới. Không có VPS/TLS/SMTP thật hoặc kiểm thiết bị trong phạm vi lần chạy này.
