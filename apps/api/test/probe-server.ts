// This file is copied only into the Docker `probe` build target, never `runtime`.
import { serve } from '@hono/node-server';
import { getConnInfo } from '@hono/node-server/conninfo';
import { createApp } from '../src/app.js';
import { loadConfig } from '../src/config.js';
import { createDbPool } from '../src/db.js';
import { checkMutationBoundary } from '../src/security.js';

const config = loadConfig();
const db = createDbPool(config);
const app = createApp(db);
app.post('/api/__probe', (c) => {
  const result = checkMutationBoundary({
    peer: getConnInfo(c).remote.address,
    trustedPeer: config.trustedCaddyIp,
    realIp: c.req.header('x-real-ip'),
    forwardedFor: c.req.header('x-forwarded-for'),
    origin: c.req.header('origin'),
    appOrigin: config.appOrigin,
  });
  if ('error' in result) return c.json(result, 403);
  return c.json(result);
});
serve({ fetch: app.fetch, hostname: '0.0.0.0', port: config.port });
console.log(`api_probe_listening:${config.port}`);
