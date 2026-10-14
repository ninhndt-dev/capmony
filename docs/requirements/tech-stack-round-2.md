# Quyết định tech stack toàn dự án – vòng 2

**Ngày:** 14/10/2026. Ninh yêu cầu hỏi rõ **dùng công nghệ gì cho từng phần** trước khi tiếp tục code. Câu trả lời qua giao diện chọn và câu trả lời tự do được ghi dưới đây. Vòng này thay phần BE Node/Hono, CSS Modules và cách triển khai production bằng Compose trong [vòng 1](tech-stack-round-1.md)/[ADR 0002](../adr/0002-project-infrastructure-and-stack.md). Những phần không bị thay chỉ còn hiệu lực theo nguồn đã xác nhận; Tech phải lập ADR mới và PM rà lại gate trước khi giao Dev.

## Ninh đã xác nhận

| Mã / câu | Phần | Ninh chọn | Tác động |
| --- | --- | --- | --- |
| TS10 | BE API | **Java Spring**, trả lời tự do | Thay Node.js/Hono/TypeScript API; dừng QA/code stack cũ |
| TS11 / Q1-A | Java | **Java 25 LTS** | Chọn JDK/JRE tương ứng, chưa pin bản vá |
| TS12 / Q2-A | Framework BE | **Spring Boot 4.1** | Chọn bản vá tương thích sau Tech review |
| TS13 / Q3-A | Build BE | **Maven** | Build và khóa dependency Java bằng Maven |
| TS14 / Q4-A | HTTP BE | **Spring MVC** | API đồng bộ, không chọn WebFlux |
| TS15 / Q5-B | Dữ liệu BE | **Spring Data JPA** | Transaction/khóa cho nghiệp vụ tiền phải được Tech chứng minh riêng |
| TS16 / Q6-B | Migration DB | **Liquibase** | Migration có version; format changelog là chi tiết Tech |
| TS17 / Q7-A | Bảo mật BE | **Spring Security** cùng tài khoản/phiên riêng | Thiết kế auth phải cập nhật theo TS30–TS34, không lấy mã Node làm nền |
| TS18 / Q8-A | Email BE | **Spring Mail / JavaMailSender** qua SMTP | Mailpit tiếp tục là hộp thử; SMTP thật chọn sau |
| TS19 / Q9-A | Build FE | **Vite + React** | Web tĩnh gọi API Java riêng |
| TS20 / Q10-A | Điều hướng FE | **React Router** | Màn nhiều route/deep link |
| TS21 / Q11-B | Giao diện FE | **Tailwind CSS** | Thay đề xuất CSS Modules của ADR 0002 |
| TS22 / Q12-A | Dữ liệu FE | **TanStack Query + fetch** | Quản lý trạng thái request/refetch; không tự bật offline |
| TS23 / Q13-A | Edge | **Caddy** | HTTPS/reverse proxy web và API |
| TS24 / Q14-B | Production VPS | **Cài Java, PostgreSQL, web trực tiếp trên VPS** | Thay đề xuất production Docker Compose; Docker chỉ dev/test |
| TS25 / Q15-A | CI | **GitHub Actions** | Build/test khi đẩy code; phát hành thật vẫn qua gate |
| TS26 / Q16-B | Backup production | **Chưa sao lưu bản đầu; chấp nhận mất dữ liệu nếu VPS/volume hỏng** | PM phải nhắc lại rủi ro trước vận hành thật; volume không phải backup |
| TS27 / Q17-A | Hệ điều hành VPS | **Ubuntu Server 24.04 LTS** | Chưa có VPS/quyền truy cập |
| TS28 / Q18-A | JDK production | **Eclipse Temurin OpenJDK 25** | Dùng bản phân phối này khi cài Java trên VPS |
| TS29 / Q19-A | Chạy API production | **systemd service** | Caddy chuyển tiếp đến service nội bộ; chưa triển khai VPS |
| TS30 / Q20 khác | Phiên đăng nhập | **JWT** | Thay phương án opaque token/phiên trước đây; phải thiết kế lại CM-01 |
| TS31 / Q21-A | Lưu JWT trên web | **Cookie HttpOnly/Secure** | Trình duyệt không đọc token bằng JavaScript |
| TS32 / Q22-A | Thời hạn | **Access JWT 15 phút + refresh token 7 ngày** | Cơ chế refresh/reuse phải được thiết kế, kiểm thử |
| TS33 / Q23-A | Thu hồi | **Thu hồi ngay khi đăng xuất/đổi mật khẩu qua trạng thái trong DB** | Tech xác định khóa/phiên và kiểm mỗi request; không coi JWT tự nó thu hồi được |
| TS34 / Q24-B | Giới hạn thử đăng nhập | **Redis riêng** | Redis là dịch vụ phụ trợ cho bộ đếm; dữ liệu tài chính vẫn ở PostgreSQL |
| TS35 / Q25-A | Redis client | **Spring Data Redis + Lettuce** | Redis dev/test và VPS phải có cấu hình riêng |
| TS36 / Q26-A | Test BE | **JUnit 5 + Spring Boot Test + Testcontainers** | Kiểm PostgreSQL/Redis thật trên build Java |
| TS37 / Q27-A | Hợp đồng HTTP | **OpenAPI 3 + springdoc** | Tech xác minh bản springdoc tương thích Spring Boot 4.1 trước khi pin |
| TS38 / Q28-A | Test FE | **Playwright với viewport iPhone/Android** | QA thực chạy trên build; viewport không thay kiểm thiết bị thật khi gate yêu cầu |

