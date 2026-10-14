import { serve } from '@hono/node-server';
import { createApp } from './app.js';
import { loadConfig } from './config.js';
import { createDbPool } from './db.js';

const config = loadConfig();
const db = createDbPool(config);

serve({ fetch: createApp(db).fetch, hostname: '0.0.0.0', port: config.port });
console.log(`api_listening:${config.port}`);
