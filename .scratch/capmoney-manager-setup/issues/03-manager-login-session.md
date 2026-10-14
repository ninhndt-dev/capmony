# CM-01.3 / #45: Đăng nhập và giữ phiên quản lý

**Parent:** [CM-01 / Planio #8](https://tanninh.planio.com/issues/8).
**Planio child:** [#45](https://tanninh.planio.com/issues/45).
**Status:** `blocked-by-hard`, **chưa `READY_FOR_IMPLEMENTATION`**; BA/Tech phải rà ticket Java và PM xét gate riêng. Opaque session/counter DB ở thiết kế Node chỉ là lịch sử.
**Blocked by:** `HARD` [CM-01.2 / #44](02-complete-manager-setup.md) hoàn tất với QA xác nhận các case của **ticket #44**; `QA_PASS` cấp Story CM-01 chỉ xét sau cả #43–#45. #44 còn chờ QA#43; hạ tầng#47/#48Closed.
**Integration blocked by:** None sau #44.
**Owner / review:** CapmmDev sau readiness; BA rà AC, reviewer code độc lập, PM rà phạm vi và QA độc lập chạy build.

## What it delivers

Ninh đăng nhập bằng email quản lý/mật khẩu đã đặt, tải lại hoặc mở URL trực tiếp vẫn thấy khu vực quản lý tối thiểu khi phiên hợp lệ. Access JWT 15 phút và refresh JWT tối đa 7 ngày trong cookie HttpOnly/Secure; server kiểm trạng thái DB mỗi request. Đăng xuất chủ động trên một điện thoại kết thúc mọi điện thoại. Refresh cũ bị dùng lại mà không phân biệt được retry/replay thì chỉ family liên quan bị thu hồi và yêu cầu đăng nhập lại. Khách, login sai, token hết hạn/bị thu hồi hoặc tài khoản ghi chép không xem được khu vực quản lý.

## Acceptance criteria tham chiếu

- [ ] `AC3` — login, tải lại, server guard và chặn khách/token không hợp lệ.
- [ ] `AC4` — quyền quản lý giới hạn, không tạo thêm quản lý hoặc tự cấp quyền khác.
- [ ] `AC7` — JWT/cookie 15 phút/7 ngày, logout mọi thiết bị, refresh reuse chỉ family.

Ba AC lấy từ [spec CM-01](../spec.md). Đặt lại mật khẩu R57 thuộc Story #22; #45 chỉ cung cấp primitive `revokeAll(managerId)` cho #22 dùng về sau.

## Hợp đồng và dữ liệu thử

- [Thiết kế Java §6–7](../technical-design-java.md): Spring Security đọc **cookie** access, không nhận Authorization header; JWT HS256 key ≥256 bit, `kid` allowlist, kiểm typ/iss/aud/iat/nbf/exp và DB account+family/epoch mỗi request. Access 15m, family refresh hết hạn **tuyệt đối 7 ngày từ login**, rotate không kéo dài; access `jti` độc lập current refresh `jti`. Cookie host-only HttpOnly/Secure/SameSite=Lax, access Path=/api, refresh Path=/api/auth; dev HTTP profile riêng, production fail nếu không Secure. Không lưu JWT ở JS storage; clear cookie đúng name/path.
- `GET /api/auth/csrf` public/no-store cấp cookie CSRF JS đọc được; mọi POST login/refresh/logout đòi `Origin` đúng và `X-XSRF-TOKEN`, sau auth transition FE lấy CSRF mới. Caddy peer/secret/IP header trước auth/throttle. `GET /api/manager/me` là server guard trả email+role tối thiểu; FE route guard không phải bảo mật quyết định. Web dùng TanStack Query + fetch (TS22) để gọi API, không cache quyền vượt trạng thái DB.
- Redis Lua throttle login 10 lần/15 phút/IP tin cậy + 60 lần/15 phút/tài khoản quản lý cố định, đếm cả thành công; 429 có Retry-After, TTL cố định. Redis hỏng 503 trước hash/DB, phiên hợp lệ hiện có vẫn kiểm DB và dùng được. Quota này chỉ cho CM-01 quản lý; khi mở #04 nhiều người/NAT phải review lại.
- Refresh transaction khóa account→family, CAS current refresh digest; reuse jti cũ đã xác thực thu hồi **family đó**. Logout ưu tiên access còn hợp lệ đã kiểm DB dưới khóa, kể cả refresh cookie stale, để `revokeAll` tăng epoch và thu hồi mọi family; không có access thì current refresh hợp lệ mới logout-all, stale refresh đơn lẻ không nâng thành logout-all. DB lỗi báo thu hồi chưa rõ, không khẳng định đã đăng xuất mọi nơi; request protected fail closed. Hai tab/mất response có thể cần đăng nhập lại theo CM01-Q3/A.
- Dữ liệu: một manager đã lập từ #44; hai/ba điện thoại thử, hai JVM chung DB/Redis, mật khẩu đúng/sai, IP chung NAT, Redis/DB down, access/refresh trước/sau hạn, hai tab rotate, lost Set-Cookie, stale refresh + valid access logout, token ký sai/alg/kid lạ, Origin/CSRF thiếu/sai. Không dùng tài khoản thật.

## Bằng chứng và rủi ro

Dev ghi commit/image, Testcontainers PostgreSQL/Redis, hai JVM/browser, keyring rotate test, HTTP/cookie attributes, DB epoch/family, quota, logout trên điện thoại khác và ảnh viewport iPhone/Android; QA theo [test spec Java](../test-spec.md) nhóm #45. Không tự gán pass cho email thật, thiết bị thật hoặc code Node. `DEV_REVIEWED` cần reviewer code khác người và PM; `QA_PASS` cần mọi AC trong phạm vi chạy trên build xác định.

Căn chỉnh15/10/2026: MVC/JUnit6 vàEDGE_SECRET_FILE; thêm migrationfamily riêng, không sửa changesetbootstrap/account. Thay HTTPBasic/permit-all hạ tầng bằng cookieauthcontract, khôngAuthorizationfallback. BA `/root/review_standards` vàTech `/root/review_spec` rà lại nội dung khôngP0/P1; readiness chờQA#44.
