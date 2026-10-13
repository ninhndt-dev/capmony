# Bước 5, vòng 5: tiếp tục bản nhập dở và khôi phục truy cập

**Trạng thái:** Đã trả lời ngày 13/10/2026. BA, Tech và PM đã rà soát trước khi hỏi; Ninh chọn B, A, B, B cho Q1–Q4.

**Phụ thuộc đã chốt:** [Phiếu luồng người dùng](user-flows.md), [vòng 4 bước 5](29-step-5-user-flows-round-4.md), [nghiệp vụ chính](core-business.md).

❓ **Q1 – Đóng trình duyệt khi chưa lưu:** Người dùng đang nhập ví hoặc giao dịch, đóng hẳn trình duyệt trước khi bấm lưu rồi mở lại trên cùng điện thoại, bằng cùng tài khoản, khi có Internet. Nội dung nhập dở nên thế nào?

➡️ **A (Bờm đề xuất):** Khôi phục bản nhập dở chỉ cho đúng tài khoản trên điện thoại đó để tiếp tục hoặc tự bỏ; tài khoản mới được mời lại cùng email không thấy bản nháp cũ. Bản nháp chưa phải dữ liệu đã lưu và không dùng ứng dụng khi mất Internet. **B:** Không khôi phục sau khi đóng trình duyệt; mở biểu mẫu trống, nhưng những dữ liệu đã lưu trước đó vẫn còn.

---

❓ **Q2 – Bản giao dịch đã đổi trên điện thoại khác:** Khi người dùng bấm lưu thay đổi từ bản cũ và hệ thống chặn theo quy tắc B30, phần họ vừa nhập nhưng chưa lưu sẽ thế nào?

➡️ **A (Bờm đề xuất):** Giữ phần vừa nhập để họ so sánh, tự nhập lại trên bản mới nhất rồi bấm lưu, hoặc bỏ; không tự hợp nhất hay ghi đè bản mới. **B:** Báo dữ liệu đã thay đổi, yêu cầu xác nhận bỏ phần vừa nhập rồi tải bản mới nhất; không cho lưu lại bản cũ.

---

❓ **Q3 – Hạn liên kết thiết lập tài khoản quản lý đầu tiên:** Liên kết thiết lập chỉ dùng một lần gửi tới email quản lý Ninh đã cung cấp nên hết hạn sau bao lâu kể từ lúc gửi? Đây là liên kết khác lời mời người dùng có hạn 3 ngày.

➡️ **A (Bờm đề xuất):** 24 giờ. **B:** 3 ngày.

---

❓ **Q4 – Liên kết thiết lập hết hạn trước khi hoàn tất:** Khi liên kết thiết lập tài khoản quản lý đầu tiên hết hạn trước khi Ninh hoàn tất thiết lập, Ninh lấy liên kết mới bằng cách nào? Sau khi thiết lập xong, luồng này không cấp lại tài khoản quản lý.

➡️ **A (Bờm đề xuất):** Liên hệ người triển khai để gửi liên kết mới chỉ tới email quản lý đã cung cấp; liên kết cũ hết hiệu lực. **B:** Tự yêu cầu gửi lại trên trang thiết lập; liên kết mới chỉ tới email quản lý đã cung cấp, không cho nhập email khác hay tự đăng ký quản lý; liên kết cũ hết hiệu lực.

## Nhật ký trả lời

Ninh trả lời bốn dòng **B, A, B, B** theo thứ tự Q1–Q4. Các lựa chọn này đã được chuyển vào [phiếu luồng người dùng](user-flows.md).

| Câu | Ninh đã xác nhận | Chưa rõ / phụ thuộc |
| --- | --- | --- |
| Q1 | Khi đóng hẳn trình duyệt trước khi lưu ví hoặc giao dịch, mở lại trên cùng điện thoại thì không khôi phục bản nhập dở; biểu mẫu mở trống. Dữ liệu đã lưu trước đó vẫn còn. | Bản nhập dở trong cùng phiên vẫn theo F08, F18. |
| Q2 | Khi B30 chặn lưu từ bản giao dịch cũ, giữ phần vừa nhập để người dùng tự so sánh, nhập lại trên bản mới nhất rồi lưu hoặc bỏ; không tự hợp nhất hay ghi đè bản mới. | Cách trình bày việc so sánh thuộc thiết kế sau. |
| Q3 | Liên kết thiết lập tài khoản quản lý đầu tiên chỉ dùng một lần, hết hạn sau 3 ngày kể từ lúc gửi. | Hạn này độc lập với lời mời người dùng 3 ngày và đặt lại mật khẩu 30 phút. |
| Q4 | Nếu liên kết thiết lập hết hạn trước khi hoàn tất, Ninh có thể tự yêu cầu gửi lại trên trang thiết lập; liên kết mới chỉ gửi tới email quản lý đã cung cấp, không cho nhập email khác hay tự đăng ký quản lý; liên kết cũ hết hiệu lực. Khi thiết lập xong, luồng này không cấp lại tài khoản quản lý. | Cách báo lỗi khi yêu cầu lặp và thời gian chờ gửi lại thuộc bước chi tiết. |

## Phụ thuộc cho bước tổng hợp

- Tổng hợp các luồng tạo/sửa ví, thu/chi, chuyển tiền, xóa/khôi phục, đăng nhập và phục hồi lỗi từ quyết định đã chốt; không hỏi lại quy tắc nghiệp vụ bước 4.
- Giữ rõ các chi tiết chưa xác nhận như cách hiện lần chuyển trong lịch sử từng ví, thông báo khi liên kết hoặc phiên hết hạn, trạng thái bản nháp giữa hai thiết bị, bằng chứng xác minh người mất email và thứ tự giao dịch cùng ngày.
- Sau khi BA → Tech → PM rà soát bản tổng hợp, đưa Ninh xác nhận hiểu đúng trước khi chuyển sang bước 6.
