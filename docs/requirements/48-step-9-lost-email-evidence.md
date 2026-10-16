# Bước 9, vòng làm rõ #14: bằng chứng hỗ trợ người mất email

**Trạng thái:** Ninh chọn phương án lưu người xác nhận, thời điểm và tài khoản được hỗ trợ trên giao diện ngày 16/10/2026. Ghi thành R79 cho Story CM-26.

Sau khi Ninh gọi lại số đã lưu từ trước **ngoài CapMoney**, nhận diện chắc đúng người và xác nhận chính người đó yêu cầu đổi email, Ninh xác nhận đã thực hiện các điều kiện này trong luồng hỗ trợ. CapMoney lưu **người xác nhận, thời điểm và tài khoản được hỗ trợ** để truy vết yêu cầu hỗ trợ và lần đổi email nếu thành công. Email mới vẫn phải được xác minh trước khi đổi có hiệu lực theo R62.

**Ví dụ:** Ninh hỗ trợ tài khoản A, đã gọi lại số Ninh biết từ trước và chắc đúng A. Ngay khi Ninh xác nhận trong ứng dụng, bản ghi hỗ trợ cho thấy tài khoản A, Ninh là người xác nhận và thời điểm xác nhận. Quản lý không vì vậy có quyền xem ví hay giao dịch của A.

R79 không yêu cầu đưa số điện thoại vào CapMoney, ghi âm cuộc gọi hoặc bắt Ninh nhập ghi chú nhận diện. Xác nhận trên ứng dụng ghi lại lời xác nhận của Ninh; bản thân nó không tự chứng minh cuộc gọi ngoài ứng dụng đã diễn ra. Cách lưu trữ, chống sửa bản ghi và thời hạn giữ là thiết kế Tech/PM cần rà trước `TECH_READY`.
