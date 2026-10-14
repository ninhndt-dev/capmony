# Independent QA plan — CM-01.1 / #43

15/10/2026, QA `/root/java_qa`. **PREPARED / NOT RUN**, chờ immutable source, independent code reviews và PM DEV_REVIEWED. Readiness đã ghi trong handoff15/10; test-spec header14/10 là trạng thái lịch sử, không kết quả hiện tại. Chưa cấp QA_PASS #43/#44/#45 hoặc Story.

| Case | Kiểm độc lập và bằng chứng cần |
| --- | --- |
| T01 | Fresh isolated DB/secret/mailbox; CLI enabled→singleton/generation/digest→Mailpit đúngmanager@example.test; HTTP bootstrap/signup không tồn tại; lưu CLI exit/state/mail recipient không lưu rawtoken. |
| T02 | Digest32byte token/generation, không plaintextDB; mailfragment; browser xóa fragment trước form và không gửi token trong request/referrer/log; reload hướng mở lại email. Scan bằng token chỉ trong memory, không in giá trị. |
| T03 | SMTP refused/silenttimeout, CLI failed/unknown khôngsuccess; retry generation mới invalidatesold trước SMTP. Kiểm DB+mail cả trước/sau, không suy SMTPaccepted là inboxdelivery. |
| T04 | Hai OS CLI processes/JVM dùng cùngDB, accepted→CAS delay/failure seam; tối đa1issued, acceptedAt khôngreset, expiry72h từ acceptedAt; CASloser khônghồi sinholdgeneration. Bằng chứng integration seam không thay manualprocessrace khi chưa chạy. |
| T05 | Emailenv khácDB, already-manager fixture, skew/roundtrip>5s failclosed/no wrongrecipient/noissued saiacceptedAt; giữ fixtures và mọi tài nguyên. |
| T06 | Chromium Android viewport+WebKit iPhoneviewport: đúng2passwordfields, fragmentremoved, nosubmit/consume claim/no validityclaim, reloadtext. **CM01-Q4 Ninh xác nhận:** Chromium/WebKit emulation đủ gate từngticket43–45; thiết bị thật chỉ giữ gate nghiệm thu Story cuối. Viewport pass có thể hoàn tấtT06ticket, không gọi thiết bị thật đã chạy. |

Harness browser chuẩn bị `.local/cm01-qa-harness/entry-browser.cjs`, chưa chạy; token synthetic khôngđụngSMTP. Backend harness `.local/cm01-qa-harness/backend43.py` nhận reviewedSHA/image, tạo DB riêng từngcase, realCLIaccepted/refused/retry/emailmismatch/twoJVMrace; chưa chạy. Clock/CAS delay/loss cần freshcopiedJUnitapprovedseams và ghi riêng giới hạn nếu chưa có fault thực. Sau codefreeze, freshsourcecopied Mavenhashmatch, retainedDocker CLI processes, SQL/mail/http snapshots sanitized, image/commit IDs và mọi case result thật. Không clean/reset/down/rm; fresh volumes/namespaces, Ryukdisabled/reuse. Không sửa app, không overwrite INFRA evidence. Kết quả mới tại `.local/cm01-qa` và `.scratch/capmoney-manager-setup/evidence/qa43/`.

## T04 actual SMTPaccepted→DBloss fixture (prepared, not run)

`.local/cm01-qa-harness/smtp-accepted-db-loss.py` is a stdlib SMTP sink for retained tools container. Dedicated case PostgreSQL container/volume, separate network alias (not shared runtimePG). Host mounts retained marker directory: sink receives DATA in memory → writes smtp-before-accept → host stops **case PG** → host writes db-stopped → sink sends250 and marksaccepted. ActualCLI then attempts CAS on unavailableDB and mustexitunknown/3 withoutaccepted claim. Host restores PG in finally, checks row notissued, records generation/state; fresh safe retry may require lease expiry (30s) and must advancegeneration. Never save rawmail/token or printsecret. Keep sink/PG/CLI/volume; no productionflags or Docker socket inside fixture. Timeout/finally must restore PG even onassertfailure. This actualfault is separate from JUnitstalegeneration/CASdelay and cannot be inferred from mocks. Wait immutableSHA/DEV_REVIEWED before execute.
