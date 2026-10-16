# Story 13: Chuyển tiền giữa hai ví

**Lifecycle:** design
**Status nguồn:** `needs-triage` tại [issue bước 8](../capmoney-overall/issues/13-chuyen-tien-giua-vi.md); trạng thái live phải đối chiếu Planio.  
**Blocked by:** #07 cấp ví thứ hai; #10 cấp lịch sử để kiểm một dòng. Phụ thuộc Dev/tích hợp, BA có thể soạn độc lập.

## Phạm vi và nguồn

- **Ninh đã xác nhận:** Là chủ tài khoản, tôi chuyển VND giữa hai ví đang dùng của mình bằng một thao tác, phí 0, để cả hai số dư và lịch sử luôn nhất quán. Mã nguồn: `R24, R42, R43, R51, R60; B01–B02, B04, B09, B12, B23, B25; F17`; đối chiếu [issue](../capmoney-overall/issues/13-chuyen-tien-giua-vi.md), [quy tắc nghiệp vụ](../../docs/requirements/core-business.md), [luồng](../../docs/requirements/user-flows.md) và [intake](../../docs/requirements/00-intake.md).
- **Ngoài phạm vi:** Sửa lần chuyển #14; xóa/khôi phục #15; lưu đồng thời #29; mất mạng/đối chiếu #34/#35; ví ngừng #17/#18. Không suy mặc định ví/chế độ form từ R70–R73.
- **Câu hỏi còn mở:** Ninh đã chốt R74 về rời form trước Lưu trong [Q&A](qna.md); R75 đã chốt hai ví ban đầu trống ở điểm vào chung. Điểm vào từ một ví cụ thể và bố cục là chi tiết cần PM/UX phân loại; không suy từ F14/R70–R73.

## Gate và bằng chứng

| Gate | Kết quả hiện tại | Bằng chứng | Review |
| --- | --- | --- | --- |
| `BA_READY` | **Đạt 16/10/2026**: CapmmPM `/root` rà bản sau quyết định/chỉnh nguồn, 20 mục, BR/AC và ranh giới; không còn câu hỏi sản phẩm chặn trong AC | [spec](spec.md), [Q&A](qna.md), [PM review](../capmoney-overall/step9-ba-review-2026-10-16.md), [handoff](handoff.md) | CapmmPM `/root` |
| `TECH_READY` | Chưa đánh giá; Tech thiết kế sau BA gate | Chưa có `technical-design.md` | Chưa có |
| `READY_FOR_IMPLEMENTATION` | Chưa tạo ticket Dev | Theo [workflow](../../docs/workflows/capmm-end-to-end.md) | Chưa có |
| `DEV_REVIEWED` / `QA_PASS` | Chưa chạy | Chưa có build/test spec cho Story này | Chưa có |

## Phụ thuộc và việc tiếp theo

#07 cấp ví thứ hai; #10 cấp lịch sử để kiểm một dòng. Phụ thuộc Dev/tích hợp, BA có thể soạn độc lập. BA/PM có thể làm rõ spec khi phụ thuộc Dev chưa hoàn thành. CapmmPM đã rà BR/AC và ranh giới cho `BA_READY`; CapmmTech lập thiết kế/hợp đồng, reviewer kỹ thuật khác người lập rà, PM kiểm phạm vi trước `TECH_READY`. Chưa chia ticket Dev; trạng thái issue live phải đối chiếu Planio.
