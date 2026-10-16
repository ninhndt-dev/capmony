const fs = require('node:fs');
const assert = require('node:assert/strict');
const {chromium} = require('playwright-core');

const base = 'http://127.0.0.1:48082';
const token = 'D'.repeat(43); // Synthetic token for isolated QA database.
const password = 'x'.repeat(72); // Exact upper UTF-8 byte boundary.

(async () => {
  const browser = await chromium.launch({executablePath: '/usr/bin/google-chrome', headless: true,
    args: ['--no-sandbox', '--disable-dev-shm-usage']});
  try {
    const context = await browser.newContext({viewport: {width: 393, height: 851}});
    const page = await context.newPage();
    let interceptedPosts = 0;
    let serverStatus = null;
    await page.route('**/api/manager/setup', async route => {
      interceptedPosts++;
      const response = await route.fetch(); // Server has completed the request and committed before abort.
      serverStatus = response.status();
      await route.abort('failed'); // Browser receives no POST response.
    });
    await page.goto(base + '/manager/setup#token=' + token);
    assert.ok(!page.url().includes('#token='));
    await page.locator('#setup-password').fill(password);
    await page.locator('#setup-confirmation').fill(password);
    await page.getByRole('button', {name: 'Hoàn tất thiết lập'}).click();
    await page.getByRole('alert').getByText('Chưa rõ kết quả thiết lập', {exact: false}).waitFor({timeout: 15000});
    assert.equal(serverStatus, 204);
    assert.equal(interceptedPosts, 1);
    assert.equal(await page.locator('input[type=password]').count(), 0);
    assert.equal(await page.getByRole('link', {name: 'Thử đăng nhập'}).count(), 1);
    await page.waitForTimeout(1500);
    assert.equal(interceptedPosts, 1); // No automatic POST retry.
    await page.screenshot({path: __dirname + '/unknown.png', fullPage: true});
    await page.reload();
    assert.equal(await page.locator('input[type=password]').count(), 0);
    assert.equal(interceptedPosts, 1);
    fs.writeFileSync(__dirname + '/unknown.json', JSON.stringify({
      browser: 'Google Chrome headless Chromium', viewport: '393x851',
      serverStatus, browserSawPostResponse: false, interceptedPosts,
      unknownMessage: true, passwordFieldsAfterUnknown: 0,
      passwordFieldsAfterReload: 0, automaticRetry: false
    }, null, 2));
    console.log('unknown-after-commit PASS');
  } finally { await browser.close(); }
})().catch(error => { console.error(error); process.exitCode = 1; });
