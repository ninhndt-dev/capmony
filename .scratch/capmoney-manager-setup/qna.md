# Q&A CM-01

| Mã | Câu hỏi | Trả lời và loại quyết định | Nguồn |
| --- | --- | --- | --- |
| CM01-Q1 | Kiểm chứng email thiết lập bằng hộp thư thử trước hay chỉ khi có email thật của Ninh? | **Ninh đã xác nhận A ngày 14/10/2026:** hộp thư thử và dữ liệu thử trước; kiểm email thật khi có hạ tầng. Đây là kế hoạch kiểm thử, không thay yêu cầu gửi email thật khi vận hành. | Câu trả lời trên giao diện trong phiên thực hiện full luồng CM-01. |

## Đề xuất và chưa rõ

- **Tech đề xuất, chưa phải quyết định sản phẩm:** framework, cơ sở dữ liệu, nhà cung cấp gửi mail, cơ chế phiên, mật khẩu và ngưỡng giới hạn thử đăng nhập. Những lựa chọn này phải đáp ứng AC của [spec](spec.md) và được review trước Dev.
- **Chưa rõ cho vận hành thật:** email quản lý riêng của Ninh, tên miền gửi và quyền cấu hình dịch vụ. Không cần dùng dữ liệu thật để BA/Tech thiết kế hoặc thử trong môi trường riêng; không ghi bí mật vào Git.
