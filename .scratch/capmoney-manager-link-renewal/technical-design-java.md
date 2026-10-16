# CM-02 — thiết kế kỹ thuật Java (bản nháp để review)

**Ngày:** 16/10/2026. **Trạng thái:** thiết kế này đã được triển khai trong code theo yêu cầu mới của Ninh; chưa có review độc lập, `TECH_READY`/`DEV_REVIEWED`/`QA_PASS` không được tự suy từ code. [Đặc tả hành vi](spec.md) và [F23–F24](../../docs/requirements/user-flows.md) là nguồn sản phẩm. Hợp đồng CM-01 hiện hành ở [thiết kế Java](../capmoney-manager-setup/technical-design-java.md); [phương án Next.js/Resend cũ](../capmoney-manager-setup/technical-options.md) chỉ là lịch sử.

## 1. Trạng thái code trước CM-02

- CM-01 có `manager_bootstrap` singleton, generation, token digest, trạng thái `empty/sending/issued/unknown/failed/consumed`, mốc `accepted_at` và `expires_at`; chỉ CLI nội bộ phát hành link. `ManagerSetupStore.complete` khóa singleton và từ chối ở đúng/sau mốc 72 giờ theo giờ PostgreSQL.
- `POST /api/manager/setup` trả cùng `SETUP_LINK_UNAVAILABLE` cho token sai, hết hạn, đã dùng hoặc đã bị thay. Web chưa có đường biết chắc một link **đã hết hạn** và chưa có thao tác xin lại.
- `BootstrapStore.reserve` từ chối mọi trạng thái `issued`, kể cả link đã hết hạn. Dùng nguyên CLI hiện tại cho CM-02 sẽ không đáp ứng yêu cầu; mở CLI ra HTTP cũng không có kiểm soát người yêu cầu.
- Khi đổi `token_digest` để giữ một generation hiện hành, hệ thống mất dấu link cũ. Nếu mail gửi lần mới lỗi/chưa rõ, trang còn giữ token cũ cần một cách thử lại an toàn. Cần migration và giao dịch riêng, không sửa migration `001-cm01-bootstrap` đã áp dụng.

## 2. Hợp đồng đề xuất và điểm cần PM/BA rà

**Đề xuất an toàn:** người xin lại phải mở email chứa **link hết hạn thuộc generation gần nhất**. Web đã đọc token từ fragment vào bộ nhớ; API nhận token trong JSON body, không nhận token ở URL/query/log. `POST /api/manager/setup-link/status` trả `active` nếu digest khớp link hiện hành còn hạn, `expired` nếu khớp link hiện hành đã hết hạn, `retryable` nếu khớp `renewal_proof_digest` sau lần gửi lỗi/chưa rõ, và `unavailable` cho trường hợp khác; mọi trạng thái còn đòi chưa có quản lý. `POST /api/manager/setup-link/renew` nhận chính token đó. Trang `/manager/setup` hiện nút xin lại khi API xác định `expired` hoặc `retryable`; đường dẫn trực tiếp không có token giải thích cần mở lại email, không cho nhập/chọn email đích. Token cũ được dùng làm **bằng chứng xin lại**, không dùng để tạo quản lý sau khi hết hạn.

Điểm này chưa được Ninh xác nhận riêng: đặc tả hiện nói “truy cập trang xin lại bằng đường dẫn trực tiếp” nhưng chưa nói đường dẫn trống có được kích hoạt gửi mail hay không. PM/BA phải đối chiếu trước `BA_READY`; nếu yêu cầu xin lại không cần link cũ, cần thiết kế endpoint công khai khác và chấp nhận rủi ro người ngoài làm cạn hạn mức gửi. Không coi đề xuất này là quyết định sản phẩm đã chốt.

