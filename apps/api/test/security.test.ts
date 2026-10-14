import { describe, expect, it } from 'vitest';
import { checkMutationBoundary } from '../src/security.js';

const valid = {
  peer: '172.30.48.10', trustedPeer: '172.30.48.10',
  realIp: '127.0.0.1', forwardedFor: '127.0.0.1',
  origin: 'http://127.0.0.1:18080', appOrigin: 'http://127.0.0.1:18080',
};

describe('mutation boundary', () => {
  it('accepts one IP from configured Caddy peer and exact Origin', () => {
    expect(checkMutationBoundary(valid)).toEqual({ clientIp: '127.0.0.1' });
  });
  it.each([
    [{ peer: '172.30.48.9' }, 'untrusted_peer'],
    [{ realIp: undefined }, 'invalid_forwarded_ip'],
    [{ forwardedFor: '127.0.0.1, 10.0.0.1' }, 'invalid_forwarded_ip'],
    [{ realIp: 'not-ip' }, 'invalid_forwarded_ip'],
    [{ realIp: '10.0.0.1' }, 'invalid_forwarded_ip'],
    [{ origin: undefined }, 'invalid_origin'],
    [{ origin: 'http://evil.test' }, 'invalid_origin'],
  ] as const)('rejects %j', (change, error) => {
    expect(checkMutationBoundary({ ...valid, ...change })).toEqual({ error });
  });
});
