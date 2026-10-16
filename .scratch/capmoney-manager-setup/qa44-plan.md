# Independent QA plan CM-01.2 / #44

15/10/2026. **PREPARED/NOT RUN**, chờ immutableSHA, hai code reviews và PM DEV_REVIEWED. QA `/root/java_qa`; không dùng #43PASS thay kết quả #44. AC2/4/5; T07–12 cùng phần setupT18/T19. CM01-Q4cho phép Chromium/WebKit emulation ở ticket, physicaldevices vẫnStoryfinal.

| Case | Phép kiểm và bằng chứng |
| --- | --- |
| T07 | DedicatedDB fixture seed issuedtoken test; acceptedAt/expiresAt theo DBclock, 71:59success vs72:00unavailable; invalid/used/stalegeneration khôngaccount. DelayCASkhônggia hạn. GhiDBtime/status trước/sau, không dựa appwallclock. |
| T08 | Password11/12/72/73UTF8bytes, Unicodebyteboundary,NUL,confirmmismatch,tokenformat/body>4KiB; error khôngconsume/create. BCryptcost12 actualbenchmark +hashconcurrency4bound. |
| T09 | PublicHTTPthroughCaddy: missing/wrongOrigin/CSRF403 trướcDBmutation; validrawXSRFcookie/header works. Noemail/token leak, noextraaccountrole. |
| T10 | TwoindependentJVM sharePG/Redis andsamecookieOrigin; concurrentsetup token⇒exactly1commit/accountid1/consumed. ForcedDBrollback via test-onlyconstraint/triggerfixture preservesissued/noaccount; keepfixtures/resources, no productionfaultflag. |
| T11 | Actualservercommit observedDB thenbrowser/proxy dropsresponse; UIunknown, no automaticPOSTretry, reopeningcannotduplicate. Distinguish Playwrightnetworkabort beforecommit from trueaftercommitdrop; only latterpasses. Loginregression waits#45. |
| T12 | Existingmanager: CLI/directPOST/oldtoken/wrongemail cannotcreate2ndrow; unique/check boundaries. |
| T18setup | GETcsrf204no-store/cookieJSreadablehost-onlypath/; missing/wrong/staleheader403DBunchanged; FEnewcsrf aftersetup. DevHTTPprofileCOOKIE_SECUREfalse explicit; noHTTPSproductionclaim. |
| T19setup | SharedRedisLua10/IP/15min across2JVM, countsuccess,11th429+RetryAfter; forgedclientIPcannotreset; fixedTTLnotextend. StopRedis→503beforehash/DB, recover. Avoid deletingkeys: freshRedisDB/keyprefix/newnetworkIPfixtures. |

Harness `.local/cm44-qa-harness/http44.py` prepared blackbox cookie/CSRF/setup/raceclient, noexecution. Finalorchestrator needs actual frozenmigration/fixture/servicecontracts; usefreshsourcecopy/hashMaven andactualpinnedimages. Newproject/ports/IPAM/DB/secretvolumes; no clean/reset/down/rm. SeedonlyfreshDBtesttokens/digests, no realpassword/email. SQLfailurefixture mustbe isolated and retained, notalter sharedDB. TwoJVMidentity +Redis/PGstates andHTTPDBsnapshots required; skip/notrun neverpass. Newartifactdestination `evidence/qa44/`, preserveqa43/infra evidence. Noappchanges/commit/Planio byQA.
