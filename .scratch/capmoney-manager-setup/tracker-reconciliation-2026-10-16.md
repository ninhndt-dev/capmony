# Đối chiếu Planio #44–#45 với bằng chứng CM-01

**Ngày:** 16/10/2026. **Phạm vi:** trạng thái ticket QA/gate; không đánh giá lại hành vi sản phẩm hoặc cấp `QA_PASS`.

## Phát hiện

- Planio #44 đang `Closed/100%` từ 15/10/2026 00:15:08 UTC. Mô tả được thêm câu “PM verified independent QA evidence” và ghi nguồn `d6824ae`; #45 được chuyển `In Progress/0%` và lặp lại câu đó ba giây sau.
- Commit `d6824ae` không có trong lịch sử Git hiện tại của repo hoặc các nhánh remote được liệt kê ngày 16/10/2026. Không thể gắn kết luận QA với một snapshot mã kiểm được bằng mã này.
- [Bằng chứng #44](qa44-evidence.md) ghi các ca T07–T12, phần setup T18–T19 đã chạy trên `059d868`, nhưng người chạy cũng là tác giả web #44. Báo cáo này nói rõ **chưa cấp `QA_PASS`** và cần QA khác người code. [Bằng chứng Dev #45](qa45-dev-evidence.md) cũng xác nhận chưa có QA độc lập cho #44/#45; sửa quyền secret Linux ở `4a4a1a4` không tự cấp gate.
- [Story](story.md), [ticket #45](issues/03-manager-login-session.md) và [handoff](handoff.md) cùng ghi `HARD` #44 → #45 chưa giải theo gate. Vì thế `Closed/100%` trên Planio #44 và câu xác nhận QA trong #44/#45 không có bằng chứng phù hợp trong Git.

## Cách điều chỉnh tracker

Khôi phục #44 về `In Progress/0%` (trạng thái và tiến độ trước journal đóng issue), giữ toàn bộ lịch sử Planio và thêm ghi chú đối chiếu dẫn tới file này. #45 giữ `In Progress/0%`, thêm ghi chú rằng mã đã có nhưng gate `HARD` chưa được PM giải; mô tả QA cũ là ảnh chụp không còn dùng để cấp gate. Sau cập nhật, đọc lại cả hai issue và ghi kết quả bên dưới.

## Gate sau đối chiếu

`QA_PASS` #44 **chưa đạt**; `READY_FOR_IMPLEMENTATION`, `DEV_REVIEWED`, `QA_PASS` #45 **chưa đạt**. QA khác người triển khai cần kiểm #44 trên build/commit pin, nhất là sửa quyền secret Linux; PM xét kết quả rồi mới giải `HARD`. Có thể tiếp tục tự kiểm, sửa lỗi và chuẩn bị bằng chứng #45, nhưng không gọi đó là QA độc lập.

## Kết quả cập nhật Planio

Sau khi file này được đẩy ở `a89ac4a`, Planio #44 đã được đưa về `In Progress/0%` và #45 giữ `In Progress/0%`. Mô tả cả hai issue có ghi chú đối chiếu dẫn đến file này; bản ghi `d6824ae` cũ được giữ trong lịch sử để truy vết. Cả hai issue được đọc lại sau khi ghi, đúng trạng thái và tiến độ trên. Không có gate QA nào được cấp từ thao tác tracker này.
