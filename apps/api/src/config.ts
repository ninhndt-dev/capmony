import { readFileSync } from 'node:fs';
import { isIP } from 'node:net';

function required(name: string): string {
  const value = process.env[name];
  if (!value) throw new Error(`missing_config:${name}`);
  return value;
}

export interface Config {
  port: number;
  appOrigin: string;
  trustedCaddyIp: string;
  dbHost: string;
  dbName: string;
  dbUser: string;
  dbPassword: string;
}

export function loadConfig(): Config {
  const appOrigin = required('APP_ORIGIN');
  const originUrl = new URL(appOrigin);
  if (originUrl.origin !== appOrigin || !['http:', 'https:'].includes(originUrl.protocol)) {
    throw new Error('invalid_config:APP_ORIGIN');
  }
  const trustedCaddyIp = required('CADDY_TRUSTED_IP');
  if (isIP(trustedCaddyIp) === 0) throw new Error('invalid_config:CADDY_TRUSTED_IP');
  const port = Number(required('PORT'));
  if (!Number.isInteger(port) || port < 1 || port > 65535) throw new Error('invalid_config:PORT');
  const dbPassword = readFileSync(required('DB_PASSWORD_FILE'), 'utf8').trimEnd();
  if (!dbPassword) throw new Error('invalid_config:DB_PASSWORD_FILE');
  return {
    port,
    appOrigin,
    trustedCaddyIp,
    dbHost: required('DB_HOST'),
    dbName: required('DB_NAME'),
    dbUser: required('DB_USER'),
    dbPassword,
  };
}
