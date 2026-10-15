const fs = require('node:fs');
const assert = require('node:assert/strict');
const {webkit} = require('playwright-core');

(async () => {
  const browser = await webkit.launch({headless: true});
  try {
    const page = await browser.newPage({viewport: {width: 390, height: 844}, deviceScaleFactor: 3});
    const statuses = [];
    page.on('response', response => {
      if (response.url().endsWith('/api/manager/setup')) statuses.push(response.status());
    });
    await page.goto('http://127.0.0.1:48082/manager/setup#token=' + 'D'.repeat(43));
    assert.ok(!page.url().includes('#token='));
    await page.locator('#setup-password').fill('x'.repeat(72));
    await page.locator('#setup-confirmation').fill('x'.repeat(72));
    await page.getByRole('button', {name: 'Hoàn tất thiết lập'}).click();
    await page.getByRole('alert').getByText('Liên kết không còn dùng được', {exact: false}).waitFor();
    assert.deepEqual(statuses, [409]);
    await page.screenshot({path: __dirname + '/webkit-replay.png', fullPage: true});
    fs.writeFileSync(__dirname + '/webkit-replay.json', JSON.stringify({engine: 'WebKit', viewport: '390x844@3x',
      setupStatuses: statuses, oldLinkUnavailableMessage: true}, null, 2));
    console.log('webkit-replay PASS');
  } finally { await browser.close(); }
})().catch(error => { console.error(error); process.exitCode = 1; });