## Trạng thái và điều còn cần Tech xác minh

- **Đã xác nhận:** lựa chọn TS10–TS38 ở trên. [Spring Boot 4.1 yêu cầu ít nhất Java 17 và hỗ trợ Java 25](https://docs.spring.io/spring-boot/system-requirements.html); [springdoc v3 là nhánh hỗ trợ Spring Boot 4](https://github.com/springdoc/springdoc-openapi). Đây là kiểm tra khả thi ban đầu, chưa phải build CapMoney Java đã chạy.
- **Đề xuất Tech cần review:** bản vá cụ thể, dependency tương thích, hợp đồng JWT/refresh/revocation, transaction JPA cho tiền, dữ liệu Redis khi lỗi, cấu hình Spring Security/Caddy, cách triển khai systemd và rollback. Không tự nâng thành gate từ câu trả lời chọn thư viện.
- **Chưa rõ cho vận hành:** VPS, domain/DNS, SMTP thật, thời điểm phát hành và quyền cài đặt. Không chặn lập kiến trúc dev/test mới.
- **Bằng chứng stack cũ:** commit Node/Hono `493f3d4` chỉ là lịch sử thử hạ tầng. QA [IF-T03 fail](../../.scratch/capmoney-infrastructure/qa-evidence.md), các case còn lại chưa đủ; không tái sử dụng kết quả để cấp `QA_PASS` cho Java.

## Điểm tương thích phát hiện ngày 15/10/2026 — chờ Ninh

TS36 ở bảng trên vẫn là **JUnit 5 đã được Ninh chọn**. Build Spring Boot 4.1.1 hiện resolve JUnit Jupiter 6.0.3; [QA Java](../../.scratch/capmoney-infrastructure/qa-evidence-java.md) đã chạy 8/8 case hạ tầng trên snapshot `310ad7f`, không phải trên JUnit 5. [Spring Framework 7 nâng baseline lên JUnit 6](https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-7.0-Release-Notes). Vì vậy không pin hạ JUnit 5 hoặc tự đổi TS36 chỉ để chốt gate.

**Đề xuất Tech, chưa xác nhận:** Giữ Java25/Spring Boot4.1 đã chọn và đổi riêng TS36 sang JUnit6 + Spring Boot Test + Testcontainers; bản thực nghiệm đã có dependency6.0.3 và bằng chứng chạy. **Lựa chọn còn lại:** Giữ JUnit5 và quay lại review stack/framework tương thích trước khi code thêm. PM giữ gate cuối #47 và phụ thuộc #43 chờ Ninh trả lời IF-Q6; kết quả chạy thành công vẫn được lưu trung thực.
