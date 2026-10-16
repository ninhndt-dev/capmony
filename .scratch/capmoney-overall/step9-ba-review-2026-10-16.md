# Rà soát BA bước 9 – CM-10 đến CM-35

**Người rà:** CapmmPM `/root`, khác các agent đã lập bản BA. PM đã bổ sung ví dụ, nhãn nguồn và quyết định mới rồi rà lại bản sau sửa; không thay BA/Tech design bằng review này. **Ngày:** 16/10/2026. Đây là review đặc tả, không phải kiểm thử phần mềm hoặc `TECH_READY`.

## Cách kiểm

- Đối chiếu 26 Story với file nguồn bước 8, phiếu R/S/B/F/H và quyết định bổ sung R74–R79. Mỗi Story có `story.md`, `qna.md`, `handoff.md` và spec 20 mục theo template.
- Kiểm liên kết Markdown nội bộ, số thứ tự 1–20, BR có ví dụ và AC liên tục; đọc các AC theo ranh giới Story, quyền, ngoại lệ và phụ thuộc. Kiểm riêng CM-28 với R71/R73/R74 để không giữ bản nháp sau khi **rời form**; CM-33–35 không kết luận “chắc chưa lưu” từ timeout.
- Quy tắc nguồn: các nhánh UX/Tech chưa được Ninh chốt được gắn đề xuất. Những nhánh có thể đổi hành vi nghiệm thu được giữ chờ nguồn riêng. Review này không xác nhận hợp đồng API/schema, test chạy, `DEV_REVIEWED` hoặc `QA_PASS`.

## Kết luận theo Story

