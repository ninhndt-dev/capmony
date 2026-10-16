const fs = require('node:fs');
const assert = require('node:assert/strict');
const {chromium} = require('playwright-core');

(async () => {
  const browser = await chromium.launch({executablePath: '/usr/bin/google-chrome', headless: true,
    args: ['--no-sandbox', '--disable-dev-shm-usage']});
  try {
    const page = await browser.newPage({viewport: {width: 393, height: 851}});
    const statuses = [];
    page.on('response', response => {
      if (response.url().endsWith('/api/manager/setup')) statuses.push(response.status());
    });
    await page.goto('http://127.0.0.1:48082/manager/setup#token=' + 'D'.repeat(43));
    await page.locator('#setup-password').fill('x'.repeat(72));
    await page.locator('#setup-confirmation').fill('x'.repeat(72));
    await page.getByRole('button', {name: 'Hoàn tất thiết lập'}).click();
    await page.getByRole('alert').getByText('Liên kết không còn dùng được', {exact: false}).waitFor();
    assert.deepEqual(statuses, [409]);
    await page.screenshot({path: __dirname + '/replay.png', fullPage: true});
    fs.writeFileSync(__dirname + '/replay.json', JSON.stringify({reopenedOldLink: true,
      setupStatuses: statuses, oldLinkUnavailableMessage: true}, null, 2));
    console.log('replay-after-commit PASS');
  } finally { await browser.close(); }
})().catch(error => { console.error(error); process.exitCode = 1; });
