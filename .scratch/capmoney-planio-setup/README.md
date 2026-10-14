# Kết nối Planio cục bộ — CapMoney

Ngày15/10/2026. Ninh yêu cầu setup Planio rồi mới đổi cấu trúc BE, cung cấp project https://tanninh.planio.com/projects/capmoney và báo đã kết nối. Agent đã kiểm API thành công: project4/identifiercapmoney, user1; #46/#47 đangIn Progress, Closed hiện cóID8. Chưa suy trạng thái issue từ tài liệu cũ.

## Sử dụng

```bash
bash infra/scripts/setup-planio.sh
python3 infra/scripts/planio.py check
python3 infra/scripts/planio.py preview-infra
```

Wizard chỉ mở trang tài khoản, nhận key ẩn trong Terminal, kiểm project và lưu ngoàiGit tại `.local/planio/credentials.env` mode0600/dir0700. Không đặt key trong chat, URL hoặc command argument. Key thay thế có backup, không xóa bản cũ. Theo [Planio API](https://plan.io/api/), key lấy tại avatar→My account→sidebar→API Access Key→Show, gửi qua header X-Redmine-API-Key. Đây là kết nối CLI cục bộ, không phải plugin đã được cài; tìm connector Planio/Redmine không có kết quả phù hợp.

`sync-infra` là thao tác đã được Ninh cho phép trong yêu cầu setup: đọc project/issue/status thật, kiểm đủ QA+IF-Q6 và source chưa đổi khỏi310ad7f, nối bằng chứng vào description#47/#46, đóng#47/100% và đọc lại. Không đóng#46 tự động vì còn follow-up MVC; không sửa#43–45, không comments/messages, không DELETE. Agent thực hiện sau review. Không chạy sync snapshot cũ sau khi đã đổi Java source.

## Kiểm và review

- `bash -n infra/scripts/setup-planio.sh`, Python compile: qua.
- `python3 -B infra/scripts/test_planio.py`:5 tests qua; sai project/parent không ghi, phát hiện edit trướcPUT, chấp nhận CRLF chuẩn hóa; idempotent và giữ description/scope#46/#47.
- Git ignore credential path: đúng; không đọc/in key trong bằng chứng.
- Standards reviewer `/root/review_standards`:0 blocker. Spec reviewer `/root/review_spec`: khôngP0/P1; P2concurrency được bổ sung snapshot trướcPUT, kiểm issue không đổi ngay trước ghi và so toàn description sau ghi. Không coi cơ chế này là khóa atomic phía server; không tự retry khi có bất đồng.

Wizard dựa trên `.agents/skills/wizard/template.sh`; giữ library nguyên trạng, override write_env trong STAGES để tránh cleanup temp của template theo quyền không xóa tài nguyên. Không cần quyền GitHub/CI hoặc thay cài đặt Planio toàn hệ thống.

## Kết quả thực ghi và đọc lại

`python3 infra/scripts/planio.py sync-infra` exit0 sau review và kiểm5 mock tests. [sync-result.json](sync-result.json): #47Closed(ID8), #46InProgress(ID6), verified_after_write=true. Giữ mô tả cũ và nối evidence; snapshot trướcPUT ngoàiGit. Không sửa#43–45. Kết nối thành công qua API không đồng nghĩa đã cài connector.
