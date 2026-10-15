const fs = require('node:fs');
const assert = require('node:assert/strict');
const { chromium, webkit } = require('playwright-core');

async function exercise(engine, viewport, deviceScaleFactor) {
  const name = engine.name();
  const browser = await engine.launch({headless: true});
  try {
    const first = await browser.newContext({viewport, deviceScaleFactor});
    const second = await browser.newContext({viewport, deviceScaleFactor});
    const firstPage = await first.newPage();
    const secondPage = await second.newPage();
    const statuses = [];
    for (const page of [firstPage, secondPage]) {
      page.on('response', response => {
        if (/\/api\/(auth\/(login|refresh|logout)|manager\/me)$/.test(response.url()))
          statuses.push([response.request().method(), new URL(response.url()).pathname, response.status()]);
      });
      await page.goto('http://127.0.0.1:49082/manager/login');
      await page.locator('#manager-email').fill('manager@example.test');
      await page.locator('#manager-password').fill('correct-password');
      await page.getByRole('button', {name: 'Đăng nhập', exact: true}).click();
      await page.getByRole('heading', {name: 'Khu vực quản lý'}).waitFor({timeout: 20000});
      await page.getByText('manager@example.test').waitFor();
    }
    await firstPage.reload();
    await firstPage.getByText('manager@example.test').waitFor({timeout: 20000});
    const cookies = await first.cookies();
    const access = cookies.find(cookie => cookie.name === 'cm_dev_access');
    const refresh = cookies.find(cookie => cookie.name === 'cm_dev_refresh');
    assert.ok(access && refresh);
    assert.ok(access.httpOnly && refresh.httpOnly);
    assert.equal(access.path, '/api');
    assert.equal(refresh.path, '/api/auth');
    await firstPage.screenshot({path: `/work/qa45-${name}-manager.png`, fullPage: true});
    await firstPage.getByRole('button', {name: 'Đăng xuất mọi điện thoại'}).click();
    await firstPage.getByRole('heading', {name: 'Đăng nhập tài khoản quản lý'}).waitFor({timeout: 20000});
    await secondPage.reload();
    await secondPage.getByRole('heading', {name: 'Đăng nhập tài khoản quản lý'}).waitFor({timeout: 20000});
    assert.ok(statuses.some(([,path,status]) => path === '/api/auth/logout' && status === 204));
    assert.ok(statuses.some(([,path,status]) => path === '/api/manager/me' && status === 401));
    return {engine: name, viewport, deviceScaleFactor, statuses,
      accessCookie: {httpOnly: access.httpOnly, path: access.path, sameSite: access.sameSite},
      refreshCookie: {httpOnly: refresh.httpOnly, path: refresh.path, sameSite: refresh.sameSite},
      reloadStayedAuthenticated: true, otherPhoneRevoked: true};
  } finally { await browser.close(); }
}

(async () => {
  const results = [
    await exercise(chromium, {width: 393, height: 851}, 1),
    await exercise(webkit, {width: 390, height: 844}, 3),
  ];
  fs.writeFileSync('/work/qa45-login.json', JSON.stringify(results, null, 2));
  console.log('CM-01 login/reload/logout-all Chromium and WebKit: PASS');
})().catch(error => { console.error(error); process.exitCode = 1; });
