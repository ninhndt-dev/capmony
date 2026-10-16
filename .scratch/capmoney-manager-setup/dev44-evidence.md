# Dev #44 — hoàn tất thiết lập singleton

15/10/2026 **IN PROGRESS** baselinec1e804c; QA43PASS/HARDgiải, PMREADY44 tronghandoff. KhôngDEV_REVIEWED/QA_PASS44 hoặcgate45.

## Web

TanStackQuery5.104.2pin theoTS22 + fetchsameorigin/no-store. Dependencychuẩn bị trongcopy mới `.local/cm44-node-*` trước copymanifest/lock và thêm thư viện mới; giữ dependency cũ. TypeScriptpass. HostNode22ngoàiengine24 nên productionimageNode24 mới làbuildruntime chuẩn; không dùnghostbuild đểkhẳng địnhNode24compatibility.

Build vào thư mục mới `.local/cm44-web-build-01 --emptyOutDir=false` exit0 để giữ artifactlượttrước; React/TanStack `use client`warnings từpackage khôngchặnbuild. Dockerbuild freshcontext sẽkiểm Node24đúngpin.

Frontend thực hiện formvalidation12–72UTF8bytes/NUL/confirmation, GETCSRF trướcPOST, rawheader, explicitretryfalse/gcTime0 vàkhôngcachemutationvariableschứacredentials. POSTunknown/5xx khôngclaimfailurechắc: clearpassword/token, lockform, hướng thửlogin; CSRFGETfail trướcPOST cho phép thửlại; POSTsuccessknown giữsuccess dùGETCSRFsauđófail. Successchuyểnmanager/loginplaceholder chờ#45. Không JWT/session từsetup.

DevPlaywright fixtures10/10pass trênChromiumAndroid/WebKitiPhone: mỗienginevalidationnopost, success1POST, unknown1POST+noretry/clearform,409linkunavailable,429rate, CSRFGET503nopost. [JSON](evidence/cm44/ui-fixtures.json), screenshotscùngthưmục. Đây làHTTPfixtures, khôngthayQAbackend thật. Reviewer `/root/review_standards` độc lập webSpec/Standards khôngP0/P1.

## Backend

CapmmDev `/root/review_spec` đangTDD tạiHTTP/Postgres/Redisđãreview; migration002accountmới, giữ001bootstrap. Chưa cókếtluậnbuild/test/backendQA.

## Nguồn

[Mutations TanStack](https://tanstack.com/query/latest/docs/framework/react/guides/mutations), [Spring Security CSRF](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html), spec/design/ticket44 trongthưmục này. Composechỉdev/testđặt profiledev vàCOOKIE_SECURE=false rõràng; productionkhôngdùngCompose này vàphảifailclosednếukhôngHTTPS/Secure.
