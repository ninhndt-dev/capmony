# Q&A CM-01

| Mã | Câu hỏi | Trả lời và loại quyết định | Nguồn |
| --- | --- | --- | --- |
| CM01-Q1 | Kiểm chứng email thiết lập bằng hộp thư thử trước hay chỉ khi có email thật của Ninh? | **Ninh đã xác nhận A ngày 14/10/2026:** hộp thư thử và dữ liệu thử trước; kiểm email thật khi có hạ tầng. Đây là kế hoạch kiểm thử, không thay yêu cầu gửi email thật khi vận hành. | Câu trả lời trên giao diện trong phiên thực hiện full luồng CM-01. |
| CM01-Q2 | Khi Ninh đăng xuất tài khoản quản lý trên một điện thoại, các điện thoại khác đang đăng nhập cùng tài khoản có tiếp tục dùng được không? | **Ninh đã xác nhận B ngày 14/10/2026:** đăng xuất trên một điện thoại thì đăng xuất tất cả điện thoại. Mọi token cũ của tài khoản quản lý bị thu hồi ngay qua trạng thái DB; lần truy cập tiếp theo trên điện thoại khác phải đăng nhập lại. | Trả lời trên giao diện chọn trong phiên rebaseline CM-01; TS33 và R56 là bối cảnh. |
| CM01-Q3 | Refresh token cũ xuất hiện lại sau khi đã dùng, không phân biệt chắc được retry hợp lệ và replay thì xử lý thế nào? | **Ninh đã xác nhận A ngày 14/10/2026:** thu hồi ngay phiên/family liên quan và yêu cầu đăng nhập lại. Ninh chấp nhận có thể phải đăng nhập lại khi hai tab cùng refresh hoặc phản hồi bị mất. Đây **không** phải lựa chọn đăng xuất mọi điện thoại; phạm vi của CM01-Q2 chỉ áp dụng khi người dùng chủ động đăng xuất. | Trả lời trên giao diện chọn sau Tech review P1.2; [review](../capmoney-infrastructure/java-tech-review.md). |

## Đề xuất và chưa rõ

- **Ninh đã xác nhận về stack:** Java Spring, PostgreSQL, JWT trong cookie `HttpOnly`/`Secure`, access 15 phút, refresh 7 ngày, thu hồi ngay qua DB, Redis cho bộ đếm giới hạn thử đăng nhập, Spring Mail/SMTP. Xem [TS10–TS38](../../docs/requirements/tech-stack-round-2.md); các câu trả lời này thay các lựa chọn Node/opaque session/DB throttle trong thiết kế cũ.
- **Tech đề xuất, chưa phải quyết định sản phẩm:** chính sách mật khẩu/ngưỡng giới hạn, chi tiết giao dịch refresh/CSRF và khi Redis lỗi, mô hình trạng thái thu hồi, nhà cung cấp SMTP thật. CM01-Q3 đã chốt cách xử lý reuse không phân biệt được; PM rà diễn đạt và Tech chứng minh bằng test trước gate.
- **Đã chốt cho CM-01:** CM01-Q2 về phạm vi đăng xuất nhiều điện thoại; CM01-Q3 về refresh reuse; PM cần đối chiếu AC7 và Tech thiết kế/kiểm thử thu hồi đúng phạm vi.
- **Chưa rõ cho vận hành thật:** email quản lý riêng của Ninh, tên miền gửi và quyền cấu hình dịch vụ. Không cần dùng dữ liệu thật để BA/Tech thiết kế hoặc thử trong môi trường riêng; không ghi bí mật vào Git.
