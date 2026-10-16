const fs = require('node:fs');
const assert = require('node:assert/strict');

const origin = 'http://127.0.0.1:48080';
const caddy = 'http://capmoney-qa44-059d868-caddy-1:8080';
const api1 = 'http://capmoney-qa44-059d868-api-1:3000';
const api2 = 'http://capmoney-qa44-059d868-api-2:3000';
const token = 'A'.repeat(43); // Synthetic QA token; no account outside the isolated stack.
const edge = fs.readFileSync('/qa/edge-auth', 'utf8');

async function csrf(base, direct = false) {
  const headers = direct ? {'X-CM-Edge-Auth': edge, 'X-Real-IP': '192.0.2.101', 'X-Forwarded-For': '192.0.2.101'} : {};
  const response = await fetch(base + '/api/auth/csrf', {headers});
  const setCookie = response.headers.get('set-cookie') || '';
  const cookie = setCookie.split(';', 1)[0];
  assert.equal(response.status, 204);
  assert.match(response.headers.get('cache-control') || '', /no-store/);
  assert.match(setCookie, /^XSRF-TOKEN=[^;]+; Path=\//);
  assert.ok(!/httponly/i.test(setCookie));
  assert.ok(!/domain=/i.test(setCookie));
  return {cookie, raw: decodeURIComponent(cookie.slice('XSRF-TOKEN='.length)), setCookie};
}

async function post(base, body, options = {}) {
  const headers = {'Content-Type': 'application/json'};
  if (options.origin !== null) headers.Origin = options.origin || origin;
  if (options.cookie) headers.Cookie = options.cookie;
  if (options.csrf) headers['X-XSRF-TOKEN'] = options.csrf;
  if (options.direct) {
    headers['X-CM-Edge-Auth'] = edge;
    headers['X-Real-IP'] = options.ip || '192.0.2.101';
    headers['X-Forwarded-For'] = options.ip || '192.0.2.101';
  } else if (options.forgedIp) {
    headers['X-Forwarded-For'] = options.forgedIp;
    headers['X-Real-IP'] = options.forgedIp;
  }
  const response = await fetch(base + '/api/manager/setup', {
    method: 'POST', headers, body: JSON.stringify(body), redirect: 'manual'
  });
  let code = null;
  try { code = (await response.json()).code || null; } catch {}
  return {status: response.status, code, retryAfter: response.headers.get('retry-after')};
}

const valid = {token, password: 'correct-password-qa44', passwordConfirmation: 'correct-password-qa44'};
const result = {};

async function security() {
  const check = await csrf(caddy);
  result.csrf = {status: 204, cookieHostOnly: !/domain=/i.test(check.setCookie), readable: !/httponly/i.test(check.setCookie)};
  result.noOrigin = await post(caddy, valid, {origin: null, cookie: check.cookie, csrf: check.raw});
  result.wrongOrigin = await post(caddy, valid, {origin: 'https://example.test', cookie: check.cookie, csrf: check.raw});
  result.noCsrf = await post(caddy, valid, {cookie: check.cookie});
  result.wrongCsrf = await post(caddy, valid, {cookie: check.cookie, csrf: 'wrong'});
  for (const key of ['noOrigin', 'wrongOrigin', 'noCsrf', 'wrongCsrf']) assert.equal(result[key].status, 403, key);
  const invalids = [
    ['short11', {...valid, password: 'x'.repeat(11), passwordConfirmation: 'x'.repeat(11)}],
    ['long73', {...valid, password: 'x'.repeat(73), passwordConfirmation: 'x'.repeat(73)}],
    ['unicode73', {...valid, password: 'é'.repeat(37), passwordConfirmation: 'é'.repeat(37)}],
    ['nul', {...valid, password: 'correct\0password', passwordConfirmation: 'correct\0password'}],
    ['mismatch', {...valid, passwordConfirmation: 'different-password'}],
    ['tokenFormat', {...valid, token: 'bad'}]
  ];
  result.invalid = {};
  for (const [name, body] of invalids) {
    result.invalid[name] = await post(caddy, body, {cookie: check.cookie, csrf: check.raw});
    assert.equal(result.invalid[name].status, 400, name);
  }
  result.wrongToken = await post(caddy, {...valid, token: 'B'.repeat(43)}, {cookie: check.cookie, csrf: check.raw});
  assert.equal(result.wrongToken.status, 409);
  const tooLarge = {...valid, padding: 'x'.repeat(4100)};
  result.tooLarge = await post(caddy, tooLarge, {cookie: check.cookie, csrf: check.raw});
  assert.equal(result.tooLarge.status, 413);
}

async function race() {
  const [first, second] = await Promise.all([csrf(api1, true), csrf(api2, true)]);
  const [one, two] = await Promise.all([
    post(api1, valid, {direct: true, ip: '192.0.2.111', cookie: first.cookie, csrf: first.raw}),
    post(api2, valid, {direct: true, ip: '192.0.2.112', cookie: second.cookie, csrf: second.raw})
  ]);
  result.race = {api1: one, api2: two};
  assert.deepEqual([one.status, two.status].sort(), [204, 409]);
  const again = await post(caddy, valid, {cookie: first.cookie, csrf: first.raw});
  result.replay = again;
  assert.equal(again.status, 409);
}

async function quota() {
  const first = await csrf(api1, true);
  const second = await csrf(api2, true);
  const ip = '192.0.2.200';
  result.attempts = [];
  for (let index = 0; index < 11; index++) {
    const target = index % 2 === 0 ? api1 : api2;
    const check = index % 2 === 0 ? first : second;
    const attempt = await post(target, valid, {direct: true, ip, cookie: check.cookie, csrf: check.raw});
    result.attempts.push({jvm: index % 2 + 1, ...attempt});
  }
  assert.ok(result.attempts.slice(0, 10).every(attempt => attempt.status === 409));
  assert.equal(result.attempts[10].status, 429);
  assert.ok(Number(result.attempts[10].retryAfter) > 0);
  const next = await post(api1, valid, {direct: true, ip, cookie: first.cookie, csrf: first.raw});
  result.afterLimit = next;
  assert.equal(next.status, 429);
  assert.ok(Number(next.retryAfter) <= Number(result.attempts[10].retryAfter));
}

async function spoof() {
  const check = await csrf(caddy);
  result.attempts = [];
  for (let index = 0; index < 11; index++) {
    const attempt = await post(caddy, valid, {
      cookie: check.cookie, csrf: check.raw, forgedIp: '198.51.100.' + (index + 1)
    });
    result.attempts.push(attempt);
  }
  fs.writeFileSync('/qa/spoof-observed.json', JSON.stringify(result, null, 2));
  assert.ok(result.attempts.slice(0, 10).every(attempt => attempt.status === 409));
  assert.equal(result.attempts[10].status, 429);
  assert.ok(Number(result.attempts[10].retryAfter) > 0);
}

async function outage() {
  const check = await csrf(caddy);
  result.setup = await post(caddy, valid, {cookie: check.cookie, csrf: check.raw});
  assert.equal(result.setup.status, 503);
}

async function recovered() {
  const check = await csrf(caddy);
  result.setup = await post(caddy, valid, {cookie: check.cookie, csrf: check.raw});
  assert.equal(result.setup.status, 409);
}

(async () => {
  if (process.argv[2] === 'security') await security();
  else if (process.argv[2] === 'race') await race();
  else if (process.argv[2] === 'quota') await quota();
  else if (process.argv[2] === 'spoof') await spoof();
  else if (process.argv[2] === 'outage') await outage();
  else if (process.argv[2] === 'recovered') await recovered();
  else throw new Error('Expected security or race');
  fs.writeFileSync('/qa/' + process.argv[2] + '.json', JSON.stringify(result, null, 2));
  console.log(process.argv[2] + ' PASS');
})().catch(error => { console.error(error); process.exitCode = 1; });
