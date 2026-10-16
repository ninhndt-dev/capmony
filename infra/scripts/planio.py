#!/usr/bin/env python3
"""Local Planio connection and bounded CapMoney infrastructure synchronization."""

import argparse
import datetime
import json
import os
from pathlib import Path
import re
import shlex
import stat
import subprocess
import sys
import urllib.error
import urllib.request


ROOT = Path(__file__).resolve().parents[2]
SITE = "https://tanninh.planio.com"
PROJECT_ID = 4
CONFIG = ROOT / ".local/planio/credentials.env"
KEY_PATTERN = re.compile(r"[A-Za-z0-9_-]{20,256}")
REPORT = ROOT / ".scratch/capmoney-infrastructure/qa-evidence-java.md"
MARKER = "CapMoney Java verification — 310ad7f"


class SetupError(Exception):
    pass


class NoRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        return None


class Planio:
    def __init__(self, key):
        if not KEY_PATTERN.fullmatch(key):
            raise SetupError("API key sai định dạng; không nhập mật khẩu tài khoản.")
        self.key = key
        self.opener = urllib.request.build_opener(NoRedirect())

    def request(self, path, method="GET", payload=None):
        if method not in ("GET", "PUT") or not path.startswith("/") or "://" in path:
            raise SetupError("Thao tác ngoài phạm vi công cụ Planio CapMoney.")
        data = None if payload is None else json.dumps(payload).encode()
        request = urllib.request.Request(
            SITE + path,
            data=data,
            method=method,
            headers={"X-Redmine-API-Key": self.key, "Accept": "application/json",
                     "Content-Type": "application/json"},
        )
        try:
            with self.opener.open(request, timeout=20) as response:
                raw = response.read()
                return json.loads(raw) if raw else None
        except urllib.error.HTTPError as error:
            raise SetupError(f"Planio trả HTTP {error.code}; kiểm quyền/API/key trên Planio.") from None
        except (urllib.error.URLError, TimeoutError, json.JSONDecodeError):
            raise SetupError("Chưa kết nối được Planio hoặc phản hồi không phải JSON; không đổi tracker.") from None

    def identity(self):
        user = self.request("/users/current.json")["user"]
        project = self.request("/projects/capmoney.json")["project"]
        if project["id"] != PROJECT_ID or project["identifier"] != "capmoney":
            raise SetupError("Dừng: project không khớp CapMoney ID4.")
        return {"site": SITE, "project_id": PROJECT_ID, "project": "capmoney",
                "user_id": user["id"]}

    def issue(self, issue_id):
        issue = self.request(f"/issues/{int(issue_id)}.json")["issue"]
        if issue["project"]["id"] != PROJECT_ID:
            raise SetupError(f"Dừng: issue#{issue_id} không thuộc CapMoney.")
        return issue

    def plan(self):
        identity = self.identity()
        child, parent = self.issue(47), self.issue(46)
        if child.get("parent", {}).get("id") != 46:
            raise SetupError("Dừng: #47 không còn là con #46; cần đối chiếu lại.")
        statuses = self.request("/issue_statuses.json")["issue_statuses"]
        closed = [s for s in statuses if s.get("is_closed") and s["name"].casefold() == "closed"]
        if len(closed) != 1:
            raise SetupError("Chưa xác định duy nhất trạng thái Closed trên Planio.")
        return {**identity, "issue_47": {"subject": child["subject"],
                "current_status": child["status"], "target_status": closed[0]},
                "issue_46": {"subject": parent["subject"], "status": parent["status"],
                "action": "Giữ trạng thái; bổ sung bằng chứng, còn follow-up MVC đã được yêu cầu."},
                "code_commit": "310ad7fbf2e86d7d3b682bbfdb303af02314b34e",
                "qa": "IF-J-T01–08 PASS; IF-Q6 Ninh đã chọn JUnit6 ngày15/10/2026",
                "writes": ["#47: ghi evidence và Closed/100%", "#46: ghi evidence, không đóng tự động"],
                "not_changed": ["CM-01 #43–45", "quan hệ phụ thuộc", "issue khác", "tài nguyên/dữ liệu"]}

    def sync_infra(self):
        plan = self.plan()
        report = REPORT.read_text()
        if any(f"IF-J-T0{i} **PASS**" not in report for i in range(1, 9)):
            raise SetupError("QA report chưa đủ8 case PASS; không đóng issue.")
        if "Ninh đã chọn A" not in report or "310ad7f" not in report:
            raise SetupError("Thiếu bằng chứng IF-Q6 hoặc snapshot QA.")
        changed = subprocess.check_output(
            ["git", "diff", "310ad7f", "--", "apps/api", "apps/web", "infra/compose.yaml",
             "infra/compose.probe.yaml", "infra/Caddyfile"], cwd=ROOT, text=True)
        if changed:
            raise SetupError("Source đã khác snapshot QA310ad7f; cần review trước khi đồng bộ.")
        block = "\n\n" + MARKER + "\n" + (
            "Code: 310ad7fbf2e86d7d3b682bbfdb303af02314b34e\n"
            "Documentation: 2afe59e (local Git; remote publication not verified).\n"
            "DEV_REVIEWED and QA_PASS: infrastructure only, 15/10/2026.\n"
            "Independent reviews: /root/review_standards and /root/review_spec.\n"
            "Independent tester: /root/java_qa; IF-J-T01 through IF-J-T08 PASS.\n"
            "PostgreSQL stop/pause and Redis outage: bounded503 and automatic recovery, same JVM.\n"
            "Routes, OpenAPI, ingress/Origin/peer/secret, isolation, persistence, Mailpit and regression PASS.\n"
            "Ninh confirmed JUnit6, retaining Java25/Spring Boot4.1 (IF-Q6,15/10/2026).\n"
            "Evidence: .scratch/capmoney-infrastructure/qa-evidence-java.md and evidence/java-final/.\n"
            "All project resources preserved. No CM-01, production or mobile acceptance implied.\n"
            "Follow-up authorized: organize BE by feature with explicit MVC layers; separate reviewed ticket.\n"
        )
        for issue_id in (47, 46):
            issue = self.issue(issue_id)
            payload = {}
            if MARKER not in (issue.get("description") or ""):
                payload["description"] = (issue.get("description") or "") + block
            if issue_id == 47:
                if issue["status"]["id"] != plan["issue_47"]["target_status"]["id"]:
                    payload["status_id"] = plan["issue_47"]["target_status"]["id"]
                if issue.get("done_ratio") != 100:
                    payload["done_ratio"] = 100
            if payload:
                snapshot_dir = config_path().parent / "snapshots"
                snapshot_dir.mkdir(mode=0o700, exist_ok=True)
                if not snapshot_dir.resolve().is_relative_to(ROOT):
                    raise SetupError("Snapshot path nằm ngoài project; dừng trước khi ghi tracker.")
                stamp = datetime.datetime.now(datetime.timezone.utc).strftime("%Y%m%dT%H%M%S%fZ")
                snapshot = snapshot_dir / f"issue-{issue_id}-{stamp}.json"
                fd = os.open(snapshot, os.O_WRONLY | os.O_CREAT | os.O_EXCL, 0o600)
                with os.fdopen(fd, "w") as output:
                    json.dump(issue, output, ensure_ascii=False, indent=2)
                latest = self.issue(issue_id)
                if latest != issue:
                    raise SetupError(f"#{issue_id} vừa thay đổi; giữ snapshot, không ghi hoặc tự retry.")
                self.request(f"/issues/{issue_id}.json", "PUT", {"issue": payload})
            actual = self.issue(issue_id)
            if MARKER not in (actual.get("description") or ""):
                raise SetupError(f"# {issue_id}: chưa đọc lại được evidence sau ghi; không tự lặp PUT.")
            if ("description" in payload
                    and (actual.get("description") or "").replace("\r\n", "\n")
                    != payload["description"].replace("\r\n", "\n")):
                raise SetupError(f"#{issue_id}: nội dung đọc lại khác dự kiến; giữ snapshot để đối chiếu, không tự retry.")
            if issue_id == 47 and (actual["status"]["id"] != plan["issue_47"]["target_status"]["id"]
                                  or actual.get("done_ratio") != 100):
                raise SetupError("#47 chưa đạt trạng thái đích khi đọc lại; giữ gate chờ đối chiếu.")
        return {"issue_47": self.issue(47)["status"], "issue_46": self.issue(46)["status"],
                "verified_after_write": True}