**API đề xuất:** cùng origin, qua Caddy/edge secret/Origin/CSRF hiện có, JSON tối đa 4 KiB, `Cache-Control: no-store`. Không trả email đích, token, generation hoặc trạng thái SMTP chi tiết. Status đọc qua POST để token không nằm trong URL. Renew chỉ trả thành công sau khi SMTP báo nhận **và** DB commit `issued`; câu chữ là “đã gửi yêu cầu tới dịch vụ email”, không khẳng định thư đã tới hộp. Lỗi SMTP rõ ràng/chưa rõ/DB commit chưa rõ trả thông báo không xác nhận đã gửi; client không tự gửi lại, cho người dùng thử lại sau khi trạng thái được đối chiếu. Sau khi manager tồn tại, cả status/renew không gửi mail và không tạo account; UI dẫn đăng nhập nếu phù hợp nhưng không tiết lộ email.

## 3. Dữ liệu, khóa và thời gian

Migration mới cho `manager_bootstrap` thêm `renewal_proof_digest varchar(64) NULL`, có check định dạng hex và không đồng thời giữ proof sau `consumed`. Trường này chỉ giữ digest của link hết hạn đã chứng minh quyền xin lại khi một lần gửi mới đang `sending/unknown/failed`; không dùng trong `ManagerSetupStore.complete`. Sau khi link mới `issued`, xóa proof. Khi link mới hết hạn, chính `token_digest` của generation đó là proof cho lần xin tiếp theo. Không lưu token nguyên bản, email/token trong log hay browser storage.

`renew` khóa row singleton (`PESSIMISTIC_WRITE`) và đọc `clock_timestamp()` trong transaction. Chỉ bắt đầu khi không có `manager_account`, email DB khớp `MANAGER_EMAIL`, và:

1. `state=issued`, digest token khớp `token_digest`, `now >= expires_at`; hoặc
2. `state=failed/unknown` (hay `sending` đã quá lease), digest khớp `renewal_proof_digest` từ một lần renew trước.

Trước khi gửi SMTP, trong cùng transaction lưu proof từ link hết hạn (hoặc giữ proof cũ), tăng generation, thay `token_digest` bằng digest mới, ghi `sending`, `send_started_at=DB now`, xóa `accepted_at/expires_at`, commit. Từ commit đó, link cũ không thể hoàn tất. Không nhận thêm lần renew khi `sending` còn lease hoặc link mới `issued` còn hạn. Nếu hai request đua, chỉ request giành khóa đầu được reserve; request sau không gửi mail. Nếu submit CM-01 đua với renew trước mốc hạn thì renew bị từ chối; tại/qua mốc hạn thì submit bị từ chối. Unique manager singleton vẫn là phòng tuyến cuối.

Gửi SMTP ngoài transaction như `BootstrapIssuer`: timeout hữu hạn, chụp `acceptedAt` ngay sau `mail.send` trả thành công; kiểm chênh app/DB và CAS `generation + sending + no manager` trước khi ghi `issued`, `accepted_at=acceptedAt`, `expires_at=acceptedAt+72h`. Giữ nguyên mốc 72 giờ từ SMTP accepted quan sát được, không tính lại từ lúc CAS. Nếu SMTP từ chối rõ: `failed`; timeout/không rõ hoặc CAS/DB lỗi: `unknown`. Cả hai trường hợp không cho dùng link mới cho tới khi DB có `issued`; giữ `renewal_proof_digest` để thử lại có kiểm soát. Callback gửi cũ không được chuyển generation mới sang `issued`. Nếu kết quả commit chưa rõ, client không khẳng định thành công; khi thử lại phải đọc trạng thái: đã `issued` thì báo kiểm tra hộp thư/chờ hết hạn, chưa `issued` thì cho reserve generation mới sau cooldown. Không bao giờ cấp hai link hiệu lực cùng lúc.

## 4. Giới hạn và bảo vệ riêng CM-02

