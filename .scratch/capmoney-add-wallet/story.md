# Story 07: Thêm ví

**Lifecycle:** specification
**Status:** needs-info
**Blocked by:** Story #06 – Tạo ví đầu tiên (phụ thuộc triển khai trong [Story ứng viên #07](../capmoney-overall/issues/07-them-vi.md)); hai câu hỏi sản phẩm ở [Q&A](qna.md) chặn `BA_READY`.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** người dùng có nhiều ví riêng (R12, R15); ví thêm có tên bắt buộc (R66), số dư ban đầu tùy chọn, bỏ trống dùng 0 VND, khi nhập là số nguyên VND có thể âm (R23, B10, B15, H04). Số dư ban đầu là mốc, không tạo bản ghi khoản thu (R30, B01). Đóng trình duyệt **trước khi bấm Lưu** thì không khôi phục bản nhập dở (F21). Xem [phiếu tiếp nhận](../../docs/requirements/00-intake.md), [nghiệp vụ chính](../../docs/requirements/core-business.md), [luồng người dùng](../../docs/requirements/user-flows.md) và [danh mục chức năng](../../docs/requirements/system-functions.md).
- **Phạm vi Story:** tạo ví thứ hai trở đi, xem lại danh sách và số dư riêng của các ví đã lưu. [Đặc tả 20 mục](spec.md) ghi AC và các nhánh cần quyết định.
- **Ngoài phạm vi:** tạo ví đầu tiên (#06), điều hướng từ luồng ghi chép trở về sau khi tạo ví, ghi giao dịch, sửa tên/số dư (#16), ngừng/bật lại hoặc xóa ví (#18–#19); không nhập dữ liệu app cũ.
- **Chưa rõ, chặn BA_READY:** giữ bản nháp khi rời trang trước Lưu và cách xử lý lần Lưu ví thêm chưa rõ kết quả sau khi đóng trình duyệt. Hai câu đã gửi Ninh qua giao diện nhưng chưa có câu trả lời trong repo; xem [Q&A](qna.md).

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Reviewer khác người lập |
| --- | --- | --- | --- |
| BA_READY | **chưa đạt**: Q1–Q2 còn chờ Ninh; PM đã rà phần độc lập và BA đã sửa ba ranh giới | [spec](spec.md), [Q&A](qna.md), [handoff](handoff.md) | CapmmPM: đã rà phần độc lập; chưa xác nhận gate |
| TECH_READY | chưa đánh giá | `technical-design.md` chưa tạo | chưa có |
| READY_FOR_IMPLEMENTATION | chưa đánh giá cho từng ticket | chưa tạo ticket Dev | chưa có |
| DEV_REVIEWED | chưa chạy | chưa có code, commit/PR hoặc kết quả kiểm tra | chưa có |
| QA_PASS | chưa chạy | chưa có build, test spec hoặc log thực thi | chưa có |

## Phụ thuộc và việc tiếp theo

- **Sản phẩm:** Ninh trả lời Q1–Q2; CapmmBA cập nhật Q&A/spec, rồi CapmmPM kiểm độc lập trước khi xét `BA_READY`.
- **Story #06:** là tiền đề tạo/đọc ví trong tracker hiện có. PM xác định loại phụ thuộc `HARD`/`CONTRACT`/`SOFT` trước lập kế hoạch; không tự coi #06 đã qua gate mới.
- **Được phép tiếp theo:** hoàn thiện phần BA độc lập với hai câu hỏi và xin Ninh trả lời. Không lập thiết kế kỹ thuật, ticket Dev hoặc đánh dấu `ready-for-agent` từ bản nháp này.
