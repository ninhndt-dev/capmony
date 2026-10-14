# Test Specification: INFRA-01 – Docker dev/test

**QA gate:** đã chạy một phần ngày 14/10/2026 trên Node/Hono commit `493f3d4`, **không QA_PASS**. IF-T03 fail; sau đó Ninh đổi BE API sang Java Spring nên dừng kiểm thử stack cũ. Xem [bằng chứng QA](qa-evidence.md) để phân biệt image ban đầu với image chứa patch chưa commit do workspace dùng chung. V1–V6 chưa đạt toàn bộ.

| Mã | AC/BR | Chiều | Thao tác/kết quả cần chứng minh | Thực tế/bằng chứng |
| --- | --- | --- | --- | --- |
| IF-T01 | AC-IF01-1 | AC, integration | Compose pin version dựng stack; secret test ngoài Git/image/log/`compose config`; sai/thiếu config báo lỗi | **partial**, xem bằng chứng QA |
| IF-T02 | AC-IF01-2, BR-IF01-03 | AC, route | GET `/`, reload `/manager/setup`, GET `/api/health` qua cùng Caddy origin; prefix `/api` giữ nguyên | **pass trên build Node/Hono cũ** |
| IF-T03 | AC-IF01-2 | error/recovery | Tắt DB; API health không báo healthy, bật lại phục hồi | **fail trên build Node/Hono cũ**: API thoát, HTTP 502 cả sau DB hồi phục |
| IF-T04 | AC-IF01-3, BR-IF01-01 | security, permission | Inspect networks/ports; chỉ Caddy loopback, web không kết nối DB, API/DB/Mailpit không host public | **pass trên topology Node/Hono cũ** |
| IF-T05 | AC-IF01-3 | security | Probe POST test-only: Origin thiếu/sai bị từ chối; IP header giả gửi qua Caddy bị ghi đè và request với Origin hợp lệ vẫn theo chính sách; peer không phải Caddy hoặc header IP sai/nhiều IP phía API bị từ chối. Probe không có trong image sản phẩm | **not-run** do đổi stack |
| IF-T06 | AC-IF01-4, BR-IF01-04 | state, recovery | Ghi marker DB, restart/recreate, đọc lại; reset test không ảnh hưởng dev | **partial**: marker còn sau recreate; reset chưa chạy |
| IF-T07 | AC-IF01-5 | integration, error | Gửi thư test nội bộ đến Mailpit, kiểm hộp; SMTP lỗi/timeout không báo gửi thành công | **partial**: gửi/nhận và lỗi khi Mailpit down đã chạy; timeout riêng chưa chạy |
| IF-T08 | AC-IF01-1–5 | regression | Dừng/chạy lại stack, kiểm route/health/DB/Mailpit; không có production secret/URL | **not-run** do đổi stack |

Các chiều AC, BR, state, boundary (Origin/IP/port), error/recovery, security/permission, integration và regression đều có case. Nếu kiểm trên máy ngoài VPS, TLS hoặc iPhone/Android là phạm vi sau, ghi `not-run` và lý do; INFRA-01 không được tự coi production/CM-01 đã QA.
