# Quyết định tech stack toàn dự án – vòng 1

**Ngày hỏi:** 14/10/2026. Nguồn là câu trả lời của Ninh qua giao diện chọn trong phiên triển khai CM-01. Đây là quyết định kiến trúc dự án; thiết kế Tech và gate CM-01 chịu ảnh hưởng phải được rà lại trước khi tiếp tục code.

**Cập nhật cùng ngày:** Ninh đã chọn Java Spring cho BE và trả lời chi tiết TS10–TS38 trong [vòng 2](tech-stack-round-2.md). TS09/ADR 0002 là lịch sử cho Node/Hono, CSS Modules và production Compose; không dùng các phần đó làm nền triển khai mới. Các lựa chọn còn hiệu lực phải đối chiếu vòng 2 và ADR thay thế.

## Ninh đã xác nhận

| Mã | Nội dung Ninh chọn | Tác động cần Tech/PM xử lý |
| --- | --- | --- |
| TS01 | **Q1/B:** tách React web và API thành hai dịch vụ | Thay đề xuất một ứng dụng Next.js trong ADR 0001; cần hợp đồng HTTP, phiên và origin giữa hai dịch vụ |
| TS02 | **Q2/A lúc đầu:** Vercel cho web/server và Neon PostgreSQL quản lý sẵn khi mở thật | **Được thay bằng TS06 và TS08 bên dưới** sau khi Ninh làm rõ Docker/VPS; giữ ở đây làm lịch sử, không lấy làm quyết định hiện hành |
| TS03 | **Q3/A:** mở rộng cơ chế tài khoản/phiên riêng đã review ở CM-01 cho quản lý và người dùng cá nhân, review bảo mật từng module | Không mặc nhiên dùng Better Auth/Supabase Auth; Tech phải thiết kế tách quyền và dữ liệu cho #04 trở đi |
| TS04 | **Q4/A:** dùng SMTP chuẩn, Mailpit để thử; đổi nhà cung cấp gửi được | Giữ hợp đồng email trong ADR 0001, nhà cung cấp email thật chưa chọn |
| TS05 | **Q5/B:** có hoặc dự kiến có mục đích thương mại/thu phí | Hiện chọn VPS nên không cần gói Vercel; nếu sau này quay về Vercel thì Hobby phi thương mại không phù hợp. Chi phí VPS/SMTP được xét khi mua, chưa được tự chi |
| TS06 | **Q7, câu trả lời khác:** Docker để dev và test; sau này deploy dùng VPS riêng | **Thay TS02 về nơi chạy thật**: không chốt Vercel; dùng Docker cho môi trường dev/test, VPS riêng khi vận hành thật |
| TS07 | **Q8/A:** chốt sơ đồ kiến trúc, ADR, môi trường và thứ tự triển khai; sau review mới dựng Docker | Dừng FE/BE CM-01.1, hoàn tất kiến trúc và review trước; Docker là bước kế tiếp sau kiến trúc |
| TS08 | **Q9/A:** PostgreSQL nằm trong VPS riêng với volume riêng khi vận hành thật | Không dùng Neon cho production theo lựa chọn hiện hành; volume là nơi lưu bền trên VPS, không tự thành bản sao lưu |
| TS09 | **Sau Tech/PM review, Ninh trả lời “continue”** cho câu hỏi duyệt toàn bộ ADR 0002 và sơ đồ hạ tầng để chuyển sang ticket Docker dev/test | Xác nhận tiếp tục theo phương án A vừa được trình; ADR 0002 được chấp nhận ở mức kiến trúc, chưa chứng minh Docker chạy hay cho phép triển khai VPS thật |

## Đề xuất và chưa rõ

- **Ninh trả lời Q6:** “tạm thời dùng docker để triển khai, tôi muốn xây dựng kiến trúc hạ tầng trước fe be devops”. TS06–TS08 làm rõ Docker cho dev/test, VPS riêng với PostgreSQL volume cho production và thiết kế kiến trúc trước khi dựng Docker/FE/BE.
- **TS09 là quyết định lịch sử đã bị TS10–TS38 thay một phần:** khi đó Ninh tiếp tục theo React/Vite, Node/Hono API, Docker dev/test và Compose production. Vòng 2 thay BE bằng Java Spring, CSS Modules bằng Tailwind và production Compose bằng cài trực tiếp; chỉ những phần được xác nhận lại hoặc không mâu thuẫn còn được xem xét trong ADR mới.
- **Chưa rõ cho vận hành thật:** VPS cụ thể, tên miền, email/SMTP thật, quyền quản trị máy và thời điểm triển khai. Không cần đưa bí mật vào Git hoặc tự tạo dịch vụ trả phí ở bước kiến trúc.
