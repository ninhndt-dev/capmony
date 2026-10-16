const assert = require('node:assert/strict');
const fs = require('node:fs');
const { execFileSync } = require('node:child_process');
const { chromium } = require('playwright-core');

const origin = process.env.CM02_ORIGIN;
const tokenFile = process.env.CM02_TOKEN_FILE;
const mailpit = process.env.CM02_MAILPIT_CONTAINER;
if (!origin || !tokenFile || !mailpit) throw new Error('CM02_ORIGIN, CM02_TOKEN_FILE and CM02_MAILPIT_CONTAINER are required');
const oldToken = fs.readFileSync(tokenFile, 'utf8').trim();
assert.match(oldToken, /^[A-Za-z0-9_-]{43}$/);
const initialMessageCount = JSON.parse(mailpitApi('/api/v1/messages')).messages.length;

function mailpitApi(path) {
  return execFileSync('docker', ['exec', mailpit, 'wget', '-qO-', `http://127.0.0.1:8025${path}`],
    { encoding: 'utf8', timeout: 10000 });
}
async function renewedToken() {
  for (let attempt = 0; attempt < 30; attempt++) {
    const listing = JSON.parse(mailpitApi('/api/v1/messages'));
    if (listing.messages.length === initialMessageCount + 1) {
      const tokens = listing.messages.map(({ ID }) => {
        const body = mailpitApi(`/api/v1/message/${ID}`);
        const token = body.match(/#token=([A-Za-z0-9_-]{43})/);
        assert.ok(token, 'every setup email must contain a link');
        return token[1];
      });
      const fresh = tokens.find(token => token !== oldToken && !initialTokens.has(token));
      assert.ok(fresh, 'Mailpit must contain a distinct renewal token');
      return fresh;
    }
    await new Promise(resolve => setTimeout(resolve, 200));
  }
  throw new Error('Mailpit did not receive exactly two setup emails');
}
const initialTokens = new Set(JSON.parse(mailpitApi('/api/v1/messages')).messages.map(({ ID }) => {
  const body = mailpitApi(`/api/v1/message/${ID}`);
  const token = body.match(/#token=([A-Za-z0-9_-]{43})/);
  assert.ok(token);
  return token[1];
}));

(async () => {
  const browser = await chromium.launch({ headless: true, executablePath: '/usr/bin/google-chrome',
    args: ['--no-sandbox'] });
  try {
    const page = await browser.newPage({ viewport: { width: 393, height: 851 } });
    await page.goto(`${origin}/manager/setup?case=expired#token=${oldToken}`);
    await page.getByText('Liên kết thiết lập đã hết hạn.').waitFor();
    assert.equal(await page.evaluate(() => location.hash), '', 'fragment must be removed');
    await page.getByRole('button', { name: 'Xin liên kết mới' }).click();
    await page.getByText('Yêu cầu gửi liên kết mới đã được dịch vụ email tiếp nhận.').waitFor();
    const fresh = await renewedToken();

    await page.goto(`${origin}/manager/setup?case=old#token=${oldToken}`);
    await page.getByText('Liên kết không còn dùng được.').waitFor();
    assert.equal(await page.getByRole('button', { name: 'Xin liên kết mới' }).count(), 0);

    await page.goto(`${origin}/manager/setup?case=fresh#token=${fresh}`);
    await page.getByRole('button', { name: 'Hoàn tất thiết lập' }).waitFor();
    assert.equal(await page.evaluate(() => location.hash), '');
    await page.getByLabel('Mật khẩu mới').fill('capmoney-test-2026!');
    await page.getByLabel('Xác nhận mật khẩu').fill('capmoney-test-2026!');
    await page.getByRole('button', { name: 'Hoàn tất thiết lập' }).click();
    await page.waitForURL(`${origin}/manager/login`);

    await page.goto(`${origin}/manager/setup?case=consumed#token=${fresh}`);
    await page.getByText('Liên kết không còn dùng được.').waitFor();
    assert.equal(await page.getByRole('button', { name: 'Hoàn tất thiết lập' }).count(), 0);
    console.log('Chromium real API: expired → renew → Mailpit → setup → consumed PASS');
  } finally { await browser.close(); }
})().catch(error => {
  console.error(String(error).replace(/#token=[A-Za-z0-9_-]{43}/g, '#token=[redacted]'));
  process.exitCode = 1;
});