Redis Lua nguyên tử theo IP tin cậy từ `IngressFilter` và khóa quota chung; kiểm trước reserve và trả `429` cùng `Retry-After`, Redis lỗi thì `503` không gửi. Code hiện giới hạn **3 lần xin lại/IP/giờ**, **10 lần xin lại toàn hệ thống/24 giờ**, **15 phút giữa hai lần reserve**, và riêng endpoint kiểm trạng thái là 30/IP/giờ, 300/toàn hệ thống/15 phút. Quota Redis đếm lượt xin lại đã qua kiểm proof, kể cả lượt thua race DB; thao tác đồng thời vẫn chỉ một lần gửi do row lock/generation. Request sai hình thức hoặc không có proof không đốt quota gửi. Các ngưỡng và tác động lên người dùng còn cần PM rà.

Giới hạn toàn cục có thể bị người cầm link cũ làm Ninh chờ 24 giờ; ngưỡng và thông điệp phải được PM rà tác động trải nghiệm. Không hiện địa chỉ email cấu hình, kể cả che một phần, trừ khi Ninh chốt UI khác. API không được trở thành endpoint tự tạo admin hoặc gửi đến email từ request. CSRF/Origin, edge secret, body limit, no-store, referrer policy áp dụng như CM-01.

## 5. Web và lỗi

Khi mở link setup, giữ token trong bộ nhớ tab, xóa fragment ngay. Gọi status chỉ sau khi đã có token hợp lệ về hình thức. `active` hiện form thiết lập; `expired` hiện thông báo hết hạn và nút xin lại; `retryable` hiện thông báo lần gửi trước chưa xác nhận và cho xin lại sau thời gian chờ; `unavailable` hiện thông báo chung và đường đăng nhập. Nếu status timeout, báo “chưa kiểm tra được” và cho thử lại; không suy ra hết hạn. Sau renew thành công, xóa token cũ khỏi memory, báo kiểm tra email; link mới mở trang setup bình thường. Nếu renew timeout/503, không tự gửi lại; hiển thị kết quả chưa rõ và nút kiểm tra trạng thái. Reload sau khi fragment đã xóa vẫn theo hợp đồng CM-01: cần mở email lại. Không cache token trong TanStack Query hay session/local storage.

Không dùng mã `SETUP_LINK_UNAVAILABLE` của POST setup để tự suy luận `expired`; mã này bao gồm nhiều nguyên nhân. Lỗi gửi mail chưa rõ không được hiển thị là “đã gửi”.

## 6. Kiểm chứng trước gate và Dev

- **BA/PM:** chốt quy tắc cần link hết hạn khi xin lại, cách vào đường dẫn trực tiếp, ngưỡng quota/cooldown và câu chữ sau kết quả SMTP chưa rõ. Rebaseline spec 20 mục, AC có mã, review BA/PM trước `BA_READY`.
- **Tech reviewer độc lập:** kiểm race reserve/complete/status, gửi SMTP ngoài transaction, proof sau failed/unknown, quota Redis nhiều JVM, khả năng retry khi DB commit chưa rõ và quan hệ với CM-01; PM kiểm phạm vi rồi mới `TECH_READY`.
- **Dev/QA:** migration trên DB thử riêng; đồng hồ trước/đúng/sau 72 giờ; old/new email đến lệch thứ tự; SMTP reject/timeout; Redis/DB outage; hai JVM renew đồng thời và renew đua complete; sau khi manager được tạo không phát mail. Test tích hợp hiện có mới kiểm hai request song song trong một JVM và cơ sở dữ liệu thật; hai JVM riêng, Mailpit thật, WebKit/Chromium với API thật và thiết bị vật lý vẫn là kiểm chứng tiếp theo. Không reset/xóa dữ liệu thật để test.
- CM-01 #44 và #45 còn gate độc lập; chỉ dùng hợp đồng/code đã có làm nền thiết kế, không coi CM-02 sẵn sàng triển khai hay CM-01 đã QA_PASS.
