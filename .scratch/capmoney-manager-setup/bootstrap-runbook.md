# Vận hành CLI thiết lập quản lý đầu tiên

Phạm vi #43, dữ liệu thử/Mailpit trước. Không có API gửi link hay form chọn email quản lý. Không gửi email thật ở vòng dev/test. Không đặt secret/token vào chat, Git, command arguments hoặc log.

## Chạy

Trong môi trường API đã cấu hình JDBC/secrets/SMTP hiện hành, đặt `BOOTSTRAP_ENABLED=true`, `MANAGER_EMAIL=manager@example.test` và `APP_ORIGIN` đúng origin web thử. Dùng cùng `API_SUBNET`/`EDGE_SECRET_FILE` hạ tầng; không tạo edge secret qua biến khác. Chạy một tiến trình nội bộ riêng từ image/jar đã build:

```sh
java -jar /app/app.jar --capmoney.bootstrap-manager
```

Ví dụ cụ thể cho Compose dev **sau** `./infra/scripts/stack.sh dev up` từ gốc repo:

```sh
docker exec -u 10001 -e BOOTSTRAP_ENABLED=true -e MANAGER_EMAIL=manager@example.test capmoney-dev-api-1 java -jar /app/app.jar --capmoney.bootstrap-manager
```

Lệnh dùng cùng cấu hình, secret và mạng của API container nhưng chạy JVM riêng; không thay đổi API đang phục vụ HTTP. Email đi vào Mailpit nội bộ và không được CLI in ra. [Runbook Compose](../../infra/README.md) có lệnh `docker exec ... wget` để đọc danh sách/thư Mailpit qua API nội bộ; giữ link/token chỉ trong terminal riêng, không đưa vào Docker logs, chat hoặc Git. Môi trường test dùng container `capmoney-test-api-1` với dữ liệu thử riêng. Không dùng lệnh này với email thật trước khi cấu hình SMTP và origin production được kiểm.

Lệnh chạy Spring không web, không mở HTTP listener. Chỉ in mã kết quả và generation, không in link/email/token. Không bật debug SMTP hoặc log tham số DB ở môi trường thật.

| Kết quả | Exit | Ý nghĩa / xử lý |
| --- | --- | --- |
| accepted | 0 | Nhà cung cấp SMTP chấp nhận và DB xác nhận link issued. Kiểm Mailpit riêng để chứng minh thư tới hộp. |
| failed | 2 | Gửi bị từ chối rõ ràng; không có link hiệu lực. Có thể chạy lại để tạo generation mới. |
| unknown | 3 | Không chắc gửi/ghi DB; không báo thành công. Kiểm state/generation trong DB, không phát lại callback cũ. Retry CLI tăng generation trước gửi mới. |
| unavailable | 2 | Đã có manager/link issued, email khác cấu hình hoặc gửi đang diễn ra. Không đổi email DB hay sửa state thủ công. |
| disabled / invalid_configuration | 2 | Chưa bật lệnh hoặc cấu hình không hợp lệ; sửa cấu hình rồi chạy lại. |

`Sending` còn trong lease30giây không cho tiến trình khác phát hành. Sau lease, CLI mới có thể fencing generation cũ; callback cũ không phát hành lại. `issued` kể cả hết hạn không được tự cấp lại trong #43: luồng xin lại link thuộc #02. Không xóa row/volume hoặc reset schema để thử lại; tạo môi trường thử mới và giữ môi trường cũ.

Mốc72giờ tính từ lúc JavaMailSender trả accepted, giữ nguyên qua CAS; không tính lại từ lúc ghi DB chậm. Lệch đồng hồ/round-trip không đáng tin làm unknown, fail closed. Link dùng fragment nên không đi qua GET; mở email vào `/manager/setup`, trang xóa fragment ngay và chỉ giữ trong memory. Reload phải mở lại email. Hoàn tất tài khoản thuộc#44; đăng nhập/phiên thuộc#45.
