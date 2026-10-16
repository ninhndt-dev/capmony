const assert = require('node:assert/strict');
const { chromium, webkit } = require('playwright-core');

async function run(engine, viewport) {
  const browser = await engine.launch({ headless: true });
  try {
    const context = await browser.newContext({ viewport });
    await context.addCookies([{ name: 'XSRF-TOKEN', value: 'known', url: 'http://127.0.0.1:49300' }]);
    const page = await context.newPage();
    let refreshRequests = 0;
    let allowMe = false;
    await page.route('**/api/**', async route => {
      const path = new URL(route.request().url()).pathname;
      if (path === '/api/manager/me') {
        return route.fulfill({ status: allowMe ? 200 : 401, contentType: 'application/json',
          body: JSON.stringify(allowMe ? { email: 'manager@example.test', role: 'MANAGER' } : { code: 'AUTH_REQUIRED' }) });
      }
      if (path === '/api/auth/csrf') return route.fulfill({ status: 204 });
      if (path === '/api/auth/refresh') {
        refreshRequests++;
        if (refreshRequests === 1) return route.abort('failed');
        allowMe = true;
        return route.fulfill({ status: 204 });
      }
      if (path === '/api/auth/login') {
        allowMe = true;
        return route.fulfill({ status: 204 });
      }
      throw new Error(`Unexpected API path: ${path}`);
    });

    await page.goto('http://127.0.0.1:49300/manager');
    await page.getByText('Chưa rõ trạng thái đăng nhập.').waitFor({ timeout: 15000 });
    assert.equal(refreshRequests, 1);
    await page.reload();
    await page.getByText('Lần gia hạn phiên trước chưa rõ kết quả').waitFor({ timeout: 15000 });
    assert.equal(refreshRequests, 1, 'reload must not retry an uncertain refresh');
    assert.equal(await page.evaluate(() => localStorage.getItem('capmoney.manager.refresh-unknown')), '1');

    await page.getByRole('button', { name: 'Đến trang đăng nhập' }).click();
    await page.locator('#manager-email').fill('manager@example.test');
    await page.locator('#manager-password').fill('correct-password');
    await page.getByRole('button', { name: 'Đăng nhập', exact: true }).click();
    await page.getByText('Đang đăng nhập:').waitFor({ timeout: 15000 });
    assert.equal(await page.evaluate(() => localStorage.getItem('capmoney.manager.refresh-unknown')), null);

    allowMe = false;
    await page.reload();
    await page.getByText('Đang đăng nhập:').waitFor({ timeout: 15000 });
    assert.equal(refreshRequests, 2, 'known login clears the marker and permits a new refresh');
    console.log(`${engine.name()}: PASS; uncertain refresh did not repeat after reload; login cleared marker`);
  } finally {
    await browser.close();
  }
}

(async () => {
  await run(chromium, { width: 393, height: 851 });
  await run(webkit, { width: 390, height: 844 });
})().catch(error => { console.error(error); process.exitCode = 1; });
