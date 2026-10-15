const fs = require('node:fs');
const assert = require('node:assert/strict');
const {webkit} = require('playwright-core');

(async () => {
  const browser = await webkit.launch({headless: true});
  try {
    const context = await browser.newContext({viewport: {width: 390, height: 844}, deviceScaleFactor: 3});
    const page = await context.newPage();
    const setupStatuses = [];
    let csrfGets = 0;
    page.on('response', response => {
      if (response.url().endsWith('/api/manager/setup')) setupStatuses.push(response.status());
      if (response.url().endsWith('/api/auth/csrf') && response.request().method() === 'GET') csrfGets++;
    });
    await page.goto('http://127.0.0.1:48084/manager/setup#token=' + 'F'.repeat(43));
    await page.locator('#setup-password').fill('abcdefghijkl');
    await page.locator('#setup-confirmation').fill('abcdefghijkl');
    await page.getByRole('button', {name: 'Hoàn tất thiết lập'}).click();
    await page.getByRole('heading', {name: 'Đăng nhập tài khoản quản lý'}).waitFor({timeout: 15000});
    assert.deepEqual(setupStatuses, [204]);
    assert.ok(csrfGets >= 2, 'CSRF must be renewed after setup succeeds');
    assert.ok(page.url().endsWith('/manager/login'));
    const cookieNames = (await context.cookies()).map(cookie => cookie.name);
    assert.deepEqual(cookieNames, ['XSRF-TOKEN']); // Setup must not issue an auth session.
    await page.screenshot({path: __dirname + '/webkit-success.png', fullPage: true});
    fs.writeFileSync(__dirname + '/webkit-success.json', JSON.stringify({engine: 'WebKit',
      viewport: '390x844@3x', setupStatuses, csrfGets, loginPlaceholderShown: true,
      authSessionIssued: false, cookieNames}, null, 2));
    console.log('webkit-success PASS');
  } finally { await browser.close(); }
})().catch(error => { console.error(error); process.exitCode = 1; });
