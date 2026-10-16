# CapMM end to end tại CapMoney

Tài liệu này là quy trình vận hành cho **lần khởi động lại CapMoney**. Nó chuyển thể [workflow Ninh gửi](workflow-bot-capmm-original.md) sang repo hiện có. [Phiếu tiếp nhận](../requirements/00-intake.md) là nguồn quyết định sản phẩm hiện hành; workflow nguồn không xác nhận yêu cầu sản phẩm.

**Áp dụng tại bước 9/11.** Bước 1–8 đã qua các xác nhận ghi trong [quy trình 11 bước](../agents/workflow.md); Ninh đã duyệt cách chia **35 lát** ở bước 8 và chúng nay là **Story ứng viên** trên [Planio](https://tanninh.planio.com/projects/capmoney/issues), với file nguồn trong Git. Việc chuyển tracker không hủy quyết định duyệt cách chia; ticket Dev là hiện vật cấp dưới tạo sau gate. Đặc tả #01–#06 ban đầu được rà theo chuỗi cũ, nên không tự gán gate mới từ những bản đó. Riêng INFRA-01 và CM-01 đã được rebaseline sang Java Spring theo [TS10–TS38](../requirements/tech-stack-round-2.md): #47/#48 và CM-01 #43 có QA ticket, #44 còn chờ QA độc lập, #45 có code/bằng chứng Dev nhưng còn gate #44 và review/QA. Xem [trạng thái mã](../project-overview.md) và [handoff CM-01](../../.scratch/capmoney-manager-setup/handoff.md). Node/Hono và QA IF-T03 là lịch sử, không chứng minh stack Java.

## Nguồn sự thật và vị trí lưu

| Nội dung | Vị trí |
| --- | --- |
| Câu trả lời và quyết định của Ninh | `docs/requirements/`; phân biệt đã xác nhận, đề xuất, chưa rõ |
| Story, đặc tả, Q&A, thiết kế, test spec và bàn giao | `.scratch/<feature-slug>/`; tạo sau khi đủ yêu cầu để xác định một chức năng |
| Story ứng viên đã chia ở bước 8 | Planio `CM-01`–`CM-35`; [manifest](redmine-story-manifest.csv) nối ID với `.scratch/capmoney-overall/issues/<NN>-<slug>.md`; không tự coi là ticket Dev |
| Ticket triển khai sau hai gate | Issue cấp dưới trên Planio, dẫn tới `.scratch/<feature-slug>/issues/<NN>-<slug>.md` nếu cần file chi tiết; chỉ tạo khi PM chia sau `TECH_READY` |
| Mẫu file | `.scratch/_templates/`; copy rồi điền, không xem mẫu là bằng chứng hoàn thành |
| Review code, kết quả build và test | Đường dẫn commit/PR, lệnh, môi trường, log hoặc ảnh trong ticket/test spec |

Planio là tracker Ninh đã chọn và đã được [đối chiếu](redmine-migration.md). Dona trong workflow gốc không phải hệ thống CapMoney đang dùng; chưa có Notion, Figma hay staging đã duyệt. Không tạo ID trạng thái, đường dẫn, PR, kết quả test hoặc sự phê duyệt giả. Ảnh CapMoney trước đó chỉ là tài liệu tham khảo.

## Trạng thái và gate

Trạng thái công việc hiện hành ở Planio (`Open`, `In Progress`, `Feedback`, `Closed`). `Status:` trong các file Story nguồn là ảnh chụp lúc xuất bản. [Nhãn triage của skills](../agents/triage-labels.md) có thể ghi trong tài liệu chi tiết để điều phối, nhưng **không chứng minh** một gate đã đạt và không thay trạng thái Planio.

`Lifecycle:` của Story dùng `intake → specification → design → planning → implementing → verification → closed`. Blocker được ghi riêng bằng `Blocked by:` và người xử lý; chỉ công việc phụ thuộc vào blocker phải dừng. Nếu thay đổi yêu cầu, quay về giai đoạn và gate bị ảnh hưởng, cập nhật traceability trước khi tiếp tục.

| Gate | Cấp | Người lập và người kiểm | Điều kiện tối thiểu và bằng chứng | Mở khóa |
| --- | --- | --- | --- | --- |
| `BA_READY` | Story | CapmmBA lập, CapmmPM đối chiếu | Nguồn xác nhận của Ninh; mục tiêu/phạm vi, actor/quyền, luồng chính và ngoại lệ liên quan, BR kèm ví dụ, AC quan sát được, phụ thuộc và câu hỏi chặn đã xử lý; spec 20 mục có nội dung hoặc `Không áp dụng` kèm lý do; BA ghi `RESULT: BA_READY` và PM ghi nhận review | Tech thiết kế chức năng |
| `TECH_READY` | Story | CapmmTech thiết kế, reviewer khác người thiết kế kiểm kỹ thuật; PM kiểm phạm vi | Hợp đồng/ranh giới hệ thống, mô hình dữ liệu nếu cần, lỗi và an toàn dữ liệu, kiểm thử, phụ thuộc, rủi ro; thay đổi nền tảng có review riêng; quyết định được ghi trong `technical-design.md` | PM chia ticket |
| `READY_FOR_IMPLEMENTATION` | Từng ticket | CapmmPM chuẩn bị, Tech/BA rà phần liên quan | `BA_READY` và `TECH_READY` còn hiệu lực; ticket dọc có ≤5 mã AC tham chiếu, hợp đồng, dữ liệu test, dependency khởi động không chặn, rủi ro và người thực hiện; `Status: ready-for-agent` hoặc `ready-for-human` | Dev nhận chính ticket đó |
| `DEV_REVIEWED` | Từng ticket | CapmmDev thực hiện, reviewer khác người code kiểm PR/thay đổi; PM kiểm phạm vi | Code tích hợp, self review, kiểm tra liên quan có lệnh và kết quả, PR/commit hoặc diff; không có blocker tích hợp còn mở | Test execution trên bản chạy được |
| `QA_PASS` | Story | CapmmQA lập test spec; người kiểm thử độc lập chạy; PM đối chiếu | Mỗi AC trong phạm vi có case thực chạy `pass` trên build xác định; không còn case bắt buộc `not-run`, `blocked`, `fail`; lỗi đã sửa được retest, có log/ảnh và môi trường, UI trên thiết bị nếu thuộc phạm vi. Ngoại lệ cần Ninh xác nhận và ghi phạm vi | PM đóng Story |

Một `RESULT` chỉ là thông báo; gate có hiệu lực khi file đích và review có bằng chứng. `story.md` tổng hợp link gate của **tất cả ticket**; một ticket ready/reviewed không mở khóa các ticket khác. Không tự cho qua `TECH_READY` bằng `SKIP_TECH`; nếu tính năng thuần tài liệu không cần thiết kế kỹ thuật, PM ghi phạm vi và lý do riêng, không biến nó thành ticket code. `QA_PASS` không thể suy ra từ test spec hay code review.

## Vai trò và thứ tự chuyển giao

1. **CapmmBA:** tiếp nhận yêu cầu theo [11 bước](../agents/workflow.md); ghi Q&A, quyết định có nguồn, spec 20 mục và AC/BR; báo thiếu căn cứ. BA chỉ đề xuất `BA_READY` cho Story cụ thể khi đủ điều kiện.
2. **CapmmPM:** kiểm BA, ưu tiên Story, kiểm thay đổi phạm vi và giao Tech. PM không biến dữ liệu lịch sử thành quyết định mới.
3. **CapmmTech:** thiết kế và nêu rủi ro, yêu cầu review độc lập. Nếu đụng nền tảng chung như auth, đồng bộ, tiền hoặc lưu trữ, ghi quyết định kiến trúc trước khi nhận `TECH_READY`.
4. **CapmmPM:** sau hai gate, chia ticket theo lát cắt có thể trình diễn từ đầu đến cuối. Mỗi ticket tối đa 5 mã AC và chỉ dẫn mã đến spec, không chép lại nội dung AC. Có thể có subtasks data/backend/UI trong cùng lát cắt; chỉ xong khi đường đi tích hợp và test được. Ghi phụ thuộc `HARD`, `CONTRACT` hoặc `SOFT` và người nhận.
5. **CapmmDev:** nhận ticket đã ready, code và kiểm tra có bằng chứng, tự review; chuyển cho reviewer và PM. Phát hiện sai nghiệp vụ thì trả BA, sai thiết kế thì trả Tech, không tự điền quy tắc còn thiếu.
6. **CapmmPM:** kiểm review, phạm vi và bản chạy; chuẩn bị môi trường test phù hợp nền tảng đã chốt. Staging/deploy chỉ là bước có điều kiện khi dự án đã có hạ tầng đó.
7. **CapmmQA:** thiết kế test cases song song khi PM chia ticket sau `TECH_READY`, theo tám chiều: AC, BR, state transition, boundary, error/recovery, security/permission, integration, regression; chiều không liên quan ghi lý do. **Người kiểm thử độc lập**, hoặc CapmmTest nếu runner thực sự được cấu hình và có log chạy, thực thi trên bản build; QA tổng hợp kết quả, PM chỉ đóng sau `QA_PASS` và review. Repo hiện chưa có runner CapmmTest.

Đây là thứ tự trách nhiệm, không bắt buộc một người cho từng bot. Review độc lập phải ghi tên người/agent khác người tạo hiện vật; nếu chưa có reviewer thì giữ gate chờ. `implement-spec` có thể điều phối các ticket độc lập sau readiness, không bỏ qua điểm chặn.

## Blocker và thay đổi

| Vấn đề | Hành động tại ticket và nơi xử lý |
| --- | --- |
| Thiếu quy tắc, actor/quyền, copy UI hoặc câu trả lời của Ninh | `Status: needs-info`, câu hỏi trong `qna.md`, trả CapmmBA; chỉ Ninh xác nhận quyết định sản phẩm |
| Thiếu hợp đồng, thiết kế, quyết định nền tảng hoặc rủi ro an toàn dữ liệu | Ghi `Blocked by:` và trả CapmmTech; PM không phát ticket code phụ thuộc |
| Ticket quá 5 AC, phụ thuộc vòng, sai phạm vi hoặc thiếu người review | Trả CapmmPM để chia lại/điều phối; Tech xử lý cạnh phụ thuộc kỹ thuật |
| Thiếu test data, thiết bị, quyền truy cập hoặc bản build | CapmmQA nêu điều kiện; PM/Dev chuẩn bị. Không ghi pass khi chưa chạy |
| Bản chạy/deploy lỗi | PM giữ Story ở `verification` có blocker, Dev sửa rồi QA chạy lại |
| Nhu cầu mới ngoài phạm vi đã chốt | PM ghi đề xuất thay đổi và hỏi Ninh; chỉ cập nhật spec/gate sau quyết định |

`HARD` chặn khởi động phần phụ thuộc cho tới khi hoàn tất; `CONTRACT` cho phép bắt đầu với mock khi hợp đồng đã chốt nhưng chặn tích hợp/hoàn tất nếu bên cung cấp chưa sẵn sàng; `SOFT` chỉ là thứ tự ưu tiên. `Blocked by:` trên ticket liệt kê blocker `HARD` khi bắt đầu, `Integration blocked by:` liệt kê `CONTRACT` chưa giải. Không dừng các ticket độc lập. Khi blocker được giải quyết, BA/Tech/PM kiểm lại gate chịu ảnh hưởng.

## Nhật ký bàn giao năm trường

Mỗi cặp **actor + Story/ticket** có một mục tác nghiệp hiện hành trong `handoff.md`; mỗi lượt chỉnh mục đó, lịch sử có Git. Nhiều actor/ticket có các mục riêng trong cùng file, không ghi đè nhau. Mẫu ở `.scratch/_templates/handoff.md` dùng đúng các trường `DONE`, `RESULT`, `NEED HUMAN`, `PENDING`, `DETAIL` và dòng ký tên agent. `NEED HUMAN: None` khi không có việc cần Ninh/người kiểm; câu hỏi phải có mã, người cần trả lời và tác động. Dẫn file/commit/build thật, ghi rõ `chưa chạy` nếu chưa thử. Không sao chép các ID và status Dona vào Markdown.

## Ánh xạ workflow nguồn

| Trong bản Ninh gửi | Áp dụng tại CapMoney |
| --- | --- |
| Dona `Open (1)` + comment `BA_READY`; chỗ khác ghi `[Capmm BA] Ready (17)` | Chỉ dùng gate ngữ nghĩa `BA_READY` có bằng chứng và PM review; không dùng Dona ID |
| Notion Feature Spec 20 mục, Figma mapping | `spec.md` 20 mục; mapping hình/màn hình chỉ khi có thiết kế được Ninh chốt |
| Chia DATA/BE/FE task | Ticket lát cắt dọc, cho phép subtasks theo lớp trong ticket |
| QA ở sau staging trong sơ đồ, nhưng mục 9 thiết kế test sớm và không chạy test | QA lập test spec lúc chia ticket; người kiểm thử độc lập chạy sau khi có build (CapmmTest chỉ khi có runner thật); QA tổng hợp |
| Deploy staging, tài khoản test, dịch vụ đối tác và CR thương mại | Chỉ kích hoạt nếu CapMoney sau này chọn hạ tầng/phạm vi tương ứng |

Quy trình này không xác nhận bất kỳ tính năng, nền tảng hay kiến trúc nào. Với Story ứng viên chưa qua gate như [#07 – Thêm ví](../../.scratch/capmoney-overall/issues/07-them-vi.md), bắt đầu bằng BA lập `story.md`, Q&A và spec 20 mục từ các quyết định đã xác nhận, rồi PM kiểm gate. Công việc CM-01 đang mở phải theo [handoff](../../.scratch/capmoney-manager-setup/handoff.md) và phụ thuộc ticket riêng. Không tạo ticket Dev hoặc chuyển `ready-for-agent` chỉ vì đã có Story ứng viên.
