const assert = require('node:assert/strict');
const { chromium, webkit } = require('playwright-core');

const origin = 'http://127.0.0.1:49302';
const token = 'a'.repeat(43);

async function scenario(browser, status, renewalResult, viewport) {
  const context = await browser.newContext({ viewport });
  try {
    await context.addCookies([{ name: 'XSRF-TOKEN', value: 'known', url: origin }]);
    const page = await context.newPage();
    let renewals = 0;
    await page.route('**/api/**', route => {
      const path = new URL(route.request().url()).pathname;
      if (path === '/api/auth/csrf') return route.fulfill({ status: 204 });
      if (path === '/api/manager/setup-link/status') return route.fulfill({ status: 200,
        contentType: 'application/json', body: JSON.stringify({ status }) });
      if (path === '/api/manager/setup-link/renew') {
        renewals++;
        return renewalResult === 'unknown' ? route.abort('failed') : route.fulfill({ status: 204 });
      }
      throw new Error(`Unexpected API path: ${path}`);
    });
    await page.goto(`${origin}/manager/setup#token=${token}`);
    assert.equal(await page.evaluate(() => location.hash), '', 'fragment must be removed');
    if (status === 'active') {
      await page.getByRole('button', { name: 'Hoàn tất thiết lập' }).waitFor();
      assert.equal(renewals, 0);
    } else {
      await page.getByRole('button', { name: 'Xin liên kết mới' }).click();
      if (renewalResult === 'unknown') {
        await page.getByText('Chưa rõ yêu cầu gửi đã hoàn tất hay chưa.').waitFor();
        assert.equal(renewals, 1);
        await page.getByRole('button', { name: 'Kiểm tra lại trạng thái' }).click();
        await page.getByRole('button', { name: 'Xin liên kết mới' }).waitFor();
        assert.equal(renewals, 1, 'uncertain delivery must not auto retry');
      } else {
        await page.getByText('Yêu cầu gửi liên kết mới đã được dịch vụ email tiếp nhận.').waitFor();
        assert.equal(renewals, 1);
      }
    }
  } finally { await context.close(); }
}

(async () => {
  for (const [engine, viewport] of [[chromium, { width: 393, height: 851 }], [webkit, { width: 390, height: 844 }]]) {
    const browser = await engine.launch({ headless: true });
    try {
      await scenario(browser, 'active', 'accepted', viewport);
      await scenario(browser, 'expired', 'accepted', viewport);
      await scenario(browser, 'retryable', 'unknown', viewport);
      const page = await browser.newPage({ viewport });
      await page.goto(`${origin}/manager/setup`);
      await page.getByText('Hãy mở lại liên kết trong email thiết lập.').waitFor();
      assert.equal(await page.getByRole('button', { name: 'Xin liên kết mới' }).count(), 0);
      await page.close();
      console.log(`${engine.name()}: CM-02 setup/renew UI PASS`);
    } finally { await browser.close(); }
  }
})().catch(error => { console.error(error); process.exitCode = 1; });
