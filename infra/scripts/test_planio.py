"""No network/key required: verify the permitted tracker write scope."""
import copy
import importlib.util
from pathlib import Path
import unittest
from unittest.mock import patch
import uuid

spec = importlib.util.spec_from_file_location("planio_client", Path(__file__).with_name("planio.py"))
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class FakePlanio(module.Planio):
    def __init__(self):
        self.calls = []
        self.issues = {
            47: {"id": 47, "project": {"id": 4}, "parent": {"id": 46},
                 "subject": "INFRA-01.1", "description": "Existing ticket details",
                 "status": {"id": 6, "name": "In Progress"}, "done_ratio": 0},
            46: {"id": 46, "project": {"id": 4}, "subject": "INFRA-01",
                 "description": "Existing parent details", "status": {"id": 6, "name": "In Progress"}},
        }

    def request(self, path, method="GET", payload=None):
        self.calls.append((method, path))
        if path == "/users/current.json":
            return {"user": {"id": 1}}
        if path == "/projects/capmoney.json":
            return {"project": {"id": 4, "identifier": "capmoney"}}
        if path == "/issue_statuses.json":
            return {"issue_statuses": [{"id": 99, "name": "Closed", "is_closed": True}]}
        issue_id = int(path.split("/")[-1].split(".")[0])
        if method == "PUT":
            update = dict(payload["issue"])
            status_id = update.pop("status_id", None)
            self.issues[issue_id].update(update)
            if status_id is not None:
                self.issues[issue_id]["status"] = {"id": status_id, "name": "Closed"}
            return None
        return {"issue": copy.deepcopy(self.issues[issue_id])}


class TrackerScopeTests(unittest.TestCase):
    def setUp(self):
        path = module.ROOT / ".local/planio-client-tests" / uuid.uuid4().hex / "credentials.env"
        path.parent.mkdir(parents=True, mode=0o700)
        self.config_patch = patch.object(module, "CONFIG", path)
        self.config_patch.start()
        self.report = path.parent / "qa-fixture.md"
        self.report.write_text("Ninh đã chọn A; 310ad7f\n" + "\n".join(
            f"IF-J-T0{i} **PASS**" for i in range(1, 9)))
        self.report_patch = patch.object(module, "REPORT", self.report)
        self.report_patch.start()
        self.diff_patch = patch.object(module.subprocess, "check_output", return_value="")
        self.diff_mock = self.diff_patch.start()

    def tearDown(self):
        self.diff_patch.stop()
        self.report_patch.stop()
        self.config_patch.stop()  # Test files remain; no cleanup deletion.

    def test_changed_source_stops_before_any_write(self):
        self.diff_mock.return_value = "source changed"
        client = FakePlanio()
        with self.assertRaisesRegex(module.SetupError, "Source đã khác"):
            client.sync_infra()
        self.assertFalse(any(method == "PUT" for method, _ in client.calls))

    def test_incomplete_qa_stops_before_any_write(self):
        self.report.write_text("Ninh đã chọn A; 310ad7f\nIF-J-T01 **PASS**")
        client = FakePlanio()
        with self.assertRaises(module.SetupError):
            client.sync_infra()
        self.assertFalse(any(method == "PUT" for method, _ in client.calls))

    def test_wrong_project_is_rejected_before_any_write(self):
        client = FakePlanio()
        client.issues[47]["project"]["id"] = 123
        with self.assertRaises(module.SetupError):
            client.sync_infra()
        self.assertFalse(any(method == "PUT" for method, _ in client.calls))

    def test_wrong_parent_is_rejected_before_any_write(self):
        client = FakePlanio()
        client.issues[47]["parent"]["id"] = 123
        with self.assertRaises(module.SetupError):
            client.sync_infra()
        self.assertFalse(any(method == "PUT" for method, _ in client.calls))

    def test_sync_preserves_existing_content_and_is_idempotent(self):
        client = FakePlanio()
        result = client.sync_infra()
        self.assertEqual(result["issue_47"]["id"], 99)
        self.assertEqual(result["issue_46"]["id"], 6)
        self.assertEqual(client.issues[47]["done_ratio"], 100)
        self.assertTrue(client.issues[47]["description"].startswith("Existing ticket details"))
        self.assertTrue(client.issues[46]["description"].startswith("Existing parent details"))
        writes = [path for method, path in client.calls if method == "PUT"]
        self.assertEqual(writes, ["/issues/47.json", "/issues/46.json"])
        client.sync_infra()
        self.assertEqual([path for method, path in client.calls if method == "PUT"], writes)

    def test_concurrent_edit_stops_before_put(self):
        class ConcurrentPlanio(FakePlanio):
            reads = 0

            def issue(self, issue_id):
                if issue_id == 47:
                    self.reads += 1
                    if self.reads == 3:
                        self.issues[47]["description"] += " concurrent edit"
                return super().issue(issue_id)

        client = ConcurrentPlanio()
        with self.assertRaises(module.SetupError):
            client.sync_infra()
        self.assertFalse(any(method == "PUT" for method, _ in client.calls))

    def test_server_crlf_normalization_is_accepted_without_losing_content(self):
        class CrlfPlanio(FakePlanio):
            def request(self, path, method="GET", payload=None):
                result = super().request(path, method, payload)
                if method == "PUT":
                    issue_id = int(path.split("/")[-1].split(".")[0])
                    self.issues[issue_id]["description"] = self.issues[issue_id]["description"].replace("\n", "\r\n")
                return result

        self.assertTrue(CrlfPlanio().sync_infra()["verified_after_write"])


if __name__ == "__main__":
    unittest.main()
