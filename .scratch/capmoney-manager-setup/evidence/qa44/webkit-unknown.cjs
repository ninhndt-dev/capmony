const fs = require('node:fs');
const assert = require('node:assert/strict');
const {webkit} = require('playwright-core');

const base = 'http://127.0.0.1:48083';
const token = 'E'.repeat(43);
const password = 'é'.repeat(36); // exactly 72 UTF-8 bytes

(async () => {
  const browser = await webkit.launch({headless: true});
  try {
    const page = await browser.newPage({viewport: {width: 390, height: 844}, deviceScaleFactor: 3});
    let interceptedPosts = 0;
    let serverStatus = null;
    await page.route('**/api/manager/setup', async route => {
      interceptedPosts++;
      const response = await route.fetch();
      serverStatus = response.status();
      await route.abort('failed');
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
    await page.waitForTimeout(1500);
    assert.equal(interceptedPosts, 1);
    await page.screenshot({path: __dirname + '/webkit-unknown.png', fullPage: true});
    await page.reload();
    assert.equal(await page.locator('input[type=password]').count(), 0);
    fs.writeFileSync(__dirname + '/webkit-unknown.json', JSON.stringify({engine: 'WebKit',
      viewport: '390x844@3x', passwordUtf8Bytes: 72, serverStatus,
      browserSawPostResponse: false, interceptedPosts, unknownMessage: true,
      formCleared: true, automaticRetry: false, formEmptyAfterReload: true}, null, 2));
    console.log('webkit-unknown-after-commit PASS');
  } finally { await browser.close(); }
})().catch(error => { console.error(error); process.exitCode = 1; });
