# CM-02 — bàn giao hiện hành

## CapmmTech / Story CM-02 / 16-10-2026

**DONE:** Đối chiếu đặc tả cũ với mã Java CM-01 (`BootstrapStore`, `BootstrapIssuer`, `ManagerSetupStore`, `SetupEntry`), tạo [thiết kế Java nháp](technical-design-java.md) về kiểm link hết hạn, gửi lại, chứng minh quyền thử lại, race và giới hạn gửi.

**RESULT:** `DRAFT`; chưa `BA_READY`, chưa `TECH_READY`, chưa tạo ticket Dev. Đây là phân tích/thiết kế tài liệu, không phải code hay kết quả QA.

**NEED HUMAN:** PM/BA rà lựa chọn yêu cầu mở link hết hạn để xin lại, hành vi truy cập đường dẫn trực tiếp, hạn mức gửi/cooldown. Chỉ chuyển gate sau khi có review đúng vai trò và các quyết định sản phẩm còn thiếu được đối chiếu.

**PENDING:** Rebaseline spec 20 mục với AC/BR, review BA/PM, Tech reviewer độc lập, spike Redis quota hai JVM, PM kiểm phạm vi, rồi mới cân nhắc ticket dọc. CM-01 #44/#45 vẫn chờ gate riêng.

**DETAIL:** Nguồn [spec CM-02](spec.md), [CM-01 Java](../capmoney-manager-setup/technical-design-java.md), [workflow](../../docs/workflows/capmm-end-to-end.md). Chưa chạy test vì chưa thay code.

**Người ghi:** Codex (CapmmTech, tác giả thiết kế; không tự nhận vai reviewer độc lập).
