# Bước 8, vòng làm rõ 2: lấy lại tài khoản, xóa tài khoản và lần lưu chưa rõ

**Trạng thái:** Ninh trả lời “A hết” cho Q1–Q3 ngày 13/10/2026; các quyết định R62–R64 đã được ghi vào phiếu tiếp nhận. Cách chia 35 ticket đã được Ninh duyệt.

**Nguồn:** [Đặc tả tổng thể](../../.scratch/capmoney-overall/spec.md), [luồng người dùng](user-flows.md), [bản chia ticket](35-step-8-ticket-breakdown.md).

❓ **Q1 – Xác minh người mất email cũ:** Khi người dùng đã đăng xuất và không dùng được email cũ, Ninh dựa vào cách nào để nhận đúng người trước khi đổi email đăng nhập cho họ?

➡️ **A (Bờm đề xuất):** Chỉ hỗ trợ người Ninh đã quen và nhận diện được. Ninh gọi lại số điện thoại đã lưu từ trước **ở ngoài CapMoney**, nhận diện người đó qua cuộc gọi rồi xác nhận yêu cầu đổi email. Nếu không có số đã biết trước hoặc không chắc đúng người, chưa đổi. **B:** Ninh gặp trực tiếp người đã quen và nhận diện được để xác nhận yêu cầu; nếu không gặp hoặc không chắc đúng người, chưa đổi. Cả hai cách đều phải xác minh email mới trước khi đổi.

---

❓ **Q2 – Phiên trên điện thoại khác sau khi xóa tài khoản:** Tài khoản và dữ liệu đã chốt xóa ngay sau xác nhận cuối; phiên trên điện thoại khác không được đọc/sửa nữa. Ứng dụng nên báo cho điện thoại đó khi nào?

➡️ **A (Bờm đề xuất):** Ở lần điện thoại đó tải lại, quay lại ứng dụng hoặc thao tác tiếp theo, báo tài khoản đã bị xóa và đưa về đăng nhập; từ thời điểm xóa, mọi yêu cầu đọc/sửa đều bị từ chối. **B:** Báo và đưa về đăng nhập ngay cả khi điện thoại đó đang mở ứng dụng, không đợi thao tác tiếp theo; mọi yêu cầu đọc/sửa vẫn bị từ chối ngay từ thời điểm xóa.

---

❓ **Q3 – Khi kết quả Lưu giao dịch còn “chưa rõ”:** Trong lúc ứng dụng đang tự đối chiếu lần lưu và chặn thử lại cùng thao tác, người dùng có thể tiếp tục việc khác trong CapMoney không?

➡️ **A (Bờm đề xuất):** Có thể rời biểu mẫu và làm việc khác khi còn Internet; trạng thái “đang kiểm tra” vẫn thấy khi quay lại hoặc mở lại, và không cho lưu lặp đúng thao tác đang chờ. **B:** Giữ người dùng ở màn chờ, chưa cho làm thao tác tài chính khác tới khi biết kết quả; mở lại vẫn tự đối chiếu.

## Nhật ký trả lời

Ninh trả lời **“A hết”**: chọn A cho Q1 (xác minh qua cuộc gọi lại số đã biết ngoài CapMoney khi Ninh nhận diện chắc người quen), Q2 (điện thoại khác báo khi tải lại/quay lại/thao tác tiếp; quyền bị thu hồi ngay) và Q3 (có thể làm việc khác khi có Internet, vẫn theo dõi lần lưu chưa rõ và chặn lưu lặp đúng thao tác). Theo F09/F25, khi quay lại mà đối chiếu đã xong thì hiện kết quả thay cho trạng thái chờ. Nguồn quyết định: R62–R64 trong [phiếu tiếp nhận](00-intake.md). Chi tiết màn hình và cách thực hiện kỹ thuật vẫn để bước 9.
