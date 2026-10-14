# Test Specification: CM-01 – Thiết lập và đăng nhập quản lý

**Rebaseline 14/10/2026:** Các ca kiểm dưới đây cần đối chiếu lại với Java Spring, JWT cookie và Redis theo [TS10–TS38](../../docs/requirements/tech-stack-round-2.md). Kết quả/spike trên Node/Hono hoặc opaque session không chứng minh bản Java; chưa có `QA_PASS` cho CM-01.

**QA gate:** chưa chạy; đây là kế hoạch kiểm thử theo `BA_READY` và `TECH_READY` ngày 14/10/2026. Chỉ điền pass/fail trên commit/build xác định, Mailpit và PostgreSQL thử cô lập. Ninh đã chọn hộp thư thử trước; email thật và thiết bị thật kiểm khi có hạ tầng. Spike Tech ở `/tmp` không thay kết quả QA của ứng dụng.

| Mã | AC/BR/State | Chiều | Dữ liệu/điều kiện | Thao tác và kết quả mong đợi | Thực tế/bằng chứng |
| --- | --- | --- | --- | --- | --- |
| T01 | AC1, BR-01-01 | AC, integration | DB trống, email quản lý thử đã cấu hình, Mailpit sạch | Chạy lệnh khởi tạo; đúng một email có link tới hộp thử đã cấu hình; không có form/API đổi email đích | not-run |
| T02 | AC6 | error/recovery | SMTP từ chối hoặc cổng sai | Chạy lệnh; không báo đã gửi; không có link trạng thái hợp lệ | not-run |
| T03 | AC6 | error/recovery, state | Mô phỏng SMTP chấp nhận nhưng ghi DB chưa rõ/lỗi | Lệnh báo chưa rõ; retry an toàn, thế hệ cũ không dùng được, đúng tối đa một link hiệu lực | not-run |
| T04 | AC1, AC6 | concurrency | DB trống; hai lệnh khởi tạo đồng thời | Chỉ một thế hệ/link được phát hành; email cấu hình khác DB bị chặn | not-run |
| T05 | AC2, BR-01-02 | AC, boundary | Link thử còn hạn, đúng mục đích | Mở link, nhập mật khẩu đạt chính sách, hoàn tất trước mốc 72 giờ; tạo đúng một quản lý | not-run |
| T06 | AC2 | boundary, security | Link sai, hết hạn đúng mốc 72h, đã dùng, thế hệ cũ | Từng link bị từ chối, không tạo quản lý; không lộ token hoặc email qua URL request/log | not-run |
| T07 | AC2 | validation | Mật khẩu quá ngắn/dài; xác nhận không khớp | Giữ form để sửa, không tạo quản lý hoặc tiêu thụ link | not-run |
| T08 | AC5, BR-01-03 | concurrency, state | Một link hợp lệ; hai browser/tiến trình thật | Hai POST gần đồng thời: tối đa một thành công, DB cuối đúng một quản lý, link consumed | not-run |
| T09 | AC4 | permission, regression | Đã có quản lý | Lặp lệnh khởi tạo, dùng lại link hoặc gọi đường setup trực tiếp; không tạo quản lý thứ hai | not-run |
| T10 | AC3, BR-01-04 | AC, integration | Quản lý vừa lập | Đăng nhập đúng, tải lại và mở URL quản lý trực tiếp; phiên còn hạn hiển thị khu vực quản lý tối thiểu | not-run |
| T11 | AC3 | security, permission | Khách và email/mật khẩu sai | Đăng nhập sai không tạo phiên; khách mở URL/route quản lý bị chặn ở server; `/signup` không tồn tại | not-run |
| T12 | AC3 | state, boundary | Phiên hợp lệ; đồng hồ DB test điều khiển được | Hết hạn hoặc thu hồi phiên rồi tải lại/đánh URL trực tiếp; từ chối và yêu cầu đăng nhập | not-run |
| T13 | AC3 | security, error | POST setup/login/logout từ Origin thiếu hoặc sai; DB throttle lỗi | Từ chối trước đổi trạng thái, không cấp phiên; lỗi chung không lộ email/token | not-run |
| T14 | AC3 | security, concurrency | Hai tiến trình ứng dụng chung DB, nhiều yêu cầu sai | Quota IP/tài khoản tính chung và nguyên tử; lần vượt ngưỡng nhận thời gian thử lại, phiên đang hợp lệ còn dùng được; reset ở cửa sổ 15 phút | not-run |
| T15 | AC1–AC5 | UI, integration | Build chạy trên web mobile, hộp thư thử | Chạy luồng mở email → thiết lập → đăng nhập trên Chrome mobile emulation, iPhone và Android thật khi có thiết bị; mở trang setup thiếu fragment/tải lại sau khi fragment bị xóa phải hướng dẫn mở lại email; ghi ảnh và kích thước màn hình | not-run |
| T16 | AC1, AC6 | integration, release | Hạ tầng email thật được Ninh cấp sau | Gửi email thật tới email quản lý đã cấu hình, kiểm tới hộp và link; ghi build, thời gian, bằng chứng đã che dữ liệu nhạy cảm | blocked: chưa có hạ tầng email thật |

Các chiều AC, BR, state, boundary, error/recovery, security/permission, integration, regression đều có case. QA phải ghi commit/build, lệnh chạy, môi trường, log/ảnh và kết quả từng case trước khi đề xuất `QA_PASS`. Nếu chưa có iPhone/Android thật hoặc email thật, ghi rõ giới hạn và xin Ninh xác nhận ngoại lệ theo workflow; không tự đánh dấu pass.
