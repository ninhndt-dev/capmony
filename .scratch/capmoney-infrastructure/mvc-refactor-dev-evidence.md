# Dev evidence — INFRA-01.2 / #48

Ngày15/10/2026. Người code `/root`, sau Planio#47Closed và #48In Progress/readiness. Fixedpoint5c55101. Chưa QA_PASS cho code mới.

## Thay đổi

Giữ một Maven project/rootApiApplication. Di chuyển config/security; health controller chỉHTTP, service trảHealthStatus và giữ query/ping/error semantics cũ. Probe test-only chuyển securitypackage và đóng gói đúng BOOT-INF/classes. Các file được rename, lịch sử/nội dung có trong Git; không xóa tài nguyên. Không tạo businessentity/repository/DTO hay thay dependency/timeout. Docker context bỏ qua target cũ.

## Kiểm đã chạy

Host source/pom/wrapper copy mới vào.local/build/mvc-dev-16ed5c63327c4651a94524570fab0c72.16file được so bytes/hash với source, target cũ giữ nguyên. [Manifest](evidence/mvc-dev/source-manifest.json).

Lệnh wrapper `-B -f <fresh>/pom.xml test`, JDK25 và Docker VMcapmoney-infra. Biến TESTCONTAINERS_RYUK_DISABLED=true, TESTCONTAINERS_REUSE_ENABLE=true, CAPMONEY_CONTAINER_TESTS=true. Exit0;6tests,5pass/1skip(Mailpit host chưaSMTP_HOST),0failure/error. Có HealthService outage tests và SpringBoot context với PG/Redis thật; không coi skipMailpit là pass. [Summary](evidence/mvc-dev/maven-summary.txt). APIkeyPlanio không nằm trong env/build/source hay output.

Hai trục [code review](mvc-refactor-code-review.md) độc lập không có blocker. Snapshot/image build, lệnh runtime/probe và QA mới sẽ bổ sung; chưa chuyểnQA_PASS310ad7f sang refactor.

## Snapshot và image Dev đã build

Code `5a4a130b9b207ee8478fb4e958acfbe541e3d958`, source không đổi sau hosttest. Docker build3target(runtime/probe/build)exit0; `--rm=false` giữ cả intermediatecontainers, labels org.opencontainers.image.revision đúng SHA. [images.json](evidence/mvc-dev/images.json) ghi tag/digest/lệnhlog.

- Runtime: sha256:1bc05a0431e1c054c40c176854f800850fb5f778356bc1b81ce9292955ffc445.
- Probe: sha256:14d7e4e9230b7965b3764923d398895e9acda72c304f660b9fe4b48d4a5a2405.
- Tools/build: sha256:f23c78cff8c33c0eb207be63f44e72c9e969b201996a10cff3fa957840f4ac3a.

PM đối chiếu code review độc lập + hostMaven/SpringTestContainers thật và3build: DEV_REVIEWED đạt riêng#48, chờ QA độc lập trên đúngimage. Không dùng pass#47 thay thế.
