import { isIP } from 'node:net';

export type SecurityFailure = 'untrusted_peer' | 'invalid_forwarded_ip' | 'invalid_origin';

function singleIp(value: string | undefined): value is string {
  return !!value && value.trim() === value && isIP(value) !== 0;
}

export function checkMutationBoundary(input: {
  peer: string | undefined;
  trustedPeer: string;
  realIp: string | undefined;
  forwardedFor: string | undefined;
  origin: string | undefined;
  appOrigin: string;
}): { clientIp: string } | { error: SecurityFailure } {
  if (input.peer !== input.trustedPeer) return { error: 'untrusted_peer' };
  if (!singleIp(input.realIp) || !singleIp(input.forwardedFor) || input.realIp !== input.forwardedFor) {
    return { error: 'invalid_forwarded_ip' };
  }
  if (input.origin !== input.appOrigin) return { error: 'invalid_origin' };
  return { clientIp: input.realIp };
}
