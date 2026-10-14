import nodemailer from 'nodemailer';
import { randomUUID } from 'node:crypto';
import { setTimeout as delay } from 'node:timers/promises';

const host = process.env.SMTP_HOST;
const port = Number(process.env.SMTP_PORT);
const to = process.env.SMTP_TO;
if (!host || !Number.isInteger(port) || !to) throw new Error('missing_smtp_test_config');
const subject = `CapMoney infrastructure test ${randomUUID()}`;

const transport = nodemailer.createTransport({
  host, port, secure: false,
  connectionTimeout: 1500,
  greetingTimeout: 1500,
  socketTimeout: 1500,
});
let accepted = false;
try {
  const result = await transport.sendMail({
    from: 'test@capmoney.local', to,
    subject,
    text: 'Non-sensitive test message',
  });
  if (!result.accepted.includes(to)) throw new Error('smtp_not_accepted');
  accepted = true;
} catch {
  console.error('smtp_unavailable_or_result_unknown');
  process.exitCode = 1;
} finally {
  transport.close();
}

if (accepted) {
  let received = false;
  for (let attempt = 0; attempt < 5; attempt++) {
    try {
      const response = await fetch('http://mailpit:8025/api/v1/messages?limit=10', {
        signal: AbortSignal.timeout(1500),
      });
      if (response.ok) {
        const body = await response.json() as { messages?: Array<{ Subject?: string }> };
        received = body.messages?.some((message) => message.Subject === subject) ?? false;
      }
    } catch {
      // Retry briefly while Mailpit indexes the accepted message.
    }
    if (received) break;
    await delay(200);
  }
  if (received) console.log('smtp_test_received_in_mailpit');
  else {
    console.error('smtp_accepted_inbox_unverified');
    process.exitCode = 1;
  }
}
