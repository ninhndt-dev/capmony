# CM-01.1 / #43: Gửi liên kết thiết lập đầu tiên

**Parent:** [CM-01 / Planio #8](https://tanninh.planio.com/issues/8).
**Planio child:** [#43](https://tanninh.planio.com/issues/43).
**Status:** `blocked-by-hard`, **chưa `READY_FOR_IMPLEMENTATION`**; bản Node/Next trước TS10–TS38 chỉ là lịch sử. BA/Tech còn phải rà ticket Java này và PM xét gate riêng.
**Blocked by:** `HARD` [INFRA-01.1 / #47](../../capmoney-infrastructure/issues/01-docker-dev-test.md) hoàn tất/`QA_PASS` môi trường Java/Redis dev/test (Planio relation #8).
**Integration blocked by:** None sau #47.
**Owner / review:** CapmmDev sau readiness; BA rà AC, reviewer code độc lập và PM rà phạm vi, QA chạy build.

## What it delivers

Người triển khai cấu hình trước email quản lý và chạy CLI nội bộ phát hành liên kết thiết lập đầu tiên; Mailpit nhận thư đúng email thử. Ninh mở link vào trang entry React trên điện thoại; trang đọc token từ URL fragment vào memory, xóa fragment, hiện form mật khẩu/xác nhận nhưng chưa hoàn tất thiết lập. Gửi bị từ chối hoặc chưa rõ phải báo đúng trạng thái và thử lại an toàn. Không có HTTP bootstrap, public signup hoặc form chọn email đích.

## Acceptance criteria tham chiếu

- [ ] `AC1` — liên kết đầu tiên chỉ tới email quản lý cấu hình sẵn, không có tự đăng ký.
- [ ] `AC6` — SMTP từ chối/chưa rõ và retry an toàn, không hai link cùng hiệu lực.

Hai AC lấy từ [spec CM-01](../spec.md), không thêm AC mới. Trang entry là phần dọc cần để demo [thiết kế Java §4](../technical-design-java.md); xác minh/tiêu thụ link và Lưu form thuộc #44.

## Hợp đồng và dữ liệu thử

- Java 25/Spring Boot 4.1, Spring Mail/JavaMailSender + SMTP Mailpit, Spring Data JPA/Liquibase/PostgreSQL. Migration singleton `manager_bootstrap(id=1)` seed trạng thái `empty`; token 32 byte CSPRNG, chỉ digest trong DB, generation/CAS. CLI `--capmoney.bootstrap-manager` cần `BOOTSTRAP_ENABLED=true`, không tạo endpoint HTTP. Email env khác DB hoặc đã có manager phải fail closed. Không in token/link/email thật ra stdout/log.
- Mốc `acceptedAt` chụp **ngay khi JavaMailSender trả thành công, trước CAS**, giữ nguyên tới DB; `expires_at=acceptedAt+72h`. Kiểm lệch app–DB/round-trip ≤5 giây; không chắc mốc, SMTP timeout hoặc DB lỗi thì `unknown`/`failed`, không báo link hiệu lực. CAS generation cũ không thể phát hành lại; retry vô hiệu hóa generation cũ trước khi gửi mới. Một link `issued` còn hạn không tự cấp link khác; xin lại link thuộc #02.
- Link dùng fragment `#token=`; React xóa URL fragment trước khi hiển thị, không tải tài nguyên bên thứ ba, không gửi token qua GET/log/referrer. Reload sau khi đã xóa fragment hướng mở lại email. Trang chưa xác nhận link hợp lệ hoặc submit POST của #44.
- Dữ liệu: PostgreSQL CM-01 thử sạch, `manager@example.test`, Mailpit, hai CLI process đồng thời, SMTP accepted/refused/timeout/DB CAS mất, email env lệch DB, clock skew quá ngưỡng; không dùng email/tài khoản thật.

## Bằng chứng và rủi ro

Dev ghi commit/image, JUnit/Testcontainers + hai process, Mailpit inbox, DB generation/state/acceptedAt/expiresAt, ảnh mobile entry, HTTP/cache/log đã che secret; QA theo [test spec Java](../test-spec.md) nhóm #43. SMTP accepted **không chứng minh thư tới inbox**; cần kiểm Mailpit riêng. Nếu accepted→CAS trễ/DB mất, không kéo dài hạn từ lúc CAS và không tự báo thành công. Chỉ `DEV_REVIEWED` sau code review khác người code; QA chạy độc lập. #43 hoàn tất mới giải cạnh HARD cho #44; không tự mở gate #44.
