# Quy trình 11 bước và chuỗi agent

**Áp dụng từ sau ticket #06:** Ninh đã bổ sung [workflow CapMM end to end](../workflows/capmm-end-to-end.md) cho các Story tiếp theo. 11 bước trong file này vẫn là lộ trình sản phẩm; workflow mới thêm các gate có bằng chứng cho từng Story và ticket triển khai. 35 Story ứng viên đang ở [Planio](https://tanninh.planio.com/projects/capmoney/issues), với file nguồn trong Git; việc chuyển không tự cấp `BA_READY` hoặc `TECH_READY`. Từ #07, BA → PM kiểm spec 20 mục trước khi Tech thiết kế; PM chỉ chia ticket Dev sau hai gate, và chỉ giao code khi từng ticket đạt `READY_FOR_IMPLEMENTATION`.

**Rebaseline Java:** Sau TS10–TS38, gate theo thiết kế Node/Hono chỉ còn là lịch sử. INFRA #47/#48 và CM-01 #43 đã được rà/QA theo Java; #44 đang chờ QA độc lập, #45 đã có code/bằng chứng Dev nhưng còn gate #44, code review và QA. Xem [quyết định stack](../requirements/tech-stack-round-2.md), [tổng quan code](../project-overview.md) và [handoff](../../.scratch/capmoney-manager-setup/handoff.md). Quy trình dưới đây vẫn là điều kiện để cấp các gate còn lại.

Quy trình này áp dụng cho lần khởi động lại. **Hiện tại: phần đặc tả chức năng bước 9 đã hoàn tất sau R79; 35/35 Story có spec, CM-10–CM-35 có 26/26 `BA_READY` theo [PM review](../../.scratch/capmoney-overall/step9-ba-review-2026-10-16.md). #01–#06 đã qua lượt BA → PM → Tech → PM theo quy trình trước workflow CapMM; rebaseline Java cần được xem theo từng Story. `TECH_READY` và triển khai còn xét riêng theo gate.** Bước 1 đã có phiếu tiếp nhận và mười vòng hỏi đáp; bước 2 đã có [mục tiêu và phạm vi](../requirements/goal-and-scope.md), được BA/PM rà soát và Ninh xác nhận ngày 12/10/2026; bước 3 đã có [người dùng và quyền](../requirements/actors-and-roles.md), được BA/PM rà soát và Ninh xác nhận chuyển tiếp; bước 4 đã có [nghiệp vụ chính](../requirements/core-business.md), được BA/PM rà soát và Ninh xác nhận chuyển tiếp; bước 5 đã có [luồng người dùng](../requirements/user-flows.md), được BA/Tech/PM rà soát và Ninh xác nhận chuyển tiếp; bước 6 đã có [danh mục chức năng và ưu tiên](../requirements/system-functions.md), được PM rà soát và Ninh chọn A xác nhận chuyển tiếp; bước 7 đã có [đặc tả tổng thể](../../.scratch/capmoney-overall/spec.md), được PM → Tech rà soát và Ninh chọn A/A xác nhận nội dung, điểm kiểm thử chính; bước 8 đã có [35 ticket dọc](../../.scratch/capmoney-overall/issues/) theo cách chia Ninh duyệt. `needs-triage` trong file nguồn bước 8 là ảnh chụp lịch sử; trạng thái hiện tại nằm trên Planio. Không đánh dấu bước sau đã hoàn tất từ bản code hoặc tài liệu cũ.

| Bước Ninh đưa ra | Đầu ra | Skill hỗ trợ | Vai trò kiểm tra |
| --- | --- | --- | --- |
| 1. Thu thập yêu cầu | Phiếu tiếp nhận, nhật ký câu trả lời, câu hỏi còn mở | `grill-with-docs`, `grilling`, `domain-modeling` | BA → PM |
| 2. Mục tiêu và phạm vi | Mục tiêu, phạm vi bản đầu, tiêu chí thành công | `grill-with-docs` | BA → PM |
| 3. Actor / Role | Người dùng, quyền và trường hợp ngoại lệ đã xác nhận | `domain-modeling` | BA → PM |
| 4. Nghiệp vụ chính | Các quy tắc đã xác nhận và ví dụ biên | `grill-with-docs` | BA → PM |
| 5. Business / User Flow | Các luồng dùng chính, lỗi và phục hồi theo phạm vi đã chốt | `prototype` khi cần | BA → Tech → PM |
| 6. Chức năng hệ thống | Danh mục chức năng và mức ưu tiên | `to-spec` | PM |
| 7. Đặc tả tổng thể | Spec có user stories, phạm vi, điểm kiểm thử | `to-spec` | PM → Tech |
| 8. Tách chức năng | Ticket dọc, có phụ thuộc và demo riêng | `to-tickets` | PM → Tech |
| 9. Đặc tả chi tiết | Tiêu chí nghiệm thu từng module | `to-spec`, `to-tickets` | BA → PM → Tech |
| 10. PM/Tester review | Đối chiếu phạm vi, hành vi, rủi ro | `code-review`, `tdd` khi đã có code | PM → QA |
| 11. Chốt tài liệu | Tình trạng quyết định và version rõ ràng | `retro` sau vòng triển khai | Ninh/PM |

## Triển khai từng module

BA diễn đạt nhu cầu → PM xác định phạm vi → Tech đề xuất hợp đồng và cách kiểm thử → PM kiểm tra lại → Dev code một ticket đủ nhỏ → PM rà chức năng → QA thử theo tiêu chí nghiệm thu. `implement` phù hợp cho một ticket; `implement-spec` có thể phối hợp nhiều agent khi đã có ticket và cạnh phụ thuộc. Agent không tự biến lựa chọn chưa được Ninh xác nhận thành “đã chốt”.

Đầu ra QA cần ghi rõ bằng chứng build và chạy trên nền tảng Ninh chọn, kiểm thử API/đồng bộ/quyền truy cập nếu chúng thuộc phạm vi được chốt, và duyệt UI trên thiết bị. Không thay bằng việc chỉ đọc code hoặc đếm dấu ngoặc.
