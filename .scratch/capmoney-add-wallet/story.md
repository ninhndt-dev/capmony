# Story 07: Thêm ví

**Lifecycle:** design
**Status:** needs-triage
**Blocked by:** Story #06 – Tạo ví đầu tiên (`HARD` cho Dev/kiểm chứng tích hợp #07); #04 – phiên ghi chép/quyền là tiền đề tích hợp qua #06. Các gap kỹ thuật của #07 ghi riêng dưới `TECH_READY`; không còn câu hỏi sản phẩm chặn BA.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** người dùng có nhiều ví riêng (R12, R15); ví thêm có tên bắt buộc (R66), số dư ban đầu tùy chọn, bỏ trống dùng 0 VND, khi nhập là số nguyên VND có thể âm (R23, B10, B15, H04). Số dư ban đầu là mốc, không tạo bản ghi khoản thu (R30, B01). Rời biểu mẫu ví thêm **trước Lưu** rồi quay lại cùng phiên thì biểu mẫu trống (R68); đóng trình duyệt trước Lưu cũng không khôi phục bản nhập dở (F21). Đã bấm Lưu ví thêm rồi đóng khi kết quả chưa rõ thì mở lại bằng cùng tài khoản có Internet tự đối chiếu đúng thao tác, chỉ báo kết quả khi chắc và chặn lưu trùng lúc còn chưa rõ (R69). Xem [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [vòng trả lời #07](../../docs/requirements/41-step-9-add-wallet-round-1.md), [nghiệp vụ chính](../../docs/requirements/core-business.md), [luồng người dùng](../../docs/requirements/user-flows.md) và [danh mục chức năng](../../docs/requirements/system-functions.md).
- **Phạm vi Story:** tạo ví thứ hai trở đi, xem lại danh sách và số dư riêng của các ví đã lưu. [Đặc tả 20 mục](spec.md) ghi AC và các nhánh cần quyết định.
- **Ngoài phạm vi:** tạo ví đầu tiên (#06), điều hướng từ luồng ghi chép trở về sau khi tạo ví, ghi giao dịch, sửa tên/số dư (#16), ngừng/bật lại hoặc xóa ví (#18–#19); không nhập dữ liệu app cũ.
- **Đã trả lời:** Q1=B và Q2=A, ghi thành R68–R69; [Q&A](qna.md) có nguồn. Các chi tiết tên trùng, giới hạn độ dài, thông báo và kỹ thuật đối chiếu chưa được Ninh chốt riêng; chúng không thay đổi hành vi R68–R69.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| BA_READY | **Đạt ngày 14/10/2026**: CapmmBA đề xuất, CapmmPM đã đối chiếu đủ 20 mục, BR có ví dụ, AC1–AC8 và nguồn R68–R69; không còn câu hỏi sản phẩm chặn | [spec](spec.md), [Q&A](qna.md), [handoff](handoff.md), [vòng trả lời](../../docs/requirements/41-step-9-add-wallet-round-1.md) | CapmmPM: đã rà bản sau R68–R69 và ví dụ BR |
| TECH_READY | **`STORY_TECH_GAP`**, chưa đạt: thiếu bằng chứng về kết thúc “chắc chưa lưu” trước request tới muộn, ghi một lần atomic/idempotent và nền tảng chung #04/#06; các điểm P1 cũng cần giải | [technical design và kết luận review độc lập](technical-design.md), [handoff](handoff.md) | `/root/tech_independent` đã review kỹ thuật độc lập; CapmmPM đã rà phạm vi, chưa mở gate |
| READY_FOR_IMPLEMENTATION | chưa đánh giá cho từng ticket | chưa tạo ticket Dev | chưa có |
| DEV_REVIEWED | chưa chạy | chưa có code, commit/PR hoặc kết quả kiểm tra | chưa có |
| QA_PASS | chưa chạy | chưa có build, test spec hoặc log thực thi | chưa có |

## Phụ thuộc và việc tiếp theo

- **Sản phẩm:** Q1–Q2 đã có nguồn R68–R69; CapmmPM đã kiểm độc lập và công nhận `BA_READY`. Vị trí UI, tên trùng, giới hạn và câu chữ vẫn là đề xuất hoặc chi tiết thiết kế, không thành quyết định của Ninh.
- **Story #06 → #07: `HARD` cho khởi động Dev và kiểm chứng tích hợp #07:** #07 cần một ví đã lưu cùng khả năng tạo/đọc ví từ #06; #06 còn `needs-triage` và chưa có hợp đồng kỹ thuật được duyệt. Phụ thuộc này không chặn `BA_READY` hoặc việc Tech thiết kế #07. Chỉ xét đổi phần việc độc lập sang `CONTRACT` nếu hợp đồng #06 được chốt và Tech/PM rà lại; demo tích hợp vẫn cần #06 chạy thật. #04 cung cấp phiên/quyền cho #06 và #07, hiện cũng chưa có hợp đồng được duyệt; việc Tech khảo sát #07 lúc chờ #04 không mở khóa Dev.
- **F20 / Story #18:** là bối cảnh tích hợp đầu ra của #07, không phải blocker ngược hay AC của #07. Điều hướng khi mọi ví đã ngừng và quay về ghi chép thuộc #18; phụ thuộc #07 nằm gián tiếp qua các Story #08 và #10 trong kế hoạch hiện có.
- **Được phép tiếp theo:** CapmmTech xử lý P0/P1 trong [technical design](technical-design.md), làm spike/quyết định nền tảng chung #04/#06 rồi xin review kỹ thuật độc lập lại; PM rà phạm vi sau đó. Chưa đạt `TECH_READY`, chưa tạo ticket Dev hoặc đánh dấu `ready-for-agent`.
