# CM-01.1: Gửi liên kết thiết lập đầu tiên

**Parent:** [CM-01 / Planio #8](https://tanninh.planio.com/issues/8)
**Planio child:** [#43](https://tanninh.planio.com/issues/43), tạo và đọc lại ngày 14/10/2026.
**Status:** readiness tạm dừng sau TS10–TS38 đổi API sang Java Spring/JWT/Redis; PM phải xét lại `READY_FOR_IMPLEMENTATION` sau BA/Tech/PM review và ADR stack mới. Gate Next.js/Node trước đó chỉ là lịch sử. Hợp đồng `technical-design.md` bên dưới chưa cập nhật cho Java. Trạng thái công việc hiện hành xem trên Planio.
**Blocked by:** `HARD` – [INFRA-01.1 / Planio #47](../../capmoney-infrastructure/issues/01-docker-dev-test.md) hoàn tất môi trường Java Spring/Redis dev/test và được QA xác nhận; Planio relation #8 là #47 `blocks` #43. Ngoài ra, BA/PM/Tech/PM phải cấp lại gate thiết kế CM-01 và PM xét readiness của chính #43 trước Dev.
**Integration blocked by:** None.
**Owner / review:** Codex `/root` trong vai CapmmDev thực hiện; reviewer code khác người code và CapmmPM rà trước QA. BA review `/root/cm01_pm_ba`, Tech review `/root/cm01_tech_review` đã rà nội dung.

## What it delivers

Người triển khai cấu hình email quản lý và chạy thao tác phát hành đầu tiên; hộp thư thử nhận liên kết đúng email, Ninh mở được trang thiết lập từ link đó. **CM-01.1 phải giao trang entry thật**: đọc token từ fragment, xóa fragment khỏi URL, giữ token trong bộ nhớ của form và hiển thị ô mật khẩu/xác nhận. Trang chưa khẳng định link hợp lệ và chưa gửi form; xác minh token, thao tác Lưu và tạo quản lý thuộc CM-01.2. Không có đường web cho người ngoài chọn email hay tự tạo quản lý. Nếu gửi thất bại hoặc chưa rõ, thao tác nói đúng trạng thái và cho retry an toàn, không để hai liên kết hợp lệ.

## Acceptance criteria tham chiếu

- [ ] `AC1` trong [spec CM-01](../spec.md).
- [ ] `AC6` trong [spec CM-01](../spec.md).

## Hợp đồng và dữ liệu thử

- Theo [technical-design](../technical-design.md) mục 1–3: singleton bootstrap, email cấu hình cố định, generation, CAS phản hồi SMTP, trạng thái `sending|issued|unknown|failed`, token 32 byte chỉ lưu hash và hạn 72 giờ từ lúc SMTP chấp nhận. Link dùng fragment; trang thiết lập không tải tài nguyên ngoài. Nếu mở trang không có fragment hoặc tải lại sau khi đã xóa fragment, token không còn trong bộ nhớ: hướng dẫn Ninh mở lại liên kết trong email, không khẳng định link hợp lệ.
- Dữ liệu thử: PostgreSQL sạch riêng CM-01, `manager@example.test`, Mailpit, một token/đợt gửi. Không dùng email thật hoặc tài khoản thật; secret chỉ ở môi trường server. Cần chứng minh hai lệnh phát hành đồng thời, SMTP từ chối, timeout, phản hồi lệch generation, env email khác DB.
- Demo: chạy phát hành, nhìn đúng một thư trong Mailpit, mở link trên web mobile và quan sát form entry không tuyên bố link hợp lệ; thử lỗi gửi cho thông báo an toàn. Việc bấm Lưu sẽ được giao trong CM-01.2; không coi SMTP accepted là thư đã tới hộp.

## Rủi ro và kiểm chứng

- SMTP và DB không chung transaction; retry phải vô hiệu hóa generation cũ. Log không chứa token, mật khẩu hay nội dung thư.
- Review theo [test-spec](../test-spec.md) T01–T04 và các trường hợp liên quan T13/T15. Cần lưu commit/build, lệnh test và kết quả thật; chưa có kết quả QA.
