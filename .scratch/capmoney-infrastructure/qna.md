# Q&A INFRA-01

| Mã | Nội dung | Trạng thái và nguồn |
| --- | --- | --- |
| IF-Q1 | Web/API một ứng dụng hay hai dịch vụ? | **Ninh đã xác nhận TS01/B:** React web và API riêng. |
| IF-Q2 | Docker cho dev/test hay chạy thật? | **Ninh đã xác nhận TS06:** dev/test dùng Docker, chạy thật dùng VPS riêng. |
| IF-Q3 | Hạ tầng trước FE/BE ở mức nào? | **Ninh đã xác nhận TS07/A:** sơ đồ/ADR/môi trường/thứ tự, review xong mới dựng Docker. |
| IF-Q4 | DB production ở đâu? | **Ninh đã xác nhận TS08/A:** PostgreSQL trên VPS riêng với volume. |
| IF-Q5 | Có tiếp tục theo bundle ADR 0002? | **Ninh trả lời “continue” sau câu hỏi duyệt A/B**, ghi TS09: tiếp tục theo phương án A và chuyển sang ticket Docker dev/test. |

## Chưa rõ không chặn dev/test

- VPS/domain/SMTP thật và chi phí vận hành; không cần để chạy dữ liệu thử local.
- Địa chỉ IP tĩnh của Caddy trong Docker network và cách API xác thực peer là chi tiết Tech phải chứng minh trong ticket, không tự coi đã chạy.
