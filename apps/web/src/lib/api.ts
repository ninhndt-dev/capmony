export class ApiFailure extends Error {
  constructor(readonly code: string, readonly uncertain = false, readonly retryAfter?: string) {
    super(code);
  }
}

const uncertainRefreshKey = 'capmoney.manager.refresh-unknown';
const refreshMarkerStores = [() => window.localStorage, () => window.sessionStorage];
let uncertainRefreshInMemory = false;

export function refreshResultUnknown(): boolean {
  if (uncertainRefreshInMemory) return true;
  let storageAvailable = false;
  for (const getStorage of refreshMarkerStores) {
    try {
      const storage = getStorage();
      if (storage.getItem(uncertainRefreshKey) === '1') return true;
      storageAvailable = true;
    } catch { /* If storage is unavailable, auto refresh must fail closed. */ }
  }
  return !storageAvailable;
}

function markRefreshResultUnknown(): void {
  uncertainRefreshInMemory = true;
  for (const getStorage of refreshMarkerStores) {
    try { getStorage().setItem(uncertainRefreshKey, '1'); } catch { /* A second store may still work. */ }
  }
}

function clearRefreshResultUnknown(): void {
  uncertainRefreshInMemory = false;
  for (const getStorage of refreshMarkerStores) {
    try { getStorage().removeItem(uncertainRefreshKey); } catch { /* Auto refresh stays disabled if storage is unavailable. */ }
  }
}

function csrfCookie(): string | undefined {
  return document.cookie.split('; ').find((value) => value.startsWith('XSRF-TOKEN='))?.slice('XSRF-TOKEN='.length);
}

export async function renewCsrf(): Promise<string> {
  let response: Response;
  try {
    response = await fetch('/api/auth/csrf', { credentials: 'same-origin', cache: 'no-store', signal: AbortSignal.timeout(10000) });
  } catch {
    throw new ApiFailure('CSRF_UNAVAILABLE');
  }
  const cookie = csrfCookie();
  if (!response.ok || !cookie) throw new ApiFailure('CSRF_UNAVAILABLE');
  try { return decodeURIComponent(cookie); } catch { throw new ApiFailure('CSRF_UNAVAILABLE'); }
}

export async function postJson(path: string, body: unknown): Promise<void> {
  const csrf = await renewCsrf();
  let response: Response;
  try {
    response = await fetch(path, {
      method: 'POST', credentials: 'same-origin', cache: 'no-store', signal: AbortSignal.timeout(10000),
      headers: { 'Content-Type': 'application/json', 'X-XSRF-TOKEN': csrf }, body: JSON.stringify(body),
    });
  } catch {
    if (path === '/api/auth/refresh') markRefreshResultUnknown();
    throw new ApiFailure('RESULT_UNKNOWN', true);
  }
  if (!response.ok) {
    // A failed dependency response may follow an uncertain commit. Never promise it was not saved.
    if (response.status >= 500) {
      if (path === '/api/auth/refresh') markRefreshResultUnknown();
      throw new ApiFailure('RESULT_UNKNOWN', true);
    }
    let code = 'REQUEST_REJECTED';
    try {
      const data: unknown = await response.json();
      if (data && typeof data === 'object' && 'code' in data && typeof data.code === 'string') code = data.code;
    } catch { /* The HTTP rejection remains known even if its error body is unavailable. */ }
    throw new ApiFailure(code, false, response.headers.get('Retry-After') ?? undefined);
  }
  if (path === '/api/auth/refresh' || path === '/api/auth/login') clearRefreshResultUnknown();
  // Refresh after the transition; success is already known if this safe GET fails.
  try { await renewCsrf(); } catch { /* The next POST obtains a fresh token itself. */ }
}

export type ManagerLinkStatus = 'active' | 'expired' | 'retryable' | 'unavailable';

export async function managerLinkStatus(token: string): Promise<ManagerLinkStatus> {
  const csrf = await renewCsrf();
  let response: Response;
  try {
    response = await fetch('/api/manager/setup-link/status', {
      method: 'POST', credentials: 'same-origin', cache: 'no-store', signal: AbortSignal.timeout(10000),
      headers: { 'Content-Type': 'application/json', 'X-XSRF-TOKEN': csrf }, body: JSON.stringify({ token }),
    });
  } catch { throw new ApiFailure('RESULT_UNKNOWN', true); }
  if (!response.ok) {
    if (response.status === 429) throw new ApiFailure('RATE_LIMITED', false, response.headers.get('Retry-After') ?? undefined);
    throw new ApiFailure('STATUS_UNAVAILABLE', response.status >= 500);
  }
  const body: unknown = await response.json();
  if (!body || typeof body !== 'object' || !('status' in body) || typeof body.status !== 'string'
    || !['active', 'expired', 'retryable', 'unavailable'].includes(body.status)) {
    throw new ApiFailure('STATUS_UNAVAILABLE', true);
  }
  return body.status as ManagerLinkStatus;
}

export async function managerMe(): Promise<{ email: string; role: 'MANAGER' }> {
  let response: Response;
  try {
    response = await fetch('/api/manager/me', { credentials: 'same-origin', cache: 'no-store', signal: AbortSignal.timeout(10000) });
  } catch { throw new ApiFailure('RESULT_UNKNOWN', true); }
  if (response.status === 401) throw new ApiFailure('AUTH_REQUIRED');
  if (!response.ok) throw new ApiFailure('RESULT_UNKNOWN', true);
  const body: unknown = await response.json();
  if (!body || typeof body !== 'object' || !('email' in body) || typeof body.email !== 'string'
    || !('role' in body) || body.role !== 'MANAGER') throw new ApiFailure('RESULT_UNKNOWN', true);
  return { email: body.email, role: 'MANAGER' };
}
