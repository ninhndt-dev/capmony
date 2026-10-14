# Dev #43 — phát hành link và trang entry

15/10/2026, **IN PROGRESS**, không DEV_REVIEWED/QA_PASS. Baseline `1f44012` sau INFRA/MVC QA. Ninh yêu cầu hoàn tất CM-01; gate riêng#43 được PM ghi sau BA/Tech review lại và INFRAClosed. Không cấp gate#44/#45 từ tài liệu này.

## Web

Trang `/manager/setup` nhận43ký tự base64url từ fragment, xóa fragment trước Reactmount (kể cảStrictMode), giữ trongmemory; không networkPOST, không storage, không khẳng định link hợp lệ. Hai ô mật khẩu/xác nhận; nút chưa thực hiện cho đến#44. Reload hướng mở lại email. HTMLReferrerPolicy no-referrer; nginxno-store/no-referrer. Khôngthirdparty.

`npm run build` exit0; TypeScript/Vite thành công. ReactRouter có warning `use client` từ package, không buildfailure.

Dev browser `node .local/cm01-browser/entry-check.cjs` exit0, Chromium145Pixel7 và WebKit26iPhone13 viewport: fragment cleared, token absentnetwork/storage, nooverflow,2passwordinputs, buttondisabled, reloadreopenemail. [JSON](evidence/cm43/entry-browser.json), [Android](evidence/cm43/entry-android.png), [iPhone](evidence/cm43/entry-iphone.png). Đây là emulation, không thiết bị thật; NinhCM01-Q4 cho phép quaQA từngticket bằngemulation.

## Backend

CapmmDev `/root/review_spec` triển khai riêngCLI/SMTP/PostgreSQL#43 theo test-specT01–T05. Kết quả build/test/sourcefreeze và reviews sẽ được ghi khi hoàn tất; không suy từ code đang viết.

## Nguồn

Thiết kế Java/spec/ticket trong thư mục này; căn chỉnhAPI hiện hành với [SpringApplication](https://docs.spring.io/spring-boot/reference/features/spring-application.html) và [Spring Security CSRF](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html). CSRFmutation thuộc#44/#45, #43 không thêmendpointmutation.

### Backend kiểm thử trước freeze

TDD dùng seams đã định nghĩa T01–05: disabledCLI red→green; fresh-copy PostgreSQL/Mailpit integration v3 và mailreceiptstrengthening đều BUILD SUCCESS. Hai CLI JVM thật: mộtacceptedgeneration5/mộtunavailable; refusedconnection/SMTP550DATA failed, timeoutunknown, clockskewunknown, retryfencing. Mailpit linkdigest khớp generationDB, không dựa inbox cũ. Fullsuite đầu9tests phát hiện clockpositivefixturefail; host/DBVM lệch≥87ms (.local/cm43-clock-diagnosis.json) làm acceptedAt giả lập trước sendStartedAt. Giữ productionguard, positivefixture lấy timestampDB sau reserve; testfix cần green riêng trước DEV_REVIEWED. Các log trong.local giữ lại, không stage rawlogs.

Positiveclockfixture đã GREEN: `.local/cm43-clock.log`1/1, BUILD SUCCESS39s, acceptedAt/expiry bất biến và staleCASkhônghồi sinh. Mailreceiptcurrentdigest `.local/cm43-mailreceipt.log`1/1GREEN, haiCLIthật. Fullsuite trướcfix chỉfailfixtuređồng hồ,7pass/1fail/1skip; targetedrerunđãfix. QA sẽ chạy toàn bộsnapshot đóngbăng, không ghép log để giả mộtfullsuitepass. ActualSMTPaccepted→DBloss chưaDevtest, phảiQA kiểm riêng T04. Code review khôngP0/P1, optionalnamingP2recorded.

### Image đóng băng f63efe4

API vàweb buildexit0, [digests](evidence/cm43/images.json). APIfirstbuildexit137 doVM4GB còn nhiềuAPItestcũ; tạmstop7containerAPIcũ giữcontainer/data, retrycùngsource thành công. Khôngdelete/cleanup. QA độc lập freshsuite9tests8pass1skip,0fail/error; SMTPmissinghostskip được kiểm bằngCLI/Mailpit và riêngmailrunner khi regression. QA đang kiểm actualCLI/fault/imageHTTP; chưaQA_PASS.
