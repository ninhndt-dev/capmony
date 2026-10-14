import { Hono } from 'hono';

export function createApp(db: { query(sql: string): Promise<unknown> }): Hono {
  const app = new Hono();
  app.get('/api/health', async (c) => {
    try {
      await db.query('SELECT 1');
      return c.json({ status: 'healthy' });
    } catch {
      return c.json({ status: 'degraded', error: 'db_unavailable' }, 503);
    }
  });
  return app;
}