def config_path():
    if not CONFIG.resolve().is_relative_to(ROOT):
        raise SetupError("Credential path phải nằm trong project local, không qua symlink ra ngoài.")
    return CONFIG


def load_key():
    path = config_path()
    if not path.is_file():
        raise SetupError("Chưa có key cục bộ. Chạy bash infra/scripts/setup-planio.sh trong Terminal.")
    if stat.S_IMODE(path.stat().st_mode) != 0o600:
        raise SetupError("Credential file cần quyền0600; không đọc key khi quyền đang rộng.")
    tokens = shlex.split(path.read_text())
    if len(tokens) != 1 or not tokens[0].startswith("PLANIO_API_KEY="):
        raise SetupError("Credential file không đúng định dạng.")
    return tokens[0].partition("=")[2]


def save_key(key):
    path = config_path()
    path.parent.mkdir(parents=True, exist_ok=True, mode=0o700)
    path.parent.chmod(0o700)
    if path.exists():
        stamp = datetime.datetime.now(datetime.timezone.utc).strftime("%Y%m%dT%H%M%S%fZ")
        backup = path.with_name("credentials-" + stamp + ".env")
        fd = os.open(backup, os.O_WRONLY | os.O_CREAT | os.O_EXCL, 0o600)
        with os.fdopen(fd, "wb") as output:
            output.write(path.read_bytes())
    fd = os.open(path, os.O_WRONLY | os.O_CREAT | os.O_TRUNC | os.O_NOFOLLOW, 0o600)
    os.fchmod(fd, 0o600)
    with os.fdopen(fd, "w") as output:
        output.write(f"PLANIO_API_KEY='{key}'\n")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("action", choices=["configure", "check", "preview-infra", "sync-infra"])
    args = parser.parse_args()
    try:
        if args.action == "configure":
            key = sys.stdin.read().strip()
            client = Planio(key)
            result = client.identity()
            save_key(key)
            result["credentials_saved"] = ".local/planio/credentials.env (0600)"
        else:
            client = Planio(load_key())
            if args.action == "check":
                result = client.identity()
            elif args.action == "preview-infra":
                result = client.plan()
            else:
                result = client.sync_infra()
        print(json.dumps(result, ensure_ascii=False, indent=2))
    except (SetupError, OSError) as error:
        # Never emit server response bodies or account JSON: they can include the key.
        print(f"Planio setup: {error if isinstance(error, SetupError) else 'Lỗi filesystem/network cục bộ.'}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
