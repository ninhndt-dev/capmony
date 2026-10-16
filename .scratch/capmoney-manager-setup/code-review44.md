# Code review #44

15/10/2026, baselinec1e804c; review source44 trước immutablecommit. KhôngQA_PASS hoặcgate45.

## Backend Spec

Reviewer `/root`, khác tác giảbackend `/root/review_spec`. Đối chiếu AC2/4/5/design§3/5/7: migration002accountmới/001khôngđổi; controllerHTTP/servicevalidation-hash-throttle/repositorytransaction; rawCSRF204SPA+cookieprofile/Origin/edge trướcmutation;4KiBcontent-length vàchunkedbufferbounded;12–72UTF8bytes/NUL/confirm/token; BCrypt12semaphore4; RedisLuaatomic10/IP/fixed900s/failclosed trướchash; locksingleton/DBclockexpirystrict/constanttimecurrentdigest; insertaccount1+consume+digestnull cùngcommit; unknowncommit503khôngclaimnot-saved. ErrorDTOredacted, JSONcode/message/requestId/no-store, known400/405/413/415mapping. KhôngcấpJWT/setupsession hoặc45endpoint. KhôngP0/P1 từreviewmã; runtime/haiJVM/commitloss cầnQA.

## Standards và web Spec

Reviewerđộc lập `/root/review_standards` rà backend44/ComposeStandards và webSpec/Standards: khôngP0/P1, khônghardstandardviolation. Xác nhận ingress→body→CSRF order trêncode, explicitdev exception, boundedbuffer, atomictransaction/quota. WebunknownPOSTclearscredentials/locksresubmit, retryfalse, CSRFsafeGETbehavior, fieldlabels/errors/pending đúngscope. KhôngtựgánstaticreviewlàruntimeQA.

### Recheck cấu hình cuối

RootSpec phát hiện mixedprod+dev vẫn lọt exceptiondev; Dev đã testred và fix startupreject `(prod|production)+(dev|test)`. Loại defaultUserDetailsautoconfig vốnkhôngdùng được nhưng in generatedpassword. Cảhai giữcontract khôngfallbackHTTPBasic vàproductionfailclosed; khôngthêm45auth. Fullsuitev1 chỉfailfixtureRedisquota cũ do reusefixedIP; fixturetạoRedisUUIDmới/giữservercũ, khôngxóa counters. Chờfullv2 vàQA chứngminhruntime.