| CM | Đặc tả | Kết luận BA/PM | Việc tiếp theo |
| --- | --- | --- | --- |
| 10 | [Lịch sử](../capmoney-history/spec.md) | `BA_READY`: AC lọc/thứ tự/số dư bám R59–R60/B01/B17; nhãn bổ sung chỉ là UX | Tech thiết kế lịch sử/quyền |
| 11 | [Sửa thu/chi](../capmoney-edit-ledger/spec.md) | `BA_READY`: trường sửa, số dư, ngày/ví và ví ngừng có nguồn | Tech thiết kế cập nhật nguyên tử |
| 12 | [Xóa/khôi phục thu/chi](../capmoney-delete-restore-ledger/spec.md) | `BA_READY`: xác nhận/xóa mềm/khôi phục và số dư có nguồn; thông báo khi mất phản hồi là Tech gap | Tech thiết kế kết quả chưa rõ |
| 13 | [Chuyển tiền](../capmoney-transfer/spec.md) | `BA_READY`: R74–R75 đã có nguồn; PM rà lại BR/AC sau sửa, phân biệt form trước và sau Lưu | Tech thiết kế chuyển nguyên tử |
| 14 | [Sửa chuyển tiền](../capmoney-edit-transfer/spec.md) | `BA_READY`: một lần chuyển, các ví và số dư bị ảnh hưởng có AC độc lập | Tech thiết kế cập nhật nguyên tử |
| 15 | [Xóa/khôi phục chuyển](../capmoney-delete-restore-transfer/spec.md) | `BA_READY`: toàn lần chuyển và hai ví được giữ nhất quán; lỗi mất phản hồi là Tech gap | Tech thiết kế kết quả chưa rõ |
| 16 | [Sửa ví](../capmoney-wallet-edit/spec.md) | `BA_READY`: R76 chặn tên trống khi đổi; PM rà lại BR/AC, không áp đặt tên duy nhất | Tech thiết kế sửa ví |
| 17 | [Ngừng/bật ví trống](../capmoney-wallet-empty-toggle/spec.md) | `BA_READY`: số dư 0, bật lại và quyền có nguồn | Tech thiết kế trạng thái ví |
| 18 | [Ngừng ví có lịch sử](../capmoney-wallet-history-toggle/spec.md) | `BA_READY`: R77 chọn ví vừa tạo/bật, phần nhập cũ bị bỏ theo R71/R73; PM rà lại sau sửa | Tech thiết kế trạng thái/điều hướng |
| 19 | [Xóa ví](../capmoney-wallet-delete/spec.md) | `BA_READY`: B19 bao gồm giao dịch đã xóa mềm, số dư và quyền | Tech thiết kế kiểm điều kiện lúc xóa |
| 20 | [Thêm/đổi tên danh mục](../capmoney-category-manage/spec.md) | `BA_READY`: R78 chặn tên trống; PM rà lại BR/AC, không áp đặt tên duy nhất | Tech thiết kế danh mục |
| 21 | [Xóa danh mục](../capmoney-category-delete/spec.md) | `BA_READY`: B05/B11 giữ danh mục đã dùng, kể cả giao dịch xóa mềm | Tech thiết kế tham chiếu |
| 22 | [Reset mật khẩu quản lý](../capmoney-manager-password-reset/spec.md) | `BA_READY`: link 30 phút/thu hồi phiên theo nguồn; dùng một lần gắn nhãn điều kiện an toàn Tech/PM, PM rà lại | Tech thiết kế link/phiên |
| 23 | [Đổi email quản lý](../capmoney-manager-email-change/spec.md) | `BA_READY`: email mới xác minh trước đổi, phiên cũ hết; TTL/xung đột là Tech safety | Tech thiết kế xác minh/thu hồi |
| 24 | [Reset mật khẩu ghi chép](../capmoney-recorder-password-reset/spec.md) | `BA_READY`: link 30 phút/thu hồi phiên theo nguồn; dùng một lần gắn nhãn điều kiện an toàn Tech/PM, PM rà lại | Tech thiết kế link/phiên |
| 25 | [Đổi email ghi chép](../capmoney-recorder-email-change/spec.md) | `BA_READY`: email mới xác minh và dữ liệu/phiên giữ đúng phạm vi | Tech thiết kế xác minh/thu hồi |
| 26 | [Hỗ trợ mất email](../capmoney-lost-email-support/spec.md) | `BA_READY`: R79 chốt bản ghi người xác nhận, thời điểm, tài khoản ngay khi Ninh xác nhận; AC5 chặn đổi khi chưa xác nhận điều kiện R62; PM rà lại sau sửa | Tech thiết kế bản ghi/quyền/đổi email |
| 27 | [Xóa tài khoản ghi chép](../capmoney-recorder-account-delete/spec.md) | `BA_READY`: xác nhận cuối, xóa ngay, điện thoại khác mất quyền và mời lại dữ liệu trống | Tech thiết kế xóa/thu hồi |
| 28 | [Điện thoại thứ hai](../capmoney-second-phone-refresh/spec.md) | `BA_READY`: giữ form **đang mở** khi refresh; rời form trước Lưu tuân R71/R73/R74 | Tech thiết kế tải dữ liệu |
| 29 | [Hai giao dịch mới](../capmoney-concurrent-creates/spec.md) | `BA_READY`: hai ý định khác nhau đã báo Lưu đều được giữ, số dư đúng | Tech thiết kế đồng thời |
| 30 | [Hai sửa đổi](../capmoney-concurrent-edits/spec.md) | `BA_READY`: hai giao dịch khác nhau, phân biệt xung đột cùng bản | Tech thiết kế đồng thời |
| 31 | [Bản cũ thu/chi](../capmoney-stale-income-expense/spec.md) | `BA_READY`: chặn ghi đè, giữ phần nhập, không tự hợp nhất | Tech thiết kế version/CAS |
| 32 | [Bản cũ chuyển](../capmoney-stale-transfer/spec.md) | `BA_READY`: chặn toàn lần chuyển, giữ hai ví nhất quán | Tech thiết kế version/CAS |
| 33 | [Lưu thu/chi chưa rõ](../capmoney-unknown-income-expense/spec.md) | `BA_READY`: ba trạng thái kết quả và chống lặp đúng thao tác có nguồn | Tech chứng minh kết quả âm/request muộn |
| 34 | [Lưu chuyển chưa rõ](../capmoney-unknown-transfer/spec.md) | `BA_READY`: một lần chuyển, hai ví; không suy thất bại từ timeout | Tech chứng minh kết quả âm/request muộn |
| 35 | [Đối chiếu sau mở lại](../capmoney-unknown-after-reopen/spec.md) | `BA_READY`: giữ đúng định danh thao tác qua đóng/mở, không phục hồi bản nháp chưa gửi | Tech chứng minh định danh bền |

`BA_READY` ở bảng này áp dụng cho **26/26 Story CM-10–CM-35** được PM review sau bản BA do agent khác lập và các quyết định bổ sung của Ninh; từng `story.md` và `handoff.md` nối đến kết luận này. Không Story mới nào có `TECH_READY` do chưa có thiết kế được reviewer Tech độc lập và PM kiểm. Về chức năng, bước 9 đã có spec cho 35/35 Story: CM-01 và CM-07–CM-09 có BA review trước đó; CM-02–CM-06 được rà theo quy trình cũ, không tự cấp gate mới.
