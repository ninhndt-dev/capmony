# CM-01.2: Hoàn tất thiết lập một quản lý

**Parent:** [CM-01 / Planio #8](https://tanninh.planio.com/issues/8)
**Planio child:** chờ tạo sau khi có API key; không tự đặt ID.
**Status:** draft, chờ đồng bộ Planio và review readiness.
**Blocked by:** `HARD` CM-01.1 – gửi liên kết thiết lập đầu tiên.
**Integration blocked by:** None sau khi CM-01.1 xong.
**Owner / review:** CapmmDev thực hiện; CapmmBA rà AC, reviewer code độc lập và CapmmPM rà trước QA.

## What it delivers

Ninh mở liên kết còn hạn, đặt và xác nhận mật khẩu trên điện thoại, rồi hoàn tất để có đúng một tài khoản quản lý. Liên kết sai, hết hạn, đã dùng hoặc hai lần xác nhận gần đồng thời không tạo thêm tài khoản.

## Acceptance criteria tham chiếu

- [ ] `AC2` trong [spec CM-01](../spec.md).
- [ ] `AC4` trong [spec CM-01](../spec.md), phần không tạo quản lý thứ hai/không tự cấp quyền khác.
- [ ] `AC5` trong [spec CM-01](../spec.md).

## Hợp đồng và dữ liệu thử

- Theo [technical-design](../technical-design.md) bước 4 của “Hợp đồng và luồng dữ liệu” cùng mục “Đồng thời, idempotency và lỗi”: POST đòi Origin đúng, giới hạn body/định dạng token, mật khẩu băm scrypt và giới hạn tài nguyên; transaction khóa singleton, kiểm lại token/trạng thái/hạn bằng đồng hồ DB, insert đúng một manager và tiêu thụ link cùng commit.
- Dữ liệu thử: link từ CM-01.1 với thời gian trước, đúng và sau 72 giờ; token sai/đã dùng/khác generation; hai browser cùng gửi. Dùng email quản lý thử, không dùng bí mật thật.
- Demo: mở link, đặt mật khẩu, thấy thông báo hoàn tất; gửi lại hoặc gửi song song chỉ một thành công; DB cuối đúng một manager, link cũ không dùng lại.

## Rủi ro và kiểm chứng

- Hai yêu cầu đồng thời từ nhiều tiến trình thật phải giữ bất biến một quản lý; lỗi trước commit phải rollback cả tài khoản và tiêu thụ link. Không log token/mật khẩu.
- Review theo [test-spec](../test-spec.md) T05–T09, T13 và T15. Cần lưu commit/build, lệnh test và kết quả thật; chưa có kết quả QA.
