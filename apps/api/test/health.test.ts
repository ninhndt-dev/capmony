import { describe, expect, it } from 'vitest';
import { createApp } from '../src/app.js';

describe('API health', () => {
  it('reports healthy only when PostgreSQL responds', async () => {
    const app = createApp({ query: async () => ({ rows: [{ '?column?': 1 }] }) });
    const response = await app.request('/api/health');
    expect(response.status).toBe(200);
    expect(await response.json()).toEqual({ status: 'healthy' });
  });

  it('reports degraded without leaking PostgreSQL errors', async () => {
    const app = createApp({ query: async () => { throw new Error('secret database url'); } });
    const response = await app.request('/api/health');
    expect(response.status).toBe(503);
    expect(await response.json()).toEqual({ status: 'degraded', error: 'db_unavailable' });
  });
});
