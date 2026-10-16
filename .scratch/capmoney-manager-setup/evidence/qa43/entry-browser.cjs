// Run after frozen source and DEV_REVIEWED; synthetic token only.
const { chromium, webkit } = require('../cm01-browser/node_modules/playwright');
const fs = require('fs');
(async()=>{const result=[];const out='.local/cm01-qa';fs.mkdirSync(out,{recursive:true});
for(const [name,type,viewport] of [['android-viewport',chromium,{width:393,height:851}],['iphone-viewport',webkit,{width:390,height:844}]]){
 const browser=await type.launch({headless:true});const page=await browser.newPage({viewport});const requests=[];page.on('request',r=>requests.push({url:r.url(),method:r.method(),body:r.postData(),referer:r.headers()['referer']}));
 const token='A'.repeat(43);await page.goto((process.env.CM01_ORIGIN||'http://127.0.0.1:15173')+'/manager/setup#token='+token);await page.waitForTimeout(500);
 if(page.url().includes('#'))throw Error('Fragment retained');const inputs=await page.locator('input[type=password]').count();if(inputs!==2)throw Error('Expected password and confirmation fields');
 if(requests.some(r=>JSON.stringify(r).includes(token)))throw Error('Token leaked to network');
 await page.screenshot({path:out+'/'+name+'.png',fullPage:true});await page.reload();await page.waitForTimeout(300);const reloadText=await page.locator('body').innerText();
 result.push({name,emulationOnly:true,physicalDevice:false,url:page.url(),passwordFields:inputs,tokenAbsentNetwork:true,reloadText,requests});await browser.close();}
fs.writeFileSync(out+'/entry-browser.json',JSON.stringify(result,null,2));})().catch(e=>{console.error(e.message);process.exit(1)});
