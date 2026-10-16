# CM-01.2 / #44: Hoàn tất thiết lập một quản lý

**Parent:** [CM-01 / Planio #8](https://tanninh.planio.com/issues/8).
**Planio child:** [#44](https://tanninh.planio.com/issues/44).
**Status:** `ready-for-agent` — READY_FOR_IMPLEMENTATION ngày15/10/2026 sau QA43 và BA/Tech reviews.
**Blocked by:** Đã giải `HARD` [CM-01.1 / #43](01-send-first-setup-link.md) hoàn tất với QA xác nhận các case của **ticket #43**; `QA_PASS` cấp Story CM-01 chỉ xét sau cả #43–#45. #43 đang QA; hạ tầng #47/#48 đãClosed.
**Integration blocked by:** None sau #43.
**Owner / review:** CapmmDev sau readiness; BA rà AC, reviewer code độc lập, PM rà phạm vi và QA độc lập chạy build.

## What it delivers

Ninh mở liên kết còn hạn, nhập và xác nhận mật khẩu trên điện thoại, rồi hoàn tất đúng một tài khoản quản lý. Link sai/hết hạn/đã dùng/khác thế hệ hoặc hai submit gần đồng thời không tạo thêm quản lý. Khi commit xong nhưng response mất, UI báo kết quả chưa rõ, không khẳng định thất bại hoặc tự tạo tài khoản thứ hai.

## Acceptance criteria tham chiếu

- [ ] `AC2` — link đúng mục đích, một lần, mốc 72 giờ; link sai/hết hạn không tạo quản lý.
- [ ] `AC4` — sau khi có quản lý không thiết lập thêm hay tự cấp quyền khác.
- [ ] `AC5` — xác nhận lặp/hai request đồng thời tối đa một thành công.

Ba AC lấy từ [spec CM-01](../spec.md). Đăng nhập và phiên thuộc #45.

## Hợp đồng và dữ liệu thử

- Dùng [thiết kế Java §3, §5, §7](../technical-design-java.md): `POST /api/manager/setup` JSON `{token,password,passwordConfirmation}`, body ≤4 KiB, token base64url 43 ký tự, `Origin==APP_ORIGIN`, CSRF từ `/api/auth/csrf`, Caddy edge trust. Mật khẩu Tech đề xuất 12–72 byte UTF-8, không NUL; BCrypt cost 12, không prehash/truncate âm thầm. Benchmark cost và giới hạn đồng thời hash trước khi QA; không log token/mật khẩu. Redis throttle setup 10 lần/15 phút/IP, lỗi Redis trả 503 trước hash/ghi DB.
- Sau validate/throttle/hash, transaction JPA khóa singleton bootstrap, kiểm `state=issued`, digest/generation, `expires_at > clock_timestamp()` và chưa có account; insert `manager_account(id=1)` rồi set link `consumed`/xóa digest trong cùng commit. DB unique/check là phòng tuyến thêm. Một request thắng, request còn lại lỗi chung; rollback phải hoàn nguyên cả account và trạng thái link. Mốc đúng 72:00 từ acceptedAt đã hết hạn.
- Form từ #43 giữ token chỉ trong memory; submit lỗi validation còn sửa khi link còn hợp lệ. FE gọi API bằng TanStack Query + fetch theo TS22 (ticket FE đầu tiên thực sự gọi API), giữ cookie cùng origin và CSRF đúng. Không tự cấp JWT/session từ setup; hoàn tất chuyển tới trang login #45. Mất response sau commit báo chưa rõ và hướng thử đăng nhập sau này, không lộ email hay xác nhận đã tạo khi chưa đối chiếu.
- Dữ liệu: link thử còn 71:59 và đúng 72:00 từ acceptedAt, sai/used/generation cũ, mật khẩu thiếu/sai/không khớp, hai JVM hoặc hai browser cùng submit, DB lỗi trước commit/sau commit, Redis down. Chỉ email quản lý thử và DB test cô lập.

## Bằng chứng và rủi ro

Dev bàn giao commit/image, Testcontainers PostgreSQL/Redis, lệnh hai JVM, DB cuối đúng một row manager, link consumed, HTTP/status/CSRF/Origin, ảnh UI mobile và log đã che secret; QA theo [test spec Java](../test-spec.md) nhóm #44. Nếu không chứng minh transaction một commit hoặc clock mốc acceptedAt, trả Tech/Dev sửa; không dùng SQL spike/QA Node cũ làm pass. #44 hoàn tất mới giải cạnh HARD tới #45.

Căn chỉnh15/10/2026: MVC5a4a130/JUnit6, secretfileEDGE_SECRET_FILE. Giữ changeset001bootstrap bất biến; thêm migrationaccount riêng. BA `/root/review_standards` vàTech `/root/review_spec` rà lại nội dung khôngP0/P1; chưa cấp readiness trướcQA#43.
