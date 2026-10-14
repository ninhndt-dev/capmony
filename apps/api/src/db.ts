import { Pool } from 'pg';
import type { Config } from './config.js';

export function createDbPool(config: Config): Pool {
  return new Pool({
    host: config.dbHost,
    port: 5432,
    database: config.dbName,
    user: config.dbUser,
    password: config.dbPassword,
    connectionTimeoutMillis: 1500,
    statement_timeout: 1500,
    max: 4,
  });
}
