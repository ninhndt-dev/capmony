# CM-02 — bàn giao hiện hành

## CapmmTech / Story CM-02 / 16-10-2026

**DONE:** Đối chiếu đặc tả cũ với mã Java CM-01 (`BootstrapStore`, `BootstrapIssuer`, `ManagerSetupStore`, `SetupEntry`), tạo [thiết kế Java nháp](technical-design-java.md) về kiểm link hết hạn, gửi lại, chứng minh quyền thử lại, race và giới hạn gửi.

**RESULT:** `DRAFT`; chưa `BA_READY`, chưa `TECH_READY`, chưa tạo ticket Dev. Đây là phân tích/thiết kế tài liệu, không phải code hay kết quả QA.

**NEED HUMAN:** PM/BA rà lựa chọn yêu cầu mở link hết hạn để xin lại, hành vi truy cập đường dẫn trực tiếp, hạn mức gửi/cooldown. Chỉ chuyển gate sau khi có review đúng vai trò và các quyết định sản phẩm còn thiếu được đối chiếu.

**PENDING:** Rebaseline spec 20 mục với AC/BR, review BA/PM, Tech reviewer độc lập, spike Redis quota hai JVM, PM kiểm phạm vi, rồi mới cân nhắc ticket dọc. CM-01 #44/#45 vẫn chờ gate riêng.

**DETAIL:** Nguồn [spec CM-02](spec.md), [CM-01 Java](../capmoney-manager-setup/technical-design-java.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Chưa chạy test vì chưa thay code.

**Người ghi:** Codex (CapmmTech, tác giả thiết kế; không tự nhận vai reviewer độc lập).

## CapmmDev / Story CM-02 / 16-10-2026

**DONE:** Theo yêu cầu Ninh triển khai tiếp, đã code API, migration, throttle Redis và giao diện xin lại link hết hạn; thêm test tích hợp, [browser regression](browser-regression.cjs), [Chrome qua API thật](browser-real-api.cjs), SMTP/Mailpit thật, hai JVM renew đồng thời, lỗi Redis/DB trước reserve và DB lỗi sau SMTP nhận thư.

**RESULT:** Code đã build và tự kiểm theo [bằng chứng](implementation-evidence.md). Chưa có `DEV_REVIEWED`/`QA_PASS` độc lập.

**NEED HUMAN:** Người khác tác giả code review thiết kế/code và QA trên build pin; PM/BA đối chiếu lựa chọn yêu cầu phải mở link hết hạn cùng ngưỡng quota. Không cần Ninh duyệt từng thao tác triển khai vì đã cho phép trước.

**PENDING:** Kiểm kết quả DB commit chưa rõ sau khi lệnh ghi đã gửi, WebKit qua API thật, thiết bị vật lý và review/QA độc lập như [evidence](implementation-evidence.md); chốt gate đúng workflow và trạng thái Planio theo bằng chứng thật.

**DETAIL:** Bản thay đổi trên `main` sau khi commit/push. Các test chạy trên tài nguyên thử mới, giữ tài nguyên cũ.

**Người ghi:** Codex (CapmmDev, tác giả code và người tự kiểm).
