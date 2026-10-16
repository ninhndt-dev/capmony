const fs = require('node:fs');
const assert = require('node:assert/strict');
const base = 'http://capmoney-qa44-boundary-caddy-1:8080';
const origin = 'http://127.0.0.1:48081';
const phase = process.argv[2];
const token = (phase === 'valid' ? 'C' : 'B').repeat(43);
const password = 'abcdefghijkl'; // exactly 12 UTF-8 bytes

(async () => {
  const csrf = await fetch(base + '/api/auth/csrf');
  assert.equal(csrf.status, 204);
  const cookie = csrf.headers.get('set-cookie').split(';', 1)[0];
  const header = decodeURIComponent(cookie.slice('XSRF-TOKEN='.length));
  const setup = await fetch(base + '/api/manager/setup', {
    method: 'POST', headers: {
      Origin: origin, Cookie: cookie, 'X-XSRF-TOKEN': header, 'Content-Type': 'application/json'
    }, body: JSON.stringify({token, password, passwordConfirmation: password})
  });
  const expected = phase === 'expired' || phase === 'stale' ? 409 : phase === 'valid' ? 204 : null;
  assert.ok(expected);
  const result = {phase, status: setup.status, expected, retryAfter: setup.headers.get('retry-after')};
  fs.writeFileSync('/qa/' + phase + '.json', JSON.stringify(result, null, 2));
  assert.equal(setup.status, expected);
  console.log(phase + ' PASS');
})().catch(error => { console.error(error); process.exitCode = 1; });
