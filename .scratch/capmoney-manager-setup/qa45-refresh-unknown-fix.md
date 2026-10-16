# CM-01.3 / #45: chặn gửi lại refresh sau kết quả không rõ

**Ngày:** 16/10/2026. **Mã:** `6676d5e` trên `main`. Đây là sửa lỗi và tự kiểm của người triển khai, không phải code review độc lập hoặc `QA_PASS`.

## Lỗi và thay đổi

Trước khi sửa, nếu request `/api/auth/refresh` đã gửi nhưng phản hồi mất, giao diện báo kết quả chưa rõ. Tải lại `/manager` lại nhận `401` từ `/api/manager/me` rồi **tự gửi lại cùng refresh cookie cũ**. Nếu lần đầu đã rotate trên server, lần gửi lặp kích hoạt nhánh reuse và thu hồi family, trái hướng dẫn không tự retry sau kết quả chưa rõ trong [ticket #45](issues/03-manager-login-session.md) và [test spec T16](test-spec.md).

Web hiện ghi một dấu trạng thái `capmoney.manager.refresh-unknown` **không chứa token** khi refresh lỗi mạng hoặc nhận 5xx. Dấu được lưu ở local/session storage; nếu cả hai không dùng được, tự refresh bị tắt để tránh retry không kiểm soát. Khi `/manager/me` báo 401 sau đó, giao diện đưa người dùng tới đăng nhập mà không gửi lại refresh cũ. Đăng nhập thành công xóa dấu; refresh thành công bình thường cũng xóa dấu. Dấu này chỉ xử lý hành vi client, không thay quy tắc server thu hồi family khi refresh reuse.

## Bằng chứng tự kiểm

- Docker web build Node 24/TypeScript/Vite **PASS** trên source của `6676d5e`, image `sha256:46f7188549e413f12e0b80d3dd187fe7fdcb49712bb18e720a7cd71c9caa2d2f`. Cảnh báo `use client` từ TanStack Query không làm build fail.
- [Browser regression](evidence/qa45/refresh-unknown-regression.cjs) dùng API giả lập: refresh đầu bị mất phản hồi; tải lại trang không gửi refresh thứ hai; đăng nhập thành công xóa dấu; sau đó một refresh mới hợp lệ chạy được. Chromium 393×851 và WebKit 390×844 đều **PASS**. Script chỉ chứng minh logic web khi response chưa rõ; không chứng minh server đã commit hoặc T16 đầy đủ trên API thật.
- API không đổi trong commit này. Docker image API build `sha256:399d5c5ef6bf6afcfd16795d937631932fdea64cc12932e71e173b8b9d53475f`. Bộ Maven/Testcontainers đầy đủ: 14 test, 0 assertion failure, **1 error** khi một PostgreSQL container không kịp startup, 1 skipped; `AuthHttpIntegrationTest` và `SetupHttpIntegrationTest` pass. Chạy riêng `BootstrapIntegrationTest` sau khi các build khác kết thúc: **PASS**. Báo cáo Surefire của lượt đầu lưu cục bộ tại `.local/cm01-audit-a89ac4a-surefire/` (không đưa vào Git); hai lượt chạy không được gộp thành một full suite xanh.

## Cách lặp browser regression

Build web bằng Docker rồi chép `/work/apps/web/dist` từ image build vào một thư mục riêng. Chạy [static server](evidence/qa45/static-server.py) với thư mục dist và cổng `49300`. Cài `playwright-core@1.64.1` ở thư mục thử cục bộ; trong image `mcr.microsoft.com/playwright:v1.64.1-noble`, mount script và thư viện, đặt `NODE_PATH` tới thư viện rồi chạy `node refresh-unknown-regression.cjs` với `--network host`. Giữ container/ảnh thử để tra cứu; không dùng script này thay QA hai JVM/DB/Redis của #45.

## Gate còn chờ

Planio #44 đã được [đối chiếu](tracker-reconciliation-2026-10-16.md) về `In Progress/0%`: QA độc lập của #44 và PM chốt gate vẫn thiếu. Vì vậy #45 còn `HARD` #44 → #45; cần reviewer code khác người triển khai, QA độc lập T13–T20 trên build pin, rồi T21/thiết bị thật ở cấp Story. Bản tự kiểm này không cấp `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED` hay `QA_PASS`.
