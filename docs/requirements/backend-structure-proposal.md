# Đề xuất cấu trúc BE Spring MVC

Ngày15/10/2026. **Ninh đã đồng ý triển khai ngày15/10/2026; đã triển khai trên5a4a130, qua review và QA8/8; Planio#48Closed.** Tài liệu này không thay hợp đồng hoặc kết quả QA của INFRA-01/#47 trên310ad7f.

## Nguồn yêu cầu

**Ninh đã yêu cầu:** “cấu trúc thư mục dự án BE là theo dự án thư viện nhé cho tường minh và theo mvc”. Khi hỏi phân biệt nhiều module Maven/library với một Maven project chia package MVC, Ninh hỏi cách nào phổ biến nhất hiện nay, rồi đồng ý sau đề xuất một Maven project bằng lời “ok tiến hành setup planio và sau đó sửa cấu trúc thư mục nhé”. Chốt một Maven project/package theo tính năng, phân lớp MVC; không chia nhiều thư viện JAR.

**Phương án Ninh đã đồng ý:** Một Maven project cho BE, package theo tính năng; mỗi tính năng phân lớp rõ theo trách nhiệm MVC/layered. [Spring Boot minh họa package theo tính năng với controller/service/repository/model](https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html), không bắt buộc một layout duy nhất. Đây là cơ sở chọn cấu trúc phù hợp, không phải số liệu chứng minh thị phần/phổ biến nhất. [Spring có hướng dẫn nhiều Maven module gồm library và application](https://spring.io/guides/gs/multi-module/) khi thật sự cần thư viện dùng lại.

## Cấu trúc đã chốt

```text
apps/api/
├── pom.xml
├── mvnw
├── .mvn/wrapper/
└── src/
    ├── main/
    │   ├── java/com/capmoney/api/
    │   │   ├── ApiApplication.java
    │   │   ├── config/
    │   │   ├── security/
    │   │   ├── health/
    │   │   │   ├── controller/
    │   │   │   └── service/
    │   │   └── <tinh-nang-da-du-gate>/
    │   │       ├── controller/
    │   │       ├── service/
    │   │       ├── repository/
    │   │       ├── model/
    │   │       └── dto/
    │   └── resources/
    │       ├── application.yaml
    │       └── db/changelog/
    └── test/java/com/capmoney/api/
        └── <cau-truc-tuong-ung>/
```

Đây là sơ đồ trong tài liệu, không tạo thư mục/class rỗng hoặc schema cho module chưa đủ gate. Tính năng không cần một lớp thì không ép thêm lớp chuyển tiếp. Health chỉ kiểm dependency, không tự tạo entity/repository nghiệp vụ.

| Phần | Trách nhiệm và cách kiểm |
| --- | --- |
| controller | Nhận HTTP, validation đầu vào, gọi service, ánh xạ DTO/status; test hợp đồng HTTP. |
| service | Thực hiện use case, quy tắc và transaction; kiểm nghiệp vụ qua interface của module, nhận dependency qua constructor. |
| repository | Truy cập lưu trữ/JPA; kiểm bằng PostgreSQL thật khi ticket có dữ liệu. Không để controller gọi trực tiếp. |
| model | Entity/value object theo thiết kế ticket đã duyệt; không dùng entity như JSON response mặc định. |
| dto | Hợp đồng request/response ở HTTP; không biến thành schema DB. |
| config/security | Wiring và chính sách dùng chung đã được thiết kế/review; không thành nơi chứa nghiệp vụ của mọi module. |

Luồng mặc định: `HTTP → Controller → Service → Repository → PostgreSQL`. Service trả kết quả để controller ánh xạ response. Đây là cách tổ chức Spring MVC API; phần View của sản phẩm ở React riêng, không thêm Thymeleaf/JSP. Controller không chứa truy vấn DB hoặc quy tắc tiền.

## Khi cân nhắc Maven library riêng

Tách JAR khi có interface ổn định và nhu cầu dùng lại/kiểm soát dependency có thật. Maven parent có thể quản lý module library và application runnable; library không đóng gói executable Boot JAR. Chi tiết module, package scan, migration ownership và transaction phải được Tech review trước khi thay source. Không mặc định mỗi lớp MVC hoặc mỗi Story thành một thư viện.

## Trạng thái sau triển khai

Refactor MVC đã được thực hiện và QA 8/8 trên `5a4a130`, #48 Closed; xem [bằng chứng](../../.scratch/capmoney-infrastructure/mvc-refactor-qa-evidence.md). CM-01 #43–#45 đã dùng bố cục này trong code sau đó. Khi thêm module mới, giữ package theo tính năng và chỉ tạo lớp cần thiết; ticket/thiết kế riêng vẫn quyết định schema, quyền và giao dịch. Không chuyển nguyên QA của snapshot cũ sang code mới; [tổng quan hiện hành](../project-overview.md) và handoff tính năng ghi build/gate tương ứng.
