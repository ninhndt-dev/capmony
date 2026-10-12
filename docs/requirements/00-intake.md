# Phiếu tiếp nhận yêu cầu CapMoney

**Ngày lập:** 12/10/2026  
**Trạng thái:** Hỏi lại toàn bộ yêu cầu từ đầu; vòng 1 đã trả lời  
**Bước:** 1/11 – Thu thập yêu cầu  
**Chủ sản phẩm:** Ninh

## Nhu cầu Ninh đã nêu

Ninh yêu cầu bỏ toàn bộ code ứng dụng đã làm và dùng bộ skills của `mattpocock/skills` để khởi động lại tại bước tiếp nhận yêu cầu. Cách làm trước đó mong muốn cấu trúc dự án rõ ràng và các module nhỏ qua BA → PM → Tech → PM → Dev → PM → QA. Các yêu cầu sản phẩm cụ thể sẽ được hỏi lại; ảnh iPhone CapMoney chỉ để tham khảo.

Các câu dưới đây được Ninh xác nhận trong lần làm trước. **Ngày 12/10/2026 Ninh chọn hỏi lại tất cả**, vì vậy bảng này chỉ là lịch sử để đặt câu hỏi, chưa phải yêu cầu của lần khởi động mới.

| Mã | Câu trả lời trong lần trước | Trạng thái lần này |
| --- | --- | --- |
| D01 | Phiên bản đầu nhằm để Ninh dùng thật | Cần hỏi lại |
| D02 | Chỉ chủ tài khoản được xem và sửa dữ liệu của một ví | Cần hỏi lại |
| D03 | Ứng dụng điện thoại và REST API | Cần hỏi lại |
| D04 | Điện thoại iPhone/iOS | Cần hỏi lại |
| D05 | Nhập offline, lưu trên máy và đồng bộ sau | Cần hỏi lại |
| D06 | Giữ tám nhóm lõi; giao dịch định kỳ và mục tiêu tiết kiệm sau MVP | Cần hỏi lại |

Bản nháp trước diễn giải tám nhóm là **Tài khoản, Ví, Danh mục, Giao dịch thu/chi, Chuyển tiền, Ngân sách, Dashboard, Xuất Excel**. Danh sách này chỉ là bối cảnh, cần quyết định lại. Ảnh iPhone là nguồn cảm hứng thiết kế, không phải tiêu chí bắt buộc tái tạo bố cục/hành vi.

## Quyết định của lần khởi động mới

| Mã | Quyết định | Nguồn |
| --- | --- | --- |
| R01 | Hỏi lại toàn bộ yêu cầu, không giữ sáu câu trả lời cũ làm đầu vào đã chốt | Ninh trả lời vòng 1 ngày 12/10/2026 |
| R02 | Ảnh CapMoney chỉ dùng để tham khảo giao diện | Ninh trả lời vòng 1 ngày 12/10/2026 |
| R03 | Không nhập lịch sử từ app CapMoney hiện tại; bắt đầu với dữ liệu trống | Ninh trả lời vòng 1 ngày 12/10/2026 |

## Những điều chưa được Ninh duyệt riêng

- Các quy tắc số dư, chuyển tiền và phí, ngày giao dịch, tiền tệ, số dư âm, xóa/khôi phục.
- Cách đăng nhập, xử lý xung đột, phạm vi dùng nhiều thiết bị, bảo vệ và khôi phục dữ liệu.
- Hợp đồng API, mô hình dữ liệu, công nghệ, nơi lưu dữ liệu, cách xuất Excel.
- Thiết kế giao diện riêng, màn nào nằm trong MVP, tài sản ảnh và font.
- Kế hoạch phát hành, thứ tự ưu tiên module, tiêu chí nghiệm thu bằng số.

Không chuyển các mục này thành yêu cầu đã chốt chỉ vì chúng từng xuất hiện trong tài liệu hoặc code cũ.

## Việc đang chờ

Xem [vòng hỏi 1 đã trả lời](01-interview-round-1.md) và [vòng hỏi 2](02-interview-round-2.md). BA cập nhật phiếu này sau từng câu trả lời. PM review trước khi chuyển sang mục tiêu/phạm vi của bước 2.
