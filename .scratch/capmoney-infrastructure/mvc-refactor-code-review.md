# Code review INFRA-01.2 / #48

Fixed point5c55101, source MVC WIP sau Planio setup/readiness; code commit được ghi trong Dev/QA evidence sau khi chốt snapshot. Không review thay đổi AGENTS.md có sẵn của Ninh. Scope: main/test Java package moves, health service/status extraction, Docker probe classpath, target exclusion trong Docker context.

## Standards

Reviewer độc lập `/root/review_standards`:0 findings/0 blockers. Controller xử lý HTTP, service dependency checks, model biểu diễn trạng thái; không thêm interface/DTO/repository/module rỗng. Logic trust/Origin và chính sách giữ tài nguyên được giữ. Build test dùng sourcecopy mới tránh staleclass.

## Spec

Reviewer độc lập `/root/review_spec`: khôngP0/P1 hoặc scopecreep. Layout, status codes, root scan và đường dẫn probe/Docker khớp thiết kế. IngressFilter chỉ đổi package, SecurityConfiguration chỉ đổi package/import; không đổi dependency/timeout. Unit tests và import Spring context đúng interface mới. AC-MVC01-3 còn cần bằng chứng runtime/probe và fullQA, không gán pass cũ.

Hai kết luận này là review code, không tự cấpQA_PASS. RootDev đã chạy unit/SpringBoot/Testcontainers thật:6 tests,0fail/errors,1skipMailpit host; pass5, Mailpit sẽ chạy riêng trong network nội bộ. Runtime/probe build và QA độc lập trêncommit mới vẫn cần trước kết luận hoàn tất.
